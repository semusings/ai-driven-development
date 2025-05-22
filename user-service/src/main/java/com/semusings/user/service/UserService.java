package com.semusings.user.service;

import com.semusings.twitter.model.Users;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;

@Service
public class UserService {
    private final Map<String, Users> users = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> followers = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> following = new ConcurrentHashMap<>();

    public Mono<Users> getUserById(String id) {
        Users user = users.get(id);
        return user != null ? Mono.just(user) : Mono.empty();
    }

    public Flux<Users> getAllUsers() {
        return Flux.fromIterable(users.values());
    }

    public Mono<Users> createUser(Users user) {
        users.put(user.id(), user);
        return Mono.just(user);
    }

    // Follow a user
    public Mono<Void> follow(String userId, String followerId) {
        followers.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(followerId);
        following.computeIfAbsent(followerId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        return Mono.empty();
    }

    // Get followers of a user
    public Flux<String> getFollowers(String userId) {
        return Flux.fromIterable(followers.getOrDefault(userId, Collections.emptySet()));
    }

    // Get users followed by a user
    public Flux<String> getFollowing(String userId) {
        return Flux.fromIterable(following.getOrDefault(userId, Collections.emptySet()));
    }
}
