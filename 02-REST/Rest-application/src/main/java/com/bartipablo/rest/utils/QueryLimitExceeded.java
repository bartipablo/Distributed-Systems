package com.bartipablo.rest.utils;

public class QueryLimitExceeded extends RuntimeException {

    public QueryLimitExceeded(String message) {
        super(message);
    }

}
