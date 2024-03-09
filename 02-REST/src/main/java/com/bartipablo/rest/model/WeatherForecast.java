package com.bartipablo.rest.model;


public record WeatherForecast(

        double temperature,

        double windSpeed,

        int cloudCover,

        String windDirection,

        String summary,

        String apiSource
) {
}
