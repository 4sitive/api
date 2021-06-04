package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.LinkedHashSet;


@Document(collection = "test")
@Getter
@Setter
public class Test {
    @Id
    private String id;
    private String subject;
    private String body;
    private Collection<Tag> tags = new LinkedHashSet<>();

}
