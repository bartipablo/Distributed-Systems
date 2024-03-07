package com.bartipablo.restdistributedsystem;

import com.bartipablo.restdistributedsystem.registeredvehicles.RegisteredVehicles;
import com.bartipablo.restdistributedsystem.utils.UserArguments;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
public class RestDistributedSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestDistributedSystemApplication.class, args);

    }

}
