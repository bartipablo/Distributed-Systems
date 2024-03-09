package com.bartipablo.rest.query;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class QueryFeature implements Callable<String> {

    ExternalQuery externalQuery;

    public QueryFeature(ExternalQuery externalQuery) {
        this.externalQuery = externalQuery;
    }
    @Override
    public String call() {
        return externalQuery.get();
    }
}
