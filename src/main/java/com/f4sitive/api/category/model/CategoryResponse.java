package com.f4sitive.api.category.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private String id;
    private String name;
    private long feedElements;
}
