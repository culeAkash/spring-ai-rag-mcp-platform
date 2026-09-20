package com.docmind.app.mcp;

import com.docmind.app.dto.DocumentSearchResult;
import com.docmind.app.utils.RagUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DocumentSearchTool {

    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(DocumentSearchTool.class.getName());

    @McpTool(
            name = "search_documents",
            description = """
Searches the DocMind knowledge base for information contained
in the ingested documents. Use this tool when the user asks
a question that may be answered from the available documents.
"""
    )
    public List<DocumentSearchResult> searchDocuments(String query){
        logger.info("MCP started");

        List<Document> retrievedDocs = vectorStore.similaritySearch(RagUtils.buildSearchRequest(query));

        if(retrievedDocs.isEmpty()){
            return List.of(
                    DocumentSearchResult.builder()
                            .pageNumber(-1)
                            .fileName(null)
                            .content("No relevant documents were found for the given query.")
                            .build()
            );
        }

        return retrievedDocs.stream()
                .map(document -> {
                    String fileName =
                            (String) document
                                    .getMetadata()
                                    .get("fileName");

                    Object pageNumber =
                            document
                                    .getMetadata()
                                    .get("pageNumber");

                    return DocumentSearchResult.builder()
                            .content(document.getText())
                            .pageNumber(pageNumber)
                            .fileName(fileName)
                            .build();
                }).toList();
    }
}
