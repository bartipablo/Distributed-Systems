package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ReciveUDPMulticast implements Runnable {
    
    private final MulticastSocket multicastSocket;


    public ReciveUDPMulticast(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }


    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                multicastSocket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(),
                     "UTF-8");
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

