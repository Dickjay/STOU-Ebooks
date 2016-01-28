package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;

public class Adapter_backissue_List_Ebook extends ArrayAdapter<String> implements Serializable {

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

	public Adapter_backissue_List_Ebook(Context context, int textViewResourceId, PList plist) {
		super(context, textViewResourceId);
		this.context = context;
		if (plist == null) {
			ebookList = new ArrayList<Model_EbookList>();
			this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		} else {
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

	public int getItem_BID(int position) {
		return ebookList.get(position).getBID();
	}

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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_backissue_ebook, null);
			viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.img_ebook_pcover);
			viewHolder.iv_shadow = (ImageView) convertView.findViewById(R.id.img_ebook_pshadow);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		String coverUrl = AppMain.COVER_URL + ebookList.get(position).getBID() + "/" + ebookList.get(position).getCover();
		downloader_forCache.download(coverUrl, viewHolder.iv_cover, viewHolder.iv_shadow);

		return convertView;
	}

	public static class ViewHolder {

		public ImageView iv_cover = null;
		public ImageView iv_shadow = null;

	}
}
