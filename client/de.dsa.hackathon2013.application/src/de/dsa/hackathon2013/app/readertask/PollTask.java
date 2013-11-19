package de.dsa.hackathon2013.app.readertask;


import java.util.Timer;

import android.os.AsyncTask;
import de.dsa.hackathon2013.app.DSADiagnosticsActivity;

public class PollTask extends AsyncTask<String, String, String>{
	DSADiagnosticsActivity callback;
	
	public PollTask(DSADiagnosticsActivity activity) {
		callback = activity;
	}

    @Override
    protected String doInBackground(String ... args) {
    	System.out.println("Telefonmann");
    	int iteration = 0;
    	/*while(true) {
    		new HttpGetTask(this.callback).execute("http://www.svensblog.eu/index.php/poll/getValues/1112");
    		publishProgress(Integer.toString(iteration));
    		SystemClock.sleep(500);
    	}
    	*/
    	
    	Timer timer = new Timer();
    	
    	PollTimerTask timerTask = new PollTimerTask(this.callback);
    	timer.scheduleAtFixedRate(timerTask, 0, 500);
    	
    	return "";
    	
    }
    
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        System.out.println(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        //callback.onResponse(result);
    }
}
