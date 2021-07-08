package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Document(collection = "feed")
@Getter
@Setter
public class Feed {
    @Id
    private String id;
    private String subject;
    private String body;
    private Map<String, Set<String>> emoji = new HashMap<>();
}
