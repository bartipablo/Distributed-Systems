package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientUDPThread implements Runnable {

    private final int BUFFER_SIZE = 1024;

    private final ServerTCPHandler serverTCPHandler;

    private final DatagramSocket serverSocketUDP;

    public ClientUDPThread(ServerTCPHandler serverTCPHandler, DatagramSocket serverSocketUDP) {
        this.serverTCPHandler = serverTCPHandler;
        this.serverSocketUDP = serverSocketUDP;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[BUFFER_SIZE];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                serverSocketUDP.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] parts = receivedMessage.split(" ", 3);
            String messageType = parts[0];
            String nick = parts[1];
            String actualMessage = parts[2];

            serverTCPHandler.broadcast(nick, actualMessage);
        }
    }

}
