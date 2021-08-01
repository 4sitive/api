package com.f4sitive.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class Slice<T> {
    private List<T> content;
    private String token;

    public Slice(List<T> content, String token) {
        this.content = content;
        this.token = token;
    }

    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new Slice<>(content.stream().map(converter::apply).collect(Collectors.toList()), token);
    }
}
