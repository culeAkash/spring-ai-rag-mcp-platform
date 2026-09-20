package com.docmind.app.service;

import com.docmind.app.entity.Document;
import com.docmind.app.entity.OutboxEvent;
import com.docmind.app.producer.DocumentEvent;
import com.docmind.app.repository.DocumentRepository;
import com.docmind.app.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Document createDocument(String fileName, String filePath){
        Document document = Document.builder()
                .fileName(fileName)
                .status("RECEIVED")
                .build();

        Document savedDocument = documentRepository.save(document);

        DocumentEvent documentEvent = new DocumentEvent(
                savedDocument.getId(), savedDocument.getFileName(),"CREATE",filePath
        );

        try{
            String payload = objectMapper.writeValueAsString(documentEvent);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventType("DOCUMENT_CREATE")
                    .payload(payload)
                    .createdAt(LocalDateTime.now())
                    .published(false)
                    .build();

            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to serialize document event",
                    e
            );
        }

        return savedDocument;
    }

    public void markProcessed(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Document not found: " + documentId
                        )
                );

        document.setStatus("PROCESSED");

        documentRepository.save(document);
    }
}
