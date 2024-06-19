# Group Chat

## Description

A simple chat application for group communication using Berkeley sockets (TCP and UDP).

[YouTube preview](https://www.youtube.com/watch?v=p2HPqFztxfw)

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
<message> - send message to the server using TCP.
/U <message> - send message to the server using UDP.
/M <message> - send message to the server using multicast.
/ASCII - send ASCII art to the server using TCP.
/Q - quit the chat.
```
