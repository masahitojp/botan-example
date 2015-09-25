package com.github.masahitojp.botan.utils;

public class RegexTestPattern {
    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public int getTimes() {
        return times;
    }

    private final String description;
    private final String message;
    private final int times;

    public RegexTestPattern(String description, String message, int times) {
        this.description = description;
        this.message = message;
        this.times = times;
    }
}
