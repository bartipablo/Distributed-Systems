package com.bartipablo.restdistributedsystem.registeredvehicles;

import org.springframework.beans.propertyeditors.LocaleEditor;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public record RegisteredVehiclesDTO (
        int totalVehicles,

        int avgWeight,

        int avgEngineCapacity,

        int maxEngineCapacity,

        int maxWeight,

        Map<String, Integer> registeredVehiclesByBrand,

        Map<String, Integer> registeredVehiclesByCategory,

        Map<String, Integer> registeredVehiclesByFuelType,

        List<LocalDate> dateWithoutResults
) {
}
