package com.docmind.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfIngestionService {

    private final DocumentChunkingService chunkingService;
    private final VectorStore vectorStore;
    private final PdfDocumentReaderConfig pdfDocumentReaderConfig;

    public void ingest(Long documentId,String fileName, String filePath){
        FileSystemResource resource = new FileSystemResource(filePath);


        DocumentReader reader = new PagePdfDocumentReader(resource,pdfDocumentReaderConfig);

        // 1. Extract pages from PDF
        List<Document> pages = reader.read();

        System.out.println(
                "Extracted pages: " + pages.size()
        );

        // 2. Chunk each page
        int pageNumber = 1;
        for(Document document : pages){
            List<Document> chunks = this.chunkingService.split(document,documentId,fileName,pageNumber);

            System.out.println(
                    "Chunks created: " + chunks.size()
            );

            // 3. Store chunks in PGVector
            vectorStore.add(chunks);

            // 4. Print chunks for verification
            for (Document chunk : chunks) {

                System.out.println(
                        "----- CHUNK -----"
                );

                System.out.println(
                        "Metadata: "
                                + chunk.getMetadata()
                );

                System.out.println(
                        chunk.getText()
                );
            }
        }
    }
}
