package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Comment_Detail {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private final ProgressDialog progressDialog = null;
	private String bid = "";
	private String cid = "";
	private String did = "";
	private AlertDialog alertDialog;
	private PList plist_comment, plist_detail;

	public Comment_Detail(Context context, String bid) {
		this.context = context;
		this.bid = bid;
		this.cid = "0"; // customer id
		this.did = Shared_Object.getDeviceID(context);
	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);

	}

	public void refreshEbooksDetailAPI() {
		LoadEbooksDetailAPI();
	}

	public boolean CancelTask() {
		if (asyncTask_FetchAPI != null) {
			asyncTask_FetchAPI.cancel(true);
			return true;
		}
		return false;

	}

	public void LoadEbooksDetailAPI() {

		asyncTask_FetchAPI = new AsyncTask_FetchAPI();
		asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

			@Override
			public void onTimeOut(String apiURL, int currentIndex) {

			}

			@Override
			public void onFetchStart(String apiURL, int currentIndex) {
				Log.e("onFetchStart", apiURL);
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.StartLoadPList();
						}
					}
				});
			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {
				Log.e("onFetchError", e.toString());

			}

			@Override
			public void onFetchComplete(final String apiURL, int currentIndex, final PList result) {
	//			Log.w("LoadEbooksDetailAPI", "" + apiURL);
				if (apiURL.contains("comments-list")) {
					plist_comment = result;
				}

			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList(plist_comment, null);
						}
					}
				});
			}
		});

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("comments-list", "bid=" + bid + "&page="));

	}

}
