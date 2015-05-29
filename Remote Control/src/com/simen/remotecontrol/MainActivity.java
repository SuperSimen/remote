package com.simen.remotecontrol;

import java.io.DataOutputStream;
import java.net.Socket;
import protocol.Protocol;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


public class MainActivity extends Activity {
	final String[] states = {"ready","sending"};
	String state = states[0];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.play_pause).setOnClickListener(new ClickListener(Protocol.PLAY_PAUSE));
		findViewById(R.id.next).setOnClickListener(new ClickListener(Protocol.NEXT));
		findViewById(R.id.previous).setOnClickListener(new ClickListener(Protocol.PREVIOUS));
		findViewById(R.id.jump_small_forward).setOnClickListener(new ClickListener(Protocol.JUMP_SMALL_FORWARD));
		findViewById(R.id.jump_small_backward).setOnClickListener(new ClickListener(Protocol.JUMP_SMALL_BACKWARD));
		findViewById(R.id.jump_medium_forward).setOnClickListener(new ClickListener(Protocol.JUMP_MEDIUM_FORWARD));
		findViewById(R.id.jump_medium_backward).setOnClickListener(new ClickListener(Protocol.JUMP_MEDIUM_BACKWARD));
		findViewById(R.id.jump_large_forward).setOnClickListener(new ClickListener(Protocol.JUMP_LARGE_FORWARD));
		findViewById(R.id.jump_large_backward).setOnClickListener(new ClickListener(Protocol.JUMP_LARGE_BACKWARD));
		findViewById(R.id.volume_up).setOnClickListener(new ClickListener(Protocol.VOLUME_UP));
		findViewById(R.id.volume_down).setOnClickListener(new ClickListener(Protocol.VOLUME_DOWN));
		findViewById(R.id.fullscreen).setOnClickListener(new ClickListener(Protocol.FULLSCREEN));

		showIPDialog();
	}


	IPDialog currentDialog;

	private void showIPDialog() {
		if (currentDialog != null) return;
		final Activity activity = this;

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				IPDialog dialog = new IPDialog(activity);
				currentDialog = dialog;
				dialog.show();
			}
		});
	}

	private void dismissIPDialog() {
		if (currentDialog != null) {
			currentDialog.dismiss();
			currentDialog = null;
		}
	}


	private class IPDialog extends Dialog{

		public IPDialog(Context context) {
			super(context);
			this.setContentView(R.layout.dialog_ip);
			setCanceledOnTouchOutside(false);

			final EditText input = (EditText) findViewById(R.id.ip_editable);
			String ip = getIP();
			input.setText(ip);
			input.setSelection(ip.length());
			final Button continueButton = (Button)findViewById(R.id.save_button);
			final ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar1);
			spinner.setVisibility(ProgressBar.INVISIBLE);

			continueButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					saveIp(input.getText().toString());
					(new Send(0)).start();
					spinner.setVisibility(ProgressBar.VISIBLE);
					input.setVisibility(EditText.INVISIBLE);
					continueButton.setVisibility(Button.INVISIBLE);
				}
			});
		}
	}

	private void saveIp(String ip) {
		SharedPreferences preferences = getPreferences(0);
		Editor edit = preferences.edit();
		edit.putString("ip", ip);
		edit.apply();
	}

	private String getIP() {
		SharedPreferences preferences = getPreferences(0);
		return preferences.getString("ip", "192.168.1.121");
	}

	private class ClickListener implements OnClickListener {
		int value;

		ClickListener(int value) {
			this.value = value;
		}

		@Override
		public void onClick(View v) {
			(new Send(value)).start();

		}

	}

	private class Send extends Thread {
		int message;
		Send(int message) {
			this.message = message;
		}

		@Override
		public void run() {
			if (state == states[0]) {
				state = states[1];
				try {
					Socket socket = null;
					socket = new Socket(getIP(),4568);
					DataOutputStream writer = new DataOutputStream(socket.getOutputStream());				
					writer.writeInt(message);
					socket.close();
					dismissIPDialog();

				} catch (Exception e) {
					Log.e("Feil", "Feil", e);
					dismissIPDialog();
					showIPDialog();
				}
				state = states[0];
			}
		}
	}
}
