package com.bartipablo.rest.controllers;


import com.bartipablo.rest.query.Validation;
import com.bartipablo.rest.services.WeatherService;
import com.bartipablo.rest.utils.InvalidCityName;
import com.bartipablo.rest.utils.UnauthorizedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    Validation validation;

    @GetMapping("/current-weather-diff-api/{city}")
    public ResponseEntity<?> currentWeather(
            @PathVariable String city,
            @RequestParam(value = "key", required = true, defaultValue = "") String key
    ) {
        try {
            validation.validate(key);
            var weatherApiDiff = weatherService.getWeatherCurrApiDiff(city);
            return ResponseEntity.ok(weatherApiDiff);
        } catch (InvalidCityName | UnauthorizedRequest badRequest) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(badRequest.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @GetMapping("/current-weather-diff-cities/")
    public ResponseEntity<?> futureWeather(
            @RequestParam(value = "key", required = true, defaultValue = "") String key,
            @RequestParam(value = "city1", required = true, defaultValue = "") String city1,
            @RequestParam(value = "city2", required = true, defaultValue = "") String city2
    ) {
        try {
            validation.validate(key);
            var weatherCityDiff = weatherService.getWeatherCurrCityDiff(city1, city2);
            return ResponseEntity.ok(weatherCityDiff);
        } catch (InvalidCityName | UnauthorizedRequest badRequest) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(badRequest.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
