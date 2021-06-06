package com.f4sitive.api.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.function.Function;

public class CursorImpl<T> extends SliceImpl<T> {
    private final String next;
    public CursorImpl(List<T> content, Pageable pageable, boolean hasNext, String next) {
        super(content, pageable, hasNext);
        this.next = next;
    }

    public String getNext() {
        return next;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new CursorImpl<>(getConvertedContent(converter), getPageable(), hasNext(), next);
    }
}
