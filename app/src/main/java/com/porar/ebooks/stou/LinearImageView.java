package com.porar.ebooks.stou;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.porar.ebook.control.ThrowEventReading;
import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.utils.AsyncTask_Download_Ebooks_Watcher.ErrorException;
import com.porar.ebooks.utils.AsyncTask_EbooksPageFile;

public class LinearImageView extends LinearLayout {

	public ImageViewTouch touch;

	boolean isEmbedMode;
	boolean menuVisible = false;
	Hashtable<Integer, Model_Ebooks_Page> hashtableEbookpagefile;
	int pageIndex = 0;
	int pageLoadDelay = 0;
	Handler mhandler = new Handler();
	AsyncTask_EbooksPageFile asyncTask_EbooksPageFile;
	ProgressBar pb = null;
	RelativeLayout Relayout;

	ArrayList<ThrowEventReading> throwEventReadings = new ArrayList<ThrowEventReading>();
	CheckFileSize checkFileSize;

	public void setOnCheckFileSize(CheckFileSize cFileSize) {
		checkFileSize = cFileSize;
	}

	public void setOnThrowEventReading(ThrowEventReading eventReading) {
		throwEventReadings.add(eventReading);

		init();
	}

	public LinearImageView(Context context, boolean isEmbedMode, Hashtable<Integer, Model_Ebooks_Page> hashtableEbookpagefile, int pageIndex) {
		super(context);

		this.isEmbedMode = isEmbedMode;
		this.hashtableEbookpagefile = hashtableEbookpagefile;
		this.pageIndex = pageIndex;

	}

	private void init() {

		Relayout = new RelativeLayout(getContext());
		pb = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);

		LinearLayout.LayoutParams paramsRelative = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		this.addView(Relayout, paramsRelative);

		touch = new ImageViewTouch(getContext(), null);
		touch.setScaleType(ScaleType.MATRIX);
		touch.setEmbedMode(isEmbedMode);
		touch.setOnEventListener(new IEventClassListener() {
			@Override
			public void handleClassEvent(EventClass e) {
				try {
					// Edit Mr.thanawat
					if (e.getEventName() == "touch_image") {
						for (ThrowEventReading each : throwEventReadings) {
							each.TouchMode();
						}
					}

					if (e.getEventName() == "Zoom") {
						for (ThrowEventReading each : throwEventReadings) {
							each.ZoomMode();
						}
					}
					if (e.getEventName() == "UnZoom") {
						for (ThrowEventReading each : throwEventReadings) {
							each.UnZoomMode();
						}
					}

				} catch (Exception ex) {
					Log.e(AppMain.getTag(), ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		// this.addView(touch,p2);

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.CENTER_IN_PARENT);
		Relayout.addView(pb, params2);
		Relayout.addView(touch, p2);

		if (hashtableEbookpagefile.containsKey(pageIndex)) {
			HandlerPostListener();
		}

	}

	private void HandlerPostListener() {
		if (!isEmbedMode) {
			String filepath = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + hashtableEbookpagefile.get(pageIndex).getBID();
			String key = "ebooks_" + hashtableEbookpagefile.get(pageIndex).getBID() + "_page_" + pageIndex + ".ebooks";
			filepath = filepath + "/" + key;
			File f = new File(filepath);
			if (f.exists()) {
				mhandler.removeCallbacks(EmbedmRunnableRequest);
				mhandler.postDelayed(EmbedmRunnableRequest, pageLoadDelay);
				checkFileSize.isFile();
			} else {
				mhandler.removeCallbacks(mRunnableRequest);
				mhandler.postDelayed(mRunnableRequest, pageLoadDelay);
			}

			// mhandler.removeCallbacks(mRunnableRequest);
			// mhandler.postDelayed(mRunnableRequest, pageLoadDelay);
		} else {
			mhandler.removeCallbacks(EmbedmRunnableRequest);
			mhandler.postDelayed(EmbedmRunnableRequest, pageLoadDelay);
		}
	}

	public interface CheckFileSize {
		void isFile();
	}

	Runnable mRunnableRequest = new Runnable() {

		@Override
		public void run() {
			// download page
			asyncTask_EbooksPageFile = new AsyncTask_EbooksPageFile() {

				@Override
				protected void OnError(ErrorException e, Model_Ebooks_Page model) {
					Log.e("LinearsetImage", "Download Background : OnError[ebooks_" + model.getBID() + "_page_" + model.getCurrentPagesNumber() + "]");
				}

				@Override
				protected void OnComplete(Model_Ebooks_Page model) {

					String filename = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + model.getBID() + "/ebooks_" + model.getBID() + "_page_" + model.getCurrentPagesNumber() + ".ebooks";
					File fileebook = new File(filename);
					if (fileebook.exists()) {
						if (fileebook.length() > 0) {
							Log.w("LinearsetImage", "isfile" + model.getBID());
							touch.setPageView(model, pageIndex);
						} else {
							Log.w("LinearsetImage", " file size = " + fileebook.length());
						}

					} else {
						// download
						Log.w("LinearsetImage", "no file on directory" + model.getBID());
					}

				}

			};
			asyncTask_EbooksPageFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hashtableEbookpagefile.get(pageIndex));
		}
	};
	Runnable EmbedmRunnableRequest = new Runnable() {

		@Override
		public void run() {

			String filename = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + hashtableEbookpagefile.get(pageIndex).getBID() + "/ebooks_" + hashtableEbookpagefile.get(pageIndex).getBID() + "_page_" + hashtableEbookpagefile.get(pageIndex).getCurrentPagesNumber() + ".ebooks";
			File fileebook = new File(filename);
			if (fileebook.exists()) {
				if (fileebook.exists()) {
					if (fileebook.length() > 0) {
						Log.w("LinearsetImage", "isfile" + hashtableEbookpagefile.get(pageIndex).getBID());
						touch.setPageView(hashtableEbookpagefile.get(pageIndex), pageIndex);

					} else {
						Log.w("LinearsetImage", " file size = " + fileebook.length());
					}

				} else {
					// download
					Log.w("LinearsetImage", "no file on directory" + hashtableEbookpagefile.get(pageIndex).getBID());
				}
			} else {
				// download
				Log.w("LinearsetImage", "no file on directory" + hashtableEbookpagefile.get(pageIndex).getBID());
			}

		}
	};

}
