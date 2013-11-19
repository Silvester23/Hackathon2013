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
 *    Author      Ada Lezama
 *    Created on  20.10.2013
 *
 ************************************************************************/
package de.dsa.hackathon2013.app;

import java.io.Serializable;

/**
 * An object that represents a diagnostic value from the vehicle. It will
 * contain the name of the read value and the value retrieved from the vehicle
 * during the diagnostics operation.
 *
 * This object is also serializable, therefore it can be persisted when required
 * by the UI layer due to screen configuration changes.
 */
public class DiagnosticValue implements Serializable {
    /** The version identifier for the serializable object. */
    private static final long serialVersionUID = 1L;
    private String mName;
    private String mValue;
    private String mUnit;

    /**
     * Create a new instance of a {@link DiagnosticValue} with a name and the
     * value read from the vehicle.
     *
     * @param pName the name of the read value
     * @param pValue the value read from the vehicle
     */
    public DiagnosticValue(String pName, String pValue, String pUnit) {
        mName = pName;
        mValue = pValue;
        mUnit = pUnit;
    }

    /**
     * Retrieve the name of the {@link DiagnosticValue}.
     *
     * @return as described
     */
    public String getName() {
        return mName;
    }

    /**
     * Retrieve the value of the {@link DiagnosticValue}.
     *
     * @return as described
     */
    public String getValue() {
        return mValue;
    }

    public String getUnit() {
        return mUnit;
    }

}
