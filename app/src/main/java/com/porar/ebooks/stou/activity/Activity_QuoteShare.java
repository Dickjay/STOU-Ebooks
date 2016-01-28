package com.porar.ebooks.stou.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.porar.ebook.control.Dialog_WebViewTwitter;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.UriResultImageCrop;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask;
import com.porar.ebooks.utils.RegisterFacebook;
import com.twitter.AlertDialogManager;
import com.twitter.ConnectionDetector;
import com.twitter.Dev_twitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

public class Activity_QuoteShare extends Activity {
    Activity_QuoteShare myActivity;
    Bitmap photoCrop;
    Button btn_share, btn_back;
    ImageView image_crop;
    LinearLayout layout_fb, layout_ig, layout_tw;
    EditText editText;
    Model_EBook_Shelf_List eBook_Shelf_List;

    // Twitter
    private static final int twitternumber = 100;
    private static Twitter twitter;
    private static RequestToken requestToken;
    // AccessToken accessToken;
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    // Internet Connection detector
    private ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    ProgressDialog pDialog;
    Dialog_WebViewTwitter Dtwitter;
    private ShareDialog shareDialogFacebook;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RegisterFacebook.unRegisterFacebook(Activity_QuoteShare.this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quoteshare);
        RegisterFacebook.InitFacebookSDK(Activity_QuoteShare.this);
        myActivity = this;
        getBundle();
        setId();
        OnClickEvent();

        if (UriResultImageCrop.getUriSave() != null) {
            String full_path = UriResultImageCrop.getUriSave().getPath();
            photoCrop = BitmapFactory.decodeFile(full_path);
            image_crop.setImageBitmap(photoCrop);
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(dm.widthPixels, dm.widthPixels);
        image_crop.setLayoutParams(param);
    }

