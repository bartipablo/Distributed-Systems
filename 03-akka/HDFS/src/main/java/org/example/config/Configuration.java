package org.example.config;

public record Configuration (
    int chunkSize,
    int replicationQuantity,
    int dataNodesQuantity
) {}
