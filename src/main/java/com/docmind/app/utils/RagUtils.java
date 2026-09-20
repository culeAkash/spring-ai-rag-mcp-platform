package com.docmind.app.utils;

import lombok.experimental.UtilityClass;
import org.springframework.ai.vectorstore.SearchRequest;

@UtilityClass
public class RagUtils {

    public SearchRequest buildSearchRequest(String query) {
        return SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThreshold(0.55)
                .build();
    }
}
