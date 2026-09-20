package com.docmind.app.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DocumentChunkingService {

    private final TokenTextSplitter tokenTextSplitter;

    public DocumentChunkingService() {
        this.tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(50)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(1000)
                .build();
    }

    public List<Document> split(Document document, Long documentId, String fileName, int pageNumber) {
        List<Document> chunks = tokenTextSplitter.apply(List.of(document));
        AtomicInteger index= new AtomicInteger(0);

        chunks.forEach(chunk ->
                chunk.getMetadata()
                        .putAll(Map
                                .of("documentId", documentId,
                                        "fileName", fileName,
                                        "chunkNumber",index.getAndIncrement(),
                                        "pageNumber",pageNumber)));

        return chunks;
    }
}
