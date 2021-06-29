package com.f4sitive.api.feed.model;

import com.f4sitive.api.entity.CursorImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.function.Function;

public class GetFeedResponse extends SliceImpl<GetFeedResponse.FeedResponse> {
    private final String next;

    public GetFeedResponse(List<FeedResponse> content, Pageable pageable, boolean hasNext, String next) {
        super(content, pageable, hasNext);
        this.next = next;
    }

    public String getNext() {
        return next;
    }

    @Override
    public <U> Slice<U> map(Function<? super FeedResponse, ? extends U> converter) {
        return new CursorImpl<>(getConvertedContent(converter), getPageable(), hasNext(), next);
    }

    public static class FeedResponse {

    }
}
