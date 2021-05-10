package com.foursitive.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Document(collection = "test")
@Getter
@Setter
public class Test {
    @MongoId
    private String id;
    private String subject;
    private String body;
}
