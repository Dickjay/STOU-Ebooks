package com.porar.ebook.control;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import com.porar.ebooks.stou.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Dialog_Show_Banner extends Dialog {

	// private Spinner selectDishTypeSpinner;

	// private ListView dishCatagoriesListView;
	private Context context;
	private TextView textSupport, textVipa, textPhone;
	private Button btnCancel, btnSend;
	private EditText editPhone, editCaption;
	private WebView showWeb;
	private String showBanner;

	// private ArrayAdapter<String> listviewArrayAdapter;

	public Dialog_Show_Banner(Context context, final String position) {
		super(context);
		this.context = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.dimAmount = 1.0f;
			lp.windowAnimations = R.style.PauseDialogAnimation;
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			getWindow().setAttributes(lp);
			Log.i("snuxker", "BLUR");

		} catch (Exception e) {
			Log.i("snuxker", "DIM");
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.windowAnimations = R.style.PauseDialogAnimation;
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			getWindow().setAttributes(lp);
		}

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		initView(position);
	}

	private void initView(String position) {

		setContentView(R.layout.dialog_banner);
		setCancelable(true);

		showWeb = (WebView) findViewById(R.id.webView1);

		String url = "http://203.150.225.223/stoubookapi/news-api2.aspx?";
		url += "nid=" + position;

		try {
			int widthPerPixel = 0;
			int heightPerPixel = 0;

			this.showBanner = url;
			this.showBanner += "&type=android" + "&device_width="
					+ widthPerPixel + "&device_height=" + heightPerPixel
					+ "&type=android";

			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			widthPerPixel = metrics.widthPixels;
			heightPerPixel = metrics.heightPixels;
			showWeb.getSettings().setBuiltInZoomControls(true);
			showWeb.getSettings().setDisplayZoomControls(false);
			// webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
			showWeb.setInitialScale(1);

			showWeb.getSettings().setUseWideViewPort(true);
			showWeb.getSettings().setLoadWithOverviewMode(true);
			showWeb.loadUrl(showBanner);
		} catch (Exception e) {
			// TODO: handle exception
		}
		setFullScreenDialog();
		show();
		// SHOW Dialog

	}

	private void setFullScreenDialog() {

		try {

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			Window window = getWindow();
			lp.copyFrom(window.getAttributes());
			// This makes the dialog take up the full width
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.MATCH_PARENT;
			lp.gravity = Gravity.CENTER;
			window.setAttributes(lp);

			// Grab the window of the dialog, and change the width
			// WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			// Window window = getWindow();
			// lp.copyFrom(window.getAttributes());
			// //This makes the dialog take up the full width
			// lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			// lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			// lp.gravity = Gravity.CENTER;
			// lp.windowAnimations = R.style.PauseDialogAnimation;
			// window.setAttributes(lp);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
