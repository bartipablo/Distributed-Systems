package com.bartipablo;

import com.bartipablo.threads.HandlerTCP;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Connections {

    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    private final ServerSocket serverSocket = new ServerSocket(Config.SERVER_PORT);

    public Connections() throws IOException {
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void addConnection(Client client) {
        clients.put(client.getNick(), client);
    }

    public void removeConnection(String nick) {
        Client client = clients.remove(nick);
        client.closeResources();
        System.out.println("Client " + nick + " disconnected");
    }

    public void removeAllConnections() {
        for (Client client : clients.values()) {
            client.closeResources();
            System.out.println("Client " + client.getNick() + " disconnected");
        }
        clients.clear();
    }

    public boolean userAlreadyExist(String nick) {
        Set<String> clientsNick = clients.keySet();
        return clientsNick.contains(nick);
    }

    public Set<String> getConnectedClientsNick() {
        return clients.keySet();
    }

    public List<Client> getConnectedClients() {
        return List.copyOf(clients.values());
    }

    public Client getClient(String nick) {
        return clients.get(nick);
    }

    public List<HandlerTCP> getHandlerTCPs() {
        return List.copyOf(clients.values().stream().map(Client::getHandlerTCP).toList());
    }

}
