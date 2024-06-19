package com.bartipablo;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class ReceiveUDPMulticast extends Thread {

    private final String nick;
    private final MulticastSocket multicastSocket;
    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReceiveUDPMulticast(MulticastSocket multicastSocket, String nick) {
        this.multicastSocket = multicastSocket;
        this.nick = nick;
    }

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (!exit) {
                multicastSocket.setSoTimeout(500);
                try {
                    multicastSocket.receive(receivePacket);
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                    if (!receivedMessage.startsWith(nick + ":")) {
                        System.out.println(receivedMessage);
                    }
                } catch (SocketTimeoutException e) {
                    // ignore exception
                }
            }
        } catch (IOException e) {
            if (!exit) {
                System.out.println("Error while receiving UDP multicast message");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
