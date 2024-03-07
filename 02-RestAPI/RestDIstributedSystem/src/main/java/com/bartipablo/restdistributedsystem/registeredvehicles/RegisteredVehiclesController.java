package com.bartipablo.restdistributedsystem.registeredvehicles;

import org.springframework.http.HttpStatus;
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

        try {
            UserArguments userArguments = new UserArguments(fromDate, toDate, voivodeship);
            RegisteredVehicles registeredVehicles = registeredVehiclesService.getRegisteredVehicles(userArguments);
            return new ResponseEntity<>(registeredVehicles, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
