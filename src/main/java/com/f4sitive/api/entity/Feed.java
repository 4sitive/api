package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.*;


@Document(collection = "feed")
@Getter
@Setter
public class Feed {
    @Id
    private String id;
    private String subject;
    private String body;
    @DBRef
//    @DocumentReference(collection = "mission")
    private Mission mission;
    @DBRef
    private Category category;
    private Map<String, Set<String>> emoji = new HashMap<>();
    private String userId;
    @Transient
    private String missionId;
}
