package com.bartipablo.restdistributedsystem.registeredvehicles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisteredVehiclesController {

    RegisteredVehiclesService registeredVehiclesService = new RegisteredVehiclesService();

    @GetMapping("/registered-vehicles")
    public ResponseEntity<?> getRegisteredVehiclesInfo(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate,
            @RequestParam(value = "voivodeship", required = false) String voivodeship
    ) {

        try {
            UserArguments userArguments = new UserArguments(fromDate, toDate, voivodeship);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(registeredVehiclesService.getRegisteredVehicles(userArguments));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }
}
