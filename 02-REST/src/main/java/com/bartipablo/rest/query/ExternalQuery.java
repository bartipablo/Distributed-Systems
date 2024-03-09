package com.bartipablo.rest.query;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;


public class ExternalQuery {

    String URL;

    public ExternalQuery(String URL) {
        this.URL = URL;
    }

    public String get() {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("GET URL: " + URL);
        return restTemplate.getForObject(URL, String.class);
    }

}
