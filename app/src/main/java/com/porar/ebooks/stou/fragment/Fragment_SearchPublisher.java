package com.porar.ebooks.stou.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import plist.xml.PList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.porar.ebook.adapter.Adapter_List_Publisher;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.stou.activity.Activity_Tab;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Fragment_SearchPublisher extends Fragment {

	ProgressDialog progressDialog;
	ArrayList<Model_EbookList> ebookLists_search;
	AsyncTask_FetchAPI asyncTask_FetchAPI;
	public static String tag = "Fragment_SearchPublisher";
	RelativeLayout rt_main;
	ListView search_list;
	ImageView img_back;
	FragmentActivity myActivity;
	EditText et_search;
	int actionX, actionY;
	Drawable drawble[];
	OnClickDrawBle clickDrawBle;
	private boolean check = false;
	String keyword = "";
	Handler handler = new Handler();
	AlertDialog alertDialog;
	Adapter_List_Publisher adapter_List_Publisher;
	Drawable right;
	private boolean is_drawblesearch = false;

	public void setOnClickDrawBle(OnClickDrawBle drawBle) {
		clickDrawBle = drawBle;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(tag, "onCreate");
		myActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(tag, "onCreateView");
		View view = inflater.inflate(R.layout.activity_search, container, false);
		return view;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(tag, "onActivityCreated");

		if (AppMain.isphone) {
			setId();
			setMethodDrwable();
			onClickInterface();
			if (adapter_List_Publisher != null) {
				search_list.setAdapter(adapter_List_Publisher);
			}
		} else {

		}

	}

	private void onClickInterface() {

		this.setOnClickDrawBle(new OnClickDrawBle() {

			@Override
			public void ClickDeawbleRight() {
				if (is_drawblesearch) {
					performClick(et_search.getText().toString());
				} else {
					et_search.setText("");
				}
			}
		});

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Activity_Tab.mLastTab.tag.equals("Tab2")) {
					if (Fragment_Publisher.fragment_Publisher.HaveFragment) {
						FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();

						if (Fragment_Publisher.fragment_Publisher.frag_searchpublisher != null) {
							ft.detach(Fragment_Publisher.fragment_Publisher.frag_searchpublisher);
						}
						if (Activity_Tab.mLastTab.fragment != null) {
							ft.attach(Activity_Tab.mLastTab.fragment);
						}

						ft.commit();
						myActivity.getSupportFragmentManager().executePendingTransactions();
						Fragment_Publisher.fragment_Publisher.HaveFragment = false;
					}

				}

			}
		});
		
		
	}

	private void setMethodDrwable() {
		// drawable right only
		drawble = et_search.getCompoundDrawables();
		et_search.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				Rect bounds;
				actionX = (int) ev.getX();
				actionY = (int) ev.getY();
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {
					for (int i = 0; i < drawble.length; i++) {
						if (drawble[i] != null) {
							bounds = null;
							bounds = drawble[i].getBounds();
							// drawble[i].getBounds().right != 0
							int x, y;
							int extraTapArea = 13;

							x = actionX + extraTapArea;
							y = actionY - extraTapArea;
							x = et_search.getWidth() - x;

							if (x <= 0) {
								x += extraTapArea;
							}

							if (y <= 0)
								y = actionY;

							if (bounds.contains(x, y) && clickDrawBle != null) {
								clickDrawBle.ClickDeawbleRight();
								ev.setAction(MotionEvent.ACTION_CANCEL);
								return false;
							}
						}
					}

				}

				return false;
			}
		});
		
		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if (str.length() > 0) {
					right = myActivity.getResources().getDrawable(R.drawable.picon_searchdrak);
					is_drawblesearch =  true;
				} else {
					right = myActivity.getResources().getDrawable(R.drawable.content_remove);
					is_drawblesearch = false;
				}

				et_search.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setId() {
		et_search = (EditText) getActivity().findViewById(R.id.search_edittext);
		rt_main = (RelativeLayout) getActivity().findViewById(R.id.search_rt_main_head_phone);
		search_list = (ListView) getActivity().findViewById(R.id.search_plistView1);
		img_back = (ImageView) getActivity().findViewById(R.id.search_image_pback);

		et_search.setHint("Put publisher Name");
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
	}

	private void performClick(String text) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				progressDialog = ProgressDialog.show(myActivity, "Download", "downloading", true, true);
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

						alertDialog = new AlertDialog.Builder(myActivity).create();
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

						alertDialog = new AlertDialog.Builder(myActivity).create();
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
			public void onFetchComplete(String apiURL, int currentIndex, final PList result) {
				Log.e("search onFetchComplete", apiURL);

				// for (PListObject each : (Array) result.getRootElement()) {
				// ebookLists_search.add(new Model_EbookList(each));
				// }

				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter_List_Publisher = new Adapter_List_Publisher(myActivity, 0, result);
						search_list.setAdapter(adapter_List_Publisher);
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
					AppMain.getAPIbyRefKey("search-publisher", "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&page=1&count=25"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

	}

	private void RefreshloadperformClick() {
		if (keyword != null) {
			performClick(keyword);
		} else {

		}

	}

	interface OnClickDrawBle {
		void ClickDeawbleRight();
	}

	@Override
	public void onResume() {
		Log.e(tag, "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.e(tag, "onPause");
		super.onPause();
	}

	@Override
	public void onDestroy() {
		Log.e(tag, "onDestroy");
		super.onDestroy();
	}
}
