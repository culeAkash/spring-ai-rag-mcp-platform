package com.docmind.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public String ask(String query){
        // 1. Retrieve relevant chunks
        SearchRequest request =
                SearchRequest.builder()
                        .query(query)
                        .topK(10)
                        .similarityThreshold(0.0)
                        .build();

        List<Document> relevantDocuments = vectorStore.similaritySearch(request);

        // 2. Build context from retrieved chunks
        String context = relevantDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));


        // 3. Build prompt

        String prompt = """
                You are a document question-answering assistant.

                Answer the user's question using ONLY the information
                provided in the context below.

                If the answer cannot be found in the context,
                say: "I could not find the answer in the provided documents."

                Context:
                %s

                Question:
                %s
                """.formatted(context, query);

        // 4. Send context + question to Gemini
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
