package com.bartipablo.restdistributedsystem.registeredvehicles.query;

import com.bartipablo.restdistributedsystem.registeredvehicles.RegisteredVehicles;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.concurrent.Callable;

public class RegisteredVehiclesQuery implements Callable<RegisteredVehicles> {

    Date date;

    @Override
    public RegisteredVehicles call() {

        RegisteredVehicles registeredVehicles = new RegisteredVehicles();

        String url = "https://api.cepik.gov.pl/pojazdy?wojewodztwo=02&data-od=20210303&data-do=20210303&typ-daty=1&tylko-zarejestrowane=true&pokaz-wszystkie-pola=false&limit=10&page=1";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        JSONObject resultJSON = new JSONObject(result);
        JSONArray data = resultJSON.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject vehicle = data.getJSONObject(i);
            JSONObject vehicleDetails = vehicle.getJSONObject("attributes");

            String brand = vehicleDetails.getString("marka");
            String category = vehicleDetails.getString("rodzaj-pojazdu");
            String fuel = vehicleDetails.getString("rodzaj-paliwa");
            int weight = vehicleDetails.getInt("masa-wlasna");
            int engineCapacity = vehicleDetails.getInt("pojemnosc-skokowa-silnika");

            registeredVehicles.addVehicle(brand, category, fuel, weight, engineCapacity);
        }

        return registeredVehicles;
    }



}
