package com.f4sitive.api.feed;

import com.f4sitive.api.feed.model.GetFeedResponse;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

public class FeedController {
    @GetMapping("/feeds")
    public Mono<GetFeedResponse> get() {
        return Mono.empty();
    }
}
