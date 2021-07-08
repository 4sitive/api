package com.f4sitive.api.feed.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.function.Function;

public class GetFeedsResponse<T> extends SliceImpl<T> {
    @Getter
    private final String pageToken;

    public GetFeedsResponse(List<T> content, Pageable pageable, boolean hasNext, String pageToken) {
        super(content, pageable, hasNext);
        this.pageToken = pageToken;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new GetFeedsResponse<>(getConvertedContent(converter), getPageable(), hasNext(), pageToken);
    }

    @Getter
    @Builder
    public static class Content {
        private String id;
        private String image;
    }
}
