package com.porar.ebooks.stou.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_RecyclerView_BookShelf;
import com.porar.ebook.control.DialogLogin;
import com.porar.ebook.control.Dialog_Search_Shelf;
import com.porar.ebook.control.Dialog_ShowLongClick;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.control.addTextWatcher;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.Page_Image_Slide;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/1/2015.
 */
public class Fragment_Shelf_Tablet extends Fragment {

    public static boolean searchEbooks;
    private Toolbar toolbar;

    private RelativeLayout shelf_rt_profilecustomer, shelf_rt_head;

    private LinearLayout shelf_linear_head_refresh;
    private AdjustableImageView shelf_image_refresh;

    private LinearLayout shelf_linear_head_search;
    private AdjustableImageView shelf_image_search;

    private LinearLayout shelf_linear_head_setting;
    private AdjustableImageView shelf_image_setting;

    private Button shelf_btn_new, shelf_btn_favorite, shelf_btn_trash;

    private FrameLayout frameLayout;

    public static boolean isDragAndDrop = false;

    private boolean isAllBooks = disableStated();
    private boolean isFavoriteBoook = disableStated();
    public static boolean isThrashBook = false;

    private boolean isClickedEvent = false;

    public static boolean isDeletedThrashBook = false;

    public TextView editTextView, signOutTextView;
    public static Fragment_Shelf_Tablet fragment_stou_shelf_tablet;

    public ArrayList<Model_EBook_Shelf_List> arrayListoffline;
    public EditText searchBookShelf = null;
    public addTextWatcher textWatcher;


    public Dialog_ShowLongClick dialog_ShowLongClick;
    private ProgressDialog progressDialog;
    public static Handler handler = new Handler();


