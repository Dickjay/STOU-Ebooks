package com.porar.ebook.control;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
import com.porar.ebook.adapter.MyAdapterViewPager_Comment;
import com.porar.ebook.adapter.MyAdapterViewPager_Comment_Shelf;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Comment_List;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_EBook_Shelf_List.StatusFlag;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.stou.activity.ActivityDetail_Publisher;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.EbooksShelfHeaderFile;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import java.io.File;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

//import android.util.Log;

public class Dialog_ShowLongClick extends Dialog {
    private TextView txt_title, txt_rating_comment, txt_date, txt_page,
            txt_size, txt_wrtiers, txt_price, txt_publisher, txt_other,
            txt_description, txt_comment;
    private Model_EBook_Shelf_List FavouriteModel;
    private Model_EBook_Shelf_List RemoveFavouriteModel;
    private Model_EBook_Shelf_List DeleteModel;
    ImageDownloader_forCache downloader_forCache;
    Dialog_ShowLongClick dialog_ShowLongClick;
    Model_EBook_Shelf_List model;
    RatingBar ratingBar;
    ImageView cover1, cover2, cover3, image_facebook, image_favorite,
            image_comment, img_publisher;
    Animation fade_in;
    Dot_Pageslide dot_Pageslide;
    ArrayList<Model_Comment_List> comment_Lists;
    Comment_Detail comment_DetailAPI;
    AlertDialog alertDialog;
    private LinearLayout layout_dotpage;
    MyAdapterViewPager_Comment adapterViewPager_Comment;
    private MyViewPager myViewPager;
    FragmentActivity fActivity;
    Dialog_Comment comment;
    Dialog_ViewComment viewComment;
    Model_Ebooks_Detail ebooks_Detail;
    ScrollView parentScroll, childScroll;
    private ShareDialog shareDialogFacebook;

