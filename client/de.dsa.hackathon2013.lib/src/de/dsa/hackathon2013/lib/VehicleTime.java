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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Helper class to handle time values as read from vehicle.
 * The class is used to store time values as they are returned from 
 * vehicle clock. This includes just values for hours and minutes.
 * Other time values (such as date, day of week, time zone, ...)
 * are not available. 
 */
public class VehicleTime {
    /* Member for storing the minutes for time value. */
    int mMinutes;
    /* Member for storing the hours for time value. */
    int mHours;

    /**
     * Constructor.
     * Initial values for hours and minutes has to be provided.
     * The values are stored as provided, no range checking is done. 
     * The set values can be retrieved using getter methods or 
     * overwritten with setter methods.
     * @param hours value for hours to store
     * @param minutes value for minutes to store
     */
    public VehicleTime(int hours, int minutes) {
        mHours = hours;
        mMinutes = minutes;
    }

    /**
     * Retrieve the minutes of stored time.
     * The value is returned as stored, no range check is done.  
     * @return value for minutes of stored time
     */
    public int getMinutes() {
        return mMinutes;
    }

    /**
     * Retrieve the hours of stored time.
     * The value is returned as stored, no range check is done.  
     * @return value for hours of stored time
     */
    public int getHours() {
        return mHours;
    }

    /**
     * Set the minutes of stored time to given value.
     * The value is stored as provided, no range check is done.  
     * The set value can be retrieved using method getMinutes or 
     * overwritten with another call of this method.
     * @param minutes value for minutes of time to store
     */
    public void setMinutes(int minutes) {
        mMinutes = minutes;
    }

    /**
     * Set the hours of stored time to given value.
     * The value is stored as provided, no range check is done.  
     * The set value can be retrieved using method getMi or 
     * overwritten with another call of this method.
     * @param hours value for hours of time to store
     */
    public void setHours(int hours) {
        mHours = hours;
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mHours);
        calendar.set(Calendar.MINUTE, mMinutes);
        DateFormat formatter = new SimpleDateFormat("HH:mm"); //$NON-NLS-1$
        return formatter.format(calendar.getTime());
    }

}
