package com.bartipablo.restdistributedsystem.registeredvehicles.query;

import com.bartipablo.restdistributedsystem.registeredvehicles.RegisteredVehicles;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cglib.core.Local;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.Callable;

public class RegisteredVehiclesExternalQuery implements Callable<RegisteredVehicles> {

    private final int LIMIT = 500;

    private final String url;

    public RegisteredVehiclesExternalQuery(LocalDate fromDate, LocalDate toDate, String voivodeshipCode) {

        String fromMonth = fromDate.getMonthValue() < 10 ? "0" + fromDate.getMonthValue() : String.valueOf(fromDate.getMonthValue());
        String fromDay = fromDate.getDayOfMonth() < 10 ? "0" + fromDate.getDayOfMonth() : String.valueOf(fromDate.getDayOfMonth());

        String toMonth = toDate.getMonthValue() < 10 ? "0" + toDate.getMonthValue() : String.valueOf(toDate.getMonthValue());
        String toDay = toDate.getDayOfMonth() < 10 ? "0" + toDate.getDayOfMonth() : String.valueOf(toDate.getDayOfMonth());

        this.url = "https://api.cepik.gov.pl/pojazdy?wojewodztwo=" +
                voivodeshipCode +
                "&data-od=" +
                fromDate.getYear() +
                fromMonth +
                fromDay +
                "&data-do=" +
                toDate.getYear() +
                toMonth +
                toDay +
                "&typ-daty=1&tylko-zarejestrowane=true&pokaz-wszystkie-pola=false&limit=" +
                LIMIT +
                "&page=1";
    }

    @Override
    public RegisteredVehicles call() {

        RegisteredVehicles registeredVehicles = new RegisteredVehicles();

        RestTemplate restTemplate = new RestTemplate();

        try {
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
        } catch (JSONException e) {
            //ignore exception
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }

        return registeredVehicles;
    }

}
