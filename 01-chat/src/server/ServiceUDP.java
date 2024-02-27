package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServiceUDP implements Runnable {

    private final int BUFFER_SIZE = 1024;

    private final DatagramSocket serverSocketUDP;

    private final ServerUDPHandler serverUDPHandler;

    public ServiceUDP(DatagramSocket serverSocketUDP, ServerUDPHandler serverUDPHandler) {
        this.serverSocketUDP = serverSocketUDP;
        this.serverUDPHandler = serverUDPHandler;
    }

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[BUFFER_SIZE];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocketUDP.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                String[] parts = message.split(" ", 3);
                String nick = parts[1];
                String actualMessage = parts[2];

                serverUDPHandler.broadcast(nick, actualMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
