package com.bartipablo.rest.query;

import com.bartipablo.rest.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:api.properties")
public class ExternalQueryGenerator {

    /* *** MeteoSource *** */
    private final String MeteoSourceWeatherURL = "https://www.meteosource.com/api/v1/free/point";

    private final String MeteoSourcePlaceURL = "https://www.meteosource.com/api/v1/free/find_places";

    @Value("${meteosource.api.key}")
    private String MeteoSourceKey;
    /* *** MeteoSource *** */


    /* *** WeatherApi *** */
    private final String WeatherApiURL = "https://api.weatherapi.com/v1/forecast.json";

    @Value("${weatherapi.api.key}")
    private String WeatherApiKey;
    /* *** WeatherApi *** */


    public ExternalQuery getCityLocationByMeteoSource(String city) {
        return new ExternalQuery(
                MeteoSourcePlaceURL +
                        "?text=" + city +
                        "&key=" + MeteoSourceKey +
                        "&language=en"
        );
    }

    public ExternalQuery getWeatherByMeteoSource(Location location) {
        return new ExternalQuery(
                MeteoSourceWeatherURL +
                        "?lat=" + location.latitude() +
                        "&lon=" + location.longitude() +
                        "&key=" + MeteoSourceKey +
                        "&language=en&sections=current%2Chourly&language=en&units=auto"

        );
    }

    public ExternalQuery getWeatherByWeatherApi(Location location) {
        return new ExternalQuery(
                WeatherApiURL +
                        "?key=" + WeatherApiKey +
                        "&q=" + location.latitude() + "%2C" + location.longitude() +
                        "&days=1"
        );
    }


}
