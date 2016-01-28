package com.twitter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

import com.porar.ebooks.event.OnLoadResultFromHttpGet;

public class Upload_Twittpic extends AsyncTask<File, Void, String> {
	String result = null;
	String url_stat;

	ArrayList<OnLoadResultFromHttpGet> aLoadResults = new ArrayList<OnLoadResultFromHttpGet>();

	public void setOnLoadResultMethodGet(OnLoadResultFromHttpGet loadResult) {
		aLoadResults.add(loadResult);
	}

	public Upload_Twittpic() {

		url_stat = "https://api.twitter.com/1.1/statuses/update_with_media.json";

	}

	private String uploadFiletoServer(File strFile) {
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		int resCode = 0;
		String resMessage = "";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			if (!strFile.exists()) {
				return "{\"StatusID\":\"0\",\"Error\":\"Please check path on SD Card\"}";
			}
			FileInputStream fileInputStream = new FileInputStream(strFile);
			URL url = new URL(url_stat);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"media[]\";filename=\"" + strFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Response Code and Message
			resCode = conn.getResponseCode();
			if (resCode == HttpURLConnection.HTTP_OK) {

				InputStream is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int read = 0;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}
				byte[] result = bos.toByteArray();
				bos.close();
				resMessage = new String(result);
			}

			// Log.d("resCode=", Integer.toString(resCode));
			// Log.d("resMessage=", resMessage.toString());
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

			return resMessage.toString();

		} catch (Exception ex) {
			return null;
		}

	}

	@SuppressWarnings("unused")
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
	protected String doInBackground(File... params) {
		return uploadFiletoServer(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			Log.e("ress server ", result);
		}

		super.onPostExecute(result);
	}

}
