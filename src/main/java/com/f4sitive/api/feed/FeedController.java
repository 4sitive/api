package com.f4sitive.api.feed;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.feed.model.GetFeedResponse;
import com.f4sitive.api.feed.model.PostFeedRequest;
import com.f4sitive.api.feed.model.PostFeedResponse;
import com.f4sitive.api.feed.model.PutFeedByIdEmojiRequest;
import com.f4sitive.api.feed.model.PutFeedByIdEmojiResponse;
import com.f4sitive.api.model.Slice;
import com.f4sitive.api.service.FeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
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
                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                @RequestParam(required = false) String token,
                                                @RequestParam(required = false) String categoryId,
                                                @RequestParam(required = false) String missionId,
                                                @RequestParam(required = false) String userId) {
        return feedService.findAll(pageable,
                token,
                Optional.ofNullable(categoryId).map(id -> Criteria.where("category.id").is(id)),
                Optional.ofNullable(missionId).map(id -> Criteria.where("mission.id").is(id)),
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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                .flatMap(name -> {
                    Feed feed = new Feed();
                    feed.setImage(request.getImage());
                    feed.setRequestId(request.getRequestId());
                    return feedService.save(name, request.getMissionId(), feed);
                })
                .map(PostFeedResponse::of);
    }

    @PutMapping("/feeds/{id}/emoji")
    public Mono<PutFeedByIdEmojiResponse> putFeedById(Mono<Principal> principal, @PathVariable("id") String id, @RequestBody PutFeedByIdEmojiRequest request) {
        return principal
                .map(Principal::getName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                .flatMap(name -> feedService.saveById(id, feed -> {
                    feed.getEmoji().put(name, request.getEmoji());
                    return feed;
                }))
                .map(PutFeedByIdEmojiResponse::of);
    }

    @DeleteMapping("/feeds/{id}")
    public Mono<ServerResponse> deleteFeedById(Mono<Principal> principal, @PathVariable("id") String id) {
        return principal
                .map(Principal::getName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                .flatMap(name -> ServerResponse.noContent().build(feedService.deleteById(name, id)));
    }

}
