import sys
import os
import Ice
import asyncio
from utils import help_message

sys.path.insert(0, os.path.abspath('../../../../generated'))
sys.path.insert(0, os.path.abspath('./generated'))
import Smarthome


class InvalidCommand(Exception):
    pass


class Device():
    def __init__(self, name, category, desc, ip, port, prx):
        self.name = name
        self.category = category
        self.desc = desc
        self.prx = prx
        self.ip = ip
        self.port = port
        self.nprx = None

    def get_name(self):
        return self.name
    
    def get_category(self):
        return self.category
    
    def get_desc(self):
        return self.desc
    
    def get_prx(self):
        return self.prx
    
    def get_ip(self):
        return self.ip
    
    def get_port(self):
        return self.port
    
    def set_nprx(self, nprx):
        self.nprx = nprx

    def get_nprx(self):
        return self.nprx


def loop(devices, communicator):
    while True:
        try:
            command = input(">>> ")
            
            if command == "exit":
                return
            elif command == "help":
                print(help_message)
            elif command == "list":
                list_devices(devices)
            elif command == "reconnect":
                asyncio.run(connect_devices(devices, communicator))
        
            else:
                command = command.split(" ")
                device = get_device_by_name(devices, command[0])

                if device is None:
                    raise InvalidCommand()

                func_str = command[1]

                # for device interface ------------------------
                if func_str == "getId":
                    print(device.get_nprx().getId())

                elif func_str == "getMode":
                    print(device.get_nprx().getMode())

                elif func_str == "setMode":
                    mode = command[2].lower()
                    if mode == "on":
                        device.get_nprx().setMode(Smarthome.Mode.ON)
                    elif mode == "off":
                        device.get_nprx().setMode(Smarthome.Mode.STANDBY)
                    else:
                        raise InvalidCommand()
                # for device interface ------------------------

                # for fridge interface ------------------------
                elif func_str == "setTemperature":
                    temperature = float(command[2])
                    device.get_nprx().setTemperature(temperature)

                elif func_str == "getTemperature":
                    print(device.get_nprx().getTemperature())

                elif func_str == "getTemperatureRange":
                    device_temp_range = device.get_nprx().getTemperatureRange()
                    print("min: " + str(device_temp_range.min))
                    print("max: " + str(device_temp_range.max))
                # for fridge interface ------------------------

                # for fridge with ice maker interface ------------------------
                elif func_str == "makeIce":
                    quantity = int(command[2])
                    device.get_nprx().makeIce(quantity)
                    print("finish.")
                # for fridge with ice maker interface ------------------------

                # for fridge with products monitor interface ------------------------
                elif func_str == "getProducts":
                    products = device.get_nprx().getProducts()
                    for product in products:
                        print(product)

                elif func_str == "getProduct":
                    product_id = int(command[2])
                    print(device.get_nprx().getProduct(product_id))

                elif func_str == "getExpiredProducts":
                    products = device.get_nprx().getExpiredProducts()
                    for product in products:
                        print(product)

                elif func_str == "removeProduct":
                    product_to_remove_id = int(command[2])
                    device.get_nprx().removeProduct(product_to_remove_id)

                elif func_str == "addProduct":
                    product_id = int(command[2])
                    product_name = command[3]
                    product_amount = int(command[4])
                    product_unit = command[5].lower()
                    if product_unit == "gram":
                        product_unit = Smarthome.Unit.GRAM
                    elif product_unit == "kilogram":
                        product_unit = Smarthome.Unit.KILOGRAM
                    elif product_unit == "litre":
                        product_unit = Smarthome.Unit.LITRE
                    elif product_unit == "millilitre":
                        product_unit = Smarthome.Unit.MILLILITRE
                    else:
                        raise InvalidCommand()
                    day = int(command[6])
                    month = int(command[7])
                    year = int(command[8])
                    expiration_date = Smarthome.Date(day=day, month=month, year=year)
                    product = Smarthome.Product(
                        id=product_id,
                        name=product_name,
                        amount=product_amount,
                        unit=product_unit,
                        expirationDate=expiration_date
                    )
                    device.get_nprx().addProduct(product)
                # for fridge with products monitor interface ------------------------

                # for mower interface------------------------------------------------
                elif func_str == "getPosition":
                    position = device.get_nprx().getPosition()
                    print("x: " + str(position.x))
                    print("y: " + str(position.y))

                elif func_str == "setSpeed":
                    speed = int(command[2])
                    device.get_nprx().setSpeed(speed)

                elif func_str == "getBatteryLevel":
                    print(device.get_nprx().getBatteryLevel())

                elif func_str == "getSpeed":
                    print(device.get_nprx().getSpeed())
                # for sprinkler interface------------------------------------------------
                elif func_str == "getRadius":
                    print(device.get_nprx().getRadius())
                elif func_str == "setRadius":
                    radius = int(command[2])
                    device.get_nprx().setRadius(radius)

                else:
                    raise InvalidCommand()

        except Smarthome.DevicesIsInStandbyMode:
            print("The device is in standby mode.")
            print("Switch it to ON mode to call the function.")
        except Smarthome.BatteryLvlLow:
            print("Battery level too low to call the function.")
        except Smarthome.ProductDoesNotExist:
            print("Product does not exist")
        except Smarthome.InputTemperatureOutOfRange:
            print("Invalid input temperature. Out of range.")
        except Smarthome.InputSpeedOutOfRange:
            print("Invalid input speed. Out of range.")
        except Smarthome.InvalidDate:
            print("Invalid date.")
        except (IndexError, AttributeError, InvalidCommand, ValueError):
            print("Invalid command.")
        except Ice.ConnectionRefusedException as e:
            print("Connection refused.")
        except Exception as e:
            print(f"Exception occurred: {str(e)}")


