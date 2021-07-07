package com.f4sitive.api.feed.model;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.function.Function;

public class GetFeedResponse<T> extends SliceImpl<T> {
    @Getter
    private final String pageToken;

    public GetFeedResponse(List<T> content, Pageable pageable, boolean hasNext, String pageToken) {
        super(content, pageable, hasNext);
        this.pageToken = pageToken;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new GetFeedResponse<>(getConvertedContent(converter), getPageable(), hasNext(), pageToken);
    }
}
