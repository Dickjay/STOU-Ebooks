package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
//import android.util.Log;

public class Publisher_Fan {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog = null;
	private String cid = "";
	private String did = "";
	private AlertDialog alertDialog;
	private PList plist;

	public Publisher_Fan(Context context) {
		this.context = context;
		this.cid = "" + Shared_Object.getCustomerDetail.getCID(); // customer id
		this.did = Shared_Object.getDeviceID(context);
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
			//	Log.e("onFetchStart", apiURL);

				handler.post(new Runnable() {

					@Override
					public void run() {
						if (progressDialog == null) {
							progressDialog = ProgressDialog.show(context, "", "Loading..", true, true);
						}
					}
				});
			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {
		//		Log.e("onFetchError", e.toString());

			}

			@Override
			public void onFetchComplete(final String apiURL, int currentIndex, final PList result) {
			//	Log.w("LoadEbooksDetailAPI", "" + apiURL);

				if (apiURL.contains("fans-update")) {
					plist = result;
				}
			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList(plist, progressDialog);
						}
					}
				});
			}
		});

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("fans-update", "cid=" + cid));

	}

	public void cacelTask() {
		if (asyncTask_FetchAPI != null) {
			asyncTask_FetchAPI.cancel(true);
		}
	}

}
