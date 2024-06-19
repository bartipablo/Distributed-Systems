package com.bartipablo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ReceiveUDP extends Thread {

    private final DatagramSocket socketUDP;

    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReceiveUDP(DatagramSocket socketUDP) {
        this.socketUDP = socketUDP;
    }

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (!exit) {
                try {
                    socketUDP.setSoTimeout(500);
                    socketUDP.receive(receivePacket);
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                    System.out.println(receivedMessage);
                } catch (SocketTimeoutException e) {
                    // ignore exception
                }
            }
        } catch (IOException e) {
            if (!exit) {
                System.out.println("Error while receiving UDP message");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
