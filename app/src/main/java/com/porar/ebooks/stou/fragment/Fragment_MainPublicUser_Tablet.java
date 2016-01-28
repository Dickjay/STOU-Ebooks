package com.porar.ebooks.stou.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_SearchList_Ebook;
import com.porar.ebook.control.DialogLogin_Master_STOU;
import com.porar.ebook.control.Dialog_SearchEbooks;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.stou.activity.Activity_MainTab;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.io.File;

/**
 * Created by Porar on 10/6/2015.
 */
public class Fragment_MainPublicUser_Tablet extends Fragment {

    TabLayout tabLayout;
    FrameLayout frame_public_user;

    public static boolean onLoadShelf = false;

    AdjustableImageView refresh_imageview, search_imageview, navigation_back_imageview;
    private RelativeLayout rt_dummyscreen;
    private LinearLayout linear_customforTabview;

    RelativeLayout coordinatorLayout;
    private Adapter_SearchList_Ebook adapter_SearchList;
    private boolean onPause;

    public static Fragment newInstance() {
        Fragment_MainPublicUser_Tablet fragment_public_user_ = new Fragment_MainPublicUser_Tablet();
        return fragment_public_user_;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_public_user_tablet, container, false);
        return initInstances(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPublicUserFragment();
    }

    private void showToastMessage() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();
    }

    private View initInstances(View view) {
        coordinatorLayout = (RelativeLayout) view.findViewById(R.id.rt_head);
        frame_public_user = (FrameLayout) view.findViewById(R.id.frame_public_user);
        navigation_back_imageview = (AdjustableImageView) view.findViewById(R.id.navigation_imageview);
        refresh_imageview = (AdjustableImageView) view.findViewById(R.id.refresh_imageview);
        search_imageview = (AdjustableImageView) view.findViewById(R.id.search_imageview);
        rt_dummyscreen = (RelativeLayout) view
                .findViewById(R.id.ebook_rt_dummyscreen);
        linear_customforTabview = (LinearLayout) view
                .findViewById(R.id.ebook_linear_fortabview);
        navigation_back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        refresh_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Shared_Object.checkInternetConnection(getActivity())) {
                    showToastMessage();
                } else {
                    addPublicUserFragment();
                }
            }
        });


        search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_SearchEbooks(getActivity(), R.style.PauseDialogAnimation, Fragment_MainPublicUser_Tablet.this).show();
            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabs_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.tab_public_user)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.tab_bechalor_degree)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.tab_master_degree)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.tab_intro)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.tab_contact)));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (!Shared_Object.checkInternetConnection(getActivity())) {
                            showToastMessage();
                        } else {
                            addPublicUserFragment();
                        }
                        break;
                    case 1:
                        callFragmentBachelorDegree();

                        break;
                    case 2:
                        callFragmentMasterDegree();
                        break;
                    case 3:
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out);
                        fragmentTransaction.add(R.id.frame_public_user, Fragment_HowTo_WebView.newInstance());
                        fragmentTransaction.commit();

                        break;
                    case 4:
                        // go to fragment contact
                        Activity_MainTab.viewPager.setCurrentItem(2, true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    public void addPublicUserFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out);
        fragmentTransaction.add(R.id.frame_public_user, Fragment_BookList_PublicUser_Tablet.newInstance());
        fragmentTransaction.commit();
    }

    public void callFragmentBachelorDegree() {
        if (!Shared_Object.checkInternetConnection(getActivity())) {
            showToastMessage();
        } else {
            if (Shared_Object.getCustomerDetail.getCID() > 0) {

                // StaticUtils.isAMasterDegree == isMasterDegree
                if (Shared_Object.getCustomerDetail.getBechalorDegree() <= 0) {
                    showDialogBechalorDegree();
                } else {
                    Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                    Fragment_Choice_Mode.addFragment(
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,
                                            R.anim.fade_out,
                                            R.anim.fade_in,
                                            R.anim.fade_out),
                            Fragment_SeeAll_BookList_BachelorDegree_Tablet.newInstance(), true,
                            FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
            } else {

                DialogLogin_Master_STOU dialog_login_master_stou = new DialogLogin_Master_STOU(
                        getActivity(), R.style.PauseDialogAnimation,
                        Fragment_MainPublicUser_Tablet.this);
                dialog_login_master_stou.show();
            }
        }
    }

    public void callFragmentMasterDegree() {
        if (!Shared_Object.checkInternetConnection(getActivity())) {
            showToastMessage();
        } else {
            if (Shared_Object.getCustomerDetail.getCID() > 0) {

                // StaticUtils.isAMasterDegree == isMasterDegree
                if (Shared_Object.getCustomerDetail.getMasterDegree() <= 0) {
                    showDialogMaserDegree();
                } else {
                    Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
                    Fragment_Choice_Mode.addFragment(
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,
                                            R.anim.fade_out,
                                            R.anim.fade_in,
                                            R.anim.fade_out),
                            Fragment_SeeAll_BookList_MasterDegree_Tablet.newInstance(), true,
                            FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                }
            } else {
                DialogLogin_Master_STOU dialog_login_master_stou = new DialogLogin_Master_STOU(
                        getActivity(), R.style.PauseDialogAnimation,
                        Fragment_MainPublicUser_Tablet.this);
                dialog_login_master_stou.show();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                        Fragment_MainPublicUser_Tablet.onLoadShelf = true;
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
//                                Fragment_MainPublicUser_Tablet.this);
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
                        Fragment_MainPublicUser_Tablet.onLoadShelf = true;

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
//                                Fragment_MainPublicUser_Tablet.this);
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
                Fragment_Shelf_Tablet.fragment_stou_shelf_tablet.onResume();
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
}
