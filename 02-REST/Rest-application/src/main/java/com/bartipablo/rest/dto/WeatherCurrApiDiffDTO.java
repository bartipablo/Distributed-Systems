package com.bartipablo.rest.dto;

import com.bartipablo.rest.model.WeatherForecast;
import com.bartipablo.rest.model.Location;

public record WeatherCurrApiDiffDTO (
        Location location,

        WeatherForecast weatherForecastByApiA,

        WeatherForecast weatherForecastByApiB,

        //temperature analysis------------------
        double avgTemperature,

        double absTemperatureDifference,

        double relTemperatureDifference,
        //temperature analysis------------------


        //wind speed analysis-------------------
        double avgWindSpeed,

        double absWindSpeedDifference,

        double relWindSpeedDifference,
        //wind speed analysis-------------------

        //cloud cover analysis------------------
        double avgCloudCover,

        double absCloudCoverDifference,

        double relCloudCoverDifference
        //cloud cover analysis------------------

){
}
