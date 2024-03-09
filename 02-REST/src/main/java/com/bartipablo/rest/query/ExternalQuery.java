package com.bartipablo.rest.query;

import org.springframework.web.client.RestTemplate;


public class ExternalQuery {

    String URL;

    public ExternalQuery(String URL) {
        this.URL = URL;
    }

    public String getResponse() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL, String.class);
    }

}
