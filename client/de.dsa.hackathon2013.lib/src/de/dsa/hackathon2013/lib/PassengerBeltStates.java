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
 * Helper class to handle the current states of passenger belts.
 */
public class PassengerBeltStates {
    /* Member for storing state of driver side belt. */
    BeltState mDriverSide;
    /* Member for storing state of middle  belt. */
    BeltState mMiddle;
    /* Member for storing state of passenger side belt. */
    BeltState mPassengerSide;

    /**
     * Constructor.
     * Initial values for belt states have to be provided.
     * The values are stored as provided, no range checking is done. 
     * The set values can be retrieved using getter methods or 
     * overwritten with setter methods.
     * @param driverSide state of driver side belt
     * @param middle state of middle side belt
     * @param passengerSide state of passenger side belt
     */
    public PassengerBeltStates(BeltState driverSide, BeltState middle, BeltState passengerSide) {
        mDriverSide = driverSide;
        mMiddle = middle;
        mPassengerSide = passengerSide;
    }

    /**
     * Retrieve the state of driver side belt.
     * @return state of driver side belt
     */
    public BeltState getDriverSideState() {
        return mDriverSide;
    }
    
    /**
     * Set the state of driver side belt.
     *
     * @param driverSide state of driver side belt
     */
    public void setDriverSideState(BeltState driverSide) {
        mDriverSide = driverSide;
    }

    /**
     * Set the state of middle belt.
     *
     * @param middle state of middle belt
     */
    public void setMiddleState(BeltState middle) {
        mMiddle = middle;
    }

    /**
     * Retrieve the state of middle belt.
     * @return state of middle belt
     */
    public BeltState getMiddleState() {
        return mMiddle;
    }
    
    /**
     * Retrieve the state of passenger side belt.
     * @return state of passenger side belt
     */
    public BeltState getPassengerSideState() {
        return mPassengerSide;
    }
        
    /**
     * Set the state of passenger side belt.
     *
     * @param passengerSide state of passenger side belt
     */
    public void setPassengerSideState(BeltState passengerSide) {
        mPassengerSide = passengerSide;
    }
    
    
    @Override
    public String toString() {
        return "Driver side: "+ stateToString(mDriverSide)+", middle: "+ stateToString(mMiddle)+", passenger side: "+ stateToString(mPassengerSide); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    String stateToString(BeltState state) {
        String ret;
        switch(state)
        {
            case NOT_AVAILABLE: ret = "not available"; break; //$NON-NLS-1$
            case ERROR: ret = "error"; break; //$NON-NLS-1$
            case CLOSED: ret = "closed"; break; //$NON-NLS-1$
            case OPEN: ret = "open"; break; //$NON-NLS-1$
            default:
                ret = "unknown"; break; //$NON-NLS-1$
        }
        return ret;
    }

}
