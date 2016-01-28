package com.porar.ebook.adapter;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.ImageDownloader_forCache;
/*
 * this for use ActivityFragment Not work on Tabhost view.
 */
public class MyAdapterViewPager_BookFragment extends FragmentPagerAdapter {

	private final int limit = 16;
	private int index = 0;
	public static ArrayList<ArrayList<Model_EbookList>> arrList;
	private ArrayList<Model_EbookList> arrinside;
	private final PList pList;
	public static int sizePage = 0;
	static View myView = null;

	public MyAdapterViewPager_BookFragment(FragmentManager fm, PList pList) {
		super(fm);

		this.pList = pList;
		arrList = new ArrayList<ArrayList<Model_EbookList>>();

		Array arr_check = (Array) this.pList.getRootElement();
		if ((arr_check.size() <= 0)) {

		} else {
			for (PListObject each : (Array) this.pList.getRootElement()) {
				if (index == 0) {
					arrinside = new ArrayList<Model_EbookList>();
				}
				arrinside.add(new Model_EbookList(each));
				index++;
				if (index >= limit) {
					index = 0;
					arrList.add(arrinside);

				}
			}
			if (arrinside.size() != 0 && arrinside.size() < limit) {
				arrList.add(arrinside);
			}

			sizePage = arrList.size();

		}
	}

	@Override
	public void finishUpdate(View view) {

	}

	@Override
	public Fragment getItem(int pos) {
		return Fragment_Book.newInstance(pos);
	}

	@Override
	public int getCount() {
		if (sizePage != 0) {
			return sizePage;
		} else {
			return 1;
		}
	}

	@SuppressLint("ValidFragment")
	public static class Fragment_Book extends Fragment {
		static int pos;
		static int mNum;
		ImageDownloader_forCache downloader_forCache;

		static Fragment_Book newInstance(int num) {
			Fragment_Book f = new Fragment_Book();
			pos = num;

			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			myView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tableview_ebook, null);
			return myView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			downloader_forCache = new ImageDownloader_forCache();
			for (int i = 0; i < arrList.size(); i++) {
				for (int j = 0; j < arrList.get(i).size(); j++) {
					try {
						img_covers[j] = (ImageView) myView.findViewById(id_cover[j]);
						img_shadow[j] = (ImageView) myView.findViewById(id_shadow[j]);
						downloader_forCache.download(AppMain.COVER_URL + arrList.get(mNum).get(j).getBID() + "/" + arrList.get(mNum).get(j).getCover(),
								img_covers[j],
								img_shadow[j]);
					} catch (Exception e) {
						Log.e("", "" + arrList.get(i).size());

						img_shadow[j].setVisibility(View.INVISIBLE);
						img_covers[j].setVisibility(View.INVISIBLE);
					}
				}
			}
		}
	}

	static int id_cover[] = { R.id.img_ebook_cover1_tableRow1, R.id.img_ebook_cover2_tableRow1, R.id.img_ebook_cover3_tableRow1, R.id.img_ebook_cover4_tableRow1,
			R.id.img_ebook_cover1_tableRow2, R.id.img_ebook_cover2_tableRow2, R.id.img_ebook_cover3_tableRow2, R.id.img_ebook_cover4_tableRow2,
			R.id.img_ebook_cover1_tableRow3, R.id.img_ebook_cover2_tableRow3, R.id.img_ebook_cover3_tableRow3, R.id.img_ebook_cover4_tableRow3,
			R.id.img_ebook_cover1_tableRow4, R.id.img_ebook_cover2_tableRow4, R.id.img_ebook_cover3_tableRow4, R.id.img_ebook_cover4_tableRow4 };

	static int id_shadow[] = { R.id.img_ebook_shadow1_tableRow1, R.id.img_ebook_shadow2_tableRow1, R.id.img_ebook_shadow3_tableRow1, R.id.img_ebook_shadow4_tableRow1,
			R.id.img_ebook_shadow1_tableRow2, R.id.img_ebook_shadow2_tableRow2, R.id.img_ebook_shadow3_tableRow2, R.id.img_ebook_shadow4_tableRow2,
			R.id.img_ebook_shadow1_tableRow3, R.id.img_ebook_shadow2_tableRow3, R.id.img_ebook_shadow3_tableRow3, R.id.img_ebook_shadow4_tableRow3,
			R.id.img_ebook_shadow1_tableRow4, R.id.img_ebook_shadow2_tableRow4, R.id.img_ebook_shadow3_tableRow4, R.id.img_ebook_shadow4_tableRow4 };

	static ImageView img_cover1, img_cover2, img_cover3, img_cover4, img_cover5, img_cover6, img_cover7, img_cover8, img_cover9, img_cover10, img_cover11, img_cover12, img_cover13, img_cover14, img_cover15, img_cover16;
	static ImageView img_shadow1, img_shadow2, img_shadow3, img_shadow4, img_shadow5, img_shadow6, img_shadow7, img_shadow8, img_shadow9, img_shadow10, img_shadow11, img_shadow12, img_shadow13, img_shadow14, img_shadow15, img_shadow16;
	static ImageView img_covers[] = { img_cover1, img_cover2, img_cover3, img_cover4, img_cover5, img_cover6, img_cover7, img_cover8, img_cover9, img_cover10, img_cover11, img_cover12, img_cover13, img_cover14, img_cover15, img_cover16 };
	static ImageView img_shadow[] = { img_shadow1, img_shadow2, img_shadow3, img_shadow4, img_shadow5, img_shadow6, img_shadow7, img_shadow8, img_shadow9, img_shadow10, img_shadow11, img_shadow12, img_shadow13, img_shadow14, img_shadow15, img_shadow16 };

}
