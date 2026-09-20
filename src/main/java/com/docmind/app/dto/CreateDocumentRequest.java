package com.docmind.app.dto;

public record CreateDocumentRequest (
    String fileName,
    String filePath
){}
