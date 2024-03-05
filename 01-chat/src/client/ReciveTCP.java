package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReciveTCP extends Thread {

    private final Socket socketTCP;

    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReciveTCP(Socket socketTCP) {
        this.socketTCP = socketTCP;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream(), "UTF-8"));
            String message;
            while (!exit && ((message = in.readLine()) != null)) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
