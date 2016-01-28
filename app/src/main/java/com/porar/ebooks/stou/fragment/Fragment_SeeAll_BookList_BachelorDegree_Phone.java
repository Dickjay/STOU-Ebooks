package com.porar.ebooks.stou.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.porar.ebook.control.DialogCatagoriesList;
import com.porar.ebook.control.Dialog_SearchEbooks;
import com.porar.ebook.control.Dialog_Select_Mode;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Ebooks_Public_User;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
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
public class Fragment_SeeAll_BookList_BachelorDegree_Phone extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public final String DOCUMENT = "D";
    public final String EXERCISE = "E";
    public final String SUPPLEMENT = "S";

    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private AdjustableImageView hamburger_imageview, search_imageview, back_imageview, dropdown_imageview;
    public static LinearLayoutManager gridLayoutManager;

    private SegmentedGroup segment_mode;
    private RadioButton radio_d, radio_e, radio_s;

    private static boolean isLoad = false;
    private boolean isDocument = false;
    private boolean isExercise = false;

    private String sCatid = "0";

    public static Model_Ebooks_Public_User model_ebook_see_all;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebook_see_all;

    public Model_Categories model_categories;
    public ArrayList<Model_Categories> array_model_categories;

    private Adapter_RecyclerView_ShowEbook adapter_recyclerView_showEbook;
    private RecyclerView recyclerViewList;

    public SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 7;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;

    private int currentPageD = 1;
    private int currentPageE = 1;
    private int currentPageS = 1;
    private boolean limitLoadMore = false;

    private boolean onPause = false;

    public static Fragment newInstance() {
        Fragment_SeeAll_BookList_BachelorDegree_Phone fragment_mainPublicUser_phone = new Fragment_SeeAll_BookList_BachelorDegree_Phone();
        if (isLoad) {
            isLoad = false;
        }
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
                        requestDownload();
                    }
                }, 2000);

            }
        });
    }

    private void initView(View view) {
        hamburger_imageview = (AdjustableImageView) view.findViewById(R.id.hamburger_imageview);
        search_imageview = (AdjustableImageView) view.findViewById(R.id.search_imageview);
        back_imageview = (AdjustableImageView) view.findViewById(R.id.back_imageview);
        dropdown_imageview = (AdjustableImageView) view.findViewById(R.id.dropdown_imageview);

        segment_mode = (SegmentedGroup) view.findViewById(R.id.segment_mode);

        radio_d = (RadioButton) view.findViewById(R.id.radio_button_document);
        radio_e = (RadioButton) view.findViewById(R.id.radio_button_exercise);
        radio_s = (RadioButton) view.findViewById(R.id.radio_button_supplement);

        radio_d.setText(getActivity().getResources().getString(R.string.title_bac_doc));
        radio_e.setText(getActivity().getResources().getString(R.string.title_bac_exe));
        radio_s.setText(getActivity().getResources().getString(R.string.title_sup));

        radio_d.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_e.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));
        radio_s.setTypeface(StaticUtils.getTypeface(getActivity(), StaticUtils.Font.DB_HelvethaicaMon_X));

        recyclerViewList = (RecyclerView) view.findViewById(R.id.recyclerview_list);

        recyclerViewList.setHasFixedSize(true);
        gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewList.setLayoutManager(gridLayoutManager);
        recyclerViewList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerViewList.addOnScrollListener(onScrollListener);

        segment_mode.setOnCheckedChangeListener(this);

        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        hamburger_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Select_Mode dialog_select_mode = new Dialog_Select_Mode(getActivity(), R.style.FadeInOutDialog, Fragment_SeeAll_BookList_BachelorDegree_Phone.this, true, "b");
                dialog_select_mode.show();
            }
        });

        search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_SearchEbooks(getActivity(), R.style.FadeInOutDialog, Fragment_SeeAll_BookList_BachelorDegree_Phone.this).show();
            }
        });

        if (Shared_Object.getCustomerDetail.getIsTeacher() > 0) {
            showDropdownList();
            setDropDownClick();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            isLoad = true;
            radio_d.setChecked(true);
            isDocument = true;
            isExercise = false;
        }
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
                limitLoadMore = false;
                loading = false;
                lastVisibleItem = 0;
                totalItemCount = 0;
                currentPageD = 1;
                currentPageE = 1;
                currentPageS = 1;
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
                        recyclerViewList.setAdapter(null);
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

        if (type.equals("D")) {
            executeData(asyncTask_FetchAPI, type, currentPageD);
        } else if (type.equals("E")) {
            executeData(asyncTask_FetchAPI, type, currentPageE);
        } else {
            executeData(asyncTask_FetchAPI, type, currentPageS);
        }
    }

    private void executeData(AsyncTask_FetchAPI fetchAPI, String type, int page) {

        fetchAPI.execute(AppMain
                .getAPIbyRefKey("get_ebook_list",
                        "type=" + type + "&page=1&cid=" + Shared_Object.getCustomerDetail.getCID() + "&bcode=0&mode=b&scatid=" + sCatid), "http://203.150.225.223/stoubookapi/api/categories.ashx?catid=4");


    }

    private void setAdapter() {
        adapter_recyclerView_showEbook = new Adapter_RecyclerView_ShowEbook(getActivity(), array_model_ebook_see_all, Fragment_SeeAll_BookList_BachelorDegree_Phone.this, true, recyclerViewList) {
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
        recyclerViewList.setAdapter(adapter_recyclerView_showEbook);

    }

    public void addFragmentUnit(String bCode, String type) {
        Fragment_Choice_Mode.addFragment(
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out),
                Fragment_SeeAll_Unit_BachelorDegree_Phone.newInstance(bCode, type), true,
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
                    showDropDownDialog(addItemList(item));
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
        dialogCatagoriesList.buildDialog(Fragment_SeeAll_BookList_BachelorDegree_Phone.this, item.toArray(new String[item.size()]),
                new DialogCatagoriesList.CatagoriesCallback() {
                    @Override
                    public void setCatiD(int pos) {
                        sCatid = array_model_categories.get(pos).getSCatID() + "";
                        requestDownload();
                    }
                });
    }


    private PList resultPlist;
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
                if (!loading) {
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        loading = true;
//                        addProgressBarLoadMore();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                        if (isDocument) {
                            onLoadMore(DOCUMENT);
                        } else if (isExercise) {
                            onLoadMore(EXERCISE);
                        } else {
                            onLoadMore(SUPPLEMENT);
                        }
//                            }
//                        }, 800);

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_document:
                isDocument = true;
                isExercise = false;
                requestDownload();
                return;
            case R.id.radio_button_exercise:
                isDocument = false;
                isExercise = true;
                requestDownload();
                return;
            case R.id.radio_button_supplement:
                isDocument = false;
                isExercise = false;
                requestDownload();
                return;
            default:
                isDocument = true;
                isExercise = false;
                requestDownload();
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
        currentPageD = 1;
        currentPageE = 1;
        currentPageS = 1;
        DialogCatagoriesList.check_pos = 0;
        super.onDestroy();
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
