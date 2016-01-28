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
import android.widget.Spinner;
import android.widget.TextView;

import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_STOU_Students;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.fragment.Fragment_MainPublicUser_Phone;
import com.porar.ebooks.stou.fragment.Fragment_MainPublicUser_Tablet;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Phone;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Tablet;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

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
public class DialogLogin_Master_STOU extends Dialog {

    Button btn_login, btn_cancel;
    TextView txt_email, txt_pass, txt_titlelogin, txt_titlelogin2;
    EditText edit_email, edit_pass;
    LinearLayout linear_withoutlogin, linear_stou, linear_stou_regis;
    TextView txt_withoutlogin1, txt_withoutlogin2, txt_title, txt_stou1,
            txt_regis_stou1, txt_regis_stou2, txt_stou2;
    DialogLogin_Master_STOU dialog_ShowLargeImage;
    int id_loginwithemail = R.layout.dialog_login;
    int id_choselogin = R.layout.dialog_register_master_degree;
    Fragment fragment;
    Activity activity;
    AsyncTask_FetchAPI asyncTask_FetchAPI;
    Handler handler = new Handler();
    AlertDialog alertDialog;
    // private final Session userInfoSession = null;
    ProgressDialog progressDialog;
    ProgressBar p1, p2, p3, p4, p5;
    ImageView im1, im2, im3, im4, im5;

    Bundle bundle;
    public static final String serial_username = "serial_username";
    public static final String serial_password = "serial_password";

    EditText et_email, et_pass, et_confirm, et_name, et_surname, et_mobile, et_birthdate, et_studentID;
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

    public DialogLogin_Master_STOU(Context context, int theme, Fragment fragment) {
        super(context, theme);
        this.fragment = fragment;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog_ShowLargeImage = this;
        bundle = new Bundle();

        init();

    }

    private void init() {
        setContentView(id_choselogin);
        if (AppMain.isphone) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
            LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.registration_linear_main);
            registration_linear_main.setLayoutParams(param);
        }


        im4 = (ImageView) findViewById(R.id.registration_imageView_register_stou);
        im5 = (ImageView) findViewById(R.id.registration_imageView_register_stou_register);


        p4 = (ProgressBar) findViewById(R.id.registration_progressBarstou);
        p5 = (ProgressBar) findViewById(R.id.registration_progressBarstou_register);


        im4.setVisibility(View.VISIBLE);
        p5.setVisibility(View.VISIBLE);


        p4.setVisibility(View.GONE);
        p5.setVisibility(View.GONE);


        linear_stou_regis = (LinearLayout) findViewById(R.id.registration_linear_stou_register);
        linear_stou = (LinearLayout) findViewById(R.id.registration_linear_stou);
        linear_withoutlogin = (LinearLayout) findViewById(R.id.registration_linear_withoutlogin);


        txt_stou1 = (TextView) findViewById(R.id.registration_textview_stou1);
        txt_stou2 = (TextView) findViewById(R.id.registration_textview_stou2);
        txt_regis_stou1 = (TextView) findViewById(R.id.registration_textview_stou1_register);
        txt_regis_stou2 = (TextView) findViewById(R.id.registration_textview_stou2_register);
        txt_title = (TextView) findViewById(R.id.registration_textview_title);
        txt_withoutlogin1 = (TextView) findViewById(R.id.registration_textview_withoutlogin);
        txt_withoutlogin2 = (TextView) findViewById(R.id.registration_textview_withoutlogin2);

        txt_title.setTypeface(StaticUtils.getTypeface(getContext(),
                Font.DB_HelvethaicaMon_X));
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


        linear_withoutlogin.setOnClickListener(new clickWithLogin());
        linear_stou.setOnClickListener(new clickWithLogin());
        linear_stou_regis.setOnClickListener(new clickWithLogin());


    }

    class clickWithLogin implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == linear_stou.getId()) {
                setViewLoginEmailSTOU();
            }
            if (v.getId() == linear_stou_regis.getId()) {
                setViewRegisterEmailToSTOU();
            }
            if (v.getId() == linear_withoutlogin.getId()) {
                DialogLogin_Master_STOU.this.dismiss();
            }
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


    private void createSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        re_type = "b";
                        break;
                    case 2:
                        re_type = "m";
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

            if (AppMain.isphone) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        dm.widthPixels, dm.heightPixels);
                LinearLayout registration_linear_main = (LinearLayout) findViewById(R.id.register_linear_main);
                registration_linear_main.setLayoutParams(param);
            }


            createSpinner();

            // stou
            TextView guideTextView = (TextView) findViewById(R.id.guide_text);
            headRegisterTextView = (TextView) findViewById(R.id.register_textview_title);
            // end

            et_mobile = (EditText) findViewById(R.id.register_edit_mobile);
            et_mobile.setHint(getContext().getResources().getString(R.string.student_id));
            et_studentID = (EditText) findViewById(R.id.register_edit_name);
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
                                                        getContext().getResources().getString(R.string.message_close_dialog_error_password),
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
                                                    getContext().getResources().getString(R.string.message_close_dialog_error_password),
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
                                    getContext().getResources().getString(R.string.message_close_dialog_error_password),
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
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getResources().getString(R.string.message_close_dialog_error_password),
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
                    getContext().getResources().getString(R.string.message_close_dialog_error_password),
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
                                            .setMessage(getContext().getResources().getString(R.string.message_title_dialog_exsiting_email));
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
                                        .setMessage(getContext().getResources().getString(R.string.message_title_dialog_exsiting_email));
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

                            // DialogLogin_Show.this.cancel();
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

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
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

                                    }

                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                getContext().getResources().getString(R.string.message_cancel),
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
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        final PList result) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (AppMain.isphone) {
                                Fragment_MainPublicUser_Phone.onLoadShelf = true;
                            } else {
                                Fragment_MainPublicUser_Tablet.onLoadShelf = true;
                            }

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
                            DialogLogin_Master_STOU.this.cancel();
                            if (!AppMain.isphone) {
                                Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                            } else {
                                Fragment_Shelf_Phone.fragment_shelf_phone.onResume();
                            }
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
                        alertDialog.setMessage(
                                getContext().getResources().getString(R.string.message_fill_email));
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
                        alertDialog.show();
                    } else if (edit_pass.getEditableText().toString().length() <= 0) {
                        alertDialog = new AlertDialog.Builder(getContext())
                                .create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage(getContext().getResources().getString(R.string.message_fill_password));
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
                        alertDialog.show();
                    } else {
                        if (StaticUtils.validEmail(edit_email.getEditableText().toString())) {

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
                            alertDialog.setMessage(
                                    getContext().getResources().getString(R.string.message_invalid_email));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    getContext().getResources().getString(R.string.message_try_again),
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
//									StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);
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
//									StaticUtils.Login = 0;
                                    Log.e("", "DEGREE" + cid);
                                    getInfo_STOU(modelCustomerDetail.getCID(), modelCustomerDetail);
                                }

                            } else {

                                StaticUtils.isAMasterDegree = false;
                                StaticUtils.isBechalorDegree = false;
                                StaticUtils.isTeacher = false;
                                alertDialog = new AlertDialog.Builder(
                                        getContext()).create();
                                alertDialog.setTitle(AppMain.getTag());
                                alertDialog
                                        .setMessage(
                                                getContext().getResources().getString(R.string.message_title_dialog_exsiting_email));
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

                            // DialogLogin_Show.this.cancel();
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
        Resources resources = getContext().getResources();
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
