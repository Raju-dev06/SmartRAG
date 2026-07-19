package com.practice.raggeminiworking.strategy;

public interface DocumentReaderStrategy {
    /**
     * Extracts raw text from the file bytes.
     * 
     * @param fileBytes The raw bytes of the uploaded file.
     * @return The extracted string content.
     * @throws Exception If parsing fails.
     */
    String extractText(byte[] fileBytes) throws Exception;
}
