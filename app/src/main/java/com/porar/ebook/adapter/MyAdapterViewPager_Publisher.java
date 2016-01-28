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

import com.porar.ebook.control.Publisher_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.stou.activity.ActivityDetail_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class MyAdapterViewPager_Publisher extends PagerAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Activity context;
	private final int limit = 8;
	private int index = 0;
	private final ArrayList<ArrayList<Model_Publisher>> arrList;
	private ArrayList<Model_Publisher> arrinside;
	private final PList pList;
	int sizePage = 0;

	int view_index = 0;

	// 24-04-13
	LayoutInflater inflater;
	Handler handler = new Handler();
	ImageDownloader_forCache downloader_forCache;
	LruCache<String, byte[]> mMemoryCache;

	public MyAdapterViewPager_Publisher(Activity context, PList pList) {
		this.context = context;
		this.pList = pList;
		downloader_forCache = new ImageDownloader_forCache();
		arrList = new ArrayList<ArrayList<Model_Publisher>>();

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
					arrinside = new ArrayList<Model_Publisher>();
				}
				arrinside.add(new Model_Publisher(each));
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
	public void destroyItem(final View view, final int position, final Object object) {
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
			myView = LayoutInflater.from(context).inflate(R.layout.search_item_ebook_noresult, null);
			TextView txtName = (TextView) myView.findViewById(R.id.txt_noresult);
			txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			((ViewPager) view).addView(myView);

		} else {

			myView = LayoutInflater.from(context).inflate(R.layout.layout_tableview_publisher, null);
			((ViewPager) view).addView(myView);

			for (int j = 0; j < arrList.get(view_index).size(); j++) {
				img_covers[j] = (ImageView) myView.findViewById(id_cover[j]);
				txt_names[j] = (TextView) myView.findViewById(id_name[j]);
				txt_fans[j] = (TextView) myView.findViewById(id_fan[j]);
				txt_counts[j] = (TextView) myView.findViewById(id_count[j]);
				txt_details[j] = (TextView) myView.findViewById(id_detail[j]);

				txt_details[j].setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
				txt_counts[j].setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
				txt_names[j].setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
				txt_fans[j].setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
				txt_details[j].setText("\t" + arrList.get(view_index).get(j).getDetail());
				txt_names[j].setText("\t" + arrList.get(view_index).get(j).getName());
				txt_fans[j].setText("  "+arrList.get(view_index).get(j).getFans() + "\n Fans");
				txt_counts[j].setText(context.getString(R.string.publisher_countebook) + "\n" +
						arrList.get(view_index).get(j).getCounteBooks() + "\t" + context.getString(R.string.publisher_each));

				downloader_forCache.download("http://www.ebooks.in.th/publishers/" + arrList.get(view_index).get(j).getCID() + "/" + arrList.get(view_index).get(j).getImage(),
						img_covers[j]);

				rt_row[j] = (RelativeLayout) myView.findViewById(id_rt[j]);
				OnClickToDetail clickToDetail = new OnClickToDetail();
				clickToDetail.newInstant(arrList.get(view_index).get(j));
				rt_row[j].setOnClickListener(clickToDetail);
			}

			if (arrList.get(view_index).size() < limit) {
				int over = limit - arrList.get(view_index).size();
				Log.e("over", "" + over);
				for (int i = limit - 1; i > arrList.get(view_index).size() - 1; i--) {
					rt_row[i] = (RelativeLayout) myView.findViewById(id_rt[i]);
					rt_row[i].setVisibility(View.INVISIBLE);
				}
			}

		}

		return myView;

	}

	public class OnClickToDetail implements OnClickListener {
		Model_Publisher model_Publisher;
		Model_Publisher_Detail model_Publisher_Detail;
		Publisher_Detail publisher_Detail;
		AlertDialog alertDialog;

		public void newInstant(Model_Publisher object) {
			model_Publisher = object;
		}

		@Override
		public void onClick(View v) {
			Log.i("OnClickToDetail", "ebookBID " + model_Publisher.getCID());
			Log.i("OnClickToDetail", "ebookName " + model_Publisher.getName());

			publisher_Detail = new Publisher_Detail(context, String.valueOf(model_Publisher.getCID()));
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

						Intent intent = new Intent(context, ActivityDetail_Publisher.class);
						intent.putExtra("model", model_Publisher_Detail);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
						context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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

						alertDialog = new AlertDialog.Builder(context).create();
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

	static View myView = null;

	// detail book
	static int id_detail[] = { R.id.table_text_publisherdetail_row1colunm1, R.id.table_text_publisherdetail_row1colunm2,
			R.id.table_text_publisherdetail_row2colunm1, R.id.table_text_publisherdetail_row2colunm2,
			R.id.table_text_publisherdetail_row3colunm1, R.id.table_text_publisherdetail_row3colunm2,
			R.id.table_text_publisherdetail_row4colunm1, R.id.table_text_publisherdetail_row4colunm2, };
	static TextView img_detail1, img_detail2, img_detail3, img_detail4, img_detail5, img_detail6, img_detail7, img_detail8;
	static TextView txt_details[] = { img_detail1, img_detail2, img_detail3, img_detail4, img_detail5, img_detail6, img_detail7, img_detail8 };

	// count book
	static int id_count[] = { R.id.table_text_publisher_counteBooks_row1colunm1, R.id.table_text_publisher_counteBooks_row1colunm2,
			R.id.table_text_publisher_counteBooks_row2colunm1, R.id.table_text_publisher_counteBooks_row2colunm2,
			R.id.table_text_publisher_counteBooks_row3colunm1, R.id.table_text_publisher_counteBooks_row3colunm2,
			R.id.table_text_publisher_counteBooks_row4colunm1, R.id.table_text_publisher_counteBooks_row4colunm2, };
	static TextView img_count1, img_count2, img_count3, img_count4, img_count5, img_count6, img_count7, img_count8;
	static TextView txt_counts[] = { img_count1, img_count2, img_count3, img_count4, img_count5, img_count6, img_count7, img_count8 };

	// fan
	static int id_fan[] = { R.id.table_text_publisherfan_row1colunm1, R.id.table_text_publisherfan_row1colunm2,
			R.id.table_text_publisherfan_row2colunm1, R.id.table_text_publisherfan_row2colunm2,
			R.id.table_text_publisherfan_row3colunm1, R.id.table_text_publisherfan_row3colunm2,
			R.id.table_text_publisherfan_row4colunm1, R.id.table_text_publisherfan_row4colunm2 };
	static TextView img_fan1, img_fan2, img_fan3, img_fan4, img_fan5, img_fan6, img_fan7, img_fan8;
	static TextView txt_fans[] = { img_fan1, img_fan2, img_fan3, img_fan4, img_fan5, img_fan6, img_fan7, img_fan8 };

	// name
	static int id_name[] = { R.id.table_text_publishername_row1colunm1, R.id.table_text_publishername_row1colunm2,
			R.id.table_text_publishername_row2colunm1, R.id.table_text_publishername_row2colunm2,
			R.id.table_text_publishername_row3colunm1, R.id.table_text_publishername_row3colunm2,
			R.id.table_text_publishername_row4colunm1, R.id.table_text_publishername_row4colunm2 };
	static TextView img_name1, img_name2, img_name3, img_name4, img_name5, img_name6, img_name7, img_name8;
	static TextView txt_names[] = { img_name1, img_name2, img_name3, img_name4, img_name5, img_name6, img_name7, img_name8 };

	// img_cover
	static int id_cover[] = { R.id.table_img_publisher_row1colunm1, R.id.table_img_publisher_row1colunm2,
			R.id.table_img_publisher_row2colunm1, R.id.table_img_publisher_row2colunm2,
			R.id.table_img_publisher_row3colunm1, R.id.table_img_publisher_row3colunm2,
			R.id.table_img_publisher_row4colunm1, R.id.table_img_publisher_row4colunm2 };
	static ImageView img_cover1, img_cover2, img_cover3, img_cover4, img_cover5, img_cover6, img_cover7, img_cover8;
	static ImageView img_covers[] = { img_cover1, img_cover2, img_cover3, img_cover4, img_cover5, img_cover6, img_cover7, img_cover8 };

	// relative table
	static RelativeLayout rt_1, rt_2, rt_3, rt_4, rt_5, rt_6, rt_7, rt_8;
	static int id_rt[] = { R.id.img_publisher_imag1_tableRow1colunm1, R.id.img_publisher_imag2_tableRow1colunm2,
			R.id.img_publisher_imag1_tableRow2colunm1, R.id.img_publisher_imag2_tableRow2colunm2,
			R.id.img_publisher_imag1_tableRow3colunm1, R.id.img_publisher_imag2_tableRow3colunm2,
			R.id.img_publisher_imag1_tableRow4colunm1, R.id.img_publisher_imag2_tableRow4colunm2 };
	static RelativeLayout rt_row[] = { rt_1, rt_2, rt_3, rt_4, rt_5, rt_6, rt_7, rt_8 };

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
