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
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Publisher_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.stou.activity.ActivityDetail_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;

public class Adapter_List_Publisher extends ArrayAdapter<String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Model_Publisher> List;
	ArrayList<Model_Publisher> publishersList;
	Context context;
	LayoutInflater inflater;
	ImageDownloader_forCache downloader_forCache;
	Ebook_Detail ebook_Detail;
	AlertDialog alertDialog;
	Model_Ebooks_Detail ebooks_Detail = null;
	ArrayList<Model_Comment_List> comment_Lists = null;
	LruCache<String, byte[]> mMemoryCache;

	public Adapter_List_Publisher(Context context, int textViewResourceId, PList plist) {
		super(context, textViewResourceId);
		this.context = context;

		List = new ArrayList<Model_Publisher>();
		for (PListObject each : (Array) plist.getRootElement()) {
			List.add(new Model_Publisher(each));
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
		if (List.size() != 0) {
			return List.size();
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

		if (List.size() == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView.findViewById(R.id.txt_noresult);
				// txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			}
			return convertView;
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_publisher, null);

			viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.img_publisher_pcover);
			viewHolder.iv_shadow = (ImageView) convertView.findViewById(R.id.img_publisher_pshadow);
			viewHolder.txtName = (TextView) convertView.findViewById(R.id.ptxtPublisherName_Sebook);
			viewHolder.txtDetail = (TextView) convertView.findViewById(R.id.ptxtPublisherDetail_Sebook);
			viewHolder.txtFan = (TextView) convertView.findViewById(R.id.ptxtISSUE_Sebook);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		viewHolder.txtDetail.setText("" + List.get(position).getDetail());
		viewHolder.txtName.setText("" + List.get(position).getName());
		viewHolder.txtFan.setText("" + List.get(position).getCounteBooks() + "\t ISSUE");
		String coverUrl = "http://www.ebooks.in.th/publishers/" + List.get(position).getCID() + "/" + List.get(position).getImage();
		downloader_forCache.download(coverUrl, viewHolder.iv_cover, viewHolder.iv_shadow);

		OnClickToDetail ToDetailPub = new OnClickToDetail();
		ToDetailPub.newInstant(List.get(position));
		convertView.setOnClickListener(ToDetailPub);

		return convertView;
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
						if (AppMain.pList_default_publisher_ebook == null) {
							alertDialog = new AlertDialog.Builder(context).create();
							alertDialog.setTitle(AppMain.getTag());
							alertDialog.setMessage("WARNING: An error has ocurred. Please to try again ?");
							alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									System.gc();
System.out.println("Retry");
									Intent intent = new Intent(context, ActivityDetail_Publisher.class);
									intent.putExtra("model", model_Publisher_Detail);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									context.startActivity(intent);
									((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

								}
							});
							alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Retry", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd.show();
									dialog.dismiss();
									System.gc();
									publisher_Detail.LoadEbooksDetailAPI();
								}
							});
							alertDialog.show();
						} else {
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

	public static class ViewHolder {
		public TextView txtName = null;
		public TextView txtDetail = null;
		public TextView txtFan = null;
		public ImageView iv_cover = null;
		public ImageView iv_shadow = null;

	}
}
