package com.porar.ebook.control;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.porar.ebooks.stou.R;
import com.twitter.Dev_twitter;

public class Dialog_WebViewTwitter extends Dialog {
	private WebView mWebView;
	private final Dialog_WebViewTwitter Dtwitter;
	private String mVeritifer = "";
	WebView webview;
	ProgressBar progressBar;
	LinearLayout.LayoutParams param;
	RelativeLayout layout;
	public Dialog_WebViewTwitter(Context context, int theme, String url) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		Dtwitter = this;
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		param = new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
		
		init(url);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init(String url) {
		try {
			setContentView(R.layout.webview_payment);

			layout = (RelativeLayout) findViewById(R.id.RelativeLayout_payment);
			webview = (WebView) findViewById(R.id.webView_payment);
			progressBar = (ProgressBar) findViewById(R.id.progressBar_payment);

			layout.setLayoutParams(param);

			webview.getSettings().setSupportZoom(true);
			webview.getSettings().setUseWideViewPort(false);
			webview.getSettings().setBuiltInZoomControls(true);
			webview.setWebViewClient(new MyWebViewClient(getOwnerActivity()));
			webview.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int progress) {
					if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
						progressBar.setVisibility(ProgressBar.VISIBLE);
					}
					progressBar.setProgress(progress);
					if (progress == 100) {
						progressBar.setVisibility(ProgressBar.GONE);
					}
				}
			});
			webview.loadUrl(url);
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		mWebView = new WebView(getContext());
//		mWebView.setVerticalScrollBarEnabled(false);
//		mWebView.setHorizontalScrollBarEnabled(false);
//		mWebView.setWebViewClient(new TwitterWebViewClient());
//		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.loadUrl(url);
//
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((dm.widthPixels), dm.heightPixels);
//		mWebView.setLayoutParams(param);
//		setContentView(mWebView);
	}

	private class TwitterWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);

			Log.i("", "shouldOverrideUrlLoading " + url);

			if (url.startsWith(Dev_twitter.TWITTER_CALLBACK_URL)) {
				String veritifer = getVerifier(url);
				Log.v("Oauth Verifie", "" + veritifer);

			
				Dtwitter.setResultVeritifer(veritifer);
				Dtwitter.cancel();
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("oauth://", "http://");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(Dev_twitter.URL_TWITTER_OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	public void setResultVeritifer(String veritifer) {
		mVeritifer = veritifer;
	}

	public String getVeritifer() {
		return mVeritifer;
	}
}
