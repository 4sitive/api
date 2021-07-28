package com.f4sitive.api.feed;

import com.f4sitive.api.entity.Feed;
import com.f4sitive.api.feed.model.*;
import com.f4sitive.api.service.FeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feeds")
    public Mono<GetFeedsResponse<GetFeedsResponse.Content>> getFeed(Pageable pageable,
                                                                    @RequestParam(required = false) String pageToken,
                                                                    @RequestParam(required = false) String categoryId,
                                                                    @RequestParam(required = false) String userId) {
        return Mono.just(new GetFeedsResponse(Collections.emptyList(), pageable, false, pageToken));
    }

    @GetMapping("/feeds/{id}")
    public Mono<GetFeedResponse> getFeedById(@PathVariable("id") String id) {
        return Mono.justOrEmpty(feedService.findById(id)
                .map(GetFeedResponse::of));
    }

    @PostMapping("/feeds")
    public Mono<PostFeedResponse> postFeed(@RequestBody PostFeedRequest request) {
        //TODO: 미션 체크 후 카테고리 선택 (반정규화)
        Feed feed = new Feed();
        feed.setMissionId(request.getMissionId());
        return feedService.save(feed).map(PostFeedResponse::of);
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
