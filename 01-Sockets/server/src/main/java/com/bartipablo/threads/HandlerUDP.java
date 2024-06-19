package com.bartipablo.threads;

import com.bartipablo.Communication;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class HandlerUDP extends Thread {

    private final Communication communications;

    private final DatagramSocket serverSocketUDP;

    private boolean exit = false;

    public HandlerUDP(Communication communications, DatagramSocket serverSocketUDP) {
        this.communications = communications;
        this.serverSocketUDP = serverSocketUDP;
    }

    public void shutdown() {
        exit = true;
    }

    @Override
    public void run() {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        while (!exit) {
            try {
                serverSocketUDP.setSoTimeout(200);
                serverSocketUDP.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                String[] parts = message.split(" ", 3);
                String senderNick = parts[1];
                String actualMessage = parts[2];

                communications.broadcastUPD(senderNick, actualMessage);
            } catch (SocketTimeoutException e) {
                // ignore exception
            } catch (Exception e) {
                if (!exit) {
                    System.out.println("Error while receiving UDP message");
                    e.printStackTrace();
                }
            }
        }
    }

}
