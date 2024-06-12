package org.example;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ZooKeeperClientWatcher class manages interactions with ZooKeeper server,
 * monitors changes in ZooKeeper nodes, and notifies a Controller about these changes.
 */
public class ZooKeeperClientWatcher {

    private Controller controller;
    private ZooKeeper zookeeper;
    private final ZooKeeperConfiguration config;

    /**
     * Constructs a ZooKeeperClientWatcher object with the specified ZooKeeper configuration.
     *
     * @param config The configuration settings for connecting to ZooKeeper.
     */
    ZooKeeperClientWatcher(ZooKeeperConfiguration config) {
        this.config = config;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        try {
            zookeeper = new ZooKeeper(config.zooKeeperHost(), 15000, null);
            setZNodeWatcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the hierarchical structure of ZooKeeper nodes starting from the root node.
     *
     * @return A list of strings representing the tree structure of nodes.
     */
    public List<String> getTreeStructure() {
        return getTreeStructure(config.zNodePath());
    }

    /**
     * Recursively retrieves the hierarchical structure of ZooKeeper nodes from the specified path.
     *
     * @param path The path of the node to start retrieving the structure.
     * @return A list of strings representing the tree structure of nodes.
     */
    private List<String> getTreeStructure(String path) {
        List<String> treeStructure = new LinkedList<>();
        treeStructure.add(path);

        List<String> children;

        try {
            children = zookeeper.getChildren(path, false);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return treeStructure;
        }

        for (String child : children) {
            String childPath = path + "/" + child;
            treeStructure.addAll(getTreeStructure(childPath));
        }

        return treeStructure;
    }

    /**
     * Retrieves the total number of children nodes in the ZooKeeper tree starting from the root node.
     *
     * @return The total number of children nodes.
     */
    public int getTreeChildrenQuantity() {
        return getTreeChildrenQuantity(config.zNodePath());
    }

    /**
     * Recursively calculates the total number of children nodes starting from the specified path.
     *
     * @param path The path of the node to start calculating the children quantity.
     * @return The total number of children nodes.
     */
    private int getTreeChildrenQuantity(String path) {
        List<String> children;
        try {
            children = zookeeper.getChildren(path, false);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
        int childrenQuantity = children.size();
        for (String child : children) {
            String childPath = path + "/" + child;
            childrenQuantity += getTreeChildrenQuantity(childPath);
        }
        return childrenQuantity;
    }

    /**
     * Sets a watcher on the specified ZooKeeper node to monitor for changes (create or delete events).
     * Using only for the root node.
     * If the node is created, it sets a watcher on its children nodes.
     * This method is called recursively to monitor changes in the root node.
     */
    private void setZNodeWatcher() {
        try {
            zookeeper.exists(config.zNodePath(), event -> {
                switch (event.getType()) {
                    case NodeCreated -> {
                        setZNodeChildrenWatcher(config.zNodePath(), new LinkedList<>());
                        controller.createdNodeHandler();
                    }
                    case NodeDeleted -> controller.deletedNodeHandler();
                }
                setZNodeWatcher();
            });
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a watcher on the children of the specified ZooKeeper node to monitor for changes.
     * This method is called recursively to set watchers on all children nodes.
     *
     * @param path             The path of the node to monitor its children.
     * @param previousChildren A list of previous children nodes to compare with.
     */
    private void setZNodeChildrenWatcher(String path, List<String> previousChildren) {
        try {
            zookeeper.getChildren(path, event -> {
                switch (event.getType()) {
                    case NodeChildrenChanged -> nodeChildrenChangedHandler(path, previousChildren);
                    case NodeDeleted -> {
                        return;
                    }
                }
                setZNodeChildrenWatcher(path, previousChildren);
            });
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when children nodes of a ZooKeeper node change.
     * This method is called when a new child node is created or an existing child node is deleted.
     * It updates the list of previous children nodes and sets a watcher on the new child node.
     *
     * @param path             The path of the node whose children have changed.
     * @param previousChildren A list of previous children nodes.
     */
    private void nodeChildrenChangedHandler(String path, List<String> previousChildren) {
        try {
            List<String> actualChildren = zookeeper.getChildren(path, false);

            // Check if a new child node is created
            if (actualChildren != null && actualChildren.size() > previousChildren.size()) {
                Set<String> newChildren = new HashSet<>(actualChildren);
                newChildren.removeAll(previousChildren);
                for (String child : newChildren) {
                    previousChildren.add(child);
                    setZNodeChildrenWatcher(path + "/" + child, new LinkedList<>());
                }
                controller.displayChildrenQuantity();

            // Check if a child node is deleted
            } else if (actualChildren != null && actualChildren.size() < previousChildren.size()) {
                Set<String> removedChildren = new HashSet<>(previousChildren);
                removedChildren.removeAll(actualChildren);
                previousChildren.removeAll(removedChildren);
            }

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
