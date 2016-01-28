package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Book_Master;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.fragment.Fragment_Ebook;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

public class Adapter_List_Ebook_Master extends ArrayAdapter<String> implements
		Serializable {

	/**
* 
*/

	private static final long serialVersionUID = 1L;
	ArrayList<Model_Book_Master> ebookList;

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
	Fragment_Ebook fragment_Ebook;

	public Adapter_List_Ebook_Master(Context context, int textViewResourceId,
			PList plist, Fragment_Ebook fragment_Ebook) {
		super(context, textViewResourceId);
		this.context = context;
		this.fragment_Ebook = fragment_Ebook;
		ebookList = new ArrayList<Model_Book_Master>();
		for (PListObject each : (Array) plist.getRootElement()) {
			ebookList.add(new Model_Book_Master(each));
		}

		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);

		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
	}

	public static void setTypeAdapterTop(boolean Boolean) {
		top_ = Boolean;
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
				convertView = inflater.inflate(
						R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView
						.findViewById(R.id.txt_noresult);
				// txtName.setTypeface(StaticUtils.getTypeface(context,
				// Font.DB_Helvethaica_X_Med));
			}
			return convertView;
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_ebook, null);
			viewHolder.txtName = (TextView) convertView
					.findViewById(R.id.ptxtName_Sebook);
			viewHolder.txtPrice = (TextView) convertView
					.findViewById(R.id.ptxtPrice_Sebook);
			viewHolder.txtPublisher = (TextView) convertView
					.findViewById(R.id.ptxtPublisher_Sebook);
			viewHolder.iv_cover = (ImageView) convertView
					.findViewById(R.id.img_ebook_pcover);
			viewHolder.iv_shadow = (ImageView) convertView
					.findViewById(R.id.img_ebook_pshadow);
			viewHolder.rating = (RatingBar) convertView
					.findViewById(R.id.pRating_Sebook);
			viewHolder.rating.setEnabled(false);
			viewHolder.rt_cover = (RelativeLayout) convertView
					.findViewById(R.id.pPlinearImage);

			viewHolder.txtName.setTypeface(StaticUtils.getTypeface(context,
					Font.DB_Helvethaica_X_Med));
			viewHolder.txtPrice.setTypeface(StaticUtils.getTypeface(context,
					Font.DB_Helvethaica_X_Med));
			viewHolder.txtPublisher.setTypeface(StaticUtils.getTypeface(
					context, Font.DB_Helvethaica_X_Med));

			// viewHolder.txtName.setTextSize(context.getResources().getDimension(R.dimen.small));
			// viewHolder.txtPrice.setTextSize(context.getResources().getDimension(R.dimen.small));
			// viewHolder.txtPublisher.setTextSize(context.getResources().getDimension(R.dimen.small));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		String coverUrl = ebookList.get(position).getBookCover();

		Picasso.with(context).load(coverUrl).into(viewHolder.iv_cover);
		viewHolder.iv_cover.setVisibility(View.VISIBLE);

		viewHolder.txtName.setText(ebookList.get(position).getBookName());
		viewHolder.txtPublisher.setText("");

		String newPriceTHB = ebookList.get(position).getBookPrice();
		try {
			newPriceTHB = newPriceTHB
					.substring(0, newPriceTHB.lastIndexOf("."));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (newPriceTHB.equals("0")) {
			viewHolder.txtPrice.setText(ebookList.get(position).getBookPrice());
		} else {
			viewHolder.txtPrice.setText(newPriceTHB + " baht");
		}
		viewHolder.rating.setRating(Float.parseFloat(ebookList.get(position)
				.getBookRate()));

		OnClickToDetail detail = new OnClickToDetail();
		detail.newInstant(ebookList.get(position));

		convertView.setOnClickListener(detail);

		return convertView;
	}

	public class OnClickToDetail implements OnClickListener {
		Model_Book_Master model_EbookList;

		public void newInstant(Model_Book_Master object) {
			model_EbookList = object;

		}

		@Override
		public void onClick(View v) {
			getSubCatMasterDegree(model_EbookList.getMCatID(),
					model_EbookList.getBCode());
		}

		private void getSubCatMasterDegree(String mCat, String bCode) {
			StaticUtils.sCat = mCat;
			StaticUtils.bCode = bCode;
			final AsyncTask_FetchAPI asyncTask_FetchAPI = new AsyncTask_FetchAPI();
			final ProgressDialog dialogs = new ProgressDialog(context);
			dialogs.setMax(100);
			dialogs.show();
			asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

				private PList pList;

				@Override
				public void onTimeOut(String apiURL, int currentIndex) {
					// TODO Auto-generated method stub
					asyncTask_FetchAPI.cancel(true);
					Fragment_Ebook.isChoice = false;
				}

				@Override
				public void onFetchStart(String apiURL, int currentIndex) {
					// TODO Auto-generated method stub
					Log.d("TAG", "URL :" + " " + apiURL);

				}

				@Override
				public void onFetchError(String apiURL, int currentIndex,
						Exception e) {
					// TODO Auto-generated method stub
					asyncTask_FetchAPI.cancel(true);
					Fragment_Ebook.isChoice = false;
				}

				@Override
				public void onFetchComplete(String apiURL, int currentIndex,
						PList result) {
					// TODO Auto-generated method stub
					if (apiURL.contains("getMasterBookDetail")) {
						pList = result;

					}

				}

				@Override
				public void onAllTaskDone() {
					// TODO Auto-generated method stub
					Fragment_Ebook.isChoice = true;
					// TODO Auto-generated method stub
					if (AppMain.isphone) {

						fragment_Ebook.setListViewMasterDetail(pList, dialogs);
					} else {

						fragment_Ebook.setViewPagerMasterDetail(pList, dialogs);
					}

				}

			});
			String url = "http://203.150.225.223/stoubookapi/api/getMasterBookDetail.php";
			url += "?mcat=" + mCat;
			url += "&bcode=" + bCode;
			asyncTask_FetchAPI.execute(url);

		}
	}

	public static class ViewHolder {
		public TextView txtName = null;
		public TextView txtPrice = null;
		public TextView txtPublisher = null;
		public ImageView iv_cover = null;
		public ImageView iv_shadow = null;
		public RatingBar rating = null;
		public RelativeLayout rt_cover = null;
	}
}