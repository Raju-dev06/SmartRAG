package com.practice.raggeminiworking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public List<List<Double>> createEmbeddings(List<String> texts) {
        try {
            EmbeddingRequest request = new EmbeddingRequest(texts, org.springframework.ai.embedding.EmbeddingOptions.EMPTY);
            EmbeddingResponse response = embeddingModel.call(request);
            
            return response.getResults().stream()
                    .map(Embedding::getOutput)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to create embeddings", e);
            throw new RuntimeException(e);
        }
    }
}