package com.porar.ebook.adapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_EBook_Shelf_List.StatusFlag;
import com.porar.ebooks.model.Model_Restore;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.AppMain.ScreenSize;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

public abstract class MyAdapterViewPager_BookShelf extends PagerAdapter
		implements Serializable {

	ArrayList<Model_Restore> arrayListRestores;
	static final long serialVersionUID = 1L;
	Activity context;
	int limit = 16;
	private int index = 0;
	ArrayList<ArrayList<Model_EBook_Shelf_List>> arrList;
	ArrayList<Model_EBook_Shelf_List> arrinside;
	ArrayList<ArrayList<Model_Restore>> arrList_restore;
	ArrayList<Model_Restore> arrinside_restore;
	ArrayList<String> arrBid;
	PList pList;
	private int sizePage = 0;
	int view_index = 0;

	LruCache<String, byte[]> mMemoryCache;
	private static String Edittextsearch = "";
	private boolean isDeleteBook = false;
	public static Shelf shelf = Shelf.news;

	public static enum Shelf {
		news, favorite, tresh
	};

	public static void seteNumtype(Shelf object) {
		shelf = object;
	}

	public int getSizePage() {
		return sizePage;
	}

	public static String getEdittextsearch() {
		return Edittextsearch;
	}

	public static void setEdittextsearch(String edittextsearch) {
		Edittextsearch = edittextsearch;
	}

	public MyAdapterViewPager_BookShelf(Activity context,
			ArrayList<Model_Restore> arrayListRestores) {

		this.context = context;
		this.arrayListRestores = arrayListRestores;

		if (arrayListRestores.size() <= 0) {
			sizePage = arrayListRestores.size();
		} else {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			int memoryClass = am.getMemoryClass();
			int cacheSize = 1024 * 1024 * memoryClass / 8;
			Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
			Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);
			mMemoryCache = new LruCache<String, byte[]>(cacheSize);
			switch (shelf) {
			case favorite:
				break;
			case news:
				break;
			case tresh:
				arrList_restore = new ArrayList<ArrayList<Model_Restore>>();
				for (Model_Restore each : arrayListRestores) {
					if (index == 0) {
						arrinside_restore = new ArrayList<Model_Restore>();
					}
					arrinside_restore.add(new Model_Restore(each.getBID(), each
							.getName(), each.getCoverFileNameS()));
					index++;
					if (index >= limit) {
						index = 0;
						arrList_restore.add(arrinside_restore);
					}
				}
				if (arrinside_restore.size() != 0
						&& arrinside_restore.size() < limit) {
					arrList_restore.add(arrinside_restore);
				}
				sizePage = arrList_restore.size();
				break;

			}

		}

	}

	public MyAdapterViewPager_BookShelf(Activity context, boolean offline,
			ArrayList<Model_EBook_Shelf_List> arrayList) {
		this.context = context;

		arrList = new ArrayList<ArrayList<Model_EBook_Shelf_List>>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

		// ArrayList<Model_EBook_Shelf_List> arrayList =
		// Class_Manage.getEbooksShelfInOffLineMode(context);

		if (arrayList.size() <= 0) {
			sizePage = arrayList.size();
		} else {
			switch (shelf) {
			case favorite:
				for (Model_EBook_Shelf_List each : arrayList) {
					if (index == 0) {
						arrinside = new ArrayList<Model_EBook_Shelf_List>();
					}
					if (each.getStatusFlag().equals(StatusFlag.Favourite)) {
						arrinside.add(each);
						index++;
					}

					if (index >= limit) {
						index = 0;
						arrList.add(arrinside);
					}
				}
				if (arrinside.size() != 0 && arrinside.size() < limit) {
					arrList.add(arrinside);
				}
				sizePage = arrList.size();
				break;
			case news:
				for (Model_EBook_Shelf_List each : arrayList) {
					if (index == 0) {
						arrinside = new ArrayList<Model_EBook_Shelf_List>();
					}

					if (each.getStatusFlag().equals(StatusFlag.New)) {
						arrinside.add(each);
						index++;
					}
					if (each.getStatusFlag().equals(StatusFlag.Ordinary)) {
						arrinside.add(each);
						index++;
					}

					if (index >= limit) {
						index = 0;
						arrList.add(arrinside);
					}
				}
				if (arrinside.size() != 0 && arrinside.size() < limit) {
					arrList.add(arrinside);
				}
				sizePage = arrList.size();
				break;
			case tresh:
				break;

			}
		}

	}

	public MyAdapterViewPager_BookShelf(Activity context, PList pList) {

		this.context = context;
		this.pList = pList;

		arrList = new ArrayList<ArrayList<Model_EBook_Shelf_List>>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);

		arrBid = new ArrayList<String>();

		Array arr_check = (Array) this.pList.getRootElement();
		if ((arr_check.size() <= 0)) {
			// size 0
			sizePage = arr_check.size();
		} else {
			switch (shelf) {
			case favorite:
				if (StaticUtils.arrShilf.size() != 0) {
					for (int i = 0; i < StaticUtils.arrShilf.size(); i++) {
						if (index == 0) {
							arrinside = new ArrayList<Model_EBook_Shelf_List>();
						}

						if (StaticUtils.getModelShelf(i).getStatusFlag()
								.equals(StatusFlag.Favourite)) {

							if (arrBid.contains(String.valueOf(StaticUtils
									.getModelShelf(i)))) {
								break;
							}
							if (!arrinside.contains(StaticUtils
									.getModelShelf(i))) {
								arrinside.add(StaticUtils.getModelShelf(i));
								index++;
								arrBid.add(String.valueOf(StaticUtils
										.getModelShelf(i)));
							}

							if (!arrBid.contains(String.valueOf(StaticUtils
									.getModelShelf(i)))) {
								if (arrinside.size() <= 0) {
									arrinside.add(StaticUtils.getModelShelf(i));
									index++;
								}
							}
							if (index >= limit) {
								index = 0;
								arrList.add(arrinside);

							}
						}
					}

				}
				for (PListObject each : (Array) this.pList.getRootElement()) {
					if (index == 0) {
						arrinside = new ArrayList<Model_EBook_Shelf_List>();
					}
					if (new Model_EBook_Shelf_List(each).getStatusFlag()
							.equals(StatusFlag.Favourite)) {
						arrinside.add(new Model_EBook_Shelf_List(each));
						index++;
					}
					if (StaticUtils.arrShilf.size() != 0) {
						for (int i = 0; i < StaticUtils.arrShilf.size(); i++) {
							if (StaticUtils.getModelShelf(i).getStatusFlag()
									.equals(StatusFlag.New)) {
								if (arrinside.size() > 0) {
									for (int j = 0; j < arrinside.size(); j++) {
										if (arrinside.get(j).getBID() == StaticUtils
												.getModelShelf(i).getBID()) {
											arrinside.remove(j);
											--index;
										}
									}
								}
							}
						}
					}

					if (index >= limit) {
						index = 0;
						arrList.add(arrinside);
					}
				}

				if (arrinside.size() != 0 && arrinside.size() < limit) {
					arrList.add(arrinside);
				}
				sizePage = arrList.size();
				break;
			case news:
				if (StaticUtils.arrShilf.size() != 0) {
					for (int i = 0; i < StaticUtils.arrShilf.size(); i++) {
						if (index == 0) {
							arrinside = new ArrayList<Model_EBook_Shelf_List>();
						}

						if (StaticUtils.getModelShelf(i).getStatusFlag()
								.equals(StatusFlag.New)) {

							if (arrBid.contains(String.valueOf(StaticUtils
									.getModelShelf(i)))) {
								break;
							}
							if (!arrinside.contains(StaticUtils
									.getModelShelf(i))) {
								arrinside.add(StaticUtils.getModelShelf(i));
								index++;
								arrBid.add(String.valueOf(StaticUtils
										.getModelShelf(i)));
							}

							if (!arrBid.contains(String.valueOf(StaticUtils
									.getModelShelf(i)))) {
								if (arrinside.size() <= 0) {
									arrinside.add(StaticUtils.getModelShelf(i));
									index++;
								}
							}
							if (index >= limit) {
								index = 0;
								arrList.add(arrinside);

							}
						}

					}
				}
				for (PListObject each : (Array) this.pList.getRootElement()) {
					if (index == 0) {
						arrinside = new ArrayList<Model_EBook_Shelf_List>();
					}

					if (new Model_EBook_Shelf_List(each).getStatusFlag()
							.equals(StatusFlag.New)) {
						arrinside.add(new Model_EBook_Shelf_List(each));
						index++;
					}
					if (new Model_EBook_Shelf_List(each).getStatusFlag()
							.equals(StatusFlag.Ordinary)) {
						arrinside.add(new Model_EBook_Shelf_List(each));
						index++;
					}
					if (StaticUtils.arrShilf.size() != 0) {
						for (int i = 0; i < StaticUtils.arrShilf.size(); i++) {
							if (StaticUtils.getModelShelf(i).getStatusFlag()
									.equals(StatusFlag.Favourite)) {
								if (arrinside.size() > 0) {
									for (int j = 0; j < arrinside.size(); j++) {
										if (arrinside.get(j).getBID() == StaticUtils
												.getModelShelf(i).getBID()) {
											arrinside.remove(j);
											--index;
										}
									}
								}
							}
						}
					}

					if (index >= limit) {
						index = 0;
						arrList.add(arrinside);
					}
				}

				if (arrinside.size() != 0 && arrinside.size() < limit) {
					arrList.add(arrinside);
				}
				sizePage = arrList.size();
				break;
			case tresh:
				break;

			}
		}
	}

	@Override
	public void destroyItem(final View view, final int position,
			final Object object) {
		((ViewPager) view).removeView((LinearLayout) object);

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
		Log.e("", "instantiateItem " + position);
		view_index = position;

		if (sizePage == 0) {
			myView = LayoutInflater.from(context).inflate(
					R.layout.search_item_ebook_noresult, null);
			TextView txtName = (TextView) myView
					.findViewById(R.id.txt_noresult);
			txtName.setTypeface(StaticUtils.getTypeface(context,
					Font.THSarabanNew));
			txtName.setTextSize(context.getResources().getDimension(
					R.dimen.normal));
			((ViewPager) view).addView(myView);

		} else {

			if (arrayListRestores != null) {
				// table view
				myView = LayoutInflater.from(context).inflate(
						R.layout.layout_tableview_ebook, null);
				((ViewPager) view).addView(myView);
				myView.setTag("" + myView.getId());

				for (int j = 0; j < arrList_restore.get(view_index).size(); j++) {

					img_covers[j] = (ImageView) myView
							.findViewById(id_cover[j]);
					img_shadow[j] = (ImageView) myView
							.findViewById(id_shadow[j]);

					if (Edittextsearch.length() > 0) {
						if (!arrList_restore.get(view_index).get(j).getName()
								.toLowerCase()
								.contains(Edittextsearch.toLowerCase())) {
							img_covers[j].setVisibility(View.INVISIBLE);
							img_shadow[j].setVisibility(View.INVISIBLE);
							rt_view[j].setVisibility(View.INVISIBLE);
						}
					}

					Picasso.with(context)
							.load(AppMain.COVER_URL
									+ arrList_restore.get(view_index).get(j)
											.getBID()
									+ "/"
									+ arrList_restore.get(view_index).get(j)
											.getCoverFileNameS())
							.into(img_covers[j]);
					img_shadow[j].setVisibility(View.VISIBLE);

					Log.e("restore",
							AppMain.COVER_URL
									+ arrList_restore.get(view_index).get(j)
											.getBID()
									+ "/"
									+ arrList_restore.get(view_index).get(j)
											.getCoverFileNameS());
					if (isDeleteBook) {
						img_shadow[j].bringToFront();
						img_shadow[j].setImageResource(R.drawable.icon_delete);
						img_shadow[j].setScaleType(ScaleType.CENTER_INSIDE);

						OnClickDelete clickDelete = new OnClickDelete();
						clickDelete.newInstant(
								img_covers[j],
								Integer.parseInt(arrList_restore
										.get(view_index).get(j).getBID()));
						img_shadow[j].setOnClickListener(clickDelete);
					} else {
						img_shadow[j].bringToFront();
						enableBookshelf enaBookshelf = new enableBookshelf();
						enaBookshelf.newInstant(
								img_covers[j],
								Integer.parseInt(arrList_restore
										.get(view_index).get(j).getBID()));
						img_shadow[j].setOnClickListener(enaBookshelf);
					}
				}

				if (arrList_restore.get(view_index).size() < limit) {
					int over = limit - arrList_restore.get(view_index).size();
					Log.e("over", "" + over);
					for (int i = limit - 1; i > arrList_restore.get(view_index)
							.size() - 1; i--) {
						img_covers[i] = (ImageView) myView
								.findViewById(id_cover[i]);
						img_shadow[i] = (ImageView) myView
								.findViewById(id_shadow[i]);
						rt_view[i] = (RelativeLayout) myView
								.findViewById(id_rt[i]);
						img_covers[i].setVisibility(View.INVISIBLE);
						img_shadow[i].setVisibility(View.INVISIBLE);
						rt_view[i].setVisibility(View.INVISIBLE);
					}
				}

			} else {
				myView = LayoutInflater.from(context).inflate(
						R.layout.layout_tableview_ebook, null);
				((ViewPager) view).addView(myView);
				myView.setTag("" + myView.getId());

				for (int j = 0; j < arrList.get(view_index).size(); j++) {

					img_covers[j] = (ImageView) myView
							.findViewById(id_cover[j]);
					img_shadow[j] = (ImageView) myView
							.findViewById(id_shadow[j]);
					rt_view[j] = (RelativeLayout) myView.findViewById(id_rt[j]);

					if (Edittextsearch.length() > 0) {
						if (!arrList.get(view_index).get(j).getName()
								.toLowerCase()
								.contains(Edittextsearch.toLowerCase())) {
							img_covers[j].setVisibility(View.INVISIBLE);
							img_shadow[j].setVisibility(View.INVISIBLE);
							rt_view[j].setVisibility(View.INVISIBLE);
						}
					}

					Picasso.with(context)
							.load(AppMain.COVER_URL
									+ arrList.get(view_index).get(j).getBID()
									+ "/"
									+ arrList.get(view_index).get(j).getCover())
							.into(img_covers[j]);
					img_shadow[j].setVisibility(View.VISIBLE);

					if (isDeleteBook) {
						img_shadow[j].bringToFront();
						img_shadow[j].setImageResource(R.drawable.icon_delete);
						img_shadow[j].setScaleType(ScaleType.CENTER_INSIDE);

						OnClickDelete clickDelete = new OnClickDelete();
						clickDelete.newInstant(img_covers[j],
								arrList.get(view_index).get(j).getBID());
						img_shadow[j].setOnClickListener(clickDelete);
					} else {

						if (checkEbooksCache(context, arrList.get(view_index)
								.get(j))) {
							img_covers[j].bringToFront();
						} else {
							img_shadow[j].bringToFront();
						}
						if (StaticUtils.p_bid.size() != 0) {
							for (int i = 0; i < arrList.get(position).size(); i++) {
								int pageindex = StaticUtils.getPageIndex(String
										.valueOf(arrList.get(position).get(i)
												.getBID()));
								if (pageindex != 0) {
									arrList.get(position).get(i)
											.setPointer(pageindex);
									arrList.get(position).get(i)
											.setReadingFlag(true);
								}
							}
						}

						// bookmarkgreen
						if (arrList.get(view_index).get(j).getPointer() == arrList
								.get(view_index).get(j).getPages()) {
							ImageView bookmarkgreen = new ImageView(context);

							bookmarkgreen.setVisibility(View.VISIBLE);
							if (AppMain.screenSize == ScreenSize.Normal) {
								bookmarkgreen
										.setImageResource(R.drawable.icon_bookmark_greensmall);
							} else {
								bookmarkgreen
										.setImageResource(R.drawable.icon_bookmark_green);
							}
							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							params.addRule(RelativeLayout.ALIGN_LEFT,
									img_covers[j].getId());
							params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
							rt_view[j].addView(bookmarkgreen, params);

							// normal 30x40
						} else {
							// bookmarkred
							if (arrList.get(view_index).get(j).getReadingFlag()
									|| arrList.get(view_index).get(j)
											.getPointer() == 1) {
								ImageView bookmarkred = new ImageView(context);
								if (AppMain.screenSize == ScreenSize.Normal) {
									bookmarkred
											.setImageResource(R.drawable.icon_bookmark_redsmall);
								} else {
									bookmarkred
											.setImageResource(R.drawable.icon_bookmark_red);
								}

								bookmarkred.setVisibility(View.VISIBLE);
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_LEFT,
										img_covers[j].getId());
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
								rt_view[j].addView(bookmarkred, params);

								TextView readstatus = new TextView(context); // textpointer
								readstatus.setVisibility(View.VISIBLE);
								readstatus.setTextColor(Color.WHITE);
								readstatus.setSingleLine();
								// readstatus.setTextSize(20F);
								readstatus.setTextSize(Float.parseFloat(context
										.getResources().getString(
												R.string.size_pointer)));
								readstatus.setText(" "
										+ arrList.get(view_index).get(j)
												.getPointer());
								readstatus.setTypeface(StaticUtils.getTypeface(
										context, Font.LayijiMahaniyomV105ot));
								RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params2.addRule(RelativeLayout.ALIGN_LEFT,
										img_covers[j].getId());
								params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
								rt_view[j].addView(readstatus, params2);
							} else {
								// Never been opened.
							}
						}

						OnClickIntent_PageImage_silde intent = new OnClickIntent_PageImage_silde();
						intent.newInstant(img_covers[j], arrList
								.get(view_index).get(j));
						img_covers[j].setOnClickListener(intent);

						OnClickLong_PageImage_silde clickLong = new OnClickLong_PageImage_silde();
						clickLong.newInstant(img_covers[j],
								arrList.get(view_index).get(j));
						img_covers[j].setOnLongClickListener(clickLong);
					}

				}

				if (arrList.get(view_index).size() < limit) {
					int over = limit - arrList.get(view_index).size();
					Log.e("over", "" + over);
					for (int i = limit - 1; i > arrList.get(view_index).size() - 1; i--) {

						rt_view[i] = (RelativeLayout) myView
								.findViewById(id_rt[i]);
						img_covers[i] = (ImageView) myView
								.findViewById(id_cover[i]);
						img_shadow[i] = (ImageView) myView
								.findViewById(id_shadow[i]);
						img_covers[i].setVisibility(View.INVISIBLE);
						img_shadow[i].setVisibility(View.INVISIBLE);
						rt_view[i].setVisibility(View.INVISIBLE);

					}
				}

			}

		}

		return myView;

	}

	public abstract void OnClickCover(Model_EBook_Shelf_List model);

	public abstract void OnLongClickCover(Model_EBook_Shelf_List model);

	private boolean checkEbooksCache(Context context,
			Model_EBook_Shelf_List model) {
		File file = new File(context.getFilesDir(), "ebooks_" + model.getBID()
				+ ".porar");
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public class OnClickLong_PageImage_silde implements OnLongClickListener {
		Model_EBook_Shelf_List model;
		ImageView img_cover;

		public void newInstant(ImageView imgcover, Model_EBook_Shelf_List object) {
			model = object;
			img_cover = imgcover;
		}

		@Override
		public boolean onLongClick(View v) {
			OnLongClickCover(model);

			return true;
		}

	}

	public class OnClickIntent_PageImage_silde implements OnClickListener {
		Model_EBook_Shelf_List model;
		ImageView img_cover;

		public void newInstant(ImageView imgcover, Model_EBook_Shelf_List object) {
			model = object;
			img_cover = imgcover;
		}

		@Override
		public void onClick(View v) {
			OnClickCover(model);

		}

	}

	public class enableBookshelf implements OnClickListener {
		int bid;
		String url = "";
		AlertDialog alertDialog;
		ImageView image_cover;

		public void newInstant(ImageView imgcover, int object) {
			bid = object;
			image_cover = imgcover;
			// url = "http://api.ebooks.in.th/enable-bookshelf.ashx?";
			url = AppMain.ENABLE_BOOK_SHELF_URL;
			url += "bid=" + object;
			url += "&cid=" + Shared_Object.getCustomerDetail.getCID();
		}

		@Override
		public void onClick(final View v) {

			alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle(AppMain.getTag());
			alertDialog.setMessage("Picked out of the trash?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "confirm",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							System.gc();

							LoadAPIResultString apiResultString = new LoadAPIResultString();
							apiResultString
									.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

										@Override
										public void completeResult(String result) {
											if (result != null) {
												if (result.contains("1")) {
													Toast.makeText(context,
															"complete restore",
															Toast.LENGTH_SHORT).show();
													Toast.makeText(context,
															"please refresh",
															Toast.LENGTH_SHORT).show();
													image_cover
															.setVisibility(View.INVISIBLE);
													v.setVisibility(View.INVISIBLE);
												}
											}
										}
									});
							apiResultString.execute(url);
						}
					});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							System.gc();
						}
					});
			alertDialog.show();

		}
	}

	clickDelete delete;
	ArrayList<Integer> arrayListDelete;

	public void setDeleteItem(clickDelete delete) {
		this.delete = delete;
		this.arrayListDelete = new ArrayList<Integer>();
	}

	public interface clickDelete {
		void deleteBook(ArrayList<Integer> arrayListDelete);
	}

	public class OnClickDelete implements OnClickListener {
		int bid;
		ImageView img_cover;

		public void newInstant(ImageView imgcover, int object) {
			bid = object;
			img_cover = imgcover;
		}

		@Override
		public void onClick(View v) {
			arrayListDelete.add(bid);
			delete.deleteBook(arrayListDelete);
			img_cover.setVisibility(View.INVISIBLE);
			v.setVisibility(View.INVISIBLE);
		}

	}

	static View myView = null;
	static int id_cover[] = { R.id.img_ebook_cover1_tableRow1,
			R.id.img_ebook_cover2_tableRow1, R.id.img_ebook_cover3_tableRow1,
			R.id.img_ebook_cover4_tableRow1, R.id.img_ebook_cover1_tableRow2,
			R.id.img_ebook_cover2_tableRow2, R.id.img_ebook_cover3_tableRow2,
			R.id.img_ebook_cover4_tableRow2, R.id.img_ebook_cover1_tableRow3,
			R.id.img_ebook_cover2_tableRow3, R.id.img_ebook_cover3_tableRow3,
			R.id.img_ebook_cover4_tableRow3, R.id.img_ebook_cover1_tableRow4,
			R.id.img_ebook_cover2_tableRow4, R.id.img_ebook_cover3_tableRow4,
			R.id.img_ebook_cover4_tableRow4 };

	static int id_shadow[] = { R.id.img_ebook_shadow1_tableRow1,
			R.id.img_ebook_shadow2_tableRow1, R.id.img_ebook_shadow3_tableRow1,
			R.id.img_ebook_shadow4_tableRow1, R.id.img_ebook_shadow1_tableRow2,
			R.id.img_ebook_shadow2_tableRow2, R.id.img_ebook_shadow3_tableRow2,
			R.id.img_ebook_shadow4_tableRow2, R.id.img_ebook_shadow1_tableRow3,
			R.id.img_ebook_shadow2_tableRow3, R.id.img_ebook_shadow3_tableRow3,
			R.id.img_ebook_shadow4_tableRow3, R.id.img_ebook_shadow1_tableRow4,
			R.id.img_ebook_shadow2_tableRow4, R.id.img_ebook_shadow3_tableRow4,
			R.id.img_ebook_shadow4_tableRow4 };

	static ImageView img_cover1, img_cover2, img_cover3, img_cover4,
			img_cover5, img_cover6, img_cover7, img_cover8, img_cover9,
			img_cover10, img_cover11, img_cover12, img_cover13, img_cover14,
			img_cover15, img_cover16;
	static ImageView img_shadow1, img_shadow2, img_shadow3, img_shadow4,
			img_shadow5, img_shadow6, img_shadow7, img_shadow8, img_shadow9,
			img_shadow10, img_shadow11, img_shadow12, img_shadow13,
			img_shadow14, img_shadow15, img_shadow16;
	static ImageView img_covers[] = { img_cover1, img_cover2, img_cover3,
			img_cover4, img_cover5, img_cover6, img_cover7, img_cover8,
			img_cover9, img_cover10, img_cover11, img_cover12, img_cover13,
			img_cover14, img_cover15, img_cover16 };
	static ImageView img_shadow[] = { img_shadow1, img_shadow2, img_shadow3,
			img_shadow4, img_shadow5, img_shadow6, img_shadow7, img_shadow8,
			img_shadow9, img_shadow10, img_shadow11, img_shadow12,
			img_shadow13, img_shadow14, img_shadow15, img_shadow16 };

	static int[] id_rt = { R.id.img_ebook_imag1_tableRow1,
			R.id.img_ebook_imag2_tableRow1, R.id.img_ebook_imag3_tableRow1,
			R.id.img_ebook_imag4_tableRow1, R.id.img_ebook_imag1_tableRow2,
			R.id.img_ebook_imag2_tableRow2, R.id.img_ebook_imag3_tableRow2,
			R.id.img_ebook_imag4_tableRow2, R.id.img_ebook_imag1_tableRow3,
			R.id.img_ebook_imag2_tableRow3, R.id.img_ebook_imag3_tableRow3,
			R.id.img_ebook_imag4_tableRow3, R.id.img_ebook_imag1_tableRow4,
			R.id.img_ebook_imag2_tableRow4, R.id.img_ebook_imag3_tableRow4,
			R.id.img_ebook_imag4_tableRow4 };

	static RelativeLayout rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8, rt9, rt10,
			rt11, rt12, rt13, rt14, rt15, rt16;
	static RelativeLayout[] rt_view = { rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8,
			rt9, rt10, rt11, rt12, rt13, rt14, rt15, rt16 };

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

	public boolean isDeleteBook() {
		return isDeleteBook;
	}

	public void setDeleteBook(boolean isDeleteBook) {
		this.isDeleteBook = isDeleteBook;
	}

}
