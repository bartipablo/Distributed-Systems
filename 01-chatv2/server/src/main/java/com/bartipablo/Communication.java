package com.bartipablo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communication {

    private final Connections connections;

    public Communication(Connections connections) {
        this.connections = connections;
    }

    public void broadcastTCP(String senderNick, String message) {
        var clients = connections.getConnectedClients();
        for (Client client : clients) {
            if (client.getNick().equals(senderNick)) {
                continue;
            }
            client.getWriter().println(senderNick + ": " + message);
        }
    }

    public void broadcastUPD(String senderNick, String message) {
        var clients = connections.getConnectedClients();
        for (Client client : clients) {
            if (client.getNick().equals(senderNick)) {
                continue;
            }
            String address = client.getAddress();
            int port = client.getPort();

            try {
                DatagramSocket socket = client.getDatagramSocket();
                String messageToSent = senderNick + ": " + message;
                byte[] sendData = messageToSent.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData,
                        sendData.length,
                        InetAddress.getByName(address),
                        port
                );
                socket.send(sendPacket);
            } catch (Exception e) {
                System.out.println("Error while sending UDP message to: " + client.getNick());
                e.printStackTrace();
            }
        }
    }


}
