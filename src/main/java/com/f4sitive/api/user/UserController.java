package com.f4sitive.api.user;

import com.f4sitive.api.service.UserService;
import com.f4sitive.api.user.model.GetUserByIdResponse;
import com.f4sitive.api.user.model.GetUserResponse;
import com.f4sitive.api.user.model.PutUserRequest;
import com.f4sitive.api.user.model.PutUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Mono<GetUserResponse> getUser(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .flatMap(userService::findById)
                .map(GetUserResponse::of);
    }

    @GetMapping("/users/{id}")
    public Mono<GetUserByIdResponse> getUser(@PathVariable String id) {
        return userService.findById(id)
                .map(GetUserByIdResponse::of);
    }

    @PutMapping("/users")
    public Mono<PutUserResponse> putUser(Mono<Principal> principal, @RequestBody PutUserRequest request) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> userService.saveById(name,
                        user -> {
                            user.setImage(request.getImage());
                            user.setIntroduce(request.getIntroduce());
                            return user;
                        }))
                .map(PutUserResponse::of);
    }
}
