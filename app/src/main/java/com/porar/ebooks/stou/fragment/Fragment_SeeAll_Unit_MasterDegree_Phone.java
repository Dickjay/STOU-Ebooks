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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_ShowEbook;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;
import com.porar.ebooks.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/8/2015.
 */
public class Fragment_SeeAll_Unit_MasterDegree_Phone extends Fragment {
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    private AdjustableImageView hamburger_imageview, search_imageview, back_imageview;
    private LinearLayout linear_segment;

    public static Model_Ebooks_Public_User model_ebook_see_all;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebook_see_all;

    private Adapter_RecyclerView_ShowEbook adapter_recyclerView_showEbook;
    private RecyclerView recyclerViewList;
    public static LinearLayoutManager gridLayoutManager;

    AlertDialog alertDialog;
    Model_Ebooks_Detail ebooks_Detail = null;
    Ebook_Detail ebook_DetailApi;

    public static String bCode = "";
    public static String bType = "";

    public SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    public static Fragment newInstance(String bCodeBook, String type) {
        Fragment_SeeAll_Unit_MasterDegree_Phone fragment_mainPublicUser_phone = new Fragment_SeeAll_Unit_MasterDegree_Phone();
        bCode = bCodeBook;
        bType = type;
        return fragment_mainPublicUser_phone;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_all_ebooks_phone, container, false);
        setupSwipeRefresh(view);
        initView(view);
        return view;
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
                        downloadEbooks();

                    }
                }, 2000);

            }
        });
    }

    private void initView(View view) {
        hamburger_imageview = (AdjustableImageView) view.findViewById(R.id.hamburger_imageview);
        search_imageview = (AdjustableImageView) view.findViewById(R.id.search_imageview);
        back_imageview = (AdjustableImageView) view.findViewById(R.id.back_imageview);


        recyclerViewList = (RecyclerView) view.findViewById(R.id.recyclerview_list);
        recyclerViewList.setHasFixedSize(true);
        gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewList.setLayoutManager(gridLayoutManager);
        recyclerViewList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());

        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        linear_segment = (LinearLayout) view.findViewById(R.id.linear_segment);
        linear_segment.setVisibility(View.GONE);

        hamburger_imageview.setVisibility(View.INVISIBLE);
        search_imageview.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_action_refresh));
        search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadEbooks();
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadEbooks();
    }

    public void downloadEbooks() {
        if(getActivity()==null){
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
                Log.e("onFetchStart", "URL = " + apiURL);

            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                closeProgress();
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
                }
                closeProgress();
            }

        });

        executeData(asyncTask_FetchAPI);
    }

    private void setAdapter() {
        adapter_recyclerView_showEbook = new Adapter_RecyclerView_ShowEbook(getActivity(), array_model_ebook_see_all, Fragment_SeeAll_Unit_MasterDegree_Phone.this, true, true) {
            @Override
            public void gotoDetail(ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users, int pos) {
//
//                Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(
//                        getActivity(), array_model_ebook_see_all.get(pos).getBID());
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

    private void executeData(AsyncTask_FetchAPI fetchAPI) {
        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + bType + "&page=0&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=" + bCode + "&mode=m&scatid=0"));
    }

    @Override
    public void onPause() {
        closeProgress();
        super.onPause();
    }
    private void closeProgress() {
        disableSwipeRefresh();
        dismissDialog();
        cancelTaskDownload();
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
    public void cancelTaskDownload() {
        if (asyncTask_FetchAPI != null) {
            asyncTask_FetchAPI.cancel(true);
            asyncTask_FetchAPI = null;
        }
    }
}
