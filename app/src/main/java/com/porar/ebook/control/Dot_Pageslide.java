package com.porar.ebook.control;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.porar.ebooks.stou.R;

public class Dot_Pageslide {

	Activity context;
	ImageView view;
	LinearLayout layout_dotpage, layout_main;
	HorizontalScrollView horizontalScrollView;
	int s = 0;

	public Dot_Pageslide(Activity context) {
		this.context = context;
		

	}

	public LinearLayout setImage_slide(int size) {

		layout_dotpage = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_dotpage, null);
		horizontalScrollView = (HorizontalScrollView) layout_dotpage.findViewById(R.id.dotpage_horizontalScrollView1);
		layout_main = (LinearLayout) layout_dotpage.findViewById(R.id.pagedot_li_main);
		for (int i = 0; i < size; i++) {

			view = new ImageView(context);
			view.setImageResource(R.drawable.dot_page);

			layout_main.addView(view);
		}
		return layout_dotpage;

	}

	public void setHeighlight(int position) {

		for (int i = 0; i < layout_main.getChildCount(); i++) {
			if (position == i) {
				layout_main.getChildAt(i).setEnabled(false);
			} else {
				layout_main.getChildAt(i).setEnabled(true);
			}

		}
		if (position <= 0) {
			layout_main.getChildAt(0).setEnabled(false);
		}
	}
}
