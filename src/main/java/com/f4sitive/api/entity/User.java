package com.f4sitive.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String image;
    private String introduce;
    @Indexed(unique = true)
    private String username;
    private String name;

    public static User id(String id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
