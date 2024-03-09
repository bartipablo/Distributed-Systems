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
                        "&language=en&sections=current&language=en&units=metric"

        );
    }

    public ExternalQuery getWeatherByWeatherApi(Location location) {
        return new ExternalQuery(
                WeatherApiURL +
                        "?key=" + WeatherApiKey +    //substring, because weather api don't read direction.
                        "&q=" + location.latitude().substring(0, location.latitude().length() - 1)
                        + "," + location.longitude().substring(0, location.longitude().length() - 1) +
                        "&days=1"
        );
    }


}
