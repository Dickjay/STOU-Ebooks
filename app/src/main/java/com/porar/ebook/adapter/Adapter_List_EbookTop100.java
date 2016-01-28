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
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;

public class Adapter_List_EbookTop100 extends ArrayAdapter<String> implements Serializable {

	/**
	 * 
	 */
	private final ArrayList<String> arrPoint = new ArrayList<String>();
	private static final long serialVersionUID = 1L;
	ArrayList<Model_EbookList> ebookList;
	ArrayList<Model_Publisher> publishersList;
	Context context;
	LayoutInflater inflater;
	ImageDownloader_forCache downloader_forCache;
	Ebook_Detail ebook_Detail;
	AlertDialog alertDialog;
	Model_Ebooks_Detail ebooks_Detail = null;
	ArrayList<Model_Comment_List> comment_Lists = null;
	LruCache<String, byte[]> mMemoryCache;
	static boolean top_ = false;
	int bid1 = 0;
	int bid2 = 0;
	int bid3 = 0;

	public Adapter_List_EbookTop100(Context context, int textViewResourceId, PList plist) {
		super(context, textViewResourceId);
		this.context = context;

		ebookList = new ArrayList<Model_EbookList>();
		for (PListObject each : (Array) plist.getRootElement()) {
			ebookList.add(new Model_EbookList(each));
		}

		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);

		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
	}

	@Override
	public int getCount() {
		if (ebookList.size() != 0) {
			return ebookList.size();
		} else {
			return 1;
		}
	}

	// public int getItem_BID(int position) {
	// return ebookList_Searchs.get(position).getBID();
	// }

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		
		if (ebookList.size() == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView.findViewById(R.id.txt_noresult);
				// txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			}
			return convertView;
		}
		if (position <= 2) {
			arrPoint.add(String.valueOf(ebookList.get(position).getBID()));
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.top50_list, null);
			viewHolder.txt_num = (TextView) convertView.findViewById(R.id.top50_txt_num);
			viewHolder.iv_prize = (ImageView) convertView.findViewById(R.id.top50_imageViewprize);
			viewHolder.txtName = (TextView) convertView.findViewById(R.id.top50_ptxtName_Sebook);
			viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.top50_ptxtPrice_Sebook);
			viewHolder.txtPublisher = (TextView) convertView.findViewById(R.id.top50_ptxtPublisher_Sebook);
			viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.top50_img_ebook_pcover);
			viewHolder.iv_shadow = (ImageView) convertView.findViewById(R.id.top50_img_ebook_pshadow);
			viewHolder.rating = (RatingBar) convertView.findViewById(R.id.top50_pRating_Sebook);
			viewHolder.rating.setEnabled(false);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}

		
		setItem(String.valueOf(ebookList.get(position).getBID()), viewHolder.txt_num, viewHolder.iv_prize);
		String coverUrl = AppMain.COVER_URL + ebookList.get(position).getBID() + "/" + ebookList.get(position).getCover();

		downloader_forCache.download(coverUrl, viewHolder.iv_cover, viewHolder.iv_shadow);

		viewHolder.txt_num.setText(String.valueOf((position + 1)));
		viewHolder.txtName.setText(ebookList.get(position).getName());
		viewHolder.txtPublisher.setText(ebookList.get(position).getPublisher());

		String newPriceTHB = ebookList.get(position).getPriceTHB();
		try {
			newPriceTHB = newPriceTHB.substring(0, newPriceTHB.lastIndexOf("."));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (newPriceTHB.equals("0")) {
			viewHolder.txtPrice.setText(ebookList.get(position).getPrice());
		} else {
			viewHolder.txtPrice.setText(newPriceTHB + " baht");
		}
		viewHolder.rating.setRating(ebookList.get(position).getRating());

		OnClickToDetail detail = new OnClickToDetail();
		detail.newInstant(ebookList.get(position));
		convertView.setOnClickListener(detail);
		
		return convertView;
	}

	private void setItem(String object, TextView txt, ImageView img) {

		if (arrPoint.contains(object)) {
			txt.setTextColor(Color.WHITE);
			img.setVisibility(View.VISIBLE);
		} else {
			txt.setTextColor(Color.BLACK);
			img.setVisibility(View.INVISIBLE);
		}
	}

	public class OnClickToDetail implements OnClickListener {
		Model_EbookList model_EbookList;
		Ebook_Detail ebook_DetailApi;
		AlertDialog alertDialog;
		Model_Ebooks_Detail ebooks_Detail = null;

		public void newInstant(Model_EbookList object) {
			model_EbookList = object;

		}

		@Override
		public void onClick(View v) {

			// DerializeObject
			Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(context, model_EbookList.getBID());
			if (DSebooks_Detail != null) {
				Log.v("OnClickToDetailEbook", "DeserializeObject Success" + model_EbookList.getBID());
				Intent intent = new Intent(context, Activity_Detail.class);
				intent.putExtra("model", DSebooks_Detail);
				intent.putExtra("DeserializeObject", "1");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

			} else {
				ebook_DetailApi = new Ebook_Detail(context, String.valueOf(model_EbookList.getBID()));
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
							if (Class_Manage.SaveModel_Detail(context, ebooks_Detail, ebooks_Detail.getBID())) {
								Log.v("OnClickToDetailEbook", "SerializeObject Success" + ebooks_Detail.getBID());
							}

							Intent intent = new Intent(context, Activity_Detail.class);
							intent.putExtra("model", ebooks_Detail);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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

	public static class ViewHolder {
		public TextView txt_num = null;
		public ImageView iv_prize = null;
		public TextView txtName = null;
		public TextView txtPrice = null;
		public TextView txtPublisher = null;
		public ImageView iv_cover = null;
		public ImageView iv_shadow = null;
		public RatingBar rating = null;
	}
}
