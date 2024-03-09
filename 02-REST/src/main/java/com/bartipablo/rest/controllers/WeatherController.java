package com.bartipablo.rest.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @GetMapping("/current-weather-diff/{city}")
    public ResponseEntity<?> currentWeather(@PathVariable String city) {
        // TODO: implentation
        return null;
    }


    @GetMapping("/current-weather-diff-cities/")
    public ResponseEntity<?> futureWeather(
            @RequestParam(value = "city1", required = true, defaultValue = "") String city1,
            @RequestParam(value = "city2", required = true, defaultValue = "") String city2
    ) {
        // TODO: implemenation
        return null;
    }
}
