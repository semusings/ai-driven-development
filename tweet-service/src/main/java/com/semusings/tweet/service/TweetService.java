package com.semusings.tweet.service;

import com.semusings.twitter.model.Tweets;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TweetService {

    private final Map<String, Tweets> tweets = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> likes = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> retweets = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> bookmarks = new ConcurrentHashMap<>();

    public Uni<Tweets> getTweetById(String id) {
        Tweets tweet = tweets.get(id);
        return tweet != null ? Uni.createFrom().item(tweet) : Uni.createFrom().nullItem();
    }

    public Multi<Tweets> getAllTweets() {
        return Multi.createFrom().iterable(tweets.values());
    }

    public Uni<Tweets> createTweet(Tweets tweet) {
        tweets.put(tweet.id(), tweet);
        return Uni.createFrom().item(tweet);
    }

    // Likes
    public Uni<Void> likeTweet(String tweetId, String userId) {
        likes.computeIfAbsent(tweetId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        return Uni.createFrom().voidItem();
    }

    public Uni<Set<String>> getLikes(String tweetId) {
        return Uni.createFrom().item(likes.getOrDefault(tweetId, Collections.emptySet()));
    }

    // Retweets
    public Uni<Void> retweet(String tweetId, String userId) {
        retweets.computeIfAbsent(tweetId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        return Uni.createFrom().voidItem();
    }

    public Uni<Set<String>> getRetweets(String tweetId) {
        return Uni.createFrom().item(retweets.getOrDefault(tweetId, Collections.emptySet()));
    }

    // Bookmarks
    public Uni<Void> bookmark(String tweetId, String userId) {
        bookmarks.computeIfAbsent(tweetId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        return Uni.createFrom().voidItem();
    }

    public Uni<Set<String>> getBookmarks(String tweetId) {
        return Uni.createFrom().item(bookmarks.getOrDefault(tweetId, Collections.emptySet()));
    }
}
