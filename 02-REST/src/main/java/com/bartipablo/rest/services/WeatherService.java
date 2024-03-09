package com.bartipablo.rest.services;

import com.bartipablo.rest.dto.WeatherCurrApiDiffDTO;
import com.bartipablo.rest.dto.WeatherCurrCityDiffDTO;
import com.bartipablo.rest.model.WeatherForecast;
import com.bartipablo.rest.model.Location;
import com.bartipablo.rest.query.ExternalQueryGenerator;
import com.bartipablo.rest.query.QueryFeature;
import com.bartipablo.rest.utils.InvalidCityName;
import com.bartipablo.rest.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class WeatherService {

    ExecutorService es = Executors.newFixedThreadPool(2);

    @Autowired
    private ExternalQueryGenerator queryService;

    public WeatherCurrApiDiffDTO getWeatherCurrApiDiff(String city) throws ExecutionException, InterruptedException {

        Future<String> cityLocationFuture = es.submit(
                new QueryFeature(queryService.getCityLocationByMeteoSource(city))
        );

        Location cityLocation = readLocationFromResponse(cityLocationFuture.get());

        Future<String> weatherByWeatherApiFuture = es.submit(
                new QueryFeature(queryService.getWeatherByWeatherApi(cityLocation))
        );

        Future<String> weatherByMeteoSourceFuture = es.submit(
                new QueryFeature(queryService.getWeatherByMeteoSource(cityLocation))
        );


        WeatherForecast weatherByWeatherApi = readWeatherFromWeatherApiResponse(weatherByWeatherApiFuture.get());
        WeatherForecast weatherByMeteoSource = readWeatherFromMeteSourceResponse(weatherByMeteoSourceFuture.get());

        return analyseDataFromDiffApi(cityLocation, weatherByWeatherApi, weatherByMeteoSource);
    }

    private WeatherForecast readWeatherFromWeatherApiResponse(String response) {
        JSONObject fullResponse = new JSONObject(response);
        JSONObject currentWeather = fullResponse.getJSONObject("current");

        double temperature = currentWeather.getDouble("temp_c");
        double windSpeed = currentWeather.getDouble("wind_kph");
        int cloudCover = currentWeather.getInt("cloud");
        String windDirection = currentWeather.getString("wind_dir");
        String summary = currentWeather.getJSONObject("condition").getString("text");

        return new WeatherForecast(
                Utils.round(temperature, 2),
                Utils.round(windSpeed, 2),
                cloudCover,
                windDirection,
                summary,
                "Weather API"
        );

    }

    private WeatherForecast readWeatherFromMeteSourceResponse(String response) {
        JSONObject fullResponse = new JSONObject(response);
        JSONObject currentWeather = fullResponse.getJSONObject("current");

        double temperature = currentWeather.getDouble("temperature");
        double windSpeed = currentWeather
                .getJSONObject("wind")
                .getDouble("speed") * 3.6;  // conversion to kph
        int cloudCover = currentWeather.getInt("cloud_cover");
        String windDirection = currentWeather
                .getJSONObject("wind")
                .getString("dir");
        String summary = currentWeather.getString("summary");

        return new WeatherForecast(
                Utils.round(temperature, 2),
                Utils.round(windSpeed, 2),
                cloudCover,
                windDirection,
                summary,
                "Meteo Source"
        );
    }


    private Location readLocationFromResponse(String response) {
        JSONArray responseArray = new JSONArray(response);

        if (responseArray.isEmpty()) {
            throw new InvalidCityName(response);
        }

        JSONObject cityJSON = responseArray.getJSONObject(0);

        return new Location(
                cityJSON.getString("name"),
                cityJSON.getString("country"),
                cityJSON.getString("lat"),
                cityJSON.getString("lon")
        );
    }

    private WeatherCurrApiDiffDTO analyseDataFromDiffApi(
            Location location,
            WeatherForecast weatherForecastA,
            WeatherForecast weatherForecastB) {

        double avgTemperature = (weatherForecastA.temperature() + weatherForecastB.temperature()) / 2;
        double absTemperatureDifference = Math.abs(weatherForecastA.temperature() - weatherForecastB.temperature());
        double relTemperatureDifference = (absTemperatureDifference / avgTemperature) * 100;

        double avgWindSpeed = (weatherForecastA.windSpeed() + weatherForecastB.windSpeed()) / 2;
        double absWindSpeedDifference = Math.abs(weatherForecastA.windSpeed() - weatherForecastB.windSpeed());
        double relWindSpeedDifference = (absWindSpeedDifference / avgWindSpeed) * 100;

        double avgCloudCover = (1.0 * weatherForecastA.cloudCover() + weatherForecastB.cloudCover()) / 2;
        double absCloudCoverDifference = Math.abs(weatherForecastA.cloudCover() - weatherForecastB.cloudCover());
        double relCloudCoverDifference = (absCloudCoverDifference / avgCloudCover) * 100;

        return new WeatherCurrApiDiffDTO(
                location,
                weatherForecastA,
                weatherForecastB,
                Utils.round(avgTemperature, 2),
                Utils.round(absTemperatureDifference, 2),
                Utils.round(relTemperatureDifference, 2),
                Utils.round(avgWindSpeed, 2),
                Utils.round(absWindSpeedDifference, 2),
                Utils.round(relWindSpeedDifference, 2),
                Utils.round(avgCloudCover, 2),
                Utils.round(absCloudCoverDifference, 2),
                Utils.round(relCloudCoverDifference, 2)
        );
    }




}
