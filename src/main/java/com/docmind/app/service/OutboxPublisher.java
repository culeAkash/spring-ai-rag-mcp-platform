package com.docmind.app.service;

import com.docmind.app.entity.OutboxEvent;
import com.docmind.app.producer.DocumentEvent;
import com.docmind.app.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, DocumentEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Scheduled(fixedDelay = 10000)
    public void publishEvents(){
        var events = this.outboxEventRepository
                .findTop100ByPublishedFalseOrderByIdAsc();

        for(OutboxEvent outboxEvent:events){
            try{
                DocumentEvent event =
                        objectMapper.readValue(
                                outboxEvent.getPayload(),
                                DocumentEvent.class
                        );

                kafkaTemplate.send(
                        "document-events",
                        event.documentId().toString(),
                        event
                );

                outboxEvent.setPublished(true);

                outboxEventRepository.save(outboxEvent);
            }catch (Exception e) {

                System.err.println(
                        "Failed to publish outbox event "
                                + outboxEvent.getId()
                );
            }
        }
    }
}
