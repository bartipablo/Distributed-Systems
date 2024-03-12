package com.bartipablo.rest.query;


import com.bartipablo.rest.utils.UnauthorizedRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:api.properties")
public class Validation {

    @Value("${application.api.key}")
    private String applicationKey;

    public void validate(String userKey) {
        if (!applicationKey.equals(userKey)) throw new UnauthorizedRequest("Invalid query key");
    }
}
