package com.jacob.java.remote_access.server.mouse.simple;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * Button1 is the left-click
 * Button2 is the scroll
 * Button3 is the right-click
 */
public class SimpleRemoteMouseRobot {
	public static void main(String[] args) {
		System.out.println("mouse button num: " + MouseInfo.getNumberOfButtons());
		
		Robot robot;
		try {
			robot = new Robot();
			
			System.out.println("Please move your mouse to a safer place!!! ");
			Thread.sleep(4000);
			
			System.out.println("Pressing button 1");
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			Thread.sleep(2000);
			
			System.out.println("Pressing button 2");
			robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
			Thread.sleep(2000);
			
			System.out.println("Pressing button 3");
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
			Thread.sleep(2000);
			
			System.out.println("Scrolling mouse to go up");
			robot.mouseWheel(-1);
			Thread.sleep(2000);
			
			System.out.println("Scrolling mouse to go down");
			robot.mouseWheel(1);
			Thread.sleep(2000);
			
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Programme finished");
	}
}
