package com.f4sitive.api.service;

import com.f4sitive.api.entity.User;
import com.f4sitive.api.repository.UserRepository;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Mono<User> findById(String id) {
        return Mono.justOrEmpty(userRepository.findById(id)).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<User> saveById(String id, Function<User, User> function) {
        return Mono.justOrEmpty(userRepository.findById(id).map(function).map(userRepository::save)).subscribeOn(Schedulers.boundedElastic());
    }
}
