package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class Client {

    private static String nick;

    private static final String HOST_NAME = "localhost";

    private static final String MULTICAST_ADDRESS = "239.0.0.1";

    private static final int SERVER_PORT_NUMBER = 12345;

    private static final int CLIENT_MULTICAST_PORT_NUMBER = 12346;

    public static void main(String[] args) throws IOException {

        readInputArguments(args);

        Socket socketTCP = null;
        DatagramSocket socketUDP = null;
        MulticastSocket multicastSocket = null;

        try {

            // create TCP socket
            socketTCP = new Socket(HOST_NAME, SERVER_PORT_NUMBER);


            // create UDP socket   
            socketUDP = new DatagramSocket();


            // create UDP multicast socket
            multicastSocket = new MulticastSocket(CLIENT_MULTICAST_PORT_NUMBER);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);


            // create thread to receive UDP multicast messages
            Thread receiveMulticastThread = new Thread(new ReciveUDPMulticast(multicastSocket));
            receiveMulticastThread.start();


            // create thread to receive UDP messages
            Thread receiveUDPThread = new Thread(new ReciveUDP(socketUDP));
            receiveUDPThread.start();


            // create thread to recive TCP messages
            Thread receiveTCPThread = new Thread(new ReciveTCP(socketTCP));
            receiveTCPThread.start();


            
            // chat API
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socketTCP.getOutputStream(), "UTF-8"), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            out.println(
                nick + " " + InetAddress.getLocalHost().getHostAddress() + " " + socketUDP.getLocalPort());

            String input;
            while ((input = userInput.readLine()) != null) {
                
                if (input.startsWith("/quit")) {
                    out.println("[Q] " + nick + " X");
                    break;
                } 

                else if (input.startsWith("/U ")) {
                    String messageToSend = input.substring(3);

                    byte[] sendData = ("[UDP] " + nick + " " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName(HOST_NAME), SERVER_PORT_NUMBER);
                    socketUDP.send(sendPacket);
                } 

                else if (input.startsWith("/M")) {
                    String messageToSend = input.substring(3);

                    byte[] sendData = (nick + ": " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName(MULTICAST_ADDRESS), CLIENT_MULTICAST_PORT_NUMBER);
                    multicastSocket.send(sendPacket);
                    } 
                    
                else {
                    out.println("[TCP] " + nick + " " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         finally {
            if (socketUDP != null) {
                socketUDP.close();
            }
            if (multicastSocket != null) {
                multicastSocket.close();
            }
            if (socketTCP != null) {
                socketTCP.close();
            }
        }
    }

    private static void readInputArguments(String[] args) {
        // sign nick
        if (args.length < 1) {
            System.err.println("Usage: java ClientTCP <nick>");
            System.exit(1);
        }

        nick = args[0].trim();

        if (nick.isEmpty()) {
            System.err.println("Nick cannot be empty.");
            System.exit(1);
        }
    }

}
