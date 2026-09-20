package com.docmind.app.config;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class RagConfig {

    private static final String CONTEXT_STR = """
                    Source: %s
                    Page: %s

                    Content:
                    %s
                    """;

    @Bean
    public RetrievalAugmentationAdvisor ragAdvisor(
            VectorStore vectorStore
    ) {

        VectorStoreDocumentRetriever retriever =
                VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .similarityThreshold(0.55)
                        .topK(5)
                        .build();

        PromptTemplate promptTemplate =
                new PromptTemplate("""
                        You are a document question-answering assistant.

                        Answer the user's question using ONLY the information
                        provided in the context below.

                        When answering, mention the source document
                        and page when possible.

                        If the answer cannot be found in the context,
                        say:
                        "I could not find the answer in the provided documents."

                        Context:
                        {context}

                        Question:
                        {query}
                        """);

        ContextualQueryAugmenter queryAugmenter =
                ContextualQueryAugmenter.builder()
                        .documentFormatter(documents ->
                                documents.stream()
                                        .map(document -> {

                                            String fileName =
                                                    (String) document
                                                            .getMetadata()
                                                            .get("fileName");

                                            Object pageNumber =
                                                    document
                                                            .getMetadata()
                                                            .get("pageNumber");

                                            return CONTEXT_STR.formatted(
                                                    fileName,
                                                    pageNumber,
                                                    document.getText()
                                            );
                                        })
                                        .collect(Collectors.joining(
                                                "\n\n---\n\n"
                                        ))
                        )
                        .promptTemplate(promptTemplate)
                        .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .queryAugmenter(queryAugmenter)
                .build();
    }
    }
