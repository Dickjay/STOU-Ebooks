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

public abstract class View_TypeEbookShelf {

	LinearLayout layout_tab;
	LayoutInflater inflater;
	LinearLayout linear_fotTypeEbook;
	RelativeLayout rt_dummyscreen;
	Activity context;
	Button bt_new;
	Button bt_favor;
	Button bt_trash;

	int id_head;

	public View_TypeEbookShelf(Activity context, LinearLayout linear_fotTypeEbook, RelativeLayout rt_dummyscreen, int id_head) {
		this.context = context;
		this.linear_fotTypeEbook = linear_fotTypeEbook;
		this.rt_dummyscreen = rt_dummyscreen;
		this.id_head = id_head;

		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@SuppressWarnings("static-access")
	public void initView(int left, int right, int top, int height) {

		rt_dummyscreen.setVisibility(View.VISIBLE);

		layout_tab = (LinearLayout) inflater.from(context).inflate(R.layout.layout_tabview_typeebookshelf, null);
		bt_new = (Button) layout_tab.findViewById(R.id.tabview_btn_newsshelf);
		bt_favor = (Button) layout_tab.findViewById(R.id.tabview_btn_favoriteshelf);
		bt_trash = (Button) layout_tab.findViewById(R.id.tabview_btn_trashshelf);
		bt_new.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_favor.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
		bt_trash.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));

		bt_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_new.setEnabled(false);
				bt_favor.setEnabled(true);
				bt_trash.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(0, context.getString(R.string.shelf_news));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_favor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_new.setEnabled(true);
				bt_favor.setEnabled(false);
				bt_trash.setEnabled(true);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(1, context.getString(R.string.shelf_favorite));
				TypeName(hashtable);
				onClickSrceen(rt_dummyscreen, linear_fotTypeEbook, layout_tab);
			}
		});
		bt_trash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bt_new.setEnabled(true);
				bt_favor.setEnabled(true);
				bt_trash.setEnabled(false);

				Hashtable<Integer, String> hashtable = new Hashtable<Integer, String>();
				hashtable.put(2, context.getString(R.string.shelf_trash));
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

		params.setMargins(0, 0, right, 0);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.BELOW, id_head);
		linear_fotTypeEbook.addView(layout_tab);
		linear_fotTypeEbook.setLayoutParams(params);

	}

	public abstract void onClickSrceen(RelativeLayout dummyscreen, LinearLayout layout_fortabber, LinearLayout layout_tabbar);

	public abstract void TypeName(Hashtable<Integer, String> hashtable);
}
