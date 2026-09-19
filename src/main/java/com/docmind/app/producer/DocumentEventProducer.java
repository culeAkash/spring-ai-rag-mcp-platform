package com.docmind.app.producer;

import com.docmind.app.entity.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentEventProducer {

    private static final String TOPIC = "document-events";

    private final KafkaTemplate<String,DocumentEvent> kafkaTemplate;

    public void publish(DocumentEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.documentId().toString(),
                event
        );
    }
}
