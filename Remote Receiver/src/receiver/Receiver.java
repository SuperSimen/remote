 package receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import protocol.Protocol;

public class Receiver {
	Protocol protocol = new Protocol();
	ArrayList<String> whitelist = new ArrayList<String>(), blacklist = new ArrayList<String>();

	public Receiver() throws IOException {
		if (!readWhitelistFromFile()) return;
		ServerSocket serverSocket = new ServerSocket(4568);
		
		while (true) {
			Socket socket;
			DataInputStream reader;
			
			try {
				socket = serverSocket.accept();				
				if (!validIP(socket.getInetAddress().toString())) continue;

				reader = new DataInputStream(socket.getInputStream());
				protocol.receiveCommand(reader.readInt());
				socket.close();
				
			} catch (IOException e) { 
				e.printStackTrace();
				break;
			}
		}
		
	}
	
	private boolean readWhitelistFromFile() {
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader("whitelist.txt"));
			
			String line;
			while ((line = reader.readLine()) != null) {
				whitelist.add(line);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void addToWhiteList(String ip) {
	    try {
			BufferedWriter out = new BufferedWriter(new FileWriter("whitelist.txt", true));
			out.write(ip + "\n");
			out.close();
			whitelist.add(ip);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private boolean validIP(String ip) {
		if (whitelist.contains(ip)) return true;
		if (blacklist.contains(ip)) return false;
		
		int result = JOptionPane.showConfirmDialog(null, "Accept IP: " + ip + "?", "Accept ip?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == 0) {
			addToWhiteList(ip);
		}
		else if (result == 1){
			blacklist.add(ip);
		}
		return false;
	}
	
	public static void main(String[] args) {
		try {
			new Receiver();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}