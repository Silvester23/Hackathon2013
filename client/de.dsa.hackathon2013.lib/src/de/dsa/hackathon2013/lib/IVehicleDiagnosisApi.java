/************************************************************************
 *                                                                      *
 *  DDDD     SSSS    AAA        Daten- und Systemtechnik Aachen GmbH    *
 *  D   D   SS      A   A       Pascalstrasse 28                        *
 *  D   D    SSS    AAAAA       52076 Aachen-Oberforstbach, Germany     *
 *  D   D      SS   A   A       Telefon: +49 (0)2408 / 9492-0           *
 *  DDDD    SSSS    A   A       Telefax: +49 (0)2408 / 9492-92          *
 *                                                                      *
 *                                                                      *
 *  (c) Copyright by DSA - all rights reserved                          *
 *                                                                      *
 ************************************************************************
 *
 * Initial Creation:
 *    Author      rp
 *    Created on  17.10.2013
 *
 ************************************************************************/
package de.dsa.hackathon2013.lib;

/**
 * Definitions of functions to be used for simplified vehicle measurement and 
 * I/O control for Hackathon Aachen 2013. 
 */
public interface IVehicleDiagnosisApi {
    /*
     * Measurement and identification values
     */

    /**
     * Read the temperature outside of vehicle cabin. 
     * A temperature sensor build into the vehicle is used to measure the 
     * temperature outside of the vehicle cabin. The temperature can be 
     * shown to the driver on the dashboard and used for passenger 
     * convenience functions. 
     * 
     * The unit of the retrieved value is "°C". 
     * The valid range for the temperature is from -50°C to
     * +70°C. Any value outside this range indicates a failure in retrieving
     * temperature value (e.g. broken sensor).
     * 
     * @return environmental temperate as shown to driver
     */
    float readEnvironmentTemperature();

    /**
     * Read the temperature of engine oil. 
     * The engine oil temperature is measured continuously from vehicle.
     * This value is shown to the driver and used to activate a 
     * warning lamp if necessary. The value may also be used for vehicle 
     * internal control functions. The normal operating temperature for motor
     * oil is usually around +90 °C. Higher or lower oil temperatures may 
     * result in higher engine abrasion. If the oil temperature is to high or
     * to low the engine may be damaged. 
     * 
     * The unit of the retrieved value is "°C". 
     * The valid range for the temperate is from -58°C to +190°C. Any value 
     * outside this range indicates a failure in retrieving temperate value 
     * (e.g. broken sensor).
     *  
     * @return motor oil temperature as shown to driver
     */
    float readEngineOilTemperature();

    /**
     * Read the time from the vehicle clock. The vehicle time includes hours
     * and minutes from the vehicle build in clock. No other information (e.g.
     * time zone, seconds) is available.
     * 
     * @return vehicle time
     */
    VehicleTime readClock();
    
    /**
     * Read the sensor value for environmental light. 
     * The external light is measured using a photo transistor. The value
     * can be used to trigger convenience functions of vehicle. 
     * 
     * The unit of the retrieved value is percent ("%").
     * 
     * @return measured value for environmental light
     */
    float readEnvironmentelLightValue();

    /**
     * Read the calculated fuel left in vehicle fuel tank. The value for
     * remaining fuel is calculated from fuel level sensor. The unit of the
     * retrieved value is liter (l).
     * 
     * @return remaining fuel in fuel tank
     */
    float readFuelTankReserve();

    /**
     * Read the current position of the fuel tank instrument needle in
     * dashboard. The current position for the instrument needle is calculated
     * from fuel tank level sensor. The unit of the retrieved value is °. The
     * valid range of the value is from 0 to 270°.
     * 
     * @return current angle of fuel tank instrument needle
     */
    float readFuelTankNeedlePosition();

    /**
     * Read the current value of the engine coolant temperature. The unit of the
     * retrieved value is °C. The valid range is from -47.25°C to 143 °C. Any
     * value outside this range indicates a failure in retrieving temperature
     * value (e.g. broken sensor).
     * 
     * @return current engine coolant temperature
     */
    float readEngineCoolantTemperature();

    /**
     * Read the current rounds per minute value for the engine. The unit of the
     * retrieved value is 1/min.
     * The valid range for rounds per minutes depends on vehicle type.
     * A rounds per minutes value '0' indicates that the engine is turned off (or
     * sensor is broken).
     * Engine idle value is usually around 700-1100 1/min.  When driver presses
     * accelerator, the engine controller opens the throttle and the 
     * rounds per minute value for engine rises.
     * 
     * @return current rounds per minute of engine
     */
    float readEngineRpm();

    /**
     * Read the current vehicle speed. The vehicle speed is read as it is shown
     * to the driver on dashboard. The unit of the retrieved value is km/h.
     * Negative speed values are not possible, not even when driving with 
     * activated reverse gear.
     * 
     * @return current vehicle speed
     */
    float readVehicleSpeed();

    /**
     * Read the position. The unit for the value is %.
     * In idle position the value is expected to be 
     * somewhere in rage from 0% to 20%. For maximum
     * acceleration the position is expected to be 
     * somewhere in range from 80% -100%
     * 
     * @return accelerator position
     */
    float readAcceleratorPedalPosition();

    /**
     * Read the vehicle identification number.
     * 
     * The vehicle identification number (VIN) is a 17 characters long
     * alphanumeric number. Each VIN is assigned to one vehicle only and thus
     * worldwide unique. The VIN is assigned from the manufacturer to each vehicle.
     * 
     * @return identification number of vehicle
     */
    String readVehicleIdentificationNumber();

    /**
     * Read the current range of vehicle.
     * The current range is the distance the vehicle can cover
     * using currently available gas.
     * The range does not include the distance that can 
     * be covered using the reserve fuel in gas tank.
     * 
     * The unit is km.
     * Valid values are 0 - 3000.
     *
     * @return current range for vehicle
     */
    public float readRange();

    /**
     * Read the current state of doors / lids / flaps of 
     * vehicle.
     * Each state can be either open, closed, not available or 
     * undefined.
     * A door state is only undefined if the value can not be
     * successfully read from vehicle.
     *
     * @return class containing current state of doors
     */
    public DoorStates readDoorStates();

    /*
     * Controls of vehicle
     */

    /**
     * Control to lock or unlock the driver side door.
     * 
     * @param locked true to lock door, false to unlock door
     */
    void setDriverSideDoor(boolean locked);

    /**
     * Control method for the driver side mirror. The mirror is moved to the
     * given direction for 1 second.
     * 
     * @param direction value for direction where to move the mirror to.
     */
    void moveDriverSideMirror(MirrorMovingDirection direction);

    /**
     * Control method for the driver side window. The window is moved to the
     * given direction for 1 second.
     * 
     * @param diretion UP for moving window up, DOWN for moving window down
     */

    void moveDriverWindow(WindowMovingDirection direction);

}
