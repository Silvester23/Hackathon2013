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
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.dsa.hackathon2013.DiagnosticNames;
import de.dsa.hackathon2013.app.readertask.DiagnosticReaderFragment;
import de.dsa.hackathon2013.app.readertask.DiagnosticReaderFragment.VehicleValuesReaderTaskCallbacks;
import de.dsa.hackaton.R;

/**
 * Main Activity for the Diagnostics Application. The Activity will display a
 * list of values read from the vehicle. This operation is performed in a
 * background thread and provides the activity with progress updates.
 *
 * The activity also has a menu action to allow the user to read new values from
 * the vehicle.
 */
public class DSADiagnosticsActivity extends Activity implements VehicleValuesReaderTaskCallbacks {
    /** A retain fragment to perform a background operation. */
    private DiagnosticReaderFragment mTaskFragment;
    /** Adapter to provide the data model to the values list. */
    private DiagnosticValuesListAdapter mListAdapter;
    /** A collection of read diagnostic values. */
    private List<DiagnosticValue> mDiagValues;
    /** A dialog to show the progress of the identification values reading. */
    private ProgressDialog mProgressDialog;
    /** The currently process value shown in the progress dialog. */
    private String mProcessingValue;

    /** Identifier of the argument to hold the items displayed in the view. */
    public final static String ARG_DIAGNOSTIC_VALUES = "DiagnosticValues";
    /** Identifier of the argument to hold the status of the progress. */
    private static final String ARG_PROGRESS_SHOWN = "ProgressRunning";
    /** Name of the preferences file associated to this activity. */
    public static final String SETTINGS_PREFERENCES = "Settings";
    /** Preference key for storing the selected IP address. */
    public static final String IPADDRESS_PREFERENCE = "IpAddressPreference";

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsadiagnostics);
        //        copyAssets();
        if (savedInstanceState != null) {
            // Load the already generated items during a restore of the activity
            // As long as there is no execution of the read action, it is not
            // required to generated them again.
            mDiagValues = (List<DiagnosticValue>)savedInstanceState.getSerializable(ARG_DIAGNOSTIC_VALUES);

            // Restore the processing value in the progress dialog and show the dialog
            // if processing was still in place during a configuration change.
            mProcessingValue = savedInstanceState.getString(ARG_PROGRESS_SHOWN);
            if (mProcessingValue != null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage(mProcessingValue);
                mProgressDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Configure the fragment that will perform the read operation for the
        // vehicle values.
        configureRetainedFragment();

        // Create the vehicle values list and attach the adapter that will hold
        // the data model.
        ListView valuesList = (ListView)findViewById(android.R.id.list);
        if (mListAdapter == null) {
            if (mDiagValues == null) {
                mDiagValues = new ArrayList<DiagnosticValue>();
            }
            mListAdapter = new DiagnosticValuesListAdapter(this, new ArrayList<DiagnosticValue>(mDiagValues));
        }
        valuesList.setAdapter(mListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save UI state changes to the fragment instance. The bundle will
        // be passed to onCreate if the process is killed and restarted.
        outState.putSerializable(ARG_DIAGNOSTIC_VALUES, (Serializable)mDiagValues);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            outState.putString(ARG_PROGRESS_SHOWN, mProcessingValue);
        }
    }

    @Override
    protected void onDestroy() {
        // Destroy also the progress dialog, will be recreated when the activity
        // is restored after a configuration change.
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * Configuration of the retain fragment which will run the read values
     * operation as an async task.
     */
    private void configureRetainedFragment() {
        FragmentManager fm = getFragmentManager();
        // Check to see if we have retained the worker fragment.
        // If the Fragment is non-null, then it is currently being retained
        // across a configuration change.
        mTaskFragment = (DiagnosticReaderFragment)fm.findFragmentByTag(DiagnosticReaderFragment.READER_TAG);
        if (mTaskFragment == null) {
            mTaskFragment = new DiagnosticReaderFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(mTaskFragment, DiagnosticReaderFragment.READER_TAG);
            transaction.commit();
        } else {
            // Set the target fragment as callback, required after a
            // configuration change to indicate
            // the task that updates must be notified to this fragment.
            mTaskFragment.setTaskCallback();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            SettingsDialog saveDialog = new SettingsDialog();
            saveDialog.show(getFragmentManager(), SettingsDialog.TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**************************************************************************
     * Callback methods to be executed from the Async Task running the
     * reading of diagnostic values.
     ************************************************************************* */
    @Override
    public void onPreExecute() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    public void onProgressUpdate(Object progressValue) {
        if (progressValue instanceof DiagnosticValue) {
            DiagnosticValue diagnosticValue = (DiagnosticValue)progressValue;
            // Update the adapter to display the new value, this case is only
            // executed when there is a measurement value to be display in the list
            mDiagValues.add(diagnosticValue);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (mListAdapter != null) {
                        mListAdapter.clear();
                        mListAdapter.addAll(mDiagValues);
                        mListAdapter.notifyDataSetChanged();
                    }
                }
            });

        } else if (progressValue instanceof String) {
            // Change the progress message to show which value is being processed
            mProcessingValue = (String)progressValue;
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog.setMessage(mProcessingValue);
                }
            });

        }
    }

    @Override
    public void onCancelled() {
    }

    @Override
    public void onPostExecute() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**************************************************************************
     * Action methods to be executed when a click on a button is detected
     ************************************************************************* */
    public void measurementsRefresh(View view) {
        if (mTaskFragment.isReading()) {
            mTaskFragment.cancelTask();
        }
        // Trigger the reading of the vehicle values again
        if (mListAdapter != null) {
            mListAdapter.clear();
            mDiagValues.clear();
            mListAdapter.addAll(mDiagValues);
            mListAdapter.notifyDataSetChanged();
        }
        mTaskFragment.executeDiagnostic("Measurements");
    }

    /** Called when the user touches the mirror up button. */
    public void moveMirrorUp(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.MIRROR_UP);
    }

    /** Called when the user touches the mirror down button. */
    public void moveMirrorDown(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.MIRROR_DOWN);
    }

    /** Called when the user touches the mirror left button. */
    public void moveMirrorLeft(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.MIRROR_LEFT);
    }

    /** Called when the user touches the mirror right button. */
    public void moveMirrorRight(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.MIRROR_RIGHT);
    }

    /** Called when the user touches the window up button. */
    public void moveWindowUp(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.WINDOW_UP);
    }

    /** Called when the user touches the window down button. */
    public void moveWindowDown(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.WINDOW_DOWN);
    }

    /** Called when the user touches the door lock button. */
    public void lockDoor(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.DOOR_LOCK);
    }

    /** Called when the user touches the door unlock button. */
    public void unlockDoor(View view) {
        mTaskFragment.executeDiagnostic(DiagnosticNames.DOOR_UNLOCK);
    }
}
