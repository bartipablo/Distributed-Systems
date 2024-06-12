package org.example;

public record ZooKeeperConfiguration(
        String zooKeeperHost,
        String zNodePath
) {}