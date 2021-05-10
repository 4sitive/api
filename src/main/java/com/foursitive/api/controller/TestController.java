package com.foursitive.api.controller;

import com.foursitive.api.entity.Test;
import com.foursitive.api.repository.TestRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping
    public Flux<TestResponse> get(@RequestHeader("User-Id") String userId) {
        return testRepository.findAll().map(this::mapper);
    }

    @PostMapping
    public Mono<TestResponse> post(@RequestHeader("User-Id") String userId, @RequestBody TestRequest request) {
        Test test = new Test();
        test.setSubject(request.getSubject());
        test.setBody(request.getBody());
        return testRepository.save(test).map(this::mapper);
    }

    TestResponse mapper(Test test) {
        return TestResponse.builder().id(test.getId()).subject(test.getSubject()).body(test.getBody()).build();
    }

    @Getter
    @Setter
    public static class TestRequest {
        private String id;
        private String subject;
        private String body;
    }

    @Builder
    @Getter
    public static class TestResponse {
        private String id;
        private String subject;
        private String body;
    }
}
