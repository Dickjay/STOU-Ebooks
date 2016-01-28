package com.porar.ebooks.stou.fragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.AdapterGalleryBanner;
import com.porar.ebook.adapter.Adapter_List_Publisher;
import com.porar.ebook.adapter.Adapter_SearchList_Publisher;
import com.porar.ebook.adapter.MyAdapterViewPager_Publisher;
import com.porar.ebook.control.Category_Publisher;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.Refresh_Publisher;
import com.porar.ebook.control.SaveLastUrl;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.control.View_SearchPublisher;
import com.porar.ebook.control.View_Setting;
import com.porar.ebook.control.View_TypePublisher;
import com.porar.ebooks.model.Model_Banner;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.stou.activity.Activity_Tab;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Fragment_Publisher extends Fragment {
	public FragmentActivity myActivity;
	public Bundle bundle;
	public static String tag = "Fragment_Publisher";
	public static String serial_category = "serial_category";
	public static String serial_type = "serial_type";
	private Gallery gallery;
	private ImageView img_previous, img_next;
	private ArrayList<Model_Banner> arrayBanners_banner;
	private int pageGallery = 0;
	private final Handler handler = new Handler();
	private final int scrollDelay = 5000;
	private MyViewPager myViewPager;
	public ProgressDialog progressDialog = null;
	// control
	private TextView txt_refresh, txt_search, txt_setting;
	private ImageView image_refresh, image_search, image_setting;
	private LinearLayout linear_refresh, linear_search, linear_setting, linear_customforTabview, linear_dotpage;
	private RelativeLayout rt_dummyscreen, rt_head;
	// btn
	private Button btn_type;
	// animation
	private Animation fade_in;
	private AlertDialog alertDialog;
	private MyAdapterViewPager_Publisher adapterViewPager_Publisher;
	public Dot_Pageslide dot_Pageslide;
	public static SaveLastUrl lastUrl = null;
	public Refresh_Publisher refresh_Publisher;
	public View_SearchPublisher view_SearchPublisher;
	private Adapter_SearchList_Publisher adapter_SearchList;
	public View_TypePublisher view_TypePublisher;
	public String type_publisher = "";
	public Category_Publisher category_Publisher;
	View_Setting view_Setting;

	// public boolean isphone = false;
	// :: version.phone :: Object
	ListView plistview;
	ImageView picon_refresh, picon_search;
	Button pbtn_update, pbtn_hot, pbtn_name;
	String[] str_type = { "N", "R", "F", "P" };
	Adapter_List_Publisher adapter_List_Publisher;
	public static String bundle_publisher_type_phone = "bundle_publisher_type_phone";
	public static Fragment_Publisher fragment_Publisher;
	public Fragment frag_searchpublisher = null;
	public boolean HaveFragment = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(tag, "onCreate");

		if (AppMain.isphone) {
			Log.e(tag, "onCreate isphone");
			bundle = new Bundle();
			myActivity = getActivity();
			lastUrl = new SaveLastUrl();
			fragment_Publisher = this;
			HaveFragment = false;
		} else {
			bundle = new Bundle();
			myActivity = getActivity();
			lastUrl = new SaveLastUrl();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(tag, "onCreateView");
		View view = inflater.inflate(R.layout.activity_publisher, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e(tag, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		// RelativeLayout layout_main = (RelativeLayout) getActivity().findViewById(R.id.publisher_rt_main_phone);
		if (AppMain.isphone) {

			setId_Phone();
			if (bundle.getInt(bundle_publisher_type_phone) != 0) {
				setDefaultEnableType(bundle.getInt(bundle_publisher_type_phone));
			}
			setListView(AppMain.pList_default_publisher, null);
		} else {
			// setID
			setId();
			try {
				Model_Setting model_setting = Class_Manage.getModel_Setting(myActivity);
				if (model_setting != null) {
					((RelativeLayout) getActivity().findViewById(R.id.publisher_mainlayout)).setBackgroundResource(model_setting.getDrawable_backgroundStyle());
				}
				Model_SettingShelf settingShelf = Class_Manage.getModel_SettingShelf(myActivity);
				if (settingShelf != null) {
					((ImageView) getActivity().findViewById(R.id.publisher_img_shelf1)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.publisher_img_shelf2)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.publisher_img_shelf3)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.publisher_img_shelf4)).setImageResource(settingShelf.getDrawable_ShelfStyle());
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
			}

			setBannerGallery();
			setOnclick();
			setViewPager(AppMain.pList_default_publisher, null);

			// get InstanceState
			if (bundle.getString(serial_type) != null) {
				btn_type.setText(bundle.getString(serial_type));
			}
		}

	}

	private void setListView(PList plist, ProgressDialog dialog) {
		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		try {
			Log.i(tag, "plist " + plist.toString());

			adapter_List_Publisher = null;
			adapter_List_Publisher = new Adapter_List_Publisher(myActivity, 0, plist);
			plistview.setAdapter(adapter_List_Publisher);

			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		} catch (NullPointerException e) {
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}
	}

	private void setId_Phone() {
		plistview = (ListView) getActivity().findViewById(R.id.publisher_plistview);
		picon_refresh = (ImageView) getActivity().findViewById(R.id.publisher_image_prefresh);
		picon_search = (ImageView) getActivity().findViewById(R.id.publisher_image_psearch);
		pbtn_update = (Button) getActivity().findViewById(R.id.tabview_btn_publisher_pupadate);
		pbtn_hot = (Button) getActivity().findViewById(R.id.tabview_btn_publisher_precommend);
		pbtn_name = (Button) getActivity().findViewById(R.id.tabview_btn_publisher_pname);
		fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
		picon_refresh.setOnClickListener(new OnClickControl());
		picon_search.setOnClickListener(new OnClickControl());

		pbtn_update.setEnabled(false);
		pbtn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_publisher_type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("U");
			}
		});
		pbtn_hot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_publisher_type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("R");
			}
		});
		pbtn_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_publisher_type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("N");
			}
		});

	}

	private void loadApiByType(String type_publisher) {
		AppMain.pList_default_publisher = null;
		category_Publisher = new Category_Publisher(myActivity, type_publisher);
		category_Publisher.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {
				setListView(resultPlist, pd);

			}

			@Override
			public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}

			@Override
			public void StartLoadPList() {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}
		});
		category_Publisher.LoadCategoryAPI();

	}

	private void setDefaultEnableType(int id) {

		pbtn_update.setEnabled(true);
		pbtn_hot.setEnabled(true);
		pbtn_name.setEnabled(true);

		if (id == pbtn_update.getId()) {
			pbtn_update.setEnabled(false);
		}
		if (id == pbtn_hot.getId()) {
			pbtn_hot.setEnabled(false);
		}
		if (id == pbtn_name.getId()) {
			pbtn_name.setEnabled(false);
		}

	}

	private void setViewPager(final PList pList, final ProgressDialog dialog) {

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}

		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		try {
			myViewPager.removeAllViews();
			adapterViewPager_Publisher = null;

			adapterViewPager_Publisher = new MyAdapterViewPager_Publisher(myActivity, pList);

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					removeViewDotPage();

					dot_Pageslide = new Dot_Pageslide(myActivity);
					linear_dotpage.addView(dot_Pageslide.setImage_slide(adapterViewPager_Publisher.getCount()));
					myViewPager.setAdapter(adapterViewPager_Publisher);
					dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
					myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							dot_Pageslide.setHeighlight(position);
						}

						@Override
						public void onPageScrolled(int arg0, float arg1, int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}
					});

					if (dialog != null) {
						if (dialog.isShowing()) {
							if (adapterViewPager_Publisher != null) {
								dialog.dismiss();
							}
						}
					}
					if (alertDialog != null) {
						if (alertDialog.isShowing()) {
							alertDialog.dismiss();
						}
					}
				}
			}, 50);

			alertDialog = new AlertDialog.Builder(myActivity).create();
			// TextView title = new TextView(myActivity);
			// title.setText(AppMain.getTag());
			// title.setPadding(10, 10, 10, 10);
			// title.setGravity(Gravity.CENTER);
			// title.setTextSize(23);
			// alertDialog.setCustomTitle(title);
			TextView msg = new TextView(myActivity);
			msg.setText("Please Wait... Loading");
			msg.setPadding(10, 10, 10, 10);
			msg.setGravity(Gravity.CENTER);
			alertDialog.setView(msg);
			alertDialog.show();

		} catch (NullPointerException e) {
			// load again
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();

		}

	}

	private void removeViewDotPage() {
		if (linear_dotpage.getChildCount() > 0) {
			linear_dotpage.removeAllViews();
		}
	}

	private void setBannerGallery() {
		try {
			arrayBanners_banner = new ArrayList<Model_Banner>();
			for (PListObject each : (Array) AppMain.pList_news.getRootElement()) {
				arrayBanners_banner.add(new Model_Banner(each));
			}
			gallery.setAdapter(new AdapterGalleryBanner(myActivity, arrayBanners_banner));
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {

					handler.removeCallbacks(runnableNext);
					handler.removeCallbacks(runnablePrevious);
					pageGallery = (position + 1);
					if (pageGallery == gallery.getCount()) {
						handler.postDelayed(runnablePrevious, scrollDelay);
					} else {
						handler.postDelayed(runnableNext, scrollDelay);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}
			});
		} catch (NullPointerException e) {
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
		}
	}

	private void setOnclick() {
		img_previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DisplayMetrics displayMetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				int width = (displayMetrics.widthPixels);
				gallery.onFling(null, null, (float) 2.5 * width, 0);
			}
		});
		img_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DisplayMetrics displayMetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				int width = (displayMetrics.widthPixels);
				if ((pageGallery) < gallery.getCount()) {
					gallery.onFling(null, null, (float) -2.5 * width, 0);
				}
			}
		});
	}

	private void setId() {
		rt_head = (RelativeLayout) getActivity().findViewById(R.id.publisher_rt_head);
		gallery = (Gallery) getActivity().findViewById(R.id.publisher_gallery);
		img_previous = (ImageView) getActivity().findViewById(R.id.publisher_imageView_previous);
		img_next = (ImageView) getActivity().findViewById(R.id.publisher_imageView_next);
		myViewPager = (MyViewPager) getActivity().findViewById(R.id.publisher_myViewPager1);

		linear_dotpage = (LinearLayout) getActivity().findViewById(R.id.publisher_linear_dotpage);
		rt_dummyscreen = (RelativeLayout) getActivity().findViewById(R.id.publisher_rt_dummyscreen);
		linear_customforTabview = (LinearLayout) getActivity().findViewById(R.id.publisher_linear_fortabview);
		linear_refresh = (LinearLayout) getActivity().findViewById(R.id.publisher_linear_head_refresh);
		linear_search = (LinearLayout) getActivity().findViewById(R.id.publisher_linear_head_search);
		linear_setting = (LinearLayout) getActivity().findViewById(R.id.publisher_linear_head_setting);
		image_refresh = (ImageView) getActivity().findViewById(R.id.publisher_image_refresh);
		image_search = (ImageView) getActivity().findViewById(R.id.publisher_image_search);
		image_setting = (ImageView) getActivity().findViewById(R.id.publisher_image_setting);
		txt_refresh = (TextView) getActivity().findViewById(R.id.publisher_txt_refresh);
		txt_search = (TextView) getActivity().findViewById(R.id.publisher_txt_search);
		txt_setting = (TextView) getActivity().findViewById(R.id.publisher_txt_setting);

		btn_type = (Button) getActivity().findViewById(R.id.publisher_btn_type);

		txt_refresh.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_search.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_setting.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		btn_type.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));

		btn_type.setOnClickListener(new onClickBtnAlertView());

		linear_refresh.setOnClickListener(new OnClickControl());
		linear_search.setOnClickListener(new OnClickControl());
		linear_setting.setOnClickListener(new OnClickControl());

		fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
	}

	private class onClickBtnAlertView implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == btn_type.getId()) {
				// type
				view_TypePublisher = new View_TypePublisher(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {
					@Override
					public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_tabbar) {
						// unlock screen and remove
						for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
							if (layout_fortabber.getChildAt(i).equals(layout_tabbar)) {
								layout_fortabber.removeViewAt(i);
								dummyscreen.setVisibility(View.INVISIBLE);
							}
						}
					}

					@Override
					public void TypeName(final Hashtable<Integer, String> hashtable) {
						// set btn text and type ebook
						handler.post(new Runnable() {

							@Override
							public void run() {
								for (Entry<Integer, String> each : hashtable.entrySet()) {
									btn_type.setText(each.getValue());
									setTypePublisherFor_Api(each.getKey());
									bundle.putString(serial_type, each.getValue());

								}
							}
						});

					}

				};
				view_TypePublisher.initView(0, 0, 0);
			}
		}

	}

	private void setTypePublisherFor_Api(int type) {
		switch (type) {
		case 0:
			type_publisher = "U";
			break;
		case 1:
			type_publisher = "R";
			break;
		case 2:
			type_publisher = "N";
			break;

		}
		AppMain.pList_default_publisher = null;
		category_Publisher = new Category_Publisher(myActivity, type_publisher);
		category_Publisher.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {
				setViewPager(resultPlist, pd);

			}

			@Override
			public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}

			@Override
			public void StartLoadPList() {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}
		});
		category_Publisher.LoadCategoryAPI();

	}

	private class OnClickControl implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (AppMain.isphone) {
				if (v.getId() == picon_refresh.getId()) {
					v.startAnimation(fade_in);
					fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
				}
				if (v.getId() == picon_search.getId()) {
					v.startAnimation(fade_in);
					fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
				}
			} else {
				if (v.getId() == linear_refresh.getId()) {
					v.startAnimation(fade_in);
					fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
				}
				if (v.getId() == linear_search.getId()) {
					v.startAnimation(fade_in);
					fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
				}
				if (v.getId() == linear_setting.getId()) {
					v.startAnimation(fade_in);
					fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
				}
			}

		}

	}

	private class onAnimationLitener implements AnimationListener {
		int id = 0;

		public onAnimationLitener(int id) {
			this.id = id;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (AppMain.isphone) {
				if (id == picon_refresh.getId()) {
					refresh_Publisher = new Refresh_Publisher(myActivity);
					refresh_Publisher.setOnListener(new Throw_IntefacePlist() {

						@Override
						public void PList(plist.xml.PList resultPlist, ProgressDialog pd) {
							// ::setListView phone
							setListView(resultPlist, pd);

						}

						@Override
						public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, ProgressDialog pd) {
							// TODO Auto-generated method stub
						}

						@Override
						public void StartLoadPList() {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}
					});
					refresh_Publisher.LoadRefreshAPI();
				}
				if (id == picon_search.getId()) {
					if (Activity_Tab.mLastTab != null) {

						FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();
						// detach lastTab
						if (Activity_Tab.mLastTab.fragment != null) {
							ft.detach(Activity_Tab.mLastTab.fragment);
						}

						// new fragment search
						if (frag_searchpublisher == null) {
							frag_searchpublisher = Fragment.instantiate(myActivity, Fragment_SearchPublisher.class.getName(), bundle);
							ft.add(R.id.realtabcontent, frag_searchpublisher, "searchpublisher");
							HaveFragment = true;
						} else {
							ft.attach(frag_searchpublisher);
							HaveFragment = true;
						}

						ft.commit();
						myActivity.getSupportFragmentManager().executePendingTransactions();
					}

				}
			} else {

				if (id == linear_refresh.getId()) {
					// refresh
					// refresh
					refresh_Publisher = new Refresh_Publisher(myActivity);
					refresh_Publisher.setOnListener(new Throw_IntefacePlist() {

						@Override
						public void PList(plist.xml.PList resultPlist, ProgressDialog pd) {
							setViewPager(resultPlist, pd);

						}

						@Override
						public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, ProgressDialog pd) {
							// TODO Auto-generated method stub
						}

						@Override
						public void StartLoadPList() {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}
					});
					refresh_Publisher.LoadRefreshAPI();
				}
				if (id == linear_search.getId()) {
					// search
					view_SearchPublisher = new View_SearchPublisher(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

						@Override
						public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_search) {
							for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
								if (layout_fortabber.getChildAt(i).equals(layout_search)) {
									layout_fortabber.removeViewAt(i);
									dummyscreen.setVisibility(View.INVISIBLE);
								}
							}
						}

						@Override
						public void onAdapter_SearchList(Adapter_SearchList_Publisher adapter_SearchList) {
							setAdapter_SearchList(adapter_SearchList);
						}
					};

					view_SearchPublisher.setAdapter(getAdapter_SearchList());
					view_SearchPublisher.initView(0, 0, 0);
				}
				if (id == linear_setting.getId()) {
					// setting
					view_Setting = new View_Setting(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

						@Override
						public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_search) {
							for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
								if (layout_fortabber.getChildAt(i).equals(layout_search)) {
									layout_fortabber.removeViewAt(i);
									dummyscreen.setVisibility(View.INVISIBLE);
								}
							}
						}

						@Override
						public void onChangeBackgroundStyle(int drawableStyle) {
							((RelativeLayout) getActivity().findViewById(R.id.publisher_mainlayout)).setBackgroundResource(drawableStyle);
						}

						@Override
						public void onChangeShelfStyle(int drawableStyle) {
							((ImageView) getActivity().findViewById(R.id.publisher_img_shelf1)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.publisher_img_shelf2)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.publisher_img_shelf3)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.publisher_img_shelf4)).setImageResource(drawableStyle);

						}
					};
					view_Setting.initView();
				}

			}
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

	Runnable runnableNext = new Runnable() {

		@Override
		public void run() {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int width = (displayMetrics.widthPixels);

			gallery.onFling(null, null, (float) -2.5 * width, 0);

		}
	};
	Runnable runnablePrevious = new Runnable() {

		@Override
		public void run() {
			gallery.onFling(null, null, (1000 * gallery.getCount()), 0);
		}
	};

	@Override
	public void onPause() {
		Log.e(tag, "onPause");
		System.gc();
		super.onPause();
	}

	@Override
	public void onDestroyView() {

		if (AppMain.isphone) {

		} else {
			handler.removeCallbacks(runnableNext);
			handler.removeCallbacks(runnablePrevious);
		}
		Log.e(tag, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		Log.e(tag, "onResume");
		super.onResume();
	}

	public Adapter_SearchList_Publisher getAdapter_SearchList() {
		return adapter_SearchList;
	}

	public void setAdapter_SearchList(Adapter_SearchList_Publisher adapter_SearchList) {
		this.adapter_SearchList = adapter_SearchList;
	}
}
