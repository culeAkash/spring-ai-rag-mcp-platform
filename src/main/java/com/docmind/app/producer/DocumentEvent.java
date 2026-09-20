package com.docmind.app.producer;

public record DocumentEvent(
        Long documentId,
        String fileName,
        String operation,
        String filePath
) {
}