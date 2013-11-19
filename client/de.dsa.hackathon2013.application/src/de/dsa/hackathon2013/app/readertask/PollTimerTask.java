package de.dsa.hackathon2013.app.readertask;

import java.util.TimerTask;

import de.dsa.hackathon2013.app.DSADiagnosticsActivity;

public class PollTimerTask extends TimerTask {
	int mIteration;
	DSADiagnosticsActivity mCallback;
	
	public PollTimerTask(DSADiagnosticsActivity callback) {
		mIteration = 0;
		mCallback = callback;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		new HttpGetTask(mCallback).execute("http://www.svensblog.eu/index.php/poll/getValues/1112");
		System.out.println(mIteration);
		mIteration++;
	}

}
