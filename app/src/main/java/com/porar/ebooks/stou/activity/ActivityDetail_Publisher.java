package com.porar.ebooks.stou.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.porar.ebook.adapter.Adapter_List_Ebook;
import com.porar.ebook.adapter.MyAdapterViewPager_Book;
import com.porar.ebook.control.Dialog_ShowLargeImage;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Publisher_Detail;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import plist.xml.PList;

public class ActivityDetail_Publisher extends Activity {
    ActivityDetail_Publisher myActivity;
    ImageDownloader_forCache downloader_forCache;
    TextView abs_text;
    TextView txt_detail, txt_name, txt_fan, txt_download, txt_total;
    TextView stxt_fan, stxt_download, stxt_total;
    ImageView iv_cover;
    Button btn_becomfan;
    public ProgressDialog progressDialog = null;
    // animation
    Animation fade_in;
    // Class viewpager
    private MyViewPager myViewPager;
    private MyAdapterViewPager_Book adapterViewPager_Book;
    private Dot_Pageslide dot_Pageslide;
    private LinearLayout linear_dotpage;
    private AlertDialog alertDialog;
    Handler handler = new Handler();
    Model_Publisher_Detail model_Publisher_Detail;

    ListView listviewphone;
    Adapter_List_Ebook adapter_List_Ebook;
    private ShareDialog shareDialogFacebook;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RegisterFacebook.unRegisterFacebook(ActivityDetail_Publisher.this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpublisher);
        RegisterFacebook.InitFacebookSDK(ActivityDetail_Publisher.this);

