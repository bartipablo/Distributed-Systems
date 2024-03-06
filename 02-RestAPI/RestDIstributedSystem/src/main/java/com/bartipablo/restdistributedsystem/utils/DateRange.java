package com.bartipablo.restdistributedsystem.utils;

import lombok.Getter;

import java.time.LocalDate;
import java.util.*;


@Getter
public record DateRange(LocalDate from, LocalDate to) {

    public long differenceInDays() {
        return to.toEpochDay() - from.toEpochDay() + 1;
    }

    public List<LocalDate> getDates() {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }


}
