package com.porar.ebooks.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
public class ImageDownloader_forCache {

	// Map<String, Bitmap> imageCache;
	// LruCache<String, Bitmap> imageCache;
	String datetime = null;
	Context context;
	String filename;
	File f;
	File file;
	Animation fade_in;
	TaskImageInfo_forCache image_info;
	Map<File, Bitmap> map_cache;
	private final HashMap<String, Bitmap> sHardBitmapCache;

	public ImageDownloader_forCache() {
		// imageCache = new HashMap<String, Bitmap>();
		// byte[] buffer = new byte[8 * 1024 * 1024];

		// imageCache = new LruCache<String, Bitmap>(buffer.length);

		Date date = new Date();
		SimpleDateFormat df2 = new SimpleDateFormat();
		datetime = df2.format(date);
		sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					LinkedHashMap.Entry<String, Bitmap> eldest) {
				if (size() > HARD_CACHE_CAPACITY) {
					// Entries push-out of hard reference cache are transferred to
					// soft reference cache
					sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
					return true;
				} else
					return false;
			}
		};
	}

	public void download(final String url, ImageView imageView) {
		context = imageView.getContext();

		if (cancelPotentialDownload(url, imageView)) {

			filename = String.valueOf(url.hashCode());
			f = new File(getCacheDirectory(context), filename);
			file = new File(getCacheDirectory(context), filename + "df");

			if (Shared_Object.isOfflineMode) {
				// is_offline not delete file cover
			} else {
				if (file.exists()) {
					BufferedReader reader = null;
					StringBuilder text = new StringBuilder();
					try {
						reader = new BufferedReader(new FileReader(file));
						String line;

						while ((line = reader.readLine()) != null) {
							text.append(line);
						}
						reader.close();

					} catch (IOException e) {
						// Log.e(AppMain.getTag(), e.toString());
					}
					SimpleDateFormat formatter = new SimpleDateFormat();
					try {
						Date date2 = formatter.parse(text.toString());
						Date epoch = date2;

						Date today = new Date();
						String dt = formatter.format(today);
						Date date3 = formatter.parse(dt.toString());

						long diff = date3.getTime() - epoch.getTime();
						long distance = (diff / (1000 * 60 * 60 * 24));

						if (distance >= 7) {
							f.delete();
							file.delete();
							// Log.d(AppMain.getTag(), "delete file cache");

						}

					} catch (Exception e) {

					}
				}
			}

			// Is the bitmap in our memory cache?
			Bitmap bitmap = null;

			// bitmap = imageCache.get(f.getPath());
			bitmap = getBitmapFromCache(f.getPath());
			if (bitmap == null) {

				try {
					bitmap = BitmapdecodeByteArray(f.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (bitmap != null) {
					// imageCache.put(f.getPath(), bitmap);
					addBitmapToCache(f.getPath(), bitmap);
				}
			}
			// No? download it
			if (bitmap == null) {
				try {
					BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
					DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
					imageView.setImageDrawable(downloadedDrawable);
					imageView.setVisibility(View.INVISIBLE);

					try {
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (ExceptionInInitializerError e) {
					// TODO: handle exception
				}

			} else {
				// Yes? set the image
				// imageView.setImageBitmap(imageCache.get(f.getPath()));
				imageView.setImageBitmap(getBitmapFromCache(f.getPath()));
				imageView.setVisibility(View.VISIBLE);
			}
		}
	}

	public void download(final String url, ImageView imageView, ImageView im_shadow) {

		context = imageView.getContext();
		if (url == null) {
			imageView.setImageDrawable(null);
		}
		if (cancelPotentialDownload(url, imageView)) {

			filename = String.valueOf(url.hashCode());
			f = new File(getCacheDirectory(context), filename);
			file = new File(getCacheDirectory(context), filename + "df");

			if (Shared_Object.isOfflineMode) {
				// is_offline not delete file cover
			} else {
				if (file.exists()) {
					BufferedReader reader = null;
					StringBuilder text = new StringBuilder();
					try {
						reader = new BufferedReader(new FileReader(file));
						String line;

						while ((line = reader.readLine()) != null) {
							text.append(line);
						}
						reader.close();

					} catch (IOException e) {
						// Log.e(AppMain.getTag(), e.toString());
					}
					SimpleDateFormat formatter = new SimpleDateFormat();
					try {
						Date date2 = formatter.parse(text.toString());
						Date epoch = date2;

						Date today = new Date();
						String dt = formatter.format(today);
						Date date3 = formatter.parse(dt.toString());

						long diff = date3.getTime() - epoch.getTime();
						long distance = (diff / (1000 * 60 * 60 * 24));

						if (distance >= 7) {
							f.delete();
							file.delete();
							// Log.d(AppMain.getTag(), "delete file cache");

						}

					} catch (Exception e) {

					}
				}
			}

			// Is the bitmap in our memory cache?
			Bitmap bitmap = null;

			// bitmap = imageCache.get(f.getPath());
			bitmap = getBitmapFromCache(f.getPath());
			if (bitmap == null) {

				try {
					bitmap = BitmapdecodeByteArray(f.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bitmap != null) {
					// imageCache.put(f.getPath(), bitmap);
					addBitmapToCache(f.getPath(), bitmap);
				}
			}
			// No? download it

			if (bitmap == null) {
				try {
					BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, im_shadow);
					DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
					imageView.setImageDrawable(downloadedDrawable);
					imageView.setVisibility(View.INVISIBLE);
					im_shadow.setVisibility(View.INVISIBLE);
					try {
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (ExceptionInInitializerError e) {
					// TODO: handle exception
				}

			} else {
				// Yes? set the image

				// imageView.setImageBitmap(imageCache.get(f.getPath()));

				imageView.setImageBitmap(getBitmapFromCache(f.getPath()));
				imageView.setVisibility(View.VISIBLE);
				im_shadow.setVisibility(View.VISIBLE);
			}
		}
	}

	public void download(final String url, ImageView imageView, ImageView im_shadow,boolean neverdie) {

		context = imageView.getContext();
		if (url == null) {
			imageView.setImageDrawable(null);
		}
		if (cancelPotentialDownload(url, imageView)) {

			filename = String.valueOf(url.hashCode());
			f = new File(getCacheDirectory(context), filename);
			file = new File(getCacheDirectory(context), filename + "df");

			if (Shared_Object.isOfflineMode) {
				// is_offline not delete file cover
			} else {
				
			}

			// Is the bitmap in our memory cache?
			Bitmap bitmap = null;

			// bitmap = imageCache.get(f.getPath());
			bitmap = getBitmapFromCache(f.getPath());
			if (bitmap == null) {

				try {
					bitmap = BitmapdecodeByteArray(f.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bitmap != null) {
					// imageCache.put(f.getPath(), bitmap);
					addBitmapToCache(f.getPath(), bitmap);
				}
			}
			// No? download it

			if (bitmap == null) {
				try {
					if (im_shadow!=null) {
						BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, im_shadow);
						DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
						imageView.setImageDrawable(downloadedDrawable);
						imageView.setVisibility(View.INVISIBLE);
						im_shadow.setVisibility(View.INVISIBLE);
						try {
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else{
						BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
						DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
						imageView.setImageDrawable(downloadedDrawable);
						imageView.setVisibility(View.INVISIBLE);
						try {
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				
				} catch (ExceptionInInitializerError e) {
					// TODO: handle exception
				}

			} else {
				// Yes? set the image

				// imageView.setImageBitmap(imageCache.get(f.getPath()));

				imageView.setImageBitmap(getBitmapFromCache(f.getPath()));
				if (im_shadow!=null) {
					imageView.setVisibility(View.VISIBLE);
					im_shadow.setVisibility(View.VISIBLE);
				}else{
					imageView.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	
	private Bitmap BitmapdecodeByteArray(String path) throws Exception, FileNotFoundException {
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			Log.i("", "isfile " + file.getPath());

			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPreferredConfig = Config.ARGB_8888;
			option.inDither = false;
			option.inPurgeable = true;
			option.inInputShareable = true;
			option.inTempStorage = new byte[1024];
			option.inSampleSize = 1;
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(path), new byte[1024].length);
			byte[] bMapArray = new byte[buf.available()];
			buf.read(bMapArray);
			bitmap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length, option);

			if (buf != null) {
				buf.close();
			}

			return bitmap;
		} else {
			Log.w("", "File Not Found" + file.getPath());
			return null;
		}

	}

	// cancel a download (internal only)
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				// Log.e("download", "The same URL is already being downloaded.");
				return false;
			}
		}
		return true;
	}

	// gets an existing download if one exists for the imageview
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	// our caching functions
	// Find the dir to save cached images
	private static File getCacheDirectory(Context context) {
		String sdState = android.os.Environment.getExternalStorageState();
		File cacheDir;

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, "data/ebooks.in.th/images_cache");
		}
		else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	// download asynctask
	public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;
		private WeakReference<ImageView> image_shadowReference;

		public BitmapDownloaderTask(ImageView imageView, ImageView image_shadow) {

			imageViewReference = new WeakReference<ImageView>(imageView);
			image_shadowReference = new WeakReference<ImageView>(image_shadow);

		}

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);

		}

		@Override
		// Actual download method, run in the task thread
		protected Bitmap doInBackground(String... params) {
			// params comes from the execute() call: params[0] is the url.
			url = params[0];
			// Log.d(AppMain.getTag(), "download = " + url);
			return downloadBitmap(params[0]);
		}

		@SuppressWarnings("unchecked")
		@Override
		// Once the image is downloaded, associates it to the imageView
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with it
				if (this == bitmapDownloaderTask) {

					if (bitmap == null) {
						// Log.d("", "bitmap = null set image No LOGO");
						BitmapFactory.Options option = new BitmapFactory.Options();
						option.inPreferredConfig = Config.ARGB_8888;
						option.inDither = false;
						option.inPurgeable = true;
						option.inInputShareable = true;
						option.inTempStorage = new byte[1024];
						option.inSampleSize = 1;

						bitmap = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.cover_not_available2x, option);
						imageView.setImageBitmap(bitmap);

					} else {
						String filename = String.valueOf(url.hashCode());
						File f = new File(getCacheDirectory(imageView.getContext()), filename);
						File file_read = new File(getCacheDirectory(imageView.getContext()), filename + "df");
						// imageCache.put(f.getPath(), bitmap);
						addBitmapToCache(f.getPath(), bitmap);

						if (bitmap != null) {
							// imageView.setImageBitmap(imageCache.get(f.getPath()));
							imageView.setImageBitmap(getBitmapFromCache(f.getPath()));
							fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
							fade_in.setAnimationListener(new AnimationLoad(imageView));
							imageView.setAnimation(fade_in);

						}
						if (image_shadowReference != null) {
							ImageView image_shadow = image_shadowReference.get();
							fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
							fade_in.setAnimationListener(new AnimationLoad(image_shadow));
							image_shadow.setAnimation(fade_in);
						}
						map_cache = new HashMap<File, Bitmap>();
						map_cache.put(f, bitmap);
						// writeBitmapFile inBackground
						image_info = new TaskImageInfo_forCache();
						image_info.writeDateFile(datetime, file_read);
						image_info.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, map_cache);
					}

					imageView.setVisibility(View.VISIBLE);

					// cache the image

				}
			}

		}

	}

	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super(Color.BLACK);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000;
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	// Hard cache, with a fixed maximum capacity and a life duration
	// private final HashMap<String, Bitmap> sHardBitmapCache= new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
	// @Override
	// protected boolean removeEldestEntry(
	// LinkedHashMap.Entry<String, Bitmap> eldest) {
	// if (size() > HARD_CACHE_CAPACITY) {
	// // Entries push-out of hard reference cache are transferred to
	// // soft reference cache
	// sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
	// return true;
	// } else
	// return false;
	// }
	// };

	private Bitmap getBitmapFromCache(String path) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(path);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(path);
				sHardBitmapCache.put(path, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(path);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(path);
			}
		}

		return null;
	}

	private void addBitmapToCache(String path, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(path, bitmap);
			}
		}
	}

	@SuppressWarnings("resource")
	public Bitmap decodeStream(InputStream is, Rect outPadding, Options opts) {

		if (!is.markSupported()) {
			// long free = Debug.getNativeHeapFreeSize() - 1024;
			// byte[] buffer = new byte[(int) free];
			BufferedInputStream bis = new BufferedInputStream(is, new byte[1024].length);

			is = bis;
		}
		return BitmapFactory.decodeStream(new FlushedInputStream(is), null, opts);
	}

	private class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	// the actual download code
	public Bitmap downloadBitmap(String url) {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpClient client = new DefaultHttpClient(params);
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				// Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				BufferedHttpEntity bufHttpEntity = null;
				try {
					bufHttpEntity = new BufferedHttpEntity(entity);
					inputStream = bufHttpEntity.getContent();

					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inPreferredConfig = Config.ARGB_8888;
					option.inDither = false;
					option.inPurgeable = true;
					option.inInputShareable = true;
					option.inTempStorage = new byte[1024];
					option.inSampleSize = 1;

					return decodeStream(inputStream, null, option);

				} finally {
					if (inputStream != null) {
						inputStream.close();

					}
					System.gc();

					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or IllegalStateException
			getRequest.abort();
			// Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
		} finally {
			if (client != null) {
				// client.close();
			}
		}
		return null;
	}

	private class AnimationLoad implements AnimationListener {
		ImageView imageView;

		public AnimationLoad(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			imageView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}
}
