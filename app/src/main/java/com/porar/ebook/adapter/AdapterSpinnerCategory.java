package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PListObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Categories1;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

public class AdapterSpinnerCategory extends BaseAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Activity context;
	ArrayList<Model_Categories> arr_Categories;
	ArrayList<Model_Categories1> arr_Categories1;
	ImageDownloader_forCache imageDownloader;
	LayoutInflater inflater;
	Handler handler = new Handler();

	public AdapterSpinnerCategory(Activity context,
			ArrayList<Model_Categories> arr_Categories) {
		this.context = context;
		this.arr_Categories = arr_Categories;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageDownloader = new ImageDownloader_forCache();
		addValueImages();
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
		holder.txt_name.setText(arr_Categories.get(index).getName());
		holder.txt_detail.setText(arr_Categories.get(index).getDetail());

		if (arr_Categories.get(index).getPictureURL().equals("default")) {
			try {

				Picasso.with(context)
						.load("http://203.150.225.223/stoubookapi/"
								+ arr_Categories1.get(0).getPictureURL())
						.into(holder.img_cover);

			} catch (Exception e) {
				// TODO: handle exception

				Picasso.with(context).load(R.drawable.ic_launcher)
						.into(holder.img_cover);

			}

		} else {
			try {
				Picasso.with(context)
						.load("http://203.150.225.223/stoubookapi/"
								+ arr_Categories.get(index).getPictureURL())
						.into(holder.img_cover);

			} catch (Exception e) {
				// TODO: handle exception
				Picasso.with(context).load(R.drawable.ic_launcher)
						.into(holder.img_cover);
			}

		}

		return convertView;
	}

	static class ViewHolder {
		ImageView img_cover;
		TextView txt_name;
		TextView txt_detail;
	}

	public void addValueImages() {
		try {
			arr_Categories1 = new ArrayList<Model_Categories1>();
			Model_Categories1 model_allbook = new Model_Categories1();
			model_allbook.setCatID(0);
			model_allbook.setSCatID(0);
			model_allbook.setName("เอกสารการสอน");
			model_allbook.setDetail("หนังสือทั้งหมดทุกหมวดในคลังหนังสือ");
			model_allbook.setPictureURL("images/allcat.png");
			arr_Categories1.add(model_allbook);

		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}
}