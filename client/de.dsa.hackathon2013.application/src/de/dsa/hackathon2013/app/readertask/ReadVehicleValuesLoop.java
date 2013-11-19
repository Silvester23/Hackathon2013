package de.dsa.hackathon2013.app.readertask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.Time;
import de.dsa.hackathon2013.DiagnosticNames;
import de.dsa.hackathon2013.VehicleDiagnosisApi;
import de.dsa.hackathon2013.app.DSADiagnosticsActivity;
import de.dsa.hackathon2013.app.DiagnosticValue;
import de.dsa.hackathon2013.app.readertask.DiagnosticReaderFragment.VehicleValuesReaderTaskCallbacks;
import de.dsa.hackathon2013.lib.IVehicleDiagnosisApi;
import de.dsa.hackathon2013.lib.MirrorMovingDirection;
import de.dsa.hackathon2013.lib.WindowMovingDirection;
import de.dsa.smartdiag.interpreter.provider.SmartDiagDataProviderFactory;
import de.dsa.smartdiag.vciapi.util.VciConstants;

/**
 * A task that performs the reading operation as a background task and proxies
 * progress updates and results back to the Diagnostics Activity.
 *
 * Note that we need to check if the callbacks are null in each method in case
 * they are invoked after the Activity's onDestroy() method has been called.
 */
public class ReadVehicleValuesLoop extends AsyncTask<String, Object, Void> {
	// Sleep interval in ms
	private static final int SLEEP_INTERVAL = 1000;
	
	private int mTripId;
	private String mVehicleId;
	
	private double mPreviousTime;
	private double mTime;
	private double mElapsedTime;
	
	// Total distance of this trip in km
	private double mTripDistance;
	private double mDistanceUpdate;
	
	// Current velocity in km/h
	private double mSpeed;
	private double mAverageSpeed;
	
	// Fuel consumption in liters
	private double mInitialFuel;
	private double mConsumedFuel;
	private double mAverageFuelConsumption; // in liters/100km
	
    /** Directory where the vehicle information is stored in the device. */
    private static final String DIRECTORY_VEHICLE_DATA = "/DSA/vehicle_data";

    /** The object that reacts to the output of the task. In this case will be DSADiagnostcisActivity. */
    private VehicleValuesReaderTaskCallbacks mCallbacks;

    /** The object that provides the API to perform vehicle communication. */
    private IVehicleDiagnosisApi mVehicleCommunication;

    /**
     * Creates a new instance of this task with an object to which it will
     * report the progress updates and the final output.
     *
     * @param pCallbacks
     *            The object to which the progress and output is reported
     */
    public ReadVehicleValuesLoop(VehicleValuesReaderTaskCallbacks pCallbacks) {
        mCallbacks = pCallbacks;
        configureVehicleCommunication();
    }

    /**
     * Setup the vehicle data for the Passat vehicle.
     * This method will:
     * 1. Read from the external storage DSA directory the data corresponding
     * to the vehicle.
     * 2. Setup the vehicle communication API to access the control and readings methods.
     */
    private void configureVehicleCommunication() {
        if (mVehicleCommunication == null) {
            // Initialize Vehicle Data from the information stored in the device
            File rootDir = Environment.getExternalStorageDirectory();
            SmartDiagDataProviderFactory.getProvider().setVehicleDatas(rootDir + DIRECTORY_VEHICLE_DATA + "/Passat");

            // Read vehicle data and create the communication API
            mVehicleCommunication = new VehicleDiagnosisApi();
        }
    }

    /**
     * Set the callback object to which the task must notify about changes
     * during execution.
     *
     * @param pCallbacks
     *            the callback object
     */
    public void setTaskCallback(VehicleValuesReaderTaskCallbacks pCallbacks) {
        mCallbacks = pCallbacks;
    }

    @Override
    protected void onPreExecute() {
        // invoked on the UI thread before the task is executed. This step is
        // normally used to setup the task, for instance by showing a progress
        // bar in the user interface.
        if (mCallbacks != null) {
            mCallbacks.onPreExecute();
        }
    }

