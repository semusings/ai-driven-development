package com.semusings.tweet.api;

import com.semusings.tweet.service.TweetService;
import com.semusings.twitter.model.Tweets;
import io.quarkus.vertx.web.Route;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TweetRoutes {

    @Inject
    TweetService tweetService;

    @Route(path = "/api/tweets", methods = Route.HttpMethod.GET, produces = "application/json")
    void getAllTweets(RoutingContext rc) {
        tweetService.getAllTweets()
                .collect().asList()
                .subscribe().with(
                        tweets -> rc.response()
                                .putHeader("Content-Type", "application/json")
                                .end(Json.encode(tweets))
                );
    }

    @Route(path = "/api/tweets/:id", methods = Route.HttpMethod.GET, produces = "application/json")
    void getTweetById(RoutingContext rc) {
        String id = rc.pathParam("id");
        tweetService.getTweetById(id)
                .subscribe().with(
                        tweet -> {
                            if (tweet == null) {
                                rc.response().setStatusCode(404).end();
                            } else {
                                rc.response()
                                        .putHeader("Content-Type", "application/json")
                                        .end(Json.encode(tweet));
                            }
                        }
                );
    }

    @Route(path = "/api/tweets", methods = Route.HttpMethod.POST, consumes = "application/json", produces = "application/json")
    void createTweet(RoutingContext rc) {
        rc.request().bodyHandler(buffer -> {
            Tweets tweet = Json.decodeValue(buffer, Tweets.class);
            tweetService.createTweet(tweet)
                    .subscribe().with(
                            created -> rc.response()
                                    .putHeader("Content-Type", "application/json")
                                    .end(Json.encode(created))
                    );
        });
    }

    // Likes
    @Route(path = "/api/tweets/:tweetId/likes", methods = Route.HttpMethod.GET, produces = "application/json")
    void getLikes(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        tweetService.getLikes(tweetId)
            .subscribe().with(
                likes -> rc.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Json.encode(likes))
            );
    }

    @Route(path = "/api/tweets/:tweetId/like", methods = Route.HttpMethod.POST, consumes = "application/json")
    void likeTweet(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        rc.request().bodyHandler(buffer -> {
            String userId = buffer.toJsonObject().getString("userId");
            tweetService.likeTweet(tweetId, userId)
                .subscribe().with(
                    unused -> rc.response().setStatusCode(204).end()
                );
        });
    }

    // Retweets
    @Route(path = "/api/tweets/:tweetId/retweets", methods = Route.HttpMethod.GET, produces = "application/json")
    void getRetweets(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        tweetService.getRetweets(tweetId)
            .subscribe().with(
                retweets -> rc.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Json.encode(retweets))
            );
    }

    @Route(path = "/api/tweets/:tweetId/retweet", methods = Route.HttpMethod.POST, consumes = "application/json")
    void retweet(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        rc.request().bodyHandler(buffer -> {
            String userId = buffer.toJsonObject().getString("userId");
            tweetService.retweet(tweetId, userId)
                .subscribe().with(
                    unused -> rc.response().setStatusCode(204).end()
                );
        });
    }

    // Bookmarks
    @Route(path = "/api/tweets/:tweetId/bookmarks", methods = Route.HttpMethod.GET, produces = "application/json")
    void getBookmarks(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        tweetService.getBookmarks(tweetId)
            .subscribe().with(
                bookmarks -> rc.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Json.encode(bookmarks))
            );
    }

    @Route(path = "/api/tweets/:tweetId/bookmark", methods = Route.HttpMethod.POST, consumes = "application/json")
    void bookmark(RoutingContext rc) {
        String tweetId = rc.pathParam("tweetId");
        rc.request().bodyHandler(buffer -> {
            String userId = buffer.toJsonObject().getString("userId");
            tweetService.bookmark(tweetId, userId)
                .subscribe().with(
                    unused -> rc.response().setStatusCode(204).end()
                );
        });
    }
}
