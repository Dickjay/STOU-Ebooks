package com.porar.ebook.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_STOU_Students;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import plist.type.Array;
import plist.xml.PList;

@SuppressLint("SimpleDateFormat")
public abstract class DialogLogin extends Dialog {

    Button btn_login, btn_cancel;
    ImageView btn_authfacebook;
    TextView txt_email, txt_pass, txt_titlelogin, txt_titlelogin2;
    EditText edit_email, edit_pass;
    LinearLayout linear_facebook, linear_email, linear_signup,
            linear_withoutlogin, linear_stou, linear_stou_regis;
    TextView txt_f1, txt_f2, txt_email1, txt_email2, txt_sigup1, txt_sigup2,
            txt_withoutlogin1, txt_withoutlogin2, txt_title, txt_stou1,
            txt_regis_stou1, txt_regis_stou2, txt_stou2;
    ImageDownloader_forCache downloader_forCache;
    DialogLogin dialog_ShowLargeImage;
    int id_loginwithemail = R.layout.dialog_login;
    int id_choselogin = R.layout.dialog_registration;
    Fragment fragment;
    Activity activity;
    String passFacebook = null;
    String facebookId = null;
    String facebookEmail = null;
    AsyncTask_FetchAPI asyncTask_FetchAPI;
    Handler handler = new Handler();
    AlertDialog alertDialog;
    // private final Session userInfoSession = null;
    ProgressDialog progressDialog;
    ProgressBar p1, p2, p3, p4, p5;
    ImageView im1, im2, im3, im4, im5;

    Bundle bundle;
    RelativeLayout emailViewLinear, regislViewLinear;
    ImageView imageViewemail, imageViewregister, imageViewfacebook;
    public static final String serial_username = "serial_username";
    public static final String serial_password = "serial_password";
    TextView t_email, t_pass, t_confirm, t_name, t_surname, t_mobile,
            t_birthdate;
    EditText et_email, et_pass, et_confirm, et_name, et_surname, et_mobile,
            et_birthdate, et_studentID;
    Button bt_cancel, bt_register;
    DatePickerDialog datePickerDialog;
    static String re_studentID = "";
    static String re_email = "";
    static String re_pass = "";
    static String re_name = "";
    static String re_surname = "";
    static String re_mobile = "";
    static String re_brithdate = "";
    static String re_type = "";
    private TextView headRegisterTextView;
    private Context context;

    public DialogLogin(Context context, int theme, Fragment fragment) {
        super(context, theme);
        this.context = context;
        this.fragment = fragment;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog_ShowLargeImage = this;
        bundle = new Bundle();

        init();

    }

