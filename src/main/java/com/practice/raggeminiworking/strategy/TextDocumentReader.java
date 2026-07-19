package com.practice.raggeminiworking.strategy;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TextDocumentReader implements DocumentReaderStrategy {

    @Override
    public String extractText(byte[] fileBytes) throws Exception {
        // Simple extraction for plain text files
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}
