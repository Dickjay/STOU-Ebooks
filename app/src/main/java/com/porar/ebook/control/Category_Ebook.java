package com.porar.ebook.control;

import java.util.ArrayList;

import plist.xml.PList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.fragment.Fragment_Ebook;
import com.porar.ebooks.stou.fragment.Fragment_Top;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;

public class Category_Ebook {

	protected AsyncTask_FetchAPI asyncTask_FetchAPI;
	protected final Context context;
	protected final ArrayList<Throw_IntefacePlist> intefacePlists = new ArrayList<Throw_IntefacePlist>();
	protected final Handler handler = new Handler();
	protected ProgressDialog progressDialog;
	protected String catid = "";
	protected String type = "";
	protected String period = "";
	protected AlertDialog alertDialog;

	public static enum Page {
		EBOOK_FRAGMENT, TOP_FRAGMENT, DETAIL_EBOOK
	};

	private Page page = Page.EBOOK_FRAGMENT;  // default

	protected Page PageCurrent() {
		return page;
	}

	public Category_Ebook(Context context, String catid, String type) {
		this.context = context;
		this.catid = catid;
		this.type = type;

	}

	public Category_Ebook(Context context, String catid, String period, String type) {
		this.context = context;
		this.catid = catid;
		this.type = type;
		this.period = period;
	}

	protected void setPageCurrent(Page page) {
		switch (page) {
		case EBOOK_FRAGMENT:
			this.page = page;
			Log.e("setPageCurrent", "EBOOK_FRAGMENT");
			break;
		case TOP_FRAGMENT:
			this.page = page;
			Log.e("setPageCurrent", "TOP_FRAGMENT");
			break;
		case DETAIL_EBOOK:
			this.page = page;
			Log.e("", "DETAIL_EBOOK");
			break;
		default:
			break;
		}

	}

	protected void setLastUrl(String url) {
		switch (page) {
		case EBOOK_FRAGMENT:
			if (Fragment_Ebook.lastUrl != null) {
				Fragment_Ebook.lastUrl.setUrl(url);
			}
			break;
		case TOP_FRAGMENT:
			if (Fragment_Top.lastUrl != null) {
				Fragment_Top.lastUrl.setUrl(url);
			}
			break;
		case DETAIL_EBOOK:
			break;
		default:
			break;
		}
	}

	protected void setLastPlist(PList resultPlist) {
		switch (page) {
		case EBOOK_FRAGMENT:
			AppMain.pList_default_ebook = resultPlist;
			break;
		case TOP_FRAGMENT:
			AppMain.pList_default_top = resultPlist;
			break;
		case DETAIL_EBOOK:
			break;
		default:
			break;
		}
	}

}
