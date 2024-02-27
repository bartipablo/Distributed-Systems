package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerUDPHandler {
    private final List<ClientInfo> clientsInfo = new ArrayList<>();

    public void addClientInfo(ClientInfo clientInfo) {
        clientsInfo.add(clientInfo);
    }

    public void removeClientInfo(String nick) {
        for (ClientInfo clientInfo : clientsInfo) {
            if (clientInfo.getNick().equals(nick)) {
                clientsInfo.remove(clientInfo);
                return;
            }
        }
    }

    public void broadcast(String senderNick, String message) {
        try {
            for (ClientInfo clientInfo : clientsInfo) {
                String nick = clientInfo.getNick();
                String address = clientInfo.getAddress();
                int port = clientInfo.getPort();

                if (!nick.equals(senderNick)) {
                    sendUDPPacket(address, port, senderNick + ": " + message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUDPPacket(String address, int port, String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(address),
                    port);
            socket.send(sendPacket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
