package com.porar.ebook.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

import com.porar.ebook.adapter.Adapter_SettingBackgroundStyle;
import com.porar.ebook.adapter.Adapter_SettingShelfStyle;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public abstract class View_Setting {

	LinearLayout layout_setting;
	LayoutInflater inflater;
	LinearLayout linear_fotTypeEbook;
	RelativeLayout rt_dummyscreen, rt_shelf, rt_bg;
	Activity context;
	ProgressDialog progressDialog;
	AlertDialog alertDialog;
	private int id_head = 0;
	TextView txt_bg1, txt_bg2, txt_shelf1, txt_shelf2;
	static ImageView img_bg;
	public static ImageView img_shelf;

	public View_Setting(Activity context, LinearLayout linear_fotTypeEbook,
			RelativeLayout rt_dummyscreen, int id_head) {
		this.context = context;
		this.linear_fotTypeEbook = linear_fotTypeEbook;
		this.rt_dummyscreen = rt_dummyscreen;
		this.id_head = id_head;

		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@SuppressWarnings("static-access")
	public void initView() {
		try {
			rt_dummyscreen.setVisibility(View.VISIBLE);

			layout_setting = (LinearLayout) inflater.from(context).inflate(
					R.layout.layout_setting, null);
			txt_bg1 = (TextView) layout_setting
					.findViewById(R.id.setting_text_bg1);
			txt_bg2 = (TextView) layout_setting
					.findViewById(R.id.setting_text_bg2);
			txt_shelf1 = (TextView) layout_setting
					.findViewById(R.id.setting_text_shelf1);
			txt_shelf2 = (TextView) layout_setting
					.findViewById(R.id.setting_text_shelf2);
			rt_shelf = (RelativeLayout) layout_setting
					.findViewById(R.id.setting_rt_shelf);
			rt_bg = (RelativeLayout) layout_setting
					.findViewById(R.id.setting_rt_bg);
			img_bg = (ImageView) layout_setting
					.findViewById(R.id.setting_imageview_bg);
			img_shelf = (ImageView) layout_setting
					.findViewById(R.id.setting_imageview_shelf);

			txt_bg1.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_bg2.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_shelf1.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_shelf2.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));

			Model_Setting setting = Class_Manage.getModel_Setting(context);
			if (setting != null) {
				img_bg.setImageResource(setting
						.getDrawable_backgroundStyleSmall());
			}
			Model_SettingShelf shelf = Class_Manage
					.getModel_SettingShelf(context);
			if (shelf != null) {
				img_shelf.setImageResource(shelf.getDrawable_ShelfStyleSmall());
			}

			rt_shelf.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickShelfStyle();
				}
			});
			rt_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickBackgroundStyle();
				}
			});
			rt_dummyscreen.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickSrceen(rt_dummyscreen, linear_fotTypeEbook,
							layout_setting);
					System.gc();
				}
			});

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.BELOW, id_head);
			linear_fotTypeEbook.addView(layout_setting);
			linear_fotTypeEbook.setLayoutParams(params);

		} catch (Exception e) {
			// TODO: handle exception

			// TODO: handle exception

			layout_setting = (LinearLayout) inflater.from(context).inflate(
					R.layout.layout_setting, null);
			txt_bg1 = (TextView) layout_setting
					.findViewById(R.id.setting_text_bg1);
			txt_bg2 = (TextView) layout_setting
					.findViewById(R.id.setting_text_bg2);
			txt_shelf1 = (TextView) layout_setting
					.findViewById(R.id.setting_text_shelf1);
			txt_shelf2 = (TextView) layout_setting
					.findViewById(R.id.setting_text_shelf2);
			rt_shelf = (RelativeLayout) layout_setting
					.findViewById(R.id.setting_rt_shelf);
			rt_bg = (RelativeLayout) layout_setting
					.findViewById(R.id.setting_rt_bg);
			img_bg = (ImageView) layout_setting
					.findViewById(R.id.setting_imageview_bg);
			img_shelf = (ImageView) layout_setting
					.findViewById(R.id.setting_imageview_shelf);

			txt_bg1.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_bg2.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_shelf1.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));
			txt_shelf2.setTypeface(StaticUtils.getTypeface(context,
					Font.LayijiMahaniyomV105ot));

			Model_Setting setting = Class_Manage.getModel_Setting(context);
			if (setting != null) {
				img_bg.setImageResource(setting
						.getDrawable_backgroundStyleSmall());
			}
			Model_SettingShelf shelf = Class_Manage
					.getModel_SettingShelf(context);
			if (shelf != null) {
				img_shelf.setImageResource(shelf.getDrawable_ShelfStyleSmall());
			}

			rt_shelf.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickShelfStyle();
				}
			});
			rt_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickBackgroundStyle();
				}
			});
			try {
				rt_dummyscreen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onClickSrceen(rt_dummyscreen, linear_fotTypeEbook,
								layout_setting);
						System.gc();
					}
				});
			} catch (Exception e2) {
				// TODO: handle exception

			}

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.BELOW, id_head);
			linear_fotTypeEbook.addView(layout_setting);
			linear_fotTypeEbook.setLayoutParams(params);

		}
		AppMain.isShelf = true;
	}

	public void onClickBackgroundStyle() {
		DialogBuild_bgstyle dialog = new DialogBuild_bgstyle(context,
				R.style.PauseDialog);
		dialog.show();
	}

	private class DialogBuild_bgstyle extends Dialog {

		int[] id_drawble = { R.drawable.bg_main_default,
				R.drawable.bg_finewood, R.drawable.bg_whilte_hardwood,
				R.drawable.bg_wood, R.drawable.bg_wood_wall ,R.drawable.bg_light_green };

		int[] id_drawbleSmall = { R.drawable.bg_main_default_ss,
				R.drawable.bg_finewood, R.drawable.bg_whilte_hardwood,
				R.drawable.bg_wood, R.drawable.bg_wood_wall ,R.drawable.bg_light_green };

		public DialogBuild_bgstyle(Context context, int theme) {
			super(context, theme);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
			init();
		}

		private void init() {
			ListView lv = new ListView(getContext());
			lv.setAdapter(new Adapter_SettingBackgroundStyle(context, 0));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long id) {
					DialogBuild_bgstyle.this.cancel();
					onChangeBackgroundStyle(id_drawble[position]);

					Model_Setting model_Setting = new Model_Setting();
					model_Setting
							.setDrawable_backgroundStyle(id_drawble[position]);
					model_Setting
							.setDrawable_backgroundStyleSmall(id_drawbleSmall[position]);

					if (Class_Manage.SaveSetting(context, model_Setting)) {
						img_bg.setImageResource(model_Setting
								.getDrawable_backgroundStyleSmall());
						Log.v("", "Class_Manage SaveSetting Success");
					}
				}
			});

			setContentView(lv);
		}

	}

	public void onClickShelfStyle() {
		DialogShelfStyle dialog = new DialogShelfStyle(context,
				R.style.PauseDialog);
		dialog.show();
	}

	private class DialogShelfStyle extends Dialog {

		int[] id_drawableshelf = { R.drawable.shelf_glass2x,
				R.drawable.shelf_wood2x, R.drawable.shelf_wood_steel2x,
				R.drawable.shelf_blue2x, R.drawable.shelf_red2x,
				R.drawable.shelf_dark2x };
		int[] id_drawshelfsmall = { R.drawable.shelf_glass,
				R.drawable.shelf_wood, R.drawable.shelf_wood_steel,
				R.drawable.shelf_blue, R.drawable.shelf_red,
				R.drawable.shelf_dark };

		public DialogShelfStyle(Context context, int theme) {
			super(context, theme);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
			init();
		}

		private void init() {
			ListView lv = new ListView(getContext());
			lv.setAdapter(new Adapter_SettingShelfStyle(context, 0));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long id) {
					DialogShelfStyle.this.cancel();
					onChangeShelfStyle(id_drawableshelf[position]);
					Model_SettingShelf settingShelf = new Model_SettingShelf();
					settingShelf.setDrawable_ShelfStyle(id_drawableshelf[position]);
					settingShelf.setDrawable_ShelfStyleSmall(id_drawshelfsmall[position]);

					if (Class_Manage.SaveSettingShelf(context, settingShelf)) {
						img_shelf.setImageResource(settingShelf
								.getDrawable_ShelfStyleSmall());
						Log.v("", "Class_Manage SaveSetting Success");
					}
				}
			});
			setContentView(lv);
			if (rt_dummyscreen == null) {
				onClickSrceen(rt_shelf, linear_fotTypeEbook, layout_setting);
				System.gc();
			}

		}

	}

	public abstract void onChangeBackgroundStyle(int drawableStyle);

	public abstract void onChangeShelfStyle(int drawableStyle);

	public abstract void onClickSrceen(RelativeLayout dummyscreen,
			LinearLayout layout_fortabber, LinearLayout layout_search);
}
