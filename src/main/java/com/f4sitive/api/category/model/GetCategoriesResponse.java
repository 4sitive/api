package com.f4sitive.api.category.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetCategoriesResponse {
    private List<Content> content;

    @Getter
    @Builder
    public static class Content {
        private String id;
        private String name;
        private long feedElements;
    }
}
