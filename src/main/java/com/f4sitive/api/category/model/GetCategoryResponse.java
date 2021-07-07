package com.f4sitive.api.category.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetCategoryResponse {
    private List<CategoryResponse> content;
}
