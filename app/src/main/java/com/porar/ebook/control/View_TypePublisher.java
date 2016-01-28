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

public abstract class View_TypePublisher {

	LinearLayout layout_tab;
	LayoutInflater inflater;
	LinearLayout linear_fotTypeEbook;
	RelativeLayout rt_dummyscreen;
	Activity context;
	Button bt_update;
	Button bt_recommend;
	Button bt_name;
	int id_head;

	public View_TypePublisher(Activity context, LinearLayout linear_fotTypeEbook, RelativeLayout rt_dummyscreen, int id_head) {
		this.context = context;
		this.linear_fotTypeEbook = linear_fotTypeEbook;
		this.rt_dummyscreen = rt_dummyscreen;
		this.id_head = id_head;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@SuppressWarnings("static-access")
	public void initView(int left, int top, int height) {

		rt_dummyscreen.setVisibility(View.VISIBLE);

		layout_tab = (LinearLayout) inflater.from(context).inflate(R.layout.layout_tabview_pub, null);
		bt_update = (Button) layout_tab.findViewById(R.id.tabview_btn_updatePub);
		bt_recommend = (Button) layout_tab.findViewById(R.id.tabview_btn_recommendPub);
		bt_name = (Button) layout_tab.findViewById(R.id.tabview_btn_namePub);

		bt_update.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_recommend.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_name.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));

		bt_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_update.setEnabled(false);
				bt_recommend.setEnabled(true);
				bt_name.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(0, context.getString(R.string.publisher_update));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_recommend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_update.setEnabled(true);
				bt_recommend.setEnabled(false);
				bt_name.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(1, context.getString(R.string.publisher_recommend));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_update.setEnabled(true);
				bt_recommend.setEnabled(true);
				bt_name.setEnabled(false);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(2, context.getString(R.string.publisher_name));
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
