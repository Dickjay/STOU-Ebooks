package com.porar.ebook.control;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public abstract class View_TypeEbook {

	LinearLayout layout_tab;
	LayoutInflater inflater;
	LinearLayout linear_fotTypeEbook;
	RelativeLayout rt_dummyscreen;
	Activity context;
	Button bt_news;
	Button bt_recommend;
	Button bt_free;
	Button bt_sales;
	 int id_head;

	public View_TypeEbook(Activity context, LinearLayout linear_fotTypeEbook, RelativeLayout rt_dummyscreen, int id_head) {
		this.context = context;
		this.linear_fotTypeEbook = linear_fotTypeEbook;
		this.rt_dummyscreen = rt_dummyscreen;
		this.id_head = id_head;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@SuppressWarnings("static-access")
	public void initView(int left, int top, int height) {

		rt_dummyscreen.setVisibility(View.VISIBLE);

		layout_tab = (LinearLayout) inflater.from(context).inflate(R.layout.layout_tabview, null);
		bt_news = (Button) layout_tab.findViewById(R.id.tabview_btn_news);
		bt_recommend = (Button) layout_tab.findViewById(R.id.tabview_btn_recommend);
		bt_free = (Button) layout_tab.findViewById(R.id.tabview_btn_free);
		bt_sales = (Button) layout_tab.findViewById(R.id.tabview_btn_sales);

		bt_news.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_recommend.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_free.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_sales.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));

		bt_news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_news.setEnabled(false);
				bt_recommend.setEnabled(true);
				bt_free.setEnabled(true);
				bt_sales.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(0, context.getString(R.string.new_releases));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_recommend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_news.setEnabled(true);
				bt_recommend.setEnabled(false);
				bt_free.setEnabled(true);
				bt_sales.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(1, context.getString(R.string.recommend));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_free.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_news.setEnabled(true);
				bt_recommend.setEnabled(true);
				bt_free.setEnabled(false);
				bt_sales.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(2, context.getString(R.string.free));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_sales.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_news.setEnabled(true);
				bt_recommend.setEnabled(true);
				bt_free.setEnabled(true);
				bt_sales.setEnabled(false);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(3, context.getString(R.string.sales));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});

		rt_dummyscreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.BELOW, id_head);
		linear_fotTypeEbook.addView(layout_tab);
		linear_fotTypeEbook.setLayoutParams(params);

	}

	public abstract void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_tabbar);

	public abstract void TypeName(Hashtable<Integer, String> hashtable);
}
