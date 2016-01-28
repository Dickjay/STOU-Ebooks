package it.sephiroth.android.library.imagezoom;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

import com.porar.ebooks.model.UriResultImageCrop;

@SuppressLint("NewApi")
public class ImageViewTouch_Crop extends ImageViewTouchBase_Crop {

	static final float MIN_ZOOM = 0.2f;
	protected ScaleGestureDetector mScaleDetector;
	protected GestureDetector mGestureDetector;
	protected int mTouchSlop;
	protected float mCurrentScaleFactor;
	protected float mScaleFactor;
	protected int mDoubleTapDirection;
	protected GestureListener mGestureListener;
	protected ScaleListener mScaleListener;
	PointF last = new PointF();
	PointF start = new PointF();
	private final LruCache<String, byte[]> mMemoryCache;
	private TouchImageCrop touchImageCrop;

	public void setETouchImageCrop(TouchImageCrop imageCrop) {
		touchImageCrop = imageCrop;
	}

	public ImageViewTouch_Crop(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = (1024 * 1024) * memClass / 8;
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

	}

	@Override
	protected void init()
	{
		super.init();
		mTouchSlop = ViewConfiguration.getTouchSlop();
		mGestureListener = new GestureListener();
		mScaleListener = new ScaleListener();

		mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
		mGestureDetector = new GestureDetector(getContext(), mGestureListener, null, true);
		mCurrentScaleFactor = 1f;
		mDoubleTapDirection = 1;

	}

	public interface TouchImageCrop {
		void onEnableTouch();

		void onDisableTouch();
	}

	@Override
	public void setImageRotateBitmapReset(RotateBitmap bitmap, boolean reset)
	{
		super.setImageRotateBitmapReset(bitmap, reset);
		mScaleFactor = getMaxZoom() / 4; // \ [/3]

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mScaleDetector.onTouchEvent(event);
		if (!mScaleDetector.isInProgress()) {
			mGestureDetector.onTouchEvent(event);
		}

		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_UP:
			// if (getScale() < 1F) {
			// // zoomTo(1f, 50);
			// break;
			// } else if (getScale() <= 1) {
			//
			// }
		
			
			if (touchImageCrop != null) {
				touchImageCrop.onDisableTouch();				
			}

			break;
		case MotionEvent.ACTION_MOVE:
			Log.e("", "Touch ACTION_MOVE");

			break;

		case MotionEvent.ACTION_DOWN:
			Log.e("Mode", "DRAG");
			if (touchImageCrop != null) {
				touchImageCrop.onEnableTouch();
			}

			break;
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_POINTER_2_UP:

			if (getScale() <= 1) {
				Log.e("", "Touch UnZoom");
				UriResultImageCrop.setMatrix(mBaseMatrix, mSuppMatrix);
				break;
			} else {
				Log.e("", "Touch Zoom");
				UriResultImageCrop.setMatrix(mBaseMatrix, mSuppMatrix);
				break;
			}

		}
		return true;
	}

	@Override
	protected void onZoom(float scale)
	{
		super.onZoom(scale);
		if (!mScaleDetector.isInProgress())
			mCurrentScaleFactor = scale;
	}

	protected float onDoubleTapPost(float scale, float maxZoom)
	{
		if (mDoubleTapDirection == 1) {
			// Log.e("", "Double Tab  Zoom");

			if ((scale + (mScaleFactor * 2)) <= maxZoom) {
				return scale + mScaleFactor;

			} else {
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {

			// Log.e("", "Double Tab UnZoom");

			mDoubleTapDirection = 1;
			return 1F; // 0.9999F
		}
	}

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			// float scale = getScale();
			// float targetScale = scale;
			// targetScale = onDoubleTapPost(scale, getMaxZoom());
			// targetScale = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
			// mCurrentScaleFactor = targetScale;
			// zoomTo(targetScale, e.getX(), e.getY(), 200);
			// invalidate();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			postTranslate(-distanceX, -distanceY);

			// if (e1 == null || e2 == null)
			// return false;
			// if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
			// return false;
			// if (mScaleDetector.isInProgress())
			// return false;
			// if (getScale() == 1f)
			// return false;
			// Log.e("", " distanceX " + (-distanceX));
			// Log.e("", " distanceY " + (-distanceY));
			// scrollBy(-distanceX, -distanceY);
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			// if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
			// return false;
			// if (mScaleDetector.isInProgress())
			// return false;
			//
			// float diffX = e2.getX() - e1.getX();
			// float diffY = e2.getY() - e1.getY();
			//
			// if (Math.abs(velocityX) > 800 || Math.abs(velocityY) > 800) {
			// //scrollBy(diffX / 2, diffY / 2, 300);
			// invalidate();
			// }
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		@SuppressWarnings("unused")
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{

			// float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();

			if (true) {
				targetScale = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
				float oldScale = getScale();
				float deltaScale = targetScale / oldScale;
				
				postScale(deltaScale, detector.getFocusX(), detector.getFocusY());
				// zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
				mCurrentScaleFactor = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}

}
