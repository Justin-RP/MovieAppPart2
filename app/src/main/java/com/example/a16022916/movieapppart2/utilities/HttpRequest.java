package com.example.a16022916.movieapppart2.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest {

	private static final String DEBUG_TAG = "HttpRequest";
	private String url;
	private String method;
	private StringBuffer paramString = new StringBuffer();
	private HttpRequestConnection hrc = new HttpRequestConnection();

	private OnHttpResponseListener mOnHttpResponseListener;

	public HttpRequest(String url){
		this.url = url;
		this.paramString = new StringBuffer();
		this.hrc = new HttpRequestConnection();
		this.method = "GET";

	}

	public void setMethod(String method){
		if(method.equalsIgnoreCase("GET")||method.equalsIgnoreCase("POST")||method.equalsIgnoreCase("DELETE")||method.equalsIgnoreCase("PUT")){
			this.method = method;
		}else{
			Log.d(DEBUG_TAG, "No such HTTP method:" + method);
		}		
	}

	public void addData(String key, String value){
		if (paramString.length()==0){
			paramString.append(key + "=" + value);
		} else {
			paramString.append("&" + key + "=" + value);
		}
	}

	public void execute(){
		hrc.execute(method, url, paramString.toString());
		return;
	}

	public String getResponse() {
		try{
			return hrc.get();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void onResponse(String r){

	}

	public void setOnHttpResponseListener(OnHttpResponseListener r){
        mOnHttpResponseListener = r;
	}

    public interface OnHttpResponseListener {
        void onResponse(String response);
    }

    private class HttpRequestConnection extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... strings){
			try {
				return postRequest(strings[0], strings[1], strings[2]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}			
		}

		protected void onPostExecute(String result){
            mOnHttpResponseListener.onResponse(result);
			
		}

		private String postRequest(String... strings) throws IOException {

			HttpURLConnection conn = null;

			try {
				URL url = new URL(strings[1]);
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(20000 /* milliseconds */);
				conn.setConnectTimeout(25000 /* milliseconds */);
				conn.setRequestMethod(strings[0]);
				conn.setDoInput(true);
				if((strings[0].equalsIgnoreCase("POST"))){
					conn.setDoOutput(true);
					writeOutput(conn, strings[2]);
				}			

				conn.connect();

				int response = conn.getResponseCode();
				Log.d(DEBUG_TAG, "The response is: " + response);

				String contentAsString = readResponse(conn.getInputStream());
				return contentAsString;

			} finally {
				if (conn != null) {
					conn.disconnect();
				} 
			}
		}

		private void writeOutput(HttpURLConnection conn, String paramString) {

			try{
				OutputStream os = new BufferedOutputStream(conn.getOutputStream());
				os.write(paramString.getBytes());
				os.flush();
				os.close();

			}catch(Exception e){
				e.printStackTrace();
			}
		}

		private String readResponse(InputStream stream) throws IOException {

			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String inputLine;
			StringBuffer html = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine);
			}
			stream.close();
			return new String(html);
		}
	}

}