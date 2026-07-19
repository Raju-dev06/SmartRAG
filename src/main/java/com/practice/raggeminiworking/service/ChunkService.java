package com.practice.raggeminiworking.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChunkService {

    public List<String> splitText(String text, int chunkSize, int overlap) {
        
        // Spring AI native TokenTextSplitter handles boundaries safely!
        TokenTextSplitter splitter = new TokenTextSplitter(chunkSize, overlap, 5, 1000, true);
        
        List<Document> documents = splitter.split(List.of(new Document(text)));
        
        return documents.stream()
                .map(Document::getContent)
                .collect(Collectors.toList());
    }
}