package com.bartipablo.restdistributedsystem.registeredvehicles;

import com.bartipablo.restdistributedsystem.registeredvehicles.model.Vehicle;
import com.bartipablo.restdistributedsystem.registeredvehicles.query.RegisteredVehicleResponse;
import com.bartipablo.restdistributedsystem.registeredvehicles.query.RegisteredVehiclesExternalQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisteredVehiclesService {

    public RegisteredVehiclesDTO getRegisteredVehicles(UserArguments userArgument)  {
        int threadQuantity = (int) userArgument.differenceInDays();
        ExecutorService es = Executors.newFixedThreadPool(Math.min(threadQuantity, 3));
        List<Future<RegisteredVehicleResponse>> futures = new ArrayList<>();

        List<LocalDate> dates = userArgument.getDates();
        for (LocalDate date : dates) {
            futures.add(es.submit(
                    new RegisteredVehiclesExternalQuery(
                            date,
                            date,
                            userArgument.getVoivodeship()

                    )));
        }

        return getResultsFromPromises(futures, dates);
    }

    private RegisteredVehiclesDTO getResultsFromPromises(List<Future<RegisteredVehicleResponse>> futures, List<LocalDate> dates) {
        Map<String, Integer> brandQuantity = new HashMap<>();
        Map<String, Integer> categoryQuantity = new HashMap<>();
        Map<String, Integer> fuelTypeQuantity = new HashMap<>();
        int totalWeight = 0;
        int totalEngineCapacity = 0;
        int vehiclesQuantity = 0;
        int maxEngineCapacity = 0;
        int maxWeight = 0;

        List<LocalDate> dateWithoutResults = new ArrayList<>(dates);

        for(Future<RegisteredVehicleResponse> future : futures) {
            try {
                RegisteredVehicleResponse registeredVehicles = future.get();
                if (registeredVehicles != null) {
                    for (Vehicle vehicle : registeredVehicles.getVehicles()) {
                        brandQuantity.merge(vehicle.brand(), 1, Integer::sum);
                        categoryQuantity.merge(vehicle.category(), 1, Integer::sum);
                        fuelTypeQuantity.merge(vehicle.fuelType(), 1, Integer::sum);
                        totalWeight += vehicle.weight();
                        totalEngineCapacity += vehicle.engineCapacity();
                        maxEngineCapacity = Math.max(maxEngineCapacity, vehicle.engineCapacity());
                        maxWeight = Math.max(maxWeight, vehicle.weight());
                        vehiclesQuantity++;
                    }
                }
                dateWithoutResults.remove(registeredVehicles.getFrom());
            } catch (Exception e) {
                System.err.println("Error during getting results from promises. Details: " + e.getMessage());
            }
        }

        int avgEngineCapacity;
        int avgWeight;
        if (vehiclesQuantity > 0) {
            avgEngineCapacity = totalEngineCapacity / vehiclesQuantity;
            avgWeight = totalWeight / vehiclesQuantity;
        } else {
            avgEngineCapacity = 0;
            avgWeight = 0;
        }

        return new RegisteredVehiclesDTO(
                vehiclesQuantity,
                avgWeight,
                avgEngineCapacity,
                maxEngineCapacity,
                maxWeight,
                brandQuantity,
                categoryQuantity,
                fuelTypeQuantity,
                dateWithoutResults
        );
    }

}
