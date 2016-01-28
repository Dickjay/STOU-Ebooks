package com.porar.ebooks.stou.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf.Shelf;
import com.porar.ebook.adapter.MyAdapterViewPager_Comment;
import com.porar.ebook.control.Category_EbookOfFragment;
import com.porar.ebook.control.Comment_Detail;
import com.porar.ebook.control.DialogLogin_Show;
import com.porar.ebook.control.Dialog_Comment;
import com.porar.ebook.control.Dialog_Payment;
import com.porar.ebook.control.Dialog_ShowLargeImage;
import com.porar.ebook.control.Dialog_ViewComment;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.Ebook_BackIssue;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.Publisher_Detail;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import java.io.Serializable;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

@SuppressLint("NewApi")
public class Activity_Detail extends Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    private TextView txt_title, txt_rating_comment, txt_date, txt_page,
            txt_size, txt_wrtiers, txt_price,
            txt_description, txt_comment;
    private Activity_Detail mycontext;
    private ImageView img_covermain/* , img_publisher */;
    // private ImageView img_shelf;
    private ImageView img_cover1, img_cover2;
    private ImageView img_cover1shadow, img_cover2shadow;
    private RatingBar ratingBar;
    private Button btn_buy;

    private MyViewPager myViewPager;
    private Model_Ebooks_Detail ebooks_Detail;
    private ImageDownloader_forCache downloader_forCache;
    private LinearLayout layout_dotpage;
    private Dot_Pageslide dot_Pageslide;
    private Animation fade_in;
    private Comment_Detail comment_DetailAPI;
    private ArrayList<Model_Comment_List> comment_Lists;
    private AlertDialog alertDialog;
    int DeserializeObject = 0;
    public String tag = "Activity_Detail";
    public Handler handler = new Handler();
    DialogLogin_Show dialogLogin_Show;
    Fragment fragment;
    Dialog_Comment comment;
    Ebook_Detail ebook_DetailApi;
    Dialog_ViewComment commentphone;
    ScrollView parentScroll, childScroll;
    Ebook_BackIssue ebook_BackIssue;
    Category_EbookOfFragment ebookOfFragment;
    ImageView logo;
    // stou
    public static boolean alreadyBuyAndDirectToShelf = false;
    private ShareDialog shareDialogFacebook;

    private ImageView refresh_imageview, profile_imageview;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RegisterFacebook.unRegisterFacebook(Activity_Detail.this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        RegisterFacebook.InitFacebookSDK(Activity_Detail.this);

        mycontext = this;
        downloader_forCache = new ImageDownloader_forCache();

// end
        if (AppMain.isphone) {
            getBundle();
            setIdPhone();
            if (ebooks_Detail != null) {
                setdataDetailsPhone(ebooks_Detail);
            }
        } else {

            getBundle();
            setId();
//            setUpBackgroundActionBar();

            setViewPagerComment();
            if (ebooks_Detail != null) {
                setdataDetails(ebooks_Detail);
            }
        }

        txt_price.setTextColor(getApplicationContext().getResources().getColor(R.color.red_dark_stou));
    }


    public void getBG() {
        try {

            Model_Setting model_setting = Class_Manage
                    .getModel_Setting(mycontext);
            if (model_setting != null) {
                ((LinearLayout) mycontext.findViewById(R.id.detail_mainlayout))
                        .setBackgroundResource(model_setting
                                .getDrawable_backgroundStyle());
            }
        } catch (Exception e) {
// TODO: handle exception
        }
    }


    private void setBG2() throws NullPointerException {
        try {

            int[] id_drawble = {R.drawable.bg_main_default,
                    R.drawable.bg_finewood, R.drawable.bg_whilte_hardwood,
                    R.drawable.bg_wood, R.drawable.bg_wood_wall,
                    R.drawable.bg_light_green};

            ((LinearLayout) mycontext.findViewById(R.id.detail_mainlayout))
                    .setBackgroundResource(id_drawble[5]);

        } catch (Exception e) {
// TODO: handle exception
        }
    }

    private void setdataDetailsPhone(Model_Ebooks_Detail ebooks_Detail2) {
// setListBackIssue_Reccommend(ebooks_Detail2);

        txt_title.setText(ebooks_Detail2.getName() + "");

        txt_date.setText(ebooks_Detail2.getPrintOutDate() + "");
        txt_page.setText(ebooks_Detail2.getPages() + " Pages");
        txt_size.setText(ebooks_Detail2.getFileSize() + " MB");
// txt_publisher.setText(ebooks_Detail2.getPublisherName() + "");
        ratingBar.setRating(ebooks_Detail2.getRating());
        ratingBar.setEnabled(false);

        if (ebooks_Detail2.getDetail().length() > 0) {
            txt_description.setText("\t\t" + ebooks_Detail2.getDetail() + "");
        } else {
            txt_description.setText("\t\t -");
        }

        if (ebooks_Detail2.getWriters().toString().length() > 0) {
            txt_wrtiers.setText(ebooks_Detail2.getWriters() + "");
        } else {
            txt_wrtiers.setText("-");
        }


        if (ebooks_Detail2.getSRID() > 0) {       } else {        }
        if (ebooks_Detail2.getSeriesPrice() != 0.0F) {}



        String url_coverL = AppMain.DOWNLOAD_COVER_URL
                + ebooks_Detail2.getBID() + "/" + ebooks_Detail2.getCoverL();
        String url_moreImage1 = AppMain.DOWNLOAD_COVER_URL
                + ebooks_Detail2.getBID() + "/"
                + ebooks_Detail2.getMoreImage1L();
        String url_moreImage2 = AppMain.DOWNLOAD_COVER_URL
                + ebooks_Detail2.getBID() + "/"
                + ebooks_Detail2.getMoreImage2L();

        downloader_forCache.download(url_coverL, img_covermain);

        img_covermain.setOnClickListener(new onclickShowZoomImage(url_coverL));

        OnClickToDetail ToDetailPub = new OnClickToDetail();
        ToDetailPub.newInstant(ebooks_Detail2.getCID());
        identifyReadEbook(ebooks_Detail2);

    }

    private void identifyReadEbook(Model_Ebooks_Detail ebooks_Detail2) {
        if (ebooks_Detail2.getBCode().equals("0")) {
            if (ebooks_Detail2.getPrice().contains("$")) {
                txt_price.setText(ebooks_Detail2.getOurPrice() + " �ҷ");
                btn_buy.setText(getString(R.string.addtoshelf) + "\t "
                        + ebooks_Detail2.getOurPrice() + "\t BATH");
                btn_buy.setOnClickListener(new OnClickBuy(ebooks_Detail.getBID()));
            } else {
                txt_price.setText(ebooks_Detail2.getPrice());
                btn_buy.setText(getString(R.string.addtoshelf_free));
                btn_buy.setOnClickListener(new OnClickAddToshelf(ebooks_Detail.getBID()));
            }
            if (ebooks_Detail2.getAlreadyInCart()) {
                btn_buy.setEnabled(true);
                btn_buy.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_addto_grey));
                btn_buy.setText(getString(R.string.already));
                btn_buy.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
// TODO Auto-generated method stub
                        goShelf();
                    }
                });
            }
        }else{
            if (StaticUtils.arrayList_BCODE != null) {
                for (int currentSize = 0; currentSize < StaticUtils.arrayList_BCODE.size(); currentSize++) {
                    if (!StaticUtils.arrayList_BCODE.get(currentSize).equals(ebooks_Detail2.getBCode())) {
                        disableButtonBuyEbook(ebooks_Detail2);
                    }else{
                        enableButtonBuyEbook(ebooks_Detail2);
                        break;
                    }
                }
            }
        }

    }

    private void disableButtonBuyEbook(Model_Ebooks_Detail ebooks_Detail2){
        btn_buy.setEnabled(false);
        btn_buy.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_addto_grey));
        btn_buy.setText(getString(R.string.addtoshelf));
        txt_price.setText(getString(R.string.cannot_read_ebook));
    }
    private void enableButtonBuyEbook(Model_Ebooks_Detail ebooks_Detail2){
        if (ebooks_Detail2.getPrice().contains("$")) {
            txt_price.setText(ebooks_Detail2.getOurPrice() + " �ҷ");
            btn_buy.setText(getString(R.string.addtoshelf) + "\t "
                    + ebooks_Detail2.getOurPrice() + "\t BATH");
            btn_buy.setOnClickListener(new OnClickBuy(ebooks_Detail.getBID()));
        } else {
            txt_price.setText(ebooks_Detail2.getPrice());
            btn_buy.setText(getString(R.string.addtoshelf_free));
            btn_buy.setEnabled(true);
            btn_buy.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_button_large));
            btn_buy.setOnClickListener(new OnClickAddToshelf(ebooks_Detail.getBID()));
        }


        if (ebooks_Detail2.getAlreadyInCart()) {
            btn_buy.setEnabled(true);
            btn_buy.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.btn_addto_grey));
            btn_buy.setText(getString(R.string.already));
            btn_buy.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
