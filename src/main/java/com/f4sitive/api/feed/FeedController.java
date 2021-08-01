package com.f4sitive.api.feed;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.feed.model.GetFeedResponse;
import com.f4sitive.api.feed.model.GetFeedsResponse;
import com.f4sitive.api.feed.model.PostFeedRequest;
import com.f4sitive.api.feed.model.PostFeedResponse;
import com.f4sitive.api.feed.model.PutFeedRequest;
import com.f4sitive.api.feed.model.PutFeedResponse;
import com.f4sitive.api.model.Slice;
import com.f4sitive.api.service.FeedService;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Optional;

@RestController
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feeds")
    public Mono<Slice<GetFeedResponse>> getFeed(Principal principal,
                                                Pageable pageable,
                                                @RequestParam(required = false) String token,
                                                @RequestParam(required = false) String categoryId,
                                                @RequestParam(required = false) String userId) {
        return feedService.findAll(pageable,
                token,
                Optional.ofNullable(categoryId).map(id -> Criteria.where("category.$id").is(new ObjectId(id))),
                Optional.ofNullable(userId).map(id -> Criteria.where("user").is(id))
        )
                .map(slice -> slice.map(feed -> {
                    return GetFeedResponse.of(feed, Optional.ofNullable(principal).map(Principal::getName).orElse(null));
                }));
    }

    @GetMapping("/feeds/{id}")
    public Mono<GetFeedResponse> getFeedById(Mono<Principal> principal, @PathVariable("id") String id) {
        return feedService.findById(id)
                .flatMap(feed -> principal.map(Principal::getName)
                        .map(name -> GetFeedResponse.of(feed, name))
                        .defaultIfEmpty(GetFeedResponse.of(feed, null)));
    }

    @PostMapping("/feeds")
    public Mono<PostFeedResponse> postFeed(Mono<Principal> principal, @RequestBody PostFeedRequest request) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> {
                    Feed feed = new Feed();
                    feed.setMissionId(request.getMissionId());
                    return feedService.save(name, feed);
                })
                .map(PostFeedResponse::of);
    }

    @PutMapping("/daymotion/feeds/{id}")
    public Mono<PutFeedResponse> putFeedById(@PathVariable("id") String id, @RequestBody PutFeedRequest request) {
        //TODO: 본인 글 체크
        return Mono.just(PutFeedResponse.builder().build());
    }

    @DeleteMapping("/daymotion/feeds/{id}")
    public Mono<Void> deleteFeedById(@PathVariable("id") String id) {
        //TODO: 본인 글 체크
        return Mono.empty();
    }

}
