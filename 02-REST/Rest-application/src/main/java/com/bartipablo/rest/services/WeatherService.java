package com.bartipablo.rest.services;

import com.bartipablo.rest.dto.WeatherCurrApiDiffDTO;
import com.bartipablo.rest.dto.WeatherCurrCityDiffDTO;
import com.bartipablo.rest.model.WeatherForecast;
import com.bartipablo.rest.model.Location;
import com.bartipablo.rest.query.ExternalQueryGenerator;
import com.bartipablo.rest.query.ExternalQueryFeature;
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
                new ExternalQueryFeature(queryService.getCityLocationByMeteoSource(city))
        );

        Location cityLocation = readLocationFromResponse(cityLocationFuture.get());

        Future<String> weatherByWeatherApiFuture = es.submit(
                new ExternalQueryFeature(queryService.getWeatherByWeatherApi(cityLocation))
        );

        Future<String> weatherByMeteoSourceFuture = es.submit(
                new ExternalQueryFeature(queryService.getWeatherByMeteoSource(cityLocation))
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
        double relTemperatureDifference = avgTemperature != 0 ? (absTemperatureDifference / avgTemperature) * 100 : 0;

        double avgWindSpeed = (weatherForecastA.windSpeed() + weatherForecastB.windSpeed()) / 2;
        double absWindSpeedDifference = Math.abs(weatherForecastA.windSpeed() - weatherForecastB.windSpeed());
        double relWindSpeedDifference = avgWindSpeed != 0 ? (absWindSpeedDifference / avgWindSpeed) * 100 : 0;

        double avgCloudCover = (1.0 * weatherForecastA.cloudCover() + weatherForecastB.cloudCover()) / 2;
        double absCloudCoverDifference = Math.abs(weatherForecastA.cloudCover() - weatherForecastB.cloudCover());
        double relCloudCoverDifference = avgCloudCover != 0 ? (absCloudCoverDifference / avgCloudCover) * 100 : 0;

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


    public WeatherCurrCityDiffDTO getWeatherCurrCityDiff(String cityA, String cityB) throws ExecutionException, InterruptedException {

        Future<String> cityALocationFuture = es.submit(
                new ExternalQueryFeature(queryService.getCityLocationByMeteoSource(cityA))
        );

        Future<String> cityBLocationFuture = es.submit(
                new ExternalQueryFeature(queryService.getCityLocationByMeteoSource(cityB))
        );

        Location cityLocationA = readLocationFromResponse(cityALocationFuture.get());
        Location cityLocationB = readLocationFromResponse(cityBLocationFuture.get());

        Future<String> cityAWeatherFuture = es.submit(
                new ExternalQueryFeature(queryService.getWeatherByWeatherApi(cityLocationA))
        );

        Future<String> cityBWeatherFuture = es.submit(
                new ExternalQueryFeature(queryService.getWeatherByWeatherApi(cityLocationB))
        );

        WeatherForecast cityWeatherA = readWeatherFromWeatherApiResponse(cityAWeatherFuture.get());
        WeatherForecast cityWeatherB = readWeatherFromWeatherApiResponse(cityBWeatherFuture.get());

        return analyseDataFromDiffCity(cityLocationA, cityLocationB, cityWeatherA, cityWeatherB);
    }

    private WeatherCurrCityDiffDTO analyseDataFromDiffCity(
            Location locationA,
            Location locationB,
            WeatherForecast weatherForecastA,
            WeatherForecast weatherForecastB) {

        double avgTemperature = (weatherForecastA.temperature() + weatherForecastB.temperature()) / 2;
        double absTemperatureDifference = Math.abs(weatherForecastA.temperature() - weatherForecastB.temperature());
        double relTemperatureDifference = avgTemperature != 0 ? (absTemperatureDifference / avgTemperature) * 100 : 0;

        double avgWindSpeed = (weatherForecastA.windSpeed() + weatherForecastB.windSpeed()) / 2;
        double absWindSpeedDifference = Math.abs(weatherForecastA.windSpeed() - weatherForecastB.windSpeed());
        double relWindSpeedDifference = avgWindSpeed != 0 ? (absWindSpeedDifference / avgWindSpeed) * 100 : 0;

        double avgCloudCover = (1.0 * weatherForecastA.cloudCover() + weatherForecastB.cloudCover()) / 2;
        double absCloudCoverDifference = Math.abs(weatherForecastA.cloudCover() - weatherForecastB.cloudCover());
        double relCloudCoverDifference = avgCloudCover != 0 ? (absCloudCoverDifference / avgCloudCover) * 100 : 0;

        return new WeatherCurrCityDiffDTO(
                locationA,
                weatherForecastA,
                locationB,
                weatherForecastB,
                Utils.round(absTemperatureDifference, 2),
                Utils.round(relTemperatureDifference, 2),
                Utils.round(absWindSpeedDifference, 2),
                Utils.round(relWindSpeedDifference, 2),
                Utils.round(absCloudCoverDifference, 2),
                Utils.round(relCloudCoverDifference, 2)
        );
    }
}
