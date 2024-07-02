package org.example.chuncks;

import java.io.Serializable;
import java.util.Objects;

public record PersistedDataChunkId (
        ChunkId chunkId,
        int dataNodeId,
        int versionId
) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistedDataChunkId that = (PersistedDataChunkId) o;
        return (dataNodeId == that.dataNodeId && versionId == that.versionId && Objects.equals(chunkId, that.chunkId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkId, dataNodeId, versionId);
    }
}
