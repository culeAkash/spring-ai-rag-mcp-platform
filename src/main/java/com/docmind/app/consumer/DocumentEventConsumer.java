package com.docmind.app.consumer;

import com.docmind.app.producer.DocumentEvent;
import com.docmind.app.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentEventConsumer {

    private final DocumentService documentService;

    @KafkaListener(
            topics = "document-events",
            groupId = "document-processor"
    )
    public void consume(DocumentEvent event) {
        System.out.println(
                "Receive document event: " + event
        );

        documentService.process(event);
    }
}
