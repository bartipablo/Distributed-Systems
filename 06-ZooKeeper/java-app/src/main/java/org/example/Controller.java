package org.example;

import java.util.List;

/**
 * Controller class manages the interaction between the user interface (MonitorWindow),
 * the ZooKeeper client watcher (ZooKeeperClientWatcher), and the external application logic (Application).
 * It handles node creation and deletion events, displays information on the UI, and manages
 * the lifecycle of the application.
 */
public class Controller {

    private final MonitorWindow monitorWindow;
    private final ZooKeeperClientWatcher zooKeeperClientWatcher;
    private final Application application;

    Controller(MonitorWindow monitorWindow, ZooKeeperClientWatcher zooKeeperClientWatcher, Application application) {
        this.monitorWindow = monitorWindow;
        this.zooKeeperClientWatcher = zooKeeperClientWatcher;
        this.application = application;
    }

    public void createdNodeHandler() {
        monitorWindow.show();
        application.start();
    }

    public void deletedNodeHandler() {
        monitorWindow.hide();
        application.shutdown();
    }

    public void displayChildrenQuantity() {
        int quantity = zooKeeperClientWatcher.getTreeChildrenQuantity();
        display("Number of children: " + quantity + "\n");
    }

    public void displayTreeStructure() {
        List<String> children = zooKeeperClientWatcher.getTreeStructure();
        StringBuilder treeStructure = new StringBuilder();
        for (String child : children) {
            treeStructure.append(child).append("\n");
        }
        display("Tree structure:\n" + treeStructure);
    }

    private void display(String content) {
        monitorWindow.appendContent(content);
    }

}
