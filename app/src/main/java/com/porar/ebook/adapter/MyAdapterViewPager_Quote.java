package com.porar.ebook.adapter;

import it.sephiroth.android.library.imagezoom.ImageViewTouch_Crop;
import it.sephiroth.android.library.imagezoom.ImageViewTouch_Crop.TouchImageCrop;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebooks.model.Model_Quote;
import com.porar.drag.DragController;
import com.porar.drag.DragController.ETouchImageCrop;
import com.porar.drag.DragLayer;
import com.porar.drag.MyAbsoluteLayout;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.UriResultImageCrop;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public abstract class MyAdapterViewPager_Quote extends PagerAdapter implements Serializable {

	View myView;
	private static final long serialVersionUID = 1L;
	private final Activity context;
	int sizePage = 0;
	LruCache<String, byte[]> mMemoryCache;
	// int frame[] = { R.drawable.ebooks_edit_bg1, R.drawable.ebooks_edit_bg2, R.drawable.ebooks_edit_bg3
	// , R.drawable.ebooks_edit_bg4, R.drawable.ebooks_edit_bg5 };
	ImageView img_frame;
	ImageViewTouch_Crop touch_Crop;
	TextView tv_name, tv_location, tv_date;
	ImageView image_cover;
	DragLayer mDragLayer;
	DragController mDragController;
	ImageDownloader_forCache image_cache;
	LinearLayout frame_linear_covertxt;
	private final Model_EBook_Shelf_List modelEbooks;
	AlertDialog alertDialog;
	EditText et;
	private String strChange = "DEFAULT";
	private onChange change;
	private onSaveView saveView;
	ArrayList<Model_Quote> arrModel_Quote;

	public interface onChange {
		void ChangeLoacation();
	}

	public void setChange(onChange change) {
		this.change = change;
	}

	public interface onSaveView {
		void SaveView(View view);

		void RemoveView(View view);
	}

	public void setSaveView(onSaveView saveView) {
		this.saveView = saveView;
	}

	public MyAdapterViewPager_Quote(Activity context, Model_EBook_Shelf_List modelEbooks, ArrayList<Model_Quote> arrModel_Quote) {
		this.context = context;
		this.modelEbooks = modelEbooks;
		this.arrModel_Quote = arrModel_Quote;
		// ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// int memoryClass = am.getMemoryClass();
		// int cacheSize = 1024 * 1024 * memoryClass / 8;
		// mMemoryCache = new LruCache<String, byte[]>(cacheSize);
		// sizePage = frame.length;
		sizePage = arrModel_Quote.size();
		mDragController = new DragController(context);
		image_cache = new ImageDownloader_forCache();

	}

	@Override
	public void destroyItem(final View view, final int position, final Object object) {
		((ViewPager) view).removeView((LinearLayout) object);
		if (saveView != null) {
			saveView.RemoveView((View) object);
		}
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return sizePage;

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public Object instantiateItem(final View view, final int position) {
		String url_bg_ebook = null;

		myView = LayoutInflater.from(context).inflate(R.layout.layout_frame, null);
		img_frame = (ImageView) myView.findViewById(R.id.frame_image_main_sw);

		// if (img_frame800 != null) {
		// img_frame = img_frame800;
		// // url_bg_ebook = arrModel_Quote.get(position).getEbooks_bg_L();
		// // set bg by size
		// }
		// ImageView img_frame600 = (ImageView) myView.findViewById(R.id.frame_image_main_sw600);
		// if (img_frame600 != null) {
		// img_frame = img_frame600;
		// // url_bg_ebook = arrModel_Quote.get(position).getEbooks_bg_L();
		// // set bg by size
		// }
		// ImageView img_frame0 = (ImageView) myView.findViewById(R.id.frame_image_main_swdefault);
		// if (img_frame0 != null) {
		// img_frame = img_frame0;
		// // set bg by size
		// }
		tv_name = (TextView) myView.findViewById(R.id.frame_drag_textview_Name);
		mDragLayer = (DragLayer) myView.findViewById(R.id.frame_draglayer);
		touch_Crop = (ImageViewTouch_Crop) myView.findViewById(R.id.frame_image_crop);
		image_cover = (ImageView) myView.findViewById(R.id.frame_image_cover);
		frame_linear_covertxt = (LinearLayout) myView.findViewById(R.id.frame_linear_covertxt);
		tv_location = (TextView) myView.findViewById(R.id.frame_textview_location);
		tv_date = (TextView) myView.findViewById(R.id.frame_textview_date);

		tv_location.setTypeface(StaticUtils.getTypeface(context, Font.LayijiMahaniyomV105ot));
		tv_date.setTypeface(StaticUtils.getTypeface(context, Font.LayijiMahaniyomV105ot));
		tv_name.setTypeface(StaticUtils.getTypeface(context, Font.LayijiMahaniyomV105ot));
		tv_location.setTag("location");
		myView.setTag(String.valueOf(position));

		tv_name.setText(modelEbooks.getName() + "\n" + modelEbooks.getPublisher());
		tv_location.setText(getEditChange());

		tv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// show dialog change location
				alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("Edit Location");
				et = new EditText(context);
				et.setText(tv_location.getText().toString());
				et.setPadding(10, 10, 10, 10);
				alertDialog.setView(et, 10, 10, 10, 10);
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setEditChange(et.getEditableText().toString());

						if (change != null) {
							change.ChangeLoacation();
						}
					}

				});
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		});
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat();
		String pattern = "dd-MM-yyyy";
		formatter.applyPattern(pattern);
		String formatdate = formatter.format(date);
		tv_date.setText(formatdate);

		// Square layout params w*w
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(dm.widthPixels, dm.widthPixels);
		img_frame.setLayoutParams(param);
		mDragLayer.setLayoutParams(param);
		touch_Crop.setLayoutParams(param);

		if (dm.widthPixels <= 800) {
			url_bg_ebook = arrModel_Quote.get(position).getEbooks_bg_L();
			Log.v("", "<= 800  width=" + dm.widthPixels);
		}
		if (dm.widthPixels > 800) {
			url_bg_ebook = arrModel_Quote.get(position).getEbooks_bg_XL();
			Log.v("", "> 800 width=" + dm.widthPixels);
		}
		String urlcover = AppMain.COVER_URL + modelEbooks.getBID() + "/" + modelEbooks.getCover();
		image_cache.download(urlcover, image_cover, null, true);
		image_cache.download(url_bg_ebook, img_frame, null, true);

		// position layout params center
		MyAbsoluteLayout.LayoutParams layoutParams = new MyAbsoluteLayout.LayoutParams(
				MyAbsoluteLayout.LayoutParams.WRAP_CONTENT,
				MyAbsoluteLayout.LayoutParams.WRAP_CONTENT,
				(image_cover.getDrawable().getMinimumWidth() / 2),
				(dm.widthPixels - image_cover.getDrawable().getMinimumHeight()));
		frame_linear_covertxt.setLayoutParams(layoutParams);
		frame_linear_covertxt.setPadding(5, 5, 5, 5);
		
		DragController dragController = mDragController;
		mDragLayer.setDragController(dragController);
		dragController.addDropTarget(mDragLayer);

		touch_Crop.setETouchImageCrop(new TouchImageCrop() {

			@Override
			public void onEnableTouch() {
				EnableTouch();
			}

			@Override
			public void onDisableTouch() {
				DisableTouch();
			}
		});

		dragController.setETouchImageCrop(new ETouchImageCrop() {

			@Override
			public void onEnableTouch() {

			}

			@Override
			public void onDisableTouch() {
				DisableTouch();
			}
		});
		frame_linear_covertxt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					// Log.i("", "_DOWN");
					EnableTouch();
					break;
				case MotionEvent.ACTION_MOVE:
					// Log.i("", "_MOVE");
					return startDrag(v);
				}
				return true;

			}
		});
		// image_cover.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch (event.getAction()) {
		//
		// case MotionEvent.ACTION_DOWN:
		// // Log.i("", "_DOWN");
		// EnableTouch();
		// break;
		// case MotionEvent.ACTION_MOVE:
		// // Log.i("", "_MOVE");
		// return startDrag(v);
		// }
		// return true;
		//
		// }
		// });
		// tv_name.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch (event.getAction()) {
		//
		// case MotionEvent.ACTION_DOWN:
		// // Log.i("", "_DOWN");
		// EnableTouch();
		// break;
		// case MotionEvent.ACTION_MOVE:
		// // Log.i("", "_MOVE");
		// return startDrag(v);
		// }
		// return true;
		//
		// }
		// });

		if (UriResultImageCrop.getUri() != null) {
			String full_path = UriResultImageCrop.getUri().getPath();
			Bitmap photoCrop = BitmapFactory.decodeFile(full_path);
			touch_Crop.setImageBitmapReset(photoCrop, true);

		}

		((ViewPager) view).addView(myView);
		if (saveView != null) {
			saveView.SaveView(myView);
		}
		return myView;

	}

	public boolean startDrag(View v) {
		Object dragInfo = v;
		mDragController.startDrag(v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
		return true;
	}

	public void toast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	public void setEditChange(String str) {
		strChange = str;
	}

	public String getEditChange() {
		return strChange;
	}

	public abstract void EnableTouch();

	public abstract void DisableTouch();

}
