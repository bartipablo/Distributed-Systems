#ifndef CALC_ICE
#define CALC_ICE

module Smarthome
{
  enum Mode { ON, STANDBY };


  exception DevicesIsInStandbyMode {};

  exception InputTemperatureOutOfRange {};

  exception ProductDoesNotExist {};

  exception InputSpeedOutOfRange {};

  exception BatteryLvlLow {};

  exception InvalidDate {};


  interface Device {
    string getId();

    void setMode(Mode mode);

    Mode getMode();
  };

/********************************************************
* FRIDGE
**/

  enum Unit {
    GRAM, KILOGRAM, LITRE, MILLILITRE
  };

  struct Date {
    int day;
    int month;
    int year;
  };

  struct Product {
    int id;
    string name;
    int amount;
    Unit unit;
    Date expirationDate;
  };

  struct TemperatureRange {
    double min;
    double max;
  };

  interface Fridge extends Device {
    idempotent void setTemperature(double temperature) throws DevicesIsInStandbyMode, InputTemperatureOutOfRange;

    idempotent double getTemperature() throws DevicesIsInStandbyMode;

    idempotent TemperatureRange getTemperatureRange() throws DevicesIsInStandbyMode;
  };

  sequence<Product> ProductList;


  interface FridgeWithProductsMonitoring extends Fridge {
    idempotent ProductList getProducts() throws DevicesIsInStandbyMode;

    idempotent Product getProduct(int id) throws DevicesIsInStandbyMode, ProductDoesNotExist;

    idempotent ProductList getExpiredProducts() throws DevicesIsInStandbyMode;

    idempotent void addProduct(Product product) throws DevicesIsInStandbyMode, InvalidDate;

    idempotent void addProducts(ProductList products) throws DevicesIsInStandbyMode, InvalidDate;

    idempotent void removeProduct(int productId) throws DevicesIsInStandbyMode, ProductDoesNotExist;
  };

   interface FridgeWithIceMaker extends Fridge {

    idempotent void makeIce(int weigth) throws DevicesIsInStandbyMode;

   };

/**
* FRIDGE
********************************************************/

/********************************************************
* MOWER
*/
    struct Position {
        double x;
        double y;
    };

    struct SpeedRange {
        int min;
        int max;
    };

    interface Mower extends Device {
        idempotent Position getPosition() throws DevicesIsInStandbyMode;

        idempotent void setSpeed(int speed) throws DevicesIsInStandbyMode, InputSpeedOutOfRange, BatteryLvlLow;

        idempotent int getSpeed() throws DevicesIsInStandbyMode;

        idempotent double getBatteryLevel() throws DevicesIsInStandbyMode;
    };

};
/**
* MOWER
********************************************************/

#endif