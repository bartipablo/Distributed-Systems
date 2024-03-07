package com.bartipablo.restdistributedsystem.registeredvehicles;

import com.bartipablo.restdistributedsystem.registeredvehicles.query.RegisteredVehiclesExternalQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisteredVehiclesService {

    public RegisteredVehicles getRegisteredVehicles(UserArguments userArgument) throws ExecutionException, InterruptedException {
        int threadQuantity = (int) userArgument.differenceInDays();
        ExecutorService es = Executors.newFixedThreadPool(threadQuantity);
        List<Future<RegisteredVehicles>> futures = new ArrayList<>();

        List<LocalDate> dates = userArgument.getDates();
        for (LocalDate date : dates) {
            futures.add(es.submit(
                    new RegisteredVehiclesExternalQuery(
                            date,
                            date,
                            userArgument.getVoivodeship()

                    )));
        }

        RegisteredVehicles registeredVehiclesResult = new RegisteredVehicles();

        for (Future<RegisteredVehicles> future : futures) {
            try {
                RegisteredVehicles registeredVehicles = future.get();
                if (registeredVehicles != null) {
                    registeredVehiclesResult.addVehicle(registeredVehicles);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return registeredVehiclesResult;
    }

}