    public Dialog_ShowLongClick(Context context, int theme,
                                Model_EBook_Shelf_List model, FragmentActivity fActivity) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog_ShowLongClick = this;
        this.model = model;
        this.fActivity = fActivity;
        downloader_forCache = new ImageDownloader_forCache();
        init();
    }

    public void setDetail(Model_Ebooks_Detail ebooks_Detail,
                          int DeserializeObject) {
        if (AppMain.isphone) {
            this.ebooks_Detail = ebooks_Detail;
            if (DeserializeObject == 1) {
                // Log.e("", "From DeserializeObject");
            }
            txt_title.setText(ebooks_Detail.getName() + "");
            txt_date.setText(ebooks_Detail.getPrintOutDate() + "");
            txt_page.setText(ebooks_Detail.getPages() + " Pages");
            txt_size.setText(ebooks_Detail.getFileSize() + " MB");
            txt_publisher.setText(ebooks_Detail.getPublisherName() + "");
            txt_description.setText("\t\t" + ebooks_Detail.getDetail() + "");
            ratingBar.setRating(ebooks_Detail.getRating());
            ratingBar.setEnabled(false);

            if (ebooks_Detail.getWriters().toString().length() > 0) {
                txt_wrtiers.setText(ebooks_Detail.getWriters() + "");
            } else {
                txt_wrtiers.setText("-");
            }
            if (ebooks_Detail.getPrice().contains("$")) {
                txt_price.setText(ebooks_Detail.getListPrice() + " บาท");
            } else {
                txt_price.setText(ebooks_Detail.getPrice());
            }

            String url_publishercover = "http://www.ebooks.in.th/publishers/"
                    + ebooks_Detail.getCID() + "/" + ebooks_Detail.getLogo();
            String url_coverL = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/" + ebooks_Detail.getCoverL();
            String url_moreImage1 = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/"
                    + ebooks_Detail.getMoreImage1L();
            String url_moreImage2 = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/"
                    + ebooks_Detail.getMoreImage2L();

            downloader_forCache.download(url_publishercover, img_publisher);
            downloader_forCache.download(url_coverL, cover1);
            downloader_forCache.download(url_moreImage1, cover2);
            downloader_forCache.download(url_moreImage2, cover3);

            cover1.setOnClickListener(new onclickShowZoomImage(url_coverL));
            cover2.setOnClickListener(new onclickShowZoomImage(url_moreImage1));
            cover3.setOnClickListener(new onclickShowZoomImage(url_moreImage2));

            parentScroll = (ScrollView) findViewById(R.id.longclick_scrollView_parent);
            childScroll = (ScrollView) findViewById(R.id.longclick_scrollView_child);

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
            // phone
            OnClickToDetail ToDetailPub = new OnClickToDetail();
            ToDetailPub.newInstant(ebooks_Detail.getCID());
            img_publisher.setOnClickListener(ToDetailPub);

        } else {
            this.ebooks_Detail = ebooks_Detail;
            if (DeserializeObject == 1) {
                // Log.e("", "From DeserializeObject");
            }
            txt_title.setText(ebooks_Detail.getName() + "");
            txt_date.setText(ebooks_Detail.getPrintOutDate() + "");
            txt_page.setText(ebooks_Detail.getPages() + " Pages");
            txt_size.setText(ebooks_Detail.getFileSize() + " MB");
            txt_publisher.setText(ebooks_Detail.getPublisherName() + "");
            txt_description.setText("\t\t" + ebooks_Detail.getDetail() + "");
            ratingBar.setRating(ebooks_Detail.getRating());
            ratingBar.setEnabled(false);

            if (ebooks_Detail.getWriters().toString().length() > 0) {
                txt_wrtiers.setText(ebooks_Detail.getWriters() + "");
            } else {
                txt_wrtiers.setText("-");
            }
            if (ebooks_Detail.getPrice().contains("$")) {
                txt_price.setText(ebooks_Detail.getListPrice() + " บาท");
            } else {
                txt_price.setText(ebooks_Detail.getPrice());
            }

            String url_publishercover = "http://www.ebooks.in.th/publishers/"
                    + ebooks_Detail.getCID() + "/" + ebooks_Detail.getLogo();
            String url_coverL = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/" + ebooks_Detail.getCoverL();
            String url_moreImage1 = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/"
                    + ebooks_Detail.getMoreImage1L();
            String url_moreImage2 = AppMain.DOWNLOAD_COVER_URL
                    + ebooks_Detail.getBID() + "/"
                    + ebooks_Detail.getMoreImage2L();

            downloader_forCache.download(url_publishercover, img_publisher);
            downloader_forCache.download(url_coverL, cover1);
            downloader_forCache.download(url_moreImage1, cover2);
            downloader_forCache.download(url_moreImage2, cover3);

            cover1.setOnClickListener(new onclickShowZoomImage(url_coverL));
            cover2.setOnClickListener(new onclickShowZoomImage(url_moreImage1));
            cover3.setOnClickListener(new onclickShowZoomImage(url_moreImage2));

            OnClickToDetail ToDetailPub = new OnClickToDetail();
            ToDetailPub.newInstant(ebooks_Detail.getCID());
            img_publisher.setOnClickListener(ToDetailPub);

            setViewPagerComment(ebooks_Detail);

        }
    }

    private class OnClickToDetail implements View.OnClickListener {
        Model_Publisher_Detail model_Publisher_Detail;
        Publisher_Detail publisher_Detail;
        AlertDialog alertDialog;
        int cid;

        public void newInstant(int publisherid) {
            cid = publisherid;
        }

        @Override
        public void onClick(View v) {

            publisher_Detail = new Publisher_Detail(getContext(),
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

                        Intent intent = new Intent(getContext(),
                                ActivityDetail_Publisher.class);
                        intent.putExtra("model", model_Publisher_Detail);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        fActivity.startActivity(intent);
                        fActivity.overridePendingTransition(
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

                        alertDialog = new AlertDialog.Builder(getContext())
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

    private class onclickShowZoomImage implements View.OnClickListener {
        private final String url;

        public onclickShowZoomImage(String url_moreImage1) {
            this.url = url_moreImage1;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == cover1.getId()) {
                new Dialog_ShowLargeImage(getContext(), R.style.PauseDialog,
                        url).show();
            }
            if (v.getId() == cover2.getId()) {
                new Dialog_ShowLargeImage(getContext(), R.style.PauseDialog,
                        url).show();
            }
            if (v.getId() == cover3.getId()) {
                new Dialog_ShowLargeImage(getContext(), R.style.PauseDialog,
                        url).show();
            }
        }

    }

    void removeViewDotpage() {
        layout_dotpage.removeAllViews();
    }

    void setViewPagerComment(Model_Ebooks_Detail ebooks_Detail) {
        // load API COMMENT
        comment_Lists = new ArrayList<Model_Comment_List>();
        comment_DetailAPI = new Comment_Detail(getContext(),
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
                        alertDialog = new AlertDialog.Builder(getContext())
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
                dot_Pageslide = new Dot_Pageslide(fActivity);
                layout_dotpage.addView(dot_Pageslide
                        .setImage_slide(comment_Lists.size()));
                if (comment_Lists.size() > 0) {
                    dot_Pageslide.setHeighlight(0);
                }

                myViewPager.setAdapter(new MyAdapterViewPager_Comment_Shelf(
                        fActivity, comment_Lists));
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

                TextView textloading = new TextView(getContext());
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

    private void init() {
        try {
            setContentView(R.layout.dialog_longclickshelf);
            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                int matginright = Integer.parseInt(getContext().getResources()
                        .getString(R.string.dialog_marginright));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        (dm.widthPixels - matginright), dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.longclick_linear_main);
                registration_linear_main.setLayoutParams(param);

                img_publisher = (ImageView) findViewById(R.id.dialog_longclick_img_publisher);
                ratingBar = (RatingBar) findViewById(R.id.dialog_longclick_ratingStar);
                cover1 = (ImageView) findViewById(R.id.dialog_longclick_image_cover);
                cover2 = (ImageView) findViewById(R.id.dialog_longclick_image_cover2);
                cover3 = (ImageView) findViewById(R.id.dialog_longclick_image_cover3);
                image_facebook = (ImageView) findViewById(R.id.dialog_longclick_image_facebook);
                image_favorite = (ImageView) findViewById(R.id.dialog_longclick_image_favorite);
                image_comment = (ImageView) findViewById(R.id.dialog_longclick_image_comment);
                txt_rating_comment = (TextView) findViewById(R.id.dialog_longclick_text_count_comment);
                txt_title = (TextView) findViewById(R.id.dialog_longclick_text_title);
                txt_date = (TextView) findViewById(R.id.dialog_longclick_text_datetime);
                txt_page = (TextView) findViewById(R.id.dialog_longclick_text_page);
                txt_size = (TextView) findViewById(R.id.dialog_longclick_text_size);
                txt_wrtiers = (TextView) findViewById(R.id.dialog_longclick_text_writers);
                txt_price = (TextView) findViewById(R.id.dialog_longclick_text_price_bth);
                txt_publisher = (TextView) findViewById(R.id.dialog_longclick_text_publishername);
                txt_other = (TextView) findViewById(R.id.dialog_longclick_text_otherbook);
                txt_description = (TextView) findViewById(R.id.dialog_longclick_text_descriptions);

                txt_rating_comment.setTypeface(StaticUtils.getTypeface(
                        getContext(), Font.LayijiMahaniyomV105ot));
                txt_price.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_wrtiers.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_size.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_page.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_date.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_description.setTypeface(StaticUtils.getTypeface(
                        getContext(), Font.LayijiMahaniyomV105ot));
                txt_publisher.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_other.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_title.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));

                image_facebook.setOnClickListener(new onclickImage());
                image_favorite.setOnClickListener(new onclickImage());
                image_comment.setOnClickListener(new onclickImage());

                setTextType(model.getStatusFlag());
                fade_in = AnimationUtils.loadAnimation(getContext(),
                        R.anim.fadein);

            } else {
                img_publisher = (ImageView) findViewById(R.id.dialog_longclick_img_publisher);
                myViewPager = (MyViewPager) findViewById(R.id.dialog_longclick_myViewPager_comment);
                layout_dotpage = (LinearLayout) findViewById(R.id.dialog_longclick_linear_dotpage);
                ratingBar = (RatingBar) findViewById(R.id.dialog_longclick_ratingStar);
                cover1 = (ImageView) findViewById(R.id.dialog_longclick_image_cover);
                cover2 = (ImageView) findViewById(R.id.dialog_longclick_image_cover2);
                cover3 = (ImageView) findViewById(R.id.dialog_longclick_image_cover3);
                image_facebook = (ImageView) findViewById(R.id.dialog_longclick_image_facebook);
                image_favorite = (ImageView) findViewById(R.id.dialog_longclick_image_favorite);
                image_comment = (ImageView) findViewById(R.id.dialog_longclick_image_comment);
                txt_rating_comment = (TextView) findViewById(R.id.dialog_longclick_text_count_comment);
                txt_title = (TextView) findViewById(R.id.dialog_longclick_text_title);
                txt_date = (TextView) findViewById(R.id.dialog_longclick_text_datetime);
                txt_page = (TextView) findViewById(R.id.dialog_longclick_text_page);
                txt_size = (TextView) findViewById(R.id.dialog_longclick_text_size);
                txt_wrtiers = (TextView) findViewById(R.id.dialog_longclick_text_writers);
                txt_price = (TextView) findViewById(R.id.dialog_longclick_text_price_bth);
                txt_publisher = (TextView) findViewById(R.id.dialog_longclick_text_publishername);
                txt_other = (TextView) findViewById(R.id.dialog_longclick_text_otherbook);
                txt_description = (TextView) findViewById(R.id.dialog_longclick_text_descriptions);
                txt_comment = (TextView) findViewById(R.id.dialog_longclick_text_comment);

                txt_rating_comment.setTypeface(StaticUtils.getTypeface(
                        getContext(), Font.LayijiMahaniyomV105ot));
                txt_price.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_wrtiers.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_size.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_page.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_date.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_description.setTypeface(StaticUtils.getTypeface(
                        getContext(), Font.LayijiMahaniyomV105ot));
                txt_publisher.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_other.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_title.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));
                txt_comment.setTypeface(StaticUtils.getTypeface(getContext(),
                        Font.LayijiMahaniyomV105ot));

                image_facebook.setOnClickListener(new onclickImage());
                image_favorite.setOnClickListener(new onclickImage());
                image_comment.setOnClickListener(new onclickImage());

                setTextType(model.getStatusFlag());
                fade_in = AnimationUtils.loadAnimation(getContext(),
                        R.anim.fadein);
            }

            // STOU

            img_publisher.setVisibility(View.GONE);
            findViewById(R.id.dialog_longclick_rt_covers_publisher)
                    .setVisibility(View.GONE);

            // end

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    private class onAnimationLitener implements AnimationListener {
        int id = 0;

        public onAnimationLitener(int id) {
            this.id = id;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (id == image_facebook.getId()) {
                if (StaticUtils.getFacebooklogin()) {
                    initShareFaceook();
                    ShareEbookPublisher();
                } else {
                    Toast.makeText(fActivity, "Not use facebook account",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(fActivity,
                            "Please login with facebook account",
                            Toast.LENGTH_LONG).show();
                }
            }
            if (id == image_favorite.getId()) {
                if (model.getStatusFlag() != StatusFlag.Favourite) {
                    // String url = "http://api.ebooks.in.th/addfavorite.ashx?";
                    String url = AppMain.FAVORITE_BOOK_SHELF_URL;
                    url += "bid=" + model.getBID();
                    url += "&cid=" + Shared_Object.getCustomerDetail.getCID();
                    url += "&add=1";

                    LoadAPIResultString apiResultString = new LoadAPIResultString();
                    apiResultString
                            .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                @Override
                                public void completeResult(String result) {
                                    if (result != null) {
                                        if (result.contains("1")) {
                                            image_favorite
                                                    .setImageResource(R.drawable.icon_remove_favorites2x);

                                            String filename = "ebooks_"
                                                    + model.getBID() + ".porar";
                                            File file = new File(getContext()
                                                    .getFilesDir(), filename);
                                            model.setStatusFlag(StatusFlag.Favourite);

                                            StaticUtils.setModelShelf(model);

                                            Shared_Object.getCustomerDetail
                                                    .setFavorites(Shared_Object.getCustomerDetail
                                                            .getFavorites() + 1);
                                            if (file.exists()) {
                                                EbooksShelfHeaderFile save = new EbooksShelfHeaderFile(
                                                        model,
                                                        Shared_Object.getCustomerDetail);
                                                Class_Manage.SaveEbooksObject(
                                                        getContext(), save,
                                                        filename);
                                            }

                                        }
                                    } else {
                                        try {
                                            Toast.makeText(
                                                    getContext(),
                                                    "An error has occurred. Please to try again",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException e) {
                                            // TODO: handle exception
                                        }
                                    }

                                }
                            });
                    apiResultString.execute(url);

                } else {
                    // String url = "http://api.ebooks.in.th/addfavorite.ashx?";
                    String url = AppMain.FAVORITE_BOOK_SHELF_URL;
                    url += "bid=" + model.getBID();
                    url += "&cid=" + Shared_Object.getCustomerDetail.getCID();
                    url += "&add=0";
                    LoadAPIResultString apiResultString = new LoadAPIResultString();
                    apiResultString
                            .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                @Override
                                public void completeResult(String result) {
                                    if (result != null) {
                                        if (result.contains("1")) {
                                            image_favorite
                                                    .setImageResource(R.drawable.icon_favorites2x);

                                            String filename = "ebooks_"
                                                    + model.getBID() + ".porar";
                                            File file = new File(getContext()
                                                    .getFilesDir(), filename);
                                            model.setStatusFlag(StatusFlag.New);

                                            StaticUtils.setModelShelf(model);

                                            Shared_Object.getCustomerDetail
                                                    .setFavorites(Shared_Object.getCustomerDetail
                                                            .getFavorites() - 1);
                                            if (file.exists()) {
                                                EbooksShelfHeaderFile save = new EbooksShelfHeaderFile(
                                                        model,
                                                        Shared_Object.getCustomerDetail);
                                                Class_Manage.SaveEbooksObject(
                                                        getContext(), save,
                                                        filename);
                                            }
                                        }
                                    } else {
                                        try {
                                            Toast.makeText(
                                                    getContext(),
                                                    "An error has occurred. Please to try again",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException e) {
                                            // TODO: handle exception
                                        }
                                    }

                                }
                            });
                    apiResultString.execute(url);

                }
            }
            if (id == image_comment.getId()) {
                // :phone
                if (AppMain.isphone) {
                    // comment
                    viewComment = new Dialog_ViewComment(fActivity,
                            R.style.PauseDialog, model.getBID());
                    viewComment.show();
                } else {
                    // :tablet
                    comment = new Dialog_Comment(fActivity,
                            R.style.PauseDialog, model.getBID());
                    comment.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface arg0) {
                            if (comment.isCancel()) {
                                setViewPagerComment(ebooks_Detail);
                            }
                            // Log.e("", "Dialog_Comment onCancel");
                        }
                    });
                    comment.show();
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub

        }

    }
    public void initShareFaceook() {
        if(RegisterFacebook.hasPublishPermission()){

        }else{
            RegisterFacebook.ReadPublishPermissionFacebookActivity(fActivity);
        }
        shareDialogFacebook = new ShareDialog(fActivity);
        shareDialogFacebook.registerCallback(
                RegisterFacebook.mCallbackManager, shareFacebookCallback);
    }

    public void ShareEbookPublisher() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setImageUrl(Uri.parse(AppMain.DOWNLOAD_COVER_URL + ebooks_Detail.getBID() + "/" + ebooks_Detail.getCoverL()))
                .setContentTitle(ebooks_Detail.getName())
                .setContentDescription("http://www.ebookstou.org/")
                .setContentUrl(Uri.parse("http://www.ebookstou.org/")).build();
        showContent(shareDialogFacebook, content);
    }

    private void showContent(ShareDialog shareDialog, ShareLinkContent content) {
        shareDialog.show(content);
    }

    private FacebookCallback<Sharer.Result> shareFacebookCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            Toast.makeText(fActivity, "Posted Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(fActivity, "Publish cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(fActivity, "Error posting story", Toast.LENGTH_SHORT).show();
        }
    };

    private void setTextType(StatusFlag status) {

        if (status == StatusFlag.Favourite) {
            image_favorite.setImageResource(R.drawable.icon_remove_favorites2x);
        } else {
            image_favorite.setImageResource(R.drawable.icon_favorites2x);
        }
    }

    private class onclickImage implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == image_facebook.getId()) {
                v.startAnimation(fade_in);
                fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
            }
            if (v.getId() == image_favorite.getId()) {
                v.startAnimation(fade_in);
                fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
            }
            if (v.getId() == image_comment.getId()) {
                v.startAnimation(fade_in);
                fade_in.setAnimationListener(new onAnimationLitener(v.getId()));
            }
        }

    }

    @Override
    public void cancel() {
        if (comment_DetailAPI != null) {
            if (comment_DetailAPI.CancelTask()) {
                // Log.v("CancelTask", "true");
            }
        }
        super.cancel();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public Model_EBook_Shelf_List getFavouriteModel() {
        return this.FavouriteModel;
    }

    public void setFavouriteModel(Model_EBook_Shelf_List favouriteModel) {
        this.FavouriteModel = favouriteModel;
    }

    public Model_EBook_Shelf_List getRemoveFavouriteModel() {
        return this.RemoveFavouriteModel;
    }

    public Model_EBook_Shelf_List getDeleteModel() {
        return this.DeleteModel;
    }

    public void setDeleteModel(Model_EBook_Shelf_List deleteModel) {
        this.DeleteModel = deleteModel;
    }

    public void setRemoveFavouriteModel(
            Model_EBook_Shelf_List removeFavouriteModel) {
        this.RemoveFavouriteModel = removeFavouriteModel;
    }
}
