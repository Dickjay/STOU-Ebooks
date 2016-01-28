package com.porar.ebook.adapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public abstract class Adapter_List_BookShelf extends ArrayAdapter<String>
		implements Serializable {

	private static final long serialVersionUID = 1L;
	ArrayList<ArrayList<Model_EBook_Shelf_List>> arrList;
	ArrayList<Model_EBook_Shelf_List> arrinside;
	ArrayList<ArrayList<Model_Restore>> arrList_restore;
	ArrayList<Model_Restore> arrayListRestores;
	ArrayList<Model_Restore> arrinside_restore;
	ArrayList<String> arrBid;
	Context context;
	LayoutInflater inflater;
	ImageDownloader_forCache downloader_forCache;
	LruCache<String, byte[]> mMemoryCache;
	public static ShelfPhone shelf = ShelfPhone.news;
	PList pList;
	int sizePage = 0;
	private boolean isDeleteBook = false;

	public static enum ShelfPhone {
		news, favorite, tresh
	};

	public static void seteNumtypePhone(ShelfPhone object) {
		shelf = object;
	}

	int index = 0;
	int limit = 3;
	public int id_cover[] = { R.id.img_shelf_pcover1, R.id.img_shelf_pcover2,
			R.id.img_shelf_pcover3 };
	public int id_shadow[] = { R.id.img_shelf_pshadow1,
			R.id.img_shelf_pshadow2, R.id.img_shelf_pshadow3 };
	public int id_relative[] = { R.id.pshelf_Image1, R.id.pshelf_Image2,
			R.id.pshelf_Image3 };
	public int id_relativedummy[] = { R.id.pshelf_Image1_dummy,
			R.id.pshelf_Image2_dummy, R.id.pshelf_Image3_dummy };

	public int id_bookmark_red[] = { R.id.img_shelf_bookmark_red1,
			R.id.img_shelf_bookmark_red2, R.id.img_shelf_bookmark_red3 };
	public int id_bookmark_green[] = { R.id.img_shelf_bookmark_green1,
			R.id.img_shelf_bookmark_green2, R.id.img_shelf_bookmark_green3 };
	public int id_bookmark_txt[] = { R.id.img_shelf_txt_bookmark1,
			R.id.img_shelf_txt_bookmark2, R.id.img_shelf_txt_bookmark3 };

	// private final ArrayList<String> arrayBIDFlag = new ArrayList<String>();

	public Adapter_List_BookShelf(Context context, int textViewResourceId,
			ArrayList<Model_Restore> arrayListRestores) {
		super(context, textViewResourceId);
		this.context = context;
		this.arrayListRestores = arrayListRestores;

		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;

		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
		if (arrayListRestores.size() <= 0) {
			sizePage = arrayListRestores.size();
		} else {

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

	public Adapter_List_BookShelf(Context context, int textViewResourceId,
			PList plist) {
		super(context, textViewResourceId);
		this.context = context;

		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
		arrList = new ArrayList<ArrayList<Model_EBook_Shelf_List>>();

		arrBid = new ArrayList<String>();

		Array arr_check = (Array) this.pList.getRootElement();
		if (arr_check.size() <= 0) {
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
			int i = 0;
			for (ArrayList<Model_EBook_Shelf_List> each : arrList) {

				Log.e("", "arrList[i][] " + i);
				for (Model_EBook_Shelf_List each2 : each) {

					Log.i("", "arrList[][j] count " + each2.getName());
				}
				i++;
			}

		}

	}

	public Adapter_List_BookShelf(Context context, int textViewResourceId,
			ArrayList<Model_EBook_Shelf_List> arrOffline, boolean offline) {
		super(context, textViewResourceId);
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downloader_forCache = new ImageDownloader_forCache();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memoryClass / 8;
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
		Log.v("onCreate", "1/8 of memoryClass" + memoryClass / 8);
		mMemoryCache = new LruCache<String, byte[]>(cacheSize);
		arrList = new ArrayList<ArrayList<Model_EBook_Shelf_List>>();

		if (arrOffline.size() <= 0) {
			sizePage = arrOffline.size();
		} else {
			switch (shelf) {
			case favorite:
				for (Model_EBook_Shelf_List each : arrOffline) {
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
				for (Model_EBook_Shelf_List each : arrOffline) {
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

	@Override
	public int getCount() {
		if (sizePage != 0) {
			return sizePage;
		} else {
			return 1;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (sizePage == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.search_item_ebook_noresult, null);
				TextView txtName = (TextView) convertView
						.findViewById(R.id.txt_noresult);
			}
			return convertView;
		} else {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview_item_bookshelf, null);
				if (arrList_restore != null) {
					for (int j = 0; j < limit; j++) {
						viewHolder.arrimg_cover[j] = (ImageView) convertView
								.findViewById(id_cover[j]);
						viewHolder.arrimg_shadow[j] = (ImageView) convertView
								.findViewById(id_shadow[j]);
						viewHolder.arr_rt[j] = (RelativeLayout) convertView
								.findViewById(id_relative[j]);
						viewHolder.arr_rtdummy[j] = (RelativeLayout) convertView
								.findViewById(id_relativedummy[j]);
						viewHolder.pshelf_linearmain = (LinearLayout) convertView
								.findViewById(R.id.pshelf_linearmain);
						viewHolder.bookmarkred[j] = (ImageView) convertView
								.findViewById(id_bookmark_red[j]);
						viewHolder.bookmarkgreen[j] = (ImageView) convertView
								.findViewById(id_bookmark_green[j]);
						viewHolder.readstatus[j] = (TextView) convertView
								.findViewById(id_bookmark_txt[j]);
					}
				} else {
					for (int j = 0; j < limit; j++) {
						viewHolder.arrimg_cover[j] = (ImageView) convertView
								.findViewById(id_cover[j]);
						viewHolder.arrimg_shadow[j] = (ImageView) convertView
								.findViewById(id_shadow[j]);
						viewHolder.arr_rt[j] = (RelativeLayout) convertView
								.findViewById(id_relative[j]);
						viewHolder.arr_rtdummy[j] = (RelativeLayout) convertView
								.findViewById(id_relativedummy[j]);
						viewHolder.pshelf_linearmain = (LinearLayout) convertView
								.findViewById(R.id.pshelf_linearmain);
						viewHolder.bookmarkred[j] = (ImageView) convertView
								.findViewById(id_bookmark_red[j]);
						viewHolder.bookmarkgreen[j] = (ImageView) convertView
								.findViewById(id_bookmark_green[j]);
						viewHolder.readstatus[j] = (TextView) convertView
								.findViewById(id_bookmark_txt[j]);
					}

				}
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (arrList_restore != null) {
				if (arrList_restore.get(position).size() < limit) {
					for (int i = limit - 1; i > arrList_restore.get(position)
							.size() - 1; i--) {
						try {
							viewHolder.arrimg_cover[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.arrimg_shadow[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.arr_rt[i].setVisibility(View.INVISIBLE);
							viewHolder.arr_rtdummy[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.bookmarkred[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.bookmarkgreen[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.readstatus[i]
									.setVisibility(View.INVISIBLE);
							Log.e("", "i" + i);
						} catch (NullPointerException e) {

						}
					}
					for (int ix = 0; ix < arrList_restore.get(position).size(); ix++) {
						viewHolder.arrimg_cover[ix].setVisibility(View.VISIBLE);
						viewHolder.arrimg_shadow[ix]
								.setVisibility(View.VISIBLE);
						viewHolder.arr_rt[ix].setVisibility(View.VISIBLE);
						viewHolder.arr_rtdummy[ix]
								.setVisibility(View.INVISIBLE);
						viewHolder.bookmarkred[ix]
								.setVisibility(View.INVISIBLE);
						viewHolder.bookmarkgreen[ix]
								.setVisibility(View.INVISIBLE);
						viewHolder.readstatus[ix].setVisibility(View.INVISIBLE);
						Log.e("", "ix" + ix);

						if (isDeleteBook) {
							viewHolder.arr_rt[ix].setVisibility(View.VISIBLE);
							viewHolder.arrimg_cover[ix]
									.setVisibility(View.VISIBLE);
							viewHolder.arr_rtdummy[ix]
									.setVisibility(View.INVISIBLE);
							viewHolder.arrimg_shadow[ix].bringToFront();
							viewHolder.arrimg_shadow[ix]
									.setImageResource(R.drawable.icon_delete);
							viewHolder.arrimg_shadow[ix]
									.setScaleType(ScaleType.CENTER_INSIDE);

							if (arrayListDelete.size() != 0) {
								for (int j = 0; j < arrayListDelete.size(); j++) {
									if (arrayListDelete.get(j) == arrList
											.get(position).get(ix).getBID()) {
										viewHolder.arr_rt[ix]
												.setVisibility(View.INVISIBLE);
									}
								}
							}

							OnClickDelete clickDelete = new OnClickDelete();
							clickDelete.newInstant(
									viewHolder.arr_rt[ix],
									Integer.parseInt(arrList_restore
											.get(position).get(ix).getBID()));
							viewHolder.arrimg_shadow[ix]
									.setOnClickListener(clickDelete);
						} else {
							viewHolder.arrimg_shadow[ix].bringToFront();
							enableBookshelf enaBookshelf = new enableBookshelf();
							enaBookshelf.newInstant(
									viewHolder.arrimg_cover[ix],
									Integer.parseInt(arrList_restore
											.get(position).get(ix).getBID()));
							viewHolder.arrimg_shadow[ix]
									.setOnClickListener(enaBookshelf);
						}

					}
				} else {
					for (int i = 0; i < arrList_restore.get(position).size(); i++) {
						viewHolder.arrimg_cover[i].setVisibility(View.VISIBLE);
						viewHolder.arrimg_shadow[i].setVisibility(View.VISIBLE);
						viewHolder.arr_rt[i].setVisibility(View.VISIBLE);
						viewHolder.arr_rtdummy[i].setVisibility(View.INVISIBLE);
						viewHolder.bookmarkred[i].setVisibility(View.INVISIBLE);
						viewHolder.bookmarkgreen[i]
								.setVisibility(View.INVISIBLE);
						viewHolder.readstatus[i].setVisibility(View.INVISIBLE);
						Log.e("", "ix" + i);

						if (isDeleteBook) {
							viewHolder.arr_rt[i].setVisibility(View.VISIBLE);
							viewHolder.arrimg_cover[i]
									.setVisibility(View.VISIBLE);
							viewHolder.arr_rtdummy[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.arrimg_shadow[i].bringToFront();
							viewHolder.arrimg_shadow[i]
									.setImageResource(R.drawable.icon_delete);
							viewHolder.arrimg_shadow[i]
									.setScaleType(ScaleType.CENTER_INSIDE);

							if (arrayListDelete.size() != 0) {
								for (int j = 0; j < arrayListDelete.size(); j++) {
									if (arrayListDelete.get(j) == arrList
											.get(position).get(i).getBID()) {
										viewHolder.arr_rt[i]
												.setVisibility(View.INVISIBLE);
									}
								}
							}

							OnClickDelete clickDelete = new OnClickDelete();
							clickDelete.newInstant(
									viewHolder.arr_rt[i],
									Integer.parseInt(arrList_restore
											.get(position).get(i).getBID()));
							viewHolder.arrimg_shadow[i]
									.setOnClickListener(clickDelete);
						} else {
							viewHolder.arrimg_shadow[i].bringToFront();
							enableBookshelf enaBookshelf = new enableBookshelf();
							enaBookshelf.newInstant(
									viewHolder.arrimg_cover[i],
									Integer.parseInt(arrList_restore
											.get(position).get(i).getBID()));
							viewHolder.arrimg_shadow[i]
									.setOnClickListener(enaBookshelf);
						}
					}

				}
				for (int j = 0; j < arrList_restore.get(position).size(); j++) {
					downloader_forCache.download(AppMain.COVER_URL
							+ arrList_restore.get(position).get(j).getBID()
							+ "/"
							+ arrList_restore.get(position).get(j)
									.getCoverFileNameS(),
							viewHolder.arrimg_cover[j],
							viewHolder.arrimg_shadow[j], true);
				}

			} else {
				if (arrList.get(position).size() < limit) {
					for (int i = limit - 1; i > arrList.get(position).size() - 1; i--) {
						try {
							viewHolder.arr_rt[i].setVisibility(View.INVISIBLE);

						} catch (NullPointerException e) {

						}

					}
					for (int ix = 0; ix < arrList.get(position).size(); ix++) {
						viewHolder.arr_rt[ix].setVisibility(View.VISIBLE);

						OnClickIntent_PageImage_silde intent = new OnClickIntent_PageImage_silde();
						intent.newInstant(viewHolder.arrimg_cover[ix], arrList
								.get(position).get(ix));
						viewHolder.arrimg_cover[ix].setOnClickListener(intent);

						OnClickLong_PageImage_silde clickLong = new OnClickLong_PageImage_silde();
						clickLong.newInstant(viewHolder.arrimg_cover[ix],
								arrList.get(position).get(ix));
						viewHolder.arrimg_cover[ix]
								.setOnLongClickListener(clickLong);

						if (isDeleteBook) {

							viewHolder.arr_rt[ix].setVisibility(View.VISIBLE);
							viewHolder.arr_rtdummy[ix]
									.setVisibility(View.INVISIBLE);
							viewHolder.arrimg_shadow[ix].bringToFront();
							viewHolder.arrimg_shadow[ix]
									.setImageResource(R.drawable.icon_delete);
							viewHolder.arrimg_shadow[ix]
									.setScaleType(ScaleType.CENTER_INSIDE);

							if (arrayListDelete.size() != 0) {
								for (int j = 0; j < arrayListDelete.size(); j++) {
									if (arrayListDelete.get(j) == arrList
											.get(position).get(ix).getBID()) {
										viewHolder.arr_rt[ix]
												.setVisibility(View.INVISIBLE);
									}
								}
							}
							OnClickDelete clickDelete = new OnClickDelete();
							clickDelete.newInstant(viewHolder.arr_rt[ix],
									arrList.get(position).get(ix).getBID());
							viewHolder.arrimg_shadow[ix]
									.setOnClickListener(clickDelete);
						} else {
							if (checkEbooksCache(context, arrList.get(position)
									.get(ix))) {
								viewHolder.arrimg_cover[ix].bringToFront();
								viewHolder.arr_rtdummy[ix].bringToFront();
							} else {
								viewHolder.arrimg_shadow[ix].bringToFront();

							}

							if (StaticUtils.p_bid.size() != 0) {
								for (int i2 = 0; i2 < arrList.get(position)
										.size(); i2++) {
									int pageindex = StaticUtils
											.getPageIndex(String
													.valueOf(arrList
															.get(position)
															.get(i2).getBID()));
									if (pageindex != 0) {
										arrList.get(position).get(i2)
												.setPointer(pageindex);
										arrList.get(position).get(i2)
												.setReadingFlag(true);
									}

								}
							}

							if (arrList.get(position).get(ix).getPointer() == arrList
									.get(position).get(ix).getPages()) {

								viewHolder.bookmarkgreen[ix]
										.setVisibility(View.VISIBLE);
								viewHolder.bookmarkred[ix]
										.setVisibility(View.INVISIBLE);
								viewHolder.readstatus[ix]
										.setVisibility(View.INVISIBLE);

								if (AppMain.screenSize == ScreenSize.Normal) {
									viewHolder.bookmarkgreen[ix]
											.setImageResource(R.drawable.icon_bookmark_greensmall);
								} else {
									viewHolder.bookmarkgreen[ix]
											.setImageResource(R.drawable.icon_bookmark_green);
								}

								// normal 30x40
							} else {
								// bookmarkred
								if (arrList.get(position).get(ix)
										.getReadingFlag()
										|| arrList.get(position).get(ix)
												.getPointer() == 1) {

									if (AppMain.screenSize == ScreenSize.Normal) {
										viewHolder.bookmarkred[ix]
												.setImageResource(R.drawable.icon_bookmark_redsmall);
									} else {
										viewHolder.bookmarkred[ix]
												.setImageResource(R.drawable.icon_bookmark_red);
									}
									viewHolder.bookmarkgreen[ix]
											.setVisibility(View.INVISIBLE);
									viewHolder.bookmarkred[ix]
											.setVisibility(View.VISIBLE);
									viewHolder.readstatus[ix]
											.setVisibility(View.VISIBLE);

									viewHolder.readstatus[ix]
											.setTextColor(Color.WHITE);
									viewHolder.readstatus[ix].setSingleLine();
									//
									viewHolder.readstatus[ix]
											.setTextSize(Float
													.parseFloat(context
															.getResources()
															.getString(
																	R.string.size_pointer)));
									viewHolder.readstatus[ix].setText(" "
											+ arrList.get(position).get(ix)
													.getPointer());
									viewHolder.readstatus[ix]
											.setTypeface(StaticUtils
													.getTypeface(
															context,
															Font.LayijiMahaniyomV105ot));

								} else {
									// Never been opened.
									if (AppMain.screenSize == ScreenSize.Normal) {
										viewHolder.bookmarkred[ix]
												.setImageResource(R.drawable.icon_bookmark_redsmall);
									} else {
										viewHolder.bookmarkred[ix]
												.setImageResource(R.drawable.icon_bookmark_red);
									}
									viewHolder.bookmarkgreen[ix]
											.setVisibility(View.INVISIBLE);
									viewHolder.bookmarkred[ix]
											.setVisibility(View.VISIBLE);
									viewHolder.readstatus[ix]
											.setVisibility(View.VISIBLE);

									viewHolder.readstatus[ix]
											.setTextColor(Color.WHITE);
									viewHolder.readstatus[ix].setSingleLine();
									//
									viewHolder.readstatus[ix]
											.setTextSize(Float
													.parseFloat(context
															.getResources()
															.getString(
																	R.string.size_pointer)));
									viewHolder.readstatus[ix].setText(" "
											+ arrList.get(position).get(ix)
													.getPointer());
									viewHolder.readstatus[ix]
											.setTypeface(StaticUtils
													.getTypeface(
															context,
															Font.LayijiMahaniyomV105ot));
								}
							}

						}
					}
				} else {
					for (int i = 0; i < arrList.get(position).size(); i++) {

						viewHolder.arr_rtdummy[i].setVisibility(View.VISIBLE);
						viewHolder.arr_rt[i].setVisibility(View.VISIBLE);

						OnClickIntent_PageImage_silde intent = new OnClickIntent_PageImage_silde();
						intent.newInstant(viewHolder.arrimg_cover[i], arrList
								.get(position).get(i));
						viewHolder.arrimg_cover[i].setOnClickListener(intent);

						OnClickLong_PageImage_silde clickLong = new OnClickLong_PageImage_silde();
						clickLong.newInstant(viewHolder.arrimg_cover[i],
								arrList.get(position).get(i));
						viewHolder.arrimg_cover[i]
								.setOnLongClickListener(clickLong);

						if (isDeleteBook) {

							viewHolder.arr_rt[i].setVisibility(View.VISIBLE);
							viewHolder.arr_rtdummy[i]
									.setVisibility(View.INVISIBLE);
							viewHolder.arrimg_shadow[i].bringToFront();
							viewHolder.arrimg_shadow[i]
									.setImageResource(R.drawable.icon_delete);
							viewHolder.arrimg_shadow[i]
									.setScaleType(ScaleType.CENTER_INSIDE);
							if (arrayListDelete.size() != 0) {
								for (int j = 0; j < arrayListDelete.size(); j++) {
									if (arrayListDelete.get(j) == arrList
											.get(position).get(i).getBID()) {
										viewHolder.arr_rt[i]
												.setVisibility(View.INVISIBLE);
									}
								}
							}
							OnClickDelete clickDelete = new OnClickDelete();
							clickDelete.newInstant(viewHolder.arr_rt[i],
									arrList.get(position).get(i).getBID());
							viewHolder.arrimg_shadow[i]
									.setOnClickListener(clickDelete);
						} else {
							if (checkEbooksCache(context, arrList.get(position)
									.get(i))) {
								viewHolder.arrimg_cover[i].bringToFront();
								viewHolder.arr_rtdummy[i].bringToFront();
							} else {
								viewHolder.arrimg_shadow[i].bringToFront();
							}

							if (StaticUtils.p_bid.size() != 0) {
								for (int i2 = 0; i2 < arrList.get(position)
										.size(); i2++) {
									int pageindex = StaticUtils
											.getPageIndex(String
													.valueOf(arrList
															.get(position)
															.get(i).getBID()));
									if (pageindex != 0) {
										arrList.get(position).get(i)
												.setPointer(pageindex);
										arrList.get(position).get(i)
												.setReadingFlag(true);
									}
								}
							}

							if (arrList.get(position).get(i).getPointer() == arrList
									.get(position).get(i).getPages()) {

								viewHolder.bookmarkgreen[i]
										.setVisibility(View.VISIBLE);
								viewHolder.bookmarkred[i]
										.setVisibility(View.INVISIBLE);
								viewHolder.readstatus[i]
										.setVisibility(View.INVISIBLE);

								if (AppMain.screenSize == ScreenSize.Normal) {
									viewHolder.bookmarkgreen[i]
											.setImageResource(R.drawable.icon_bookmark_greensmall);
								} else {
									viewHolder.bookmarkgreen[i]
											.setImageResource(R.drawable.icon_bookmark_green);
								}

								// normal 30x40
							} else {

								// bookmarkred
								if (arrList.get(position).get(i)
										.getReadingFlag()
										|| arrList.get(position).get(i)
												.getPointer() == 1) {

									viewHolder.bookmarkgreen[i]
											.setVisibility(View.INVISIBLE);
									viewHolder.bookmarkred[i]
											.setVisibility(View.VISIBLE);
									viewHolder.readstatus[i]
											.setVisibility(View.VISIBLE);

									if (AppMain.screenSize == ScreenSize.Normal) {
										viewHolder.bookmarkred[i]
												.setImageResource(R.drawable.icon_bookmark_redsmall);
									} else {
										viewHolder.bookmarkred[i]
												.setImageResource(R.drawable.icon_bookmark_red);
									}

									// textpointer

									viewHolder.readstatus[i]
											.setTextColor(Color.WHITE);
									viewHolder.readstatus[i].setSingleLine();
									viewHolder.readstatus[i]
											.setTextSize(Float
													.parseFloat(context
															.getResources()
															.getString(
																	R.string.size_pointer)));
									viewHolder.readstatus[i].setText(" "
											+ arrList.get(position).get(i)
													.getPointer());
									viewHolder.readstatus[i]
											.setTypeface(StaticUtils
													.getTypeface(
															context,
															Font.LayijiMahaniyomV105ot));

								} else {
									// Never been opened.
									viewHolder.bookmarkgreen[i]
											.setVisibility(View.INVISIBLE);
									viewHolder.bookmarkred[i]
											.setVisibility(View.VISIBLE);
									viewHolder.readstatus[i]
											.setVisibility(View.VISIBLE);

									if (AppMain.screenSize == ScreenSize.Normal) {
										viewHolder.bookmarkred[i]
												.setImageResource(R.drawable.icon_bookmark_redsmall);
									} else {
										viewHolder.bookmarkred[i]
												.setImageResource(R.drawable.icon_bookmark_red);
									}

									// textpointer

									viewHolder.readstatus[i]
											.setTextColor(Color.WHITE);
									viewHolder.readstatus[i].setSingleLine();
									viewHolder.readstatus[i]
											.setTextSize(Float
													.parseFloat(context
															.getResources()
															.getString(
																	R.string.size_pointer)));
									viewHolder.readstatus[i].setText(" "
											+ arrList.get(position).get(i)
													.getPointer());
									viewHolder.readstatus[i]
											.setTypeface(StaticUtils
													.getTypeface(
															context,
															Font.LayijiMahaniyomV105ot));
								}
							}

						}
					}
				}
				for (int j = 0; j < arrList.get(position).size(); j++) {
					String urlcover = AppMain.COVER_URL
							+ arrList.get(position).get(j).getBID() + "/"
							+ arrList.get(position).get(j).getCover();
					downloader_forCache.download(urlcover,
							viewHolder.arrimg_cover[j],
							viewHolder.arrimg_shadow[j], true);

				}
			}

			return convertView;
		}

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
										public void completeResult(
												String result) {
											if (result != null) {
												if (result.contains("1")) {
													Toast.makeText(context,
															"complete restore",
															200).show();
													Toast.makeText(context,
															"please refresh",
															200).show();
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

	clickDeleteP delete;
	ArrayList<Integer> arrayListDelete;

	public void setDeleteItem(clickDeleteP delete) {
		this.delete = delete;
		this.arrayListDelete = new ArrayList<Integer>();
	}

	public interface clickDeleteP {
		void deleteBook(ArrayList<Integer> arrayListDelete);
	}

	public class OnClickDelete implements OnClickListener {
		int bid;
		View img_cover;

		public void newInstant(View imgcover, int object) {
			bid = object;
			img_cover = imgcover;
		}

		@Override
		public void onClick(View v) {
			arrayListDelete.add(bid);
			delete.deleteBook(arrayListDelete);
			img_cover.setVisibility(View.INVISIBLE);
		}

	}

	public ImageView image_cover1, image_cover2, image_cover3;
	public ImageView image_shadow1, image_shadow2, image_shadow3;
	public RelativeLayout rt1, rt2, rt3;
	public RelativeLayout rt1dummy, rt2dummy, rt3dummy;
	public TextView readstatus1, readstatus2, readstatus3;
	public ImageView bookmarkred1, bookmarkred2, bookmarkred3;
	public ImageView bookmarkgreen1, bookmarkgreen2, bookmarkgreen3;

	class ViewHolder {
		public TextView readstatus[] = { readstatus1, readstatus2, readstatus3 };
		public ImageView bookmarkred[] = { bookmarkred1, bookmarkred2,
				bookmarkred3 };
		public ImageView bookmarkgreen[] = { bookmarkgreen1, bookmarkgreen2,
				bookmarkgreen3 };
		public ImageView arrimg_cover[] = { image_cover1, image_cover2,
				image_cover3 };
		public ImageView arrimg_shadow[] = { image_shadow1, image_shadow2,
				image_shadow3 };
		public RelativeLayout arr_rt[] = { rt1, rt2, rt3 };
		public RelativeLayout arr_rtdummy[] = { rt1dummy, rt2dummy, rt3dummy };
		public LinearLayout pshelf_linearmain;
	}

	public boolean isDeleteBook() {
		return isDeleteBook;
	}

	public void setDeleteBook(boolean isDeleteBook) {
		this.isDeleteBook = isDeleteBook;
	}

	public int getSizePage() {
		// TODO Auto-generated method stub
		return sizePage;
	}
}