// TODO Auto-generated method stub
                    goShelf();
                }
            });
        }
    }
    public void goShelf() {

// AppMain.pList_default_ebookshelf = null;
        alertDialog = new AlertDialog.Builder(mycontext).create();
        alertDialog.setTitle(AppMain.getTag());
        alertDialog.setMessage(getResources().getString(R.string.already_add_book_to_shelf));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.go_to_book_shelf),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// back to shelf
                        AppMain.isGotoshelf = true;
                        finish();
                        mycontext.overridePendingTransition(
                                R.anim.slide_in_left, R.anim.slide_out_right);
                        MyAdapterViewPager_BookShelf.seteNumtype(Shelf.news);
                        dialog.dismiss();
                        System.gc();
                        Activity_MainTab.viewPager.setCurrentItem(1);
                    }
                });
        alertDialog.show();

    }

    private void setIdPhone() {
        parentScroll = (ScrollView) findViewById(R.id.detail_scrollView_parent);
        childScroll = (ScrollView) findViewById(R.id.detail_scrollView_child);
        ratingBar = (RatingBar) findViewById(R.id.detail_ratingStar);
        img_covermain = (ImageView) findViewById(R.id.detail_image_cover);
        refresh_imageview = (ImageView) findViewById(R.id.refresh_imageview);
        profile_imageview = (ImageView) findViewById(R.id.profile_imageview);

        txt_title = (TextView) findViewById(R.id.detail_text_title);
        txt_date = (TextView) findViewById(R.id.detail_text_datetime);
        txt_page = (TextView) findViewById(R.id.detail_text_page);
        txt_size = (TextView) findViewById(R.id.detail_text_size);
        txt_wrtiers = (TextView) findViewById(R.id.detail_text_writers);
        txt_price = (TextView) findViewById(R.id.detail_text_price_bth);
        txt_description = (TextView) findViewById(R.id.detail_text_descriptions);
        btn_buy = (Button) findViewById(R.id.detail_btn_price_bth);
        txt_title.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_date.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_page.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_size.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_wrtiers.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_price.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_description.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));

        btn_buy.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                childScroll.getParent().requestDisallowInterceptTouchEvent(
                        false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        profile_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentBox();
            }
        });
        refresh_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel task
                if (AppMain.isphone) {
                    // phone

                } else {
                    if (comment_DetailAPI != null) {
                        if (comment_DetailAPI.CancelTask()) {
                            Log.v("CancelTask", "true");
                        }
                    }
                }
                finish();
                mycontext.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);

                System.gc();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.v(tag, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(tag, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(tag, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(tag, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    private class OnClickBuy implements OnClickListener {
        int bid = 0;
        String posturl;

        public OnClickBuy(int bid) {
            this.bid = bid;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btn_buy.getId()) {

                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                    Log.w("OnClickBuy", "not login");
                    dialogLogin_Show = new DialogLogin_Show(mycontext,
                            R.style.PauseDialog, mycontext);
                    dialogLogin_Show.setCancelable(false);
                    dialogLogin_Show
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    if (Shared_Object.getCustomerDetail
                                            .getCID() <= 0) {
//
                                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                            Log.i("", "facebook is login");
                                            StaticUtils.setFacebooklogin(true);
                                        } else {
                                            Log.i("", "facebook not login");
                                            StaticUtils.setFacebooklogin(false);
                                        }
                                    } else {
                                        if (AppMain.isphone) {
                                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                                Log.i("", "facebook is login");
                                                StaticUtils
                                                        .setFacebooklogin(true);
                                            } else {
                                                Log.i("", "facebook not login");
                                                StaticUtils
                                                        .setFacebooklogin(false);
                                            }
                                            LoadDatadetail();
                                        } else {
                                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                                Log.i("", "facebook is login");
                                                StaticUtils
                                                        .setFacebooklogin(true);
                                            } else {
                                                Log.i("", "facebook not login");
                                                StaticUtils
                                                        .setFacebooklogin(false);
                                            }
                                            LoadDatadetail();
                                        }
// login success

                                        Log.w("dialogLogin_Show",
                                                "login success");
                                    }
                                }

                            });
                    dialogLogin_Show.show();
                } else {
// posturl = "http://ebooks.in.th/payment-android.aspx";
                    posturl = AppMain.BUY_BOOK_URL;
                    posturl += "?cid="
                            + Shared_Object.getCustomerDetail.getCID();
                    posturl += "&did=" + Shared_Object.getDeviceID(mycontext);
                    posturl += "&bid=" + bid;

                    Dialog_Payment dialog_Payment = new Dialog_Payment(
                            mycontext, R.style.PauseDialog, posturl);
                    dialog_Payment.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.v("dialog_Payment", "onCancel");
                            onCancelLoadDataAndSerializable();
                        }
                    });
                    dialog_Payment.show();
                }

            }
