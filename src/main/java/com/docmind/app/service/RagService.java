package com.docmind.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {
    private final ChatClient chatClient;

    private final RetrievalAugmentationAdvisor ragAdvisor;



    public String ask(String query){
        return chatClient
                .prompt()
                .advisors(ragAdvisor)
                .user(query)
                .call()
                .content();
    }

//    public String ask(String query){
//        // 1. Retrieve relevant chunks
//        SearchRequest request =
//                SearchRequest.builder()
//                        .query(query)
//                        .topK(10)
//                        .similarityThreshold(0.6)
//                        .build();
//
//        List<Document> relevantDocuments = vectorStore.similaritySearch(request);
//
//        // 2. Build context from retrieved chunks
//
//        String context = relevantDocuments.stream()
//                .map(document -> {
//                    String fileName =
//                            (String) document.getMetadata().get("fileName");
//
//                    Object pageNumber =
//                            document.getMetadata().get("pageNumber");
//
//
//                    return CONTEXT_STR.formatted(
//                            fileName,
//                            pageNumber,
//                            document.getText()
//                    );
//                })
//                .collect(Collectors.joining("\n\n---\n\n"));
//
//
//        // 3. Build prompt
//
//        String prompt = """
//        You are a document question-answering assistant.
//
//        Answer the user's question using ONLY the information
//        provided in the context.
//
//        When answering, mention the source document and page when possible.
//
//        If the answer cannot be found in the context,
//        say:
//        "I could not find the answer in the provided documents."
//
//        Context:
//        %s
//
//        Question:
//        %s
//        """.formatted(context, query);
//
//        // 4. Send context + question to Gemini
//        return chatClient
//                .prompt()
//                .user(prompt)
//                .call()
//                .content();
//    }
}
