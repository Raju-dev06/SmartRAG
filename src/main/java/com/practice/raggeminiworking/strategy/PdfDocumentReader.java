package com.practice.raggeminiworking.strategy;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PdfDocumentReader implements DocumentReaderStrategy {

    @Override
    public String extractText(byte[] fileBytes) throws Exception {
        // We moved the Apache PDFBox logic here (Single Responsibility Principle)
        PDDocument document = Loader.loadPDF(fileBytes);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }
}
