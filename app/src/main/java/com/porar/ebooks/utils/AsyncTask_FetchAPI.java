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

import plist.xml.PList;
import plist.xml.PListXMLHandler;
import plist.xml.PListXMLParser;
import android.app.AlertDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.porar.ebooks.event.OnFetchAPIListener;

public class AsyncTask_FetchAPI extends AsyncTask<String, Integer, PList> {

	private int currentIndex = 0;
	private int executeSize = 0;
	private String currentUrl = "";
	private boolean parsePlist = true;
	Handler handler = new Handler();
	AlertDialog alertDialog;

	private final LinkedList<OnFetchAPIListener> mListener = new LinkedList<OnFetchAPIListener>();
	
	public void setOnFetchListener(OnFetchAPIListener listener){
		mListener.add(listener);
	}
	
	
	@Override
	protected PList doInBackground(String... apiUrl) {
		executeSize = apiUrl.length;
		
		for(String eachUrl : apiUrl){
			URL url;
			try {
				currentUrl = eachUrl;
				url = new URL(currentUrl);
				
				for(OnFetchAPIListener eachListener: mListener){
					eachListener.onFetchStart(currentUrl, currentIndex);
//					System.out.println("Start Fetch API AT : " + currentUrl);
				}
				
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(3000);
				HttpURLConnection httpConnection = (HttpURLConnection)connection;
				int responseCode = httpConnection.getResponseCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK){
					InputStream in = httpConnection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null){
						sb.append(line);
					}
					in.close();
					
					if (parsePlist) {
						try{
							PListXMLParser parser = new PListXMLParser();
					        PListXMLHandler handler = new PListXMLHandler();
					        parser.setHandler(handler);
					        parser.parse(sb.toString());
					        
					        //Log.e("",eachUrl + "\n" + sb.toString());
					        
					        PList plist = ((PListXMLHandler)parser.getHandler()).getPlist();
					        if(plist!=null){
					        	onPostExecute(plist);
					        }else{
					        	onPostExecute(null);
					        }
						} catch(ParseException e){
							//Log.w(AppMain.getTag(), "ParseException : " + e.getMessage());
							for(OnFetchAPIListener eachListener: mListener){
								eachListener.onFetchError(currentUrl, currentIndex,e);
							}
							//Log.d(AppMain.getTag(), "ParseException "+e.toString());
							e.printStackTrace();
							onPostExecute(null);
						}
					}else{
						onPostExecute(null);
					}
				}
			} catch (MalformedURLException e) {
				for(OnFetchAPIListener eachListener: mListener){
					eachListener.onFetchError(currentUrl, currentIndex,e);
				}
				//Log.d(AppMain.getTag(), "MalformedURLException "+e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} 
			catch (SocketTimeoutException e) {
				for(OnFetchAPIListener eachListener: mListener){
					eachListener.onTimeOut(currentUrl, currentIndex);
				}
				//Log.d(AppMain.getTag(), "SocketTimeoutException "+e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} catch (ConnectException  e) {
				for(OnFetchAPIListener eachListener: mListener){
					eachListener.onTimeOut(currentUrl, currentIndex);
				}
				//Log.d(AppMain.getTag(), "ConnectException "+e.toString());
				e.printStackTrace();
				onPostExecute(null);
			} catch (IOException e) {
				for(OnFetchAPIListener eachListener: mListener){
					eachListener.onFetchError(currentUrl, currentIndex,e);
				}
				//Log.d(AppMain.getTag(), "IOException "+e.toString());
				// int pid = android.os.Process.myPid();
				// android.os.Process.killProcess(pid);
				e.printStackTrace();
				onPostExecute(null);
			}

			if(isCancelled()){
				break;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(PList result){
		if(result!=null){
			for(OnFetchAPIListener eachListener: mListener){
				eachListener.onFetchComplete(currentUrl, currentIndex,result);
			}
		}
		currentIndex++;
		if(currentIndex>executeSize){
			for(OnFetchAPIListener eachListener: mListener){
				eachListener.onAllTaskDone();
			}
			
			return;
		}
		super.onPostExecute(result);
	}

	public boolean isParsePlist() {
		return parsePlist;
	}

	public void setParsePlist(boolean parsePlist) {
		this.parsePlist = parsePlist;
	}

	
}