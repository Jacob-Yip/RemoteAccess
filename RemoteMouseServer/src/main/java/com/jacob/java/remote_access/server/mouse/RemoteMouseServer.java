package com.jacob.java.remote_access.server.mouse;

import java.awt.AWTException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jacob.java.remote_access.server.mouse.entity.ClientHandler;

public class RemoteMouseServer {
	// Constants
	private final int PORT = 5003;
	private final int MAXIMUM_WAIT_TIME = 10000000;
	private final ServerSocket serverSocket;
	
	List<ClientHandler> clientHandlers;
	
	// For brute exit
	private JFrame frame;
	
	
	public RemoteMouseServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
		serverSocket.setSoTimeout(MAXIMUM_WAIT_TIME);
		
		clientHandlers = new ArrayList<ClientHandler>();
		
		frame = new JFrame();
		frame.setSize(100, 100);    // Set size of frame
//		frame.setUndecorated(true);    // Hide window decorations
		frame.setFocusable(true);    // Allow it to receive key events
		
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				char keyChar = event.getKeyChar();
				if(keyChar == 'q') {
					System.out.println("Exiting programme brutally ...");
					
					// Close connections
					for(ClientHandler c: clientHandlers) {
						try {
							c.close();
						} catch(Exception e) {
							System.err.println("Error in closing socket connection brutally: " + e.getMessage());
						}
					}
					
					System.exit(keyChar);
				}
			}
		});
		
		frame.setVisible(true);
	}
	
	public void execute() {
		System.out.println("Server waiting for connection ...");
		
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				System.out.println("Connected to " + socket.getRemoteSocketAddress());
				
				ClientHandler clientHandler = new ClientHandler(socket, dis, dos);
				clientHandlers.add(clientHandler);    // For brutal exit
				clientHandler.start();
			} catch (IOException e) {
				System.err.println("Error in connecting to a client socket: " + e.getMessage());
			} catch (AWTException e) {
				System.err.println("Error in connecting to a client socket: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			RemoteMouseServer server = new RemoteMouseServer();
			
			server.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
