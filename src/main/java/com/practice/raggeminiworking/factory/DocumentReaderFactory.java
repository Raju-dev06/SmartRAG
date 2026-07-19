package com.practice.raggeminiworking.factory;

import com.practice.raggeminiworking.strategy.DocumentReaderStrategy;
import com.practice.raggeminiworking.strategy.PdfDocumentReader;
import com.practice.raggeminiworking.strategy.TextDocumentReader;
import org.springframework.stereotype.Component;

@Component
public class DocumentReaderFactory {

    private final PdfDocumentReader pdfDocumentReader;
    private final TextDocumentReader textDocumentReader;

    public DocumentReaderFactory(PdfDocumentReader pdfDocumentReader, TextDocumentReader textDocumentReader) {
        this.pdfDocumentReader = pdfDocumentReader;
        this.textDocumentReader = textDocumentReader;
    }

    public DocumentReaderStrategy getStrategy(String fileName, String contentType) {
        if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
            return pdfDocumentReader;
        } else if (fileName != null && fileName.toLowerCase().endsWith(".txt")) {
            return textDocumentReader;
        }
        
        // Default to plain text or throw exception
        throw new IllegalArgumentException("Unsupported file type: " + fileName);
    }
}
