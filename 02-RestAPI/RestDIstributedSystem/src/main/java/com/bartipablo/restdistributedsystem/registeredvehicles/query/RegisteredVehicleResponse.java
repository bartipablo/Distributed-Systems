package com.bartipablo.restdistributedsystem.registeredvehicles.query;

import com.bartipablo.restdistributedsystem.registeredvehicles.model.Vehicle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegisteredVehicleResponse {

    private final LocalDate from;

    private final LocalDate to;

    private final List<Vehicle> vehicles = new ArrayList<>();

    public RegisteredVehicleResponse(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
