package com.porar.ebooks.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import android.os.AsyncTask;

import com.porar.ebooks.event.OnFetchURLListener;

public class AsyncTask_FetchURL extends AsyncTask<String, Integer, String> {

	private int currentIndex = 0;
	private int executeSize = 0;
	private String currentUrl = "";

	private final LinkedList<OnFetchURLListener> mListener = new LinkedList<OnFetchURLListener>();

	public void setOnFetchListener(OnFetchURLListener listener) {
		mListener.add(listener);
	}

	@Override
	protected String doInBackground(String... apiUrl) {
		executeSize = apiUrl.length;

		for (String eachUrl : apiUrl) {
			URL url;
			try {
				currentUrl = eachUrl;
				url = new URL(currentUrl);
				StringBuilder sb = new StringBuilder();
				
				for (OnFetchURLListener eachListener : mListener) {
					eachListener.onFetchStart(currentUrl, currentIndex);
				}

				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(5000);
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				int responseCode = httpConnection.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream in = httpConnection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					in.close();
					onPostExecute(sb.toString());
				}
			} catch (MalformedURLException e) {
				for (OnFetchURLListener eachListener : mListener) {
					eachListener.onFetchError(currentUrl, currentIndex, e);
				}
				//Log.d(AppMain.getTag(), "MalformedURLException " + e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} catch (SocketTimeoutException e) {
				for (OnFetchURLListener eachListener : mListener) {
					eachListener.onTimeOut(currentUrl, currentIndex);
				}
				//Log.d(AppMain.getTag(), "SocketTimeoutException " + e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} catch (ConnectException e) {
				for (OnFetchURLListener eachListener : mListener) {
					eachListener.onTimeOut(currentUrl, currentIndex);
				}
				//Log.d(AppMain.getTag(), "ConnectException " + e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} catch (IOException e) {
				for (OnFetchURLListener eachListener : mListener) {
					eachListener.onFetchError(currentUrl, currentIndex, e);
				}
				//Log.d(AppMain.getTag(), "IOException " + e.toString());
				e.printStackTrace();
				onPostExecute(null);
			}

			if (isCancelled()) {
				break;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		for (OnFetchURLListener eachListener : mListener) {
			eachListener.onFetchComplete(currentUrl, currentIndex, result);
		}
		currentIndex++;
		if (currentIndex > executeSize) {
			for (OnFetchURLListener eachListener : mListener) {
				eachListener.onAllTaskDone();
			}
			return;
		}
		super.onPostExecute(result);
	}
}