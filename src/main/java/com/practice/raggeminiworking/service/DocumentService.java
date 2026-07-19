package com.practice.raggeminiworking.service;

import com.practice.raggeminiworking.factory.DocumentReaderFactory;
import com.practice.raggeminiworking.model.VectorChunk;
import com.practice.raggeminiworking.strategy.DocumentReaderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class DocumentService {

    private final DocumentReaderFactory documentReaderFactory;
    private final ChunkService chunkService;
    private final VectorStoreService vectorStoreService;
    private final EmbeddingService embeddingService;

    public DocumentService(DocumentReaderFactory documentReaderFactory,
                           ChunkService chunkService,
                           VectorStoreService vectorStoreService,
                           EmbeddingService embeddingService) {
        this.documentReaderFactory = documentReaderFactory;
        this.chunkService = chunkService;
        this.vectorStoreService = vectorStoreService;
        this.embeddingService = embeddingService;
    }

    public String processDocument(MultipartFile file) throws Exception {
        String sessionId = UUID.randomUUID().toString();
        byte[] fileBytes = file.getBytes(); // Read synchronously
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        CompletableFuture.runAsync(() -> {
            try {
                processAndStore(fileBytes, sessionId, originalFilename, contentType);
            } catch (Exception e) {
                log.error("Failed to process document asynchronously for session {}", sessionId, e);
            }
        });

        return sessionId;
    }

    private void processAndStore(byte[] fileBytes, String sessionId, String fileName, String contentType) throws Exception {
        // FACADE PATTERN: Delegate responsibilities!
        
        // 1. Get correct strategy (PDF vs Text)
        DocumentReaderStrategy reader = documentReaderFactory.getStrategy(fileName, contentType);
        
        // 2. Read text
        String documentText = reader.extractText(fileBytes);

        // 3. Split chunks
        List<String> textChunks = chunkService.splitText(documentText, 500, 100);
        log.info("Total chunks generated: {}", textChunks.size());

        int batchSize = 10;
        List<VectorChunk> allVectorChunks = new ArrayList<>();

        // 4. Generate Embeddings (in batches)
        for (int i = 0; i < textChunks.size(); i += batchSize) {
            int end = Math.min(textChunks.size(), i + batchSize);
            List<String> batchTexts = textChunks.subList(i, end);

            List<List<Double>> batchEmbeddings = embeddingService.createEmbeddings(batchTexts);

            for (int j = 0; j < batchTexts.size(); j++) {
                VectorChunk vc = new VectorChunk(null, sessionId, batchTexts.get(j), batchEmbeddings.get(j));
                vc.setFileName(fileName);
                vc.setSourceType(contentType);
                allVectorChunks.add(vc);
            }
            log.debug("Processed batch {} to {}", i, end);
        }

        // 5. Store chunks
        log.info("Batch storing {} chunks into the database...", allVectorChunks.size());
        vectorStoreService.storeAllChunks(allVectorChunks);
        log.info("Successfully processed and stored Document with {} chunks for session {}", allVectorChunks.size(), sessionId);
    }
}
