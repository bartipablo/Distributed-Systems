package com.bartipablo.restdistributedsystem.registeredvehicles;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RegisteredVehicles {

    private int totalVehicles = 0;

    private final Map<String, Integer> registeredVehiclesByBrand = new HashMap<>();

    private final Map<String, Integer> registeredVehiclesByCategory = new HashMap<>();

    private final Map<String, Integer> registeredVehiclesByFuelType = new HashMap<>();

    private int totalWeight = 0;

    private int totalEngineCapacity = 0;

    public void addVehicle(String brand, String category, String fuel, int weight, int engineCapacity) {
        totalVehicles++;
        registeredVehiclesByBrand.put(brand, registeredVehiclesByBrand.getOrDefault(brand, 0) + 1);
        registeredVehiclesByCategory.put(category, registeredVehiclesByCategory.getOrDefault(category, 0) + 1);
        registeredVehiclesByFuelType.put(fuel, registeredVehiclesByFuelType.getOrDefault(fuel, 0) + 1);
        totalWeight += weight;
        totalEngineCapacity += engineCapacity;
    }

}
