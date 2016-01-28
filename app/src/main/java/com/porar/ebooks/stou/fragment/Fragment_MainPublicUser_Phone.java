package com.porar.ebooks.stou.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_ShowEbook;
import com.porar.ebook.control.Dialog_SearchEbooks;
import com.porar.ebook.control.Dialog_Select_Mode;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;
import com.porar.ebooks.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;
import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/8/2015.
 */
public class Fragment_MainPublicUser_Phone extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private AdjustableImageView hamburger_imageview, search_imageview, back_imageview;
    public static LinearLayoutManager gridLayoutManager;

    private SegmentedGroup segment_mode;
    private RadioButton radio_d, radio_e;

    private static boolean isLoad = false;
    private boolean isLiterature = false;
    private boolean isExercise = false;

    public static Model_Ebooks_Public_User model_ebook_see_all;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebook_see_all;

    private Adapter_RecyclerView_ShowEbook adapter_recyclerView_showEbook;
    private RecyclerView recyclerViewList;

    public SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    AlertDialog alertDialog;
    Model_Ebooks_Detail ebooks_Detail = null;
    Ebook_Detail ebook_DetailApi;

    public static boolean onLoadShelf = false;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 7;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;

    private int currentPageL = 1;
    private int currentPageM = 1;
    private boolean limitLoadMore = false;

    private PList resultPlist;
    private boolean onPause = false;

    public static Fragment newInstance() {
        Fragment_MainPublicUser_Phone fragment_mainPublicUser_phone = new Fragment_MainPublicUser_Phone();
        if (isLoad) {
            isLoad = false;
        }
        return fragment_mainPublicUser_phone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_public_user_phone, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSwipeRefresh(view);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isLoad) {
            isLoad = true;
            radio_d.setChecked(true);
            isLiterature = true;
            isExercise = false;
        }
    }

    private void setupSwipeRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout = StaticUtils.setColorSwipeReresh(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLiterature) {
                            downloadEbooks("L");
                        } else if (isExercise) {
                            downloadEbooks("M");
                        } else {
                            downloadEbooks("L");
                        }
                    }
                }, 2000);

            }
        });
    }

    private void setRecyclerView(View view) {
        recyclerViewList = (RecyclerView) view.findViewById(R.id.recyclerview_list);
        recyclerViewList.setHasFixedSize(true);
        gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewList.setLayoutManager(gridLayoutManager);
        recyclerViewList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerViewList.addOnScrollListener(onScrollListener);
    }

    private void initView(View view) {
        hamburger_imageview = (AdjustableImageView) view.findViewById(R.id.hamburger_imageview);
        search_imageview = (AdjustableImageView) view.findViewById(R.id.search_imageview);
        back_imageview = (AdjustableImageView) view.findViewById(R.id.back_imageview);

        segment_mode = (SegmentedGroup) view.findViewById(R.id.segment_mode);
        segment_mode.setOnCheckedChangeListener(this);

        radio_d = (RadioButton) view.findViewById(R.id.radio_button_document);
        radio_e = (RadioButton) view.findViewById(R.id.radio_button_exercise);

        radio_d.setText(getActivity().getResources().getString(R.string.literature));
        radio_e.setText(getActivity().getResources().getString(R.string.lesson));

        radio_d.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_e.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));

        setRecyclerView(view);


        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        hamburger_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Select_Mode dialog_select_mode = new Dialog_Select_Mode(getActivity(), R.style.FadeInOutDialog, Fragment_MainPublicUser_Phone.this);
                dialog_select_mode.show();
            }
        });


        search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_SearchEbooks(getActivity(), R.style.FadeInOutDialog, Fragment_MainPublicUser_Phone.this).show();
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        onPause = false;
    }

    public void downloadEbooks(String type) {
        if (getActivity() == null) {
            return;
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                //                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
//                        true, false);
                swipeRefreshLayout.setRefreshing(true);

            }
        });

        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {

            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("downloadEbooks", "URL = " + apiURL);
                limitLoadMore = false;
                loading = false;
                lastVisibleItem = 0;
                totalItemCount = 0;
                currentPageL = 1;
                currentPageM = 1;
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {

            }

            @Override
            public void onFetchComplete(final String apiURL, int currentIndex,
                                        final PList result) {
                if (apiURL.contains("get_ebook_list")) {
                    array_model_ebook_see_all = new ArrayList<Model_Ebooks_Public_User>();
                    for (PListObject each : (Array) result.getRootElement()) {
                        model_ebook_see_all = new Model_Ebooks_Public_User(each);
                        array_model_ebook_see_all.add(model_ebook_see_all);
                    }


                }
            }

            @Override
            public void onAllTaskDone() {
                Log.e("onAllTaskDone", "onAllTaskDone");

                if (array_model_ebook_see_all != null && array_model_ebook_see_all.size() > 0) {
                    setAdapter();
                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                    recyclerViewList.setAdapter(null);
                }
                closeProgress();
            }

        });

        executeData(asyncTask_FetchAPI, type);
    }

    private void executeData(AsyncTask_FetchAPI fetchAPI, String type) {
        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + type + "&page=1&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=0&mode=o&scatid=0"));
    }

    private void setAdapter() {
        adapter_recyclerView_showEbook = new Adapter_RecyclerView_ShowEbook(getActivity(), array_model_ebook_see_all, Fragment_MainPublicUser_Phone.this) {
            @Override
            public void gotoDetail(ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users, int pos) {
//                Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(getActivity(), array_model_ebook_see_all.get(pos).getBID());
//                if (DSebooks_Detail != null) {
//                    Log.v("OnClickToDetailEbook", "DeserializeObject Success"
//                            + array_model_ebook_see_all.get(pos).getBID());
//                    Intent intent = new Intent(getActivity(), Activity_Detail.class);
//                    intent.putExtra("model", DSebooks_Detail);
//                    intent.putExtra("DeserializeObject", "1");
//                    StaticUtils.phoneValue = 1;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    getActivity().startActivity(intent);
//                    ((Activity) getActivity()).overridePendingTransition(
//                            R.anim.slide_in_right, R.anim.slide_out_left);
//
//                } else {
                ebook_DetailApi = new Ebook_Detail(getActivity(),
                        String.valueOf(array_model_ebook_see_all.get(pos).getBID()));
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
                            if (Class_Manage.SaveModel_Detail(getActivity(),
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());
                            }

                            Intent intent = new Intent(getActivity(),
                                    Activity_Detail.class);
                            intent.putExtra("model", ebooks_Detail);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            ((Activity) getActivity()).overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left);

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
                                    .setMessage("WARNING: An error has ocurred. Please to try again ?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    "Retry",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            pd.show();
                                            System.out.println("Retry");
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
//                }
            }

            @Override
            public void gotoUnit(String bCode) {

            }
        };
        recyclerViewList.setAdapter(adapter_recyclerView_showEbook);

    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (array_model_ebook_see_all.size() < 0) {
                return;
            } else {
                if (!limitLoadMore) {
                    if (!loading) {
                        totalItemCount = gridLayoutManager.getItemCount();
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            loading = true;
                            //add null , so the adapter will check view_type and show progress bar at bottom
//                            addProgressBarLoadMore();
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
                            if (isLiterature) {
                                onLoadMore("L");
                            } else if (isExercise) {
                                onLoadMore("M");
                            } else {
                                onLoadMore("L");
                            }
//                                }
//                            }, 800);

                        }
                    }
                }

            }

        }

        public void addProgressBarLoadMore() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (array_model_ebook_see_all != null) {
                        array_model_ebook_see_all.add(null);
                        adapter_recyclerView_showEbook.notifyItemInserted(array_model_ebook_see_all.size() - 1);
                    }
                }
            });
        }

        public void removeProgressBarLoadMore() {
            if (onPause) {
                return;
            }
            if (array_model_ebook_see_all.size() > 0) {
                array_model_ebook_see_all.remove(array_model_ebook_see_all.size() - 1);
                adapter_recyclerView_showEbook.notifyItemRemoved(array_model_ebook_see_all.size());
            }
        }

        public void onLoadMore(String type) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
