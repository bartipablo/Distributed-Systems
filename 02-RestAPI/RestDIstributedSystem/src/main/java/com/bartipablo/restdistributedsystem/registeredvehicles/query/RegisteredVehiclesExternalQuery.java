package com.bartipablo.restdistributedsystem.registeredvehicles.query;

import com.bartipablo.restdistributedsystem.registeredvehicles.model.Vehicle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.concurrent.Callable;

public class RegisteredVehiclesExternalQuery implements Callable<RegisteredVehicleResponse> {

    private final int LIMIT = 500;

    private final String url;

    private final RegisteredVehicleResponse response;

    public RegisteredVehiclesExternalQuery(LocalDate fromDate, LocalDate toDate, String voivodeshipCode) {

        response = new RegisteredVehicleResponse(fromDate, toDate);

        String fromMonth = concatSingletonWithZero(fromDate.getMonthValue());
        String fromDay = concatSingletonWithZero(fromDate.getDayOfMonth());

        String toMonth = concatSingletonWithZero(toDate.getMonthValue());
        String toDay = concatSingletonWithZero(toDate.getDayOfMonth());

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
    public RegisteredVehicleResponse call() {

        RestTemplate restTemplate = new RestTemplate();

        try {
            System.out.println("GET URL: " + url);
            String result = restTemplate.getForObject(url, String.class);


            JSONObject resultJSON = new JSONObject(result);
            JSONArray data = resultJSON.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject vehicle = data.getJSONObject(i);

                    JSONObject vehicleDetails = vehicle.getJSONObject("attributes");

                    String brand = vehicleDetails.getString("marka");
                    String category = vehicleDetails.getString("rodzaj-pojazdu");
                    String fuel = vehicleDetails.getString("rodzaj-paliwa");
                    int weight = vehicleDetails.getInt("masa-wlasna");
                    int engineCapacity = vehicleDetails.getInt("pojemnosc-skokowa-silnika");

                    Vehicle vehicleRecord = new Vehicle(brand, category, fuel, weight, engineCapacity);
                    response.addVehicle(vehicleRecord);
                } catch (JSONException e) {
                    //ignore exception
                }
            }
        } catch (JSONException | RestClientException e) {
            throw e;
        }

        return response;
    }


    private String concatSingletonWithZero(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

}
