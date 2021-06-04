package com.f4sitive.api.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CursorImpl<T> extends SliceImpl<T> {
    private String next;
    public CursorImpl(List<T> content, Pageable pageable, boolean hasNext, String next) {
        super(content, pageable, hasNext);
        this.next = next;
    }
}
