package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReciveUDP implements Runnable {
    
    private final DatagramSocket socketUDP;

    
    public ReciveUDP(DatagramSocket socketUDP) {
        this.socketUDP = socketUDP;
    }

    
    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                socketUDP.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(),
                                "UTF-8");
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
