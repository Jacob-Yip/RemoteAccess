package com.jacob.java.remote_access.client.simple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SimpleRemoteMouseClient extends Thread {
    private Socket socket = null;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final int PORT = 5003;
    private final String serverIP = "0.0.0.0";


    public SimpleRemoteMouseClient() {

    }

    @Override
    public void run() {
        Scanner scan = null;
        try {
            InetAddress serverAddress = InetAddress.getByName(serverIP);

            socket = new Socket(serverAddress, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Client connected");

            // Send something to the server
            scan = new Scanner(System.in);
            String clientMessage = "";

            while(!clientMessage.equalsIgnoreCase("exit")) {
                clientMessage = scan.nextLine();

                outputStream.writeUTF(clientMessage);
            }

            System.out.println("Client terminated ...");
        } catch(UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(scan != null) {
                scan.close();
            }
        }
    }

    public static void main(String[] args) {
        SimpleRemoteMouseClient client = new SimpleRemoteMouseClient();

        client.start();
    }
}