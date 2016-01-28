package com.porar.ebook.control;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean enabled;
	private boolean Swipe;
	Handler handler = new Handler();

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
		this.Swipe = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled && this.Swipe) {
			Log.e("", "onTouchEvent");
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		try {
			if (this.enabled && this.Swipe) {
				// unlock
				return super.onInterceptTouchEvent(event);
			}

		} catch (final Exception e) {
			// if you read this: don't worry! just close this class and do something else!
			Log.e("", "Exception " + e.toString());
		}
		// lock
		return false;

	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setSwipeEnabled(boolean enabled) {
		this.Swipe = enabled;
	}
}
