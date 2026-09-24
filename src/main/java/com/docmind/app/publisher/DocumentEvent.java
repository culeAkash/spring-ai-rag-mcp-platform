package com.docmind.app.publisher;

import java.util.UUID;

public record DocumentEvent(
        UUID eventId,
        Long documentId,
        String fileName,
        String operation,
        String filePath
) {
}