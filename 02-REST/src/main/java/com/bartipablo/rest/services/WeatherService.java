package com.bartipablo.rest.services;

import com.bartipablo.rest.dto.ResultDTO;
import com.bartipablo.rest.model.Location;
import com.bartipablo.rest.query.ExternalQuery;
import com.bartipablo.rest.query.ExternalQueryGenerator;
import com.bartipablo.rest.utils.InvalidCityName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WeatherService {

    ExecutorService es = Executors.newFixedThreadPool(2);

    @Autowired
    private ExternalQueryGenerator queryService;

    public ResultDTO getWeatherCurrDiff(String city) {
        Location cityLocation = getLocation(city);

        return null;
    }


    private Location getLocation(String city) {
        ExternalQuery cityLocationQuery = queryService.getCityLocationByMeteoSource(city);
        String response = cityLocationQuery.get();

        JSONArray responseArray = new JSONArray(response);

        if (responseArray.isEmpty()) {
            throw new InvalidCityName(city);
        }

        JSONObject cityJSON = responseArray.getJSONObject(0);

        return new Location(
                city,
                cityJSON.getString("lat"),
                cityJSON.getString("lon")
        );
    }



}
