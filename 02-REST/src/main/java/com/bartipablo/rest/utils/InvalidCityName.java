package com.bartipablo.rest.utils;

public class InvalidCityName extends RuntimeException {
    public InvalidCityName(String message) {
        super(message);
    }
}
