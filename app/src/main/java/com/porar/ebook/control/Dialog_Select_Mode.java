package com.porar.ebook.control;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.stou.activity.Activity_MainTab;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.fragment.Fragment_Choice_Mode;
import com.porar.ebooks.stou.fragment.Fragment_HowTo_WebView;
import com.porar.ebooks.stou.fragment.Fragment_MainPublicUser_Tablet;
import com.porar.ebooks.stou.fragment.Fragment_MainPublicUser_Phone;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_BookList_BachelorDegree_Phone;
import com.porar.ebooks.stou.fragment.Fragment_SeeAll_BookList_MasterDegree_Phone;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Phone;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Tablet;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.io.File;

/**
 * Created by Porar on 10/8/2015.
 */
public class Dialog_Select_Mode extends Dialog {
    private Context context;
    private Fragment fragment;

    private boolean fromStudent = false;
    private String levelStudent = "";
    private LinearLayout linear_bachalor_degree, linear_master_degree, linear_other_book, linear_intro, linear_contact;
    private TextView textview_bachalor_degree, textview_master_degree, textview_other_book, textview_intro, textview_contact;
    private AdjustableImageView back_imageview;

    private DialogLogin_Master_STOU dialog_login_master_stou;

    public Dialog_Select_Mode(Context context, int theme, Fragment fragment) {
        super(context,theme);
        this.context = context;
        this.fragment = fragment;
        setStyleDialog();
        initVIew();
    }

    public Dialog_Select_Mode(Context context, int theme, Fragment fragment, boolean fromStudent, String level) {
        super(context,theme);
        this.context = context;
        this.fragment = fragment;
        this.fromStudent = fromStudent;
        this.levelStudent = level;
        setStyleDialog();
        initVIew();
    }

