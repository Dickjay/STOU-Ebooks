package com.porar.ebook.control;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.porar.ebooks.stou.R;

public class Dialog_Payment extends Dialog {

	WebView webview;
	ProgressBar progressBar;
	LinearLayout.LayoutParams param;
	RelativeLayout layout;

	public Dialog_Payment(Context context, int theme, String url) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		param = new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
//		Log.e("","rawWidth "+dm.widthPixels + "\n" +"rawHeight "+dm.heightPixels);
		
		// Display display = getWindow().getWindowManager().getDefaultDisplay();
		// try {
		// Method mGetRawH = Display.class.getMethod("getRawHeight");
		// Method mGetRawW = Display.class.getMethod("getRawWidth");
		// int rawWidth = (Integer) mGetRawW.invoke(display);
		// int rawHeight = (Integer) mGetRawH.invoke(display);
		// param = new LinearLayout.LayoutParams(rawWidth, rawHeight);
		// Log.e("","rawWidth "+rawWidth + "\n" +"rawHeight "+rawHeight);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// init(url);
		// }
		init(url);
	}

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

	}

}
