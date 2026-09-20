package com.docmind.app.controller;

import com.docmind.app.dto.CreateDocumentRequest;
import com.docmind.app.entity.Document;
import com.docmind.app.service.DocumentService;
import com.docmind.app.service.PdfIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Document createDocument(
            @RequestBody CreateDocumentRequest request
    ) {
        return documentService.createDocument(request.fileName(),request.filePath());
    }

}
