package com.jacob.java.remote_access.client.entity;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final int PORT;
    private final String serverIP;
    private RemoteAccessPacket packet;

//    private static final int MAX_THREAD = 2;    // Maximum number of threads a single client application can use
//    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD);


    /**
     * No need to close the stream and connections even if an exception is thrown
     *
     * @param serverIP
     * @param PORT
     * @param packet
     * @throws UnknownHostException
     * @throws IOException
     */
    public Client(final String serverIP, final int PORT, RemoteAccessPacket packet) throws UnknownHostException, IOException {
        this.serverIP = serverIP;
        this.PORT = PORT;

        InetAddress serverAddress = InetAddress.getByName(serverIP);

        // Establish socket connection
        final boolean[] successfulConnection = {true};
        try {
            socket = new Socket(serverAddress, PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            close();

            successfulConnection[0] = false;

            System.err.println("Error in establishing connection: " + e.getMessage());
        }

        this.packet = packet;

        if(!successfulConnection[0]) {
            throw new IOException("Error in establishing socket connection");
        }
    }

    /**
     * Send data packet to server
     */
    public void sendPacket() {
        if(socket == null) {
            System.out.println("socket == null (skipped sendPacket())");
            return;
        }

        if(dos == null) {
            System.out.println("dos == null (skipped sendPacket())");
            return;
        }

        try {
            dos.writeUTF(packet.toString());
        } catch (IOException e) {
            System.err.println("Error in sendPacket(): " + e.getMessage());
        }
    }

    public void close() {
        try {
            dis.close();
        } catch (Exception e) {
            System.out.println("try close dis: " + e.getMessage());
        }

        try {
            dos.close();
        } catch (Exception e) {
            System.out.println("try close dos: " + e.getMessage());
        }

        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("try close socket: " + e.getMessage());
        }
    }

}
