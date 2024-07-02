package org.example.config;

public interface Configuration {
    int DEFAULT_CHUNK_SIZE = 1024;
    int DEFAULT_REPLICATION_QUANTITY = 3;
    int DEFAULT_DATA_NODES_QUANTITY = 4;

    default int getChunkSize() {
        return DEFAULT_CHUNK_SIZE;
    }

    default int getReplicationQuantity() {
        return DEFAULT_REPLICATION_QUANTITY;
    }

    default int getDataNodesQuantity() {
        return DEFAULT_DATA_NODES_QUANTITY;
    }
}
