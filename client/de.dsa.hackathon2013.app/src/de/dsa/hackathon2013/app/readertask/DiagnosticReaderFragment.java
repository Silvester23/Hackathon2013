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
package de.dsa.hackathon2013.app.readertask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.os.Bundle;

/**
 * Task fragment required to perform the read of measurement values operation in
 * a background thread. This fragment will be a RETAINED fragment which will
 * keep running even on the hosting fragment disposal due to screen
 * configuration changes (e.g. changes in orientation)
 *
 * This fragment is intended to run outside the UI-Thread.
 */
public class DiagnosticReaderFragment extends Fragment implements OnBackStackChangedListener {
    /** Tag used to identify this fragment. */
    public static final String READER_TAG = "DiagnosticValuesReader";

    /** The activity which reacts to the output of the task. */
    private VehicleValuesReaderTaskCallbacks mCallbacks;

    /** The asynchronous task to read the values. */
    private ReadVehicleValuesTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the task's
     * current progress and results. The Android framework will pass us a
     * reference to the newly created Activity after each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getFragmentManager().addOnBackStackChangedListener(this);
        // Identify the target fragment (Compare Fragment) as the observer of this task.
        mCallbacks = (VehicleValuesReaderTaskCallbacks)getActivity();
    }

    /**
     * Set the target as the task callback. This method can be used in case the
     * target activity has been destroyed in a configuration change but still
     * needs to get notified about the progress updates from the task.
     */
    public void setTaskCallback() {
        mCallbacks = (VehicleValuesReaderTaskCallbacks)getActivity();
        if (mTask != null) {
            mTask.setTaskCallback(mCallbacks);
        }
    }

    /**
     * This method will only be called once when the retained Fragment is first
     * created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    /**
     * Restart the vehicle's values reading task. If there is an already running
     * task this must be stopped before starting the new one.
     *
     * @param pOperation the type of diagnostic operation to be executed
     */
    public void executeDiagnostic(String pOperation) {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
        mTask = new ReadVehicleValuesTask(mCallbacks);
        mTask.execute(pOperation);
    }

    /**
     * Allow to cancel the currently running task. This method can be called
     * from the UI thread to indicate that the user selected to cancel the task.
     */
    public void cancelTask() {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
    }

    /**
     * Indicate if the task is currently running
     *
     * @return true if the task is active
     */
    public boolean isReading() {
        return mTask != null && !mTask.isCancelled();
    }

    /**
     * Set the callback to null so we don't accidentally leak the Activity
     * instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Callback interface through which the fragment will report the task's
     * progress and results back to the Activity.
     */
    public static interface VehicleValuesReaderTaskCallbacks {
        /**
         * Callback method to perform operations before the task is executed.
         */
        void onPreExecute();

        /**
         * Callback method to perform operations during the execution of the
         * task and to report the progress which can be then shown in an UI
         * element.
         *
         * @param value a diagnostic value that has been read from the vehicle
         */
        void onProgressUpdate(Object value);

        /**
         * Callback method to perform the operations required when the task has
         * been cancelled.
         */
        void onCancelled();

        /**
         * Callback method to perform operations one the task has been
         * completed.
         */
        void onPostExecute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // Always call the superclass
        // Stop the task if it is still running
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
    }

    @Override
    public void onBackStackChanged() {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }

        if (getActivity() != null) {
            FragmentManager fm = getActivity().getFragmentManager();
            // Remove this fragment when the back button is pressed
            // There is no need to keep this fragment once the user leaves the activity to which it is associated.
            // The reading will be performed again when coming from the previous fragment.
            fm.beginTransaction().remove(this).commit();
        }
    }
}
