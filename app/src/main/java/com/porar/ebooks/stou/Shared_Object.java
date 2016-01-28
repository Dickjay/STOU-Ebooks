package com.porar.ebooks.stou;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_STOU_Students;

@SuppressLint("NewApi")
public class Shared_Object extends Application {
	public static Model_STOU_Students getStudentStous;
	public static Model_Customer_Detail getCustomerDetail;
	public static ArrayList<String> embedBooksBID = new ArrayList<String>();

	// Flag Application
	public static boolean isOfflineMode = false;
	public static int screenDensity = 0;

	// Facebook
	public static String facebookAppID = "111674755548929";
	public static String DeviceModel = android.os.Build.MODEL;

	public static void loadConfigData(Context context) {

		File dir = new File(Environment.getExternalStorageDirectory() + "/STOU");
		if (!dir.exists()) {
			dir.mkdirs();
			Log.e(AppMain.getTag(), "Shared_Object - Prepare Directory");
		}
		Class_Manage.ListFileInApp(context);

		getCustomerDetail = Class_Manage.LoadCustomerHeaderFile(context);
		embedBooksBID = Class_Manage.LoadEbooksEmbedList(context);
	}

	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static boolean checkInternetConnection(Context context) {
		int version_code = android.os.Build.VERSION.SDK_INT;
		if (version_code >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			if (conn.getActiveNetworkInfo().isConnectedOrConnecting()) {

				URL url = new URL("http://ebooks.in.th/android/_/android-check-internet.php");
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setRequestProperty("User-Agent", "Ebooks.in.th - Device ID :" + getDeviceID(context));

				urlConn.setConnectTimeout(2 * 1000);// mTimeout is in seconds
				urlConn.setReadTimeout(5* 1000);
				urlConn.connect();

				if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

					InputStream in = urlConn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					in.close();

					if (sb.toString().equals("http://www.ebooks.in.th")) {
						Log.e(AppMain.getTag(), "Shared_Object - checkInternetConnection[Work Online]");
						isOfflineMode = false;
						return true;
					} else {
						Log.e(AppMain.getTag(), "Shared_Object - checkInternetConnection[Work Offline]");
						isOfflineMode = true;
						return false;
					}
				}
			}

		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

}
