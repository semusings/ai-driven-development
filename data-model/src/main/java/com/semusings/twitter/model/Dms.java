package com.semusings.twitter.model;

public record Dms(
    String id,
    String senderId,
    String receiverId,
    String content
) {}
