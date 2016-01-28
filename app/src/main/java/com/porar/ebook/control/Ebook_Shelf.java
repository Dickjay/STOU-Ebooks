package com.porar.ebook.control;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import plist.type.Array;
import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_Restore;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

//import android.util.Log;

public class Ebook_Shelf {
	public LoadAPIResultString apiResultString;
	public AsyncTask_FetchAPI asyncTask_FetchAPI;
	private final Context context;
	private final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	private final Handler handler = new Handler();
	private ProgressDialog progressDialog = null;
	private String cid = "";
	private String did = "";
	private String email = "";
	private String type = "";
	private String password = "";
	private AlertDialog alertDialog;
	private PList pList;
	private final ArrayList<Model_Restore> model_Restores = new ArrayList<Model_Restore>();
	private final ArrayList<ThrowModel_Restore> intefaceRestore = new ArrayList<ThrowModel_Restore>();

	public Ebook_Shelf(Context context) {
		this.context = context;
		this.cid = "" + Shared_Object.getCustomerDetail.getCID(); // customer id
		this.email = "" + Shared_Object.getCustomerDetail.getEmail(); // customer
																		// id
		this.password = "" + Shared_Object.getCustomerDetail.getPassword(); // customer
																			// id
		this.type = "A";
		this.did = Shared_Object.getDeviceID(context);
	}

	public void setOnListener(Throw_IntefacePlist intefacePlist) {
		intefacePlists.add(intefacePlist);
	}

	public void setOnListenerRestore(ThrowModel_Restore interfaceRestore) {
		intefaceRestore.add(interfaceRestore);
	}

	public void refreshEbooksDetailAPI() {
		LoadEbooksDetailAPI();
	}

	public void refreshTrashAPI() {
		LoadEbooksTrashAPI();
	}

	public void LoadEbooksTrashAPI() {
		apiResultString = new LoadAPIResultString();
		apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

			@Override
			public void completeResult(String result) {

				System.out.println("RESULT TRASH : " + result);

				try {
					JSONArray array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject data = array.getJSONObject(i);
						model_Restores.add(new Model_Restore(data
								.getString("BID"), data.getString("Name"), data
								.getString("CoverFileNameS")));
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							for (ThrowModel_Restore each : intefaceRestore) {
								each.throwModel(model_Restores);
							}
						}
					});
				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		});

		// Log.e("", "AppMain.TRASH_SHELF_URL + cid :" + AppMain.TRASH_SHELF_URL
		// + cid);

		apiResultString.execute(AppMain.TRASH_SHELF_URL + cid);

	}

	public void LoadEbooksDetailAPI() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				progressDialog = ProgressDialog.show(context, "", "Loading..",
						true, true);

			}
		});
		asyncTask_FetchAPI = new AsyncTask_FetchAPI();
		asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

			@Override
			public void onTimeOut(String apiURL, int currentIndex) {

			}

			@Override
			public void onFetchStart(String apiURL, int currentIndex) {
				Log.e("onFetchStart", apiURL);

			}

			@Override
			public void onFetchError(String apiURL, int currentIndex,
					Exception e) {
				// Log.e("onFetchError", e.toString());

			}

			@Override
			public void onFetchComplete(final String apiURL, int currentIndex,
					final PList result) {
				// Log.w("LoadEbooksDetailAPI", "" + apiURL);

				if (apiURL.contains("update-bookshelf")) {

				}

				if (apiURL.contains("bookshelf-list")) {
					pList = result;
					AppMain.pList_default_ebookshelf = result;
				}
				if (apiURL.contains("customer-detail")) {
					try {
						Array plistObject = (Array) result.getRootElement();
						Model_Customer_Detail modelCustomerDetail = new Model_Customer_Detail(
								plistObject);
						if (Shared_Object.getCustomerDetail.getCID() > 0) {
							Shared_Object.getCustomerDetail.seteBooks(modelCustomerDetail.geteBooks());
							Shared_Object.getCustomerDetail.setFavorites(modelCustomerDetail.getFavorites());
							Shared_Object.getCustomerDetail.setTotal(modelCustomerDetail.getTotal());

							Class_Manage.SaveEbooksObject(context,
									Shared_Object.getCustomerDetail,
									"customer_detail.porar");
						}

					} catch (NullPointerException e) {
						// Log.i("", " Parse Error ");
					}
				}
			}

			@Override
			public void onAllTaskDone() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						for (Throw_IntefacePlist each : intefacePlists) {
							each.PList(pList, progressDialog);
						}
					}
				});
			}
		});

		asyncTask_FetchAPI.execute(
				AppMain.getAPIbyRefKey("customer-detail", "cid=" + cid),
				AppMain.getAPIbyRefKey("update-bookshelf", "cid=" + cid
						+ "&did=" + did),
				AppMain.getAPIbyRefKey("bookshelf-list", "email=" + email
						+ "&password=" + password + "&type=" + type
						+ "&retina=0"));

	}

}
