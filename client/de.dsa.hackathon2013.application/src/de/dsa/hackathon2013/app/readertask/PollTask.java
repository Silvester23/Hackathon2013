package de.dsa.hackathon2013.app.readertask;


import android.os.AsyncTask;
import de.dsa.hackathon2013.app.DSADiagnosticsActivity;

public class PollTask extends AsyncTask<String, String, String>{
	DSADiagnosticsActivity callback;
	
	public PollTask(DSADiagnosticsActivity activity) {
		callback = activity;
	}

    @Override
    protected String doInBackground(String ... args) {
    	int iteration = 0;
    	
    	new HttpGetTask(this).execute("http://www.svensblog.eu/index.php/poll/getValues/1112");
    	new HttpGetTask(this).execute("http://www.svensblog.eu/index.php/poll/getValues/1112");
    	new HttpGetTask(this).execute("http://www.svensblog.eu/index.php/poll/getValues/1112");
    	
    	
        while(true) {
        	try {
        		//System.out.println("Iteration: " + iteration);
        		iteration++;
        	    Thread.sleep(200);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        }
    }
    
    public void onResponse(String result) {
    	this.callback.onResponse(result);
    }

    @Override
    protected void onPostExecute(String result) {
        //callback.onResponse(result);
    }
}
