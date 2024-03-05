package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ReciveUDP extends Thread {

    private final DatagramSocket socketUDP;

    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReciveUDP(DatagramSocket socketUDP) {
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
            e.printStackTrace();
        }
    }
}
