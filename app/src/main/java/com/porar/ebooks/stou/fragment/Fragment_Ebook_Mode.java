package com.porar.ebooks.stou.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.AdapterGalleryBanner;
import com.porar.ebook.control.DialogLogin_Master_STOU;
import com.porar.ebook.control.DialogLogin_Show;
import com.porar.ebook.control.Dialog_Show_Banner;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Banner;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;

import java.io.File;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PListObject;

public class Fragment_Ebook_Mode extends Fragment {

    // Initial View
    private RelativeLayout relative_banner, relative_display_title;
    private TextView textview_welcome;
    @SuppressWarnings("deprecation")
    private Gallery banner_gallery;
    private ImageView ebook_imageView_previous, ebook_imageView_next;
    private ImageView button_normal, button_master, ebook_icon_setting,
            ebook_image_psearch;

    // Handler Banner
    private ArrayList<Model_Banner> arrayBanners_banner;
    private final Handler handler = new Handler();
    private final int scrollDelay = 5000;
    private int pageGallery = 0;

    // Check Login
    private DialogLogin_Master_STOU dialogLogin_Show;
    private boolean isMasterDegree = false;
    LinearLayout linear_customforTabview;
    RelativeLayout rt_dummyscreen, rt_head;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        RegisterFacebook.InitFacebookSDK(getActivity());
    }

    public static Fragment_Ebook_Mode newInstance() {
        Fragment_Ebook_Mode fragment = new Fragment_Ebook_Mode();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View isView = inflater.inflate(R.layout.activity_ebook_mode, container,
                false);
        initialView(isView);

        return isView;
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @SuppressWarnings("deprecation")
    private void initialView(View isView) {

        if (AppMain.isphone) {

            button_normal = (ImageView) isView.findViewById(R.id.button_normal);
            button_master = (ImageView) isView.findViewById(R.id.button_master);
            setOnClick();
        } else {
            relative_banner = (RelativeLayout) isView
                    .findViewById(R.id.relative_banner);
            banner_gallery = (Gallery) isView.findViewById(R.id.banner_gallery);
            ebook_imageView_previous = (ImageView) isView
                    .findViewById(R.id.ebook_imageView_previous);
            ebook_imageView_next = (ImageView) isView
                    .findViewById(R.id.ebook_imageView_next);

            button_normal = (ImageView) isView.findViewById(R.id.button_normal);
            button_master = (ImageView) isView.findViewById(R.id.button_master);

            setOnClick();
            if (AppMain.pList_news2 != null) {

                saveBanner();

            }
        }

    }

    private void setOnClick() {
        if (AppMain.isphone) {


            // Button Normal
            button_normal.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isMasterDegree = false;
                    if (Shared_Object.getCustomerDetail.getCID() > 0) {
                        moveToFragment();

                    } else {
                        DialogLogin_Show dialogLogin_ShowRegis = new DialogLogin_Show(
                                getActivity(), R.style.PauseDialogAnimation,
                                Fragment_Ebook_Mode.this);
                        dialogLogin_ShowRegis.show();
                    }

                }
            });
            // Button Maser Degree
            button_master.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isMasterDegree = true;
                    if (Shared_Object.getCustomerDetail.getCID() > 0) {

                        // StaticUtils.isAMasterDegree == isMasterDegree
                        if (Shared_Object.getCustomerDetail.getMasterDegree() <= 0) {
                            showDialogMaser();
                        } else {
                            moveToFragment();
                        }
                    } else {
                        DialogLogin_Master_STOU dialogLogin_STOU = new DialogLogin_Master_STOU(
                                getActivity(), R.style.PauseDialogAnimation,
                                Fragment_Ebook_Mode.this);
                        dialogLogin_STOU.show();
                    }

                }
            });
        } else {

            // Previous Banner
            ebook_imageView_previous.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View arg0) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay()
                            .getMetrics(displayMetrics);
                    int width = (displayMetrics.widthPixels);
                    banner_gallery.onFling(null, null, (float) 2.5 * width, 0);
                }
            });
            // Next Banner
            ebook_imageView_next.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay()
                            .getMetrics(displayMetrics);
                    int width = (displayMetrics.widthPixels);
                    if ((pageGallery) < banner_gallery.getCount()) {
                        banner_gallery.onFling(null, null,
                                (float) -2.5 * width, 0);
                    }
                }
            });
            // Button Normal
            button_normal.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isMasterDegree = false;
                    if (Shared_Object.getCustomerDetail.getCID() > 0) {
                        moveToFragment();

                    } else {
                        DialogLogin_Show dialogLogin_ShowRegis = new DialogLogin_Show(
                                getActivity(), R.style.PauseDialogAnimation,
                                Fragment_Ebook_Mode.this);
                        dialogLogin_ShowRegis.show();
                    }

                }
            });
            // Button Maser Degree
            button_master.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isMasterDegree = true;
                    if (Shared_Object.getCustomerDetail.getCID() > 0) {

                        // StaticUtils.isAMasterDegree == isMasterDegree
                        if (Shared_Object.getCustomerDetail.getMasterDegree() <= 0) {
                            showDialogMaser();
                        } else {
                            moveToFragment();
                        }
                    } else {
                        DialogLogin_Master_STOU dialogLogin_STOU = new DialogLogin_Master_STOU(
                                getActivity(), R.style.PauseDialogAnimation,
                                Fragment_Ebook_Mode.this);
                        dialogLogin_STOU.show();
                    }

                }
            });
        }

    }

    private void moveToFragment() {
        if (isMasterDegree) {
            StaticUtils.isAMasterDegree = true;
            StaticUtils.pageID = 2;
            StaticUtils.phonePage = 2;
            StaticUtils.shelfID = 2;
            StaticUtils.phoneValue = 2;
            Fragment_Choice_Mode.addFragment(
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.fade_out),
                    new Fragment_Ebook(), true,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            isMasterDegree = false;
        } else {
            StaticUtils.isAMasterDegree = false;
            StaticUtils.pageID = 1;
            StaticUtils.phonePage = 1;
            StaticUtils.phoneValue = 1;
            StaticUtils.shelfID = 1;
            Fragment_Choice_Mode.addFragment(
                    getFragmentManager().beginTransaction(),
                    new Fragment_Ebook(), true,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            isMasterDegree = false;

        }

    }

    private void showDialogMaser() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .create();
        alertDialog.setTitle(AppMain.getTag());
        alertDialog
                .setMessage(getResources().getString(R.string.alert_message_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.logout_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // clearCacheFacebookLogin();
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
                        DialogLogin_Master_STOU dialogLogin_STOU = new DialogLogin_Master_STOU(
                                getActivity(), R.style.PauseDialogAnimation,
                                Fragment_Ebook_Mode.this);
                        dialogLogin_STOU.show();

                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.logout_cancel),
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
            }
        });
        apiResultString.execute(registerURL);

    }

    private void clearCacheFacebookLogin() {
        RegisterFacebook.FacebookLogout();
        StaticUtils.Login = 0;
        StaticUtils.isAMasterDegree = false;
        StaticUtils.isBechalorDegree = false;
        StaticUtils.shelfID = 0;
        StaticUtils.pageID = 0;
        StaticUtils.phonePage = 0;
        StaticUtils.phoneValue = 0;
    }


    public void saveBanner() {
        arrayBanners_banner = new ArrayList<Model_Banner>();
        for (PListObject each : (Array) AppMain.pList_news2.getRootElement()) {
            arrayBanners_banner.add(new Model_Banner(each));
        }
        Log.e("MASTER", "MASTER1\t" + arrayBanners_banner.size());
        setBannerGallery();
    }

    private void setBannerGallery() {
        try {

            banner_gallery.removeAllViewsInLayout();
            banner_gallery.setAdapter(new AdapterGalleryBanner(getActivity(),
                    arrayBanners_banner));
            banner_gallery
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View view, int position, long arg3) {

                            handler.removeCallbacks(runnableNext);
                            handler.removeCallbacks(runnablePrevious);
                            pageGallery = (position + 1);
                            if (pageGallery == banner_gallery.getCount()) {
                                handler.postDelayed(runnablePrevious,
                                        scrollDelay);
                            } else {
                                handler.postDelayed(runnableNext, scrollDelay);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

            banner_gallery.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    String bannerPosition = arrayBanners_banner.get(position)
                            .getNID();
                    new Dialog_Show_Banner(getActivity(), bannerPosition) {

                    };
                }
            });

            banner_gallery.refreshDrawableState();
        } catch (NullPointerException e) {
            // load again
            Toast.makeText(getActivity(), "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
        }

    }

    Runnable runnableNext = new Runnable() {

        @SuppressWarnings("deprecation")
        @Override
        public void run() {

            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();

                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(displayMetrics);
                int width = (displayMetrics.widthPixels);

                banner_gallery.onFling(null, null, (float) -2.5 * width, 0);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    };
    Runnable runnablePrevious = new Runnable() {

        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            banner_gallery.onFling(null, null,
                    (1000 * banner_gallery.getCount()), 0);
        }
    };

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!AppMain.isphone) {
            if (AppMain.pList_news2 != null) {

                saveBanner();

            }
        }

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

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        RegisterFacebook.unRegisterFacebook(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
    }
}
