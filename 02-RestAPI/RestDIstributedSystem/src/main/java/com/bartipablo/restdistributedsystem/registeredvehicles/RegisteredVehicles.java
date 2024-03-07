package com.bartipablo.restdistributedsystem.registeredvehicles;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RegisteredVehicles {

    private int totalVehicles;

    private final Map<String, Integer> registeredVehiclesByBrand;

    private final Map<String, Integer> registeredVehiclesByCategory;

    private final Map<String, Integer> registeredVehiclesByFuelType;

    private int totalWeight;

    private int maxWeight;

    private int totalEngineCapacity;

    private int maxEngineCapacity;

    public RegisteredVehicles() {
        registeredVehiclesByBrand = new HashMap<>();
        registeredVehiclesByCategory = new HashMap<>();
        registeredVehiclesByFuelType = new HashMap<>();
        totalVehicles = 0;
        totalWeight = 0;
        totalEngineCapacity = 0;
        maxWeight = 0;
        maxEngineCapacity = 0;
    }

    public void addVehicle(String brand, String category, String fuel, int weight, int engineCapacity) {
        totalVehicles++;
        registeredVehiclesByBrand.put(brand, registeredVehiclesByBrand.getOrDefault(brand, 0) + 1);
        registeredVehiclesByCategory.put(category, registeredVehiclesByCategory.getOrDefault(category, 0) + 1);
        registeredVehiclesByFuelType.put(fuel, registeredVehiclesByFuelType.getOrDefault(fuel, 0) + 1);
        totalWeight += weight;
        totalEngineCapacity += engineCapacity;
        maxWeight = Math.max(maxWeight, weight);
        maxEngineCapacity = Math.max(maxEngineCapacity, engineCapacity);
    }

    public void addVehicle(RegisteredVehicles vehicle) {
        totalVehicles += vehicle.totalVehicles;
        vehicle.registeredVehiclesByBrand.forEach((brand, count) -> registeredVehiclesByBrand.put(brand, registeredVehiclesByBrand.getOrDefault(brand, 0) + count));
        vehicle.registeredVehiclesByCategory.forEach((category, count) -> registeredVehiclesByCategory.put(category, registeredVehiclesByCategory.getOrDefault(category, 0) + count));
        vehicle.registeredVehiclesByFuelType.forEach((fuel, count) -> registeredVehiclesByFuelType.put(fuel, registeredVehiclesByFuelType.getOrDefault(fuel, 0) + count));
        totalWeight += vehicle.totalWeight;
        totalEngineCapacity += vehicle.totalEngineCapacity;
        maxWeight = Math.max(maxWeight, vehicle.maxWeight);
        maxEngineCapacity = Math.max(maxEngineCapacity, vehicle.maxEngineCapacity);
    }

}
