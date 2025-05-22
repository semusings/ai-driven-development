package com.semusings.twitter.model;

import java.time.Instant;

public record Tweets(
    String id,
    String authorId,
    String content,
    Instant createdAt
) {}
