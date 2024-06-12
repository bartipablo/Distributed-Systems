# ZooKeeper

## Description
- Application in the Zookeeper environment (Java) which using the mechanism of watches allows the following functionality:

1) If a znode named "a" is created, an external
graphical application (any, specified in the command line),
2) If it is deleted "a" the external application is stopped,
3) Each time a descendant is added to "a", a graphical
information on the screen about the current number of descendants.

- In addition, the application should be able to display the entire
tree structure of "a"

The created application work in the environment "Replicated
ZooKeeper".


### Running ZooKeeper servers
```
bin/zkServer.sh start-foreground conf1/zoo.cfg
bin/zkServer.sh start-foreground conf2/zoo.cfg
bin/zkServer.sh start-foreground conf3/zoo.cfg
```

### Running ZooKeeper clients
```
bin/zkCli.sh -server localhost:2181,localhost:2182,localhost:2183
```

## Running znodes monitor applicaton

### Compilation
```
mvn compile
```

### running
```
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="localhost:2181,localhost:2182,localhost:2183 /a '<command>'"
```