package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.porar.ebook.control.MyWebViewClient;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Adapter_List_Comment extends ArrayAdapter<String> implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	Context context;
	LayoutInflater inflater;
	ImageDownloader_forCache downloader_forCache;
	Model_Ebooks_Detail ebooks_Detail = null;
	ArrayList<Model_Comment_List> comment_Lists = null;
	LruCache<String, byte[]> mMemoryCache;
	String readRawTextFile = "";
	String appCachePath = "";

	public Adapter_List_Comment(Context context, int textViewResourceId, ArrayList<Model_Comment_List> comment_Lists) {
		super(context, textViewResourceId);
		this.context = context;

		this.comment_Lists = comment_Lists;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);

		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
	}

	@Override
	public int getCount() {
		if (comment_Lists.size() != 0) {
			return comment_Lists.size();
		} else {
			return 1;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (comment_Lists.size() == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView.findViewById(R.id.txt_noresult);
				// txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			}
			return convertView;
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_comment, null);
			viewHolder.txtName = (TextView) convertView.findViewById(R.id.ptxtName_Sebook_comment);
			viewHolder.txtdate = (TextView) convertView.findViewById(R.id.ptxtdate_Sebook_comment);
			viewHolder.txtcommentr = (TextView) convertView.findViewById(R.id.ptxtcomment_Sebook_comment);
			viewHolder.iv_cover = (WebView) convertView.findViewById(R.id.img_ebook_pcover_comment);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtName.setText(comment_Lists.get(position).getName() + "");
		viewHolder.txtcommentr.setText(comment_Lists.get(position).getComment() + "");
		viewHolder.txtdate.setText(comment_Lists.get(position).getUpdateDateTime() + "");
		viewHolder.txtName.setTypeface(StaticUtils.getTypeface(getContext(), Font.THSarabanNew));
		viewHolder.txtcommentr.setTypeface(StaticUtils.getTypeface(getContext(), Font.THSarabanNew));
		viewHolder.txtdate.setTypeface(StaticUtils.getTypeface(getContext(), Font.THSarabanNew));

		appCachePath = getContext().getApplicationContext().getCacheDir().getAbsolutePath();
		readRawTextFile = StaticUtils.readRawTextFile(getContext(), R.raw.bg_cover_html);

		if (comment_Lists.get(position).getPictrueUrl().length() <= 0 || comment_Lists.get(position).getPictrueUrl() == "") {
			readRawTextFile = readRawTextFile.replace("{{{url}}}", "http://www.ebooks.in.th/images/no_avt.jpg");
		} else {
			readRawTextFile = readRawTextFile.replace("{{{url}}}", comment_Lists.get(position).getPictrueUrl());
		}
		viewHolder.iv_cover.setVisibility(View.VISIBLE);
		viewHolder.iv_cover.getSettings().setJavaScriptEnabled(true);
		viewHolder.iv_cover.loadDataWithBaseURL(null, readRawTextFile, "text/html", "UTF-8", null);
		viewHolder.iv_cover.setWebViewClient(new MyWebViewClient(null));
		viewHolder.iv_cover.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		viewHolder.iv_cover.getSettings().setAppCachePath(appCachePath);
		viewHolder.iv_cover.getSettings().setAppCacheEnabled(true);
		viewHolder.iv_cover.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		viewHolder.iv_cover.getSettings().setDomStorageEnabled(true);
		viewHolder.iv_cover.getSettings().setAllowFileAccess(true);

		return convertView;
	}

	public static class ViewHolder {
		public TextView txtName = null;
		public TextView txtdate = null;
		public TextView txtcommentr = null;
		public WebView iv_cover = null;

	}
}
