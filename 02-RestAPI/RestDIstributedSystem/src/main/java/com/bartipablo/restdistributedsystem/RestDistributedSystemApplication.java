package com.bartipablo.restdistributedsystem;

import com.bartipablo.restdistributedsystem.registeredvehicles.query.RegisteredVehiclesQuery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestDistributedSystemApplication {

    public static void main(String[] args) {
        //SpringApplication.run(RestDistributedSystemApplication.class, args);
        RegisteredVehiclesQuery registeredVehiclesQuery = new RegisteredVehiclesQuery();
        System.out.println(registeredVehiclesQuery.call());
    }

}
