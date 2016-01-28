package com.porar.ebook.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

@SuppressLint("SetJavaScriptEnabled")
public class MyLinear_CommentView_Shelf extends LinearLayout {
	private LayoutInflater inLayoutInflater;
	LinearLayout layoutview;

	WebView webviewPhoto;
	TextView tv_name;
	TextView tv_comment;
	RatingBar rating;
	TextView tv_datetime;
	String readRawTextFile = "";
	String appCachePath = "";

	public MyLinear_CommentView_Shelf(Context context, Model_Comment_List comment_List) {
		super(context);
		init(comment_List);
	}

	private void init(Model_Comment_List comment_List) {
		inLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutview = (LinearLayout) inLayoutInflater.inflate(R.layout.comments_list_shelf, null);

		webviewPhoto = (WebView) layoutview.findViewById(R.id.webView_comment_detail);
		tv_name = (TextView) layoutview.findViewById(R.id.tv_comment_name);
		tv_comment = (TextView) layoutview.findViewById(R.id.tv_comment_detail);
		rating = (RatingBar) layoutview.findViewById(R.id.Rating_commemt_detail);
		tv_datetime = (TextView) layoutview.findViewById(R.id.tv_comment_datetime);

		rating.setEnabled(false);
		tv_name.setText(comment_List.getName() + "");
		tv_comment.setText(comment_List.getComment() + "");
		tv_datetime.setText(comment_List.getUpdateDateTime() + "");
		tv_name.setTypeface(StaticUtils.getTypeface(getContext(), Font.DB_Helvethaica_X_Med));
		tv_comment.setTypeface(StaticUtils.getTypeface(getContext(), Font.DB_Helvethaica_X_Med));
		tv_datetime.setTypeface(StaticUtils.getTypeface(getContext(), Font.DB_Helvethaica_X_Med));
		rating.setRating(comment_List.getRating());

		appCachePath = getContext().getApplicationContext().getCacheDir().getAbsolutePath();
		readRawTextFile = StaticUtils.readRawTextFile(getContext(), R.raw.bg_cover_html);

		if (comment_List.getPictrueUrl().length() <= 0 || comment_List.getPictrueUrl() == "") {
			readRawTextFile = readRawTextFile.replace("{{{url}}}", "http://www.ebooks.in.th/images/no_avt.jpg");
		} else {
			readRawTextFile = readRawTextFile.replace("{{{url}}}", comment_List.getPictrueUrl());
		}

		webviewPhoto.setVisibility(View.VISIBLE);
		webviewPhoto.getSettings().setJavaScriptEnabled(true);
		webviewPhoto.loadDataWithBaseURL(null, readRawTextFile, "text/html", "UTF-8", null);
		webviewPhoto.setWebViewClient(new MyWebViewClient((Activity) getContext()));
		webviewPhoto.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		webviewPhoto.getSettings().setAppCachePath(appCachePath);
		webviewPhoto.getSettings().setAppCacheEnabled(true);
		webviewPhoto.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webviewPhoto.setWebViewClient(new MyWebViewClient((Activity) getContext()));
		webviewPhoto.getSettings().setDomStorageEnabled(true);
		webviewPhoto.getSettings().setAllowFileAccess(true);

		this.addView(layoutview);
	}

}
