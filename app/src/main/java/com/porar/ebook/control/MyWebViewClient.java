package com.porar.ebook.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

	Activity context;
	
	int count = 0;

	public MyWebViewClient(Activity context) {
		this.context = context;

	}
	

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
		Log.i("","onLoadResource " + url);

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.i("","onPageFinished " + url);
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		view.getSettings().setJavaScriptEnabled(true);
		view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		view.setScrollbarFadingEnabled(false);
		view.setVerticalScrollBarEnabled(false);
		view.setHorizontalScrollBarEnabled(false);

	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		Log.i("","shouldOverrideUrlLoading " + url);
		return super.shouldOverrideUrlLoading(view, url);
	}

	// @Override
	// public boolean shouldOverrideUrlLoading(WebView view, String url)
	// {
	// view.loadUrl(url);
	// return true;
	// }

}