        myActivity = this;
        downloader_forCache = new ImageDownloader_forCache();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(R.layout.abs_layout_publisher);
        abs_text = (TextView) actionBar.getCustomView().findViewById(R.id.mytext);
        abs_text.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_actionbars));

        actionBar.setDisplayShowHomeEnabled(true);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fadein);

        ImageView logo = (ImageView) findViewById(android.R.id.home);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.btn_back_new2));
        logo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(fade_in);
                finish();
                myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                System.gc();

            }
        });

        if (AppMain.isphone) {
            getBundle();
            setId_Phone();
            if (AppMain.pList_default_publisher_ebook != null && model_Publisher_Detail != null) {
                setDetailPhone(model_Publisher_Detail);
            }
        } else {
            getBundle();
            setId();
            Model_Setting model_setting = Class_Manage.getModel_Setting(myActivity);
            if (model_setting != null) {
                ((LinearLayout) myActivity.findViewById(R.id.detailpublisher_mainlayout)).setBackgroundResource(model_setting.getDrawable_backgroundStyle());
            }
            Model_SettingShelf settingShelf = Class_Manage.getModel_SettingShelf(myActivity);
            if (settingShelf != null) {
                ((ImageView) myActivity.findViewById(R.id.detailpublisher_img_shelf1)).setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) myActivity.findViewById(R.id.detailpublisher_img_shelf2)).setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) myActivity.findViewById(R.id.detailpublisher_img_shelf3)).setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) myActivity.findViewById(R.id.detailpublisher_img_shelf4)).setImageResource(settingShelf.getDrawable_ShelfStyle());
            }
            if (AppMain.pList_default_publisher_ebook != null && model_Publisher_Detail != null) {
                setViewPager(AppMain.pList_default_publisher_ebook, null);
                setDetail(model_Publisher_Detail);
            }
        }
    }

    private void setDetailPhone(Model_Publisher_Detail model_Publisher_Detail2) {
        txt_detail.setText("\t" + model_Publisher_Detail2.getDetail());
        txt_name.setText("" + model_Publisher_Detail2.getName());
        txt_fan.setText("" + model_Publisher_Detail2.getFans());
        txt_download.setText("" + model_Publisher_Detail2.getDownloads());
        txt_total.setText("" + model_Publisher_Detail2.geteBooks());

        String url_cover = "http://www.ebooks.in.th/publishers/" + model_Publisher_Detail2.getCID() + "/" + model_Publisher_Detail2.getImage();
        downloader_forCache.download(url_cover, iv_cover);
        iv_cover.setOnClickListener(new onclickShowZoomImage(url_cover));
        btn_becomfan.setOnClickListener(new onclickBecomeFan(model_Publisher_Detail2.getCID()));

        if (model_Publisher_Detail2.getAlreadyFan()) {
            btn_becomfan.setEnabled(false);
            btn_becomfan.setText(getString(R.string.already_fan));
        }
        setListView(AppMain.pList_default_publisher_ebook, null);

    }

    private void setListView(PList plist, ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                listviewphone.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway", Toast.LENGTH_LONG).show();
        }
        try {
            Log.i("", "plist " + plist.toString());

            adapter_List_Ebook = null;
            adapter_List_Ebook = new Adapter_List_Ebook(myActivity, 0, plist);
            listviewphone.setAdapter(adapter_List_Ebook);

            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    private void setId_Phone() {
        listviewphone = (ListView) findViewById(R.id.detailpublisher_listview_ebook);
        iv_cover = (ImageView) findViewById(R.id.detailpublisher_imageview_cover);
        btn_becomfan = (Button) findViewById(R.id.detailpublisher_button_brcomefan);
        txt_detail = (TextView) findViewById(R.id.detailpublisher_textVie_detail);
        txt_name = (TextView) findViewById(R.id.detailpublisher_textVie_name);
        txt_fan = (TextView) findViewById(R.id.detailpublisher_textView2fan);
        txt_download = (TextView) findViewById(R.id.detailpublisher_textView2download);
        txt_total = (TextView) findViewById(R.id.detailpublisher_textView2ebook);
        stxt_fan = (TextView) findViewById(R.id.detailpublisher_textView1fan);
        stxt_download = (TextView) findViewById(R.id.detailpublisher_textView1download);
        stxt_total = (TextView) findViewById(R.id.detailpublisher_textView1ebook);

        txt_detail.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        txt_name.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        btn_becomfan.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        txt_fan.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        txt_download.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        txt_total.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        stxt_fan.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        stxt_download.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
        stxt_total.setTypeface(StaticUtils.getTypeface(myActivity, Font.THSarabanNew));
    }

    private void setDetail(Model_Publisher_Detail model_Publisher_Detail2) {
        txt_detail.setText("\t" + model_Publisher_Detail2.getDetail());
        txt_name.setText("" + model_Publisher_Detail2.getName());
        txt_fan.setText("" + model_Publisher_Detail2.getFans());
        txt_download.setText("" + model_Publisher_Detail2.getDownloads());
        txt_total.setText("" + model_Publisher_Detail2.geteBooks());

        String url_cover = "http://www.ebooks.in.th/publishers/" + model_Publisher_Detail2.getCID() + "/" + model_Publisher_Detail2.getImage();
        downloader_forCache.download(url_cover, iv_cover);
        iv_cover.setOnClickListener(new onclickShowZoomImage(url_cover));
        btn_becomfan.setOnClickListener(new onclickBecomeFan(model_Publisher_Detail2.getCID()));

        if (model_Publisher_Detail2.getAlreadyFan()) {
            btn_becomfan.setEnabled(false);
            btn_becomfan.setText(getString(R.string.already_fan));
        }
    }

    private class onclickBecomeFan implements OnClickListener {
        String cid = "";
        String did = "";
        String urladdFan = "";

        public onclickBecomeFan(int pcid) {
            this.cid = "" + Shared_Object.getCustomerDetail.getCID();
            ; // customer id
            this.did = Shared_Object.getDeviceID(myActivity);
            urladdFan = "http://api.ebooks.in.th/addfan.ashx?pcid=" + pcid + "&cid=" + cid + "&did=" + did;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btn_becomfan.getId()) {
                btn_becomfan.setText("Loading...");
                btn_becomfan.setEnabled(false);
                LoadAPIResultString apiResultString = new LoadAPIResultString();
                apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                    @Override
                    public void completeResult(String result) {
                        Log.e("addFan", "result :" + result);
                        if (result != null) {
                            if (result.equals("1")) {
                                btn_becomfan.setEnabled(false);
                                btn_becomfan.setText(getString(R.string.already_fan));
                            }
                        } else {

                            Toast.makeText(ActivityDetail_Publisher.this, "An error has ocurred.", Toast.LENGTH_LONG).show();
                            btn_becomfan.setEnabled(true);
                            btn_becomfan.setText(getString(R.string.become_fan));

                        }

                    }
                });
                apiResultString.execute(urladdFan);
                // apiResultString.setUrl_FromHttpClient(urladdFan);
            }
        }

    }

    private class onclickShowZoomImage implements OnClickListener {
        private final String url;

        public onclickShowZoomImage(String url_moreImage1) {
            this.url = url_moreImage1;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == iv_cover.getId()) {
                new Dialog_ShowLargeImage(myActivity, R.style.PauseDialog, url).show();
            }
        }

    }

    private void setViewPager(final PList pList, final ProgressDialog dialog) {
        myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        try {
            myViewPager.removeAllViews();
            adapterViewPager_Book = null;
            adapterViewPager_Book = new MyAdapterViewPager_Book(myActivity, pList);

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide.setImage_slide(adapterViewPager_Book.getCount()));
                    myViewPager.setAdapter(adapterViewPager_Book);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            dot_Pageslide.setHeighlight(position);
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int arg0) {

                        }
                    });

                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            if (adapterViewPager_Book != null) {
                                dialog.dismiss();
                            }
                        }
                    }
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                    // if (progressDialog != null) {
                    // if (progressDialog.isShowing()) {
                    // progressDialog.dismiss();
                    // }
                    // }
                }
            }, 50);

            alertDialog = new AlertDialog.Builder(myActivity).create();
            // TextView title = new TextView(myActivity);
            // title.setText(AppMain.getTag());
            // title.setPadding(10, 10, 10, 10);
            // title.setGravity(Gravity.CENTER);
            // title.setTextSize(23);
            // alertDialog.setCustomTitle(title);
            TextView msg = new TextView(myActivity);
            msg.setText("Please Wait... Loading");
            msg.setPadding(10, 10, 10, 10);
            msg.setGravity(Gravity.CENTER);
            alertDialog.setView(msg);
            alertDialog.show();

            //

        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error", Toast.LENGTH_LONG).show();
        }

    }

    private void removeViewDotPage() {
        if (linear_dotpage.getChildCount() > 0) {
            linear_dotpage.removeAllViews();
        }
    }

    private void getBundle() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            model_Publisher_Detail = (Model_Publisher_Detail) b.get("model");
        }
    }

    private void setId() {

        iv_cover = (ImageView) findViewById(R.id.detailpublisher_imageview_cover);
        myViewPager = (MyViewPager) findViewById(R.id.detailpublisher_myViewPager1);
        linear_dotpage = (LinearLayout) findViewById(R.id.detailpublisher_linear_dotpage);
        btn_becomfan = (Button) findViewById(R.id.detailpublisher_button_brcomefan);
        txt_detail = (TextView) findViewById(R.id.detailpublisher_textVie_detail);
        txt_name = (TextView) findViewById(R.id.detailpublisher_textVie_name);
        txt_fan = (TextView) findViewById(R.id.detailpublisher_textView_fan);
        txt_download = (TextView) findViewById(R.id.detailpublisher_textView_download);
        txt_total = (TextView) findViewById(R.id.detailpublisher_textView_total);
        stxt_fan = (TextView) findViewById(R.id.TextViewpublisher_fan);
        stxt_download = (TextView) findViewById(R.id.TextViewpublisher_download);
        stxt_total = (TextView) findViewById(R.id.TextViewpublisher_total);

        txt_detail.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        txt_name.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        txt_fan.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        txt_download.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        txt_total.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        stxt_fan.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        stxt_download.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
        stxt_total.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));

        btn_becomfan.setTypeface(StaticUtils.getTypeface(myActivity, Font.DB_Helvethaica_X_Med));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_publisher, menu);
        return true;
    }


    public void initShareFaceook() {
        RegisterFacebook.ReadPublishPermissionFacebookActivity(ActivityDetail_Publisher.this);
        shareDialogFacebook = new ShareDialog(ActivityDetail_Publisher.this);
        shareDialogFacebook.registerCallback(
                RegisterFacebook.mCallbackManager, shareFacebookCallback);
    }

    public void ShareEbookPublisher() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setImageUrl(
                        Uri.parse("http://www.ebooks.in.th/publishers/" + model_Publisher_Detail.getCID() + "/" + model_Publisher_Detail.getImage()))
                .setContentTitle(model_Publisher_Detail.getName())
                .setContentDescription("www.ebooks.in.th")
                .setContentUrl(
                        Uri.parse("http://ebooks.in.th" + model_Publisher_Detail.getUrl())).build();
        showContent(shareDialogFacebook, content);
    }

    private void showContent(ShareDialog shareDialog, ShareLinkContent content) {
        shareDialog.show(content);
    }

    private FacebookCallback<Sharer.Result> shareFacebookCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            Toast.makeText(ActivityDetail_Publisher.this, "Posted Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(ActivityDetail_Publisher.this, "Publish cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(ActivityDetail_Publisher.this, "Error posting story", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                for (int i = 0; i < item.getSubMenu().size(); i++) {

                    item.getSubMenu().getItem(i).setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @SuppressLint("DefaultLocale")
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().toLowerCase().contains("facebook")) {
                                if (StaticUtils.getFacebooklogin()) {
                                    initShareFaceook();
                                    ShareEbookPublisher();
                                } else {
                                    Toast.makeText(ActivityDetail_Publisher.this, "Not use facebook account", Toast.LENGTH_LONG).show();
                                    Toast.makeText(ActivityDetail_Publisher.this, "Please login with facebook account", Toast.LENGTH_LONG).show();
                                }
                            }
                            return false;
                        }
                    });
                }

                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    finish();
                    myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        }
        return false;

    }
}
