package com.porar.ebook.adapter;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Adapter_SettingBackgroundStyle extends ArrayAdapter<String> implements Serializable {

	int[] id_image = { R.drawable.bg_main_default_ss, R.drawable.bg_fine_wood_ss2x,
			R.drawable.bg_white_hardwood_ss2x, R.drawable.bg_wood_ss2x,
			R.drawable.bg_wood_wall_ss2x };
	String[] name_image;
	String[] name_image2;
	private static final long serialVersionUID = 1L;
	Context context;
	LayoutInflater inflater;
	
	public Adapter_SettingBackgroundStyle(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	

		name_image = ((Activity) context).getResources().getStringArray(R.array.name_image);
		name_image2= ((Activity) context).getResources().getStringArray(R.array.name_image2);
	}

	@Override
	public int getCount() {
		return id_image.length;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.setting_item_bgstyle, null);
			viewHolder.iv_bg = (ImageView) convertView.findViewById(R.id.ivCover_setting);
			viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txtName_setting);
			viewHolder.txt_name2 = (TextView) convertView.findViewById(R.id.txtName_setting2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.iv_bg.setImageResource(id_image[position]);
		viewHolder.txt_name.setText("" + name_image[position]);
		viewHolder.txt_name2.setText("" + name_image2[position]);
		viewHolder.txt_name.setTypeface(StaticUtils.getTypeface(context, Font.LayijiMahaniyomV105ot));
		viewHolder.txt_name2.setTypeface(StaticUtils.getTypeface(context, Font.LayijiMahaniyomV105ot));
		return convertView;
	}

	public static class ViewHolder {
		public ImageView iv_bg = null;
		public TextView txt_name = null;
		public TextView txt_name2 = null;
	}
}
