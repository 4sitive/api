package com.f4sitive.api.feed;

import com.f4sitive.api.feed.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
public class FeedController {
    @GetMapping("/daymotion/feeds")
    public Mono<GetFeedResponse<FeedResponse>> getFeed(Pageable pageable,
                                                       @RequestParam(required = false) String pageToken,
                                                       @RequestParam(required = false) String categoryName) {
        return Mono.just(new GetFeedResponse(Collections.emptyList(), pageable, false, pageToken));
    }

    @GetMapping("/daymotion/feeds/{id}")
    public Mono<FeedResponse> getFeed(@PathVariable("id") String id) {
        return Mono.just(FeedResponse.builder().build());
    }

    @PostMapping("/daymotion/feeds")
    public Mono<PostFeedResponse> getFeed(@RequestBody PostFeedRequest request) {
        //TODO: 미션 체크 후 카테고리 선택 (반정규화)
        return Mono.just(PostFeedResponse.builder().build());
    }

    @PutMapping("/daymotion/feeds/{id}")
    public Mono<PutFeedResponse> putFeed(@PathVariable("id") String id, @RequestBody PutFeedRequest request) {
        //TODO: 본인 글 체크
        return Mono.just(PutFeedResponse.builder().build());
    }

    @DeleteMapping("/daymotion/feeds/{id}")
    public Mono<Void> deleteFeed(@PathVariable("id") String id) {
        //TODO: 본인 글 체크
        return Mono.empty();
    }

}
