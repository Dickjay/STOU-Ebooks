package com.porar.ebook.control;

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
import java.util.ArrayList;

import android.os.AsyncTask;

import com.porar.ebooks.event.OnLoadResultFromHttpGet;

public class LoadAPIResultString extends AsyncTask<String, Integer, String> {

	ArrayList<OnLoadResultFromHttpGet> aLoadResults = new ArrayList<OnLoadResultFromHttpGet>();

	public void setOnLoadResultMethodGet(OnLoadResultFromHttpGet loadResult) {
		aLoadResults.add(loadResult);
	}

	private String readStream(InputStream is) {
		String result = null;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			result = sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}

		return result;

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			URL connectionurl = new URL(params[0]);
			URLConnection connection = connectionurl.openConnection();
			connection.setConnectTimeout(5000);
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			

			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readStream(httpConnection.getInputStream());
			} 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return null;

		} catch (ConnectException e) {
			e.printStackTrace();
			return null;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	protected void onPostExecute(String result) {
		for (OnLoadResultFromHttpGet each : aLoadResults) {
			each.completeResult(result);
		}
		super.onPostExecute(result);
	}
}
