package com.f4sitive.api.user;

import com.f4sitive.api.service.UserService;
import com.f4sitive.api.user.model.GetUserByIdResponse;
import com.f4sitive.api.user.model.GetUserResponse;
import com.f4sitive.api.user.model.PutUserRequest;
import com.f4sitive.api.user.model.PutUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Optional;

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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN)))
                .flatMap(name -> userService.saveById(name,
                        user -> {
                            Optional.ofNullable(request.getImage()).ifPresent(user::setImage);
                            Optional.ofNullable(request.getIntroduce()).ifPresent(user::setIntroduce);
                            Optional.ofNullable(request.getName()).ifPresent(user::setName);
                            return user;
                        }))
                .map(PutUserResponse::of);
    }
}
