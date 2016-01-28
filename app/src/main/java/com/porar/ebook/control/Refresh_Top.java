package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.fragment.Fragment_Top;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
//import android.util.Log;

public class Refresh_Top {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	public boolean tag_category = false;

	public Refresh_Top(Context context) {
		this.context = context;
		this.tag_category = false;
	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);

	}

	public void refreshCategortAPI() {
		LoadRefreshAPI();
	}

	public void LoadRefreshAPI() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				progressDialog = ProgressDialog.show(context, "", "Loading..", true, true);

			}
		});
		asyncTask_FetchAPI = new AsyncTask_FetchAPI();
		asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

			@Override
			public void onTimeOut(String apiURL, int currentIndex) {

				handler.post(new Runnable() {

					@Override
					public void run() {

						progressDialog.dismiss();

						alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle(AppMain.getTag());
						alertDialog.setMessage("A problem occurred while Internet Connection");
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								dialog.dismiss();
								System.gc();
								LoadRefreshAPI();
							}
						});
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								int pid = android.os.Process.myPid();
								android.os.Process.killProcess(pid);
								Runtime r = Runtime.getRuntime();

								r.gc();
								System.gc();
							}
						});
						alertDialog.show();

					}
				});

			}

			@Override
			public void onFetchStart(String apiURL, int currentIndex) {
		//		Log.e("onFetchStart", apiURL);
			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {

				handler.post(new Runnable() {

					@Override
					public void run() {

						progressDialog.dismiss();

						alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle(AppMain.getTag());
						alertDialog.setMessage("A problem occurred while Internet Connection");
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								dialog.dismiss();
								System.gc();
								LoadRefreshAPI();
							}
						});
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								int pid = android.os.Process.myPid();
								android.os.Process.killProcess(pid);
								Runtime r = Runtime.getRuntime();

								r.gc();
								System.gc();
							}
						});
						alertDialog.show();

					}
				});

			}

			@Override
			public void onFetchComplete(String apiURL, int currentIndex, final PList result) {
		//		Log.e("refresh_api", "" + apiURL);
				AppMain.pList_default_top = result;

				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList(result, progressDialog);
						}
					}
				});

			}

			@Override
			public void onAllTaskDone() {

			}
		});

		if (Fragment_Top.lastUrl != null) {
			if (Fragment_Top.lastUrl.getUrl() != null) {
				String new_url = Fragment_Top.lastUrl.getUrl();
				if (new_url.toString().length() > 0) {
					asyncTask_FetchAPI.execute(Fragment_Top.lastUrl.getUrl());
					tag_category = true;
				}
			}
			if (AppMain.pList_default_top != null) {
				if (!tag_category) {
					asyncTask_FetchAPI.execute(AppMain.getAPIbyRefKey("top100", "type=D&period=D&catid=0&retina=0"));
				}
			} else {
				if (!tag_category) {
					asyncTask_FetchAPI.execute(AppMain.getAPIbyRefKey("top100", "type=D&period=D&catid=0&retina=0"));
				}
			}

		}

	}

}