    public DialogLogin(Context context, int theme, Activity activity) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog_ShowLargeImage = this;
        bundle = new Bundle();
        this.context = context;
        this.activity = activity;
        init();

    }

    private void init() {
        Log.e("", "ClickedClicked");
        try {

            setContentView(id_choselogin);
            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.registration_linear_main);
                registration_linear_main.setLayoutParams(param);
            }

            im1 = (ImageView) findViewById(R.id.registration_imageViewfacebook);
            im2 = (ImageView) findViewById(R.id.registration_imageViewemail);
            im3 = (ImageView) findViewById(R.id.registration_imageView_register);
            im4 = (ImageView) findViewById(R.id.registration_imageView_register_stou);
            im5 = (ImageView) findViewById(R.id.registration_imageView_register_stou_register);

            p1 = (ProgressBar) findViewById(R.id.registration_progressBarfacebook);
            p2 = (ProgressBar) findViewById(R.id.registration_progressemail);
            p3 = (ProgressBar) findViewById(R.id.registration_progress_register);
            p4 = (ProgressBar) findViewById(R.id.registration_progressBarstou);
            p5 = (ProgressBar) findViewById(R.id.registration_progressBarstou_register);

            im1.setVisibility(View.VISIBLE);
            im2.setVisibility(View.VISIBLE);
            im3.setVisibility(View.VISIBLE);
            im4.setVisibility(View.VISIBLE);
            p5.setVisibility(View.VISIBLE);

            p1.setVisibility(View.GONE);
            p2.setVisibility(View.GONE);
            p3.setVisibility(View.GONE);
            p4.setVisibility(View.GONE);
            p5.setVisibility(View.GONE);

            emailViewLinear = (RelativeLayout) findViewById(R.id.emailLinear);
            regislViewLinear = (RelativeLayout) findViewById(R.id.regislLinear);
            imageViewregister = (ImageView) findViewById(R.id.registration_imageView_register);
            imageViewemail = (ImageView) findViewById(R.id.registration_imageViewemail);
            imageViewfacebook = (ImageView) findViewById(R.id.registration_imageViewfacebook);
            linear_stou_regis = (LinearLayout) findViewById(R.id.registration_linear_stou_register);
            linear_stou = (LinearLayout) findViewById(R.id.registration_linear_stou);
            linear_facebook = (LinearLayout) findViewById(R.id.registration_linear_facebook);
            linear_email = (LinearLayout) findViewById(R.id.registration_linear_email);
            linear_signup = (LinearLayout) findViewById(R.id.registration_linear_signup);
            linear_withoutlogin = (LinearLayout) findViewById(R.id.registration_linear_withoutlogin);

            txt_f1 = (TextView) findViewById(R.id.registration_textview_facebook1);
            txt_f2 = (TextView) findViewById(R.id.registration_textview_facebook2);
            txt_email1 = (TextView) findViewById(R.id.registration_textview_email1);
            txt_email2 = (TextView) findViewById(R.id.registration_textview_email2);
            txt_sigup1 = (TextView) findViewById(R.id.registration_textview_signup1);
            txt_sigup2 = (TextView) findViewById(R.id.registration_textview_signup2);
            txt_stou1 = (TextView) findViewById(R.id.registration_textview_stou1);
            txt_stou2 = (TextView) findViewById(R.id.registration_textview_stou2);
            txt_regis_stou1 = (TextView) findViewById(R.id.registration_textview_stou1_register);
            txt_regis_stou2 = (TextView) findViewById(R.id.registration_textview_stou2_register);
            txt_title = (TextView) findViewById(R.id.registration_textview_title);
            txt_withoutlogin1 = (TextView) findViewById(R.id.registration_textview_withoutlogin);
            txt_withoutlogin2 = (TextView) findViewById(R.id.registration_textview_withoutlogin2);

            txt_regis_stou1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_regis_stou2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_stou1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_stou2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_withoutlogin1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_withoutlogin2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_f1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_f2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_email1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_email2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_sigup1.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_sigup2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_title.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));

            linear_email.setOnClickListener(new clickWithLogin());
            linear_signup.setOnClickListener(new clickWithLogin());
            linear_withoutlogin.setOnClickListener(new clickWithLogin());
            linear_stou.setOnClickListener(new clickWithLogin());
            linear_stou_regis.setOnClickListener(new clickWithLogin());

            btn_authfacebook = (ImageView) findViewById(R.id.registration_authFacebook);

            checkFBLogin();

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    private void checkFBLogin() {
        if (StaticUtils.getFacebooklogin()) {
            changeFacebookIcon(R.drawable.btn_logout_facebook);
            btn_authfacebook.setOnClickListener(facebookLogoutListener);
        } else {
            btn_authfacebook.setOnClickListener(facebookLoginListener);
        }
    }

    private View.OnClickListener facebookLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragment != null) {
                initFacebookFragment(fragment);
            } else {
                initFacebookActivity(activity);
            }
        }
    };

    private View.OnClickListener facebookLogoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegisterFacebook.FacebookLogout();
            removeAccount();
        }
    };

    private void changeFacebookIcon(final int drawable) {
        if (fragment != null) {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_authfacebook.setImageDrawable(fragment.getResources().getDrawable(drawable));
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_authfacebook.setImageDrawable(fragment.getResources().getDrawable(drawable));
                }
            });
        }
    }

    private void initFacebookFragment(final Fragment fragment) {
        RegisterFacebook.FacebookLogout();
        RegisterFacebook.ReadPermissionFacebookFragment(fragment);
        LoginManager.getInstance().registerCallback(RegisterFacebook.mCallbackManager, resultCallBack);
    }

    private void initFacebookActivity(final Activity activity) {
        RegisterFacebook.FacebookLogout();
        RegisterFacebook.ReadPermissionFacebookActivity(activity);
        LoginManager.getInstance().registerCallback(RegisterFacebook.mCallbackManager, resultCallBack);
    }

    private FacebookCallback<LoginResult> resultCallBack = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            // TODO Auto-generated method stub
            if (loginResult != null && !loginResult.getAccessToken().equals("")) {
                StaticUtils.setFacebooklogin(true);
                RegisterFacebook.accessToken = loginResult.getAccessToken();
                fetchGraphAPI(loginResult.getAccessToken());
            }
        }

        @Override
        public void onError(FacebookException error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

        }
    };

    public void fetchGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject jsonUser,
                                            GraphResponse response) {
                        if (jsonUser != null) {
                            StaticUtils.setFacebooklogin(true);
                            FacebookLogin(jsonUser);
                        } else {
                            return;
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,birthday,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void removeAccount() {

        String registerURL = AppMain.LOGOUT_URL_STOU;
        // end
        registerURL += "cid=" + Shared_Object.getCustomerDetail.getCID();
        registerURL += "&udid=" + Shared_Object.getDeviceID(getContext());

        // register
        Log.e("registerURL", "cid" + registerURL);
        LoadAPIResultString apiResultString = new LoadAPIResultString();
        apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

            @Override
            public void completeResult(String cid) {
                ResetAccountTask resetAccountTask = new ResetAccountTask();
                resetAccountTask.execute();
            }
        });
        apiResultString.execute(registerURL);

    }

    private class ResetAccountTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Shared_Object.getCustomerDetail = new Model_Customer_Detail(null);
            File customerFile = new File(context.getFilesDir(), "/" + "customer_detail.porar");
            if (customerFile.exists()) {
                customerFile.delete();
                customerFile = null;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            System.gc();
            Toast.makeText(getContext(), "Logout", Toast.LENGTH_LONG).show();
            changeFacebookIcon(R.drawable.btn_login_facebook);
        }
    }

    protected void FacebookLogin(final JSONObject jsonUser) {
        im1.setVisibility(View.GONE);
        p1.setVisibility(View.VISIBLE);
        String loginURL = "";
        loginURL = AppMain.LOGIN_FACEBOOK_URL + jsonUser.optString("id").toString();
        if (jsonUser.optString("email") != null && !jsonUser.optString("email").toString().equals("")) {
            loginURL += "&email=" + jsonUser.optString("email").toString();
        } else {
            loginURL += "&email=" + jsonUser.optString("id").toString();
        }
        loginURL += "&firstname=" + jsonUser.optString("first_name").toString();
        loginURL += "&lastname=" + jsonUser.optString("first_name").toString();
        loginURL += "&gender=" + jsonUser.optString("gender").toString();

        LoadAPIResultString apiResultString = new LoadAPIResultString();
        apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

            @Override
            public void completeResult(String cid) {
                try {

                    Log.w("", "FacebookLogin cid " + cid);
                    getPasswordFacebook(jsonUser.optString("id").toString(), cid);
                } catch (Exception e) {

                }
            }
        });
        apiResultString.execute(loginURL);
    }

    protected void getPasswordFacebook(final String FacebookId, final String cid) {

        String getPasswordURL = "";
        // facebook
        // getPasswordURL =
        // "http://api.ebooks.in.th/facebook-password.ashx?fbid=" + FacebookId +
        // "&cid=" + cid;
        getPasswordURL = AppMain.GET_PASSWORD_FACEBOOK_URL + FacebookId
                + "&cid=" + cid;
        LoadAPIResultString apiResultString = new LoadAPIResultString();
        apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

            @Override
            public void completeResult(String password) {
                try {
                    passFacebook = password;
                    getCustomer_Detail(FacebookId, cid, password);
                    Log.w("", "passFacebook " + passFacebook);
                } catch (NullPointerException e) {
                    // not password
                }

            }
        });
        apiResultString.execute(getPasswordURL);
    }

    // response PLIST
    protected void getCustomer_Detail(final String FacebookId,
                                      final String cid, final String passwordfacebook) {
        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                        RefreshloadCustomer_Detail(FacebookId,
                                                cid, passwordfacebook);

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();

                    }
                });
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {

            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                        RefreshloadCustomer_Detail(FacebookId,
                                                cid, passwordfacebook);

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        PList result) {
                try {
                    Array plistObject = (Array) result.getRootElement();
                    Model_Customer_Detail modelCustomerDetail = new Model_Customer_Detail(
                            plistObject);


                    if (!modelCustomerDetail.getSubjectID().equals("")) {
                        MySharedPref mySharedPref = new MySharedPref(getContext());
                        mySharedPref.setBCodeList(MySharedPref.TAG_BCODE, modelCustomerDetail.getSubjectID());
                        mySharedPref.getBCodeList(MySharedPref.TAG_BCODE, StaticUtils.temp_bcode);
                    }

                    modelCustomerDetail.setFacebookId(FacebookId);
                    modelCustomerDetail.setPassword(passwordfacebook);
                    modelCustomerDetail.setFavorites(modelCustomerDetail
                            .getFavorites());

                    Shared_Object.getCustomerDetail = modelCustomerDetail;
                    Class_Manage.SaveEbooksObject(getContext(),
                            modelCustomerDetail, "customer_detail.porar");
                    // StaticUtils.Login = 0;
                    DialogLogin.this.cancel();


                } catch (NullPointerException e) {
                    Log.i("", " Parse Error ");
                }

            }

            @Override
            public void onAllTaskDone() {
                try {
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                    loginComplete();
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

            }
        });
        asyncTask_FetchAPI.execute(AppMain.getAPIbyRefKey("customer-detail",
                "cid=" + cid));
    }

    private void RefreshloadCustomer_Detail(final String FacebookId,
                                            final String cid, final String passwordfacebook) {
        getCustomer_Detail(FacebookId, cid, passwordfacebook);
    }


    protected void EbookLogin(final String username, final String password) {
        init();
        im2.setVisibility(View.GONE);
        p2.setVisibility(View.VISIBLE);

        String loginURL = "";
        // ebook
        loginURL = AppMain.LOGIN_URL_STOU + username;
        loginURL += "&password=" + password;

        // Log.e("snuxker", "LOGIN URL : " + loginURL);

        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {
            Model_STOU_Students modelCustomerDetail;

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();

                    }
                });
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("", "onFetchStart" + apiURL);
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        final PList result) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            // Check Login Success // That API return values
                            // response
                            // <<<CID>>><<<MasterDegree>>><<<Student_ID>>>
                            Array plistObject = (Array) result.getRootElement();
                            modelCustomerDetail = new Model_STOU_Students(
                                    plistObject);
                            if (modelCustomerDetail.getIsTeacher() > 0) {

                                if (modelCustomerDetail.getError() > 0) {
                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = true;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
                                    // StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(cid, modelCustomerDetail);
                                }

                            } else if (modelCustomerDetail.getMasterDegree() > 0) {

                                if (modelCustomerDetail.getError() > 0) {
                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = true;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = false;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
                                    // StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(cid, modelCustomerDetail);
                                }

                            } else if (modelCustomerDetail.getBechalorDegree() > 0) {
                                if (modelCustomerDetail.getError() > 0) {

                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = true;
                                    StaticUtils.isTeacher = false;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
                                    // StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(cid, modelCustomerDetail);
                                }
                            } else {
                                StaticUtils.isAMasterDegree = false;
                                StaticUtils.isBechalorDegree = false;
                                StaticUtils.isTeacher = false;
                                getCustomer_Detail("", modelCustomerDetail.getCID(), password);
                                if (modelCustomerDetail.getError() > 0) {
                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                }
                            }

                            // DialogLogin.this.cancel();
                        } catch (NullPointerException e) {
                            Log.i("", " Parse Error ");
                        }
                    }
                });
            }

            @Override
            public void onAllTaskDone() {
                try {
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    im4.setVisibility(View.VISIBLE);
                    p4.setVisibility(View.GONE);

                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

            }
        });
        asyncTask_FetchAPI.execute(loginURL);

    }


    class clickWithLogin implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getId() == linear_email.getId()) {
                setViewLoginEmailToApp();
            }
            if (v.getId() == linear_signup.getId()) {
                setViewRegisterEmailToApp();
            }
            if (v.getId() == linear_stou.getId()) {
                setViewLoginEmailSTOU();
            }
            if (v.getId() == linear_stou_regis.getId()) {
                setViewRegisterEmailToSTOU();
            }
            if (v.getId() == linear_withoutlogin.getId()) {
                try {
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

                DialogLogin.this.dismiss();
            }
        }

    }

    protected void EbookRegisterToApp() {
        init();
        im3.setVisibility(View.GONE);
        p3.setVisibility(View.VISIBLE);

        // String registerURL = "http://api.ebooks.in.th/register.ashx?";
        // stou

        String registerURL = AppMain.REGISTER_URL;
        // end
        registerURL += "email=" + re_email;
        registerURL += "&password=" + re_pass;
        registerURL += "&name=" + URLEncoder.encode(re_name);
        registerURL += "&surname=" + URLEncoder.encode(re_surname);
        registerURL += "&mobile=" + URLEncoder.encode(re_mobile);
        registerURL += "&birthdate=" + URLEncoder.encode(re_brithdate);
        // register
        Log.e("registerURL", "cid" + registerURL);
        LoadAPIResultString apiResultString = new LoadAPIResultString();
        apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

            @Override
            public void completeResult(String cid) {
                try {
                    Log.w("", "EbookLogin cid " + cid);

                    if (Integer.parseInt(cid) > 0) {
                        getCustomer_Detail("", cid, re_pass);

                    } else {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_exsiting_email));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        try {
                                            im3.setVisibility(View.VISIBLE);
                                            p3.setVisibility(View.GONE);
                                        } catch (NullPointerException e) {

                                        }

                                        dialog.dismiss();
                                        System.gc();
                                    }

                                });
                        alertDialog.show();
                    }

                } catch (Exception e) {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }

            }
        });
        apiResultString.execute(registerURL);
    }

    private void setViewRegisterEmailToApp() {
        try {
            re_email = "";
            re_pass = "";
            re_name = "";
            re_surname = "";
            re_mobile = "";
            re_brithdate = "";

            setContentView(R.layout.dialog_register);

            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.register_linear_main);
                registration_linear_main.setLayoutParams(param);
            }

            // stou
            headRegisterTextView = (TextView) findViewById(R.id.register_textview_title);
            // end
            TextView guideTextView = (TextView) findViewById(R.id.guide_text);
            et_email = (EditText) findViewById(R.id.register_edit_email);
            et_pass = (EditText) findViewById(R.id.register_edit_pass);
            et_confirm = (EditText) findViewById(R.id.register_edit_pass2);
            et_name = (EditText) findViewById(R.id.register_edit_name);
            et_surname = (EditText) findViewById(R.id.register_edit_surname);
            et_mobile = (EditText) findViewById(R.id.register_edit_mobile);
            et_birthdate = (EditText) findViewById(R.id.register_edit_birthdate);
            bt_cancel = (Button) findViewById(R.id.register_btn_cancel);
            bt_register = (Button) findViewById(R.id.register_btn_login);

            // stou
            headRegisterTextView.setTypeface(StaticUtils.getTypeface(
                    getContext(), Font.DB_HelvethaicaMon_X));
            // end
            guideTextView.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_confirm.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_name.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_surname.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_mobile.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_birthdate.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            bt_cancel.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            bt_register.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));

            bt_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    init();
                }
            });

            bt_register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (et_email.getEditableText().length() > 0) {
                        if (StaticUtils.validEmail(et_email.getEditableText()
                                .toString())) {
                            re_email = et_email.getEditableText().toString();

                            if (et_pass.getEditableText().length() > 0) {
                                if (et_confirm.getEditableText().length() > 0) {
                                    if (et_pass
                                            .getEditableText()
                                            .toString()
                                            .equals(et_confirm
                                                    .getEditableText()
                                                    .toString())) {
                                        re_pass = et_confirm.getEditableText()
                                                .toString();
                                        if (et_name.getEditableText().length() > 0) {
                                            re_name = et_name.getEditableText()
                                                    .toString();
                                            if (et_surname.getEditableText()
                                                    .length() > 0) {
                                                re_surname = et_surname
                                                        .getEditableText()
                                                        .toString();
                                                if (et_mobile.getEditableText()
                                                        .length() > 0) {
                                                    re_mobile = et_mobile
                                                            .getEditableText()
                                                            .toString();
                                                    if (et_birthdate
                                                            .getEditableText()
                                                            .length() > 0) {
                                                        re_brithdate = et_birthdate
                                                                .getEditableText()
                                                                .toString();

                                                        if (re_email != ""
                                                                & re_pass != ""
                                                                & re_name != ""
                                                                & re_surname != ""
                                                                & re_mobile != ""
                                                                & re_brithdate != "") {
                                                            Log.e("", ""
                                                                    + re_email);
                                                            Log.e("", ""
                                                                    + re_pass);
                                                            Log.e("", ""
                                                                    + re_name);
                                                            Log.e("",
                                                                    ""
                                                                            + re_surname);
                                                            Log.e("", ""
                                                                    + re_mobile);
                                                            Log.e("",
                                                                    ""
                                                                            + re_brithdate);

                                                            EbookRegisterToApp();
                                                        }
                                                    } else {
                                                        registerEmail();
                                                    }
                                                } else {
                                                    registerEmail();
                                                }
                                            } else {
                                                registerEmail();
                                            }

                                        } else {
                                            registerEmail();
                                        }
                                    } else {
                                        alertDialog = new AlertDialog.Builder(
                                                getContext()).create();
                                        alertDialog.setTitle(AppMain.getTag());
                                        alertDialog
                                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_password));
                                        alertDialog
                                                .setButton(
                                                        AlertDialog.BUTTON_POSITIVE,
                                                        getContext().getResources().getString(R.string.message_ok),
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
                                } else {
                                    alertDialog = new AlertDialog.Builder(
                                            getContext()).create();
                                    alertDialog.setTitle(AppMain.getTag());
                                    alertDialog
                                            .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_password));
                                    alertDialog
                                            .setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getContext().getResources().getString(R.string.message_ok),
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
                            } else {
                                registerEmail();
                            }
                        } else {
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog.setMessage(getContext().getResources().getString(R.string.message_invalid_email));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    getContext().getResources().getString(R.string.message_ok),
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
                    } else {
                        registerEmail();
                    }

                }

                private void registerEmail() {

                    alertDialog = new AlertDialog.Builder(getContext())
                            .create();
                    alertDialog.setTitle(AppMain.getTag());
                    alertDialog.setMessage(getContext().getResources().getString(R.string.message_invalid_infomation));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            getContext().getResources().getString(R.string.message_ok),
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

            });
            et_birthdate.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                        if (v == et_birthdate) {
                            showDialog();
                            return true;
                        }
                    }

                    return false;
                }

                private void showDialog() {
                    Calendar c = Calendar.getInstance();
                    int cyear = c.get(Calendar.YEAR);
                    int cmonth = c.get(Calendar.MONTH);
                    int cday = c.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(getContext(),
                            mDateSetListener, cyear, cmonth, cday);
                    datePickerDialog.show();
                }
            });

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    @SuppressLint("SimpleDateFormat")
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date_selected1 = String.valueOf(dayOfMonth) + " /"
                    + String.valueOf(monthOfYear + 1) + " /"
                    + String.valueOf(year);

            re_brithdate = "19700101";
            SimpleDateFormat df = new SimpleDateFormat("dd /MM /yyyy");
            try {
                Date dateFromString = df.parse(date_selected1);

                df = new SimpleDateFormat("yyyyMMdd");
                re_brithdate = df.format(dateFromString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            et_birthdate.setText(date_selected1);
            if (datePickerDialog.isShowing()) {
                datePickerDialog.dismiss();
            }
        }
    };

    public void setViewLoginEmailToApp() {

        try {
            setContentView(id_loginwithemail);

            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.login_linear_main);
                registration_linear_main.setLayoutParams(param);
            }

            edit_email = (EditText) findViewById(R.id.login_edit_email);
            edit_pass = (EditText) findViewById(R.id.login_edit_pass);
            txt_email = (TextView) findViewById(R.id.login_textview_email);
            txt_pass = (TextView) findViewById(R.id.login_textview_pass);
            txt_titlelogin = (TextView) findViewById(R.id.login_textview_title);
            txt_titlelogin2 = (TextView) findViewById(R.id.login_textview_title2);
            btn_login = (Button) findViewById(R.id.login_btn_login);
            btn_cancel = (Button) findViewById(R.id.login_btn_cancel);

            btn_cancel.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            btn_login.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            edit_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            edit_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_titlelogin.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_titlelogin2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));

            if (bundle.getString(serial_username) != null) {
                edit_email.setText(bundle.getString(serial_username));
            }
            if (bundle.getString(serial_password) != null) {
                edit_pass.setText(bundle.getString(serial_password));
            }
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    init();
                }
            });

            btn_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (edit_email.getEditableText().toString().length() <= 0) {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage(getContext().getResources().getString(R.string.message_fill_email));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                    }

                                });
                        alertDialog.show();
                    } else if (edit_pass.getEditableText().toString().length() <= 0) {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage(getContext().getResources().getString(R.string.message_fill_password));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                    }

                                });
                        alertDialog.show();
                    } else {
                        if (StaticUtils.validEmail(edit_email.getEditableText()
                                .toString())) {

                            bundle.putString(serial_username,
                                    edit_email.getEditableText() + "");
                            bundle.putString(serial_password,
                                    edit_pass.getEditableText() + "");

                            EbookLogin(edit_email.getEditableText().toString(),
                                    edit_pass.getEditableText().toString());

                        } else {
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog.setMessage(getContext().getResources().getString(R.string.message_invalid_email));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    getContext().getResources().getString(R.string.message_ok),
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

                }
            });
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    private void createSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        re_type = "b";
//                        Toast.makeText(parent.getContext(), "Selected: " + re_type, Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        re_type = "m";
//                        Toast.makeText(parent.getContext(), "Selected: " + re_type, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        re_type = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add(getContext().getResources().getString(R.string.title_selected));
        categories.add(getContext().getResources().getString(R.string.bechalor_degree));
        categories.add(getContext().getResources().getString(R.string.master_degree));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setVisibility(View.VISIBLE);
    }

    private void setViewRegisterEmailToSTOU() {

        // This Method to display layout for Login

        try {
            re_studentID = "";
            re_email = "";
            re_pass = "";
            re_name = "";
            re_surname = "";
            re_mobile = "";
            re_brithdate = "";

            setContentView(R.layout.dialog_register);
            createSpinner();
            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.register_linear_main);
                registration_linear_main.setLayoutParams(param);
            }

            // stou
            TextView guideTextView = (TextView) findViewById(R.id.guide_text);
            headRegisterTextView = (TextView) findViewById(R.id.register_textview_title);
            // end

            et_mobile = (EditText) findViewById(R.id.register_edit_mobile);
            et_mobile.setHint(getContext().getResources().getString(R.string.student_id));
            et_studentID = (EditText) findViewById(R.id.register_edit_surname);
            et_email = (EditText) findViewById(R.id.register_edit_email);
            et_email = (EditText) findViewById(R.id.register_edit_email);
            et_pass = (EditText) findViewById(R.id.register_edit_pass);
            et_confirm = (EditText) findViewById(R.id.register_edit_pass2);
            et_name = (EditText) findViewById(R.id.register_edit_name);
            et_surname = (EditText) findViewById(R.id.register_edit_surname);

            et_birthdate = (EditText) findViewById(R.id.register_edit_birthdate);
            bt_cancel = (Button) findViewById(R.id.register_btn_cancel);
            bt_register = (Button) findViewById(R.id.register_btn_login);

            // stou
            headRegisterTextView.setTypeface(StaticUtils.getTypeface(
                    getContext(), Font.DB_HelvethaicaMon_X));
            // end
            guideTextView.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_mobile.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_studentID.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_confirm.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_name.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            et_surname.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));

            et_birthdate.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            bt_cancel.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            bt_register.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));

            bt_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    init();
                }
            });

            bt_register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (et_email.getEditableText().length() > 0) {
                        if (StaticUtils.validEmail(et_email.getEditableText()
                                .toString())) {
                            re_email = et_email.getEditableText().toString();

                            if (et_pass.getEditableText().length() > 0) {
                                if (et_confirm.getEditableText().length() > 0) {
                                    if (et_pass
                                            .getEditableText()
                                            .toString()
                                            .equals(et_confirm
                                                    .getEditableText()
                                                    .toString())) {
                                        re_pass = et_confirm.getEditableText()
                                                .toString();
                                        if (et_name.getEditableText().length() > 0) {
                                            re_name = et_name.getEditableText()
                                                    .toString();
                                            if (et_surname.getEditableText()
                                                    .length() > 0) {
                                                re_surname = et_surname
                                                        .getEditableText()
                                                        .toString();
                                                if (et_studentID
                                                        .getEditableText()
                                                        .length() > 0) {
                                                    re_studentID = et_mobile
                                                            .getEditableText()
                                                            .toString();
                                                    if (re_brithdate.length() > 0)

                                                    {

                                                        if (re_email != ""
                                                                & re_pass != ""
                                                                & re_name != ""
                                                                & re_surname != ""
                                                                & re_studentID != ""
                                                                & re_brithdate != "") {
                                                            Log.e("", ""
                                                                    + re_email);
                                                            Log.e("", ""
                                                                    + re_pass);
                                                            Log.e("", ""
                                                                    + re_name);
                                                            Log.e("",
                                                                    ""
                                                                            + re_surname);
                                                            Log.e("",
                                                                    ""
                                                                            + re_studentID);
                                                            Log.e("",
                                                                    ""
                                                                            + re_brithdate);
                                                            Log.e("registerEmail",
                                                                    "cid"
                                                                            + re_email);
                                                            checkRegisterSTOU(
                                                                    re_name,
                                                                    re_surname,
                                                                    re_studentID,
                                                                    re_brithdate,
                                                                    re_email,
                                                                    re_pass, re_type);
                                                        }
                                                    } else {
                                                        registerEmail();
                                                    }
                                                } else {
                                                    registerEmail();
                                                }
                                            } else {
                                                registerEmail();
                                            }

                                        } else {
                                            registerEmail();
                                        }
                                    } else {
                                        alertDialog = new AlertDialog.Builder(
                                                getContext()).create();
                                        alertDialog.setTitle(AppMain.getTag());
                                        alertDialog
                                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_password));
                                        alertDialog
                                                .setButton(
                                                        AlertDialog.BUTTON_POSITIVE,
                                                        getContext().getResources().getString(R.string.message_ok),
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
                                } else {
                                    alertDialog = new AlertDialog.Builder(
                                            getContext()).create();
                                    alertDialog.setTitle(AppMain.getTag());
                                    alertDialog
                                            .setMessage(
                                                    getContext().getResources().getString(R.string.message_title_dialog_error_password));
                                    alertDialog
                                            .setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getContext().getResources().getString(R.string.message_ok),
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
                            } else {
                                registerEmail();
                            }
                        } else {
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog.setMessage(
                                    getContext().getResources().getString(R.string.message_invalid_email));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    getContext().getResources().getString(R.string.message_ok),
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
                    } else {
                        registerEmail();
                    }

                }

                private void registerEmail() {

                    alertDialog = new AlertDialog.Builder(getContext())
                            .create();
                    alertDialog.setTitle(AppMain.getTag());
                    alertDialog.setMessage(getContext().getResources().getString(R.string.message_invalid_infomation));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            getContext().getResources().getString(R.string.message_ok),
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

            });
            et_birthdate.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                        if (v == et_birthdate) {
                            showDialog();
                            return true;
                        }
                    }

                    return false;
                }

                private void showDialog() {
                    Calendar c = Calendar.getInstance();
                    int cyear = c.get(Calendar.YEAR);
                    int cmonth = c.get(Calendar.MONTH);
                    int cday = c.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(getContext(),
                            mDateSetListener, cyear, cmonth, cday);
                    datePickerDialog.show();
                }
            });

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    protected void checkRegisterSTOU(final String name, final String surname,
                                     final String id, final String birthdate, final String email,
                                     final String password, final String type) {
        // This method do Check Register , After Enter the Info Success
        if (type.equals("")) {
            alertDialog = new AlertDialog.Builder(getContext())
                    .create();
            alertDialog.setTitle(AppMain.getTag());
            alertDialog.setMessage(getContext().getResources().getString(R.string.message_must_choose));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    getContext().getResources().getString(R.string.message_ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            alertDialog.dismiss();
                        }

                    });
            alertDialog.show();
            return;
        }
        String registerStudent = AppMain.CHECK_STUDENT_AVAIABLE;
        // end
        registerStudent += "&student_id=" + re_studentID;
        registerStudent += "&student_name=" + URLEncoder.encode(re_name);
        registerStudent += "&student_surname=" + URLEncoder.encode(re_surname);
        registerStudent += "&student_birthdate="
                + URLEncoder.encode(re_brithdate);
        registerStudent += "&student_email=" + re_email;
        registerStudent += "&student_password=" + re_pass;
        registerStudent += "&student_type=" + type;

        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {
            Model_STOU_Students modelCustomerDetail;

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(
                                        getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                        // getStudent_STOU(name, surname, id,
                                        // birthdate, email, password);

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();

                    }
                });
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("API", "apiURL " + apiURL);
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                        // getStudent_STOU(name, surname, id,
                                        // birthdate, email, password);

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        final PList result) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            Array plistObject = (Array) result.getRootElement();
                            modelCustomerDetail = new Model_STOU_Students(
                                    plistObject);
                            // After check Register Success
                            // response
                            // <<<CID>>><<<MasterDegree>>><<<Student_ID>>>
                            // 361245 , 0 , 5230230023
                            if (!modelCustomerDetail.getCID().equals("0")) {

                                // Return 361245 , 1, 5230230023 or 361245 , 0,
                                // 5230230023
                                if (modelCustomerDetail.getIsTeacher() > 0) {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = true;
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);

                                } else if (modelCustomerDetail.getMasterDegree() > 0) {
                                    StaticUtils.isAMasterDegree = true;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = false;
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);
                                } else if (modelCustomerDetail.getBechalorDegree() > 0) {

                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = true;
                                    StaticUtils.isTeacher = false;
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);
                                } else {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = false;
                                    alertDialog = new AlertDialog.Builder(
                                            getContext()).create();
                                    alertDialog.setTitle(AppMain.getTag());
                                    alertDialog
                                            .setMessage(getContext().getResources().getString(R.string.message_title_dialog_master_degree));
                                    alertDialog
                                            .setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getContext().getResources().getString(R.string.message_register_again_dialog_master_degree),
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            try {
                                                                im3.setVisibility(View.VISIBLE);
                                                                p3.setVisibility(View.GONE);
                                                            } catch (NullPointerException e) {

                                                            }

                                                            dialog.dismiss();
                                                            System.gc();
                                                        }

                                                    });
                                    alertDialog.show();
                                }

                            } else {

                                // <<<CID>>><<<MasterDegree>>><<<Student_ID>>>
                                // Return 0 , 0 , 0
                                alertDialog = new AlertDialog.Builder(
                                        getContext()).create();
                                alertDialog.setTitle(AppMain.getTag());
                                alertDialog
                                        .setMessage(getContext().getResources().getString(R.string.message_title_dialog_master_degree));
                                alertDialog.setButton(
                                        AlertDialog.BUTTON_POSITIVE,
                                        getContext().getResources().getString(R.string.message_register_again_dialog_master_degree),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                try {
                                                    im3.setVisibility(View.VISIBLE);
                                                    p3.setVisibility(View.GONE);
                                                } catch (NullPointerException e) {

                                                }

                                                dialog.dismiss();
                                                System.gc();
                                            }

                                        });
                                alertDialog.show();
                            }

                            // DialogLogin.this.cancel();
                        } catch (NullPointerException e) {
                            Log.i("", " Parse Error ");

                        }
                    }
                });

            }

            @Override
            public void onAllTaskDone() {
                try {
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

            }
        });
        Log.e("Regis", "" + registerStudent);
        asyncTask_FetchAPI.execute(registerStudent);
    }

    protected void getInfo_STOU(final String CID, final Model_STOU_Students model_STOUStudents) {
        Log.w("No", "getInfo_STOU ");
        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.w("No", "onTimeOut ");
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("", "onFetchStart" + apiURL);
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.w("", "onFetchError ");
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        final PList result) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {


                            if (!model_STOUStudents.getSubjectID().equals("")) {
                                MySharedPref mySharedPref = new MySharedPref(getContext());
                                mySharedPref.setBCodeList(MySharedPref.TAG_BCODE, model_STOUStudents.getSubjectID());
                                mySharedPref.getBCodeList(MySharedPref.TAG_BCODE, StaticUtils.temp_bcode);
                            }

                            // response Info Studennt STOU
                            // <<<NAME>>><<<Surname>>><<<MasterDegree>>> *
                            Log.w("", "onFetchComplete ");
                            Array plistObject = (Array) result.getRootElement();
                            Model_Customer_Detail model_Customer_Detail = new Model_Customer_Detail(
                                    plistObject);
                            model_Customer_Detail.setPassword(re_pass);
                            Shared_Object.getCustomerDetail = model_Customer_Detail;
                            Class_Manage.SaveEbooksObject(getContext(),
                                    model_Customer_Detail,
                                    "customer_detail.porar");
                            DialogLogin.this.cancel();
                        } catch (NullPointerException e) {
                            Log.i("", " Parse Error ");
                        }
                    }
                });

            }

            @Override
            public void onAllTaskDone() {
                try {
                    Log.w("No", "onAllTaskDone ");
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                    loginComplete();
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

            }
        });

        asyncTask_FetchAPI.execute(AppMain.getAPIbyRefKey("customer-detail",
                "cid=" + CID));
    }

    public void setViewLoginEmailSTOU() {

        try {
            setContentView(id_loginwithemail);

            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.login_linear_main);
                registration_linear_main.setLayoutParams(param);
            }

            edit_email = (EditText) findViewById(R.id.login_edit_email);
            edit_pass = (EditText) findViewById(R.id.login_edit_pass);
            txt_email = (TextView) findViewById(R.id.login_textview_email);
            txt_pass = (TextView) findViewById(R.id.login_textview_pass);
            txt_titlelogin = (TextView) findViewById(R.id.login_textview_title);
            txt_titlelogin2 = (TextView) findViewById(R.id.login_textview_title2);
            btn_login = (Button) findViewById(R.id.login_btn_login);
            btn_cancel = (Button) findViewById(R.id.login_btn_cancel);

            btn_cancel.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            btn_login.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            edit_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            edit_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_email.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_pass.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_titlelogin.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));
            txt_titlelogin2.setTypeface(StaticUtils.getTypeface(getContext(),
                    Font.DB_HelvethaicaMon_X));


            if (bundle.getString(serial_username) != null) {
                edit_email.setText(bundle.getString(serial_username));
            }
            if (bundle.getString(serial_password) != null) {
                edit_pass.setText(bundle.getString(serial_password));
            }
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    init();
                }
            });

            btn_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (edit_email.getEditableText().toString().length() <= 0) {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage(getContext().getResources().getString(R.string.message_fill_email));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                    }

                                });
                        alertDialog.show();
                    } else if (edit_pass.getEditableText().toString().length() <= 0) {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage(getContext().getResources().getString(R.string.message_fill_password));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();
                                    }

                                });
                        alertDialog.show();
                    } else {
                        if (StaticUtils.validEmail(edit_email.getEditableText()
                                .toString())) {

                            bundle.putString(serial_username,
                                    edit_email.getEditableText() + "");
                            bundle.putString(serial_password,
                                    edit_pass.getEditableText() + "");

                            EbookLoginSTOU(edit_email.getEditableText()
                                    .toString(), edit_pass.getEditableText()
                                    .toString());

                        } else {
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .create();
                            alertDialog.setTitle(AppMain.getTag());
                            alertDialog.setMessage(getContext().getResources().getString(R.string.message_invalid_email));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    getContext().getResources().getString(R.string.message_ok),
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

                }
            });
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    public abstract void loginComplete();

    protected void EbookLoginSTOU(final String username, final String password) {

        // This Method do CheckLogin , After Enter Email & Password Success !!

        init();
        im4.setVisibility(View.GONE);
        p4.setVisibility(View.VISIBLE);
        String type = "android";
        String loginURL = "";
        // ebook
        loginURL = AppMain.LOGIN_URL_STOU + username;
        loginURL += "&password=" + password;
        loginURL += "&type=" + type;
        loginURL += "&udid=" + StaticUtils.deviceID;
        // Log.e("snuxker", "LOGIN URL : " + loginURL);

        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {
            Model_STOU_Students modelCustomerDetail;

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();

                    }
                });
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                Log.e("", "onFetchStart" + apiURL);
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog
                                .setMessage(getContext().getResources().getString(R.string.message_title_dialog_error_internet));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                getContext().getResources().getString(R.string.message_try_again),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        System.gc();

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterFacebook.FacebookLogout();
                                        dialog.dismiss();
                                        System.gc();
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        final PList result) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            // Check Login Success // That API return values
                            // response
                            // <<<CID>>><<<MasterDegree>>><<<Student_ID>>>
                            Array plistObject = (Array) result.getRootElement();
                            modelCustomerDetail = new Model_STOU_Students(
                                    plistObject);
                            if (modelCustomerDetail.getIsTeacher() > 0) {
                                if (modelCustomerDetail.getError() > 0) {
                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = true;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
//									StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);
                                }

                            } else if (modelCustomerDetail.getMasterDegree() > 0) {

                                if (modelCustomerDetail.getError() > 0) {
                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = true;
                                    StaticUtils.isBechalorDegree = false;
                                    StaticUtils.isTeacher = false;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
                                    // StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(cid, modelCustomerDetail);
                                }

                            } else if (modelCustomerDetail.getBechalorDegree() > 0) {
                                if (modelCustomerDetail.getError() > 0) {

                                    checkErrorCode(modelCustomerDetail.getError(), im4, p4);
                                } else {
                                    StaticUtils.isAMasterDegree = false;
                                    StaticUtils.isBechalorDegree = true;
                                    StaticUtils.isTeacher = false;
                                    re_pass = password;
                                    String cid = modelCustomerDetail.getCID();
                                    // StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(cid, modelCustomerDetail);
                                }
                            } else {
                                StaticUtils.isAMasterDegree = false;
                                StaticUtils.isBechalorDegree = false;
                                StaticUtils.isTeacher = false;
                                alertDialog = new AlertDialog.Builder(
                                        getContext()).create();
                                alertDialog.setTitle(AppMain.getTag());
                                alertDialog.setMessage(getContext().getResources().getString(R.string.message_title_dialog_exsiting_email));
                                alertDialog.setButton(
                                        AlertDialog.BUTTON_POSITIVE,
                                        getContext().getResources().getString(R.string.message_register_again_dialog_master_degree),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                try {
                                                    im4.setVisibility(View.VISIBLE);
                                                    p4.setVisibility(View.GONE);
                                                } catch (NullPointerException e) {

                                                }

                                                dialog.dismiss();
                                                System.gc();
                                            }

                                        });
                                alertDialog.show();
                            }

                            // DialogLogin.this.cancel();
                        } catch (NullPointerException e) {
                            Log.i("", " Parse Error ");
                        }
                    }
                });
            }

            @Override
            public void onAllTaskDone() {
                try {
                    im1.setVisibility(View.VISIBLE);
                    im2.setVisibility(View.VISIBLE);
                    im3.setVisibility(View.VISIBLE);
                    im4.setVisibility(View.VISIBLE);
                    p4.setVisibility(View.GONE);

                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }

            }
        });
        asyncTask_FetchAPI.execute(loginURL);
    }

    private void checkErrorCode(int errorCode, ImageView imageView, ProgressBar progressBar) {
        Resources resources = context.getResources();
        switch (errorCode) {
            case 201:
                //รหัส นศ ป.ตรี ไม่ถูก
                showDialogError(resources.getString(R.string.error_201), imageView, progressBar);
                break;
            case 202:
                //รหัส นศ ป.โท ไม่ถูก
                showDialogError(resources.getString(R.string.error_202), imageView, progressBar);
                break;
            case 300:
                //รหัสผ่านผิด
                showDialogError(resources.getString(R.string.error_300), imageView, progressBar);
                showDialogError("", imageView, progressBar);
                break;
            case 400:
                //ไม่มีอีเมล์ในระบบ
                showDialogError(resources.getString(R.string.error_400), imageView, progressBar);
                break;
            case 500:
                //อุปกรณ์เกิน
                showDialogError(resources.getString(R.string.error_500), imageView, progressBar);
                break;
            default:
                showDialogError(resources.getString(R.string.message_try_again_dialog_master_degree), imageView, progressBar);
                break;
        }

    }

    private void showDialogError(String message, final ImageView imageView, final ProgressBar progressBar) {
        alertDialog = new AlertDialog.Builder(
                getContext()).create();
        alertDialog.setTitle(AppMain.getTag());
        alertDialog
                .setMessage(message);
        alertDialog
                .setButton(
                        AlertDialog.BUTTON_POSITIVE,
                        getContext().getResources().getString(R.string.message_try_again_dialog_master_degree),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {

                                try {
                                    imageView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } catch (NullPointerException e) {

                                }

                                dialog.dismiss();
                                System.gc();
                            }

                        });
        alertDialog.show();
    }
}
