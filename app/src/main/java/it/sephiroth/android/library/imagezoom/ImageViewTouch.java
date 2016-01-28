package it.sephiroth.android.library.imagezoom;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

import com.porar.ebooks.image2ebooks.EbooksPageFile;
import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.EventClass;
import com.porar.ebooks.stou.IEventClassListener;
import com.porar.ebooks.stou.Serialization;

@SuppressLint("NewApi")
public class ImageViewTouch extends ImageViewTouchBase {

	static final float MIN_ZOOM = 0.9f;
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

	// We can be in one of these 3 states

	private final List<IEventClassListener> _listeners = new ArrayList<IEventClassListener>();
	private EbooksPageFile restore;
	private final LruCache<String, byte[]> mMemoryCache;
	private String lastKey = "";
	private boolean isEmbedMode = false;

	private synchronized void throwEvent(String eventName) {
		EventClass event = new EventClass(getContext(), eventName);
		Iterator<IEventClassListener> i = _listeners.iterator();
		while (i.hasNext()) {
			i.next().handleClassEvent(event);
		}
	}

	public ImageViewTouch(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = (1024 * 1024) * memClass / 8;
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

	}

	public synchronized void setOnEventListener(IEventClassListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeOnEventListener(IEventClassListener listener) {
		_listeners.remove(listener);
	}

	String filepath = "";
	String key = "";

	public void setPageView(Model_Ebooks_Page model, int pageIndex) {

		// alert
		if (this.isEmbedMode) {
			filepath = "embed_book/" + model.getBID();
			key = "ebooks_" + model.getBID() + "_page_" + pageIndex + ".ebooks";
			filepath = filepath + "/" + key;

			restore = Serialization.deserializeEmbedEbooksAndStore(getContext(), filepath);
		} else {
			filepath = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/" + model.getBID();
			key = "ebooks_" + model.getBID() + "_page_" + pageIndex + ".ebooks";
			filepath = filepath + "/" + key;
			// restore = Serialization.deserializeEbooksAndStore(filepath);

			new AsyncTask<String, Void, EbooksPageFile>() {

				@Override
				protected EbooksPageFile doInBackground(String... params) {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return Serialization.deserializeEbooksAndStore(getContext(),params[0]);
				}

				@Override
				protected void onPostExecute(EbooksPageFile result) {
					restore = result;
					if (restore == null) {
						if (isEmbedMode == false) {
							File file = new File(filepath);
							file.delete();
							file = null;
						}

					} else {
						try {
							final int RawBitmapLengt = restore.getRawBitmapLength();
							addBitmapToMemoryCache(key, restore.getRawBitmap());
							restore = null;
							System.gc();

							new AsyncTask<String, Void, Bitmap>() {

								@Override
								protected Bitmap doInBackground(String... params) {
									BitmapFactory.Options options = new BitmapFactory.Options();
									options.inDither = false;
									options.inPurgeable = true;
									options.inInputShareable = true;
									options.inTempStorage = new byte[1024];
									options.inSampleSize = 1;

									return BitmapFactory.decodeByteArray(getBitmapFromMemCache(params[0]), 0, RawBitmapLengt, options);

								}

								@Override
								protected void onPostExecute(Bitmap result) {
									setImageBitmapReset(result, true);
									super.onPostExecute(result);
								}
							}.execute(key);
						} catch (Throwable e) {
							System.gc();
						}
					}

					super.onPostExecute(result);
				}

			}.execute(filepath);

		}

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

			if (getScale() < 1F) {
				zoomTo(1f, 50);
				break;
			} else if (getScale() <= 1) {
				throwEvent("touch_image");
			}

			break;

		case MotionEvent.ACTION_DOWN:
			Log.e("Mode", "DRAG");
			break;
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_POINTER_2_UP:

			if (getScale() <= 1) {
				Log.e("", "Touch UnZoom");
				throwEvent("UnZoom");
				break;
			} else {
				Log.e("", "Touch Zoom");
				throwEvent("Zoom");
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
			throwEvent("Zoom");
			if ((scale + (mScaleFactor * 2)) <= maxZoom) {
				return scale + mScaleFactor;

			} else {
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {

			// Log.e("", "Double Tab UnZoom");
			throwEvent("UnZoom");
			mDoubleTapDirection = 1;
			return 1F; // 0.9999F
		}
	}

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			float scale = getScale();
			float targetScale = scale;
			targetScale = onDoubleTapPost(scale, getMaxZoom());
			targetScale = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
			mCurrentScaleFactor = targetScale;
			zoomTo(targetScale, e.getX(), e.getY(), 200);
			invalidate();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			if (e1 == null || e2 == null)
				return false;
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			if (mScaleDetector.isInProgress())
				return false;
			if (getScale() == 1f)
				return false;
			scrollBy(-distanceX, -distanceY);
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			if (mScaleDetector.isInProgress())
				return false;

			float diffX = e2.getX() - e1.getX();
			float diffY = e2.getY() - e1.getY();

			if (Math.abs(velocityX) > 800 || Math.abs(velocityY) > 800) {
				scrollBy(diffX / 2, diffY / 2, 300);
				invalidate();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		@SuppressWarnings("unused")
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();
			if (true) {
				targetScale = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
				zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
				mCurrentScaleFactor = Math.min(getMaxZoom(), Math.max(targetScale, MIN_ZOOM));
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}

	private void addBitmapToMemoryCache(String key, byte[] bitmap) {
		try {
			mMemoryCache.remove(lastKey);
		} catch (Exception e) {
			// None
		}
		if (getBitmapFromMemCache(key) == null) {
			lastKey = key;
			mMemoryCache.put(key, bitmap);
		}
	}

	private byte[] getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * @return the isEmbedMode
	 */
	public boolean isEmbedMode() {
		return isEmbedMode;
	}

	/**
	 * @param isEmbedMode
	 *            the isEmbedMode to set
	 */
	public void setEmbedMode(boolean isEmbedMode) {
		this.isEmbedMode = isEmbedMode;
	}
}
