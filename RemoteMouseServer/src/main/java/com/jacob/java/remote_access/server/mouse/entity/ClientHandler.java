package com.jacob.java.remote_access.server.mouse.entity;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
	private final Socket socket;
	private final DataInputStream dis;
	private final DataOutputStream dos;
	
	private final Robot agent;
	private static final int MOVE_SMOOTHNESS = 100;    // How smooth we want the agent to move the pointer/mouse; The higher the value, the smoother the motion
	
	public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) throws AWTException {
		this.socket = socket;
		this.dis = dis;
		this.dos = dos;
		
		this.agent = new Robot();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String packetString = dis.readUTF();
				RemoteAccessPacket packet = RemoteAccessPacket.parseString(packetString);
				
				if(packet.isExit()) {
					break;
				}
				
				// Update mouse
				updateMouse(packet);
			} catch (IOException e) {
				System.err.println("Error in handling packet: " + e.getMessage());
				
				close();
				
				return;
			}
		}
	}
	
	private void updateMouse(RemoteAccessPacket packet) {
		System.out.println("=============");
		System.out.println(packet);
		System.out.println("=============");
		
		if(agent == null) {
			System.err.println("Robot is null in server");
			return;
		}
		
		// Button 1 (Left)
		if(packet.isButton1Down()) {
//			System.out.println("Left Down");
			agent.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		} else {
//			System.out.println("Left Up");
			agent.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
		
		// Button 2 (Right)
		if(packet.isButton2Down()) {
//			System.out.println("Right Down");
			agent.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		} else {
//			System.out.println("Right Up");
			agent.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		}
		
		// Button 3 (Scroll)
		if(packet.isButton3Down()) {
//			System.out.println("Button 3 Down");
			agent.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		} else {
//			System.out.println("Button 3 Up");
			agent.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}
		
		
		// Scroll
//		System.out.println("Scroll (positive: Up; negative: Down): " + packet.getScroll());
		agent.mouseWheel(packet.getScroll());
		
		// Coordinates
		int currentX = MouseInfo.getPointerInfo().getLocation().x;
		int currentY = MouseInfo.getPointerInfo().getLocation().y;

		
		if(packet.getMouseCoordinates().getDx() != 0 || packet.getMouseCoordinates().getDy() != 0) {
			for(int i = 0; i < MOVE_SMOOTHNESS; i++) {
//				System.out.println("Moving mouse: (" + (int) (currentX + (packet.getMouseCoordinates().getDx() / MOVE_SMOOTHNESS)) + ", " + (int) (currentY + (packet.getMouseCoordinates().getDy() / MOVE_SMOOTHNESS)) + ")");
				agent.mouseMove((int) (currentX + (packet.getMouseCoordinates().getDx() / MOVE_SMOOTHNESS)), (int) (currentY + (packet.getMouseCoordinates().getDy() / MOVE_SMOOTHNESS)));
			}
		}
		
//		System.out.println("============= End of Packet =================");
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @return the dis
	 */
	public DataInputStream getDis() {
		return dis;
	}

	/**
	 * @return the dos
	 */
	public DataOutputStream getDos() {
		return dos;
	}
	
	public void close() {
		try {
			dis.close();
		} catch(IOException e) {
			System.out.println("try close dis: " + e.getMessage());
		}
		
		try {
			dos.close();
		} catch(IOException e) {
			System.out.println("try close dos: " + e.getMessage());
		}
		
		try {
			socket.close();
		} catch(IOException e) {
			System.out.println("try close socket: " + e.getMessage());
		}
	}
}
