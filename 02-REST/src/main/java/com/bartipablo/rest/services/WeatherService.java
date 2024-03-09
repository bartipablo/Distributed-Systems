package com.bartipablo.rest.services;

import com.bartipablo.rest.dto.ResultDTO;
import com.bartipablo.rest.model.Location;
import com.bartipablo.rest.query.ExternalQuery;
import com.bartipablo.rest.query.ExternalQueryGenerator;
import com.bartipablo.rest.utils.InvalidCityName;
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
        return null;
    }

    private Location getLocation(String city) throws Exception {
        ExternalQuery cityLocationQuery = queryService.getCityLocationByMeteoSource(city);
        JSONObject cityLocation = cityLocationQuery.get();

        if (cityLocation.isEmpty()) {
            throw new InvalidCityName("City not found");
        }

        // TODO: implementation
        return null;

    }



}
