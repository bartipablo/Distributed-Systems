package com.bartipablo.restdistributedsystem.registeredvehicles;

import java.util.Map;

public record RegisteredVehiclesDTO (
        int totalVehicles,

        int totalWeight,

        int maxWeight,

        int totalEngineCapacity,

        int maxEngineCapacity,

        Map<String, Integer> registeredVehiclesByBrand,

        Map<String, Integer> registeredVehiclesByCategory
) {
}
