package com.docmind.app.consumer;

import com.docmind.app.entity.ProcessedEvent;
import com.docmind.app.publisher.DocumentEvent;
import com.docmind.app.repository.ProcessedEventRepository;
import com.docmind.app.service.DocumentService;
import com.docmind.app.service.PdfIngestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentEventConsumer {

    private final DocumentService documentService;
    private final PdfIngestionService pdfIngestionService;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(
            topics = "document-events",
            groupId = "document-processor"
    )
    @Transactional
    public void consume(DocumentEvent event) {
        System.out.println(
                "Receive document event: " + event
        );

        // making sure no duplicate events are processed, making consumer idempotent
        if(processedEventRepository.existsById(event.eventId())){
            System.out.println(
                    "Duplicate event ignored: " + event.eventId()
            );

            return;
        }

        System.out.println(
                "Processing event: " + event.eventId()
        );


        if("CREATE".equals(event.operation())){
            pdfIngestionService.ingest(
                    event.documentId(),
                    event.fileName(),
                    event.filePath()
            );

            documentService.markProcessed(
                    event.documentId()
            );

            // Mark event as processed
            processedEventRepository.save(
                    new ProcessedEvent(event.eventId())
            );
        }
    }
}
