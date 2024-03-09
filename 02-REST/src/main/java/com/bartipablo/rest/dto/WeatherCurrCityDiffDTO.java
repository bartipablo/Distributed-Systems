package com.bartipablo.rest.dto;

import com.bartipablo.rest.model.WeatherForecast;
import com.bartipablo.rest.model.Location;

public record WeatherCurrCityDiffDTO (

        String sourceApi,

        Location locationA,

        WeatherForecast weatherForecastAtLocationA,

        Location locationB,

        WeatherForecast weatherForecastAtLocationB
){
}