def list_devices(devices):
    print("\ndevices:")
    for device in devices:
        print(f"{device.get_name()} - {device.get_desc()}")
        

def get_device_by_name(devices, name):
    for device in devices:
        if device.get_name() == name:
            return device
    return None


def get_proxy_string(ip, port, category, name):
    return f"{category}/{name}:tcp -h {ip} -p {port} :udp -h {ip} -p {port}"


async def connect_device(device, communicator):
    base = communicator.stringToProxy(get_proxy_string(
        device.get_ip(),
        device.get_port(), 
        device.get_category(), 
        device.get_name()))
    
    prx = device.get_prx()
    device.set_nprx(prx.checkedCast(base))

    if not device.get_nprx():
        raise ValueError("Invalid proxy")


async def connect_devices(devices, communicator):
    for device in devices:
        try:
            await connect_device(device, communicator)
        except Exception as e:
            print(f"Error during connect with device {device.get_name()}:\n{str(e)}\n")


def main():
    server_ip = "127.0.0.1"
    kitchen_server_port = 40041
    yard_server_port = 40042

    devices = [
        Device("Mower1", "yard-devices", "mower", server_ip, yard_server_port, Smarthome.MowerPrx),
        Device("Sprinkler1", "yard-devices", "sprinkler", server_ip, yard_server_port, Smarthome.SprinklerPrx),
        Device("Sprinkler2", "yard-devices", "sprinkler", server_ip, yard_server_port, Smarthome.SprinklerPrx),
        Device("Fridge1", "kitchen-devices", "fridge", server_ip, kitchen_server_port, Smarthome.FridgePrx),
        Device("Fridge2", "kitchen-devices", "fridge with ice maker", server_ip, kitchen_server_port, Smarthome.FridgeWithIceMakerPrx),
        Device("Fridge3", "kitchen-devices", "fridge with product monitoring", server_ip, kitchen_server_port, Smarthome.FridgeWithProductsMonitoringPrx)
    ]

    communicator = None

    try:
        communicator = Ice.initialize()

        asyncio.run(connect_devices(devices, communicator))

        loop(devices, communicator)

    except Exception as ex:
        print(str(ex))
        exit(1)
    finally:
        if communicator:
            communicator.destroy()


if __name__ == "__main__":
    main()