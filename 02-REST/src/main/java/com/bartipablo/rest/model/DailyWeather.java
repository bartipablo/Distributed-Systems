package com.bartipablo.rest.model;

import java.time.LocalDate;

public record DailyWeather (
        LocalDate date,

        double temperature,

        double windSpeed,

        Direction windDirection,

        String description
) {
}
