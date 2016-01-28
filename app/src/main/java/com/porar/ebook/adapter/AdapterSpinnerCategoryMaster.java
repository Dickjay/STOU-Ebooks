package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebook.adapter.AdapterSpinnerCategory.ViewHolder;
import com.porar.ebooks.model.Model_Book_Master;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Categories1;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

public class AdapterSpinnerCategoryMaster extends BaseAdapter implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Activity context;
	ArrayList<Model_Book_Master> arr_Categories;

	ImageDownloader_forCache imageDownloader;
	LayoutInflater inflater;
	Handler handler = new Handler();

	public AdapterSpinnerCategoryMaster(Activity context,
			ArrayList<Model_Book_Master> arr_Categories) {
		this.context = context;
		this.arr_Categories = arr_Categories;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageDownloader = new ImageDownloader_forCache();

	}

	@Override
	public int getCount() {
		if (arr_Categories == null)
			return 0;
		return arr_Categories.size();
	}

	@Override
	public Model_Book_Master getItem(int position) {
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
			convertView = inflater.inflate(R.layout.layout_category, null);
			holder.img_cover = (ImageView) convertView
					.findViewById(R.id.category_image_cover);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.category_txt_name);
			holder.txt_detail = (TextView) convertView
					.findViewById(R.id.category_txt_detail);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_name.setTypeface(StaticUtils.getTypeface(context,
				Font.DB_Helvethaica_X_Med));
		holder.txt_detail.setTypeface(StaticUtils.getTypeface(context,
				Font.DB_Helvethaica_X_Med));
		holder.txt_name.setText(arr_Categories.get(index).getBookName());
		holder.txt_detail.setVisibility(View.GONE);

		try {
			Picasso.with(context)
					.load(arr_Categories.get(index).getBookCover())
					.into(holder.img_cover);

		} catch (Exception e) {
			// TODO: handle exception
			Picasso.with(context).load(R.drawable.ic_launcher)
					.into(holder.img_cover);
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView img_cover;
		TextView txt_name;
		TextView txt_detail;
	}

}