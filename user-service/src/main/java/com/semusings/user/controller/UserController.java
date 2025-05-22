package com.semusings.user.controller;

import com.semusings.twitter.model.Users;
import com.semusings.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Mono<Users> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Flux<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Mono<Users> createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    // Follow a user
    @PostMapping("/{userId}/follow")
    public Mono<Void> followUser(@PathVariable String userId, @RequestBody Map<String, String> body) {
        String followerId = body.get("followerId");
        return userService.follow(userId, followerId);
    }

    // Get followers of a user
    @GetMapping("/{userId}/followers")
    public Flux<String> getFollowers(@PathVariable String userId) {
        return userService.getFollowers(userId);
    }

    // Get users followed by a user
    @GetMapping("/{userId}/following")
    public Flux<String> getFollowing(@PathVariable String userId) {
        return userService.getFollowing(userId);
    }
}
