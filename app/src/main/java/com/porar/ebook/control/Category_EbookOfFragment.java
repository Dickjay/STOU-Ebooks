package com.porar.ebook.control;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Category_EbookOfFragment extends Category_Ebook {
	private boolean top50 = false;
	private PList pList1, pList2;
	private int count = 0;

	public Category_EbookOfFragment(Context context, String catid, String type, Page page) {
		super(context, catid, type);
		setPageCurrent(page);
	}

	public Category_EbookOfFragment(Context context, String catid, String period, String type, Page page) {
		super(context, catid, period, type);
		setPageCurrent(page);
	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);
	}

	public void LoadTop50period(boolean top50) {
		this.top50 = top50;
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
								System.out.println("Retry");
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
				Log.e("onFetchStart", "" + apiURL);

				if (!apiURL.contains("top50")) {
					setLastUrl(apiURL);
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
				//Log.i("LoadCategoryAPI", "" + apiURL);

				if (apiURL.contains("top")) {
					if (apiURL.contains("top100")) {
						pList1 = result;
						setLastPlist(result);
					}
					if (apiURL.contains("top50")) {
						pList2 = result;
					}
				}
				if (apiURL.contains("ebooks-list")) {
					pList1 = result;

					switch (PageCurrent()) {
					case DETAIL_EBOOK:
						break;
					case EBOOK_FRAGMENT:
						setLastPlist(result);
						break;
					case TOP_FRAGMENT:
						setLastPlist(result);
						break;
					default:
						break;

					}
				}
				count = currentIndex;
			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (count == 0) { // =1
							for (Throw_IntefacePlist each : intefacePlists) {
								each.PList(pList1, progressDialog);
							}
						}
						if (count == 1) { // =2
							for (Throw_IntefacePlist each : intefacePlists) {
								each.PList_TopPeriod(pList1, pList2, progressDialog);
							}
						}
						if (count < 0) { // =null
							for (Throw_IntefacePlist each : intefacePlists) {
								each.PList(null, progressDialog);
							}
						}
					}
				});

			}
		});
		switch (PageCurrent()) {
		case EBOOK_FRAGMENT:
			Log.e("LoadCategoryAPI", "" + "EBOOK_FRAGMENT");
			asyncTask_FetchAPI.execute(
					AppMain.getAPIbyRefKey("ebooks-list", "type=" + type + "&count=80&retina=0&catid=" + catid +"&cid=" + Shared_Object.getCustomerDetail.getCID()));
			break;
		case TOP_FRAGMENT:
			Log.e("LoadCategoryAPI", "" + "TOP_FRAGMENT");
			if (top50) {
				asyncTask_FetchAPI.execute(
						AppMain.getAPIbyRefKey("top100", "type=" + type + "&period=" + period + "&catid=" + catid + "&retina=0"),
						AppMain.getAPIbyRefKey("top50", "type=" + period));
				top50 = false;
			} else {
				asyncTask_FetchAPI.execute(
						AppMain.getAPIbyRefKey("top100", "type=" + type + "&period=" + period + "&catid=" + catid + "&retina=0"));
			}
			break;
		case DETAIL_EBOOK:
			Log.e("LoadCategoryAPI", "" + "DETAIL_EBOOK");
			asyncTask_FetchAPI.execute(
					AppMain.getAPIbyRefKey("ebooks-list", "type=" + type + "&count=20&retina=0&catid=" + catid));
			break;
		default:
			break;

		}

	}
}
