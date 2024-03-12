package com.bartipablo;

import com.bartipablo.threads.HandlerTCP;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;

public class Client {

    private final String nick;

    private final String address;

    private final int port;

    private final Socket socket;

    private final BufferedReader reader;

    private final PrintWriter writer;

    private final DatagramSocket datagramSocket;

    private final HandlerTCP handlerTCP;

    public Client(String nick, String address, int port, Socket socket, BufferedReader reader, PrintWriter writer, DatagramSocket datagramSocket, HandlerTCP handlerTCP) {
        this.nick = nick;
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.datagramSocket = datagramSocket;
        this.handlerTCP = handlerTCP;
    }

    public void closeResources() {
        try {
            socket.close();
            datagramSocket.close();
            reader.close();
            writer.close();
        } catch (Exception e) {
            System.err.println("Error while closing resources in client: " + nick);
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public HandlerTCP getHandlerTCP() {
        return handlerTCP;
    }
}
