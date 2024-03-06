package com.bartipablo.restdistributedsystem.registeredvehicles;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisteredVehiclesController {

    RegisteredVehiclesService registeredVehiclesService = new RegisteredVehiclesService();

    @GetMapping("/registered-vehicles")
    public ResponseEntity<RegisteredVehicles> getRegisteredVehiclesInfo(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate,
            @RequestParam(value = "voivodeship", required = false) String voivodeship
    ) {




        //TODO: implement business logic and exception handling
        return null;
    }
}
