package com.practice.raggeminiworking.controller;

import com.practice.raggeminiworking.model.VectorChunk;
import com.practice.raggeminiworking.service.EmbeddingService;
import com.practice.raggeminiworking.service.GeminiService;
import com.practice.raggeminiworking.service.VectorStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final GeminiService geminiService;
    private final VectorStoreService vectorStoreService;
    private final EmbeddingService embeddingService;

    public ChatController(GeminiService geminiService,
                          VectorStoreService vectorStoreService,
                          EmbeddingService embeddingService) {

        this.geminiService = geminiService;
        this.vectorStoreService = vectorStoreService;
        this.embeddingService = embeddingService;
    }

    @GetMapping
    public String ask(@RequestParam String sessionId,
                      @RequestParam String question) {

        List<Double> questionEmbedding =
                embeddingService.createEmbeddings(List.of(question)).get(0);

        List<VectorChunk> chunks =
                vectorStoreService.searchSimilarChunks(sessionId, question, questionEmbedding, 5);

        log.debug("Found {} similar chunks for the question", chunks.size());

        String context = chunks.stream()
                .map(VectorChunk::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        return geminiService.askGeminiWithContext(context, question);
    }
}