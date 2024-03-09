package com.bartipablo.rest.query;

import java.util.concurrent.Callable;

public class ExternalQueryFeature implements Callable<String> {

    ExternalQuery externalQuery;

    public ExternalQueryFeature(ExternalQuery externalQuery) {
        this.externalQuery = externalQuery;
    }

    @Override
    public String call() {
        return externalQuery.getResponse();
    }
}
