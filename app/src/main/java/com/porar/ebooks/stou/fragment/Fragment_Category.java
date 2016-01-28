package com.porar.ebooks.stou.fragment;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_List_Category;
import com.porar.ebook.adapter.Adapter_List_Ebook;
import com.porar.ebook.control.Category_SubCate;
import com.porar.ebook.control.Category_SubCate_Ebookslist;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Fragment_Category extends Fragment {

	private Animation fade_in;
	Bundle bundle;
	public FragmentActivity myActivity;
	public static String tag = "Fragment_Category";

	// public boolean isphone = false;
	// :: version.phone :: Object
	ListView plistview;
	ImageView picon_back, img_logo;
	Adapter_List_Category adapter_List_Category;
	public static String bundle_ebook_type_phone = "bundle_ebook_type_phone";
	ArrayList<Model_Categories> arr_Categories;
	ArrayList<Model_Categories> arr_SubCategories;
	Category_SubCate category_SubCate;
	Category_SubCate_Ebookslist category_SubCate_Ebookslist;
	Adapter_List_Ebook adapter_List_Ebook;
	public static Fragment_Category category;
	TextView txt_title;
	String titlename = "";
	String subtitlename = "";
	String SubCategory = "SubCategory";
	String Category = "Category";
	String ListView = "ListView";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(tag, "onCreate");

		if (AppMain.isphone) {
			Log.e(tag, "onCreate isphone");
			bundle = new Bundle();
			myActivity = getActivity();
			category = this;
		} else {
			bundle = new Bundle();
			myActivity = getActivity();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(tag, "onCreateView");
		View view = inflater.inflate(R.layout.activity_category, container, false);
		return view;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(tag, "onActivityCreated");

		// RelativeLayout layout_main = (RelativeLayout) getActivity().findViewById(R.id.category_rt_main_phone);
		if (AppMain.isphone) {

			setId_Phone();
			if (bundle.getString(bundle_ebook_type_phone) != null) {
				if (bundle.getString(bundle_ebook_type_phone).equals(Category)) {
					setListViewCategory(AppMain.pList_categories, null);
				}
				if (bundle.getString(bundle_ebook_type_phone).equals(SubCategory)) {
					setListView_SubCategory(AppMain.pList_Subcategories, null);
				}
				if (bundle.getString(bundle_ebook_type_phone).equals(ListView)) {
					setListView(AppMain.pList_Subcategories_ebook, null);
				}
				//stou
//				if (bundle.getString(bundle_ebook_type_phone).equals(Category)) {
//					setListViewCategory(AppMain.pList_categories2, null);
//				}
				//end
			} else {
				setListViewCategory(AppMain.pList_categories, null);
//				setListViewCategory(AppMain.pList_categories2, null);
			}

		} else {

		}

	}

	public void setListViewCategory(PList pList, ProgressDialog dialog) {

		bundle.putString(bundle_ebook_type_phone, Category);

		arr_Categories = new ArrayList<Model_Categories>();

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		try {
			img_logo.setVisibility(View.VISIBLE);
			picon_back.setVisibility(View.INVISIBLE);
			txt_title.setVisibility(View.INVISIBLE);
			for (PListObject each : (Array) pList.getRootElement()) {
				arr_Categories.add(new Model_Categories(each));
			}
			adapter_List_Category = null;
			adapter_List_Category = new Adapter_List_Category(myActivity, arr_Categories);
			plistview.setAdapter(adapter_List_Category);

			plistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					loadApiBySubCate(arr_Categories.get(position).getCatID());
					titlename = arr_Categories.get(position).getName();
				}
			});
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		} catch (NullPointerException e) {
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	}

	public void setListView_SubCategory(PList pList, ProgressDialog dialog) {

		bundle.putString(bundle_ebook_type_phone, SubCategory);
		arr_SubCategories = new ArrayList<Model_Categories>();

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		try {
			img_logo.setVisibility(View.INVISIBLE);
			picon_back.setVisibility(View.VISIBLE);
			txt_title.setVisibility(View.VISIBLE);
			txt_title.setText("" + titlename);
			for (PListObject each : (Array) pList.getRootElement()) {
				arr_SubCategories.add(new Model_Categories(each));
			}
			adapter_List_Category = null;
			adapter_List_Category = new Adapter_List_Category(myActivity, arr_SubCategories);
			plistview.setAdapter(adapter_List_Category);

			plistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					loadApiBySubCateTypeEbook(arr_SubCategories.get(position).getCatID(),
							arr_SubCategories.get(position).getSCatID());
					subtitlename = arr_SubCategories.get(position).getName();

				}
			});
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		} catch (NullPointerException e) {
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	}

	private void loadApiBySubCateTypeEbook(int CatID, int SCatID) {
		category_SubCate_Ebookslist = new Category_SubCate_Ebookslist(myActivity, CatID, SCatID);
		category_SubCate_Ebookslist.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void StartLoadPList() {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList_TopPeriod(PList Plist1, PList Plist2, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList_Detail_Comment(PList Plistdetail, PList Plistcomment, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {
				setListView(resultPlist, pd);
			}
		});
		category_SubCate_Ebookslist.LoadCategoryAPI();
	}

	private void setListView(PList plist, ProgressDialog dialog) {

		bundle.putString(bundle_ebook_type_phone, ListView);
		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
		}
		try {

			img_logo.setVisibility(View.INVISIBLE);
			picon_back.setVisibility(View.VISIBLE);
			txt_title.setVisibility(View.VISIBLE);
			txt_title.setText("" + subtitlename);

			Log.i(tag, "plist " + plist.toString());

			adapter_List_Ebook = null;
			adapter_List_Ebook = new Adapter_List_Ebook(myActivity, 0, plist);
			plistview.setAdapter(adapter_List_Ebook);

			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		} catch (NullPointerException e) {
			Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}
	}

	private void loadApiBySubCate(int CatID) {
		category_SubCate = new Category_SubCate(myActivity, CatID);
		category_SubCate.setOnListener(new Throw_IntefacePlist() {

			@Override
			public void PList(PList resultPlist, ProgressDialog pd) {
				setListView_SubCategory(resultPlist, pd);

			}

			@Override
			public void PList_Detail_Comment(plist.xml.PList Plistdetail, plist.xml.PList Plistcomment, ProgressDialog pd) {
				// TODO Auto-generated method stub
			}

			@Override
			public void StartLoadPList() {
				// TODO Auto-generated method stub

			}

			@Override
			public void PList_TopPeriod(plist.xml.PList Plist1, plist.xml.PList Plist2, ProgressDialog pd) {
				// TODO Auto-generated method stub

			}
		});
		category_SubCate.LoadCategoryAPI();
	}

	private void setId_Phone() {
		txt_title = (TextView) getActivity().findViewById(R.id.categorty_title);
		txt_title.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
		img_logo = (ImageView) getActivity().findViewById(R.id.catrgory_image_logo);
		picon_back = (ImageView) getActivity().findViewById(R.id.category_image_back);
		plistview = (ListView) getActivity().findViewById(R.id.category_listView1);

		picon_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (AppMain.pList_Subcategories != null && AppMain.pList_Subcategories_ebook != null) {
					setListView_SubCategory(AppMain.pList_Subcategories, null);
					AppMain.pList_Subcategories_ebook = null;

					return;
				}
				if (AppMain.pList_Subcategories != null) {
					setListViewCategory(AppMain.pList_categories, null);
					AppMain.pList_Subcategories = null;

					return;
				}

			}
		});
	}

	@Override
	public void onPause() {

		Log.e(tag, "onPause");
		System.gc();
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		Log.e(tag, "onDestroyView");

		super.onDestroyView();
	}

	@Override
	public void onResume() {

		Log.e(tag, "onResume");
		super.onResume();
	}

}
