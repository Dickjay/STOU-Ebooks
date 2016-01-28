package com.porar.ebooks.stou.fragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.AdapterSpinnerCategory;
import com.porar.ebook.adapter.Adapter_List_EbookTop100;
import com.porar.ebook.adapter.Adapter_SearchList_Ebook;
import com.porar.ebook.adapter.MyAdapterViewPager_Book;
import com.porar.ebook.control.Category_Ebook.Page;
import com.porar.ebook.control.Category_EbookOfFragment;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.Publisher_Detail;
import com.porar.ebook.control.Refresh_Top;
import com.porar.ebook.control.SaveLastUrl;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.control.View_SearchEbook;
import com.porar.ebook.control.View_Setting;
import com.porar.ebook.control.View_TopPeriodEbook;
import com.porar.ebook.control.View_TopTypeEbook;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Ebooks_DetailShort;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.model.Model_Top50;
import com.porar.ebooks.stou.activity.ActivityDetail_Publisher;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Fragment_Top extends Fragment {

	private final Handler handler = new Handler();
	private MyViewPager myViewPager;
	public ProgressDialog progressDialog = null;
	// control
	private TextView txt_refresh, txt_search, txt_setting;
	private ImageView image_refresh, image_search, image_setting;
	private LinearLayout linear_refresh, linear_search, linear_setting, linear_customforTabview, linear_dotpage;
	private RelativeLayout rt_dummyscreen, rt_head;
	// btn
	private Button btn_category, btn_type, btn_period;
	private Spinner spinner_category;// spinner_type;
	// animation
	private Animation fade_in;
	// Class
	private MyAdapterViewPager_Book adapterViewPager_Book;
	private Refresh_Top refresh_Top;
	private Category_EbookOfFragment category_Ebook;
	private ArrayList<Model_Categories> arr_Categories;
	public static SaveLastUrl lastUrl = null;
	public String type_ebook = "";
	public String catid = "";
	public String period = "";
	private Dot_Pageslide dot_Pageslide;
	private View_SearchEbook view_SearchEbook;
	private AlertDialog alertDialog;
	private Adapter_SearchList_Ebook adapter_SearchList;
	public String tag = "Fragment_Top";
	public FragmentActivity myActivity;
	public Bundle bundle;
	public static String serial_category = "serial_category";
	public static String serial_type = "serial_type";
	public static String serial_period = "serial_period";
	public View_TopTypeEbook view_TopTypeEbook;
	public View_TopPeriodEbook view_TopPeriodEbook;
	// top 3
	private TextView txt_name1, txt_name2, txt_name3;
	private TextView txt_pagesize1, txt_pagesize2, txt_pagesize3;
	private TextView txt_writter1, txt_writter2, txt_writter3;
	private TextView txt_publisher1, txt_publisher2, txt_publisher3;
	private RatingBar ratingBar1, ratingBar2, ratingBar3;
	private ImageView img_publisher1, img_publisher2, img_publisher3;
	private ImageView img_shadow1, img_shadow2, img_shadow3;
	private ImageView img_cover1, img_cover2, img_cover3;
	private Model_Ebooks_DetailShort detailShort1;
	private Model_Ebooks_DetailShort detailShort2;
	private Model_Ebooks_DetailShort detailShort3;
	private ImageDownloader_forCache downloader_forCache;
	private AsyncTask_FetchAPI asyncTask_FetchAPI;
	View_Setting view_Setting;

	// public boolean isphone = false;
	// :: version.phone :: Object
	ListView plistview;
	ImageView picon_refresh;
	Button pbtn_day, pbtn_week, pbtn_mounth;
	Adapter_List_EbookTop100 adapter_List_EbookTop100;
	public static String bundle_Top_Type_phone = "bundle_Top_Type_phone";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(tag, "onCreate");

		if (AppMain.isphone) {
			Log.e(tag, "onCreate isphone");
			bundle = new Bundle();
			myActivity = getActivity();
			lastUrl = new SaveLastUrl();
			type_ebook = "D"; // D = top download, B = bestseller // default
			period = "D"; // D = day, W = week, M =month // default;
			catid = "0";  // category id // default
		} else {
			bundle = new Bundle();
			myActivity = getActivity();
			lastUrl = new SaveLastUrl();
			downloader_forCache = new ImageDownloader_forCache();
			type_ebook = "D"; // D = top download, B = bestseller // default
			period = "D"; // D = day, W = week, M =month // default;
			catid = "0";  // category id // default
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(tag, "onCreateView");
		View view = inflater.inflate(R.layout.activity_top, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(tag, "onActivityCreated");
		// RelativeLayout layout_main = (RelativeLayout) getActivity().findViewById(R.id.top_rt_main_phone);
		if (AppMain.isphone) {

			setId_Phone();
			if (bundle.getInt(bundle_Top_Type_phone) != 0) {
				setDefaultEnableType(bundle.getInt(bundle_Top_Type_phone));
			}
			setListView(AppMain.pList_default_top, null);

		} else {
			setId();

			try {
				Model_Setting model_setting = Class_Manage.getModel_Setting(myActivity);
				if (model_setting != null) {
					((RelativeLayout) getActivity().findViewById(R.id.top_mainlayout)).setBackgroundResource(model_setting.getDrawable_backgroundStyle());
				}
				Model_SettingShelf settingShelf = Class_Manage.getModel_SettingShelf(myActivity);
				if (settingShelf != null) {
					((ImageView) getActivity().findViewById(R.id.top_img_shelf1)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.top_img_shelf2)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.top_img_shelf3)).setImageResource(settingShelf.getDrawable_ShelfStyle());
					((ImageView) getActivity().findViewById(R.id.top_img_shelf4)).setImageResource(settingShelf.getDrawable_ShelfStyle());
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
			}

			setSpinnerAdapter();
			setTop3(AppMain.pList_default_top50);
			setViewPager(AppMain.pList_default_top, null);

			// get InstanceState
			if (bundle.getString(serial_category) != null) {
				btn_category.setText(bundle.getString(serial_category));
			}
			if (bundle.getString(serial_type) != null) {
				btn_type.setText(bundle.getString(serial_type));
			}
			if (bundle.getString(serial_period) != null) {
				btn_period.setText(bundle.getString(serial_period));
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

			adapter_List_EbookTop100 = null;
			adapter_List_EbookTop100 = new Adapter_List_EbookTop100(myActivity, 0, plist);

			plistview.setAdapter(adapter_List_EbookTop100);

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
		plistview = (ListView) getActivity().findViewById(R.id.top_plistview);
		picon_refresh = (ImageView) getActivity().findViewById(R.id.top_image_prefresh);
		pbtn_day = (Button) getActivity().findViewById(R.id.tabview_btn_top_p_day);
		pbtn_week = (Button) getActivity().findViewById(R.id.tabview_btn_top_p_week);
		pbtn_mounth = (Button) getActivity().findViewById(R.id.tabview_btn_top_p_mounth);
		fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
		picon_refresh.setOnClickListener(new OnClickControl());

		pbtn_day.setEnabled(false);
		pbtn_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_Top_Type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("D");
			}
		});
		pbtn_week.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_Top_Type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("W");
			}
		});
		pbtn_mounth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bundle.putInt(bundle_Top_Type_phone, v.getId());
				setDefaultEnableType(v.getId());
				loadApiByType("M");
			}

		});
	}

	private void loadApiByType(String period) {

		AppMain.pList_default_top = null;
		category_Ebook = new Category_EbookOfFragment(myActivity, catid, period, type_ebook, Page.TOP_FRAGMENT);
		category_Ebook.LoadTop50period(false);
		category_Ebook.setOnListener(new Throw_IntefacePlist() {

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
		category_Ebook.LoadCategoryAPI();

	}

	private void setDefaultEnableType(int id) {

		pbtn_day.setEnabled(true);
		pbtn_week.setEnabled(true);
		pbtn_mounth.setEnabled(true);

		if (id == pbtn_day.getId()) {
			pbtn_day.setEnabled(false);
		}
		if (id == pbtn_week.getId()) {
			pbtn_week.setEnabled(false);
		}
		if (id == pbtn_mounth.getId()) {
			pbtn_mounth.setEnabled(false);
		}

	}

	private void setTop3(PList pList) {
		String[] arrTop3BID = new String[3];
		String[] arrTop3Cover = new String[3];
		ImageView[] arrImage = { img_cover1, img_cover2, img_cover3 };

		try {
			int i = 0;
			for (PListObject each : (Array) pList.getRootElement()) {
				Model_Top50 model_Top50 = new Model_Top50(each);

				arrTop3BID[i] = String.valueOf(model_Top50.getBID());
				arrTop3Cover[i] = String.valueOf(model_Top50.getCover());

				OnClickToDetail clickToDetail = new OnClickToDetail();
				clickToDetail.newInstant(model_Top50.getBID());
				arrImage[i].setOnClickListener(clickToDetail);
				i++;

				if (i == 3) {
					break;
				}
			}
			downloader_forCache.download(AppMain.COVER_URL + arrTop3BID[0] + "/" + arrTop3Cover[0],
					img_cover1, img_shadow1);
			downloader_forCache.download(AppMain.COVER_URL + arrTop3BID[1] + "/" + arrTop3Cover[1],
					img_cover2, img_shadow2);
			downloader_forCache.download(AppMain.COVER_URL + arrTop3BID[2] + "/" + arrTop3Cover[2],
					img_cover3, img_shadow3);

			asyncTask_FetchAPI = new AsyncTask_FetchAPI();
			asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

				@Override
				public void onTimeOut(String apiURL, int currentIndex) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFetchStart(String apiURL, int currentIndex) {
					Log.e("onFetchStart", "" + apiURL);
				}

				@Override
				public void onFetchError(String apiURL, int currentIndex, Exception e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFetchComplete(String apiURL, final int currentIndex, final PList result) {
					Log.w("onFetchComplete", "" + apiURL);
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (currentIndex == 0) {
								for (PListObject each : (Array) result.getRootElement()) {
									detailShort1 = new Model_Ebooks_DetailShort(each);
								}
							}
							if (currentIndex == 1) {
								for (PListObject each : (Array) result.getRootElement()) {
									detailShort2 = new Model_Ebooks_DetailShort(each);
								}
							}
							if (currentIndex == 2) {
								for (PListObject each : (Array) result.getRootElement()) {
									detailShort3 = new Model_Ebooks_DetailShort(each);
								}
							}
						}
					});
				}

				@Override
				public void onAllTaskDone() {
					try {
						if (detailShort1 != null) {
							txt_name1.setText("" + detailShort1.getName());
							txt_pagesize1.setText("" + detailShort1.getPages() + " Pages" + detailShort1.getFileSize() + " Mb");
							txt_publisher1.setText("" + detailShort1.getPublisherName());
							txt_writter1.setText("" + detailShort1.getWriters());
							ratingBar1.setRating(detailShort1.getRating());
							downloader_forCache.download("http://www.ebooks.in.th/publishers/" + detailShort1.getCID() + "/" + detailShort1.getLogo(),
									img_publisher1);

							OnClickToDetailPublisher clickToDetailPublisher = new OnClickToDetailPublisher();
							clickToDetailPublisher.newInstant(detailShort1.getCID());
							((RelativeLayout) getActivity().findViewById(R.id.top_rt_publisher_top1)).setOnClickListener(clickToDetailPublisher);
						}
						if (detailShort2 != null) {
							txt_name2.setText("" + detailShort2.getName());
							txt_pagesize2.setText("" + detailShort2.getPages() + " Pages" + detailShort2.getFileSize() + " Mb");
							txt_publisher2.setText("" + detailShort2.getPublisherName());
							txt_writter2.setText("" + detailShort2.getWriters());
							ratingBar2.setRating(detailShort2.getRating());
							downloader_forCache.download("http://www.ebooks.in.th/publishers/" + detailShort2.getCID() + "/" + detailShort2.getLogo(),
									img_publisher2);
							OnClickToDetailPublisher clickToDetailPublisher = new OnClickToDetailPublisher();
							clickToDetailPublisher.newInstant(detailShort2.getCID());
							((RelativeLayout) getActivity().findViewById(R.id.top_rt_publisher_top2)).setOnClickListener(clickToDetailPublisher);
						}
						if (detailShort3 != null) {
							txt_name3.setText("" + detailShort3.getName());
							txt_pagesize3.setText("" + detailShort3.getPages() + " Pages" + detailShort3.getFileSize() + " Mb");
							txt_publisher3.setText("" + detailShort3.getPublisherName());
							txt_writter3.setText("" + detailShort3.getWriters());
							ratingBar3.setRating(detailShort3.getRating());
							downloader_forCache.download("http://www.ebooks.in.th/publishers/" + detailShort3.getCID() + "/" + detailShort3.getLogo(),
									img_publisher3);

							OnClickToDetailPublisher clickToDetailPublisher = new OnClickToDetailPublisher();
							clickToDetailPublisher.newInstant(detailShort3.getCID());
							((RelativeLayout) getActivity().findViewById(R.id.top_rt_publisher_top3)).setOnClickListener(clickToDetailPublisher);
						}
					} catch (NullPointerException e) {
						if (asyncTask_FetchAPI != null) {
							asyncTask_FetchAPI.cancel(true);

							Log.e("NullPointerException", "asyncTask_FetchAPI Cancel=true");
						}

					}

				}
			});
			asyncTask_FetchAPI.execute(
					AppMain.getAPIbyRefKey("ebooks-detail-short", "bid=" + arrTop3BID[0]),
					AppMain.getAPIbyRefKey("ebooks-detail-short", "bid=" + arrTop3BID[1]),
					AppMain.getAPIbyRefKey("ebooks-detail-short", "bid=" + arrTop3BID[2]));

		} catch (NullPointerException e) {
			// top50 null
			// refresh
		}

	}

	public class OnClickToDetailPublisher implements OnClickListener {
		int cid;
		Model_Publisher_Detail model_Publisher_Detail;
		Publisher_Detail publisher_Detail;
		AlertDialog alertDialog;

		public void newInstant(int object) {
			cid = object;
		}

		@Override
		public void onClick(View v) {

			publisher_Detail = new Publisher_Detail(myActivity, String.valueOf(cid));
			publisher_Detail.setOnListener(new Throw_IntefacePlist() {

				@Override
				public void PList_Detail_Comment(plist.xml.PList plist_Publisherdetail, plist.xml.PList plist_PublisherEbook, final ProgressDialog pd) {

					try {
						for (PListObject each : (Array) plist_Publisherdetail.getRootElement()) {
							model_Publisher_Detail = new Model_Publisher_Detail(each);
						}

						for (PListObject each : (Array) plist_PublisherEbook.getRootElement()) {
							AppMain.pList_default_publisher_ebook = plist_PublisherEbook;
						}

						Intent intent = new Intent(myActivity, ActivityDetail_Publisher.class);
						intent.putExtra("model", model_Publisher_Detail);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						myActivity.startActivity(intent);
						myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
						alertDialog.setMessage("WARNING: An error has ocurred. Please to try again ?");
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								pd.show();
								dialog.dismiss();
								System.gc();
								publisher_Detail.LoadEbooksDetailAPI();
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

				@Override
				public void PList(plist.xml.PList resultPlist, ProgressDialog pd) {
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
			publisher_Detail.LoadEbooksDetailAPI();

		}

	}

	public class OnClickToDetail implements OnClickListener {
		int Bid;
		Ebook_Detail ebook_DetailApi;
		AlertDialog alertDialog;
		Model_Ebooks_Detail ebooks_Detail = null;

		public void newInstant(int object) {
			Bid = object;

		}

		@Override
		public void onClick(View v) {
			// DerializeObject
			Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(myActivity, Bid);
			if (DSebooks_Detail != null) {
				Log.v("OnClickToDetailEbook", "DeserializeObject Success" + Bid);
				Intent intent = new Intent(myActivity, Activity_Detail.class);
				intent.putExtra("model", DSebooks_Detail);
				intent.putExtra("DeserializeObject", "1");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myActivity.startActivity(intent);
				myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

			} else {
				ebook_DetailApi = new Ebook_Detail(myActivity, String.valueOf(Bid));
				ebook_DetailApi.setOnListener(new Throw_IntefacePlist() {

					@Override
					public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, final ProgressDialog pd) {

					}

					@Override
					public void PList(plist.xml.PList resultPlist, final ProgressDialog pd) {

						try {
							for (PListObject each : (Array) resultPlist.getRootElement()) {
								ebooks_Detail = new Model_Ebooks_Detail(each);
							}

							// SerializeObject
							ebooks_Detail.setDateTime();
							if (Class_Manage.SaveModel_Detail(myActivity, ebooks_Detail, ebooks_Detail.getBID())) {
								Log.v("OnClickToDetailEbook", "SerializeObject Success" + ebooks_Detail.getBID());
							}

							Intent intent = new Intent(myActivity, Activity_Detail.class);
							intent.putExtra("model", ebooks_Detail);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							myActivity.startActivity(intent);
							myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
							alertDialog.setMessage("WARNING: An error has ocurred. Please to try again ?");
							alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd.show();
									dialog.dismiss();
									System.gc();
									ebook_DetailApi.LoadEbooksDetailAPI();
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

					@Override
					public void StartLoadPList() {
						// TODO Auto-generated method stub

					}

					@Override
					public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
						// TODO Auto-generated method stub

					}
				});
				ebook_DetailApi.LoadEbooksDetailAPI();
			}

		}

	}

	private void setSpinnerAdapter() {
		try {
			arr_Categories = new ArrayList<Model_Categories>();
			Model_Categories model_allbook = new Model_Categories();
			model_allbook.setCatID(0);
			model_allbook.setSCatID(0);
			model_allbook.setName(getString(R.string.allbook));
			model_allbook.setDetail(getString(R.string.allbook_detail));
			model_allbook.setPictureURL("default");

			arr_Categories.add(model_allbook);
			for (PListObject each : (Array) AppMain.pList_categories.getRootElement()) {
				arr_Categories.add(new Model_Categories(each));
			}
			spinner_category.setAdapter(new AdapterSpinnerCategory(myActivity, arr_Categories));

		} catch (NullPointerException e) {
			// TODO: handle exception
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
			adapterViewPager_Book = null;
			adapterViewPager_Book = new MyAdapterViewPager_Book(myActivity, pList);

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					removeViewDotPage();

					dot_Pageslide = new Dot_Pageslide(myActivity);
					linear_dotpage.addView(dot_Pageslide.setImage_slide(adapterViewPager_Book.getCount()));
					myViewPager.setAdapter(adapterViewPager_Book);
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
							if (adapterViewPager_Book != null) {
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
			TextView msg = new TextView(myActivity);
			msg.setText("Please Wait... Loading");
			msg.setPadding(10, 10, 10, 10);
			msg.setGravity(Gravity.CENTER);
			alertDialog.setView(msg);
			alertDialog.show();

			//

		} catch (NullPointerException e) {
			// load again
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	}

	private void removeViewDotPage() {
		if (linear_dotpage.getChildCount() > 0) {
			linear_dotpage.removeAllViews();
		}
	}

	private void setId() {
		ratingBar1 = (RatingBar) getActivity().findViewById(R.id.top_ratingbar_top1);
		ratingBar2 = (RatingBar) getActivity().findViewById(R.id.top_ratingbar_top2);
		ratingBar3 = (RatingBar) getActivity().findViewById(R.id.top_ratingbar_top3);
		img_shadow1 = (ImageView) getActivity().findViewById(R.id.top_image_shadow_top1);
		img_shadow2 = (ImageView) getActivity().findViewById(R.id.top_image_shadow_top2);
		img_shadow3 = (ImageView) getActivity().findViewById(R.id.top_image_shadow_top3);
		img_cover1 = (ImageView) getActivity().findViewById(R.id.top_image_cover_top1);
		img_cover2 = (ImageView) getActivity().findViewById(R.id.top_image_cover_top2);
		img_cover3 = (ImageView) getActivity().findViewById(R.id.top_image_cover_top3);
		img_publisher1 = (ImageView) getActivity().findViewById(R.id.top_img_publisher_top1);
		img_publisher2 = (ImageView) getActivity().findViewById(R.id.top_img_publisher_top2);
		img_publisher3 = (ImageView) getActivity().findViewById(R.id.top_img_publisher_top3);

		txt_name1 = (TextView) getActivity().findViewById(R.id.top_textview_name_top1);
		txt_name2 = (TextView) getActivity().findViewById(R.id.top_textview_name_top2);
		txt_name3 = (TextView) getActivity().findViewById(R.id.top_textview_name_top3);
		txt_pagesize1 = (TextView) getActivity().findViewById(R.id.top_textview_pagesize_top1);
		txt_pagesize2 = (TextView) getActivity().findViewById(R.id.top_textview_pagesize_top2);
		txt_pagesize3 = (TextView) getActivity().findViewById(R.id.top_textview_pagesize_top3);
		txt_writter1 = (TextView) getActivity().findViewById(R.id.top_textview_writter_top1);
		txt_writter2 = (TextView) getActivity().findViewById(R.id.top_textview_writter_top2);
		txt_writter3 = (TextView) getActivity().findViewById(R.id.top_textview_writter_top3);
		txt_publisher1 = (TextView) getActivity().findViewById(R.id.top_textview_publosher_top1);
		txt_publisher2 = (TextView) getActivity().findViewById(R.id.top_textview_publosher_top2);
		txt_publisher3 = (TextView) getActivity().findViewById(R.id.top_textview_publosher_top3);

		txt_name1.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_name2.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_name3.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_pagesize1.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_pagesize2.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_pagesize3.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_writter1.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_writter2.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_writter3.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_publisher1.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_publisher2.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_publisher3.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));

		rt_head = (RelativeLayout) getActivity().findViewById(R.id.top_rt_head);
		myViewPager = (MyViewPager) getActivity().findViewById(R.id.top_myViewPager1);
		linear_dotpage = (LinearLayout) getActivity().findViewById(R.id.top_linear_dotpage);
		rt_dummyscreen = (RelativeLayout) getActivity().findViewById(R.id.top_rt_dummyscreen);
		linear_customforTabview = (LinearLayout) getActivity().findViewById(R.id.top_linear_fortabview);
		linear_refresh = (LinearLayout) getActivity().findViewById(R.id.top_linear_head_refresh);
		linear_search = (LinearLayout) getActivity().findViewById(R.id.top_linear_head_search);
		linear_setting = (LinearLayout) getActivity().findViewById(R.id.top_linear_head_setting);
		image_refresh = (ImageView) getActivity().findViewById(R.id.top_image_refresh);
		image_search = (ImageView) getActivity().findViewById(R.id.top_image_search);
		image_setting = (ImageView) getActivity().findViewById(R.id.top_image_setting);
		txt_refresh = (TextView) getActivity().findViewById(R.id.top_txt_refresh);
		txt_search = (TextView) getActivity().findViewById(R.id.top_txt_search);
		txt_setting = (TextView) getActivity().findViewById(R.id.top_txt_setting);

		btn_category = (Button) getActivity().findViewById(R.id.top_btn_category);
		btn_type = (Button) getActivity().findViewById(R.id.top_btn_type);
		btn_period = (Button) getActivity().findViewById(R.id.top_btn_period);
		spinner_category = (Spinner) getActivity().findViewById(R.id.top_spinner_category);

		txt_refresh.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_search.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		txt_setting.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		btn_category.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		btn_type.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
		btn_period.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));

		linear_refresh.setOnClickListener(new OnClickControl());
		linear_search.setOnClickListener(new OnClickControl());
		linear_setting.setOnClickListener(new OnClickControl());

		btn_category.setOnTouchListener(new onTouchBtnAlertView());
		btn_type.setOnClickListener(new onClickBtnAlertView());
		btn_period.setOnClickListener(new onClickBtnAlertView());

		fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
	}

	private class onClickBtnAlertView implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == btn_type.getId()) {
				// type
				view_TopTypeEbook = new View_TopTypeEbook(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

					@Override
					public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_tabbar) {
						// unlock screen and remove
						for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
							if (layout_fortabber.getChildAt(i).equals(layout_tabbar)) {
								layout_fortabber.removeViewAt(i);
								// layout_fortabber.removeAllViews();
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
									setTypeEbookFor_Api(each.getKey());
									bundle.putString(serial_type, each.getValue());

								}
							}
						});
					}
				};
				int[] location = new int[2];
				v.getLocationOnScreen(location);  // 0=x 1=y
				DisplayMetrics displayMetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				int width = (displayMetrics.widthPixels);
				int distanceRight = width - location[0] - v.getWidth();
				Log.e("", "" + distanceRight);
				view_TopTypeEbook.initView(0, distanceRight, 0, 0);

			}
			if (v.getId() == btn_period.getId()) {
				// period
				view_TopPeriodEbook = new View_TopPeriodEbook(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

					@Override
					public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_tabbar) {
						// unlock screen and remove
						for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
							if (layout_fortabber.getChildAt(i).equals(layout_tabbar)) {
								layout_fortabber.removeViewAt(i);
								// layout_fortabber.removeAllViews();
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
									btn_period.setText(each.getValue());
									setTypeEbookForPeriod_Api(each.getKey());
									bundle.putString(serial_period, each.getValue());

								}
							}
						});
					}
				};
				int[] location = new int[2];
				v.getLocationOnScreen(location);  // 0=x 1=y
				DisplayMetrics displayMetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
				int width = (displayMetrics.widthPixels);
				int distanceRight = width - location[0] - v.getWidth();
				Log.e("", "" + distanceRight);
				view_TopPeriodEbook.initView(0, distanceRight, 0, 0);
			}

		}

	}

	private void setTypeEbookForPeriod_Api(int type) {
		switch (type) {
		case 0:
			period = "D";
			break;
		case 1:
			period = "W";
			break;
		case 2:
			period = "M";
			break;
		}
		AppMain.pList_default_top = null;
		category_Ebook = new Category_EbookOfFragment(myActivity, catid, period, type_ebook, Page.TOP_FRAGMENT);
		category_Ebook.LoadTop50period(true);
		category_Ebook.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {

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
				setViewPager(Plist1, pd);
				if (Plist2 != null) {
					setTop3(Plist2);
					Log.e("", "not null");
				}

			}
		});
		category_Ebook.LoadCategoryAPI();

	}

	private void setTypeEbookFor_Api(int type) {
		switch (type) {
		case 0:
			type_ebook = "D";
			break;
		case 1:
			type_ebook = "B";
			break;

		}
		AppMain.pList_default_top = null;
		category_Ebook = new Category_EbookOfFragment(myActivity, catid, period, type_ebook, Page.TOP_FRAGMENT);
		category_Ebook.LoadTop50period(false);
		category_Ebook.setOnListener(new Throw_IntefacePlist() {

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
		category_Ebook.LoadCategoryAPI();

	}

	private class OnClickControl implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (AppMain.isphone) {
				if (v.getId() == picon_refresh.getId()) {
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

	private class onTouchBtnAlertView implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (v.getId() == btn_category.getId()) {
					// category
					spinner_category.setOnItemSelectedListener(new OnSelectSpinner());
				}
				return true;
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (v.getId() == btn_category.getId()) {
					// category
					spinner_category.performClick();
				}

				return true;
			}
			return false;

		}

	}

	private interface EventToText {
		public void setTextCategory();
	}

	private class OnSelectSpinner implements EventToText, OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (parent.getId() == spinner_category.getId()) {
				catid = String.valueOf(arr_Categories.get(position).getCatID());
				setTextCategory();
				AppMain.pList_default_top = null;
				category_Ebook = new Category_EbookOfFragment(myActivity, catid, period, type_ebook, Page.TOP_FRAGMENT);
				category_Ebook.LoadTop50period(false);
				category_Ebook.setOnListener(new Throw_IntefacePlist() {

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

					}
				});
				category_Ebook.LoadCategoryAPI();

			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setTextCategory() {
			for (Model_Categories each : arr_Categories) {
				if (String.valueOf(each.getCatID()) == catid) {
					btn_category.setText(each.getName());

					bundle.putString(serial_category, each.getName());
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
					refresh_Top = new Refresh_Top(myActivity);
					refresh_Top.setOnListener(new Throw_IntefacePlist() {

						@Override
						public void StartLoadPList() {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_TopPeriod(PList Plist1, PList Plist2, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_Detail_Comment(PList Plistdetail, PList Plistcomment, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList(PList resultPlist, ProgressDialog pd) {
							setListView(resultPlist, pd);

						}
					});
					refresh_Top.LoadRefreshAPI();
				}
			} else {

				if (id == linear_refresh.getId()) {
					// refresh
					refresh_Top = new Refresh_Top(myActivity);
					refresh_Top.setOnListener(new Throw_IntefacePlist() {

						@Override
						public void StartLoadPList() {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_TopPeriod(PList Plist1, PList Plist2, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList_Detail_Comment(PList Plistdetail, PList Plistcomment, ProgressDialog pd) {
							// TODO Auto-generated method stub

						}

						@Override
						public void PList(PList resultPlist, ProgressDialog pd) {
							setViewPager(resultPlist, pd);

						}
					});
					refresh_Top.LoadRefreshAPI();

				}
				if (id == linear_search.getId()) {
					// search

					view_SearchEbook = new View_SearchEbook(myActivity, linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

						@Override
						public void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_search) {
							// unlock screen and remove
							for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
								if (layout_fortabber.getChildAt(i).equals(layout_search)) {
									layout_fortabber.removeViewAt(i);
									dummyscreen.setVisibility(View.INVISIBLE);
								}
							}
						}

						@Override
						public void onAdapter_SearchList(Adapter_SearchList_Ebook adapter_SearchList) {
							setAdapter_SearchList(adapter_SearchList);
						}
					};
					view_SearchEbook.setAdapter(getAdapter_SearchList());
					view_SearchEbook.initView(0, 0, 0);

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
							((RelativeLayout) getActivity().findViewById(R.id.top_mainlayout)).setBackgroundResource(drawableStyle);
						}

						@Override
						public void onChangeShelfStyle(int drawableStyle) {
							((ImageView) getActivity().findViewById(R.id.top_img_shelf1)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.top_img_shelf2)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.top_img_shelf3)).setImageResource(drawableStyle);
							((ImageView) getActivity().findViewById(R.id.top_img_shelf4)).setImageResource(drawableStyle);

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

	@Override
	public void onDestroyView() {
		Log.e(tag, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onResume() {

		Log.e(tag, "onResume");
		super.onResume();
	}

	/**
	 * @return the adapter_SearchList
	 */
	public Adapter_SearchList_Ebook getAdapter_SearchList() {
		return adapter_SearchList;
	}

	/**
	 * @param adapter_SearchList
	 *            the adapter_SearchList to set
	 */
	public void setAdapter_SearchList(Adapter_SearchList_Ebook adapter_SearchList) {
		this.adapter_SearchList = adapter_SearchList;
	}

}
