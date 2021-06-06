package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "tag")
@Getter
@Setter
@ToString(callSuper = false)
@NoArgsConstructor
public class Tag {
    @Id
    private String id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}