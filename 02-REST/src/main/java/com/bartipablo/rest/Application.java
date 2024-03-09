package com.bartipablo.rest;

import com.bartipablo.rest.query.ExternalQueryGenerator;
import com.bartipablo.rest.services.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);

        WeatherService weatherService = context.getBean(WeatherService.class);
        System.out.println("elo");

    }

}
