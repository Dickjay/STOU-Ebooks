package com.porar.ebook.control;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class DialogProfile_Show extends Dialog {

	ImageDownloader_forCache downloader_forCache;
	DialogProfile_Show dialog_ShowLargeImage;
	Fragment fragment;
	Activity activity;

	Handler handler = new Handler();

	// private final Session userInfoSession = null;
	ImageView img_profile, img_close;
	TextView txt_name, txt_email;
	TextView txt_ebook1, txt_ebook2, txt_fav1, txt_fav2, txt_total1,
			txt_total2;
	Button btn_signout;
	Bundle bundle;

	public DialogProfile_Show(Context context, int theme, Fragment fragment) {
		super(context, theme);
		this.fragment = fragment;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		dialog_ShowLargeImage = this;
		bundle = new Bundle();

		init();
	}

	private void init() {
		try {
			setContentView(R.layout.dialog_profilephone);
			if (AppMain.isphone) {
				DisplayMetrics dm = new DisplayMetrics();
				getWindow().getWindowManager().getDefaultDisplay()
						.getMetrics(dm);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						dm.widthPixels, dm.heightPixels);
				LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.profile_linear_main);
				registration_linear_main.setLayoutParams(param);
			}

			img_close = (ImageView) findViewById(R.id.profile_img_close);
			txt_name = (TextView) findViewById(R.id.profile_name);
			txt_email = (TextView) findViewById(R.id.profile_email);
			txt_ebook1 = (TextView) findViewById(R.id.profile_txt_ebook1);
			txt_ebook2 = (TextView) findViewById(R.id.profile_txt_ebook2);
			txt_fav1 = (TextView) findViewById(R.id.profile_txt_favorite1);
			txt_fav2 = (TextView) findViewById(R.id.profile_txt_favorite2);
			txt_total1 = (TextView) findViewById(R.id.profile_txt_Total1);
			txt_total2 = (TextView) findViewById(R.id.profile_txt_Total2);

			txt_name.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_email.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_ebook1.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_ebook2.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_fav1.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_fav2.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_total1.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));
			txt_total2.setTypeface(StaticUtils.getTypeface(getContext(),
					Font.LayijiMahaniyomV105ot));

			txt_name.setText(""
					+ Shared_Object.getCustomerDetail.getFirstName() + " "
					+ Shared_Object.getCustomerDetail.getLastName());
			txt_email.setText("" + Shared_Object.getCustomerDetail.getEmail());
			txt_ebook2
					.setText("" + Shared_Object.getCustomerDetail.geteBooks());
			txt_fav2.setText(""
					+ Shared_Object.getCustomerDetail.getFavorites());
			txt_total2.setText("" + Shared_Object.getCustomerDetail.getTotal());

			img_profile = (ImageView) findViewById(R.id.profile_img_profile);
			btn_signout = (Button) findViewById(R.id.profile_btn_signout);
			downloader_forCache = new ImageDownloader_forCache();
			downloader_forCache.download(
					Shared_Object.getCustomerDetail.getPictureUrl(),
					img_profile);

			btn_signout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Shared_Object.getCustomerDetail.getCID() > 0) {
						removeAccount();
						Shared_Object.getCustomerDetail = new Model_Customer_Detail(null);
						File customerFile = new File(getContext().getFilesDir(), "/"+ "customer_detail.porar");
						if (customerFile.exists()) {
							customerFile.delete();
							customerFile = null;
							AppMain.pList_default_ebookshelf = null;// default
							StaticUtils.arrShilf.clear();
							clearCacheFacebookLogin();
							DialogProfile_Show.this.cancel();
						}
					}
				}
			});

			img_close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					DialogProfile_Show.this.cancel();
				}
			});
		} catch (NullPointerException e) {

		}
	}

	public void removeAccount() {

		// String registerURL = "http://api.ebooks.in.th/register.ashx?";
		// stou

		String registerURL = AppMain.LOGOUT_URL_STOU;
		// end
		registerURL += +Shared_Object.getCustomerDetail.getCID();
		registerURL += "&udid=" + StaticUtils.deviceID;

		// register
		Log.e("registerURL", "cid" + registerURL);
		LoadAPIResultString apiResultString = new LoadAPIResultString();
		apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

			@Override
			public void completeResult(String cid) {
				try {

					if (cid == null) {
						Toast.makeText(getOwnerActivity(), "Logout",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getOwnerActivity(), "Logout",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		apiResultString.execute(registerURL);

	}

	private void clearCacheFacebookLogin() {
		if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
			RegisterFacebook.FacebookLogout();
		}
		RegisterFacebook.resetValue();

	}
}
