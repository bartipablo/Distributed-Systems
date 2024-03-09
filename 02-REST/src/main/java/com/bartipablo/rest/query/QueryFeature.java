package com.bartipablo.rest.query;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class QueryFeature implements Callable<JSONObject> {

    ExternalQuery externalQuery;

    public QueryFeature(ExternalQuery externalQuery) {
        this.externalQuery = externalQuery;
    }
    @Override
    public JSONObject call() throws Exception {
        return externalQuery.get();
    }
}
