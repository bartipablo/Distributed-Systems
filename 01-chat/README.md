# Simple chat

## setup:

```
mvn compile
```

## running the server:

```
cd ./target/classes
java server.Server
```

## running the client:

```
cd ./target/classes
java client.Client <nick>
```

## list of client commands:

```
<message>     # sending a message using TCP
/U <message>  # sending a message using UDP
/M <message>  # sending a message using UDP multicast
/quit         # connection closing
```
