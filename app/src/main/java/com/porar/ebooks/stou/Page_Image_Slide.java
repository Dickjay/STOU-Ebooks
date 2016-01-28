package com.porar.ebooks.stou;

import it.sephiroth.android.library.imagezoom.ImageViewTouch_Crop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.AdapterViewPegerReading;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.SetupViewReading;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.image2ebooks.EbooksPageFile;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Page;
import com.porar.ebooks.model.UriResultImageCrop;
import com.porar.ebooks.stou.activity.Activity_Quote;
import com.porar.ebooks.utils.AsyncTask_Download;
import com.porar.ebooks.utils.AsyncTask_Download_Ebooks_Watcher.ErrorException;
import com.porar.ebooks.utils.AsyncTask_EbooksPageFile;
import com.porar.ebooks.utils.AsyncTask_FetchURL;
import com.porar.ebooks.utils.StaticUtils;

@SuppressLint("NewApi")
public class Page_Image_Slide extends Activity {

	ImageButton btnBackToShelf, btnPrevious, bttnnext;
	LinearLayout layout_onzoom;
	static RelativeLayout topMenu;
	static RelativeLayout middleMenu;
	static RelativeLayout bottomMenu;
	SeekBar seekPage;
	TextView txtEbooksName;
	TextView txtPageSeek;
	MyViewPager viewPager;
	// thumbnails
	ProgressBar mProgress;
	AdapterViewPegerReading adapterViewPegerReading;
	// -------------//
	boolean isEmbedMode;
	int BID = 0;
	private int pageIndex = 0;
	private int numMaxPage = 0;
	private Hashtable<Integer, Model_Ebooks_Page> hashablePageList = new Hashtable<Integer, Model_Ebooks_Page>();
	// -------------//
	private EbooksShelfHeaderFile headerFile;
	private Model_EBook_Shelf_List modelEbooks;
	private Model_Customer_Detail customerDetail;
	// private int currentPage = 0;
	AsyncTask_FetchURL asyncTask_FetchURL;
	private final Handler mHandler = new Handler();
	ArrayList<LinearImageView> arrayListView_;
	int mProgressStatus = 0;
	int delay = 500;
	public static boolean click = false;
	static boolean hide = false;
	public static Handler handler = new Handler();
	ProgressDialog progressDialog = null;
	AlertDialog alertDialog;
	// Private Download
	private AsyncTask_Download_Page_Background downloader = null;
	private boolean isFree = false;
	private ImageView action_quote;
	ImageViewTouch_Crop image_cropView;
	private RelativeLayout relative_imagecrop;
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	Animation fade_in;
	private LruCache<String, byte[]> mMemoryCache;
	private String lastKey = "";
	Button button_cancel, button_next;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.layout_main_imageview2);
		int memClass = ((ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = (1024 * 1024) * memClass / 4;
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

		getBundle();
		setId();

		// Crop // Image // Quote

		fade_in = AnimationUtils.loadAnimation(this, R.anim.fadein);

		button_cancel = (Button) this.findViewById(R.id.button_cancel_crop);
		button_next = (Button) this.findViewById(R.id.button_next_crop);
		action_quote = (ImageView) this.findViewById(R.id.image_quote);
		// if (bookmark == true)
		// {
		// action_quote.setEnabled(false);
		// }
		image_cropView = (ImageViewTouch_Crop) this
				.findViewById(R.id.imageview_crop_background);

		relative_imagecrop = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout_imagecrop);
		relative_imagecrop.setVisibility(View.INVISIBLE);
		// action_quote.setVisibility(View.INVISIBLE);
		if (action_quote != null) 
		{
			action_quote.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View arg0) {
					Log.i("", "quote");
					StaticUtils.bookmark = true;
			
					saveLastPage();
				}
			});
			relative_imagecrop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("", "relative_imagecrop");
				}
			});
			button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clearView();
					Class_Manage.DeleteAllImageCropTemp();
				}
			});
			button_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// new MyViewPager
					Log.i("", "button_next");
					if (downloader != null) {
						downloader.cancelAllTask();
					}

					Intent intent = new Intent(Page_Image_Slide.this,
							Activity_Quote.class);
					intent.putExtra("model", modelEbooks);
					Page_Image_Slide.this.startActivity(intent);
					Page_Image_Slide.this.overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				}
			});

		}

		// Crop // Image // Quote
		// **********************

		setEbookPage_Reading();
		seekPage();
		BtnImplement();

		btnBackToShelf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (downloader != null) {
					downloader.cancelAllTask();
				}
				Page_Image_Slide.this.finish();
				Page_Image_Slide.this.overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

				if (StaticUtils.bookmark == false) {
					saveLastPage();
				} else {

				}
				// bookmark = false;

			}
		});

	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("bid", modelEbooks.getBID() + "");
		intent.putExtra("pointer", pageIndex + "");

		this.setResult(100, intent);
		super.finish();
	}

	private void BtnImplement() {
		btnPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
				viewPager.setPagingEnabled(true);

			}
		});
		bttnnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
				viewPager.setPagingEnabled(true);

			}
		});
	}

	private void setEbookPage_Reading() {

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				viewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}

		} catch (Exception e) {
			Toast.makeText(Page_Image_Slide.this,
					"Older OS, No HW acceleration anyway", Toast.LENGTH_LONG)
					.show();
		}

		hashablePageList = Class_Manage.getEbooksFileList(this,
				Shared_Object.getCustomerDetail.getCID(), modelEbooks.getBID(),
				modelEbooks.getName(), modelEbooks.getPages());
		if (hashablePageList != null) {
			numMaxPage = modelEbooks.getPages();

			ArrayList<Model_EBook_Shelf_List> arrayList = Class_Manage
					.getEbooksShelfInOffLineMode(this);
			for (int i = 0; i < arrayList.size(); i++) {
				if (String.valueOf(arrayList.get(i).getBID()).contains(
						String.valueOf(modelEbooks.getBID()))) {
					Log.i("", "bid " + modelEbooks.getBID());
					Log.i("", "name " + modelEbooks.getName());
					Log.i("", "page " + modelEbooks.getPages());
					if (arrayList.get(i).getPages() != modelEbooks.getPages()) {
						Log.i("", "not match");
						Log.i("", "file in device page : "
								+ arrayList.get(i).getPages());
						Log.i("", "file model page : " + modelEbooks.getPages());
						try {
							String filepath = Class_Manage.CURRENT_EBOOKS_FILE_PATH
									+ "/" + modelEbooks.getBID();
							File fileEbook = new File(filepath);
							for (File each : fileEbook.listFiles()) {
								if (each.delete()) {
									Log.i("", "delete " + each.getPath());
									Log.i("", "delete " + each.getName());
								}
							}
						} catch (NullPointerException e) {
							// TODO: handle exception
						}
						// delete

					}
					break;
				}
			}

			mProgress.setMax(numMaxPage);
			seekPage.setMax(numMaxPage - 1);

			if (modelEbooks.getPointer() != 0) {
				pageIndex = modelEbooks.getPointer();
				txtPageSeek.setText("Page : " + (modelEbooks.getPointer())
						+ "/" + numMaxPage);

			} else {
				pageIndex = 1;
				txtPageSeek.setText("Page : " + (pageIndex) + "/" + numMaxPage);

			}
			seekPage.setProgress(pageIndex - 1);
			txtEbooksName.setText(modelEbooks.getName());

			if (!modelEbooks.getPrice().contains("$")) {
				isFree = true;
				progressDialog = ProgressDialog.show(Page_Image_Slide.this, "",
						"WARNING: Check File", true, true);
			}

			setDownloadEbooksPage(pageIndex - 1);

			adapterViewPegerReading = new AdapterViewPegerReading(this,
					modelEbooks, hashablePageList, isEmbedMode);
			adapterViewPegerReading.setprogressDialog(progressDialog);

			viewPager.setAdapter(adapterViewPegerReading);
			viewPager.setCurrentItem(pageIndex - 1);
			adapterViewPegerReading
					.setOnControlViewPager(new SetupViewReading() {

						@Override
						public void setOnUnableZoom() {
							viewPager.setPagingEnabled(true);
							viewPager.setSwipeEnabled(true);

						}

						@Override
						public void setOnEnableZoom() {
							viewPager.setPagingEnabled(false);
							viewPager.setSwipeEnabled(false);

						}

						@Override
						public void setOnTouch() {

							if (click) {
								HideControl();
							} else {
								ShowControl();
							}
						}

					});
			HideControl();

			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(final int position) {

					Log.e("", "onPageState " + position);
					pageIndex = position + 1;
					txtPageSeek.setText("Page : " + (position + 1) + "/"
							+ numMaxPage);
					seekPage.setProgress(position);
					seekPage.setKeyProgressIncrement(position);

					if (StaticUtils.pid == pageIndex) {
						System.out.println(StaticUtils.pid);
						System.out.println(pageIndex);
						action_quote.setEnabled(false);
					} else {
						action_quote.setEnabled(true);
					}
					if (!isEmbedMode) {
						if (downloader != null) {
							downloader.cancelAllTask();
						}

						setDownloadEbooksPage(position);
					} else {

					}
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int position) {

				}
			});
			// currentPage = pageIndex;
		} else {
			Toast.makeText(Page_Image_Slide.this, "Page Not Found",
					Toast.LENGTH_LONG).show();
		}

	}

	private static void HideControl() {
		topMenu.setVisibility(View.INVISIBLE);
		middleMenu.setVisibility(View.INVISIBLE);
		bottomMenu.setVisibility(View.INVISIBLE);
		click = false;

	}

	private static void ShowControl() {
		topMenu.setVisibility(View.VISIBLE);
		middleMenu.setVisibility(View.VISIBLE);
		bottomMenu.setVisibility(View.VISIBLE);
		click = true;

	}

	private void seekPage() {
		seekPage.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromTouch) {
				txtPageSeek.setText("Page : " + (progress + 1) + "/"
						+ numMaxPage);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(final SeekBar seekBar) {
				Page_Image_Slide.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						pageIndex = (seekBar.getProgress() + 1);
						viewPager.setCurrentItem(seekBar.getProgress());
						viewPager.setPagingEnabled(true);
					}
				});

				if (!isEmbedMode) {
					if (downloader != null) {
						downloader.cancelAllTask();
					}

					setDownloadEbooksPage(pageIndex);
				} else {

				}

			}
		});
	}

	private void setDownloadEbooksPage(int pageIndex) {
		if (!isEmbedMode) {
			Log.e(AppMain.getTag(), "Set download page index : " + pageIndex);
			downloader = new AsyncTask_Download_Page_Background(
					circleArrayListByIndex(pageIndex, hashablePageList));

		} else {
			// Log.e(AppMain.getTag(), "Set download page index : Embed !!!");

		}
	}

	private static void getCacheDirectory(Context context, int BID) {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/ebooks.in.th/" + BID);
		if (!dir.exists()) {
			dir.mkdirs();
			Log.e(AppMain.getTag(),
					"Shared_Object - Prepare Directory" + dir.getAbsolutePath());
		}
	}

	private ArrayList<Model_Ebooks_Page> circleArrayListByIndex(
			int arrayStartIndex,
			Hashtable<Integer, Model_Ebooks_Page> hashablePageList) {
		String fileName = null;
		File currentFile = null;

		if (arrayStartIndex <= 0) {
			arrayStartIndex = 1;
		}
		if (arrayStartIndex > hashablePageList.size()) {
			arrayStartIndex = (hashablePageList.size());
		}
		int addIndex = 0;
		int endIndex = (hashablePageList.size() + 1);
		ArrayList<Model_Ebooks_Page> newList = new ArrayList<Model_Ebooks_Page>();
		while (arrayStartIndex < endIndex) {
			Model_Ebooks_Page model = hashablePageList.get(arrayStartIndex);
			if (model == null) {
				continue;
			}
			fileName = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/"
					+ model.getBID() + "/ebooks_" + model.getBID() + "_page_"
					+ model.getCurrentPagesNumber() + ".ebooks";
			currentFile = new File(fileName);

			if (!currentFile.exists()) {
				newList.add(model);
			} else {
				if (currentFile.length() <= 0) {
					Log.i("check file size", "isFolder  isfile not byte");
					newList.add(model);
				}
			}

			arrayStartIndex++;
			addIndex++;
		}

		if (addIndex <= endIndex) {
			arrayStartIndex = 0;
			endIndex = (hashablePageList.size() - addIndex);
			while (arrayStartIndex < endIndex) {
				arrayStartIndex++;
				Model_Ebooks_Page model = hashablePageList.get(arrayStartIndex);
				if (model == null) {
					continue;
				}
				fileName = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/"
						+ model.getBID() + "/ebooks_" + model.getBID()
						+ "_page_" + model.getCurrentPagesNumber() + ".ebooks";
				currentFile = new File(fileName);

				if (!currentFile.exists()) {
					newList.add(model);
				} else {
					if (currentFile.length() <= 0) {
						Log.i("check file size", "isFolder  isfile not byte");
						newList.add(model);
					}
				}

			}
		}
		mProgressStatus = numMaxPage - newList.size();
		return newList;
	}

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	private void saveLastPage() {

		try {
			String filehead = "ebooks_" + BID + ".porar";
			File obj = new File(this.getFilesDir() + "/" + filehead);

			headerFile = Class_Manage.LoadEbooksShelfHeaderFile(
					Page_Image_Slide.this, obj);
			modelEbooks = headerFile.getModelShelf();
			customerDetail = headerFile.getModelCustomer();
			StaticUtils.pid = pageIndex;
			modelEbooks.setPointer(pageIndex);
			headerFile.setModelShelf(modelEbooks);
			headerFile.setModelCustomer(customerDetail);
			Class_Manage.SaveEbooksObject(Page_Image_Slide.this, headerFile,
					"ebooks_" + modelEbooks.getBID() + ".porar");

			// String url =
			// "http://api.ebooks.in.th/update-bookshelf-pointer.ashx?";
			String url = AppMain.UPDATE_BOOK_SHELF_POINTER_URL;
			url += "bid=" + modelEbooks.getBID();
			url += "&cid=" + customerDetail.getCID();
			url += "&pointer=" + pageIndex;

			LoadAPIResultString apiResultString = new LoadAPIResultString();
			apiResultString
					.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

						@Override
						public void completeResult(String result) {
							Log.i("saveLastPage", "saveLastPage result= "
									+ result);
						}
					});
			System.out.println(url);
			apiResultString.execute(url);
		} catch (NullPointerException e) {
			Log.e("", "NullPointerException");
		}

		if (StaticUtils.bookmark == true) {
			action_quote.setEnabled(false);
		} else {
			action_quote.setEnabled(true);
		}

		// embed book
		// String embedList = Shared_Object.embedBooksBID.toString();
		// if (embedList.contains(modelEbooks.getBID() + "")) {
		// File sdDir = android.os.Environment.getExternalStorageDirectory();
		// File cacheDir = new File(sdDir, "data/hbook/embed/");
		// for (File f : cacheDir.listFiles()) {
		// if (f.getName().endsWith(".porar")) {
		// Class_Manage.SaveEbooksObjectEmbed(Page_Image_Slide.this, headerFile,
		// f.getAbsolutePath());
		// }
		// }
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				// cancel task
				if (relative_imagecrop != null) {
					if (relative_imagecrop.getVisibility() == View.VISIBLE) {
						clearView();
						Class_Manage.DeleteAllImageCropTemp();
						return true;
					}
				}

				if (this.downloader != null) {
					this.downloader.cancelAllTask();
				}

				Page_Image_Slide.this.finish();
				Page_Image_Slide.this.overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
				if (StaticUtils.bookmark == false) {
					saveLastPage();
				} else {

				}
				// bookmark = false;

			}

		}
		return false;

	}

	private void setId() {

		btnPrevious = (ImageButton) this
				.findViewById(R.id.btnPreviousPageslide);
		bttnnext = (ImageButton) this.findViewById(R.id.btnNextPageslide);
		mProgress = (ProgressBar) this.findViewById(R.id.reading_loadbar);
		btnBackToShelf = (ImageButton) this.findViewById(R.id.btnBackToShelf2);
		topMenu = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout_topMenu2);
		middleMenu = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout_middleMenu2);
		bottomMenu = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout_bottomMenu2);
		txtEbooksName = (TextView) this.findViewById(R.id.txtEbooksName2);
		txtPageSeek = (TextView) this.findViewById(R.id.txtEbooksPage2);
		seekPage = (SeekBar) this.findViewById(R.id.seekEbooksPage2);
		viewPager = (MyViewPager) this.findViewById(R.id.reading_viewpager);

	}

	private void getBundle() {

		isEmbedMode = false;
		Bundle b = getIntent().getExtras();
		if (b != null) {
			BID = b.getInt("bid");
			isEmbedMode = b.getBoolean("embed");

			String filehead = "ebooks_" + BID + ".porar";
			File obj = new File(this.getFilesDir() + "/" + filehead);
			try {
				headerFile = Class_Manage.LoadEbooksShelfHeaderFile(
						Page_Image_Slide.this, obj);
				modelEbooks = headerFile.getModelShelf();
				customerDetail = headerFile.getModelCustomer();

			} catch (NullPointerException e) {
				modelEbooks = (Model_EBook_Shelf_List) b.get("model");
				customerDetail = (Model_Customer_Detail) b.get("customer");

				EbooksShelfHeaderFile save = new EbooksShelfHeaderFile(
						modelEbooks, customerDetail);
				Class_Manage.SaveEbooksObject(Page_Image_Slide.this, save,
						filehead);
			} finally {
				getCacheDirectory(Page_Image_Slide.this, modelEbooks.getBID()); // create
																				// directory

			}
		}
	}

	private class AsyncTask_Download_Page_Background {

		private AsyncTask_EbooksPageFile asyncDownloadPage = null;
		private ArrayList<Model_Ebooks_Page> currentUrlPaths = null;
		private Model_Ebooks_Page currentModel = null;

		private int currentIndex = 0;
		private int maxIndex = 0;

		private Timer timer = null;
		private boolean isCancel = false;

		public AsyncTask_Download_Page_Background(
				ArrayList<Model_Ebooks_Page> pageUrlWithIndex) {
			this.currentUrlPaths = pageUrlWithIndex;
			this.maxIndex = pageUrlWithIndex.size();
			this.startDownload();

		}

		private void runPageDownload(Model_Ebooks_Page pageModel) {

			this.asyncDownloadPage = new AsyncTask_EbooksPageFile() {
				@Override
				protected void OnComplete(Model_Ebooks_Page model) {
					if (isFree) {
						String filename = Class_Manage.CURRENT_EBOOKS_FILE_PATH
								+ "/" + model.getBID() + "/ebooks_"
								+ model.getBID() + "_page_"
								+ model.getCurrentPagesNumber() + ".ebooks";
						File fileebook = new File(filename);
						if (fileebook.exists()) {
							Log.w("isFree", "isfile" + model.getBID());
							Log.w("LinearsetImage",
									" file size = " + fileebook.length());
							if (fileebook.length() <= 0) {

								if (asyncDownloadPage != null) {
									asyncDownloadPage.cancel(true);
									Log.w("isFree",
											"asyncDownloadPage.cancel(true)");
								}
								if (downloader != null) {
									downloader.cancelAllTask();
									Log.w("isFree",
											"downloader.cancelAllTask()");
								}
								DownloadEbookFile();
							}

							if (progressDialog != null) {
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
							}
							if (mProgressStatus < numMaxPage) {
								mProgressStatus++;
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										Log.e("", "setProgress "
												+ mProgressStatus + "/"
												+ numMaxPage);
										mProgress.setProgress(mProgressStatus);
									}
								});
							}
						} else {
							Log.w("isFree",
									"no file in directory" + model.getBID());
							if (asyncDownloadPage != null) {
								asyncDownloadPage.cancel(true);
								Log.w("isFree",
										"asyncDownloadPage.cancel(true)");
							}
							if (downloader != null) {
								downloader.cancelAllTask();
								Log.w("isFree", "downloader.cancelAllTask()");

							}

						}
					} else {
						if (mProgressStatus < numMaxPage) {
							mProgressStatus++;
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									Log.e("", "setProgress " + mProgressStatus
											+ "/" + numMaxPage);
									mProgress.setProgress(mProgressStatus);
								}
							});
						}
					}

					if (!isCancel) {
						startDownload();
					}
				}

				@Override
				protected void OnError(ErrorException e,
						final Model_Ebooks_Page model) {

					Log.e(AppMain.getTag(),
							"Download Background : OnError[ebooks_"
									+ model.getBID() + "_page_"
									+ model.getCurrentPagesNumber() + "]");
					if (downloader != null) {
						downloader.cancelAllTask();
						Log.w("OnError", "downloader.cancelAllTask()");
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(Page_Image_Slide.this,
									"An error has occurred Internet",
									Toast.LENGTH_LONG).show();
							if (progressDialog != null) {
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
							}

							if (isFree) {
								try {
									String filename = Class_Manage.CURRENT_EBOOKS_FILE_PATH
											+ "/" + modelEbooks.getBID();
									File fileebook = new File(filename);
									if (fileebook.listFiles().length <= 0) {
										Toast.makeText(Page_Image_Slide.this,
												"Error No file.",
												Toast.LENGTH_LONG).show();
										Toast.makeText(Page_Image_Slide.this,
												"Error No connection.",
												Toast.LENGTH_LONG).show();

									}
									for (File f : fileebook.listFiles()) {
										if (f.getName().contains(".ebooks")) {
											if (f.length() > 0) {
												// Log.e("OnError",
												// "isFree  file byte = " +
												// f.length());
											} else {
												// Log.e("OnError",
												// "isFree  file no byte = " +
												// f.length());
												DownloadEbookFile();
												break;
											}
										}
									}
								} catch (NullPointerException e) {
									Log.e("OnError", "no directory bid = "
											+ modelEbooks.getBID());
								}
							}

						}
					});

				}
			};
			this.asyncDownloadPage.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, pageModel);
		}

		private void startDownload() {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					timer.cancel();
					if (currentIndex < maxIndex) {
						currentModel = currentUrlPaths.get(currentIndex);
						currentIndex++;
						runPageDownload(currentModel);

					} else {
						if (progressDialog != null) {
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
						}
						Log.e(AppMain.getTag(),
								"Download Background : Done !!!");
					}
				}
			}, 0, 1000);
		}

		public void cancelAllTask() {
			isCancel = true;
			Log.e(AppMain.getTag(), "Download Background : cancelAllTask");
			if (this.asyncDownloadPage != null) {
				this.asyncDownloadPage.cancel(true);
			}
			if (timer != null) {
				timer.cancel();
				timer.purge();
			}
		}
	}

	public void DownloadEbookFile() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				String filename = "ebooks_" + modelEbooks.getBID()
						+ modelEbooks.getExtension();
				alertDialog = new AlertDialog.Builder(Page_Image_Slide.this)
						.create();
				alertDialog.setTitle(AppMain.getTag());
				if (checkEbooksCache(filename)) {
					alertDialog.setMessage("Open File PDF ?");
				} else {
					alertDialog.setMessage("Download File PDF ?");
				}
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "confirm",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
								System.gc();
								callExternalViewIntent(modelEbooks);
							}
						});
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Page_Image_Slide.this.finish();
								dialog.dismiss();
								System.gc();
							}
						});
				alertDialog.show();
			}
		});
	}

	AsyncTask_Download asyncTaskDownload;

	private void callExternalViewIntent(final Model_EBook_Shelf_List model) {
		String filename = "ebooks_" + model.getBID() + model.getExtension();
		if (this.checkEbooksCache(filename)) {
			// checkEbooksCache = true
			String filehead = "ebooks_" + model.getBID() + ".hash";
			File obj = new File(Page_Image_Slide.this.getFilesDir() + "/"
					+ filehead);
			EbooksHashFile hash = Class_Manage.LoadEbooksHashFile(
					Page_Image_Slide.this, obj);
			if (hash == null) {
				downloadFileEbook(model);
			} else {
				// Hash enable
				File target = new File(Class_Manage.CURRENT_EBOOKS_FILE_PATH
						+ "/" + filename);
				if (hash.getHashBytes() != target.length()) {
					// Delete incomplete download file
					target.delete();
					Toast.makeText(Page_Image_Slide.this,
							"File not found waiting for loading ...",
							Toast.LENGTH_LONG).show();
					downloadFileEbook(model);
				} else {
					Page_Image_Slide.this.callApplicationViewIntent(model);
				}
				target = null;
				System.gc();
			}
		} else {
			// checkEbooksCache = false
			downloadFileEbook(model);
		}

	}

	private boolean checkEbooksCache(String FileName) {
		String filename = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/"
				+ FileName;
		File file = new File(filename);
		if (file.exists()) {
			file = null;
			return true;
		}
		return false;
	}

	private void downloadFileEbook(Model_EBook_Shelf_List model) {
		asyncTaskDownload = new AsyncTask_Download(Page_Image_Slide.this,
				Shared_Object.getCustomerDetail) {
			@Override
			protected void OnComplete(Model_EBook_Shelf_List model) {
				Log.e("Ebooks.in.th",
						"Service_Download - Complete[" + model.getBID() + "]");
				Page_Image_Slide.this.callApplicationViewIntent(model);
			}

			@Override
			protected void OnError(Model_EBook_Shelf_List model) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								Page_Image_Slide.this,
								"Error while download this book, Please try again.",
								Toast.LENGTH_SHORT).show();

					}
				});
			}
		};
		asyncTaskDownload.execute(model);
	}

	@SuppressLint("DefaultLocale")
	private void callApplicationViewIntent(Model_EBook_Shelf_List model) {
		String filehead = "ebooks_" + model.getBID() + ".porar";
		File viewFolder = new File(Page_Image_Slide.this.getFilesDir() + "/"
				+ filehead);
		if (!viewFolder.exists()) {
			EbooksShelfHeaderFile save = new EbooksShelfHeaderFile(model,
					Shared_Object.getCustomerDetail);
			Class_Manage
					.SaveEbooksObject(Page_Image_Slide.this, save, filehead);
		}
		String tempFile = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/ebooks_"
				+ model.getBID() + model.getExtension();
		File target = new File(tempFile);
		PackageManager packageManager = Page_Image_Slide.this
				.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setType("application/pdf");
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() > 0) {
			intent.setDataAndType(Uri.fromFile(target), "application/pdf");
			Page_Image_Slide.this.startActivity(intent);
		} else {
			Toast.makeText(
					Page_Image_Slide.this,
					"Please install an app to view "
							+ model.getExtension().toUpperCase() + " file",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void setPara(final float scale, final float centerX,
			final float centerY, final float durationMs, final float getScale) {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				final long startTime = System.currentTimeMillis();
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				if (currentMs < durationMs) {
					HideControl();
				}
			}
		}, 100);

	}

	private void clearView() {
		relative_imagecrop.setVisibility(View.INVISIBLE);
		image_cropView.setImageBitmapReset(null, false);
	}

	private Uri createSaveCropFile(Bitmap bmp) {
		Uri uri;
		java.util.Date date = new java.util.Date();
		File file = new File(Class_Manage.getCacheDirectoryForCrop(this),
				"image_crop" + date.getHours() + "_" + date.getMinutes() + "_"
						+ date.getSeconds() + "_" + modelEbooks.getBID()
						+ ".jpg");
		Log.i("", "file.getPath  " + file.getPath());

		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(file), bmp.getByteCount());
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		uri = Uri.fromFile(file);
		AppMain.uri_imageCrop = uri;
		return uri;
	}

	// public boolean copyFile(File srcFile, File destFile) {
	// boolean result = false;
	// try {
	// InputStream in = new FileInputStream(srcFile);
	// try {
	// result = copyToFile(in, destFile);
	// } finally {
	// in.close();
	// }
	// } catch (IOException e) {
	// result = false;
	// }
	// return result;
	// }
	//
	// public boolean copyToFile(InputStream inputStream, File destFile) {
	// try {
	// OutputStream out = new FileOutputStream(destFile);
	// try {
	// byte[] buffer = new byte[4096];
	// int bytesRead;
	// while ((bytesRead = inputStream.read(buffer)) >= 0) {
	// out.write(buffer, 0, bytesRead);
	// }
	// } finally {
	// out.close();
	// }
	// return true;
	// } catch (IOException e) {
	// return false;
	// }
	// }

	private void doCropPhotoAction() {

		String filepath = Class_Manage.CURRENT_EBOOKS_FILE_PATH + "/"
				+ modelEbooks.getBID();
		String key = "ebooks_" + modelEbooks.getBID() + "_page_" + pageIndex
				+ ".ebooks";
		filepath = filepath + "/" + key;

		File f = new File(filepath);

		if (f.exists()) {
			EbooksPageFile result = Serialization.deserializeEbooksAndStore(
					this, filepath);
			if (result != null) {

				try {
					final int RawBitmapLengt = result.getRawBitmapLength();
					addBitmapToMemoryCache(key, result.getRawBitmap());
					result = null;
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

							return BitmapFactory.decodeByteArray(
									getBitmapFromMemCache(params[0]), 0,
									RawBitmapLengt, options);

						}

						@Override
						protected void onPostExecute(Bitmap result) {
							if (result != null) {
								UriResultImageCrop
										.setUri(createSaveCropFile(result));
								Log.e("", "" + UriResultImageCrop.getUri());

								Intent intent = new Intent(
										"com.android.camera.action.CROP");
								intent.setDataAndType(
										UriResultImageCrop.getUri(), "image/*");
								intent.putExtra("output",
										UriResultImageCrop.getUri());
								startActivityForResult(intent, CROP_FROM_CAMERA);
							}
							super.onPostExecute(result);
						}
					}.execute(key);
				} catch (Throwable e) {
					System.gc();
				}

			}

		} else {
			Log.e("no image", "no image: page  " + pageIndex);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM:
			// Log.d("", "PICK_FROM_ALBUM");
			//
			// mImage_CaptureUri = data.getData();
			// File original_file = getImageFile(mImage_CaptureUri);
			//
			// mImage_CaptureUri = createSaveCropFile();
			// File cpoy_file = new File(mImage_CaptureUri.getPath());
			//
			// Intent intent = new Intent("com.android.camera.action.CROP");
			// intent.setDataAndType(mImage_CaptureUri, "image/*");
			//
			// intent.putExtra("output", mImage_CaptureUri);
			//
			// startActivityForResult(intent, CROP_FROM_CAMERA);

			break;

		case PICK_FROM_CAMERA:
			// if (cropImageCrop.getUri() == null) {
			// cropImageCrop.setUri(AppMain.uri_imageCrop);
			// }
			//
			// Intent intent = new Intent("com.android.camera.action.CROP");
			// intent.setDataAndType(cropImageCrop.getUri(), "image/*");
			// intent.putExtra("output", cropImageCrop.getUri());
			// startActivityForResult(intent, CROP_FROM_CAMERA);

			break;

		case CROP_FROM_CAMERA:

			String full_path = UriResultImageCrop.getUri().getPath();
			Bitmap photo = BitmapFactory.decodeFile(full_path);

			relative_imagecrop.setVisibility(View.VISIBLE);
			image_cropView.setImageBitmapReset(photo, true);
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
}
