package com.porar.ebook.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.porar.ebook.control.Dialog_Show_Banner;
import com.porar.ebooks.model.Model_Banner;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.squareup.picasso.Picasso;

public class AdapterGalleryBanner extends BaseAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Activity context;
	ArrayList<Model_Banner> banners;
	ImageDownloader_forCache imageDownloader;
	LayoutInflater inflater;
	Handler handler = new Handler();

	public AdapterGalleryBanner(Activity context,
			ArrayList<Model_Banner> banners) {

		this.context = context;
		this.banners = banners;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageDownloader = new ImageDownloader_forCache();
	}

	@Override
	public int getCount() {
		if (banners == null)
			return 0;
		return banners.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return banners.get(position);
	}

	// @Override
	// public Model_Banner getItem(int position) {
	// return banners.get(position);
	// }

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.image_banner, null);
			holder.img_banner = (ImageView) convertView
					.findViewById(R.id.img_image_banner);
			holder.img_banner.setScaleType(ScaleType.FIT_XY);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// imageDownloader.download(
		// "http://www.ebooks.in.th/images/api/" +
		// banners.get(index).getImageFile(),
		// holder.img_banner);
		try {

			Picasso.with(context)
					.load(AppMain.NEW_LIST_URL2
							+ banners.get(position).getImageFile())
					.into(holder.img_banner);

		} catch (Exception e) {
			// TODO: handle exception
			Picasso.with(context)
					.load(AppMain.NEW_LIST_URL2
							+ banners.get(position).getImageFile())
					.into(holder.img_banner);
		}

		// holder.img_banner.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// String bannerPosition = banners.get(position).getNID();
		// new Dialog_Show_Banner(context, bannerPosition) {
		//
		// };
		// }
		// });

		return convertView;
	}

	static class ViewHolder {
		ImageView img_banner = null;
	}

}
