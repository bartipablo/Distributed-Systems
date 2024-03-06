package com.bartipablo.restdistributedsystem.registeredvehicles;

import com.bartipablo.restdistributedsystem.utils.DateRange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisteredVehiclesController {

    @GetMapping("/registered-vehicles")
    public RegisteredVehicles getRegisteredVehiclesInfo(DateRange dateRange) {
        //TODO: implement business logic and exception handling
        return null;
    }
}
