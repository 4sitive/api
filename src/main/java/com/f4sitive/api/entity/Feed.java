package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Document(collection = "feed")
@Getter
@Setter
public class Feed {
    @Id
    private String id;
    private String image;
    @DBRef
    private Mission mission;
    @DBRef
    private Category category;
    @DocumentReference
    private User user;
    private String requestId;
    private Map<String, Set<String>> emoji = new LinkedHashMap<>();
}