    private void setStyleDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setFullScreenDialog();
    }


    private void initVIew() {
        setContentView(R.layout.dialog_select_mode);

        back_imageview = (AdjustableImageView)findViewById(R.id.back_imageview);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismissDialog();
            }
        });
        linear_bachalor_degree = (LinearLayout) findViewById(R.id.linear_bachalor_degree);
        linear_master_degree = (LinearLayout) findViewById(R.id.linear_master_degree);
        linear_other_book = (LinearLayout) findViewById(R.id.linear_other_book);
        linear_intro = (LinearLayout) findViewById(R.id.linear_intro);
        linear_contact = (LinearLayout) findViewById(R.id.linear_contact);

        linear_bachalor_degree.setOnClickListener(bachalorClickListener);
        linear_master_degree.setOnClickListener(masterClickListener);
        linear_other_book.setOnClickListener(otherClickListener);
        linear_intro.setOnClickListener(introClickListener);
        linear_contact.setOnClickListener(contactClickListener);


        textview_bachalor_degree = (TextView) findViewById(R.id.textview_bachalor_degree);
        textview_master_degree = (TextView) findViewById(R.id.textview_master_degree);
        textview_other_book = (TextView) findViewById(R.id.textview_other_book);
        textview_intro = (TextView) findViewById(R.id.textview_intro);
        textview_contact = (TextView) findViewById(R.id.textview_contact);

        textview_bachalor_degree.setTypeface(StaticUtils
                .getTypeface(
                        context,
                        StaticUtils.Font.DB_HelvethaicaMon_X));
        textview_master_degree.setTypeface(StaticUtils
                .getTypeface(
                        context,
                        StaticUtils.Font.DB_HelvethaicaMon_X));
        textview_other_book.setTypeface(StaticUtils
                .getTypeface(
                        context,
                        StaticUtils.Font.DB_HelvethaicaMon_X));
        textview_intro.setTypeface(StaticUtils
                .getTypeface(
                        context,
                        StaticUtils.Font.DB_HelvethaicaMon_X));
        textview_contact.setTypeface(StaticUtils
                .getTypeface(
                        context,
                        StaticUtils.Font.DB_HelvethaicaMon_X));
    }

    private void showToastMessage() {
        Toast.makeText(context, context.getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();

    }

    private void dismissDialog() {
        dismiss();
    }

    private void xFragment(){
        fragment.getFragmentManager().popBackStack();
    }

    private View.OnClickListener bachalorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Shared_Object.getCustomerDetail.getCID() > 0) {
                if (Shared_Object.getCustomerDetail.getBechalorDegree() > 0) {
                    xFragment();
                    dismissDialog();
                    Fragment_Choice_Mode.addFragment(
                            fragment.getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,
                                            R.anim.fade_out,
                                            R.anim.fade_in,
                                            R.anim.fade_out),
                            Fragment_SeeAll_BookList_BachelorDegree_Phone.newInstance(), true,
                            FragmentTransaction.TRANSIT_FRAGMENT_OPEN);


                } else {
                    showDialogBechalorDegree();

                }
            } else {
                dialog_login_master_stou = new DialogLogin_Master_STOU(
                        context, R.style.PauseDialogAnimation,
                        fragment);
                dialog_login_master_stou.show();
            }


        }
    };

    private View.OnClickListener masterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Shared_Object.getCustomerDetail.getCID() > 0) {
                if (Shared_Object.getCustomerDetail.getMasterDegree() > 0) {
                    xFragment();
                    dismissDialog();
                    Fragment_Choice_Mode.addFragment(
                            fragment.getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,
                                            R.anim.fade_out,
                                            R.anim.fade_in,
                                            R.anim.fade_out),
                            Fragment_SeeAll_BookList_MasterDegree_Phone.newInstance(), true,
                            FragmentTransaction.TRANSIT_FRAGMENT_OPEN);


                } else {
                    showDialogMaserDegree();

                }
            } else {
                dialog_login_master_stou = new DialogLogin_Master_STOU(
                        context, R.style.PauseDialogAnimation,
                        fragment);
                dialog_login_master_stou.show();
            }

        }
    };

    private View.OnClickListener otherClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            xFragment();
            dismissDialog();
            Fragment_Choice_Mode.addFragment(
                    fragment.getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.fade_out),
                    Fragment_MainPublicUser_Phone.newInstance(), true,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN);


        }
    };

    private View.OnClickListener introClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog();
            Fragment_Choice_Mode.addFragment(
                    fragment.getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.fade_out),
                    Fragment_HowTo_WebView.newInstance(), true,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        }
    };

    private View.OnClickListener contactClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog();
            Activity_MainTab.viewPager.setCurrentItem(2, true);

        }
    };

    private void showDialogMaserDegree() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .create();
        alertDialog.setTitle(context.getResources().getString(R.string.title_logout));
        alertDialog
                .setMessage(context.getResources().getString(R.string.message_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.logout_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // clearCacheFacebookLogin();
                        if (AppMain.isphone) {
                            Fragment_MainPublicUser_Phone.onLoadShelf = true;
                        } else {
                            Fragment_MainPublicUser_Tablet.onLoadShelf = true;
                        }

                        removeAccount();
                        Shared_Object.getCustomerDetail = new Model_Customer_Detail(
                                null);
                        File customerFile = new File(context
                                .getFilesDir(), "/" + "customer_detail.porar");
                        if (customerFile.exists()) {

                            customerFile.delete();
                            customerFile = null;
                        }
                        clearCacheFacebookLogin();
                        System.gc();
                        dialog.dismiss();
                        // StaticUtils.Login = 1;
//                        DialogLogin_Master_STOU dialogLogin_STOU = new DialogLogin_Master_STOU(
//                                getActivity(), R.style.PauseDialogAnimation,
//                                Fragment_MainLogin.this);
//                        dialogLogin_STOU.show();

                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.message_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        alertDialog.show();

    }

    private void showDialogBechalorDegree() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .create();
        alertDialog.setTitle(context.getResources().getString(R.string.title_logout));
        alertDialog
                .setMessage(context.getResources().getString(R.string.message_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.logout_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // clearCacheFacebookLogin();
                        if (AppMain.isphone) {
                            Fragment_MainPublicUser_Phone.onLoadShelf = true;
                        } else {
                            Fragment_MainPublicUser_Tablet.onLoadShelf = true;
                        }
                        removeAccount();
                        Shared_Object.getCustomerDetail = new Model_Customer_Detail(
                                null);
                        File customerFile = new File(context
                                .getFilesDir(), "/" + "customer_detail.porar");
                        if (customerFile.exists()) {

                            customerFile.delete();
                            customerFile = null;
                        }
                        clearCacheFacebookLogin();
                        System.gc();
                        dialog.dismiss();
                        // StaticUtils.Login = 1;
//                        DialogLogin_Master_STOU dialogLogin_STOU = new DialogLogin_Master_STOU(
//                                getActivity(), R.style.PauseDialogAnimation,
//                                Fragment_MainLogin.this);
//                        dialogLogin_STOU.show();

                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.message_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        alertDialog.show();

    }

    public void removeAccount() {

        // String registerURL = "http://api.ebooks.in.th/register.ashx?";
        // stou

        String registerURL = AppMain.LOGOUT_URL_STOU;
        // end
        registerURL += +Shared_Object.getCustomerDetail.getCID();
        registerURL += "&udid=" + StaticUtils.deviceID;

        // register
        Log.e("registerURL", "cid" + registerURL);
        LoadAPIResultString apiResultString = new LoadAPIResultString();
        apiResultString.setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

            @Override
            public void completeResult(String cid) {
                Toast.makeText(context, "Logout", Toast.LENGTH_LONG)
                        .show();
                if (!AppMain.isphone) {
                    Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                } else {
                    Fragment_Shelf_Phone.fragment_shelf_phone.onResume();
                }

            }
        });
        apiResultString.execute(registerURL);
    }

    private void clearCacheFacebookLogin() {
        StaticUtils.Login = 0;
        StaticUtils.isAMasterDegree = false;
        StaticUtils.isBechalorDegree = false;
        StaticUtils.shelfID = 0;
        StaticUtils.pageID = 0;
        StaticUtils.phonePage = 0;
        StaticUtils.phoneValue = 0;
    }


    private void setFullScreenDialog() {
        try {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = getWindow();
            lp.copyFrom(window.getAttributes());
            // This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.TOP;
            try{
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }catch (NullPointerException e){
                window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            }

            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.getAttributes().windowAnimations = R.style.FadeInOutDialog;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