// if (v.getId() == btn_buy_subcrip.getId()) {
// // buy
// }
// if (v.getId() == btn_buy_print.getId()) {
// // buy
// }

        }

        public void onCancelLoadDataAndSerializable() {

            ebook_DetailApi = new Ebook_Detail(mycontext, String.valueOf(bid));
            ebook_DetailApi.setOnListener(new Throw_IntefacePlist() {

                @Override
                public void PList_Detail_Comment(plist.xml.PList Plistdetail,
                                                 plist.xml.PList Plistcomment, final ProgressDialog pd) {

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
                                mycontext.getApplicationContext(),
                                ebooks_Detail, ebooks_Detail.getBID())) {
                            Log.v("OnClickToDetailEbook",
                                    "SerializeObject Success"
                                            + ebooks_Detail.getBID());
                        }

                        if (AppMain.isphone) {
                            setdataDetailsPhone(ebooks_Detail);
                            if (ebooks_Detail.getAlreadyInCart()) {
                                ContinusOrBacktoShelf();
                            }
                        } else {
                            setdataDetails(ebooks_Detail);
                            if (ebooks_Detail.getAlreadyInCart()) {
                                ContinusOrBacktoShelf();
                            }
                        }

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

                        alertDialog = new AlertDialog.Builder(mycontext)
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage("WARNING: An error has occurred. Please to try again ?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "Retry", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        pd.show();
                                        dialog.dismiss();
                                        System.gc();
                                        ebook_DetailApi.LoadEbooksDetailAPI();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                "Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
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

        public void LoadDatadetail() {

// DerializeObject
            Model_Ebooks_Detail DSebooks_Detail = Class_Manage
                    .getModel_DetailByNever(mycontext.getApplicationContext(),
                            bid);
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success" + bid);

                if (AppMain.isphone) {
                    setdataDetailsPhone(DSebooks_Detail);
                } else {
                    setdataDetails(DSebooks_Detail);
                }

            } else {
                ebook_DetailApi = new Ebook_Detail(mycontext,
                        String.valueOf(bid));
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
                                    mycontext.getApplicationContext(),
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());
                            }

                            if (AppMain.isphone) {
                                setdataDetailsPhone(ebooks_Detail);
                            } else {
                                setdataDetails(ebooks_Detail);
                            }

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

                            alertDialog = new AlertDialog.Builder(mycontext)
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

    public void ContinusOrBacktoShelf() {
// AppMain.pList_default_ebookshelf = null;
        alertDialog = new AlertDialog.Builder(mycontext).create();
        alertDialog.setTitle(AppMain.getTag());
        alertDialog.setMessage(getResources().getString(R.string.add_to_shelf_success));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.buy_ebook),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.gc();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.go_to_book_shelf),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// back to shelf
                        AppMain.isGotoshelf = true;
                        finish();
                        mycontext.overridePendingTransition(
                                R.anim.slide_in_left, R.anim.slide_out_right);
                        MyAdapterViewPager_BookShelf.seteNumtype(Shelf.news);
                        dialog.dismiss();
                        System.gc();
                        Activity_MainTab.viewPager.setCurrentItem(1);
                    }
                });
        alertDialog.show();
    }

    class OnClickAddToshelf implements OnClickListener {
        int bid = 0;
        String addcartURL;
        LoadAPIResultString apiResultString;

        public OnClickAddToshelf(int bid) {
            this.bid = bid;

            addcartURL = AppMain.ADD_TO_SHELF_URL;
            addcartURL += "?bid=" + bid;
// addcartURL += "&did=" + Shared_Object.getDeviceID(mycontext);
            addcartURL += "&cid=" + Shared_Object.getCustomerDetail.getCID();
            addcartURL += "&paid=0";

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btn_buy.getId()) {
                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                    Log.w("OnClickBuy", "not login");
                    dialogLogin_Show = new DialogLogin_Show(mycontext,
                            R.style.PauseDialog, mycontext);
                    dialogLogin_Show.setCancelable(false);
                    dialogLogin_Show
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    if (Shared_Object.getCustomerDetail
                                            .getCID() <= 0) {
//
                                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                            Log.i("", "facebook is login");
                                            StaticUtils.setFacebooklogin(true);
                                        } else {
                                            Log.i("", "facebook not login");
                                            StaticUtils.setFacebooklogin(false);
                                        }
                                    } else {
                                        if (AppMain.isphone) {
                                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                                Log.i("", "facebook is login");
                                                StaticUtils
                                                        .setFacebooklogin(true);
                                            } else {
                                                Log.i("", "facebook not login");
                                                StaticUtils
                                                        .setFacebooklogin(false);
                                            }
                                            LoadDatadetail();
                                        } else {
                                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                                Log.i("", "facebook is login");
                                                StaticUtils
                                                        .setFacebooklogin(true);
                                            } else {
                                                Log.i("", "facebook not login");
                                                StaticUtils
                                                        .setFacebooklogin(false);
                                            }
                                            LoadDatadetail();
                                        }
