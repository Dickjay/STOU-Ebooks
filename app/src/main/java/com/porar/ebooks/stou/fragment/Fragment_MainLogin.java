package com.porar.ebooks.stou.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.porar.ebook.control.DialogLogin_Master_STOU;
import com.porar.ebook.control.Dialog_ShowCase_View;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.io.File;

/**
 * Created by Porar on 10/2/2015.
 */
public class Fragment_MainLogin extends Fragment {

    private AdjustableImageView login_master_degree_imageview, login_bachelor_degree_imageview, login_public_user_imageview;

    // Check Login
    private DialogLogin_Master_STOU dialog_login_master_stou;
    private boolean isMasterDegree = false;
    private boolean isBechalorDegree = false;
    private static boolean isShow = false;

    public static Fragment newInstance() {
        Fragment_MainLogin fragment_mainLogin = new Fragment_MainLogin();
        return fragment_mainLogin;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stou_main_login, container, false);
        initToolbar(view);
        login_public_user_imageview = (AdjustableImageView) view.findViewById(R.id.login_public_user_imageview);
        login_bachelor_degree_imageview = (AdjustableImageView) view.findViewById(R.id.login_bachelor_degree_imageview);
        login_master_degree_imageview = (AdjustableImageView) view.findViewById(R.id.login_master_degree_imageview);

        login_public_user_imageview.setOnClickListener(public_user_listener);
        login_bachelor_degree_imageview.setOnClickListener(bachelor_degree_listener);
        login_master_degree_imageview.setOnClickListener(master_degree_listener);
        return view;
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void showToastMessage() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener public_user_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
            } else {
                isMasterDegree = false;
                isBechalorDegree = false;
//                if (!AppMain.isphone) {
//                    Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
//                } else {
//                    Fragment_Shelf_Phone.fragment_shelf_phone.onResume();
//                }

                moveToFragment();
            }

        }
    };
    private View.OnClickListener bachelor_degree_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
            } else {
                if (Shared_Object.getCustomerDetail.getCID() > 0) {

                    // StaticUtils.isAMasterDegree == isMasterDegree
                    if (Shared_Object.getCustomerDetail.getBechalorDegree() <= 0) {
                        showDialogBechalorDegree();
                    } else {
                        isMasterDegree = false;
                        isBechalorDegree = true;
                        if (!AppMain.isphone) {
                            Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                        } else {
                            Fragment_Shelf_Phone.fragment_shelf_phone.onResume();
                        }
                        moveToFragment();
                    }
                } else {
                    dialog_login_master_stou = new DialogLogin_Master_STOU(
                            getActivity(), R.style.PauseDialogAnimation,
                            Fragment_MainLogin.this);
                    dialog_login_master_stou.show();
                }
            }
        }
    };
    private View.OnClickListener master_degree_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
            } else {
                if (Shared_Object.getCustomerDetail.getCID() > 0) {

                    // StaticUtils.isAMasterDegree == isMasterDegree
                    if (Shared_Object.getCustomerDetail.getMasterDegree() <= 0) {
                        showDialogMaserDegree();
                    } else {
                        isMasterDegree = true;
                        isBechalorDegree = false;
                        if (!AppMain.isphone) {
                            Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                        } else {
                            Fragment_Shelf_Phone.fragment_shelf_phone.onResume();
                        }
                        moveToFragment();
                    }
                } else {
                    dialog_login_master_stou = new DialogLogin_Master_STOU(
                            getActivity(), R.style.PauseDialogAnimation,
                            Fragment_MainLogin.this);
                    dialog_login_master_stou.show();
                }
            }
        }
    };


    private void moveToFragment() {
        if (isBechalorDegree) {// student bachalor degree
            StaticUtils.isBechalorDegree = true;
            StaticUtils.isAMasterDegree = false;

            StaticUtils.pageID = 3;
            StaticUtils.phonePage = 3;
            StaticUtils.phoneValue = 3;
            StaticUtils.shelfID = 3;

            if (AppMain.isphone) {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_SeeAll_BookList_BachelorDegree_Phone.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            } else {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_SeeAll_BookList_BachelorDegree_Tablet.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
            isMasterDegree = false;
            isBechalorDegree = false;
        } else if (isMasterDegree) { // student master degree
            StaticUtils.isAMasterDegree = true;
            StaticUtils.isBechalorDegree = false;

            StaticUtils.pageID = 2;
            StaticUtils.phonePage = 2;
            StaticUtils.shelfID = 2;
            StaticUtils.phoneValue = 2;
            if (AppMain.isphone) {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_SeeAll_BookList_MasterDegree_Phone.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            } else {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_SeeAll_BookList_MasterDegree_Tablet.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }


            isMasterDegree = false;
            isBechalorDegree = false;
        } else { // public user
            StaticUtils.isAMasterDegree = false;
            StaticUtils.isBechalorDegree = false;

            StaticUtils.pageID = 1;
            StaticUtils.phonePage = 1;
            StaticUtils.phoneValue = 1;
            StaticUtils.shelfID = 1;
            if (AppMain.isphone) {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_MainPublicUser_Phone.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            } else {
                Fragment_Choice_Mode.addFragment(
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out,
                                        R.anim.fade_in,
                                        R.anim.fade_out),
                        Fragment_MainPublicUser_Tablet.newInstance(), true,
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }


            isMasterDegree = false;
            isBechalorDegree = false;

        }

    }


    private void showDialogMaserDegree() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        alertDialog.setTitle(getResources().getString(R.string.title_logout));
        alertDialog
                .setMessage(getResources().getString(R.string.message_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.logout_confirm),
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
                        File customerFile = new File(getActivity()
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
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.message_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        alertDialog.show();

    }

    private void showDialogBechalorDegree() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        alertDialog.setTitle(getResources().getString(R.string.title_logout));
        alertDialog
                .setMessage(getResources().getString(R.string.message_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.logout_confirm),
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
                        File customerFile = new File(getActivity()
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
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.message_confirm),
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
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG)
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isShow){
            isShow=true;
            new Dialog_ShowCase_View(getActivity(), R.style.FadeInOutDialog, Fragment_MainLogin.this);

        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        AppMain.isShelf = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
