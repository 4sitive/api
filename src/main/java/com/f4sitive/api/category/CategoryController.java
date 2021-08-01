package com.f4sitive.api.category;

import com.f4sitive.api.category.model.GetCategoryResponse;
import com.f4sitive.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(description = "카테고리 조회")
    @GetMapping("/categories")
    public Mono<GetCategoryResponse> getCategory() {
        return categoryService.findAll()
                .map(GetCategoryResponse::of);
    }
}
