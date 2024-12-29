package com.jacob.java.remote_access.client.simple;

import com.jacob.java.remote_access.client.entity.Client;
import com.jacob.java.remote_access.client.entity.RemoteAccessPacket;

import java.io.IOException;

public class ClientTester {
//    public static final String SERVER_IP = "10.205.241.1" ;    // Mac
//    public static final String SERVER_IP = "10.0.2.2" ;    // Localhost (Android)
    public static final String SERVER_IP = "127.0.0.1" ;    // Localhost
    public static final int PORT = 5003;

    public static void main(String[] args) {
        // Create RemoteAccessPacket
        RemoteAccessPacket packet = new RemoteAccessPacket(false, false, false, 0, 0, 0, false);

        try {
            System.out.println("Initiate Client");
            Client client = new Client(SERVER_IP, PORT, packet);

            System.out.println("Send packet");
            client.sendPacket();
        } catch(IOException e) {
            e.printStackTrace();
        }


    }
}
