package com.bartipablo.rest.utils;

public class UnauthorizedRequest extends RuntimeException {

    public UnauthorizedRequest(String message) {
        super(message);
    }

}
