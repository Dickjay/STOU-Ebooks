package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.fragment.Fragment_Ebook;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

public class MyAdapterViewPager_Book extends PagerAdapter implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Activity context;
	private final int limit = 16;
	private int index = 0;
	private final ArrayList<ArrayList<Model_EbookList>> arrList;
	private ArrayList<Model_EbookList> arrinside;
	private final PList pList;
	int sizePage = 0;

	int view_index = 0;

	// 24-04-13
	LayoutInflater inflater;
	Handler handler = new Handler();
	ImageDownloader_forCache downloader_forCache;
	LruCache<String, byte[]> mMemoryCache;

	public MyAdapterViewPager_Book(Activity context, PList pList) {
		Fragment_Ebook.isChoice = false;
		Fragment_Ebook.isSelectedMaster = false;
		Fragment_Ebook.isTabletChoose = false;
		Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
		StaticUtils.sCat = "";
		StaticUtils.bCode = "";
		Fragment_Ebook.bCode = "";
		Fragment_Ebook.sCatIDs = "";

		this.context = context;
		this.pList = pList;
		downloader_forCache = new ImageDownloader_forCache();
		arrList = new ArrayList<ArrayList<Model_EbookList>>();

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);

		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

		Array arr_check = (Array) this.pList.getRootElement();
		if ((arr_check.size() <= 0)) {

		} else {
			for (PListObject each : (Array) this.pList.getRootElement()) {
				if (index == 0) {
					arrinside = new ArrayList<Model_EbookList>();
				}
				arrinside.add(new Model_EbookList(each));
				index++;
				if (index >= limit) {
					index = 0;
					arrList.add(arrinside);

				}
			}
			if (arrinside.size() != 0 && arrinside.size() < limit) {
				arrList.add(arrinside);
			}

			sizePage = arrList.size();

		}

	}

	@Override
	public void destroyItem(final View view, final int position,
			final Object object) {
		((ViewPager) view).removeView((LinearLayout) object);

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (sizePage != 0) {
			return sizePage;
		} else {
			return 1;
		}
	}

	@Override
	public Object instantiateItem(final View view, final int position) {
		Log.e("", "instantiateItem " + position);
		view_index = position;

		if (sizePage == 0) {
			myView = LayoutInflater.from(context).inflate(
					R.layout.search_item_ebook_noresult, null);
			TextView txtName = (TextView) myView
					.findViewById(R.id.txt_noresult);
			txtName.setTypeface(StaticUtils.getTypeface(context,
					Font.DB_Helvethaica_X_Med));
			((ViewPager) view).addView(myView);

		} else {
			// table view

			myView = LayoutInflater.from(context).inflate(
					R.layout.layout_tableview_ebook, null);
			((ViewPager) view).addView(myView);

			for (int j = 0; j < arrList.get(view_index).size(); j++) {

				img_covers[j] = (ImageView) myView.findViewById(id_cover[j]);
				img_shadow[j] = (ImageView) myView.findViewById(id_shadow[j]);
				rt_view[j] = (RelativeLayout) myView.findViewById(id_rt[j]);

				Picasso.with(context)
						.load(AppMain.COVER_URL
								+ arrList.get(view_index).get(j).getBID() + "/"
								+ arrList.get(view_index).get(j).getCover())
						.into(img_covers[j]);
				img_shadow[j].setVisibility(View.VISIBLE);

				if (arrList.get(view_index).get(j).getPrice().contains("$")) {
					// show
					TextView tv_price = new TextView(context);
					tv_price.setBackgroundColor(Color.RED);
					try {
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
							tv_price.setAlpha(0.7F);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					tv_price.setPadding(2, 2, 2, 2);
					tv_price.setTextColor(Color.WHITE);
					tv_price.setTextSize(Float.parseFloat(context
							.getResources().getString(R.string.size_pointer)));
					tv_price.setTypeface(StaticUtils.getTypeface(context,
							Font.LayijiMahaniyomV105ot));
					String newPriceTHB = arrList.get(view_index).get(j)
							.getPriceTHB();
					try {
						newPriceTHB = newPriceTHB.substring(0,
								newPriceTHB.lastIndexOf("."));
					} catch (Exception e) {
						// TODO: handle exception
					}

					tv_price.setText("ß" + newPriceTHB);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_RIGHT,
							img_covers[j].getId());
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					rt_view[j].addView(tv_price, params);
				}

				OnClickToDetail toDetail = new OnClickToDetail();
				toDetail.newInstant(arrList.get(view_index).get(j));
				img_covers[j].setOnClickListener(toDetail);
			}

			if (arrList.get(view_index).size() < limit) {
				int over = limit - arrList.get(view_index).size();
				Log.e("over", "" + over);
				for (int i = limit - 1; i > arrList.get(view_index).size() - 1; i--) {
					img_covers[i] = (ImageView) myView
							.findViewById(id_cover[i]);
					img_shadow[i] = (ImageView) myView
							.findViewById(id_shadow[i]);
					img_covers[i].setVisibility(View.INVISIBLE);
					img_shadow[i].setVisibility(View.INVISIBLE);
					rt_view[i] = (RelativeLayout) myView.findViewById(id_rt[i]);
					rt_view[i].setVisibility(View.INVISIBLE);
				}
			}

		}

		return myView;

	}

	private class OnClickToDetail implements OnClickListener {
		Model_EbookList model_EbookList;
		Ebook_Detail ebook_DetailApi;
		AlertDialog alertDialog;
		Model_Ebooks_Detail ebooks_Detail = null;

		public void newInstant(Model_EbookList object) {
			model_EbookList = object;

		}

		@Override
		public void onClick(View v) {

			Log.i("OnClickToDetail", "ebookBID " + model_EbookList.getBID());
			Log.i("OnClickToDetail", "ebookName " + model_EbookList.getName());

			// DerializeObject
			Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(
					context, model_EbookList.getBID());
			if (DSebooks_Detail != null) {
				Log.v("OnClickToDetailEbook", "DeserializeObject Success"
						+ model_EbookList.getBID());
				Intent intent = new Intent(context, Activity_Detail.class);
				intent.putExtra("model", DSebooks_Detail);
				intent.putExtra("DeserializeObject", "1");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			} else {
				ebook_DetailApi = new Ebook_Detail(context,
						String.valueOf(model_EbookList.getBID()));
				ebook_DetailApi.setOnListener(new Throw_IntefacePlist() {

					@Override
					public void PList_Detail_Comment(
							plist.xml.PList Plistdetail,
							plist.xml.PList Plistcomment,
							final ProgressDialog pd) {

					}

					@Override
					public void PList(plist.xml.PList resultPlist,
							final ProgressDialog pd) {

						try {
							for (PListObject each : (Array) resultPlist
									.getRootElement()) {
								ebooks_Detail = new Model_Ebooks_Detail(each);
							}

							// SerializeObject
							ebooks_Detail.setDateTime();
							if (Class_Manage.SaveModel_Detail(context,
									ebooks_Detail, ebooks_Detail.getBID())) {
								Log.v("OnClickToDetailEbook",
										"SerializeObject Success"
												+ ebooks_Detail.getBID());
							}

							Intent intent = new Intent(context,
									Activity_Detail.class);
							intent.putExtra("model", ebooks_Detail);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							context.overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);

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

							alertDialog = new AlertDialog.Builder(context)
									.create();
							alertDialog.setTitle(AppMain.getTag());
							alertDialog
									.setMessage("WARNING: An error has occurred. Please to try again ?");
							alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
									"Retry",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											pd.show();
											System.out.println("Retry");
											dialog.dismiss();
											System.gc();
											ebook_DetailApi
													.LoadEbooksDetailAPI();
										}
									});
							alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
									"Cancel",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
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
					public void PList_TopPeriod(plist.xml.PList Plist1,
							plist.xml.PList Plist2, ProgressDialog pd) {
						// TODO Auto-generated method stub

					}
				});
				ebook_DetailApi.LoadEbooksDetailAPI();
			}

		}

	}

	static View myView = null;
	static int id_cover[] = { R.id.img_ebook_cover1_tableRow1,
			R.id.img_ebook_cover2_tableRow1, R.id.img_ebook_cover3_tableRow1,
			R.id.img_ebook_cover4_tableRow1, R.id.img_ebook_cover1_tableRow2,
			R.id.img_ebook_cover2_tableRow2, R.id.img_ebook_cover3_tableRow2,
			R.id.img_ebook_cover4_tableRow2, R.id.img_ebook_cover1_tableRow3,
			R.id.img_ebook_cover2_tableRow3, R.id.img_ebook_cover3_tableRow3,
			R.id.img_ebook_cover4_tableRow3, R.id.img_ebook_cover1_tableRow4,
			R.id.img_ebook_cover2_tableRow4, R.id.img_ebook_cover3_tableRow4,
			R.id.img_ebook_cover4_tableRow4 };

	static int id_shadow[] = { R.id.img_ebook_shadow1_tableRow1,
			R.id.img_ebook_shadow2_tableRow1, R.id.img_ebook_shadow3_tableRow1,
			R.id.img_ebook_shadow4_tableRow1, R.id.img_ebook_shadow1_tableRow2,
			R.id.img_ebook_shadow2_tableRow2, R.id.img_ebook_shadow3_tableRow2,
			R.id.img_ebook_shadow4_tableRow2, R.id.img_ebook_shadow1_tableRow3,
			R.id.img_ebook_shadow2_tableRow3, R.id.img_ebook_shadow3_tableRow3,
			R.id.img_ebook_shadow4_tableRow3, R.id.img_ebook_shadow1_tableRow4,
			R.id.img_ebook_shadow2_tableRow4, R.id.img_ebook_shadow3_tableRow4,
			R.id.img_ebook_shadow4_tableRow4 };

	static ImageView img_cover1, img_cover2, img_cover3, img_cover4,
			img_cover5, img_cover6, img_cover7, img_cover8, img_cover9,
			img_cover10, img_cover11, img_cover12, img_cover13, img_cover14,
			img_cover15, img_cover16;
	static ImageView img_shadow1, img_shadow2, img_shadow3, img_shadow4,
			img_shadow5, img_shadow6, img_shadow7, img_shadow8, img_shadow9,
			img_shadow10, img_shadow11, img_shadow12, img_shadow13,
			img_shadow14, img_shadow15, img_shadow16;
	static ImageView img_covers[] = { img_cover1, img_cover2, img_cover3,
			img_cover4, img_cover5, img_cover6, img_cover7, img_cover8,
			img_cover9, img_cover10, img_cover11, img_cover12, img_cover13,
			img_cover14, img_cover15, img_cover16 };
	static ImageView img_shadow[] = { img_shadow1, img_shadow2, img_shadow3,
			img_shadow4, img_shadow5, img_shadow6, img_shadow7, img_shadow8,
			img_shadow9, img_shadow10, img_shadow11, img_shadow12,
			img_shadow13, img_shadow14, img_shadow15, img_shadow16 };

	static int[] id_rt = { R.id.img_ebook_imag1_tableRow1,
			R.id.img_ebook_imag2_tableRow1, R.id.img_ebook_imag3_tableRow1,
			R.id.img_ebook_imag4_tableRow1, R.id.img_ebook_imag1_tableRow2,
			R.id.img_ebook_imag2_tableRow2, R.id.img_ebook_imag3_tableRow2,
			R.id.img_ebook_imag4_tableRow2, R.id.img_ebook_imag1_tableRow3,
			R.id.img_ebook_imag2_tableRow3, R.id.img_ebook_imag3_tableRow3,
			R.id.img_ebook_imag4_tableRow3, R.id.img_ebook_imag1_tableRow4,
			R.id.img_ebook_imag2_tableRow4, R.id.img_ebook_imag3_tableRow4,
			R.id.img_ebook_imag4_tableRow4 };

	static RelativeLayout rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8, rt9, rt10,
			rt11, rt12, rt13, rt14, rt15, rt16;
	static RelativeLayout[] rt_view = { rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8,
			rt9, rt10, rt11, rt12, rt13, rt14, rt15, rt16 };

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

}
