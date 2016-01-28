package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
//import android.util.Log;

public class Ebook_BackIssue {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private final ProgressDialog progressDialog = null;
	private String bid = "";

	private PList plist_detail;

	public Ebook_BackIssue(Context context, String bid) {
		this.context = context;
		this.bid = bid;

	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);

	}

	public void refreshEbooksDetailAPI() {
		LoadEbooksDetailAPI();
	}

	public void LoadEbooksDetailAPI() {

		asyncTask_FetchAPI = new AsyncTask_FetchAPI();
		asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

			@Override
			public void onTimeOut(String apiURL, int currentIndex) {

			}

			@Override
			public void onFetchStart(String apiURL, int currentIndex) {
		//		Log.e("onFetchStart", apiURL);

			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {
		//		Log.e("onFetchError", e.toString());

			}

			@Override
			public void onFetchComplete(final String apiURL, int currentIndex, final PList result) {
	//			Log.w("LoadEbooksDetailAPI", "" + apiURL);

				if (apiURL.contains("back-issues")) {
					plist_detail = result;
				}
			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList(plist_detail, progressDialog);
						}
					}
				});
			}
		});

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("back-issues", "bid=" + bid + "&count=20&includeafter=1&retina=0"));

	}

}
