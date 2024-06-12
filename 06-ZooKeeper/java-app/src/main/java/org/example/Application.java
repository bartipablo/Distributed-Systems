package org.example;

import java.io.IOException;

/**
 * The Application class provides a way to manage an external application process.
 * It allows you to start and stop the external application using the specified command.
 */
public class Application {

    private final String command;
    private Process process;

    /**
     * Constructs an Application instance with the specified command.
     *
     * @param command The command to run the external application. This should include the full
     *                path to the executable and any necessary arguments.
     */
    public Application(String command) {
        this.command = command;
    }

    public void start() {
        if (process != null && process.isAlive()) {
            return;
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            System.out.println("Error while running external application");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (process != null) {
            process.destroy();
        }
    }

}