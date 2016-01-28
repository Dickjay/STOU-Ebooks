package com.porar.ebooks.stou.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_BookShelf;
import com.porar.ebook.control.Dialog_ShowLongClick;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.helper.OnStartDragListener;
import com.porar.ebook.helper.SimpleItemTouchHelperCallback;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.Page_Image_Slide;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.widget.FlexibleDividerDecoration;
import com.porar.ebooks.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 9/30/2015.
 */
public class Fragment_AllEbooks extends Fragment implements OnStartDragListener, FlexibleDividerDecoration.VisibilityProvider {
    public static Fragment_AllEbooks fragment_allEbooks;

    public static ArrayList<Model_EBook_Shelf_List> array_ebook_shelf_list;
    public Model_EBook_Shelf_List model_ebook_shelf_list;

    // for search ebooksID in shelf
    public static ArrayList<Model_EBook_Shelf_List> array_ebook_shelf_list_search;

    // for offline mode
    public static ArrayList<Model_EBook_Shelf_List> arrayListoffline;

    // for deleted ebooksID
    public static ArrayList<Integer> array_deleted_ebooksID;

    // sort ebooksID
    public static ArrayList<String> array_sort_ebooksID;
    public static ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID;

    public static ProgressDialog progressDialog;

    public static RecyclerView mRecyclerView;
    public static GridLayoutManager gridLayoutManager;
    public static Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf;

    public static ItemTouchHelper.Callback callback;
    public static ItemTouchHelper mItemTouchHelper;
    public boolean isAddDecoration = false;

    public static Handler handler = new Handler();
    public Dialog_ShowLongClick dialog_ShowLongClick;

    public static Fragment newInstance() {
        fragment_allEbooks = new Fragment_AllEbooks();
        return fragment_allEbooks;
    }

    public static void clearItem() {
        if (array_ebook_shelf_list_search != null) {
            array_ebook_shelf_list_search.clear();
        }
        if (arrayListoffline != null) {
            arrayListoffline.clear();
        }
        if (array_deleted_ebooksID != null) {
            array_deleted_ebooksID.clear();
        }
        if (array_ebook_shelf_list != null) {
            array_ebook_shelf_list.clear();
        }
        if(adapter_recyclerView_bookShelf!=null){
            adapter_recyclerView_bookShelf.notifyDataSetChanged();
        }
    }

    public static Fragment newInstance(ProgressDialog progressDialog) {
        fragment_allEbooks = new Fragment_AllEbooks();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        return fragment_allEbooks;
    }

