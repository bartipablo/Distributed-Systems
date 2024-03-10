package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class ReciveUDPMulticast extends Thread {

    private final String nick;
    private final MulticastSocket multicastSocket;
    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReciveUDPMulticast(MulticastSocket multicastSocket, String nick) {
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
                    // Sprawdź czy wiadomość nie pochodzi od samego siebie
                    if (!receivedMessage.startsWith(nick + ":")) {
                        System.out.println(receivedMessage);
                    }
                } catch (SocketTimeoutException e) {
                    // ignore exception
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
