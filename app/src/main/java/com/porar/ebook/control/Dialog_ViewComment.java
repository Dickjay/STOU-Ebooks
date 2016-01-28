package com.porar.ebook.control;

import java.net.URLEncoder;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_List_Comment;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;

//import android.util.Log;

public class Dialog_ViewComment extends Dialog {

	ImageDownloader_forCache downloader_forCache;
	Dialog_ViewComment commentview;
	Button btn_submit;
	ImageView img_commentv;
	EditText et_comment, et_name;
	ListView plistview_comment;
	RatingBar ratingBar;
	int BID;
	Animation fade_in;
	private final boolean cancel = false;
	Dialog_Comment comment;
	Comment_Detail comment_DetailAPI;
	AlertDialog alertDialog;
	ArrayList<Model_Comment_List> comment_Lists;
	Adapter_List_Comment adapter_List_Comment;

	public Dialog_ViewComment(Context context, int theme, int bid) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		commentview = this;
		this.BID = bid;
		downloader_forCache = new ImageDownloader_forCache();
		init();
	}

	private void init() {
		try {
			setContentView(R.layout.dialog_viewcomment);
			DisplayMetrics dm = new DisplayMetrics();
			getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int matginright = Integer.parseInt(getContext().getResources()
					.getString(R.string.dialog_marginright));
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					dm.widthPixels - matginright, dm.heightPixels);
			RelativeLayout viewcomment_rt_main = (RelativeLayout) findViewById(R.id.viewcommment_linear_main);
			viewcomment_rt_main.setLayoutParams(param);

			img_commentv = (ImageView) findViewById(R.id.viewcommment_image_pcomment);
			plistview_comment = (ListView) findViewById(R.id.viewcommment_plistView1);
			fade_in = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);

			img_commentv.setOnClickListener(new onClickImageViewComment());
			setListComment();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private class onClickImageViewComment implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == img_commentv.getId()) {
				v.startAnimation(fade_in);
				fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
			}
		}

	}

	private void setListComment() {
		comment_Lists = new ArrayList<Model_Comment_List>();
		comment_DetailAPI = new Comment_Detail(getContext(),
				String.valueOf(BID));
		comment_DetailAPI.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void PList_Detail_Comment(PList Plistdetail,
					PList Plistcomment, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {

				try {
					for (PListObject each : (Array) resultPlist
							.getRootElement()) {
						comment_Lists.add(new Model_Comment_List(each));
					}

					if (pd != null) {
						if (pd.isShowing()) {
							pd.dismiss();
						}
					}
					// Log.i("", "Loading...success");

					adapter_List_Comment = new Adapter_List_Comment(
							getContext(), 0, comment_Lists);
					plistview_comment.setAdapter(adapter_List_Comment);
				} catch (NullPointerException e) {
					if (pd != null) {
						if (pd.isShowing()) {
							pd.dismiss();
						}
					}
					if (comment_Lists == null) {
						alertDialog = new AlertDialog.Builder(getContext())
								.create();
						alertDialog.setTitle(AppMain.getTag());
						alertDialog
								.setMessage("WARNING:Comment An error has ocurred. Please to try again ?");
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
								"Retry", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.dismiss();
										System.gc();
										comment_DetailAPI.LoadEbooksDetailAPI();
									}
								});
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								"Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										System.gc();
									}
								});
						alertDialog.show();
					}

				}

			}

			@Override
			public void StartLoadPList() {

				// Log.i("", "Loading...");

			}

			@Override
			public void PList_TopPeriod(plist.xml.PList Plist1,
					plist.xml.PList Plist2, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}
		});
		comment_DetailAPI.LoadEbooksDetailAPI();
	}

	private class onAnimationLitener implements AnimationListener {
		int id = 0;

		public onAnimationLitener(int id) {
			this.id = id;
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			if (id == img_commentv.getId()) {
				comment = new Dialog_Comment(getContext(), R.style.PauseDialog,
						BID);
				comment.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface arg0) {
						if (comment.isCancel()) {
							setListComment();
							// Log.e("",
							// "Dialog_Comment onCancel  true success");
						}
						// Log.e("", "Dialog_Comment onCancel");
					}
				});
				comment.show();
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub

		}

	}

	private class onClickViewComment implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == btn_submit.getId()) {
				// String urlpostcomment =
				// "http://api.ebooks.in.th/post-comment.ashx?";
				String urlpostcomment = AppMain.POST_COMMENT_URL;
				urlpostcomment += "cid="
						+ Shared_Object.getCustomerDetail.getCID();
				urlpostcomment += "&bid=" + BID;
				urlpostcomment += "&comment="
						+ URLEncoder.encode(et_comment.getEditableText()
								.toString());
				urlpostcomment += "&name="
						+ URLEncoder.encode(et_name.getEditableText()
								.toString());
				urlpostcomment += "&rating=" + (int) ratingBar.getRating();
				LoadAPIResultString apiResultString = new LoadAPIResultString();
				apiResultString
						.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

							@Override
							public void completeResult(String result) {
								if (result != null) {
									if (result.contains("1")) {
										// Log.e("", "urlpostcomment result = "
										// + result);
										// comment.cancel();
									}

								} else {
									try {
										Toast.makeText(
												getContext(),
												"An error has occurred. Please to try again",
												Toast.LENGTH_SHORT).show();
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