    @Override
    protected Void doInBackground(String... pOperation) {
        String operation = pOperation[0];

        // Configure the IP Address of the WDI. Must be performed before any
        // operation since it could have been changed between operations.
        setIpAddress();

        if (operation.equals("Measurements")) {
        	// Initialize fuel
        	mInitialFuel = mVehicleCommunication.readFuelTankReserve();
        	mConsumedFuel = 0;
        	mAverageFuelConsumption = 0;
        	
        	mSpeed = 0;
        	mTripDistance = 0;
        	mDistanceUpdate = 0;
        	mAverageSpeed = 0;
        	
        	// Initialize Time
        	mTime = (double)System.currentTimeMillis() / 1000 / 3600;
        	mPreviousTime = (double)System.currentTimeMillis() /1000 / 3600;
        	mElapsedTime = 0;
        	
        	// Get some static values
        	mVehicleId = getVehicleId();
        	mTripId = requestTripId();
        	
        	// This loop continuously requests vehicle data
        	while (true) {
        		// Get time delta
        		updateSpeed();
        		updateFuel();
        		updateTime();
        		updateTripDistance(mTime - mPreviousTime);
        		updateAverageSpeed();
        		updateAverageFuelConsumption();
        		
        		// Test output
        		// Populate update
                onProgressUpdate("Vehicle ID: " + mVehicleId + "\n" +
                				 "Trip ID: " + mTripId + "\n" +
                				 "Trip distance: " + Double.toString(mTripDistance) + "km\n" +
                				 "Consumed fuel: " + Double.toString(mConsumedFuel) + "liters\n" +
                				 "Average consumption: " + Double.toString(mAverageFuelConsumption) + "liters/100km\n" +
                				 "Average speed: " + Double.toString(mAverageSpeed) + "km/h");
        		
        		// Wait...
        		try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        } else {
            // Execute the control command selected by the user
            executeControl(operation);
        }
        return null;
    }
    
    protected String getVehicleId() {
    	return "MyCar"; //mVehicleCommunication.readVehicleIdentificationNumber();
    }
    
	protected int requestTripId() {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        int responseInt = -1;
        try {
            response = httpclient.execute(new HttpGet(new URI("http://svensblog.eu/index.php/push/getNewTrip/" + mVehicleId)));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                responseInt = Integer.parseInt(responseString);
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch (Exception e) {
        	System.out.println("Failed getting trip id from server");
        }
        
        return responseInt;
	}
    
    protected void updateTime() {
    	mPreviousTime = mTime;
    	mTime = (double)System.currentTimeMillis() / 1000 / 3600;
    	mElapsedTime += mTime - mPreviousTime;
    }
    
    protected void updateSpeed() {
    	// Get actual speed
        //mSpeed = mVehicleCommunication.readVehicleSpeed();
        
        // Random modifications
        Random rand = new Random();
        mSpeed = 60 + ((rand.nextFloat() * 40) - 20);
    }
    
    protected void updateFuel() {
    	// Actual consumption
    	//float currentFuel = mVehicleCommunication.readFuelTankReserve();
    	//mConsumedFuel = mInitialFuel - currentFuel;
    	
    	// Fake consumption: 8 liter/100km
    	Random rand = new Random();
    	mConsumedFuel += mDistanceUpdate / 100 * ((rand.nextDouble() * 4) + 6);
    }
    
    protected void updateTripDistance(double timeDelta) {
    	mDistanceUpdate = mSpeed * timeDelta;
    	mTripDistance += mDistanceUpdate;
    }
    
    protected void updateAverageSpeed() {
    	mAverageSpeed = mTripDistance / mElapsedTime;
    }
    
    protected void updateAverageFuelConsumption() {
    	mAverageFuelConsumption = mConsumedFuel / mTripDistance * 100;
    }

    @Override
    protected void onProgressUpdate(Object... value) {
        // Invoked on the UI thread after a call to publishProgress(Progress...).
        // The timing of the execution is undefined. This method is used to
        // display any form of progress in the user interface while the background
        // computation is still executing. For instance, it can be used to animate a
        // progress bar or show logs in a text field.
        if (mCallbacks != null && value.length > 0) {
            mCallbacks.onProgressUpdate(value[0]);
        }
    }

    @Override
    protected void onCancelled() {
        if (mCallbacks != null) {
            mCallbacks.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(Void ignore) {
        if (mCallbacks != null) {
            mCallbacks.onPostExecute();
        }
    }

    /**
     * Set the IP Address of the WDI before performing any operation
     * on the vehicle.
     *
     * Default is set to 192.168.126.2, since it is the default WDI
     */
    private void setIpAddress() {
        if (mCallbacks instanceof Activity) {
            Activity activity = (Activity)mCallbacks;
            // Get from preferences stored in the settings dialog
            final SharedPreferences mPreferenceSettings = activity.getSharedPreferences(DSADiagnosticsActivity.SETTINGS_PREFERENCES, 0);
            String storedAddress = mPreferenceSettings.getString(DSADiagnosticsActivity.IPADDRESS_PREFERENCE, "192.168.126.2");
            VciConstants.getInstance().setVCI5Ip(storedAddress);
        }
    }

    /**
     * Execute a control command in the vehicle. These operations will not return any
     * value but will execute an action in the vehicle.
     *
     * @param pAction the action to be performed
     */
    private void executeControl(String pAction) {
        if (pAction.equals(DiagnosticNames.MIRROR_UP)) {
            onProgressUpdate("Moving Mirror Up");
            mVehicleCommunication.moveDriverSideMirror(MirrorMovingDirection.UP);
        } else if (pAction.equals(DiagnosticNames.MIRROR_DOWN)) {
            onProgressUpdate("Moving Mirror Down");
            mVehicleCommunication.moveDriverSideMirror(MirrorMovingDirection.DOWN);
        } else if (pAction.equals(DiagnosticNames.MIRROR_LEFT)) {
            onProgressUpdate("Moving Mirror Left");
            mVehicleCommunication.moveDriverSideMirror(MirrorMovingDirection.LEFT);
        } else if (pAction.equals(DiagnosticNames.MIRROR_RIGHT)) {
            onProgressUpdate("Moving Mirror Right");
            mVehicleCommunication.moveDriverSideMirror(MirrorMovingDirection.RIGHT);
        } else if (pAction.equals(DiagnosticNames.WINDOW_UP)) {
            onProgressUpdate("Moving Window Up");
            mVehicleCommunication.moveDriverWindow(WindowMovingDirection.UP);
        } else if (pAction.equals(DiagnosticNames.WINDOW_DOWN)) {
            onProgressUpdate("Moving Window Down");
            mVehicleCommunication.moveDriverWindow(WindowMovingDirection.DOWN);
        } else if (pAction.equals(DiagnosticNames.DOOR_LOCK)) {
            onProgressUpdate("Lock Door");
            mVehicleCommunication.setDriverSideDoor(true);
        } else if (pAction.equals(DiagnosticNames.DOOR_UNLOCK)) {
            onProgressUpdate("Unlock Door");
            mVehicleCommunication.setDriverSideDoor(false);
        }
    }

    /**
     * Read the measurement values from the vehicle all at once.
     */
    private void readMeasurementValues() {
    	onProgressUpdate("Reading: Telefonmann!");
        //String clockValue = mVehicleCommunication.readClock().toString();
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.CLOCK, "DAS TELEFON KLINGELT", ""));
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.CLOCK, "ICH BIN IM KELLER", ""));
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.CLOCK, "VIELLEICHT NUR VERWAEHLT", ""));
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.CLOCK, "DOCH ICH BIN SCHNELLER", ""));
    	
        onProgressUpdate("Reading: " + DiagnosticNames.CLOCK);
        String clockValue = mVehicleCommunication.readClock().toString();
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.CLOCK, clockValue, ""));

        onProgressUpdate("Reading: " + DiagnosticNames.FUEL_RESERVE);
        String fuelReserve = Float.toString(mVehicleCommunication.readFuelTankReserve());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.FUEL_RESERVE, fuelReserve, "l"));

        onProgressUpdate("Reading: " + DiagnosticNames.FUELTANK_NEEDLE_POSITION);
        String fuelNeedlePosition = Float.toString(mVehicleCommunication.readFuelTankNeedlePosition());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.FUELTANK_NEEDLE_POSITION, fuelNeedlePosition, "°"));

        onProgressUpdate("Reading: " + DiagnosticNames.OUTER_TEMPERATURE);
        String temperatureValue = Float.toString(mVehicleCommunication.readEnvironmentTemperature());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.OUTER_TEMPERATURE, temperatureValue, "°C"));

        onProgressUpdate("Reading: " + DiagnosticNames.OIL_TEMPERATURE);
        String oilTemperatureValue = Float.toString(mVehicleCommunication.readEngineOilTemperature());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.OIL_TEMPERATURE, oilTemperatureValue, "°C"));

        onProgressUpdate("Reading: " + DiagnosticNames.PHOTO_TRANSISTOR);
        String transitorValue = Float.toString(mVehicleCommunication.readEnvironmentelLightValue());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.PHOTO_TRANSISTOR, transitorValue, "%"));

        onProgressUpdate("Reading: " + DiagnosticNames.COOLANT_TEMPERATURE);
        String coolantTemperature = Float.toString(mVehicleCommunication.readEngineCoolantTemperature());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.COOLANT_TEMPERATURE, coolantTemperature, "°C"));

        onProgressUpdate("Reading: " + DiagnosticNames.ENGINE_RPM);
        String engineRpm = Float.toString(mVehicleCommunication.readEngineRpm());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.ENGINE_RPM, engineRpm, "/min"));

        onProgressUpdate("Reading: " + DiagnosticNames.SPEED_SENSOR);
        String speed = Float.toString(mVehicleCommunication.readVehicleSpeed());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.SPEED_SENSOR, speed, "km/h"));

        onProgressUpdate("Reading: " + DiagnosticNames.ACCELERATOR_POSITION);
        String pedalPosition = Float.toString(mVehicleCommunication.readAcceleratorPedalPosition());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.ACCELERATOR_POSITION, pedalPosition, "%"));

        onProgressUpdate("Reading: " + DiagnosticNames.RANGE);
        String range = Float.toString(mVehicleCommunication.readRange());
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.RANGE, range, "km"));

        onProgressUpdate("Reading: " + DiagnosticNames.DOOR_STATE);
        String doorState = mVehicleCommunication.readDoorStates().toString();
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.DOOR_STATE, doorState, ""));

        onProgressUpdate("Reading: " + DiagnosticNames.VEHICLE_ID_NUMBER);
        String vehicleIdNumber = mVehicleCommunication.readVehicleIdentificationNumber();
        onProgressUpdate(new DiagnosticValue(DiagnosticNames.VEHICLE_ID_NUMBER, vehicleIdNumber, ""));
    }
}