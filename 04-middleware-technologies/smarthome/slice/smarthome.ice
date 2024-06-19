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

  exception InputRadiusOutOfRange {};


  interface Device {
    idempotent string getId();

    idempotent void setMode(Mode mode);

    idempotent Mode getMode();
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

    void addProduct(Product product) throws DevicesIsInStandbyMode, InvalidDate;

    void addProducts(ProductList products) throws DevicesIsInStandbyMode, InvalidDate;

    void removeProduct(int productId) throws DevicesIsInStandbyMode, ProductDoesNotExist;
  };

   interface FridgeWithIceMaker extends Fridge {
    void makeIce(int weigth) throws DevicesIsInStandbyMode;
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
/**
* MOWER
********************************************************/

/********************************************************
* SPRINKLER
*/
    struct RadiusRange {
        int min;
        int max;
    };

    interface Sprinkler extends Device {
        idempotent void setRadius(int radius) throws DevicesIsInStandbyMode, InputRadiusOutOfRange;

        idempotent int getRadius() throws DevicesIsInStandbyMode;
    };
/**
* SPRINKLER
********************************************************/
};


#endif