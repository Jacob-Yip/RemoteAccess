package com.jacob.java.remote_access.server.mouse.simple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * inputStream.readUTF() is a blocking statement, meaning that the number of times this function is invoked matter
 */
public class SimpleRemoteMouseServer extends Thread {
	// Constants
	private final int PORT = 5003;
	private final int MAXIMUM_WAIT_TIME = 10000000;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private String inputPacket = "";
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public SimpleRemoteMouseServer() throws IOException {
		// Initialise
		serverSocket = new ServerSocket(PORT);
		serverSocket.setSoTimeout(MAXIMUM_WAIT_TIME);
	}
	
	@Override
	public void run() {
		while(socket == null) {
			try {
				System.out.println("Waiting for client connection: " + serverSocket.getLocalSocketAddress() + " -> port: " + serverSocket.getLocalPort());
				socket = serverSocket.accept();
				System.out.println("Connected to " + socket.getRemoteSocketAddress());
				inputStream = new DataInputStream(socket.getInputStream());
				outputStream = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		String clientMessage = "";
		while(!clientMessage.equalsIgnoreCase("exit")) {
			try {
				clientMessage = inputStream.readUTF();
				
				System.out.println("Client message: " + clientMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Server terminated ...");
	}
	
	public static void main(String[] args) {
		SimpleRemoteMouseServer server;
		
		try {
			server = new SimpleRemoteMouseServer();
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
