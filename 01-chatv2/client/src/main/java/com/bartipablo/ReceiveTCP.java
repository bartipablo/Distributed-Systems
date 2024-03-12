package com.bartipablo;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveTCP extends Thread {

    private final BufferedReader in;

    private boolean exit = false;

    public void shutdown() {
        exit = true;
    }

    public ReceiveTCP(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while (!exit && ((message = in.readLine()) != null)) {
                System.out.println(message);
            }
            if (!exit) System.out.println("The connection to the server has been terminated.");
            System.exit(0);
        } catch (IOException e) {
            if (!exit) {
                System.out.println("Error while receiving TCP message");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }
}
