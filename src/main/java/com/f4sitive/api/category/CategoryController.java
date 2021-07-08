package com.f4sitive.api.category;

import com.f4sitive.api.category.model.GetCategoriesResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {
    @Operation(description = "카테고리 조회")
    @GetMapping("/daymotion/categories")
    public Mono<GetCategoriesResponse> getCategory() {
        return Mono.just(GetCategoriesResponse.builder().build());
    }
}
