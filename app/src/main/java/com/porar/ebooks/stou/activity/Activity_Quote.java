package com.porar.ebooks.stou.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebooks.model.Model_Quote;
import com.porar.ebook.adapter.MyAdapterViewPager_Quote;
import com.porar.ebook.adapter.MyAdapterViewPager_Quote.onChange;
import com.porar.ebook.adapter.MyAdapterViewPager_Quote.onSaveView;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyLocation;
import com.porar.ebook.control.MyLocation.LocationResult;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.Quote_API;
import com.porar.ebook.control.Quote_API.Quote;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.UriResultImageCrop;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.XmlParser;

public class Activity_Quote extends Activity {

	String full_path;
	Activity_Quote myActivity;
	MyViewPager myViewPager;
	Button btn_cancel, btn_save;
	LinearLayout layoutDotpage;
	MyAdapterViewPager_Quote adapterViewPager_Quote;
	Dot_Pageslide dot_Pageslide;
	private Model_EBook_Shelf_List modelEbooks;
	Quote_API quote_API;
	AlertDialog alertDialog;
	ArrayList<Model_Quote> arrModel_Quote;
	ArrayList<HashMap<String, String>> menuItems;
	String addressString;
	LoadAPIResultString apiResultString;
	LocationManager locationManager;
	Location location;
	String provider;

