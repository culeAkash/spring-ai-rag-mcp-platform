package com.docmind.app.consumer;

import com.docmind.app.producer.DocumentEvent;
import com.docmind.app.service.DocumentService;
import com.docmind.app.service.PdfIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentEventConsumer {

    private final DocumentService documentService;
    private final PdfIngestionService pdfIngestionService;

    @KafkaListener(
            topics = "document-events",
            groupId = "document-processor"
    )
    public void consume(DocumentEvent event) {
        System.out.println(
                "Receive document event: " + event
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
        }
    }
}
