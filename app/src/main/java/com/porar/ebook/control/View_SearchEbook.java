package com.porar.ebook.control;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.porar.ebook.adapter.Adapter_SearchList_Ebook;
import com.porar.ebooks.event.DrawableClickListener;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public abstract class View_SearchEbook {

	private LinearLayout layout_search;
	private final LayoutInflater inflater;
	private final LinearLayout linear_fotTypeEbook;
	private final RelativeLayout rt_dummyscreen;
	private final Activity context;
	private CustomEditText et_search;
	private ListView lv_search;
	private Drawable imgL, imgR;
	private final Handler handler = new Handler();
	private boolean check = false;
	private String keyword = "";
	ProgressDialog progressDialog;
	ArrayList<Model_EbookList> ebookLists_search;
	AsyncTask_FetchAPI asyncTask_FetchAPI;
	AlertDialog alertDialog;
	Adapter_SearchList_Ebook adapter_SearchList;
	Ebook_Detail ebook_Detail;
	private int id_head = 0;

	public View_SearchEbook(Activity context, LinearLayout linear_fotTypeEbook, RelativeLayout rt_dummyscreen, int id_head) {
		this.context = context;
		this.linear_fotTypeEbook = linear_fotTypeEbook;
		this.rt_dummyscreen = rt_dummyscreen;
		this.id_head = id_head;

		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setAdapter(Adapter_SearchList_Ebook adapter_SearchList) {
		this.adapter_SearchList = adapter_SearchList;
	}

	@SuppressWarnings("static-access")
	public void initView(int left, int top, int height) {
		rt_dummyscreen.setVisibility(View.VISIBLE);

		layout_search = (LinearLayout) inflater.from(context).inflate(R.layout.layout_search, null);
		et_search = (CustomEditText) layout_search.findViewById(R.id.search_et_search);
		lv_search = (ListView) layout_search.findViewById(R.id.lv_dialog_search);
		imgL = context.getResources().getDrawable(R.drawable.picon_searchdrak);
		imgR = context.getResources().getDrawable(R.drawable.content_remove);
		imgR.setBounds(0, 0, 20, 20);
		imgL.setBounds(0, 0, 20, 20);

		lv_search.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		if (adapter_SearchList != null) {
			lv_search.setAdapter(adapter_SearchList);
		}

		et_search.setCompoundDrawables(imgL, null, imgR, null);
		et_search.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));

		et_search.setInputType(InputType.TYPE_CLASS_TEXT);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (v.getEditableText().toString().length() > 0) {
					if (event != null && event.getAction() != KeyEvent.ACTION_DOWN)
						return check;
					if (actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
						performClick(v.getEditableText().toString());
					check = true;
				}

				return check;
			}

		});

		et_search.setDrawableClickListener(new DrawableClickListener() {

			@Override
			public void onClick(DrawablePosition target) {
				switch (target) {
				case LEFT:
					// do something
					performClick(et_search.getEditableText().toString());
					break;

				case RIGHT:
					// do something
					et_search.setText("");
					break;
				case BOTTOM:
					break;
				case TOP:
					break;
				default:
					break;
				}
			}

		});

		rt_dummyscreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_search);
				InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(et_search.getApplicationWindowToken(), 0);
				et_search.clearFocus();
				System.gc();
			}
		});
		// linear_fotTypeEbook.removeAllViews();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.BELOW, id_head);
		linear_fotTypeEbook.addView(layout_search);
		linear_fotTypeEbook.setLayoutParams(params);

	}

	private void performClick(String text) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				progressDialog = ProgressDialog.show(context, "Download", "downloading", true, true);
			}
		});

		keyword = text;

		ebookLists_search = new ArrayList<Model_EbookList>();
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
								RefreshloadperformClick();

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
				// TODO Auto-generated method stub

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
								RefreshloadperformClick();

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
			public void onFetchComplete(String apiURL, int currentIndex, PList result) {
				Log.e("search onFetchComplete", apiURL);

				for (PListObject each : (Array) result.getRootElement()) {
					ebookLists_search.add(new Model_EbookList(each));
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter_SearchList = new Adapter_SearchList_Ebook(context, 0, ebookLists_search);
						lv_search.setAdapter(adapter_SearchList);
						onAdapter_SearchList(adapter_SearchList);

						if (progressDialog.isShowing()) {
							progressDialog.dismiss();

						}
					}
				});

			}

			@Override
			public void onAllTaskDone() {

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}
		});
		try {
			asyncTask_FetchAPI.execute(
					AppMain.getAPIbyRefKey("search", "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&page=1&count=25"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// Log.d("search", "search");

	}

	private void RefreshloadperformClick() {
		if (keyword != null) {
			performClick(keyword);
		} else {

		}

	}

	public abstract void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_search);

	public abstract void onAdapter_SearchList(Adapter_SearchList_Ebook adapter_SearchList);
}
