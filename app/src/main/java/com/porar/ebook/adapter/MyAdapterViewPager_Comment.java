package com.porar.ebook.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.porar.ebook.control.MyLinear_CommentView;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class MyAdapterViewPager_Comment extends PagerAdapter {

	private final Activity context;
	int sizePage = 0;
	private View myView = null;
	public LayoutInflater inflater;
	private final ArrayList<Model_Comment_List> comment_List;

	public MyAdapterViewPager_Comment(Activity context,ArrayList<Model_Comment_List> comment_List) {
		this.context = context;
		this.comment_List = comment_List;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		sizePage = comment_List.size();
	}

	@Override
	public void destroyItem(final View view, final int position, final Object object) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				((ViewPager) view).removeView((LinearLayout) object);
			}
		});

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (sizePage != 0) {
			return sizePage;
		} else {
			return 1;
		}
	}

	@Override
	public Object instantiateItem(final View view, final int position) {

		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (sizePage == 0) {
					myView = LayoutInflater.from(context).inflate(R.layout.search_item_ebook_noresult, null);
					TextView txtName = (TextView) myView.findViewById(R.id.txt_noresult);
					txtName.setText("No Comment");
					txtName.setTypeface(StaticUtils.getTypeface(context, Font.DB_Helvethaica_X_Med));
					((ViewPager) view).addView(myView);

				} else {
					// do View
					myView = new MyLinear_CommentView(context, comment_List.get(position));
					((ViewPager) view).addView(myView);
				}

			}
		});
		return myView;

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}
