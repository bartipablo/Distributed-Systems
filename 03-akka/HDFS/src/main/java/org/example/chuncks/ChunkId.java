package org.example.chuncks;

import java.io.Serializable;
import java.util.Objects;

public record ChunkId (
        int id,
        String artefactId
) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkId chunkId = (ChunkId) o;
        return id == chunkId.id && Objects.equals(artefactId, chunkId.artefactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artefactId);
    }


    @Override
    public String toString() {
        return "ChunkId{" +
                "id=" + id +
                ", artefactId='" + artefactId + '\'' +
                '}';
    }
}
