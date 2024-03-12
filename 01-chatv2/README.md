# Simple chat

## Description
A simple chat application enabling group communication using TCP and UDP.

## setup server:

```
cd ./server
mvn compile
```

## setup client:

```
cd ./client
mvn compile
```

## running the server:

```
cd ./server/target/classes
java com.bartipablo.Server
```

## running the client:

```
cd ./client/target/classes
java com.bartipablo.Client <nick>
```

## list of client commands:

```
<message> - send message to the server using TCP
/U <message> - send message to the server using UDP"
/M <message> - send message to the server using multicast"
/ASCII - send ASCII art to the server using TCP"
/Q - quit the chat"
```
