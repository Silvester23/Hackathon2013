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
 *    Created on  30.10.2013
 *
 ************************************************************************/
package de.dsa.hackathon2013.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import de.dsa.hackaton.R;

/**
 * A {@link DialogFragment} which displays a the configuration settings for the application.
 * It will contain a text field for setting the IP Address of the WDI.
 */
public class SettingsDialog extends DialogFragment {

    /** Identifier of the dialog. */
    public static final String TAG = "SettingsDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences mPreferenceSettings = getActivity().getSharedPreferences(DSADiagnosticsActivity.SETTINGS_PREFERENCES, 0);
        String storedAddress = mPreferenceSettings.getString(DSADiagnosticsActivity.IPADDRESS_PREFERENCE, "192.168.126.2");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater to inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layoutView = inflater.inflate(R.layout.dialog_settings, null);
        builder.setView(layoutView);

        // Configure the Dialog
        builder.setTitle("Configuration");
        final TextView editTextView = (TextView)layoutView.findViewById(R.id.ipaddress_text);
        editTextView.setText(storedAddress);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String ipAddress = editTextView.getText().toString();
                if (ipAddress != null) {
                    // Store the selected IP as a preference for the activity
                    mPreferenceSettings.edit().putString(DSADiagnosticsActivity.IPADDRESS_PREFERENCE, ipAddress).commit();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, do nothing
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