	Timer timer;
	Geocoder geocoder;
	// View myView = null;
	ArrayList<View> arrViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_quote);

		myActivity = this;
		arrViews = new ArrayList<View>();
		getBundle();

		setId();
		LoadAPIQuote();
		OnClickEvent();

	}

	protected void showCurrentLocation() {
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				makeUseOfNewLocation(location);
			}
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}

	private void makeUseOfNewLocation(final Location location) {
		addressString = "input my location";
		// menuItems = new ArrayList<HashMap<String, String>>();

		if (location != null) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			final String urlgetLocation = "http://api.ebooks.in.th/getLocation.php?lat=" + lat + "&lon=" + lon + "&lim=1";
			Log.v("", "lat " + lat);
			Log.v("", "lon " + lon);
			Log.v("", "urlgetLocation " + urlgetLocation);

			apiResultString = new LoadAPIResultString();
			apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

				@Override
				public void completeResult(String result) {
					try {
						if (result.contains("CDATA[200]")) {
							XmlParser parser = new XmlParser();
							Document doc = parser.getDomElement(result);
							NodeList nl = doc.getElementsByTagName("item");

							for (int i = 0; i < nl.getLength(); i++) {
								Element e = (Element) nl.item(i);
								Log.v("", "name " + i + "_" + parser.getValue(e, "name"));

								addressString = parser.getValue(e, "name");
								if (myViewPager != null) {
									for (int i2 = 0; i2 < myViewPager.getChildCount(); i2++) {
										Object ob = myViewPager.getChildAt(i2);
										ViewGroup vg = (ViewGroup) ob;
										for (int j = 0; j < vg.getChildCount(); j++) {
											Object ob2r = vg.getChildAt(j);
											ViewGroup vg2r = (ViewGroup) ob2r;
											for (int r = 0; r < vg2r.getChildCount(); r++) {
												Object ob2 = vg2r.getChildAt(r);
												ViewGroup vg2 = (ViewGroup) ob2;
												for (int k = 0; k < vg2.getChildCount(); k++) {
													Object ob3 = vg2.getChildAt(k);
													ViewGroup vg3 = (ViewGroup) ob3;
													for (int l = 0; l < vg3.getChildCount(); l++) {
														Object ob4 = vg3.getChildAt(l);
														if (ob4 instanceof LinearLayout) {
															for (int m = 0; m < ((ViewGroup) ob4).getChildCount(); m++) {
																if (((ViewGroup) ob4).getChildAt(m) instanceof TextView) {
																	if (((ViewGroup) ob4).getChildAt(m).getTag() != null) {
																		((TextView) ((ViewGroup) ob4).getChildAt(m)).setText(addressString);
																		adapterViewPager_Quote.setEditChange(addressString);
																	}
																}
															}
														}
													}
												}
											}

										}
									}
								}
							}

						} else {
							Log.v("", "No address found");
							Toast.makeText(myActivity, "No GPS or network ..Signal please fill the location manually! No address found", Toast.LENGTH_LONG).show();

							if (myViewPager != null) {
								for (int i2 = 0; i2 < myViewPager.getChildCount(); i2++) {
									Object ob = myViewPager.getChildAt(i2);
									ViewGroup vg = (ViewGroup) ob;
									for (int j = 0; j < vg.getChildCount(); j++) {
										Object ob2r = vg.getChildAt(j);
										ViewGroup vg2r = (ViewGroup) ob2r;
										for (int r = 0; r < vg2r.getChildCount(); r++) {
											Object ob2 = vg2r.getChildAt(r);
											ViewGroup vg2 = (ViewGroup) ob2;
											for (int k = 0; k < vg2.getChildCount(); k++) {
												Object ob3 = vg2.getChildAt(k);
												ViewGroup vg3 = (ViewGroup) ob3;
												for (int l = 0; l < vg3.getChildCount(); l++) {
													Object ob4 = vg3.getChildAt(l);
													if (ob4 instanceof LinearLayout) {
														for (int m = 0; m < ((ViewGroup) ob4).getChildCount(); m++) {
															if (((ViewGroup) ob4).getChildAt(m) instanceof TextView) {
																if (((ViewGroup) ob4).getChildAt(m).getTag() != null) {
																	((TextView) ((ViewGroup) ob4).getChildAt(m)).setText(addressString);
																	adapterViewPager_Quote.setEditChange(addressString);
																}
															}
														}
													}
												}
											}
										}

									}
								}
							}
						}

					} catch (NullPointerException e) {
						e.printStackTrace();
						makeUseOfNewLocation(location);
					}
				}
			});
			apiResultString.execute(urlgetLocation);

		} else {
			Toast.makeText(myActivity, "No GPS or network ..Signal please fill the location manually!", Toast.LENGTH_LONG).show();
		}
	}

	private void LoadAPIQuote() {
		arrModel_Quote = new ArrayList<Model_Quote>();
		quote_API = new Quote_API(myActivity);
		quote_API.setQuoteListener(new Quote() {

			@Override
			public void onQuote(PList plList, final ProgressDialog pd) {
				try {
					for (PListObject each : (Array) plList.getRootElement()) {
						arrModel_Quote.add(new Model_Quote(each));
					}

					setViewpager(modelEbooks);

					if (pd != null) {
						if (pd.isShowing()) {
							pd.dismiss();
						}
					}
				} catch (NullPointerException e) {
					// refresh
					if (pd != null) {
						if (pd.isShowing()) {
							pd.dismiss();
						}
					}

					alertDialog = new AlertDialog.Builder(myActivity).create();
					alertDialog.setTitle(AppMain.getTag());
					alertDialog.setMessage("WARNING: An error has occurred. Please to try again ?");
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							pd.show();
							dialog.dismiss();
							System.gc();
							quote_API.loadQuote_API();
						}
					});
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							System.gc();
						}
					});
					alertDialog.show();

				}

			}

		});

		quote_API.loadQuote_API();
	}

	private void getBundle() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			modelEbooks = (Model_EBook_Shelf_List) b.get("model");
		}
	}

	private void OnClickEvent() {
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (quote_API != null) {
					if (quote_API.CancelTask()) {
						Log.v("CancelTask", "true");
					}
				}
				if (timer != null) {
					timer.cancel();
				}

				finish();
				myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// CaptureScreen
				if (myViewPager != null) {
					if (arrViews.size() > 0) {
						for (View each : arrViews) {
							Log.e("", "arrViews " + each.getTag());
							if (each.getTag().equals(String.valueOf(myViewPager.getCurrentItem()))) {
								ViewGroup vg = (ViewGroup) each;
								for (int j = 0; j < vg.getChildCount(); j++) {
									Object ob2r = vg.getChildAt(j);
									ViewGroup vg2r = (ViewGroup) ob2r;
									for (int r = 0; r < vg2r.getChildCount(); r++) {
										Object ob2 = vg2r.getChildAt(j);
										ViewGroup vg2 = (ViewGroup) ob2;
										vg2.setDrawingCacheEnabled(false);
										vg2.setDrawingCacheEnabled(true);
										if (vg2.isDrawingCacheEnabled()) {
											Bitmap bmp = vg2.getDrawingCache();
											UriResultImageCrop.setUriSave(createSaveCropFile(bmp));

											Intent intent = new Intent(myActivity, Activity_QuoteShare.class);
											intent.putExtra("model", modelEbooks);
											myActivity.startActivity(intent);
											myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
										}
									}

								}

							}

						}
						Log.e("View ", "position " + (myViewPager.getCurrentItem()));
					}
				}
			}
		});

	}

	private Uri createSaveCropFile(Bitmap bmp) {
		Uri uri;
		Date date = new Date();
		File file = new File(Class_Manage.getCacheDirectoryForSave(this), "image_crop" + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds() + "_" + modelEbooks.getBID() + ".jpg");

		Log.i("", "file.getPath  " + file.getPath());

		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), bmp.getByteCount());
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();

			Toast.makeText(myActivity, "Save..", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		uri = Uri.fromFile(file);
		AppMain.uri_imageCrop = uri;
		return uri;
	}

	private void setViewpager(Model_EBook_Shelf_List ebooks_Detail) {
		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}

		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		adapterViewPager_Quote = new MyAdapterViewPager_Quote(myActivity, ebooks_Detail, arrModel_Quote) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void EnableTouch() {
				myViewPager.setPagingEnabled(false);
				myViewPager.setSwipeEnabled(false);
			}

			@Override
			public void DisableTouch() {
				myViewPager.setPagingEnabled(true);
				myViewPager.setSwipeEnabled(true);
			}

		};
		adapterViewPager_Quote.setSaveView(new onSaveView() {

			@Override
			public void SaveView(View view) {
				arrViews.add(view);
				Log.e("View ", "add View " + view.getTag());
			}

			@Override
			public void RemoveView(View view) {
				arrViews.remove(view);
				Log.e("View ", "remove View " + view.getTag());
			}
		});
		adapterViewPager_Quote.setEditChange("Loading...");
		myViewPager.setAdapter(adapterViewPager_Quote);
		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				dot_Pageslide.setHeighlight(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		dot_Pageslide = new Dot_Pageslide(myActivity);
		layoutDotpage.addView(dot_Pageslide.setImage_slide(adapterViewPager_Quote.getCount()));
		if (adapterViewPager_Quote.getCount() > 0) {
			dot_Pageslide.setHeighlight(0);
		}

		adapterViewPager_Quote.setChange(new onChange() {

			@Override
			public void ChangeLoacation() {

				for (int i2 = 0; i2 < myViewPager.getChildCount(); i2++) {
					Object ob = myViewPager.getChildAt(i2);
					ViewGroup vg = (ViewGroup) ob;
					for (int j = 0; j < vg.getChildCount(); j++) {
						Object ob2r = vg.getChildAt(j);
						ViewGroup vg2r = (ViewGroup) ob2r;
						for (int r = 0; r < vg2r.getChildCount(); r++) {
							Object ob2 = vg2r.getChildAt(r);
							ViewGroup vg2 = (ViewGroup) ob2;
							for (int k = 0; k < vg2.getChildCount(); k++) {
								Object ob3 = vg2.getChildAt(k);
								ViewGroup vg3 = (ViewGroup) ob3;
								for (int l = 0; l < vg3.getChildCount(); l++) {
									Object ob4 = vg3.getChildAt(l);
									if (ob4 instanceof LinearLayout) {
										for (int m = 0; m < ((ViewGroup) ob4).getChildCount(); m++) {
											if (((ViewGroup) ob4).getChildAt(m) instanceof TextView) {
												if (((ViewGroup) ob4).getChildAt(m).getTag() != null) {
													((TextView) ((ViewGroup) ob4).getChildAt(m)).setText(adapterViewPager_Quote.getEditChange());
												}
											}
										}

									}
								}
							}
						}

					}
				}

			}
		});

		showCurrentLocation();

	}

	private void setId() {
		btn_cancel = (Button) findViewById(R.id.quote_btn_cancel);
		btn_save = (Button) findViewById(R.id.quote_btn_save);
		myViewPager = (MyViewPager) findViewById(R.id.quote_viewpager);
		layoutDotpage = (LinearLayout) findViewById(R.id.quote_linear_dotpage);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (quote_API != null) {
					if (quote_API.CancelTask()) {
						Log.v("CancelTask", "true");
					}
				}
				if (timer != null) {
					timer.cancel();
				}
				finish();
				myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}

		}
		return false;

	}

	private void updateWithNewLocation(Location location) {
		String addressString = "No address found";

		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lat, lng, 1);
				if (addresses.size() == 1) {
					addressString = "";
					Address address = addresses.get(0);
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
						addressString = addressString + address.getAddressLine(i) + " ";
					}
					addressString = addressString + address.getFeatureName();
					addressString = addressString + address.getLocality() + " ";
					addressString = addressString + address.getPostalCode() + " ";
					addressString = addressString + address.getCountryName() + " ";
				}
			} catch (IOException ioe) {
				Log.e("Geocoder IOException exception: ", ioe.getMessage());
			}
		}

		Log.i("", "Your Current Position is:\n" + "\n" + addressString);
	}

}
