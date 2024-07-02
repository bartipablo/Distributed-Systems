package org.example.persistence;

import org.example.chuncks.ChunkId;
import org.example.chuncks.PersistedDataChunkId;
import org.example.config.Configuration;

import java.util.*;

public class PersistenceManager {

    private final Set<PersistedDataChunkId> confirmedChunks;
    private final Set<PersistedDataChunkId> unconfirmedChunks;
    private final Set<PersistedDataChunkId> deletedChunks;
    private final Set<Artefact> artefacts;
    private final Map<String, Set<PersistedDataChunkId>> artefactToChunksMap;
    private final Configuration config;

    private int actualVersionId = 0;
    private int actualDataNodeId = 0;

    public PersistenceManager(Configuration config) {
        this.config = config;
        confirmedChunks = new HashSet<>();
        unconfirmedChunks = new HashSet<>();
        deletedChunks = new HashSet<>();
        artefacts = new HashSet<>();
        artefactToChunksMap = new HashMap<>();
    }


    public void uploadArtefact(Artefact artefact) {
        if (artefacts.contains(artefact)) {
            removeArtefact(artefact);
        }

        List<PersistedDataChunkId> persistedChunksIds = artefact.getChunksIds()
                .stream()
                .map(chunkId -> assignChunkToDataNodes(chunkId, config.getReplicationQuantity(), config.getDataNodesQuantity()))
                .flatMap(Collection::stream)
                .toList();

        artefacts.add(artefact);
        unconfirmedChunks.addAll(persistedChunksIds);

        artefactToChunksMap.put(artefact.getId(), new HashSet<>(persistedChunksIds));
    }


    public void removeArtefact(Artefact artefact) {
        if (!artefacts.contains(artefact)) {
            return;
        }

        artefacts.remove(artefact);
        String artefactId = artefact.getId();

        deleteChunksByArtefactId(confirmedChunks, artefactId);
        deleteChunksByArtefactId(unconfirmedChunks, artefactId);
        artefactToChunksMap.remove(artefactId);
    }


    private void deleteChunksByArtefactId(Set<PersistedDataChunkId> chunks, String artefactId) {
        Set<PersistedDataChunkId> chunksToDelete = artefactToChunksMap.getOrDefault(artefactId, Collections.emptySet());

        for (PersistedDataChunkId chunkId : chunksToDelete) {
            if (chunks.contains(chunkId)) {
                deletedChunks.add(chunkId);
                chunks.remove(chunkId);
            }
        }
    }


    private List<PersistedDataChunkId> assignChunkToDataNodes(
            ChunkId chunkId,
            int replicationQuantity,
            int dataNodesQuantity
    ) {
        List<PersistedDataChunkId> result = new ArrayList<>();

        for (int i = 0; i < replicationQuantity; i++) {
            PersistedDataChunkId persistedChunkId = new PersistedDataChunkId(chunkId, actualDataNodeId, actualVersionId);
            result.add(persistedChunkId);

            actualDataNodeId++;
            if (actualDataNodeId >= dataNodesQuantity) actualDataNodeId = 0;
        }

        actualVersionId++;
        if (actualVersionId >= Integer.MAX_VALUE - 1) actualVersionId = 0;

        return result;
    }


    public void confirmChunk(PersistedDataChunkId persistedChunkId) {
        if (unconfirmedChunks.contains(persistedChunkId)) {
            unconfirmedChunks.remove(persistedChunkId);
            confirmedChunks.add(persistedChunkId);

            String artefactId = persistedChunkId.chunkId().artefactId();
            Set<PersistedDataChunkId> persistedChunksForArtefactId = artefactToChunksMap.getOrDefault(artefactId, Collections.emptySet());

            for (PersistedDataChunkId chunkId : persistedChunksForArtefactId) {
                if (unconfirmedChunks.contains(chunkId)) {
                    return;
                }
                Artefact artefact = artefacts.stream().filter(a -> a.getId().equals(artefactId)).findFirst().orElseThrow();
                artefact.removeChunkContent(chunkId.chunkId());
            }
        }
    }


    public void confirmDeleteChunk(PersistedDataChunkId persistedChunkId) {
        deletedChunks.remove(persistedChunkId);
    }


    public List<PersistedDataChunkId> getUnconfirmedChunks() {
        return new ArrayList<>(unconfirmedChunks);
    }


    public List<PersistedDataChunkId> getDeletedChunks() {
        return new ArrayList<>(deletedChunks);
    }


    public Optional<Artefact> getArtefact(String artefactId) {
        return artefacts.stream().filter(a -> a.getId().equals(artefactId)).findFirst();
    }
}
