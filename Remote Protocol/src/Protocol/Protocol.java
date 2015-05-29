package protocol;

import java.awt.AWTException;
import java.awt.Robot;


public class Protocol {
	public static final int PLAY_PAUSE = 1, NEXT = 2, PREVIOUS = 3,
			JUMP_SMALL_FORWARD = 4, JUMP_SMALL_BACKWARD = 5,
			JUMP_MEDIUM_FORWARD = 6, JUMP_MEDIUM_BACKWARD = 7,
			JUMP_LARGE_FORWARD = 8, JUMP_LARGE_BACKWARD = 9, 
			VOLUME_UP = 10, VOLUME_DOWN = 11,
			FULLSCREEN = 12,
			CONFIRM = 13;

	private Robot robot;

	public Protocol() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receiveCommand(int input) {
		System.out.println(input);
		switch (input) {
		case PLAY_PAUSE:
			pressKey(32);
			break;
		case NEXT:
			pressKey(34);
			break;
		case PREVIOUS:
			pressKey(33);
			break;

		case JUMP_SMALL_FORWARD:
			pressKey(18,39);
			break;
		case JUMP_SMALL_BACKWARD:
			pressKey(18,37);
			break;

		case JUMP_MEDIUM_FORWARD:
			pressKey(17,39);
			break;
		case JUMP_MEDIUM_BACKWARD:
			pressKey(17,37);
			break;

		case JUMP_LARGE_FORWARD:
			pressKey(18,17,39);
			break;
		case JUMP_LARGE_BACKWARD:
			pressKey(18,17,37);
			break;

		case VOLUME_UP:
			pressKey(38);
			break;
		case VOLUME_DOWN:
			pressKey(40);
			break;
		
		case FULLSCREEN:
			pressKey(18,10);
		
		}
	}

	private void pressKey(int key) {
		robot.keyPress(key);
		robot.keyRelease(key);
	}

	private void pressKey(int key1, int key2) {
		robot.keyPress(key1);
		robot.keyPress(key2);
		robot.keyRelease(key2);
		robot.keyRelease(key1);
	}
	
	private void pressKey(int key1, int key2, int key3) {
		robot.keyPress(key1);
		robot.keyPress(key2);
		robot.keyPress(key3);
		robot.keyRelease(key3);
		robot.keyRelease(key2);
		robot.keyRelease(key1);
		
	}


}