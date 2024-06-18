import sys
import os
import Ice
import asyncio

sys.path.insert(0, os.path.abspath('../../../../generated'))
import Smarthome


class InvalidCommand(Exception):
    pass


help_message = """
    available devices:
      devid=Fridge1 (fridge)
      devid=Fridge2 (fridge with ice maker)
      devid=Fridge3 (fridge with products monitoring)
      devid=Mower (mower)
    
    available commands:
      for each devices:
        <devid> getId
        <devid> getMode
        <devid> setMode <on | off>
      for each fridges:
        <devid> setTemperature <float>
        <devid> getTemperature
        <devid> getTemperatureRange
      for fridges with ice maker:
        <devid> makeIce <int>
      for fridges with product monitoring:
        <devid> getProducts
        <devid> getProduct <int>
        <devid> getExpiredProducts
        <devid> removeProduct <int>
        <devid> addProduct <int> <string> <int> <string> <int> <int> <int>
      for each mower:
        <devid> getPosition
        <devid> setSpeed <int>
        <devid> getBatteryLevel
        <devid> getSpeed
"""


def get_proxy_string(ip, port, category, name):
    return f"{category}/{name}:tcp -h {ip} -p {port} :udp -h {ip} -p {port}"


async def get_prx(ip, port, communicator, category, name, prx):
    base = communicator.stringToProxy(get_proxy_string(ip, port, name, category))
    nprx = prx.checkedCast(base)
    if not nprx:
        raise ValueError("Invalid proxy")
    return nprx


async def get_devices(ip, port, communicator):
    return {
        "Fridge1": await get_prx(ip, port, communicator, "Fridge1", "fridge", Smarthome.FridgePrx),
        "Fridge2": await get_prx(ip, port, communicator, "FridgeWithIceMaker1", "fridge",
                                 Smarthome.FridgeWithIceMakerPrx),
        "Fridge3": await get_prx(ip, port, communicator, "FridgeWithProductsMonitoring1", "fridge",
                                 Smarthome.FridgeWithProductsMonitoringPrx),
        "Mower": await get_prx(ip, port, communicator, "Mower1", "mower", Smarthome.MowerPrx)
    }


def loop(devices):
    while True:
        try:
            command = input(">>> ")
            if command == "exit":
                return

            elif command == "help":
                print(help_message)

            else:
                command = command.split(" ")
                device_str = command[0]
                device = devices.get(device_str)

                if device is None:
                    raise InvalidCommand()

                func_str = command[1]

                # for device interface ------------------------
                if func_str == "getId":
                    print(device.getId())

                elif func_str == "getMode":
                    print(device.getMode())

                elif func_str == "setMode":
                    mode = command[2].lower()
                    if mode == "on":
                        device.setMode(Smarthome.Mode.ON)
                    elif mode == "off":
                        device.setMode(Smarthome.Mode.STANDBY)
                    else:
                        raise InvalidCommand()
                # for device interface ------------------------

                # for fridge interface ------------------------
                elif func_str == "setTemperature":
                    temperature = float(command[2])
                    device.setTemperature(temperature)

                elif func_str == "getTemperature":
                    print(device.getTemperature())

                elif func_str == "getTemperatureRange":
                    device_temp_range = device.getTemperatureRange()
                    print("min: " + str(device_temp_range.min))
                    print("max: " + str(device_temp_range.max))
                # for fridge interface ------------------------

                # for fridge with ice maker interface ------------------------
                elif func_str == "makeIce":
                    quantity = int(command[2])
                    device.makeIce(quantity)
                    print("finish.")
                # for fridge with ice maker interface ------------------------

                # for fridge with products monitor interface ------------------------
                elif func_str == "getProducts":
                    products = device.getProducts()
                    for product in products:
                        print(product)

                elif func_str == "getProduct":
                    product_id = int(command[2])
                    print(device.getProduct(product_id))

                elif func_str == "getExpiredProducts":
                    products = device.getExpiredProducts()
                    for product in products:
                        print(product)

                elif func_str == "removeProduct":
                    product_to_remove_id = int(command[2])
                    device.removeProduct(product_to_remove_id)

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
                    device.addProduct(product)
                # for fridge with products monitor interface ------------------------

                # for mower interface------------------------------------------------
                elif func_str == "getPosition":
                    position = device.getPosition()
                    print("x: " + str(position.x))
                    print("y: " + str(position.y))

                elif func_str == "setSpeed":
                    speed = int(command[2])
                    device.setSpeed(speed)

                elif func_str == "getBatteryLevel":
                    print(device.getBatteryLevel())

                elif func_str == "getSpeed":
                    print(device.getSpeed())

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
        except Exception as e:
            print(f"Exception occurred: {str(e)}")


def main():
    if len(sys.argv) < 3:
        print("Usage: python3 main.py <server_ip> <server_port> <ice_args>")
        return 1

    server_ip = sys.argv[1]
    server_port = sys.argv[2]
    ice_args = sys.argv[3:]

    communicator = None

    try:
        communicator = Ice.initialize(ice_args)

        devices = asyncio.run(get_devices(server_ip, server_port, communicator))

        loop(devices)

    except Exception as ex:
        print(str(ex))
        exit(1)
    finally:
        if communicator:
            communicator.destroy()


if __name__ == "__main__":
    main()