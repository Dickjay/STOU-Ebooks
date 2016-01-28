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

public class Publisher_Detail {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog = null;
	private String pcid = ""; // publisher id
	private String did = ""; // device id
	private String cid = ""; // customer id
	private AlertDialog alertDialog;
	private PList plist_PublisherEbook, plist_Publisherdetail;

	public Publisher_Detail(Context context, String pcid) {
		this.context = context;
		this.pcid = pcid;
		this.cid = "" + Shared_Object.getCustomerDetail.getCID(); // customer
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
		//		Log.e("onFetchStart", apiURL);

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
		//		Log.e("LoadEbooksDetailAPI", "" + apiURL);

				if (apiURL.contains("publisher-detail")) {
					plist_Publisherdetail = result;
				}
				if (apiURL.contains("publisher-ebooks")) {
					plist_PublisherEbook = result;
				}

			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList_Detail_Comment(plist_Publisherdetail, plist_PublisherEbook, progressDialog);
						}
					}
				});
			}
		});

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("publisher-detail", "pcid=" + pcid + "&cid=" + cid + "&did=" + did + "&provider="),
				AppMain.getAPIbyRefKey("publisher-ebooks", "cid=" + pcid + "&count="));

	}

}
