package com.bartipablo.rest.model;

public record Location(
    String name,
    String country,
    String latitude,
    String longitude
) {
}
