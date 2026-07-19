package com.practice.raggeminiworking.service;

import com.practice.raggeminiworking.model.VectorChunk;
import com.practice.raggeminiworking.repository.ChunkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorStoreService {

    private final ChunkRepository chunkRepository;

    public VectorStoreService(ChunkRepository chunkRepository) {
        this.chunkRepository = chunkRepository;
    }

    public void storeChunk(VectorChunk chunk) {
        chunkRepository.saveChunk(chunk);
    }

    @Transactional
    public void storeAllChunks(List<VectorChunk> chunks) {
        chunkRepository.saveAllChunks(chunks);
    }

    public List<VectorChunk> searchSimilarChunks(String sessionId, String question, List<Double> embedding, int limit) {

        String vectorString = embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        return chunkRepository.searchSimilarChunks(sessionId, question, vectorString, limit);
    }
}