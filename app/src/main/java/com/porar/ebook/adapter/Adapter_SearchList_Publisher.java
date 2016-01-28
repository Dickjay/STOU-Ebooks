package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PListObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.stou.activity.ActivityDetail_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Adapter_SearchList_Publisher extends ArrayAdapter<String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Model_Publisher> publishersList;
	Context context;
	LayoutInflater inflater;
	ImageDownloader_forCache downloader_forCache;
	Ebook_Detail ebook_Detail;
	AlertDialog alertDialog;

	public Adapter_SearchList_Publisher(Context context, int textViewResourceId, ArrayList<Model_Publisher> publishersList) {
		super(context, textViewResourceId);
		this.context = context;
		this.publishersList = publishersList;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();
	}

	@Override
	public int getCount() {
		if (publishersList.size() != 0) {
			return publishersList.size();
		} else {
			return 1;
		}
	}

//	public int getItem_BID(int position) {
//		return publishersList.get(position).getCID();
//	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (publishersList.size() == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView.findViewById(R.id.txt_noresult);
				txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			}
			return convertView;
		}

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.search_item_publisher, null);
			viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtpublishet_name);
			viewHolder.txtCout = (TextView) convertView.findViewById(R.id.txtpublishet_count);
			viewHolder.txtDetail = (TextView) convertView.findViewById(R.id.txtpublishet_detail);
			viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_publisher);

			viewHolder.txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			viewHolder.txtCout.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
			viewHolder.txtDetail.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.txtName.setText("" + publishersList.get(position).getName());
		viewHolder.txtDetail.setText("" + publishersList.get(position).getDetail());
		viewHolder.txtCout.setText("" + publishersList.get(position).getCounteBooks() + " " + context.getString(R.string.publisher_each));

		String coverUrl = "http://www.ebooks.in.th/publishers/" + publishersList.get(position).getCID() + "/" + publishersList.get(position).getImage();
		downloader_forCache.download(coverUrl, viewHolder.iv_cover);

		OnClickToDetail clickToDetail = new OnClickToDetail();
		clickToDetail.newInstant(publishersList.get(position));
		convertView.setOnClickListener(clickToDetail);

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
		public TextView txtCout = null;
		public TextView txtDetail = null;
		public ImageView iv_cover = null;

	}
}
