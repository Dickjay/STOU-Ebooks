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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/6/2015.
 */
public class Fragment_BookList_PublicUser_Tablet extends Fragment implements
        Serializable {

    public static Handler handler = new Handler();
    private ProgressDialog progressDialog;

    private TextView title_lesson_textview, title_lesson_see_all_textview, title_recommend_textview, title_recommend_see_all_textview,
            head_recommend_textview, title_literature_textview, title_literature_see_all_textview;
    private LinearLayout lesson_list_linear, recommend_list_linear, literature_list_linear;


    public static Model_Ebooks_Public_User model_ebookList_cat1;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebookList_cat1;

    public static Model_Ebooks_Public_User model_ebookList_cat2;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebookList_cat2;


    public static Model_Ebooks_Public_User model_ebookList_cat3;
    public ArrayList<Model_Ebooks_Public_User> array_model_ebookList_cat3;


    AlertDialog alertDialog;
    Model_Ebooks_Detail ebooks_Detail = null;
    Ebook_Detail ebook_DetailApi;
    private AsyncTask_FetchAPI asyncTask_FetchAPI;

    public static Fragment newInstance() {
        Fragment_BookList_PublicUser_Tablet fragment_public_user_ebook = new Fragment_BookList_PublicUser_Tablet();
        return fragment_public_user_ebook;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_public_user_ebook_list_tablet, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Shared_Object.checkInternetConnection(getActivity())) {
            showToastMessage();
        } else {
            downloadEbooks();
        }
    }

    private void intiView(View view) {
        title_lesson_textview = (TextView) view.findViewById(R.id.title_lesson_textview);
        title_lesson_see_all_textview = (TextView) view.findViewById(R.id.title_lesson_see_all_textview);
        title_recommend_textview = (TextView) view.findViewById(R.id.title_recommend_textview);
        title_recommend_see_all_textview = (TextView) view.findViewById(R.id.title_recommend_see_all_textview);
        head_recommend_textview = (TextView) view.findViewById(R.id.head_recommend_textview);
        title_literature_textview = (TextView) view.findViewById(R.id.title_literature_textview);
        title_literature_see_all_textview = (TextView) view.findViewById(R.id.title_literature_see_all_textview);

        title_lesson_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        title_lesson_see_all_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        title_recommend_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        title_recommend_see_all_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        head_recommend_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        title_literature_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));
        title_literature_see_all_textview.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));


        lesson_list_linear = (LinearLayout) view.findViewById(R.id.lesson_list_linear);
        recommend_list_linear = (LinearLayout) view.findViewById(R.id.recommend_list_linear);
        literature_list_linear = (LinearLayout) view.findViewById(R.id.literature_list_linear);

        title_lesson_see_all_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Shared_Object.checkInternetConnection(getActivity())) {
                    showToastMessage();
                } else {
                    callFragmentSeeAll(false, false);
                }
            }
        });
        title_recommend_see_all_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Shared_Object.checkInternetConnection(getActivity())) {
                    showToastMessage();
                } else {
                    callFragmentSeeAll(false, true);
                }
            }
        });
        title_recommend_see_all_textview.setVisibility(View.INVISIBLE);
        title_literature_see_all_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Shared_Object.checkInternetConnection(getActivity())) {
                    showToastMessage();
                } else {
                    callFragmentSeeAll(true, false);
                }
            }
        });
    }

    private void showToastMessage() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();
    }

    public void downloadEbooks() {

        handler.post(new Runnable() {

            @Override
            public void run() {
                progressDialog = ProgressDialog.show(getActivity(), "", "Loading..",
                        true, false);

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

            }

            @Override
            public void onFetchComplete(final String apiURL, int currentIndex,
                                        final PList result) {
                if (apiURL.contains("get_ebook_list")) {

                    if (apiURL.contains("type=M")) {
                        array_model_ebookList_cat1 = new ArrayList<Model_Ebooks_Public_User>();
                        for (PListObject each : (Array) result.getRootElement()) {
                            model_ebookList_cat1 = new Model_Ebooks_Public_User(each);
                            array_model_ebookList_cat1.add(model_ebookList_cat1);
                        }
                    }

                    if (apiURL.contains("type=L")) {
                        array_model_ebookList_cat2 = new ArrayList<Model_Ebooks_Public_User>();
                        for (PListObject each : (Array) result.getRootElement()) {
                            model_ebookList_cat2 = new Model_Ebooks_Public_User(each);
                            array_model_ebookList_cat2.add(model_ebookList_cat2);
                        }
                    }
                    if (apiURL.contains("type=R")) {
                        array_model_ebookList_cat3 = new ArrayList<Model_Ebooks_Public_User>();
                        for (PListObject each : (Array) result.getRootElement()) {
                            model_ebookList_cat3 = new Model_Ebooks_Public_User(each);
                            array_model_ebookList_cat3.add(model_ebookList_cat3);
                        }
                    }

                }
            }

            @Override
            public void onAllTaskDone() {
                Log.e("onAllTaskDone", "onAllTaskDone");

                if (array_model_ebookList_cat1 != null && array_model_ebookList_cat1.size() > 0) {
                    lesson_list_linear.removeAllViews();
                    for (int currentItem = 0; currentItem < array_model_ebookList_cat1.size(); currentItem++) {
                        addBooksGuidelineItem(currentItem);
                    }
                }

                if (array_model_ebookList_cat3 != null && array_model_ebookList_cat3.size() > 0) {
                    recommend_list_linear.removeAllViews();
                    for (int currentItem = 0; currentItem < array_model_ebookList_cat3.size(); currentItem++) {
                        addBooksRecommendItem(currentItem);
                    }
                }

                if (array_model_ebookList_cat2 != null && array_model_ebookList_cat2.size() > 0) {
                    literature_list_linear.removeAllViews();
                    for (int currentItem = 0; currentItem < array_model_ebookList_cat2.size(); currentItem++) {
                        addBooksLiteratureItem(currentItem);
                    }
                }
                dismissDialog();
            }

        });
        executeData(asyncTask_FetchAPI);
    }


    private void executeData(AsyncTask_FetchAPI fetchAPI) {
        if (isTeacher()) {
            fetchAPI.execute(AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=M&page=1&cid=0&bcode=0&mode=o&scatid=0"), AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=R&page=1&cid=0&bcode=0&mode=o&scatid=0"), AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=L&page=1&cid=0&bcode=0&mode=o&scatid=0"));
        } else {
            fetchAPI.execute(AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=M&page=1&cid=0&bcode=0&mode=o&scatid=0"), AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=R&page=1&cid=0&bcode=0&mode=o&scatid=0"), AppMain
                    .getAPIbyRefKey("get_ebook_list",
                            "type=L&page=1&cid=0&bcode=0&mode=o&scatid=0"));
        }

    }

    public boolean isTeacher() {
        return Shared_Object.getCustomerDetail.getIsTeacher() > 0;
    }

    private void callFragmentSeeAll(boolean isLite, boolean isRecommend) {
        Fragment_Choice_Mode.addFragment(
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out),
                Fragment_SeeAll_BookList_PublicUser_Tablet.newInstance(isLite, isRecommend), true,
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }

    private void addBooksGuidelineItem(int currentSize) {
        final AdjustableImageView ebooks_imageview = new AdjustableImageView(
                getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 10, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        // download books cover
        String coverUrl = array_model_ebookList_cat1.get(currentSize).getBookCover();
        StaticUtils.picasso.with(getActivity()).load(coverUrl)
                .noFade().into(ebooks_imageview);

        ebooks_imageview
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(
//                                getActivity(), array_model_ebookList_cat1.get(v.getId()).getBID());
//                        if (DSebooks_Detail != null) {
//                            Log.v("OnClickToDetailEbook", "DeserializeObject Success"
//                                    + array_model_ebookList_cat1.get(v.getId()).getBID());
//                            Intent intent = new Intent(getActivity(), Activity_Detail.class);
//                            intent.putExtra("model", DSebooks_Detail);
//                            intent.putExtra("DeserializeObject", "1");
//                            StaticUtils.phoneValue = 1;
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            getActivity().startActivity(intent);
//                            ((Activity) getActivity()).overridePendingTransition(
//                                    R.anim.slide_in_right, R.anim.slide_out_left);
//
//                        } else {
                        ebook_DetailApi = new Ebook_Detail(getActivity(),
                                String.valueOf(array_model_ebookList_cat1.get(v.getId()).getBID()));
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
//                        }
                    }
                });

        ebooks_imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ebooks_imageview.setAdjustViewBounds(true);
        ebooks_imageview.setLayoutParams(layoutParams);
        ebooks_imageview.setPadding(5, 5, 5, 5);
        ebooks_imageview.setId(currentSize);
        lesson_list_linear.addView(ebooks_imageview);
    }

    private void addBooksRecommendItem(int currentSize) {
        final AdjustableImageView ebooks_imageview = new AdjustableImageView(
                getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10, 0, 0, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        // download books cover

        String coverUrl = array_model_ebookList_cat3.get(currentSize).getBookCover();
        StaticUtils.picasso.with(getActivity()).load(coverUrl)
                .noFade().into(ebooks_imageview);

        ebooks_imageview
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(
                                getActivity(), array_model_ebookList_cat3.get(v.getId()).getBID());
                        if (DSebooks_Detail != null) {
                            Log.v("OnClickToDetailEbook", "DeserializeObject Success"
                                    + array_model_ebookList_cat3.get(v.getId()).getBID());
                            Intent intent = new Intent(getActivity(), Activity_Detail.class);
                            intent.putExtra("model", DSebooks_Detail);
                            intent.putExtra("DeserializeObject", "1");
                            StaticUtils.phoneValue = 1;
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            ((Activity) getActivity()).overridePendingTransition(
                                    R.anim.slide_in_right, R.anim.slide_out_left);

                        } else {
                            ebook_DetailApi = new Ebook_Detail(getActivity(),
                                    String.valueOf(array_model_ebookList_cat3.get(v.getId()).getBID()));
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
                        }
                    }
                });

        ebooks_imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ebooks_imageview.setAdjustViewBounds(true);
        ebooks_imageview.setLayoutParams(layoutParams);
        ebooks_imageview.setPadding(5, 5, 5, 5);
        ebooks_imageview.setId(currentSize);
        recommend_list_linear.addView(ebooks_imageview);
    }


    private void addBooksLiteratureItem(final int currentSize) {
        final AdjustableImageView ebooks_imageview = new AdjustableImageView(
                getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 10, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        // download books cover
        String coverUrl = array_model_ebookList_cat2.get(currentSize).getBookCover();

        StaticUtils.picasso.with(getActivity()).load(coverUrl)
                .noFade().into(ebooks_imageview);

        ebooks_imageview
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model_Ebooks_Detail DSebooks_Detail = Class_Manage.getModel_Detail(
                                getActivity(), array_model_ebookList_cat2.get(v.getId()).getBID());
                        if (DSebooks_Detail != null) {
                            Log.v("OnClickToDetailEbook", "DeserializeObject Success"
                                    + array_model_ebookList_cat2.get(v.getId()).getBID());
                            Intent intent = new Intent(getActivity(), Activity_Detail.class);
                            intent.putExtra("model", DSebooks_Detail);
                            intent.putExtra("DeserializeObject", "1");
                            StaticUtils.phoneValue = 1;
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            ((Activity) getActivity()).overridePendingTransition(
                                    R.anim.slide_in_right, R.anim.slide_out_left);

                        } else {
                            ebook_DetailApi = new Ebook_Detail(getActivity(),
                                    String.valueOf(array_model_ebookList_cat2.get(v.getId()).getBID()));
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
                        }
                    }
                });

        ebooks_imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ebooks_imageview.setAdjustViewBounds(true);
        ebooks_imageview.setLayoutParams(layoutParams);
        ebooks_imageview.setPadding(5, 5, 5, 5);
        ebooks_imageview.setId(currentSize);
        literature_list_linear.addView(ebooks_imageview);
    }

    @Override
    public void onPause() {
        dismissDialog();
        cancelTaskDownload();
        super.onPause();
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
