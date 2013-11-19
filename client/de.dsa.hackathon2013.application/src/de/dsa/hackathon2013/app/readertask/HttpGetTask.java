package de.dsa.hackathon2013.app.readertask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import de.dsa.hackathon2013.app.DSADiagnosticsActivity;

public class HttpGetTask extends AsyncTask<String, String, String>{
DSADiagnosticsActivity callback;
	
	public HttpGetTask(DSADiagnosticsActivity activity) {
		callback = activity;
	}
	
	protected void onPreExecute() {
		System.out.println("Started http task");
    }

    @Override
    protected String doInBackground(String... uri) {
    	System.out.println("Started executing HttpTask");
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("Got a result!");
        callback.onResponse(result);

    }
}