    public static Fragment newInstance(ArrayList<Model_EBook_Shelf_List> arrayOffline) {
        Fragment_AllEbooks fragment_allEbooks = new Fragment_AllEbooks();
        arrayListoffline = arrayOffline;
        return fragment_allEbooks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Shared_Object.checkInternetConnection(getActivity())) {
            if (arrayListoffline != null && arrayListoffline.size() > 0) {
                setItemOffline();
            }
        } else {
            LoadEbooksDetailAPI("A");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Fragment_Shelf_Tablet.searchEbooks) {
            Fragment_Shelf_Tablet.searchEbooks = false;
            array_ebook_shelf_list_search = null;
            LoadEbooksDetailAPI("A");
        }
    }

    private void setItemOffline() {
        setListAdapterAllBook(arrayListoffline);
        int spanCount = getResources().getInteger(R.integer.grid_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        addItemDecoration();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (mItemTouchHelper != null)
            mItemTouchHelper.startDrag(viewHolder);
    }

    public void addItemDecoration() {
        if (!isAddDecoration) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    isAddDecoration = true;
                    mRecyclerView.setHasFixedSize(true);
                    gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_columns));
                    mRecyclerView.setLayoutManager(gridLayoutManager);

                    if (Shared_Object.getCustomerDetail.getBechalorDegree() > 0) {
                        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                                .drawable(R.drawable.shelf_glass2x_l)
                                .size(30).visibilityProvider(adapter_recyclerView_bookShelf)
                                .build());
                    } else if (Shared_Object.getCustomerDetail.getMasterDegree() > 0) {
                        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                                .drawable(R.drawable.shelf_blue2x_l)
                                .size(30).visibilityProvider(adapter_recyclerView_bookShelf)
                                .build());
                        mRecyclerView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_light_green));
                    } else {
                        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                                .drawable(R.drawable.shelf_default)
                                .size(30).visibilityProvider(adapter_recyclerView_bookShelf)
                                .build());
                    }
                }
            });


        }

    }

    public void LoadEbooksDetailAPI(String typeEbook) {
        String cid = "" + Shared_Object.getCustomerDetail.getCID(); // customer id
        String email = "" + Shared_Object.getCustomerDetail.getEmail(); // customer id
        String password = "" + Shared_Object.getCustomerDetail.getPassword(); // customer id
        String did = Shared_Object.getDeviceID(getActivity());

        handler.post(new Runnable() {

            @Override
            public void run() {
                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
                        true, false);

            }
        });
        AsyncTask_FetchAPI asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {

            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("onFetchStart", apiURL);

            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {

            }

            @Override
            public void onFetchComplete(final String apiURL, int currentIndex,
                                        final PList result) {
                if (apiURL.contains("update-bookshelf")) {
                }

                if (apiURL.contains("bookshelf-list")) {
                    array_ebook_shelf_list = new ArrayList<Model_EBook_Shelf_List>();
                    for (PListObject each : (Array) result
                            .getRootElement()) {
                        model_ebook_shelf_list = new Model_EBook_Shelf_List(each);
                        array_ebook_shelf_list.add(model_ebook_shelf_list);
                    }
                }
                if (apiURL.contains("customer-detail")) {
                    try {
                        Array plistObject = (Array) result.getRootElement();
                        Model_Customer_Detail modelCustomerDetail = new Model_Customer_Detail(plistObject);
                        if (Shared_Object.getCustomerDetail.getCID() > 0) {
                            Shared_Object.getCustomerDetail.seteBooks(modelCustomerDetail.geteBooks());
                            Shared_Object.getCustomerDetail.setFavorites(modelCustomerDetail.getFavorites());
                            Shared_Object.getCustomerDetail.setTotal(modelCustomerDetail.getTotal());
                            Class_Manage.SaveEbooksObject(getActivity(),
                                    Shared_Object.getCustomerDetail,
                                    "customer_detail.porar");
                        }

                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onAllTaskDone() {
                if (array_ebook_shelf_list != null && array_ebook_shelf_list.size() > 0) {
                    array_ebook_shelf_list_search = array_ebook_shelf_list;
                    setListAdapterAllBook(array_ebook_shelf_list);
                    final int spanCount = getResources().getInteger(R.integer.grid_columns);
                    final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
                    mRecyclerView.setLayoutManager(layoutManager);
                    if (AppMain.isphone) {
                        if (Fragment_Shelf_Phone.isDragAndDrop) {

                            eneableDragDrop(true);
                        } else {

                            eneableDragDrop(false);
                        }
                    } else {
                        if (Fragment_Shelf_Tablet.isDragAndDrop) {

                            eneableDragDrop(true);
                        } else {

                            eneableDragDrop(false);
                        }
                    }

                    addItemDecoration();
                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                }
                dismissDialog();
            }

            public void dismissDialog() {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
        if (array_ebook_shelf_list != null && array_ebook_shelf_list.size() > 0) {
            array_ebook_shelf_list.clear();
        }
        asyncTask_FetchAPI.execute(
                AppMain.getAPIbyRefKey("customer-detail", "cid=" + cid),
                AppMain.getAPIbyRefKey("update-bookshelf", "cid=" + cid
                        + "&did=" + did),
                AppMain.getAPIbyRefKey("bookshelf-list", "email=" + email
                        + "&password=" + password + "&type=" + typeEbook
                        + "&retina=0"));

    }

    public static void eneableDragDrop(boolean isDragAndDrop) {
        if (isDragAndDrop) {
            callback = new SimpleItemTouchHelperCallback(adapter_recyclerView_bookShelf);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        } else {
            callback = null;
            if (mItemTouchHelper != null) {
                mItemTouchHelper.attachToRecyclerView(null);
            }
            mItemTouchHelper = null;
        }

    }

    public void setListAdapterAllBook(ArrayList<Model_EBook_Shelf_List> array_ebook_shelf_list) {

        adapter_recyclerView_bookShelf = new Adapter_RecyclerView_BookShelf(getActivity(), array_ebook_shelf_list, this, Fragment_AllEbooks.this) {
            @Override
            public void OnClickCover(Model_EBook_Shelf_List model) {
                Intent myIntent = new Intent(getActivity(), Page_Image_Slide.class);
                myIntent.putExtra("bid", model.getBID());
                myIntent.putExtra("model", model);
                myIntent.putExtra("customer", Shared_Object.getCustomerDetail);

                Fragment_AllEbooks.this.startActivityForResult(myIntent, 2013);
                getActivity().overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }


            @Override
            public void getAllItem(ArrayList<Model_EBook_Shelf_List> m_array_ebook_shelf_list) {
                if (array_sort_ebooksID == null) {
                    array_sort_ebooksID = new ArrayList<>();
                }
                arraylist_shelf_ebooksID = m_array_ebook_shelf_list;
                adapter_recyclerView_bookShelf.notifyDataSetChanged();
            }

            @Override
            public void getBookID(ArrayList<Integer> arrayList, int position) {
                array_deleted_ebooksID = arrayList;
            }

            @Override
            public void OnLongClickCover(Model_EBook_Shelf_List model) {
                dialog_ShowLongClick = new Dialog_ShowLongClick(getActivity(),
                        R.style.PauseDialog, model, getActivity());
                dialog_ShowLongClick
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dia) {
                            }
                        });

                LoadDetailDialog detailDialog = new LoadDetailDialog();
                detailDialog.newInstant(model.getBID());
                detailDialog.LoadDatadetail();
            }
        };
        mRecyclerView.setAdapter(adapter_recyclerView_bookShelf);

    }


    // After LongClick Display Deatil Book
    public class LoadDetailDialog {
        int model_EbookList;
        Ebook_Detail ebook_DetailApi;
        AlertDialog alertDialog;
        Model_Ebooks_Detail ebooks_Detail = null;

        public void newInstant(int bid) {
            model_EbookList = bid;

        }

        public void LoadDatadetail() {

            // DerializeObject
            Model_Ebooks_Detail DSebooks_Detail = Class_Manage
                    .getModel_DetailByNever(getActivity().getApplicationContext(),
                            model_EbookList);
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success" + model_EbookList);
                // intent
                dialog_ShowLongClick.setDetail(DSebooks_Detail, 1);
                dialog_ShowLongClick.show();
            } else {
                ebook_DetailApi = new Ebook_Detail(getActivity(),
                        String.valueOf(model_EbookList));
                ebook_DetailApi.setOnListener(new Throw_IntefacePlist() {

                    @Override
                    public void PList_Detail_Comment(
                            plist.xml.PList Plistdetail,
                            plist.xml.PList Plistcomment,
                            final ProgressDialog pd) {

                    }

                    @Override
                    public void PList(plist.xml.PList resultPlist,
                                      final ProgressDialog pd) {

                        try {
                            for (PListObject each : (Array) resultPlist
                                    .getRootElement()) {
                                ebooks_Detail = new Model_Ebooks_Detail(each);
                            }

                            // SerializeObject
                            ebooks_Detail.setDateTime();
                            if (Class_Manage.SaveModel_Detail(
                                    getActivity().getApplicationContext(),
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());

                            }

                            // intent

                            dialog_ShowLongClick.setDetail(ebooks_Detail, 0);
                            dialog_ShowLongClick.show();

                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                        } catch (NullPointerException e) {
                            // refresh
                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }

                            alertDialog = new AlertDialog.Builder(getActivity())
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog
                                    .setMessage("WARNING: An error has occurred. Please to try again ?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    "Retry",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            pd.show();
                                            dialog.dismiss();
                                            System.gc();
                                            ebook_DetailApi
                                                    .LoadEbooksDetailAPI();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();
                                            System.gc();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }

                    @Override
                    public void StartLoadPList() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void PList_TopPeriod(plist.xml.PList Plist1,
                                                plist.xml.PList Plist2, ProgressDialog pd) {
                        // TODO Auto-generated method stub

                    }
                });
                ebook_DetailApi.LoadEbooksDetailAPI();
            }
        }

    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        if (AppMain.isphone) {
            if (Fragment_Shelf_Phone.isDragAndDrop) {
                return true;
            }
        } else {
            if (Fragment_Shelf_Tablet.isDragAndDrop) {
                return true;
            }
        }

        return false;

    }

}