// login success

                                        Log.w("dialogLogin_Show",
                                                "login success");
                                    }
                                }

                            });
                    dialogLogin_Show.show();
                } else {
                    btn_buy.setEnabled(false);
                    btn_buy.setText("Loading...");
                    apiResultString = new LoadAPIResultString();
                    apiResultString
                            .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                @Override
                                public void completeResult(String result) {
                                    if (result != null) {
                                        if (result.length() > 0) {
                                            int responseInt = Integer
                                                    .parseInt(result);
                                            if (responseInt == 1) {
                                                btn_buy.setText(getString(R.string.already));
                                                btn_buy.setEnabled(false);
                                                Log.e(AppMain.getTag(),
                                                        "Page_Detail - AddCart[Complete]");

                                                if (DeserializeObject == 1) {
                                                    Model_Ebooks_Detail modelEdit = ebooks_Detail;
                                                    modelEdit
                                                            .setAlreadyInCart(true);

                                                    if (Class_Manage
                                                            .SaveModel_Detail(
                                                                    mycontext,
                                                                    modelEdit,
                                                                    ebooks_Detail
                                                                            .getBID())) {
                                                        Log.v("OnClickToDetailEbook",
                                                                "SerializeObject Success"
                                                                        + ebooks_Detail
                                                                        .getBID());

                                                    }
                                                } else {
                                                    ebooks_Detail
                                                            .setAlreadyInCart(true);

                                                    if (Class_Manage
                                                            .SaveModel_Detail(
                                                                    mycontext,
                                                                    ebooks_Detail,
                                                                    ebooks_Detail
                                                                            .getBID())) {
                                                        Log.v("OnClickToDetailEbook",
                                                                "SerializeObject Success"
                                                                        + ebooks_Detail
                                                                        .getBID());
                                                    }

                                                }

// STOU
// if (AppMain.isphone) {
// Activity_Tab.mTabHost.setCurrentTabByTag("Tab5");
// }else {
// Activity_Tab.mTabHost.setCurrentTabByTag("Tab4");
// }

// dialogGoDirectToShelf();

// end
                                                ContinusOrBacktoShelf();
                                            } else {
                                                Toast.makeText(
                                                        Activity_Detail.this,
                                                        "Function Not Response : ",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    } else {

                                        Toast.makeText(Activity_Detail.this,
                                                "An error has ocurred.",
                                                Toast.LENGTH_LONG).show();
                                        btn_buy.setEnabled(true);
                                        btn_buy.setText(getString(R.string.addtoshelf_free));

                                    }

                                }

// private void dialogGoDirectToShelf() {
//
//
// alertDialog = new
// AlertDialog.Builder(mycontext).create();
// alertDialog.setTitle(AppMain.getTag());
// alertDialog.setMessage("��ҹ��ͧ�����ҹ˹ѧ���������� �͹���������� ?");
// alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
// "�׹�ѹ", new
// DialogInterface.OnClickListener() {
//
// @Override
// public void onClick(DialogInterface dialog,
// int which) {
//
//
// if (AppMain.isphone) {
// // phone
// } else {
// if (comment_DetailAPI != null) {
// if (comment_DetailAPI.CancelTask()) {
// Log.v("CancelTask", "true");
// }
// }
// }
//
// alreadyBuyAndDirectToShelf = true;
// finish();
// mycontext.overridePendingTransition(R.anim.slide_in_left,
// R.anim.slide_out_right);
// }
// });
// alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
// "¡��ԡ", new
// DialogInterface.OnClickListener() {
//
// @Override
// public void onClick(DialogInterface dialog,
// int which) {
//
// dialog.dismiss();
// }
// });
// alertDialog.show();
// }
                            });

// Log.e("TOoper", "addcartURL : " + addcartURL);

                    apiResultString.execute(addcartURL);
                }
// AddToshelf Free
// check login

            }
        }

        public void LoadDatadetail() {

// DerializeObject
            Model_Ebooks_Detail DSebooks_Detail = Class_Manage
                    .getModel_DetailByNever(mycontext.getApplicationContext(),
                            bid);
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success" + bid);

                if (AppMain.isphone) {
                    setdataDetailsPhone(DSebooks_Detail);
                } else {
                    setdataDetails(DSebooks_Detail);
                }

            } else {
                ebook_DetailApi = new Ebook_Detail(mycontext,
                        String.valueOf(bid));
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
                                    mycontext.getApplicationContext(),
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());
                            }

                            if (AppMain.isphone) {
                                setdataDetailsPhone(ebooks_Detail);
                            } else {
                                setdataDetails(ebooks_Detail);
                            }

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

                            alertDialog = new AlertDialog.Builder(mycontext)
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


    private void setdataDetails(Model_Ebooks_Detail ebooks_Detail) {
// setListBackIssue_Reccommend(ebooks_Detail);
        txt_title.setText(ebooks_Detail.getName() + "");
        txt_date.setText(ebooks_Detail.getPrintOutDate() + "");
        txt_page.setText(ebooks_Detail.getPages() + " Pages");
        txt_size.setText(ebooks_Detail.getFileSize() + " MB");
// txt_publisher.setText(ebooks_Detail.getPublisherName() + "");
        txt_description.setText("\t\t" + ebooks_Detail.getDetail() + "");
        ratingBar.setRating(ebooks_Detail.getRating());
        ratingBar.setEnabled(false);

        if (ebooks_Detail.getWriters().toString().length() > 0) {
            txt_wrtiers.setText(ebooks_Detail.getWriters() + "");
        } else {
            txt_wrtiers.setText("-");
        }

        if (ebooks_Detail.getSRID() > 0) {
        } else {
        }
        if (ebooks_Detail.getSeriesPrice() != 0.0F) {
        }
        String url_publishercover = "http://www.ebooks.in.th/publishers/"
                + ebooks_Detail.getCID() + "/" + ebooks_Detail.getLogo();
        String url_coverL = AppMain.DOWNLOAD_COVER_URL + ebooks_Detail.getBID()
                + "/" + ebooks_Detail.getCoverL();
        String url_moreImage1 = AppMain.DOWNLOAD_COVER_URL
                + ebooks_Detail.getBID() + "/" + ebooks_Detail.getMoreImage1L();
        String url_moreImage2 = AppMain.DOWNLOAD_COVER_URL
                + ebooks_Detail.getBID() + "/" + ebooks_Detail.getMoreImage2L();
        downloader_forCache.download(url_coverL, img_covermain);
        downloader_forCache.download(url_moreImage1, img_cover1,
                img_cover1shadow);
        downloader_forCache.download(url_moreImage2, img_cover2,
                img_cover2shadow);

        img_covermain.setOnClickListener(new onclickShowZoomImage(url_coverL));
        img_cover1.setOnClickListener(new onclickShowZoomImage(url_moreImage1));
        img_cover2.setOnClickListener(new onclickShowZoomImage(url_moreImage2));

        OnClickToDetail ToDetailPub = new OnClickToDetail();
        ToDetailPub.newInstant(ebooks_Detail.getCID());
        identifyReadEbook(ebooks_Detail);
    }

    private class OnClickToDetail implements OnClickListener {
        Model_Publisher_Detail model_Publisher_Detail;
        Publisher_Detail publisher_Detail;
        AlertDialog alertDialog;
        int cid;

        public void newInstant(int publisherid) {
            cid = publisherid;
        }

        @Override
        public void onClick(View v) {

            publisher_Detail = new Publisher_Detail(mycontext,
                    String.valueOf(cid));
            publisher_Detail.setOnListener(new Throw_IntefacePlist() {

                @Override
                public void PList_Detail_Comment(
                        plist.xml.PList plist_Publisherdetail,
                        plist.xml.PList plist_PublisherEbook,
                        final ProgressDialog pd) {

                    try {
                        for (PListObject each : (Array) plist_Publisherdetail
                                .getRootElement()) {
                            model_Publisher_Detail = new Model_Publisher_Detail(
                                    each);
                        }

                        for (PListObject each : (Array) plist_PublisherEbook
                                .getRootElement()) {
                            AppMain.pList_default_publisher_ebook = plist_PublisherEbook;
                        }

                        Intent intent = new Intent(mycontext,
                                ActivityDetail_Publisher.class);
                        intent.putExtra("model", model_Publisher_Detail);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mycontext.startActivity(intent);
                        mycontext.overridePendingTransition(
                                R.anim.slide_in_right, R.anim.slide_out_left);

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

                        alertDialog = new AlertDialog.Builder(mycontext)
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage("WARNING: An error has ocurred. Please to try again ?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "Retry", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        pd.show();
                                        dialog.dismiss();
                                        System.gc();
                                        publisher_Detail.LoadEbooksDetailAPI();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                "Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();

                    }

                }

                @Override
                public void PList(plist.xml.PList resultPlist, ProgressDialog pd) {
// TODO Auto-generated method stub

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
            publisher_Detail.LoadEbooksDetailAPI();

        }

    }

    public class onclickShowZoomImage implements OnClickListener {
        private final String url;

        public onclickShowZoomImage(String url_moreImage1) {
            this.url = url_moreImage1;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == img_covermain.getId()) {
                new Dialog_ShowLargeImage(mycontext, R.style.PauseDialog, url)
                        .show();
            }

            if (!AppMain.isphone) {

                if (v.getId() == img_cover1.getId()) {
                    new Dialog_ShowLargeImage(mycontext, R.style.PauseDialog,
                            url).show();
                }
                if (v.getId() == img_cover2.getId()) {
                    new Dialog_ShowLargeImage(mycontext, R.style.PauseDialog,
                            url).show();
                }
            }

        }

    }

    public void removeViewDotpage() {
        layout_dotpage.removeAllViews();
    }

    private void setViewPagerComment() {
// load API COMMENT
        comment_Lists = new ArrayList<Model_Comment_List>();
        comment_DetailAPI = new Comment_Detail(mycontext,
                String.valueOf(ebooks_Detail.getBID()));
        comment_DetailAPI.setOnListener(new Throw_IntefacePlist() {

            @Override
            public void PList_Detail_Comment(PList Plistdetail,
                                             PList Plistcomment, ProgressDialog pd) {
                // TODO Auto-generated method stub

            }

            @Override
            public void PList(PList resultPlist, ProgressDialog pd) {

                try {
                    for (PListObject each : (Array) resultPlist
                            .getRootElement()) {
                        comment_Lists.add(new Model_Comment_List(each));
                    }

                    txt_comment.setText(" " + comment_Lists.size()
                            + " Comments");
                    txt_rating_comment.setText(" " + comment_Lists.size()
                            + " Comments");

                    if (pd != null) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                } catch (NullPointerException e) {
                    if (pd != null) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                    if (comment_Lists == null) {
                        alertDialog = new AlertDialog.Builder(mycontext)
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage("WARNING:Comment An error has ocurred. Please to try again ?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "Retry", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                        comment_DetailAPI.LoadEbooksDetailAPI();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                "Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }

                }
                removeViewDotpage();
                dot_Pageslide = new Dot_Pageslide(mycontext);
                layout_dotpage.addView(dot_Pageslide
                        .setImage_slide(comment_Lists.size()));
                if (comment_Lists.size() > 0) {
                    dot_Pageslide.setHeighlight(0);
                }

                myViewPager.setAdapter(new MyAdapterViewPager_Comment(
                        mycontext, comment_Lists));
                myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        dot_Pageslide.setHeighlight(position);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        // TODO Auto-generated method stub

                    }
                });

            }

            @Override
            public void StartLoadPList() {

                TextView textloading = new TextView(mycontext);
                textloading.setText("Loading...");
                layout_dotpage.addView(textloading);

            }

            @Override
            public void PList_TopPeriod(plist.xml.PList Plist1,
                                        plist.xml.PList Plist2, ProgressDialog pd) {
                // TODO Auto-generated method stub

            }
        });
        comment_DetailAPI.LoadEbooksDetailAPI();

    }

    private void setId() {
        refresh_imageview = (ImageView) findViewById(R.id.refresh_imageview);
        profile_imageview = (ImageView) findViewById(R.id.profile_imageview);
        profile_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentBox();

            }
        });
        refresh_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel task
                if (AppMain.isphone) {
                    // phone

                } else {
                    if (comment_DetailAPI != null) {
                        if (comment_DetailAPI.CancelTask()) {
                            Log.v("CancelTask", "true");
                        }
                    }
                }
                finish();
                mycontext.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);

                System.gc();
            }
        });
        // txt_backissue = (TextView) findViewById(R.id.detail_txt_backissue);
        // horizontalListView = (HorizontalListView)
        // findViewById(R.id.detail_horizallistview);
        parentScroll = (ScrollView) findViewById(R.id.detail_scrollView_parent);
        childScroll = (ScrollView) findViewById(R.id.detail_scrollView_child);
        layout_dotpage = (LinearLayout) findViewById(R.id.detail_linear_dotpage);
        // img_publisher = (ImageView) findViewById(R.id.detail_img_publisher);
        img_cover1shadow = (ImageView) findViewById(R.id.detail_img_ebook_shadow1);
        img_cover2shadow = (ImageView) findViewById(R.id.detail_img_ebook_shadow2);
        img_cover1 = (ImageView) findViewById(R.id.detail_img_ebook_cover1);
        img_cover2 = (ImageView) findViewById(R.id.detail_img_ebook_cover2);
        myViewPager = (MyViewPager) findViewById(R.id.detail_myViewPager_comment);
        btn_buy = (Button) findViewById(R.id.detail_btn_price_bth);
        ratingBar = (RatingBar) findViewById(R.id.detail_ratingStar);
        txt_rating_comment = (TextView) findViewById(R.id.detail_text_count_comment);
        img_covermain = (ImageView) findViewById(R.id.detail_image_cover);
        txt_title = (TextView) findViewById(R.id.detail_text_title);
        txt_date = (TextView) findViewById(R.id.detail_text_datetime);
        txt_page = (TextView) findViewById(R.id.detail_text_page);
        txt_size = (TextView) findViewById(R.id.detail_text_size);
        txt_wrtiers = (TextView) findViewById(R.id.detail_text_writers);
        txt_price = (TextView) findViewById(R.id.detail_text_price_bth);
        txt_description = (TextView) findViewById(R.id.detail_text_descriptions);
        txt_comment = (TextView) findViewById(R.id.detail_text_comment);
        txt_rating_comment.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_price.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_wrtiers.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_size.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_page.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_date.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_description.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_title.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));
        txt_comment.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));

        btn_buy.setTypeface(StaticUtils.getTypeface(mycontext,
                Font.DB_HelvethaicaMon_X));

        setIDTitle();
        findViewById(R.id.add_comment_image).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        commentBox();

                    }
                });
        // END

        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                childScroll.getParent().requestDisallowInterceptTouchEvent(
                        false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void setIDTitle() {
        try {

            if (!StaticUtils.isAMasterDegree) {

            } else {
                txt_title.setTextColor(getResources().getColor(
                        R.color.green_stou));
            }

            if (StaticUtils.shelfID == 0) {

            } else if (StaticUtils.shelfID == 1) {
                txt_title
                        .setTextColor(getResources().getColor(R.color.tabDark));
            } else {
                txt_title.setTextColor(getResources().getColor(
                        R.color.green_stou));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private class onClickPublisher implements OnClickListener {

        @Override
        public void onClick(View v) {
            // goPublisher
        }

    }

    private void getBundle() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            try {
                ebooks_Detail = (Model_Ebooks_Detail) b.get("model");
                if (b.get("DeserializeObject") != null) {
                    DeserializeObject = Integer.parseInt(b.get(
                            "DeserializeObject").toString());
                }

            } catch (NullPointerException e) {
                Toast.makeText(mycontext, "WARNING: Data Not Found",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        // STOU
        if (!AppMain.isphone) {

            MenuItem item = menu.findItem(R.id.action_comment);
            item.setVisible(false);
        }
        // end
        return true;
    }

    public void initShareFaceook() {
        if (RegisterFacebook.hasPublishPermission()) {

        } else {
            RegisterFacebook.ReadPublishPermissionFacebookActivity(Activity_Detail.this);
        }
        shareDialogFacebook = new ShareDialog(Activity_Detail.this);
        shareDialogFacebook.registerCallback(
                RegisterFacebook.mCallbackManager, shareFacebookCallback);
    }

    public void ShareEbookPublisher() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setImageUrl(
                        Uri.parse(AppMain.DOWNLOAD_COVER_URL + ebooks_Detail.getBID() + "/"
                                + ebooks_Detail.getCoverL()))
                .setContentTitle(ebooks_Detail.getName())
                .setContentDescription("http://www.ebookstou.org/")
                .setContentUrl(
                        Uri.parse("http://www.ebookstou.org/")).build();
        showContent(shareDialogFacebook, content);
    }

    private void showContent(ShareDialog shareDialog, ShareLinkContent content) {
        shareDialog.show(content);
    }

    private FacebookCallback<Sharer.Result> shareFacebookCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            Toast.makeText(Activity_Detail.this, "Posted Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(Activity_Detail.this, "Publish cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(Activity_Detail.this.getApplicationContext(), "Error posting story", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                for (int i = 0; i < item.getSubMenu().size(); i++) {

                    item.getSubMenu()
                            .getItem(i)
                            .setOnMenuItemClickListener(
                                    new OnMenuItemClickListener() {

                                        @SuppressLint("DefaultLocale")
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {

                                            if (item.getTitle().toString()
                                                    .toLowerCase()
                                                    .contains("facebook")) {
                                                if (StaticUtils.getFacebooklogin()) {
                                                    initShareFaceook();
                                                    ShareEbookPublisher();
                                                } else {
                                                    Toast.makeText(
                                                            mycontext,
                                                            "Not use facebook account",
                                                            Toast.LENGTH_LONG)
                                                            .show();
                                                    Toast.makeText(
                                                            mycontext,
                                                            "Please login with facebook account",
                                                            Toast.LENGTH_LONG)
                                                            .show();

                                                }

                                            }
                                            // doShare
                                            return false;
                                        }
                                    });
                }

                break;
            case R.id.action_comment:

                commentBox();


                break;

            default:
                break;
        }

        return true;
    }


    private void commentBox() {

        // doComment
        if (Shared_Object.getCustomerDetail.getCID() > 0) {
            if (AppMain.isphone) {
                commentphone = new Dialog_ViewComment(mycontext,
                        R.style.PauseDialog, ebooks_Detail.getBID());
                commentphone.show();

            } else {
                comment = new Dialog_Comment(mycontext, R.style.PauseDialog,
                        ebooks_Detail.getBID());
                comment.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        if (comment.isCancel()) {
                            setViewPagerComment();
                        }
                        Log.e("", "Dialog_Comment onCancel");
                    }
                });
                comment.show();
            }

        } else {
            dialogLogin_Show = new DialogLogin_Show(mycontext,
                    R.style.PauseDialog, mycontext);
            dialogLogin_Show.setCancelable(false);
            dialogLogin_Show.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                        //
                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                            Log.i("", "facebook is login");
                            StaticUtils.setFacebooklogin(true);
                        } else {
                            Log.i("", "facebook not login");
                            StaticUtils.setFacebooklogin(false);
                        }
                    } else {
                        // login success
                        if (AppMain.isphone) {
                            // phone

                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                Log.i("", "facebook is login");
                                StaticUtils.setFacebooklogin(true);
                            } else {
                                Log.i("", "facebook not login");
                                StaticUtils.setFacebooklogin(false);
                            }
                            LoadDatadetail();
                            // setdata

                        } else {
                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                Log.i("", "facebook is login");
                                StaticUtils.setFacebooklogin(true);
                            } else {
                                Log.i("", "facebook not login");
                                StaticUtils.setFacebooklogin(false);
                            }
                            LoadDatadetail();
                            comment = new Dialog_Comment(mycontext,
                                    R.style.PauseDialog, ebooks_Detail.getBID());
                            comment.setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    if (comment.isCancel()) {
                                        setViewPagerComment();
                                    }
                                    Log.e("", "Dialog_Comment onCancel");
                                }
                            });
                            comment.show();
                        }
                    }
                }

                public void LoadDatadetail() {

                    // DerializeObject
                    Model_Ebooks_Detail DSebooks_Detail = Class_Manage
                            .getModel_DetailByNever(
                                    mycontext.getApplicationContext(),
                                    ebooks_Detail.getBID());
                    if (DSebooks_Detail != null) {
                        Log.v("OnClickToDetailEbook",
                                "DeserializeObject Success"
                                        + ebooks_Detail.getBID());

                        if (AppMain.isphone) {
                            setdataDetailsPhone(DSebooks_Detail);
                        } else {
                            setdataDetails(DSebooks_Detail);
                        }

                    } else {
                        ebook_DetailApi = new Ebook_Detail(mycontext, String
                                .valueOf(ebooks_Detail.getBID()));
                        ebook_DetailApi
                                .setOnListener(new Throw_IntefacePlist() {

                                    @Override
                                    public void PList_Detail_Comment(
                                            plist.xml.PList Plistdetail,
                                            plist.xml.PList Plistcomment,
                                            final ProgressDialog pd) {

                                    }

                                    @Override
                                    public void PList(
                                            plist.xml.PList resultPlist,
                                            final ProgressDialog pd) {

                                        try {
                                            for (PListObject each : (Array) resultPlist
                                                    .getRootElement()) {
                                                ebooks_Detail = new Model_Ebooks_Detail(
                                                        each);
                                            }

                                            // SerializeObject
                                            ebooks_Detail.setDateTime();
                                            if (Class_Manage.SaveModel_Detail(
                                                    mycontext
                                                            .getApplicationContext(),
                                                    ebooks_Detail,
                                                    ebooks_Detail.getBID())) {
                                                Log.v("OnClickToDetailEbook",
                                                        "SerializeObject Success"
                                                                + ebooks_Detail
                                                                .getBID());
                                            }

                                            if (AppMain.isphone) {
                                                setdataDetailsPhone(ebooks_Detail);
                                            } else {
                                                setdataDetails(ebooks_Detail);
                                            }

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

                                            alertDialog = new AlertDialog.Builder(
                                                    mycontext).create();
                                            alertDialog.setTitle(AppMain
                                                    .getTag());
                                            alertDialog
                                                    .setMessage("WARNING: An error has occurred. Please to try again ?");
                                            alertDialog
                                                    .setButton(
                                                            AlertDialog.BUTTON_POSITIVE,
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
                                            alertDialog
                                                    .setButton(
                                                            AlertDialog.BUTTON_NEGATIVE,
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
                                    public void PList_TopPeriod(
                                            plist.xml.PList Plist1,
                                            plist.xml.PList Plist2,
                                            ProgressDialog pd) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        ebook_DetailApi.LoadEbooksDetailAPI();
                    }
                }
            });
            dialogLogin_Show.show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    // cancel task
                    if (AppMain.isphone) {
                        // phone
                    } else {
                        if (comment_DetailAPI != null) {
                            if (comment_DetailAPI.CancelTask()) {
                                Log.v("CancelTask", "true");
                            }
                        }
                    }

                    finish();
                    mycontext.overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
            }

        }
        return false;

    }
}
