package com.porar.ebook.control;

import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Dialog_Comment extends Dialog {

	ImageDownloader_forCache downloader_forCache;
	Dialog_Comment comment;
	TextView txt_rating, txt_comment, txt_name;
	Button btn_cancel, btn_submit;
	EditText et_comment, et_name;
	RatingBar ratingBar;
	int BID;
	private boolean cancel = false;

	public Dialog_Comment(Context context, int theme, int bid) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		comment = this;
		this.BID = bid;
		downloader_forCache = new ImageDownloader_forCache();
		setCancel(false);
		init();
	}

	private void init() {
		try {
			setContentView(R.layout.dialog_comment);
			ratingBar = (RatingBar) findViewById(R.id.dialogcomment_rating_dialogcommemt);
			txt_rating = (TextView) findViewById(R.id.dialogcomment_textView_rating);
			txt_comment = (TextView) findViewById(R.id.dialogcomment_textView_comment);
			txt_name = (TextView) findViewById(R.id.dialogcomment_textView_name);
			btn_submit = (Button) findViewById(R.id.dialogcomment__btn_login);
			btn_cancel = (Button) findViewById(R.id.dialogcomment__btn_cancel);
			et_comment = (EditText) findViewById(R.id.dialogcomment_edittext_commemt);
			et_name = (EditText) findViewById(R.id.dialogcomment_edittext_name);
			ratingBar.setRating(5.0F);
			txt_rating.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			txt_comment.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			txt_name.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			btn_submit.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			btn_cancel.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			et_name.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));
			et_comment.setTypeface(StaticUtils.getTypeface(getContext(), Font.LayijiMahaniyomV105ot));

			et_name.setText(Shared_Object.getCustomerDetail.getFirstName()+" "+Shared_Object.getCustomerDetail.getLastName());
			btn_submit.setOnClickListener(new onclickComment());
			btn_cancel.setOnClickListener(new onclickComment());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	private class onclickComment implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == btn_cancel.getId()) {
				comment.dismiss();
			}
			if (v.getId() == btn_submit.getId()) {
				String urlpostcomment = AppMain.POST_COMMENT_URL;
				urlpostcomment += "cid=" + Shared_Object.getCustomerDetail.getCID();
				urlpostcomment += "&bid=" + BID;
				urlpostcomment += "&comment=" + URLEncoder.encode(et_comment.getEditableText().toString());
				urlpostcomment += "&name=" + URLEncoder.encode(et_name.getEditableText().toString());
				urlpostcomment += "&rating=" + (int) ratingBar.getRating();
				
				Log.e("ADD COMMENT : ", "urlpostcomment : " + urlpostcomment);
				
				LoadAPIResultString apiResultString = new LoadAPIResultString();
				apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

					@Override
					public void completeResult(String result) {
						if (result != null) {
							if (result.contains("1")) {
	//							Log.e("", "urlpostcomment result = " + result);
								setCancel(true);
								comment.cancel();
							}

						} else {
							try {
								Toast.makeText(getContext(), "An error has occurred. Please to try again", Toast.LENGTH_SHORT).show();
							} catch (NullPointerException e) {
								// TODO: handle exception
							}
						}
					}
				});
				apiResultString.execute(urlpostcomment);
			}
		}
	}

}
