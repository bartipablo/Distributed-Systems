package org.example.persistence;

import org.example.chuncks.ChunkId;
import org.example.chuncks.PersistedDataChunkId;

import java.util.*;

public class PersistenceManager {

    private final Set<PersistedDataChunkId> confirmedChunks;
    private final Set<PersistedDataChunkId> unconfirmedChunks;
    private final Set<PersistedDataChunkId> deletedChunks;
    private final Map<String, Artefact> artefactIdToArtefacts;
    private final Map<String, Set<PersistedDataChunkId>> artefactIdToChunksContentMap;
    private final List<Integer> dataNodeIds;
    private final int replicationQuantity;

    private int actualVersionId = 0;
    private int actualDataNodeId = 0;

    public PersistenceManager(List<Integer> dataNodeIds, int replicationQuantity) {
        this.dataNodeIds = dataNodeIds;
        this.replicationQuantity = replicationQuantity;

        confirmedChunks = new HashSet<>();
        unconfirmedChunks = new HashSet<>();
        deletedChunks = new HashSet<>();
        artefactIdToArtefacts = new HashMap<>();
        artefactIdToChunksContentMap = new HashMap<>();
    }


    public void uploadArtefact(Artefact artefact) {
        if (artefactIdToArtefacts.containsKey(artefact.getId())) {
            removeArtefact(artefact);
        }

        List<PersistedDataChunkId> persistedChunksIds = artefact.getChunksIds()
                .stream()
                .map(chunkId -> assignChunkToDataNodes(chunkId, replicationQuantity))
                .flatMap(Collection::stream)
                .toList();

        artefactIdToArtefacts.put(artefact.getId(), artefact);
        unconfirmedChunks.addAll(persistedChunksIds);

        artefactIdToChunksContentMap.put(artefact.getId(), new HashSet<>(persistedChunksIds));
    }


    public void removeArtefact(Artefact artefact) {
        if (!artefactIdToArtefacts.containsKey(artefact.getId())) {
            return;
        }

        artefactIdToArtefacts.remove(artefact.getId());
        String artefactId = artefact.getId();

        deleteChunksByArtefactId(confirmedChunks, artefactId);
        deleteChunksByArtefactId(unconfirmedChunks, artefactId);
        artefactIdToChunksContentMap.remove(artefactId);
    }


    private void deleteChunksByArtefactId(Set<PersistedDataChunkId> chunks, String artefactId) {
        Set<PersistedDataChunkId> chunksToDelete = artefactIdToChunksContentMap.getOrDefault(artefactId, Collections.emptySet());

        for (PersistedDataChunkId chunkId : chunksToDelete) {
            if (chunks.contains(chunkId)) {
                deletedChunks.add(chunkId);
                chunks.remove(chunkId);
            }
        }
    }


    private List<PersistedDataChunkId> assignChunkToDataNodes(
            ChunkId chunkId,
            int replicationQuantity
    ) {
        List<PersistedDataChunkId> result = new ArrayList<>();

        for (int i = 0; i < replicationQuantity; i++) {
            PersistedDataChunkId persistedChunkId = new PersistedDataChunkId(chunkId, dataNodeIds.get(actualDataNodeId), actualVersionId);
            result.add(persistedChunkId);

            actualDataNodeId++;
            if (actualDataNodeId >= dataNodeIds.size()) actualDataNodeId = 0;
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
            Set<PersistedDataChunkId> persistedChunksForArtefactId = artefactIdToChunksContentMap.getOrDefault(artefactId, Collections.emptySet());

            for (PersistedDataChunkId chunkId : persistedChunksForArtefactId) {
                if (unconfirmedChunks.contains(chunkId)) {
                    return;
                }
            }

            Artefact artefact = artefactIdToArtefacts.get(artefactId);
            artefact.removeChunkContent(persistedChunkId.chunkId());
        }
    }


    public void confirmDeleteChunk(PersistedDataChunkId persistedChunkId) {
        deletedChunks.remove(persistedChunkId);
    }


    public List<PersistedDataChunkId> getUnconfirmedChunks() {
        return new ArrayList<>(unconfirmedChunks);
    }


    public List<PersistedDataChunkId> getUnconfirmedChunksByArtefactId(String artefactId) {
        Set<PersistedDataChunkId> persistedChunksForArtefactId = artefactIdToChunksContentMap.getOrDefault(artefactId, Collections.emptySet());
        return unconfirmedChunks.stream()
                .filter(persistedChunksForArtefactId::contains)
                .toList();
    }


    public List<PersistedDataChunkId> getDeletedChunks() {
        return new ArrayList<>(deletedChunks);
    }


    public List<PersistedDataChunkId> getDeletedChunksByArtefactId(String artefactId) {
        Set<PersistedDataChunkId> persistedChunksForArtefactId = artefactIdToChunksContentMap.getOrDefault(artefactId, Collections.emptySet());
        return deletedChunks.stream()
                .filter(persistedChunksForArtefactId::contains)
                .toList();
    }


    public List<PersistedDataChunkId> getConfirmedChunksByArtefactId(String artefactId) {
        Set<PersistedDataChunkId> persistedChunksForArtefactId = artefactIdToChunksContentMap.getOrDefault(artefactId, Collections.emptySet());
        return confirmedChunks.stream()
                .filter(persistedChunksForArtefactId::contains)
                .toList();
    }


    public Optional<Artefact> getArtefact(String artefactId) {
        return Optional.ofNullable(artefactIdToArtefacts.get(artefactId));
    }


    public Map<ChunkId, String> getArtefactContents(String artefactId) {
        return artefactIdToArtefacts.get(artefactId).getContents();
    }
}
