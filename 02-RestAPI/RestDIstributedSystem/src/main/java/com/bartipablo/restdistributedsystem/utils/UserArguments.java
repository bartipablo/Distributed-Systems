package com.bartipablo.restdistributedsystem.utils;

import jakarta.validation.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import jakarta.validation.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
public class UserArguments {

    private final LocalDate from;

    private final LocalDate to;

    @Pattern(regexp = "^(0[2468]|1[02468]|2[02468]|3[02])$")
    private final String voivodeship;


    public UserArguments(String fromDate, String toDate, String voivodeship) {
        this.from = LocalDate.parse(fromDate);
        this.to = LocalDate.parse(toDate);
        this.voivodeship = voivodeship;

        validateUserArguments();
    }

    private void validateUserArguments() {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        if (to.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("To date cannot be in the future");
        }

        if (from.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("From date cannot be before 1900-01-01");
        }

        if (differenceInDays() > 366) {
            throw new IllegalArgumentException("Difference in days cannot be greater than 366");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserArguments>> errors = validator.validate(this);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid voivodeship arguments");
        }
    }


    public long differenceInDays() {
        return to.toEpochDay() - from.toEpochDay() + 1;
    }

    public List<String> getDates() {

        List<String> dates = new ArrayList<>();
        for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
            dates.add(date.toString());
        }
        return dates;
    }
}
