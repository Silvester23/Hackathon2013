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
 * Helper class to handle the current states of vehicle doors.
 */
public class DoorStates {
    /* Member for storing state of driver side door. */
    DoorState mDriverSide;
    /* Member for storing state of passenger side door. */
    DoorState mPassengerSide;

    /* Member for storing state of driver side rear door. */
    DoorState mDriverSideRear;
    /* Member for storing state of passenger side rear door. */
    DoorState mPassengerSideRear;

    /* Member for storing state of trunk door. */
    DoorState mTrunk;
    /* Member for storing state of bonnet. */
    DoorState mBonnet;
    
    /**
     * Constructor.
     */
    public DoorStates() {
        mDriverSide = DoorState.UNDEFINED;
        mPassengerSide = DoorState.UNDEFINED;
        mDriverSideRear = DoorState.UNDEFINED;
        mPassengerSideRear = DoorState.UNDEFINED;

        mTrunk = DoorState.UNDEFINED;
        mBonnet = DoorState.UNDEFINED;
    }

    /**
     * Retrieve the state of driver side door.
     * @return state of driver side door
     */
    public DoorState getDriverSideState() {
        return mDriverSide;
    }
    
    /**
     * Set the state of driver side door.
     *
     * @param driverSide state of driver side door
     */
    public void setDriverSideState(DoorState driverSide) {
        mDriverSide = driverSide;
    }
    

    /**
     * Retrieve the state of passenger side door.
     * @return state of passenger side door
     */
    public DoorState getPassengerSideState() {
        return mPassengerSide;
    }
    
    /**
     * Set the state of passenger side door.
     *
     * @param passengerSide state of passenger side door
     */
    public void setPassengerSideState(DoorState passengerSide) {
        mPassengerSide = passengerSide;
    }

    /**
     * Retrieve the state of rear driver side door.
     * @return state of rear driver side door
     */
    public DoorState getDriverSideRearState() {
        return mDriverSideRear;
    }
    
    /**
     * Set the state of rear driver side door.
     *
     * @param driverSideRear state of rear driver side door
     */
    public void setDriverSideRearState(DoorState driverSideRear) {
        mDriverSideRear = driverSideRear;
    }
    
    /**
     * Retrieve the state of rear passenger side door.
     * @return state of rear passenger side door
     */
    public DoorState getPassengerSideRearState() {
        return mPassengerSideRear;
    }
    
    /**
     * Set the state of rear passenger side door.
     *
     * @param passengerSideRear state of rear passenger side door
     */
    public void setPassengerSideRearState(DoorState passengerSideRear) {
        mPassengerSideRear = passengerSideRear;
    }

    /**
     * Retrieve the state of trunk.
     * @return state of trunk
     */
    public DoorState getTrunkState() {
        return mTrunk;
    }
    
    /**
     * Set the state of trunk.
     *
     * @param trunk state of trunk
     */
    public void setTrunkState(DoorState trunk) {
        mTrunk = trunk;
    }

    /**
     * Retrieve the state of bonnet.
     * @return state of bonnet
     */
    public DoorState getBonnetState() {
        return mBonnet;
    }
    
    /**
     * Set the state of bonnet.
     *
     * @param bonnet state of bonnet
     */
    public void setBonnetState(DoorState bonnet) {
        mBonnet = bonnet;
    }
    
    @Override
    public String toString() {
        return "Driver side: "+ stateToString(mDriverSide) //$NON-NLS-1$
                +", passenger side: "+ stateToString(mPassengerSide) //$NON-NLS-1$
                +", driver side rear: "+ stateToString(mDriverSideRear) //$NON-NLS-1$
                +", passenger side rear: "+ stateToString(mPassengerSideRear) //$NON-NLS-1$
                +", trunk: "+ stateToString(mTrunk) //$NON-NLS-1$
                +", bonnet: "+ stateToString(mBonnet); //$NON-NLS-1$
    }
    
    String stateToString(DoorState state) {
        String ret;
        switch(state)
        {
            case NOT_AVAILABLE: ret = "not available"; break; //$NON-NLS-1$
            case CLOSED: ret = "closed"; break; //$NON-NLS-1$
            case OPEN: ret = "open"; break; //$NON-NLS-1$
            default:
                ret = "unknown"; break; //$NON-NLS-1$
        }
        return ret;
    }

}
