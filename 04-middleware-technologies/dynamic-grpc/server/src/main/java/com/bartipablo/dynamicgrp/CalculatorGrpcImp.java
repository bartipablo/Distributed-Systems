package com.bartipablo.dynamicgrp;

import com.calculator.grpc.AddRequest;
import com.calculator.grpc.AddResponse;
import com.calculator.grpc.CalculatorGrpc;
import com.calculator.grpc.SubRequest;
import com.calculator.grpc.SubResponse;
import com.calculator.grpc.MulRequest;
import com.calculator.grpc.MulResponse;
import com.calculator.grpc.DivRequest;
import com.calculator.grpc.DivResponse;
import com.calculator.grpc.SumRequest;
import com.calculator.grpc.SumResponse;
import com.calculator.grpc.PrimeNumbersResponse;
import com.calculator.grpc.PrimeNumbersRequest;
import io.grpc.stub.StreamObserver;

public class CalculatorGrpcImp extends CalculatorGrpc.CalculatorImplBase {

    @Override
    public void add(AddRequest request, StreamObserver<AddResponse> responseObserver) {
        int val = request.getAddend1() + request.getAddend2();
        AddResponse response = AddResponse.newBuilder().setSum(val).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sub(SubRequest request, StreamObserver<SubResponse> responseObserver) {
        int val = request.getMinuend() - request.getSubtrahend();
        SubResponse response = SubResponse.newBuilder().setDifference(val).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void mul(MulRequest request, StreamObserver<MulResponse> responseObserver) {
        int val = request.getMultiplicand() * request.getMultiplier();
        MulResponse response = MulResponse.newBuilder().setProduct(val).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void div(DivRequest request, StreamObserver<DivResponse> responseObserver) {
        int val = request.getDividend() / request.getDivisor();
        DivResponse response = com.calculator.grpc.DivResponse.newBuilder().setQuotient(val).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        int sum = request.getAddendsList().stream().mapToInt(Integer::intValue).sum();
        SumResponse response = SumResponse.newBuilder().setSum(sum).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumbers(PrimeNumbersRequest request, StreamObserver<PrimeNumbersResponse> responseObserver) {
        int start = request.getStart();
        int end = request.getEnd();
        PrimeNumbersResponse.Builder responseBuilder = PrimeNumbersResponse.newBuilder();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                responseBuilder.addPrimeNumbers(i);
            }
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