    public static Fragment_Shelf_Tablet newInstance() {
        fragment_stou_shelf_tablet = new Fragment_Shelf_Tablet();
        return fragment_stou_shelf_tablet;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stou_shelf_tablet, container, disableStated());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initView(view);
    }


    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void initView(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_stou_shelf_phone);

        shelf_rt_profilecustomer = (RelativeLayout) view.findViewById(R.id.shelf_rt_profilecustomer);
        shelf_rt_head = (RelativeLayout) view.findViewById(R.id.shelf_rt_head);

        shelf_linear_head_refresh = (LinearLayout) view.findViewById(R.id.shelf_linear_head_refresh);
        shelf_image_refresh = (AdjustableImageView) view.findViewById(R.id.shelf_image_refresh);

        shelf_linear_head_search = (LinearLayout) view.findViewById(R.id.shelf_linear_head_search);
        shelf_image_search = (AdjustableImageView) view.findViewById(R.id.shelf_image_search);

        shelf_linear_head_setting = (LinearLayout) view.findViewById(R.id.shelf_linear_head_setting);
        shelf_image_setting = (AdjustableImageView) view.findViewById(R.id.shelf_image_setting);

        shelf_btn_new = (Button) view.findViewById(R.id.shelf_btn_new);
        shelf_btn_new.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));

        shelf_btn_favorite = (Button) view.findViewById(R.id.shelf_btn_favorite);
        shelf_btn_favorite.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));

        shelf_btn_trash = (Button) view.findViewById(R.id.shelf_btn_trash);
        shelf_btn_trash.setTypeface(StaticUtils.getTypeface(getActivity(),
                StaticUtils.Font.DB_HelvethaicaMon_X));


        shelf_linear_head_refresh.setOnClickListener(refreshListener);
        shelf_linear_head_search.setOnClickListener(searchistener);
        shelf_btn_new.setOnClickListener(allBookListener);
        shelf_btn_favorite.setOnClickListener(favoriteBookListener);
        shelf_btn_trash.setOnClickListener(thrashBookListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addProfileHeader(shelf_rt_profilecustomer);
        setBackgroundHeader();
        conigTextWatcher();
    }


    public void loadItem() {
        if (Shared_Object.isOfflineMode) {
            // get ebooks from devices if is offline mode
            arrayListoffline = Class_Manage.getEbooksShelfInOffLineMode(getActivity());
            if (arrayListoffline != null && arrayListoffline.size() > 0) {
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance(arrayListoffline));
                enabletButton(disableStated(), enableStated(), enableStated());
            }
        } else {
            if (isUserLogin()) {
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance());
                enabletButton(disableStated(), enableStated(), enableStated());
            } else {
                showDialogLogin();
            }
        }
        if (Fragment_MainPublicUser_Tablet.onLoadShelf) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addProfileHeader(shelf_rt_profilecustomer);
                }
            });

            if (!Shared_Object.isOfflineMode) {
                if (isUserLogin()) {
                    isAllBooks = enableStated();
                    replaceFragmet(Fragment_AllEbooks.newInstance());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enabletButton(disableStated(), enableStated(), enableStated());
                        }
                    });
                } else {
                    replaceFragmet(Fragment_Empty.newInstance());
                    showDialogLogin();
                }
                setBackgroundHeader();
            } else {
            }
            Fragment_MainPublicUser_Tablet.onLoadShelf = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void addProfileHeader(RelativeLayout shelf_rt_profilecustomer) {
        removeProfile(shelf_rt_profilecustomer);
        if (isUserLogin()) {
            isUserEbooks(shelf_rt_profilecustomer);
        } else {
            isNotUser(shelf_rt_profilecustomer);
        }
    }

    private void removeProfile(RelativeLayout shelf_rt_profilecustomer) {
        shelf_rt_profilecustomer.removeAllViews();
    }

    private void isNotUser(final RelativeLayout shelf_rt_profilecustomer) {
        final LinearLayout profile_notlogin = (LinearLayout) LayoutInflater.from(
                getActivity()).inflate(R.layout.layout_profile_notlogin, null);
        profile_notlogin.animate().alpha(1.0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profile_notlogin
                                .setVisibility(View.VISIBLE);
                    }
                });


        if (profile_notlogin != null) {
            shelf_rt_profilecustomer.addView(profile_notlogin);

            ImageView login_imageview = (ImageView) profile_notlogin
                    .findViewById(R.id.rt_profile_imageView1);
            TextView tv1 = (TextView) profile_notlogin
                    .findViewById(R.id.rt_profile_textView1);
            TextView tv2 = (TextView) profile_notlogin
                    .findViewById(R.id.rt_profile_textView2);

            tv1.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            tv2.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            login_imageview.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showDialogLogin();
                }
            });
        }
    }

    private void setProfileImage(String urlProfileImage, ImageView imageView) {
        try {
            if (Shared_Object.getCustomerDetail.getCID() > 0) {
                ImageDownloader_forCache imageDownloader_forCache = new ImageDownloader_forCache();
                imageDownloader_forCache.download(urlProfileImage, imageView);
            } else {

                changeIconProfile(imageView, R.drawable.ic_action_person);
            }
        } catch (NullPointerException e) {
            changeIconProfile(imageView, R.drawable.ic_action_person);
        }
    }

    private void changeIconProfile(final ImageView imageView, final int drawable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(drawable);

            }
        });
    }

    private void isUserEbooks(RelativeLayout shelf_rt_profilecustomer) {
        final View profile_customer = LayoutInflater.from(getActivity()).inflate(
                R.layout.layout_profile_customer, null);

        profile_customer.animate().alpha(1.0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        profile_customer
                                .setVisibility(View.VISIBLE);
                    }
                });

        if (profile_customer != null) {
            shelf_rt_profilecustomer.addView(profile_customer);
            ImageView img_cover = (ImageView) profile_customer
                    .findViewById(R.id.shelf_imageview_cover);

            TextView txt_name = (TextView) profile_customer
                    .findViewById(R.id.name_textView);
            TextView txt_email = (TextView) profile_customer
                    .findViewById(R.id.mail_textView);
            TextView txt_address = (TextView) profile_customer
                    .findViewById(R.id.address_textView);
            TextView txt_phone = (TextView) profile_customer
                    .findViewById(R.id.tel_textView);

            TextView signInTextView = (TextView) profile_customer
                    .findViewById(R.id.sign_in_textView);
            signOutTextView = (TextView) profile_customer
                    .findViewById(R.id.sign_out_textView);
            editTextView = (TextView) profile_customer
                    .findViewById(R.id.edit_textView);

            // stou
            signInTextView.setVisibility(View.INVISIBLE);
            String userPicURL = Shared_Object.getCustomerDetail.getPictureUrl();

            if (userPicURL.length() <= 0)
                userPicURL = AppMain.DEFAULT_USER_PIC_URL;

            setProfileImage(userPicURL, img_cover);
            // end

            Log.e("snuxker",
                    "Shared_Object.getCustomerDetail.getPictureUrl() : "
                            + Shared_Object.getCustomerDetail
                            .getPictureUrl());


            txt_name.setText(""
                    + Shared_Object.getCustomerDetail.getFirstName() + " "
                    + Shared_Object.getCustomerDetail.getLastName());
            txt_email.setText(""
                    + Shared_Object.getCustomerDetail.getEmail());
            txt_address.setText(""
                    + Shared_Object.getCustomerDetail.getAddress());

            String tel = Shared_Object.getCustomerDetail.getTel();

            if (tel.length() <= 0) {
                txt_phone.setVisibility(View.GONE);
            } else {
                txt_phone.setText(getString(R.string.tel) + " "
                        + Shared_Object.getCustomerDetail.getTel());
            }

            txt_name.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_Helvethaica_X_Med));
            txt_email.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_Helvethaica_X_Med));
            txt_address.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            txt_phone.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));

            signInTextView.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            signOutTextView.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));
            editTextView.setTypeface(StaticUtils.getTypeface(getActivity(),
                    StaticUtils.Font.DB_HelvethaicaMon_X));

            // stou
            signInTextView.setEnabled(disableStated());
            signOutTextView.setEnabled(enableStated());
            editTextView.setEnabled(enableStated());

            signOutTextView.setOnClickListener(signoutLstener);
            editTextView.setOnClickListener(editListener);
        }
    }


    private View.OnClickListener editListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }

            if (Shared_Object.isOfflineMode) {
                showToastMessage();
                return;
            }
            // Restore EBooks from thrash mode
            if (isDeletedThrashBook) {
                isClickedEvent = true;
                isDragAndDrop = disableStated();
                isThrashBook = disableStated();
                isDeletedThrashBook = disableStated();
                isAllBooks = enableStated();
                replaceFragmet(Fragment_AllEbooks.newInstance());
                editTextView.setText(getResources().getString(R.string.edit));
                Fragment_FavoriteEbooks.eneableDragDrop(isDragAndDrop);
                enabletButton(disableStated(), enableStated(), enableStated());
            } else {
                // Save EBooks from drag and drop && remove Ebooks to thrash mode
                if (isDragAndDrop) {
                    isClickedEvent = true;
                    isDragAndDrop = disableStated();
                    if (isAllBooks) {
                        SendRequestSortEbookIDTask sendRequestSortEbookIDTask = new SendRequestSortEbookIDTask(Fragment_AllEbooks.array_deleted_ebooksID,
                                Fragment_AllEbooks.adapter_recyclerView_bookShelf, Fragment_AllEbooks.array_sort_ebooksID, Fragment_AllEbooks.arraylist_shelf_ebooksID, true);
                        sendRequestSortEbookIDTask.execute();


                    } else if (isFavoriteBoook) {
                        SendRequestSortEbookIDTask sendRequestSortEbookIDTask = new SendRequestSortEbookIDTask(Fragment_FavoriteEbooks.array_deleted_ebooksID,
                                Fragment_FavoriteEbooks.adapter_recyclerView_bookShelf, Fragment_FavoriteEbooks.array_sort_ebooksID, Fragment_FavoriteEbooks.arraylist_shelf_ebooksID, false);
                        sendRequestSortEbookIDTask.execute();

                    }
                    // change icon edit_imageview to R.drawable.ic_action_delete
                    editTextView.setText(getResources().getString(R.string.edit));
                    enabeButtonClick();

                } else {
                    // enable drag and drop && remove Ebooks to thrash mode
                    if (isUserLogin()) {
                        isClickedEvent = true;
                        isDragAndDrop = enableStated();
                        if (!isThrashBook) {
                            if (isAllBooks) {
                                enabletButton(disableStated(), enableStated(), enableStated());
                                replaceFragmet(Fragment_AllEbooks.newInstance());
                            }
                            if (isFavoriteBoook) {
                                enabletButton(enableStated(), disableStated(), enableStated());
                                replaceFragmet(Fragment_FavoriteEbooks.newInstance());
                            }
                        }
                        // change icon edit_imageview to R.drawable.ic_action_save
                        editTextView.setText(getResources().getString(R.string.save));
                        disableButtonClick();
                    } else {
                        showDialogLogin();
                    }
                }
            }


        }

        private void disableButtonClick() {

            shelf_linear_head_setting.setOnClickListener(null);
            shelf_linear_head_refresh.setOnClickListener(null);
            shelf_linear_head_search.setOnClickListener(null);

            signOutTextView.setOnClickListener(null);


            shelf_btn_new.setOnClickListener(null);
            shelf_btn_favorite.setOnClickListener(null);
            shelf_btn_trash.setOnClickListener(null);

        }

        private void enabeButtonClick() {

            shelf_linear_head_setting.setOnClickListener(null);
            shelf_linear_head_refresh.setOnClickListener(refreshListener);
            shelf_linear_head_search.setOnClickListener(searchistener);

            signOutTextView.setOnClickListener(signoutLstener);

            shelf_btn_new.setOnClickListener(allBookListener);
            shelf_btn_favorite.setOnClickListener(favoriteBookListener);
            shelf_btn_trash.setOnClickListener(thrashBookListener);

        }
    };
    private View.OnClickListener signoutLstener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
                return;
            } else {
                removeAccount();
                Log.i("", "facebook is login2");
                Shared_Object.getCustomerDetail = new Model_Customer_Detail(
                        null);
                File customerFile = new File(
                        getActivity().getFilesDir(), "/"
                        + "customer_detail.porar");
                if (customerFile.exists()) {

                    customerFile.delete();
                    customerFile = null;

                    AppMain.pList_default_ebookshelf = null;
                    addProfileHeader(shelf_rt_profilecustomer);
                    replaceFragmet(Fragment_Empty.newInstance());
                    StaticUtils.arrShilf.clear();
                    StaticUtils.Login = 0;
                    StaticUtils.isBechalorDegree = disableStated();
                    StaticUtils.isAMasterDegree = disableStated();
                    StaticUtils.shelfID = 0;
                    StaticUtils.pageID = 0;
                    StaticUtils.phonePage = 0;
                    StaticUtils.phoneValue = 0;
                    // Log.i("", "facebook is login3");
                    // removeAccount();
                    Shared_Object.getCustomerDetail.setMasterDegree(0);
                    Shared_Object.getCustomerDetail.setBechalorDegree(0);
                    if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                        Log.i("", "facebook is login");
                        StaticUtils.setFacebooklogin(enableStated());
                    } else {
                        Log.i("", "facebook not login");
                        StaticUtils.setFacebooklogin(enableStated());
                    }
                }
            }


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

                    // reset to fragment empty mode
                    if (isAllBooks) {
                        isAllBooks = disableStated();
                    }
                    if (isFavoriteBoook) {
                        isFavoriteBoook = disableStated();
                    }
                    if (isThrashBook) {
                        isDeletedThrashBook = disableStated();
                        isThrashBook = disableStated();
                        editTextView.setText(getResources().getString(R.string.edit));
                    }
                    addFragmentEmpty();
                    setBackgroundHeader();
                }
            });
            apiResultString.execute(registerURL);

        }

        private void addFragmentEmpty() {

            enabletButton(enableStated(), enableStated(), enableStated());
            replaceFragmet(Fragment_Empty.newInstance());
        }
    };

    private View.OnClickListener searchistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isThrashBook) {
                return;
            }
            new Dialog_Search_Shelf(getActivity(), R.style.PauseDialogAnimation, Fragment_Shelf_Tablet.this) {
                @Override
                public void onFinishInflateSearchBookshelf(EditText searchEditText) {
                    searchBookShelf = searchEditText;
                    searchBookShelf.addTextChangedListener(textWatcher);


                }
            }.show();
        }
    };

    private View.OnClickListener refreshListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }

            if (!Shared_Object.checkInternetConnection(getActivity())) {
                showToastMessage();
                return;
            } else {
                // if user login to stou-ebooks
                if (isUserLogin()) {
                    isClickedEvent = true;
                    // reset mode to AllEbooks mode
                    if (isFavoriteBoook) {
                        isFavoriteBoook = disableStated();
                    }
                    if (isThrashBook) {
                        isThrashBook = disableStated();
                    }
                    if (isDeletedThrashBook) {
                        isDeletedThrashBook = disableStated();
                    }
                    if (isDragAndDrop) {
                        isDragAndDrop = disableStated();
                    }

                    isAllBooks = enableStated();
                    replaceFragmet(Fragment_AllEbooks.newInstance());
                    editTextView.setText(getResources().getString(R.string.edit));
                    enabletButton(disableStated(), enableStated(), enableStated());
                } else {
                    // show dialog login and register to use stou-ebooks app
                    showDialogLogin();
                }
            }

        }
    };
    private View.OnClickListener allBookListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }
            if (Shared_Object.isOfflineMode) {
                showToastMessage();
                return;
            } else {
                // is user select AllEbooks mode
                if (isAllBooks) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = true;
                        isDeletedThrashBook = disableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = enableStated();
                        isFavoriteBoook = disableStated();
                        isThrashBook = disableStated();
                        replaceFragmet(Fragment_AllEbooks.newInstance());
                        enabletButton(disableStated(), enableStated(), enableStated());
                        editTextView.setText(getResources().getString(R.string.edit));
                    } else {
                        showDialogLogin();
                    }
                }
            }


        }
    };
    private View.OnClickListener favoriteBookListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }
            if (Shared_Object.isOfflineMode) {
                showToastMessage();
                return;
            } else {
                // is user select favoriteEBoook mode
                if (isFavoriteBoook) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = true;
                        isDeletedThrashBook = disableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = disableStated();
                        isFavoriteBoook = enableStated();
                        isThrashBook = disableStated();
                        replaceFragmet(Fragment_FavoriteEbooks.newInstance());
                        editTextView.setText(getResources().getString(R.string.edit));
                        enabletButton(enableStated(), disableStated(), enableStated());
                    } else {
                        showDialogLogin();
                    }
                }
            }

        }
    };
    private View.OnClickListener thrashBookListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isClickedEvent) {
                return;
            }
            if (Shared_Object.isOfflineMode) {
                showToastMessage();
                return;
            } else {
                // is user select ithrashEBook mode
                if (isThrashBook) {
                    return;
                } else {
                    if (isUserLogin()) {
                        isClickedEvent = true;
                        isDeletedThrashBook = enableStated();
                        isDragAndDrop = disableStated();
                        isAllBooks = disableStated();
                        isFavoriteBoook = disableStated();
                        isThrashBook = enableStated();
                        replaceFragmet(Fragment_ThrashEbooks.newInstance());
                        editTextView.setText(getResources().getString(R.string.save));
                        enabletButton(enableStated(), enableStated(), disableStated());
                    } else {
                        showDialogLogin();
                    }
                }
            }
        }
    };

    private boolean isUserLogin() {
        if (Shared_Object.getCustomerDetail.getCID() <= 0) {
            return disableStated();
        } else {
            return enableStated();
        }
    }

    private void showDialogLogin() {
        DialogLogin dialogLogin_show = new DialogLogin(getActivity(), R.style.PauseDialogAnimation, Fragment_Shelf_Tablet.this) {
            @Override
            public void loginComplete() {
                Shared_Object.loadConfigData(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragmet(Fragment_AllEbooks.newInstance());
                        addProfileHeader(shelf_rt_profilecustomer);
                        enabletButton(disableStated(), enableStated(), enableStated());
                        setBackgroundHeader();
                    }
                });

            }
        };
        dialogLogin_show.show();
    }

    private void setBackgroundHeader() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Shared_Object.getCustomerDetail.getBechalorDegree() > 0) {

                } else if (Shared_Object.getCustomerDetail.getMasterDegree() > 0) {
                    shelf_rt_head.setBackgroundColor(getActivity().getResources().getColor(R.color.orange_stou));
                } else {
                    shelf_rt_head.setBackgroundColor(getActivity().getResources().getColor(R.color.green_stou));
                }
            }
        });

    }


    private boolean enableStated() {
        return true;
    }

    private boolean disableStated() {
        return false;
    }

    private void enabletButton(final boolean allBook, final boolean favoriteBook, final boolean thrashBook) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shelf_btn_new.setEnabled(allBook);
                shelf_btn_favorite.setEnabled(favoriteBook);
                shelf_btn_trash.setEnabled(thrashBook);
            }
        });
    }

    private void showToastMessage() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_internet_connection), Toast.LENGTH_LONG).show();
        isClickedEvent = false;
    }

    public void replaceFragmet(Fragment fragment) {
        ReplaceFragmentTask replaceFragmentTask = new ReplaceFragmentTask(fragment);
        replaceFragmentTask.execute();
    }


    class ReplaceFragmentTask extends AsyncTask<Void, Void, Boolean> {
        private Fragment fragment;
        private FragmentTransaction fragmentTransaction;

        ReplaceFragmentTask(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out);
            return enableStated();
        }

        @Override
        protected void onPostExecute(Boolean cancelTask) {
            super.onPostExecute(cancelTask);
            try {
                fragmentTransaction.commit();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                fragmentTransaction.commitAllowingStateLoss();
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    isClickedEvent = disableStated();
                }
            }, 1000);
            cancel(cancelTask);
        }
    }

    private void createProgressDialog() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Saving..",
                true, false);
    }

    private void closeDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    public class SendRequestSortEbookIDTask extends AsyncTask<Void, Void, Boolean> {
        public ArrayList<Integer> array_deleted_ebooksID;
        public Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf;
        public ArrayList<String> array_sort_ebooksID;
        public ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID;
        public boolean isAllEbooks = false;

        SendRequestSortEbookIDTask(ArrayList<Integer> array_deleted_ebooksID, Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf,
                                   ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID, boolean isAllEbooks) {
            this.array_deleted_ebooksID = array_deleted_ebooksID;
            this.adapter_recyclerView_bookShelf = adapter_recyclerView_bookShelf;
            this.array_sort_ebooksID = array_sort_ebooksID;
            this.arraylist_shelf_ebooksID = arraylist_shelf_ebooksID;
            this.isAllEbooks = isAllEbooks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (builderEbookID(adapter_recyclerView_bookShelf, array_sort_ebooksID, arraylist_shelf_ebooksID)) {
                    return true;
                } else {
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (isAllBooks) {
                SendRequestDeletedEbookIDTask sendRequestDeletedEbookIDTask = new SendRequestDeletedEbookIDTask(array_deleted_ebooksID, isAllEbooks);
                sendRequestDeletedEbookIDTask.execute();
            } else {
                SendRequestDeletedEbookIDTask sendRequestDeletedEbookIDTask = new SendRequestDeletedEbookIDTask(array_deleted_ebooksID, isAllEbooks);
                sendRequestDeletedEbookIDTask.execute();
            }

        }
    }


    private boolean builderEbookID(Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf, ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) throws IOException {
        if (arraylist_shelf_ebooksID != null && arraylist_shelf_ebooksID.size() > 0) {

            StringBuilder stringBuilder = setStringBuilderEBooksID(adapter_recyclerView_bookShelf, array_sort_ebooksID, arraylist_shelf_ebooksID);
            if (stringBuilder != null) {
                sendRequestOkHttp(stringBuilder);
                clearSortEBooksID(array_sort_ebooksID, arraylist_shelf_ebooksID);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private void sendRequestOkHttp(StringBuilder stringBuilder) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("apikey", "f4666c85ab34b5397519ef4b4e471b43")
                .add("cid", Shared_Object.getCustomerDetail.getCID() + "")
                .add("book_list", stringBuilder + "")
                .build();
        Request request = new Request.Builder()
                .url("http://203.150.225.223/stoubookapi/api/save_book_index.php")
                .post(formBody)
                .build();
        client.newCall(request).execute();
    }

    private StringBuilder setStringBuilderEBooksID(Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf, ArrayList<String> array_bid, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) {
        if (adapter_recyclerView_bookShelf != null && adapter_recyclerView_bookShelf.getItemCount() > 0) {

            int allItem = adapter_recyclerView_bookShelf.getItemCount();

            for (int pos = 0; pos < allItem; pos++) {
                String currentItem = arraylist_shelf_ebooksID.get(pos).getBID() + "";
                array_bid.add(currentItem);
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (String value : array_bid) {
                stringBuilder.append(value).append(',');
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            return stringBuilder;
        }
        return null;
    }

    private void clearSortEBooksID(ArrayList<String> array_sort_ebooksID, ArrayList<Model_EBook_Shelf_List> arraylist_shelf_ebooksID) {
        arraylist_shelf_ebooksID.clear();
        array_sort_ebooksID.clear();
    }

    public class SendRequestDeletedEbookIDTask extends AsyncTask<Void, Void, Boolean> {
        public ArrayList<Integer> array_deleted_ebooksID;
        public boolean isAllEbooks = false;

        SendRequestDeletedEbookIDTask(ArrayList<Integer> array_deleted_ebooksID, boolean isAllEbooks) {
            this.array_deleted_ebooksID = array_deleted_ebooksID;
            this.isAllEbooks = isAllEbooks;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (requestDeletedEBookID(array_deleted_ebooksID)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            closeDialog();
            if (isAllBooks) {
                enabletButton(disableStated(), enableStated(), enableStated());
                replaceFragmet(Fragment_AllEbooks.newInstance(progressDialog));
            } else {
                enabletButton(enableStated(), disableStated(), enableStated());
                replaceFragmet(Fragment_FavoriteEbooks.newInstance(progressDialog));
            }
        }
    }

    private boolean requestDeletedEBookID(ArrayList<Integer> array_deleted_ebooksID) {


        if (array_deleted_ebooksID != null && array_deleted_ebooksID.size() > 0) {

            for (final Integer EBooksID : array_deleted_ebooksID) {

                // String url =
                // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                String url = AppMain.DELETE_BOOK_SHELF_URL;
                url += "bid=" + EBooksID;
                url += "&cid="
                        + Shared_Object.getCustomerDetail.getCID();
                url += "&confirm=0";

                OkHttpClient okHttpClient = new OkHttpClient();

                Request.Builder builder = new Request.Builder();
                Request request = builder.url(url).build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override

                    public void onFailure(Request request, IOException e) {
                        updateView("Error - " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                updateView(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                updateView("Error - " + e.getMessage());
                            }
                        } else {
                            updateView("Not Success - code : " + response.code());
                        }
                    }


                    public void updateView(final String strResult) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (strResult.contains("1")) {

                                        Toast.makeText(getActivity(),
                                                "delete to trash",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                } catch (NullPointerException e) {
                                    Toast.makeText(
                                            getActivity(),
                                            "WARNING: An error has ocurred. Delete BOOK-ID "
                                                    + EBooksID + " each",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(),
                                            "WARNING: Please Refresh.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                Class_Manage.deleteEbooksCache(getActivity(), EBooksID);
            }
            clearDeletedEBooksID(array_deleted_ebooksID);
            return true;
        } else {
            return false;
        }
    }

    private void clearDeletedEBooksID(ArrayList<Integer> array_deleted_ebooksID) {
        array_deleted_ebooksID.clear();
    }


    public void conigTextWatcher() {
        textWatcher = new addTextWatcher();
        textWatcher.addInteface(new addTextWatcher.IntefaceAfterTextChange() {

            @Override
            public void AfterTextChange(String strSearch) {
                if (isAllBooks) {
                    filterEbooksName(strSearch, Fragment_AllEbooks.fragment_allEbooks, Fragment_FavoriteEbooks.fragment_favoriteEbooks,
                            Fragment_AllEbooks.adapter_recyclerView_bookShelf, Fragment_AllEbooks.array_ebook_shelf_list_search, Fragment_AllEbooks.mRecyclerView, true, true);
                } else if (isFavoriteBoook) {

                    filterEbooksName(strSearch, Fragment_AllEbooks.fragment_allEbooks, Fragment_FavoriteEbooks.fragment_favoriteEbooks,
                            Fragment_FavoriteEbooks.adapter_recyclerView_bookShelf, Fragment_FavoriteEbooks.array_ebook_shelf_list_search, Fragment_FavoriteEbooks.mRecyclerView, true, false);
                }

            }
        });
    }

    public void filterEbooksName(String strSearch, final Fragment_AllEbooks fragment_allEbooks, final Fragment_FavoriteEbooks fragment_favoriteEbooks, Adapter_RecyclerView_BookShelf adapter_recyclerView_bookShelf, ArrayList<Model_EBook_Shelf_List>
            array_ebook_shelf_list_search, RecyclerView mRecyclerView, boolean isSearch, final boolean isAllEbooks) {
        if (strSearch.equals("")) {
            searchEbooks = isSearch;
            if (isAllEbooks) {
                fragment_allEbooks.onResume();
            } else {
                fragment_favoriteEbooks.onResume();
            }
        } else {
            if (adapter_recyclerView_bookShelf != null) {
                if (array_ebook_shelf_list_search != null) {
                    for (int i = 0; i < array_ebook_shelf_list_search.size(); i++) {
                        if (strSearch.contains(array_ebook_shelf_list_search.get(i).getName())) {
                        } else {
                            array_ebook_shelf_list_search.remove(i);
                        }
                    }
                    adapter_recyclerView_bookShelf = new Adapter_RecyclerView_BookShelf(getActivity(), array_ebook_shelf_list_search, Fragment_Shelf_Tablet.this) {
                        @Override
                        public void OnClickCover(Model_EBook_Shelf_List model) {
                            Intent myIntent = new Intent(getActivity(), Page_Image_Slide.class);
                            myIntent.putExtra("bid", model.getBID());
                            myIntent.putExtra("model", model);
                            myIntent.putExtra("customer", Shared_Object.getCustomerDetail);

                            Fragment_Shelf_Tablet.this.startActivityForResult(myIntent, 2013);
                            getActivity().overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
                        }

                        @Override
                        public void OnLongClickCover(Model_EBook_Shelf_List model) {
                            dialog_ShowLongClick = new Dialog_ShowLongClick(getActivity(),
                                    R.style.PauseDialog, model, getActivity());
                            dialog_ShowLongClick
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {

                                        @Override
                                        public void onCancel(DialogInterface dia) {
                                        }
                                    });

                            LoadDetailDialog detailDialog = new LoadDetailDialog();
                            detailDialog.newInstant(model.getBID());
                            detailDialog.LoadDatadetail();
                        }

                        @Override
                        public void getBookID(ArrayList<Integer> arrayList, int position) {
                            if (isAllEbooks) {
                                fragment_allEbooks.array_deleted_ebooksID = arrayList;
                            } else {
                                fragment_favoriteEbooks.array_deleted_ebooksID = arrayList;
                            }
                        }

                        @Override
                        public void getAllItem(ArrayList<Model_EBook_Shelf_List> m_array_ebook_shelf_list) {
                            if (isAllEbooks) {
                                fragment_allEbooks.array_sort_ebooksID = new ArrayList<String>();
                                fragment_allEbooks.arraylist_shelf_ebooksID = m_array_ebook_shelf_list;
                                fragment_allEbooks.adapter_recyclerView_bookShelf.notifyDataSetChanged();
                            } else {
                                fragment_favoriteEbooks.array_sort_ebooksID = new ArrayList<String>();
                                fragment_favoriteEbooks.arraylist_shelf_ebooksID = m_array_ebook_shelf_list;
                                fragment_favoriteEbooks.adapter_recyclerView_bookShelf.notifyDataSetChanged();
                            }
                        }
                    };
                    mRecyclerView.setAdapter(adapter_recyclerView_bookShelf);
                }
            }
        }
    }

    public class LoadDetailDialog {
        int model_EbookList;
        Ebook_Detail ebook_DetailApi;
        AlertDialog alertDialog;
        Model_Ebooks_Detail ebooks_Detail = null;

        public void newInstant(int bid) {
            model_EbookList = bid;

        }

        public void LoadDatadetail() {

            // DerializeObject
            Model_Ebooks_Detail DSebooks_Detail = Class_Manage
                    .getModel_DetailByNever(getActivity().getApplicationContext(),
                            model_EbookList);
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success" + model_EbookList);
                // intent
                dialog_ShowLongClick.setDetail(DSebooks_Detail, 1);
                dialog_ShowLongClick.show();
            } else {
                ebook_DetailApi = new Ebook_Detail(getActivity(),
                        String.valueOf(model_EbookList));
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
                                    getActivity().getApplicationContext(),
                                    ebooks_Detail, ebooks_Detail.getBID())) {
                                Log.v("OnClickToDetailEbook",
                                        "SerializeObject Success"
                                                + ebooks_Detail.getBID());

                            }

                            // intent

                            dialog_ShowLongClick.setDetail(ebooks_Detail, 0);
                            dialog_ShowLongClick.show();

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

                            alertDialog = new AlertDialog.Builder(getActivity())
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
}


