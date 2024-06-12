package org.example;

import org.example.Application;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: Main <zooKeeperHost> <zNodePath> <command>");
            System.exit(1);
        }

        String zooKeeperHost = args[0];
        String zNodePath = args[1];
        String command = args[2];

        var config = new ZooKeeperConfiguration(zooKeeperHost, zNodePath);

        var monitorWindow = new MonitorWindow();
        var zooKeeperClientWatcher = new ZooKeeperClientWatcher(config);
        var application = new Application(command);

        var controller = new Controller(monitorWindow, zooKeeperClientWatcher, application);

        monitorWindow.setController(controller);
        zooKeeperClientWatcher.setController(controller);

        zooKeeperClientWatcher.start();

        while (true) {}
    }
}
