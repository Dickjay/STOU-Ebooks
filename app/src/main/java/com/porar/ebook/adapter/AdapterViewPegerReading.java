package com.porar.ebook.adapter;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.porar.ebook.control.SetupViewReading;
import com.porar.ebook.control.ThrowEventReading;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.stou.LinearImageView;
import com.porar.ebooks.stou.LinearImageView.CheckFileSize;

public class AdapterViewPegerReading extends PagerAdapter {

	Activity context;
	ArrayList<LinearLayout> views;
	LinearImageView myView = null;
	Model_EBook_Shelf_List modelEbooks;
	Hashtable<Integer, Model_Ebooks_Page> hashablePageList;
	Handler handler = new Handler();
	boolean isEmbedMode;
	private int pageIndex = 0;
	private int maxPage = 0;
	private final int currentposition = 0;
	private final int pageindex = 0;
	private final int curIndex = 0;

	ArrayList<SetupViewReading> setupViewReadings = new ArrayList<SetupViewReading>();
	ArrayList<LinearImageView> arrayLisTouchImageView_viewvers = new ArrayList<LinearImageView>();

	public void setOnControlViewPager(SetupViewReading viewReading) {
		setupViewReadings.add(viewReading);
	}

	public AdapterViewPegerReading(final Activity context, final Model_EBook_Shelf_List modelEbooks, final Hashtable<Integer, Model_Ebooks_Page> hashablePageList, final boolean isEmbedMode) {
		this.context = context;
		this.modelEbooks = modelEbooks;
		this.isEmbedMode = isEmbedMode;
		this.hashablePageList = hashablePageList;

		views = new ArrayList<LinearLayout>();

		maxPage = (modelEbooks.getPages());
		pageIndex = modelEbooks.getPointer();
		if (pageIndex == 0) {
			pageIndex = 1;
		}
	}

	@Override
	public void destroyItem(final View view, int position, final Object object) {

		((ViewPager) view).removeView((LinearLayout) object);

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return maxPage;
	}

	@Override
	public Object instantiateItem(final View view, final int position) {

		myView = new LinearImageView(context, isEmbedMode, hashablePageList, position + 1);
		myView.setOnCheckFileSize(new CheckFileSize() {
			
			@Override
			public void isFile() {
				if (Pdialog != null) {
					if (Pdialog.isShowing()) {
						Pdialog.dismiss();
					}
				}
			}
		});
		myView.setOnThrowEventReading(new ThrowEventReading() {

			@Override
			public void ZoomMode() {
				for (SetupViewReading each : setupViewReadings) {
					each.setOnEnableZoom();
				}
			}

			@Override
			public void UnZoomMode() {
				for (SetupViewReading each : setupViewReadings) {
					each.setOnUnableZoom();
				}
			}

			@Override
			public void TouchMode() {
				for (SetupViewReading each : setupViewReadings) {
					each.setOnTouch();
				}
			}

		});
		((ViewPager) view).addView(myView);

		return myView;

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
	ProgressDialog Pdialog;
	public void setprogressDialog(ProgressDialog progressDialog) {
		Pdialog = progressDialog;
	}

}
