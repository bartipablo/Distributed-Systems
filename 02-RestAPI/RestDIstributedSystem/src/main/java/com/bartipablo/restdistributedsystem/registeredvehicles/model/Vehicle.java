package com.bartipablo.restdistributedsystem.registeredvehicles.model;

public record Vehicle (
        String brand,
        String category,
        String fuelType,
        int weight,
        int engineCapacity
){
}