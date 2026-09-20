package com.docmind.app.dto;

import lombok.Builder;

@Builder
public record DocumentSearchResult(
        String content,
        String fileName,
        Object pageNumber
) {}
