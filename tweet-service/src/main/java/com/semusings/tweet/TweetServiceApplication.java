package com.semusings.tweet;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class TweetServiceApplication {
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
