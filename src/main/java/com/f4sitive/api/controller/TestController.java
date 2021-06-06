package com.f4sitive.api.controller;

import com.f4sitive.api.entity.Tag;
import com.f4sitive.api.entity.Test;
import com.f4sitive.api.repository.TagRepository;
import com.f4sitive.api.repository.TestRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestRepository testRepository;
    private final TagRepository tagRepository;

    public TestController(TestRepository testRepository, TagRepository tagRepository) {
        this.testRepository = testRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public Slice<TestResponse> get(Principal principal, @RequestParam(required = false) Map<String, Object> param, Pageable pageable) {
//        ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
//        return principal.map(o -> {
//            System.out.print(o.getName());
//            testRepository.test(pageable);
//            return testRepository.findAll(pageable).map(this::mapper);
//        });
        System.out.print(principal);
        System.out.print(param);
        return testRepository.test(pageable, param).map(this::mapper);
    }

    @PostMapping
    public TestResponse post(@RequestHeader("User-Id") String userId, @RequestBody TestRequest request) {
        Test test = new Test();
        test.setSubject(request.getSubject());
        test.setBody(request.getBody());
        test.setTags(tags(request.getTags()));
        return mapper(testRepository.save(test));
    }

    @PutMapping("/{id}")
    public TestResponse put(@RequestHeader("User-Id") String userId, @PathVariable String id, @RequestBody TestRequest request) {
        return testRepository.findById(id)
                .map(test -> {
                    test.setBody(request.getBody());
                    test.setSubject(request.getSubject());
                    test.setTags(tags(request.getTags()));
                    return testRepository.save(test);
                })
                .map(this::mapper)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

    List<Tag> tags(Collection<String> names) {
        List<Tag> tags = tagRepository.findAllByNameIn(names);
        tagRepository.saveAll(names.stream().filter(name -> tags.stream().noneMatch(t -> name.equals(t.getName()))).map(Tag::new).collect(Collectors.toList()))
                .forEach(tag -> Collections.addAll(tags, tag));
        return tags;
    }

    TestResponse mapper(Test test) {
        return TestResponse.builder().id(test.getId()).subject(test.getSubject()).body(test.getBody()).tags(test.getTags().stream().map(Tag::getName).collect(Collectors.toList())).build();
    }

    @Getter
    @Setter
    public static class TestRequest {
        private String subject;
        private String body;
        private Collection<String> tags = new LinkedHashSet<>();
    }

    @Builder
    @Getter
    public static class TestResponse {
        private String id;
        private String subject;
        private String body;
        private Collection<String> tags;
    }
}
