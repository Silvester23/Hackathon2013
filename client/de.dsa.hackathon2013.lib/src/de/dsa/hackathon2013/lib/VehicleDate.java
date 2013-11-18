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
 * Helper class to handle date values as read from vehicle.
 * The class is used to store time values as they are returned from 
 * vehicle clock. This includes just values for year, month and day of month
 * Other time values (day of week, time zone, ...)
 * are not available. 
 */
public class VehicleDate {
    /* Member for storing the year for time value. */
    int mYear;
    /* Member for storing the month for time value. */
    int mMonth;
    /* Member for storing the day for time value. */
    int mDay;

    /**
     * Constructor.
     * Initial values for hours and minutes has to be provided.
     * The values are stored as provided, no range checking is done. 
     * The set values can be retrieved using getter methods or 
     * overwritten with setter methods.
     * @param year value for year to store
     * @param month value for month to store
     * @param day value for day of month to store
     */
    public VehicleDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    /**
     * Retrieve the day of stored date.
     * The value is returned as stored, no range check is done.  
     * @return value for day of stored date
     */
    public int getDay() {
        return mDay;
    }

    
    /**
     * Retrieve the month of stored date.
     * The value is returned as stored, no range check is done.  
     * @return value for month of stored date
     */
    public int getMonth() {
        return mMonth;
    }

    /**
     * Retrieve the year of stored date.
     * The value is returned as stored, no range check is done.  
     * @return value for year of stored date
     */
    public int getYear() {
        return mYear;
    }

    /**
     * Set the day of stored date to given value.
     * The value is stored as provided, no range check is done.  
     * The set value can be retrieved using method getDay or 
     * overwritten with another call of this method.
     * @param month value for day of date to store
     */
    public void setDay(int day) {
        mDay = day;
    }

    /**
     * Set the month of stored date to given value.
     * The value is stored as provided, no range check is done.  
     * The set value can be retrieved using method getMonth or 
     * overwritten with another call of this method.
     * @param month value for month of date to store
     */
    public void setMonth(int month) {
        mMonth = month;
    }

    /**
     * Set the year of stored date to given value.
     * The value is stored as provided, no range check is done.  
     * The set value can be retrieved using method getYear or 
     * overwritten with another call of this method.
     * @param year value for year of date to store
     */
    public void setYear(int year) {
        mYear = year;
    }

    @Override
    public String toString() {
        return mYear + "." + mMonth+ "." + mDay; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
