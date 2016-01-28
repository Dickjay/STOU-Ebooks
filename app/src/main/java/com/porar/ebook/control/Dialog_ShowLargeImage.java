package com.porar.ebook.control;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;

public class Dialog_ShowLargeImage extends Dialog {
	
	ImageDownloader_forCache downloader_forCache;
	Dialog_ShowLargeImage dialog_ShowLargeImage;

	public Dialog_ShowLargeImage(Context context, int theme , String url) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		dialog_ShowLargeImage = this;
		init(url);
	}
	private void init(String url) {
		ImageView image_large = new ImageView(getContext());
		downloader_forCache = new ImageDownloader_forCache();
		downloader_forCache.download(url, image_large);

		setContentView(image_large);
		image_large.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (ev.getAction() == KeyEvent.ACTION_DOWN) {
					dialog_ShowLargeImage.dismiss();
				}
				return false;
			}
		});

	}

}