//                        true, false);
                    swipeRefreshLayout.setRefreshing(true);

                }
            });
            asyncTask_FetchAPI = new AsyncTask_FetchAPI();
            asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {
                @Override
                public void onFetchStart(String apiURL, int currentIndex) {
                    Log.e("onFetchStart", "URL = " + apiURL);

                }

                @Override
                public void onFetchComplete(String apiURL, int currentIndex, PList result) {
                    resultPlist = result;
//                    removeProgressBarLoadMore();
                    if (((Array) resultPlist.getRootElement()).size() > 0) {
                        for (PListObject each : (Array) resultPlist.getRootElement()) {
                            model_ebook_see_all = new Model_Ebooks_Public_User(each);
                            array_model_ebook_see_all.add(model_ebook_see_all);
//                            if (!onPause) {
//                                adapter_recyclerView_showEbook.notifyItemInserted(array_model_ebook_see_all.size());
//                            }
                        }
                    } else {
                        limitLoadMore = true;
                    }
                }

                @Override
                public void onFetchError(String apiURL, int currentIndex, Exception e) {

                }

                @Override
                public void onTimeOut(String apiURL, int currentIndex) {

                }

                @Override
                public void onAllTaskDone() {


                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
//                        true, false);
                            disableSwipeRefresh();

                        }
                    }, 1500);
                    adapter_recyclerView_showEbook.notifyDataSetChanged();
                    loading = false;
                }
            });
            if (limitLoadMore) {
                disableSwipeRefresh();
//                removeProgressBarLoadMore();
                return;
            } else {
                caseLoadmore(type, asyncTask_FetchAPI);
            }


        }
    };

    public void caseLoadmore(String type, AsyncTask_FetchAPI asyncTask_fetchAPI) {
        if (isLiterature) {
            currentPageL++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageL);
        } else if (isExercise) {
            currentPageM++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageM);
        } else {
            currentPageL++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageL);
        }
    }

    public void executeLoadMore(AsyncTask_FetchAPI asyncTask_fetchAPI, String type, int page) {
        asyncTask_fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + type + "&page=" + page + "&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=0&mode=o&scatid=0"));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_document:
                downloadEbooks("L");
                isLiterature = true;
                isExercise = false;
                return;
            case R.id.radio_button_exercise:
                downloadEbooks("M");
                isLiterature = false;
                isExercise = true;
                return;

            default:
                downloadEbooks("L");
                isLiterature = true;
                isExercise = false;
                return;
        }
    }


    @Override
    public void onPause() {
        onPause = true;
        closeProgress();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        limitLoadMore = false;
        loading = false;
        lastVisibleItem = 0;
        totalItemCount = 0;
        currentPageL = 1;
        currentPageM = 1;
        super.onDestroy();
    }

    private void closeProgress() {
        disableSwipeRefresh();
        dismissDialog();
    }

    public void cancelTaskDownload() {
        if (asyncTask_FetchAPI != null) {
            asyncTask_FetchAPI.cancel(true);
            asyncTask_FetchAPI = null;
        }
    }

    public void disableSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public void dismissDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
