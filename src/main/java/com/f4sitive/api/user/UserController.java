package com.f4sitive.api.user;

import com.f4sitive.api.user.model.GetUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    @GetMapping("/daymotion/users")
    public Mono<GetUserResponse> getUser(){
        return Mono.just(GetUserResponse.builder().build());
    }

    @GetMapping("/daymotion/users/{id}")
    public Mono<GetUserResponse> getUserById(@PathVariable String id){
        return Mono.just(GetUserResponse.builder().build());
    }
}
