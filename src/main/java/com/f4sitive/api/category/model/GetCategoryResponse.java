package com.f4sitive.api.category.model;

import com.f4sitive.api.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetCategoryResponse {
    private List<Content> content;

    public static GetCategoryResponse of(List<Category> categories) {
        return GetCategoryResponse.builder()
                .content(categories.stream().map(Content::of).collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    public static class Content {
        private String id;
        private String name;
        private int feedElements;

        public static Content of(Category category) {
            return Content.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .feedElements(category.getFeeds().size())
                    .build();
        }
    }
}
