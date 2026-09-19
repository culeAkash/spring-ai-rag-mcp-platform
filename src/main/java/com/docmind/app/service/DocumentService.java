package com.docmind.app.service;

import com.docmind.app.entity.Document;
import com.docmind.app.producer.DocumentEvent;
import com.docmind.app.producer.DocumentEventProducer;
import com.docmind.app.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentEventProducer eventProducer;

    public Document createDocument(String fileName){
        Document document = Document.builder()
                .fileName(fileName)
                .status("RECEIVED")
                .build();

        Document savedDocument = documentRepository.save(document);

        DocumentEvent documentEvent = new DocumentEvent(
                savedDocument.getId(), savedDocument.getFileName(),"CREATE"
        );

        eventProducer.publish(documentEvent);

        return savedDocument;
    }

    public void process(DocumentEvent event) {
        Document document = documentRepository.findById(event.documentId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Document not found: " + event.documentId()
                        )
                );

        document.setStatus("PROCESSED");

        documentRepository.save(document);
    }
}
