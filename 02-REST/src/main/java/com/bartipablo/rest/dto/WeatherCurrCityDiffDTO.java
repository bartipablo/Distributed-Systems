package com.bartipablo.rest.dto;

import com.bartipablo.rest.model.WeatherForecast;
import com.bartipablo.rest.model.Location;

public record WeatherCurrCityDiffDTO (

        Location locationA,

        WeatherForecast weatherForecastAtLocationA,

        Location locationB,

        WeatherForecast weatherForecastAtLocationB,

        //temperature analysis------------------
        double absTemperatureDifference,

        double relTemperatureDifference,
        //temperature analysis------------------


        //wind speed analysis-------------------
        double absWindSpeedDifference,

        double relWindSpeedDifference,
        //wind speed analysis-------------------

        //cloud cover analysis------------------
        double absCloudCoverDifference,

        double relCloudCoverDifference
        //cloud cover analysis------------------
){
}
