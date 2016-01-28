package com.porar.ebooks.utils;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public abstract class Class_APIParser {

	private String apiURL = null;

	public Class_APIParser(String strURL) {
		this.apiURL = strURL;
	}

	public void getURLDataContext() {
		try {
			String connent = "";
			URL url = new URL(this.apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(8000);
			DataInputStream input = new DataInputStream(conn.getInputStream());
			for (int c = input.read(); c != -1; c = input.read()) {
				connent += (char) c;
			}
			input.close();

			onComplete(connent);
		} catch (MalformedURLException e) {
			onError();
		} catch (FileNotFoundException e) {
			onError();
		} catch (SocketTimeoutException e) {
			onError();
		} catch (IOException e) {
			onError();
		}
	}

	protected abstract void onComplete(String txt);

	protected abstract void onError();
}