    private void getBundle() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            eBook_Shelf_List = (Model_EBook_Shelf_List) b.get("model");
        }
    }

    private void setId() {
        editText = (EditText) findViewById(R.id.quoteshare_edit_caption);
        layout_fb = (LinearLayout) findViewById(R.id.quoteshare_linear_share_fb);
        layout_ig = (LinearLayout) findViewById(R.id.quoteshare_linear_share_ig);
        layout_tw = (LinearLayout) findViewById(R.id.quoteshare_linear_share_tw);
        image_crop = (ImageView) findViewById(R.id.quoteshare_imag_imagecrop);
        btn_back = (Button) findViewById(R.id.quoteshare_btn_back);
        btn_share = (Button) findViewById(R.id.quoteshare_btn_share);
    }

    private void OnClickEvent() {
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
                myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        layout_fb.setOnClickListener(new ShareEvent());
        layout_ig.setOnClickListener(new ShareEvent());
        layout_tw.setOnClickListener(new ShareEvent());
    }

    private void initShareFaceook() {

        RegisterFacebook.ReadPublishPermissionFacebookActivity(Activity_QuoteShare.this);
        shareDialogFacebook = new ShareDialog(Activity_QuoteShare.this);
        shareDialogFacebook.registerCallback(RegisterFacebook.mCallbackManager,
                shareFacebookCallback);

    }

    private FacebookCallback<Sharer.Result> shareFacebookCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            // TODO Auto-generated method stub
            if (result != null && result.getPostId() != null
                    && !result.getPostId().equals("")) {
                Toast.makeText(Activity_QuoteShare.this, "Posted Success", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(FacebookException error) {
            // TODO Auto-generated method stub
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Log.d("HelloFacebook", "Canceled");
        }
    };

    private void shareQouteToFacebook(){
        String urlpage = "http://www.ebooks.in.th/ebook/" + eBook_Shelf_List.getBID() + "/";
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(photoCrop)
                .setCaption(editText.getEditableText().toString() + " \n" + urlpage).build();
        ArrayList<SharePhoto> photos = new ArrayList<>();
        photos.add(sharePhoto);
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto).build();
        ShareApi.share(content, shareFacebookCallback);
    }

    private class ShareEvent implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == layout_fb.getId()) {
                Log.v("", "facebook");
                if (!Shared_Object.getCustomerDetail.getFacebookId().equals("0") && !Shared_Object.getCustomerDetail.getFacebookId().equals("")) {
                    initShareFaceook();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            shareQouteToFacebook();
                        }
                    }, 1500);


                } else {
                    // not login
                    Toast.makeText(myActivity, "Not use facebook account", Toast.LENGTH_LONG).show();
                    Toast.makeText(myActivity, "Please login with facebook account", Toast.LENGTH_LONG).show();

                }

            }
            if (v.getId() == layout_ig.getId()) {
                Log.v("", "instagram");
            }
            if (v.getId() == layout_tw.getId()) {
                Log.v("", "twitter");
                cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {
                    alert.showAlertDialog(myActivity, "Internet Connection Error", "Please connect to working Internet connection", false);
                    return;
                }
                // Check if twitter keys are set
                if (Dev_twitter.TWITTER_CONSUMER_KEY.trim().length() == 0 || Dev_twitter.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
                    alert.showAlertDialog(myActivity, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
                    return;
                }
                // Shared Preferences
                mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);

                if (!isTwitterLoggedInAlready()) {
                    Uri uri = getIntent().getData();
                    if (uri != null && uri.toString().startsWith(Dev_twitter.TWITTER_CALLBACK_URL)) {
                        // oAuth verifier
                        String verifier = uri.getQueryParameter(Dev_twitter.URL_TWITTER_OAUTH_VERIFIER);

                        try {
                            // Get the access token
                            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                            // Shared Preferences
                            Editor e = mSharedPreferences.edit();
                            e.putString(Dev_twitter.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                            e.putString(Dev_twitter.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
                            e.putBoolean(Dev_twitter.PREF_KEY_TWITTER_LOGIN, true);
                            e.commit(); // save changes

                            Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                            long userID = accessToken.getUserId();
                            User user = twitter.showUser(userID);
                            String username = user.getName();

                            Log.v("", "" + username);

                        } catch (Exception e) {
                            // Check log for login errors
                            Log.e("Twitter Login Error", "> " + e.getMessage());
                        }
                    } else {
                        loginToTwitter();
                    }
                } else {
                    try {
                        ConfigurationBuilder builder = new ConfigurationBuilder();
                        builder.setOAuthConsumerKey(Dev_twitter.TWITTER_CONSUMER_KEY);
                        builder.setOAuthConsumerSecret(Dev_twitter.TWITTER_CONSUMER_SECRET);

                        String access_token = mSharedPreferences.getString(Dev_twitter.PREF_KEY_OAUTH_TOKEN, "");
                        String access_token_secret = mSharedPreferences.getString(Dev_twitter.PREF_KEY_OAUTH_SECRET, "");
                        AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                        Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                        long userID = accessToken.getUserId();
                        User user = twitter.showUser(userID);
                        String username = user.getName();

                        Log.v("", "" + username);
                        // Toast.makeText(myActivity, "Post Twitter <Release>" + username, Toast.LENGTH_LONG).show();

                        TwitterPostPic(new File(UriResultImageCrop.getUriSave().getPath()), editText.getEditableText().toString());

                    } catch (TwitterException e) {
                        // Error in updating status
                        Log.e("Twitter Login Error", "> " + e.getMessage());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void TwitterPostPic(final File file, final String msg) throws TwitterException, IOException {

        if (file.exists()) {

            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    String urlpage = "http://www.ebooks.in.th/ebook/" + eBook_Shelf_List.getBID() + "/";
                    String access_token = mSharedPreferences.getString(Dev_twitter.PREF_KEY_OAUTH_TOKEN, "");
                    String access_token_secret = mSharedPreferences.getString(Dev_twitter.PREF_KEY_OAUTH_SECRET, "");

                    Configuration conf = new ConfigurationBuilder()
                            .setOAuthConsumerKey(Dev_twitter.TWITTER_CONSUMER_KEY)
                            .setOAuthConsumerSecret(Dev_twitter.TWITTER_CONSUMER_SECRET)
                            .setOAuthAccessToken(access_token)
                            .setOAuthAccessTokenSecret(access_token_secret).build();

                    ImageUploadFactory factory = new ImageUploadFactory(conf);
                    ImageUpload upload = factory.getInstance();
                    try {
                        return upload.upload(file, msg + "  " + urlpage);
                    } catch (TwitterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null) {
                        Log.v("", "result " + result);
                        Toast.makeText(myActivity, "Post Twitter <Release>" + result, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(myActivity, "Internet Connection Error Please connect to working Internet connection", Toast.LENGTH_LONG).show();
                    }
                    super.onPostExecute(result);
                }
            }.execute();

        }

    }

    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(Dev_twitter.PREF_KEY_OAUTH_TOKEN);
        e.remove(Dev_twitter.PREF_KEY_OAUTH_SECRET);
        e.remove(Dev_twitter.PREF_KEY_TWITTER_LOGIN);
        e.commit();

    }

    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Dev_twitter.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Dev_twitter.TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(Dev_twitter.TWITTER_CALLBACK_URL);
                Dtwitter = new Dialog_WebViewTwitter(myActivity, R.style.PauseDialog, requestToken.getAuthenticationURL());
                Dtwitter.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        if (Dtwitter.getVeritifer().length() > 0) {
                            try {
                                // Get the access token
                                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, Dtwitter.getVeritifer());

                                // Shared Preferences
                                Editor e = mSharedPreferences.edit();
                                e.putString(Dev_twitter.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                                e.putString(Dev_twitter.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
                                e.putBoolean(Dev_twitter.PREF_KEY_TWITTER_LOGIN, true);
                                e.commit(); // save changes

                                Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                                long userID = accessToken.getUserId();
                                User user = twitter.showUser(userID);
                                String username = user.getName();

                                Log.v("", "" + username);
                                Toast.makeText(myActivity, "Welcome Twitter" + username, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // Check log for login errors
                                Log.e("Twitter Login Error", "> " + e.getMessage());
                            }
                        }
                    }
                });
                Dtwitter.show();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(), "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTwitterLoggedInAlready() {
        return mSharedPreferences.getBoolean(Dev_twitter.PREF_KEY_TWITTER_LOGIN, false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (getIntent().getData() != null) {

                    }

                    finish();
                    myActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        }
        return false;

    }
}
