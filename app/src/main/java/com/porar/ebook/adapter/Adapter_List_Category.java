package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;

public class Adapter_List_Category extends BaseAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Activity context;
	ArrayList<Model_Categories> arr_Categories;
	ImageDownloader_forCache imageDownloader;
	LayoutInflater inflater;
	Handler handler = new Handler();

	// public AdapterSpinnerCategory(Activity context, ArrayList<Model_Categories> arr_Categories, int textViewResourceId) {
	// super(context, textViewResourceId);
	// this.context = context;
	// this.arr_Categories = arr_Categories;
	// inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// imageDownloader = new ImageDownloader_forCache();
	// }

	public Adapter_List_Category(Activity context, ArrayList<Model_Categories> arr_Categories) {
		this.context = context;
		this.arr_Categories = arr_Categories;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageDownloader = new ImageDownloader_forCache();
	}

	@Override
	public int getCount() {
		if (arr_Categories == null)
			return 0;
		return arr_Categories.size();
	}

	@Override
	public Model_Categories getItem(int position) {
		return arr_Categories.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int index = position;

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_ebookcategory, null);
			holder.txt_name = (TextView) convertView.findViewById(R.id.ptxtName_Scategory);
			holder.txt_detail = (TextView) convertView.findViewById(R.id.ptxtPublisher_Scategory);
			holder.img_cover = (ImageView) convertView.findViewById(R.id.img_category_pcover);
			holder.img_shadow = (ImageView) convertView.findViewById(R.id.img_category_pshadow);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (arr_Categories.get(index).getPictureURL().length() > 0) {
			imageDownloader.download(
					"http://www.ebooks.in.th/" + arr_Categories.get(index).getPictureURL(),
					holder.img_cover,
					holder.img_shadow);
		}else{
			
		}

		holder.txt_name.setText("" + arr_Categories.get(index).getName());
		holder.txt_detail.setText("" + arr_Categories.get(index).getDetail());

		return convertView;
	}

	static class ViewHolder {
		ImageView img_cover;
		ImageView img_shadow;
		TextView txt_name;
		TextView txt_detail;
	}
}
