package com.porar.ebooks.stou.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_SeeAll;
import com.porar.ebook.control.DialogCatagoriesList;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.activity.Activity_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/7/2015.
 */
public class Fragment_SeeAll_BookList_PublicUser_Tablet extends Fragment {
    public final String LITERATURE = "L";
    public final String MATERIAL = "M";
    public final String RECOMMEND = "R";


    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private int spanCount;
    private GridLayoutManager gridLayoutManager;
    private Adapter_RecyclerView_SeeAll adapter_recyclerView_seeAll;

    private LinearLayout linear_segment;
    private AdjustableImageView navigation_back, navigation_refresh;
    private LinearLayout linear_title;
    private TextView text_title;

    public static Model_Ebooks_Public_User model_ebook_see_all;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebook_see_all;
    public static boolean isLiterature = false;
    public static boolean isRecommend = false;

    public static Model_Categories model_ebook_catagories;
    public ArrayList<Model_Categories> array_model_catagories;

    AlertDialog alertDialog;
    Model_Ebooks_Detail ebooks_Detail = null;
    Ebook_Detail ebook_DetailApi;

    private Resources resouceID;

    public SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPageL = 1;
    private int currentPageM = 1;
    private boolean limitLoadMore = false;

    public static Fragment newInstance(boolean isLite, boolean isRecommends) {
        isLiterature = isLite;
        isRecommend = isRecommends;
        Fragment_SeeAll_BookList_PublicUser_Tablet fragment_seeAllBookListPublicUser = new Fragment_SeeAll_BookList_PublicUser_Tablet();
        return fragment_seeAllBookListPublicUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_all_ebooks_tablet, container, false);
        setupSwipeRefresh(view);
        initVIew(view);
        return view;
    }

    private void setupSwipeRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout = StaticUtils.setColorSwipeReresh(swipeRefreshLayout);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
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

    private void initVIew(View view) {

        resouceID = getActivity().getResources();

        navigation_back = (AdjustableImageView) view.findViewById(R.id.navigation_back);
        navigation_refresh = (AdjustableImageView) view.findViewById(R.id.navigation_refresh);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        spanCount = getResources().getInteger(R.integer.grid_columns);
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        linear_segment = (LinearLayout) view.findViewById(R.id.linear_segment);
        linear_segment.setVisibility(View.GONE);

        linear_title = (LinearLayout) view.findViewById(R.id.linear_title);
        linear_title.setVisibility(View.VISIBLE);
        text_title = (TextView) view.findViewById(R.id.text_title);
        text_title.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));

        if (isRecommend) {

            text_title.setText(resouceID.getString(R.string.books_recommends));
        } else {
            if (isLiterature) {
                text_title.setText(resouceID.getString(R.string.literature));
            } else {
                text_title.setText(resouceID.getString(R.string.lesson));
            }
        }

        navigation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        navigation_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadEbooks();
            }
        });

        recyclerView.addOnScrollListener(onScrollListener);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadEbooks();
    }

    public void downloadEbooks() {
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
                Log.e("onFetchStart", "URL = " + apiURL);
                resetLoadmoreStated();
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
                if (apiURL.contains("categories")) {
                    array_model_catagories = new ArrayList<Model_Categories>();
                    for (PListObject each : (Array) result.getRootElement()) {
                        model_ebook_catagories = new Model_Categories(each);
                        array_model_catagories.add(model_ebook_catagories);
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
                    recyclerView.setAdapter(null);
                }
                closeProgress();

            }

        });
        if (isRecommend) {

            executeEbooksRecommends(asyncTask_FetchAPI);
        } else {
            if (isLiterature) {
                executeEbooksLiterature(asyncTask_FetchAPI);
            } else {
                executeEbooksMaterial(asyncTask_FetchAPI);
            }
        }


    }

    private void executeEbooksRecommends(AsyncTask_FetchAPI fetchAPI) {
        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=R&page=0&cid=0&bcode=0&mode=o&scatid=0"));

    }

    private void executeEbooksMaterial(AsyncTask_FetchAPI fetchAPI) {
        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + MATERIAL + "&page=1&cid=0&bcode=0&mode=o&scatid=0"));


    }

    private void executeEbooksLiterature(AsyncTask_FetchAPI fetchAPI) {

        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + LITERATURE + "&page=1&cid=0&bcode=0&mode=o&scatid=0"));
    }

    private void setAdapter() {
        adapter_recyclerView_seeAll = new Adapter_RecyclerView_SeeAll(getActivity(), array_model_ebook_see_all,
                Fragment_SeeAll_BookList_PublicUser_Tablet.this) {
            @Override
            public void gotoDetail(ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users, int pos) {
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

        recyclerView.setAdapter(adapter_recyclerView_seeAll);
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isRecommend) {
                return;
            } else {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    loading = true;
                    swipeRefreshLayout.setRefreshing(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (isLiterature) {
                                onLoadMore(LITERATURE);
                            } else {
                                onLoadMore(MATERIAL);
                            }
                        }
                    }, 500);
                }
            }

        }

        public void onLoadMore(String type) {
            AsyncTask_FetchAPI asyncTask_fetchAPI = new AsyncTask_FetchAPI();
            asyncTask_fetchAPI.setOnFetchListener(new OnFetchAPIListener() {
                @Override
                public void onFetchStart(String apiURL, int currentIndex) {
                    Log.e("onFetchStart", "URL = " + apiURL);

                }

                @Override
                public void onFetchComplete(String apiURL, int currentIndex, PList result) {
                    if (((Array) result.getRootElement()).size() > 0) {
                        for (PListObject each : (Array) result.getRootElement()) {
                            model_ebook_see_all = new Model_Ebooks_Public_User(each);
                            array_model_ebook_see_all.add(model_ebook_see_all);
//                                adapter_recyclerView_seeAll.notifyItemInserted(array_model_ebook_see_all.size());
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
                    disableSwipeRefresh();
                    adapter_recyclerView_seeAll.notifyDataSetChanged();
                    loading = false;
                }
            });
            if (limitLoadMore) {
                disableSwipeRefresh();
                return;
            } else {
                caseLoadmore(type, asyncTask_fetchAPI);
            }


        }
    };


    public void caseLoadmore(String type, AsyncTask_FetchAPI asyncTask_fetchAPI) {
        if (type.equals(LITERATURE)) {
            currentPageL++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageL);
        } else {
            currentPageM++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageM);
        }
    }

    public void executeLoadMore(AsyncTask_FetchAPI asyncTask_fetchAPI, String type, int page) {

        asyncTask_fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + type + "&page=" + page + "&cid=0&bcode=0&mode=o&scatid=0"));
    }

    @Override
    public void onPause() {
        closeProgress();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        resetLoadmoreStated();
        DialogCatagoriesList.check_pos = 0;
        super.onDestroy();
    }

    public void resetLoadmoreStated() {
        limitLoadMore = false;
        loading = false;
        firstVisibleItem = 0;
        previousTotal = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        currentPageL = 1;
        currentPageM = 1;
    }

    private void closeProgress() {
        disableSwipeRefresh();
        dismissDialog();
        cancelTaskDownload();
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
