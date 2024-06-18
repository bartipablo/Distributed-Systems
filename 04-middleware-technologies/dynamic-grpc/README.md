# Dynamic GRPC invocation.

## Description

The goal of the task is to demonstrate the operation of a dynamic call on the client side of middleware. A dynamic call is one in which knowledge of the remote object or service interface is not required at compile time, but only at execution time. 

### Technologies
- gRPC
- gRPCurl
- Java (server)
- Python (client)

### Prerequisites

To run this project, ensure you have the following software installed:
- [grpcurl](https://github.com/fullstorydev/grpcurl)
```
# Example installation command for Ubuntu MacOS
brew install grpcurl
```

### How to run server
```
cd ./server
mvn clean compile
mvn exec:java -Dexec.mainClass="com.bartipablo.dynamicgrp.DynamicGRPCServer"
```

### How to run client
```
cd ./client
python3 client.py
```