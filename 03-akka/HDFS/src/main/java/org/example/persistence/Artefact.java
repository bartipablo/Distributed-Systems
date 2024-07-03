package org.example.persistence;

import org.example.chuncks.ChunkId;

import java.util.*;

public class Artefact {

    private final String id;

    private final Map<ChunkId, String> contents;


    public Artefact(String id) {
        this(id, "", 1);
    }


    public Artefact(String id, String content, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than 0");
        }

        this.id = id;
        contents = new HashMap<>();

        List<String> splitedContent = splitContent(content, chunkSize);
        assignChunksIds(splitedContent);
    }


    public String getId() {
        return id;
    }


    public Optional<String> getContent(ChunkId chunkId) {
        return Optional.ofNullable(contents.get(chunkId));
    }


    public Set<ChunkId> getChunksIds() {
        return contents.keySet();
    }


    public void removeChunkContent(ChunkId chunkId) {
        if (!contents.containsKey(chunkId)) {
            return;
        }
        contents.put(chunkId, null);
    }


    private List<String> splitContent(String content, int chunkSize) {
        int contentLength = content.length();
        int chunkCount = (int) Math.ceil((double) contentLength / chunkSize);

        List<String> chunksValue = new ArrayList<>();
        for (int i = 0; i < chunkCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, contentLength);
            String chunk = content.substring(start, end);
            chunksValue.add(chunk);
        }
        return chunksValue;
    }


    private void assignChunksIds(List<String> splitedContent) {
        for (int i = 0; i < splitedContent.size(); i++) {
            ChunkId chunkId = new ChunkId(i, id);
            contents.put(chunkId, splitedContent.get(i));
        }
    }


    public Map<ChunkId, String> getContents() {
        return new HashMap<>(contents);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artefact artefact = (Artefact) o;
        return Objects.equals(id, artefact.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
