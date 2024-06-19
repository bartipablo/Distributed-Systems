# Smart home

## Description

The application will allow remote management of smart home devices, which include various appliances such as carbon monoxide sensors, remotely controlled refrigerators, ovens, PTZ surveillance cameras, bubblers, etc. Each device may come in several slightly different variants, with each variant having a certain (small) number of instances.


### Technologies
- ZeroC Ice
- Java (server)
- Python (client)



### How to run server
```
 mkdir generated
 slice2java --output-dir generated slice/smarthome.ice
```
Open the project using IntelliJ IDE and run the instances of KitchenServer and YardServer.

### How to run client
```
mkdir generated
slice2py --output-dir generated slice/smarthome.ice
python3 ./src/sr/ice/client/client.py
```
