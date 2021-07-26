package com.f4sitive.api.service;

import com.f4sitive.api.entity.Category;
import com.f4sitive.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<List<Category>> findAll() {
        return Mono.fromCallable(() -> categoryRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
