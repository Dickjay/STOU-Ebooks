package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.fragment.Fragment_Publisher;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Category_Publisher {

	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private String type = "";
	private AlertDialog alertDialog;

	public Category_Publisher(Context context, String type) {
		this.context = context;
		this.type = type;

	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);

	}

	public void refreshCategortAPI() {
		LoadCategoryAPI();
	}

	public void LoadCategoryAPI() {

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
								LoadCategoryAPI();
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

				if (Fragment_Publisher.lastUrl != null) {
					Fragment_Publisher.lastUrl.setUrl(apiURL);
				}
			}

			@Override
			public void onFetchError(String apiURL, int currentIndex, Exception e) {
				Log.e("onFetchError", e.toString());
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
								LoadCategoryAPI();
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
			public void onFetchComplete(final String apiURL, int currentIndex, final PList result) {
				//Log.e("LoadCategoryAPI", "" + apiURL);
				AppMain.pList_default_publisher = result;
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

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("publishers-list", "type=" + type + "&short=&page=&count=100"));

	}

}
