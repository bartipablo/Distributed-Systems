help_message = """    

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
      for mowers:
        <devid> getPosition
        <devid> setSpeed <int>
        <devid> getBatteryLevel
        <devid> getSpeed
      for sprinklers:
        <devid> getRadius
        <devid> setRadius <int>
"""