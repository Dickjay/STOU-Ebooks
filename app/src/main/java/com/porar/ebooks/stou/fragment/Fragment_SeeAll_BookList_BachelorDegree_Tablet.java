package com.porar.ebooks.stou.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_SeeAll;
import com.porar.ebook.control.DialogCatagoriesList;
import com.porar.ebook.control.Dialog_SearchEbooks;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;
import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/7/2015.
 */
public class Fragment_SeeAll_BookList_BachelorDegree_Tablet extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public final String DOCUMENT = "D";
    public final String EXERCISE = "E";
    public final String SUPPLEMENT = "S";

    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private int spanCount;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout linear_segment;

    private Adapter_RecyclerView_SeeAll adapter_recyclerView_seeAll;

    public static Model_Ebooks_Public_User model_ebook_see_all;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebook_see_all;

    public Model_Categories model_categories;
    public ArrayList<Model_Categories> array_model_categories;

    private static boolean isLoad = false;

    public SegmentedGroup segment_mode;
    private RadioButton radio_d, radio_e, radio_s;

    private boolean isDocument = false;
    private boolean isExercise = false;
    private boolean isSupplement = false;

    private String sCatid = "0";

    private AdjustableImageView navigation_back, navigation_search, dropdown_imageview;

    public SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPageD = 1;
    private int currentPageE = 1;
    private int currentPageS = 1;
    private boolean limitLoadMore = false;

    public static Fragment newInstance() {
        Fragment_SeeAll_BookList_BachelorDegree_Tablet fragment_seeAllBookListPublicUser = new Fragment_SeeAll_BookList_BachelorDegree_Tablet();
        if (isLoad) {
            isLoad = false;
        }
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestDownload();
                    }
                }, 2000);

            }
        });
    }

    private void initVIew(View view) {
        navigation_back = (AdjustableImageView) view.findViewById(R.id.navigation_back);
        navigation_search = (AdjustableImageView) view.findViewById(R.id.navigation_search);
        dropdown_imageview = (AdjustableImageView) view.findViewById(R.id.dropdown_imageview);
        segment_mode = (SegmentedGroup) view.findViewById(R.id.segment_mode);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        spanCount = getResources().getInteger(R.integer.grid_columns);
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        linear_segment = (LinearLayout) view.findViewById(R.id.linear_segment);

        radio_d = (RadioButton) view.findViewById(R.id.radio_button_document);
        radio_e = (RadioButton) view.findViewById(R.id.radio_button_exercise);
        radio_s = (RadioButton) view.findViewById(R.id.radio_button_supplement);

        radio_d.setText(getActivity().getResources().getString(R.string.title_bac_doc));
        radio_e.setText(getActivity().getResources().getString(R.string.title_bac_exe));
        radio_s.setText(getActivity().getResources().getString(R.string.title_sup));

        radio_d.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_e.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_s.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));

        segment_mode.setOnCheckedChangeListener(this);


        navigation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        navigation_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_SearchEbooks(getActivity(), R.style.FadeInOutDialog, Fragment_SeeAll_BookList_BachelorDegree_Tablet.this).show();
            }
        });

        if (Shared_Object.getCustomerDetail.getBechalorDegree() > 0) {

        } else if (Shared_Object.getCustomerDetail.getMasterDegree() > 0) {

        } else {
            linear_segment.setVisibility(View.GONE);
        }

        if (Shared_Object.getCustomerDetail.getIsTeacher() > 0) {
            showDropdownList();
            setDropDownClick();

        }


        recyclerView.addOnScrollListener(onScrollListener);
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            isLoad = true;
            radio_d.setChecked(true);
            isDocument = true;
            isExercise = false;
            isSupplement = false;
        }
    }
    public void requestDownload() {
        if (isDocument) {
            downloadEbooks(DOCUMENT);
        } else if (isExercise) {
            downloadEbooks(EXERCISE);
        } else {
            downloadEbooks(SUPPLEMENT);
        }
    }
    public void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
