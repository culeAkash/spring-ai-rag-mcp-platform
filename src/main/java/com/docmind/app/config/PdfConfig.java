package com.docmind.app.config;

import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {

    @Bean
    public PdfDocumentReaderConfig pdfDocumentReaderConfig() {
        // We can add extra config later
        return PdfDocumentReaderConfig.builder()
                .build();
    }
}
