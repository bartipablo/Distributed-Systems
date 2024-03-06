package com.bartipablo.restdistributedsystem.drivinglicens;

import com.bartipablo.restdistributedsystem.utils.DateRange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DrivingLicenseController {

    @GetMapping("/driving-license")
    public DrivingLicenses getDrivingLicenseInfo(DateRange dateRange) {
        // TODO: implement business logic and exception handling
        return null;
    }

}