//                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
//                        true, false);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    public void downloadEbooks(String type) {
        if (getActivity() == null) {
            return;
        }
        showDialog();
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
                    array_model_categories = new ArrayList<Model_Categories>();
                    addAllBook(array_model_categories);
                    for (PListObject each : (Array) result.getRootElement()) {
                        model_categories = new Model_Categories(each);
                        array_model_categories.add(model_categories);
                    }
                }
            }

            @Override
            public void onAllTaskDone() {
                if (getActivity() != null) {
                    Log.e("onAllTaskDone", "onAllTaskDone");
                    if (array_model_ebook_see_all != null && array_model_ebook_see_all.size() > 0) {
                        setAdapter();
                    } else {
                        Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }
                    closeProgress();
                }

            }

            public void addAllBook(ArrayList<Model_Categories> array_model_categories) {
                Model_Categories model_allbook = new Model_Categories();
                model_allbook.setCatID(0);
                model_allbook.setSCatID(0);
                model_allbook.setName(getActivity().getResources().getString(R.string.allbook));
                model_allbook.setDetail(getActivity().getResources().getString(R.string.allbook_detail));
                model_allbook.setPictureURL("images/allcat.png");
                array_model_categories.add(model_allbook);
            }

        });

        executeData(asyncTask_FetchAPI, type);
    }

    private void executeData(AsyncTask_FetchAPI fetchAPI, String type) {
        fetchAPI.execute(AppMain
                        .getAPIbyRefKey("get_ebook_list",
                                "type=" + type + "&page=1&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=0&mode=b&scatid=" + sCatid),
                "http://203.150.225.223/stoubookapi/api/categories.ashx?catid=4");

    }

    private void setAdapter() {
        adapter_recyclerView_seeAll = new Adapter_RecyclerView_SeeAll(getActivity(), array_model_ebook_see_all,
                Fragment_SeeAll_BookList_BachelorDegree_Tablet.this, true) {
            @Override
            public void gotoDetail(ArrayList<Model_Ebooks_Public_User> arrayList_model_ebooks_public_users, int pos) {
            }

            @Override
            public void gotoUnit(String bCode) {
                if (isDocument) {
                    addFragmentUnit(bCode, DOCUMENT);
                } else if (isExercise) {
                    addFragmentUnit(bCode, EXERCISE);
                } else {
                    addFragmentUnit(bCode, SUPPLEMENT);
                }

            }
        };

        recyclerView.setAdapter(adapter_recyclerView_seeAll);
    }
    public void addFragmentUnit(String bCode, String type) {
        Fragment_Choice_Mode.addFragment(
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out),
                Fragment_SeeAll_Unit_BachelorDegree_Tablet.newInstance(bCode, type), true,
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }

    public void showDropdownList() {
        dropdown_imageview.setVisibility(View.VISIBLE);
    }

    private void setDropDownClick() {
        dropdown_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_model_categories != null && array_model_categories.size() > 0) {
                    ArrayList<String> item = new ArrayList<String>();
                    item = addItemList(item);
                    showDropDownDialog(item);
                }
            }
        });
    }


    public ArrayList<String> addItemList(ArrayList<String> item) {
        for (int i = 0; i < array_model_categories.size(); i++) {
            item.add(array_model_categories.get(i).getName());
        }
        return item;
    }


    public void showDropDownDialog(ArrayList<String> item) {
        DialogCatagoriesList dialogCatagoriesList = new DialogCatagoriesList();
        dialogCatagoriesList.buildDialog(Fragment_SeeAll_BookList_BachelorDegree_Tablet.this, item.toArray(new String[item.size()]),
                new DialogCatagoriesList.CatagoriesCallback() {
                    @Override
                    public void setCatiD(int pos) {
                        sCatid = array_model_categories.get(pos).getSCatID() + "";
                        requestDownload();
                    }
                });
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_document:
                isDocument = true;
                isExercise = false;
                isSupplement = false;
                requestDownload();
                return;
            case R.id.radio_button_exercise:
                isDocument = false;
                isExercise = true;
                isSupplement = false;
                requestDownload();
                return;
            case R.id.radio_button_supplement:
                isDocument = false;
                isExercise = false;
                isSupplement = true;
                requestDownload();
                return;
            default:
                isDocument = true;
                isExercise = false;
                isSupplement = false;
                requestDownload();
                return;
        }
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
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
                        if (isDocument) {
                            onLoadMore(DOCUMENT);
                        } else if (isExercise) {
                            onLoadMore(EXERCISE);
                        } else {
                            onLoadMore(SUPPLEMENT);
                        }
                    }
                }, 500);
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
        if (type.equals(DOCUMENT)) {
            currentPageD++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageD);
        } else if (type.equals(EXERCISE)) {
            currentPageE++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageE);
        } else {
            currentPageS++;
            executeLoadMore(asyncTask_fetchAPI, type, currentPageS);
        }
    }

    public void executeLoadMore(AsyncTask_FetchAPI asyncTask_fetchAPI, String type, int page) {
        asyncTask_fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + type + "&page=" + page + "&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=0&mode=b&scatid=" + sCatid));
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
        currentPageD = 1;
        currentPageE = 1;
        currentPageS = 1;
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

    public void dismissDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void disableSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
