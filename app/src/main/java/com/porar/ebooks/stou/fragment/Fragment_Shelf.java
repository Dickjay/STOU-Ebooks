package com.porar.ebooks.stou.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.Adapter_List_BookShelf;
import com.porar.ebook.adapter.Adapter_List_BookShelf.ShelfPhone;
import com.porar.ebook.adapter.Adapter_List_BookShelf.clickDeleteP;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf.Shelf;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf.clickDelete;
import com.porar.ebook.control.DialogLogin_Show;
import com.porar.ebook.control.DialogProfile_Show;
import com.porar.ebook.control.Dialog_ShowLargeImage;
import com.porar.ebook.control.Dialog_ShowLongClick;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.Ebook_Detail;
import com.porar.ebook.control.Ebook_Shelf;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.ThrowModel_Restore;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.control.View_SearchEbookShelf;
import com.porar.ebook.control.View_Setting;
import com.porar.ebook.control.View_TypeEbookShelf;
import com.porar.ebook.control.addTextWatcher;
import com.porar.ebook.control.addTextWatcher.IntefaceAfterTextChange;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Customer_Detail;
import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.model.Model_Ebooks_Detail;
import com.porar.ebooks.model.Model_Restore;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.Page_Image_Slide;
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

public class Fragment_Shelf extends Fragment {
    public String tag = "Fragment_Shelf";
    private MyViewPager myViewPager;
    public ProgressDialog progressDialog = null;
    // control
    private TextView txt_refresh, txt_editsave, txt_setting, txt_signout;
    // private ImageView image_refresh, image_editsave, image_setting,
    // image_signout;
    private LinearLayout linear_refresh, /* linear_editsave, */
            linear_setting, /*
                                                                             * linear_signout
																			 * ,
																			 */
            linear_customforTabview, linear_dotpage;
    private RelativeLayout rt_dummyscreen, rt_head, rt_profile;
    // btn
    // private Button btn_type;
    private Button btnMenuNew;
    private Button btnMenuFavorite;
    private Button btnMenuTrash;
    // animation
    private Animation fade_in;
    // Class

    private AlertDialog alertDialog;
    private Bundle bundle;
    public static String serial_category = "serial_category";
    public static String serial_type = "serial_type";
    public static String serial_search = "serial_search";
    public static String Shelf_type = "Shelf_type";
    public static String Shelf_Currunt = "Shelf_Currunt";
    public FragmentActivity myActivity;
    public DialogLogin_Show dialogLogin_Show;
    public Dialog_ShowLongClick dialog_ShowLongClick;
    Fragment_Shelf fragment_Shelf;
    // private UiLifecycleHelper uiHelper;
    private final Handler handler = new Handler();
    private Ebook_Shelf ebook_Shelf;
    private MyAdapterViewPager_BookShelf adapterViewPager_BookShelf;
    private Dot_Pageslide dot_Pageslide;
    private View_TypeEbookShelf view_TypeEbookShelf;
    private ImageDownloader_forCache downloader_forCache;
    private ArrayList<Model_Restore> arrayListRestores = null;
    // private Publisher_Fan publisher_Fan;
    // private Adapter_SearchList_PublisherFan adapter_SearchList_PublisherFan;
    // private ArrayList<Model_Publisher_Fan> arrpublisher_Fans;
    private boolean isdelete = true;
    private ArrayList<Integer> arrDeletebyBID;
    LoadAPIResultString apiResultString;
    final int Toastshort = 500;
    private final int POST_DELEY = 8000;
    int pos = 1;
    // TextView txt_favorite;
    // ListView lisview;
    // TextView txt_fanpublisher;
    public EditText editText = null;
    addTextWatcher textWatcher;
    View_Setting view_Setting;
    ArrayList<Model_EBook_Shelf_List> arrayListoffline;
    // object phone
    DialogProfile_Show dialogProfile_Show;
    ImageView img_profile, img_refresh, img_peditsave;
    Button btn_pnew, btn_pfavorite, btn_ptrash;
    ListView listview_phone;
    Adapter_List_BookShelf adapter_List_BookShelf;
    String bundle_ebook_shelftype_phone = "bundle_ebook_type_phone";

    // STOU
    private TextView signInTextView;
    private TextView signOutTextView;
    private TextView editTextView;
    private LinearLayout searchLinearLayout;
    private String searchEditTextSaveInstance;
    private TextView txt_search;

    public static Fragment_Shelf newInstance() {
        Fragment_Shelf fragment = new Fragment_Shelf();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterFacebook.InitFacebookSDK(getActivity());

        Log.e(tag, "onCreate");
        if (AppMain.isphone) {

            Log.e(tag, "onCreate  isphone");

            fragment_Shelf = this;
            bundle = new Bundle();
            myActivity = getActivity();
            downloader_forCache = new ImageDownloader_forCache();

            if (Shared_Object.isOfflineMode) {
                arrayListoffline = Class_Manage
                        .getEbooksShelfInOffLineMode(myActivity);
            }

            dialogLogin_Show = new DialogLogin_Show(myActivity,
                    R.style.PauseDialog, fragment_Shelf);
            dialogLogin_Show.setCancelable(false);

            dialogLogin_Show.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {

                    // / No CID
                    if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                        // not success
                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                            Log.i("", "facebook is login");
                            StaticUtils.setFacebooklogin(true);
                        } else {
                            Log.i("", "facebook not login");
                            StaticUtils.setFacebooklogin(false);
                        }
                    }

                    // / Have CID
                    else {
                        profilePhone();
                        // check facebook sign in
                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                            Log.i("", "facebook is login");
                            StaticUtils.setFacebooklogin(true);
                        } else {
                            Log.i("", "facebook not login");
                            StaticUtils.setFacebooklogin(false);
                        }

                        ebook_Shelf = new Ebook_Shelf(myActivity);
                        ebook_Shelf
                                .setOnListenerRestore(new ThrowModel_Restore() {

                                    @Override
                                    public void throwModel(
                                            ArrayList<Model_Restore> restore) {
                                        arrayListRestores = restore;

                                        for (Model_Restore model_Restore : restore) {
                                            Log.e("restore", "restore bid"
                                                    + model_Restore.getBID());
                                        }
                                    }
                                });
                        ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                            @Override
                            public void StartLoadPList() {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList_TopPeriod(PList Plist1,
                                                        PList Plist2, ProgressDialog pd) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList_Detail_Comment(PList Plistdetail,
                                                             PList Plistcomment, ProgressDialog pd) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList(final PList resultPlist,
                                              final ProgressDialog pd) {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        // set listview
                                        setListView(resultPlist, pd);
                                        Log.e("restore", "restore bid"
                                                + resultPlist.toString());

                                    }
                                }, 0);

                                // setProfileCustomer();

                            }
                        });
                        ebook_Shelf.LoadEbooksTrashAPI();
                        ebook_Shelf.LoadEbooksDetailAPI();

                    }

                }

            });// End of dialogShow.Cancel

        } // Is Phone

        else {
            fragment_Shelf = this;
            bundle = new Bundle();
            myActivity = getActivity();
            downloader_forCache = new ImageDownloader_forCache();

            if (Shared_Object.isOfflineMode) {
                arrayListoffline = Class_Manage
                        .getEbooksShelfInOffLineMode(myActivity);
            }
            textWatcher = new addTextWatcher();
            textWatcher.addInteface(new IntefaceAfterTextChange() {

                @Override
                public void AfterTextChange(String str) {
                    if (adapterViewPager_BookShelf != null) {
                        bundle.putString(serial_search, str);
                        MyAdapterViewPager_BookShelf.setEdittextsearch(str);
                        myViewPager.setAdapter(adapterViewPager_BookShelf);
                    }

                }
            });

            dialogLogin_Show = new DialogLogin_Show(myActivity,
                    R.style.PauseDialog, fragment_Shelf);
            dialogLogin_Show.setCancelable(false);
            dialogLogin_Show.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // No CID

                    if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                        // not success
                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                            Log.i("", "facebook is login");
                            StaticUtils.setFacebooklogin(true);
                        } else {
                            Log.i("", "facebook not login");
                            StaticUtils.setFacebooklogin(false);
                        }

                    }
                    // / Have CID
                    else {

                        // check facebook sign in
                        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                            Log.i("", "facebook is login");
                            StaticUtils.setFacebooklogin(true);
                        } else {
                            Log.i("", "facebook not login");
                            StaticUtils.setFacebooklogin(false);
                        }
                        setShelfForShelf();
                        // get instance state
                        if (bundle.getString(serial_type) != null) {
                            // btn_type.setText(bundle.getString(serial_type));

                            setEnableMenu(bundle.getString(serial_type));

                        }
                        // get instance state
                        if (bundle.getSerializable(Shelf_type) != null) {
                            MyAdapterViewPager_BookShelf
                                    .seteNumtype((Shelf) bundle
                                            .getSerializable(Shelf_type));
                        }


                        ebook_Shelf = new Ebook_Shelf(myActivity);
                        ebook_Shelf
                                .setOnListenerRestore(new ThrowModel_Restore() {

                                    @Override
                                    public void throwModel(
                                            ArrayList<Model_Restore> restore) {
                                        arrayListRestores = restore;

                                        for (Model_Restore model_Restore : restore) {
                                            Log.e("restore", "restore bid"
                                                    + model_Restore.getBID());
                                        }
                                    }
                                });
                        ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                            @Override
                            public void StartLoadPList() {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList_TopPeriod(PList Plist1,
                                                        PList Plist2, ProgressDialog pd) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList_Detail_Comment(PList Plistdetail,
                                                             PList Plistcomment, ProgressDialog pd) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void PList(final PList resultPlist,
                                              final ProgressDialog pd) {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        setViewPager(resultPlist, pd);
                                    }
                                }, 1000);

                                setProfileCustomer();
                                // setViewPager(resultPlist, pd);
                                // setProfileCustomer();
                            }
                        });
                        ebook_Shelf.LoadEbooksTrashAPI();
                        ebook_Shelf.LoadEbooksDetailAPI();

                    }
                }

            });
        }
    } // / Check ID & Phone Tablet

    private void setShelfForShelf() {
        try {
            Model_Setting model_setting = Class_Manage
                    .getModel_Setting(myActivity);
            if (model_setting != null) {
                ((RelativeLayout) getActivity().findViewById(
                        R.id.shelf_mainlayout))
                        .setBackgroundResource(model_setting
                                .getDrawable_backgroundStyle());
            }
            Model_SettingShelf settingShelf = Class_Manage
                    .getModel_SettingShelf(myActivity);
            if (settingShelf != null) {
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf1))
                        .setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf2))
                        .setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf3))
                        .setImageResource(settingShelf.getDrawable_ShelfStyle());
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf4))
                        .setImageResource(settingShelf.getDrawable_ShelfStyle());
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(tag, "onCreateView");
        View view = inflater.inflate(R.layout.activity_shelf, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(tag, "onActivityCreated");
        if (AppMain.isphone) { // Phone
            setId_Phone();
            profilePhone();

            if (Shared_Object.getCustomerDetail.getCID() <= 0) { // No CID

                dialogLogin_Show.show();

            } else { // Have CID
                if (Shared_Object.isOfflineMode) {// Offline Mode
                    if (AppMain.pList_default_ebookshelf == null) {
                        if (bundle.getInt(bundle_ebook_shelftype_phone) != 0) {
                            setDefaultEnableType(bundle
                                    .getInt(bundle_ebook_shelftype_phone));
                        } else {
                            setListViewOfflineMode(arrayListoffline, null);
                        }
                        return;
                    }
                }

                if (AppMain.pList_default_ebookshelf == null
                        || arrayListRestores == null) { // IF Array is null , Go
                    // to Load API
                    ebook_Shelf = new Ebook_Shelf(myActivity);
                    ebook_Shelf.setOnListenerRestore(new ThrowModel_Restore() {

                        @Override
                        public void throwModel(ArrayList<Model_Restore> restore) {
                            arrayListRestores = restore;
                            for (Model_Restore model_Restore : restore) {
                                Log.e("restore",
                                        "restore bid" + model_Restore.getBID());
                                Log.e("restore", "restore cover"
                                        + model_Restore.getCoverFileNameS());
                            }
                        }
                    });
                    ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                        @Override
                        public void StartLoadPList() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList_TopPeriod(PList Plist1, PList Plist2,
                                                    ProgressDialog pd) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList_Detail_Comment(PList Plistdetail,
                                                         PList Plistcomment, ProgressDialog pd) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList(final PList resultPlist,
                                          final ProgressDialog pd) {
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    setListView(resultPlist, pd);

                                }
                            }, 0);
                        }
                    });
                    ebook_Shelf.LoadEbooksTrashAPI();
                    ebook_Shelf.LoadEbooksDetailAPI();
                    return;
                } else {

                    // get instance
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            if (bundle.getInt(bundle_ebook_shelftype_phone) != 0) {
                                setDefaultEnableType(bundle
                                        .getInt(bundle_ebook_shelftype_phone));
                            } else {
                                setListView(AppMain.pList_default_ebookshelf,
                                        null);
                            }

                        }
                    }, 0);
                }
            }

        } else {// Tablet

            setId();
            if (StaticUtils.pageID == 0) {
                setShelfForShelf();

            } else if (StaticUtils.pageID == 1) {

                setShelfForShelf();

            } else {
                setBackground();
                setShelf();
            }

            // check login value cid
            if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                setProfileCustomer();
                dialogLogin_Show.show();

                // linear_signout.setEnabled(false);
                // try {
                // if (android.os.Build.VERSION.SDK_INT >=
                // android.os.Build.VERSION_CODES.HONEYCOMB) {
                // linear_signout.setAlpha(0.5F);
                // }
                // } catch (Exception e) {
                // // TODO: handle exception
                // }
            } else {
                // signInTextView.setEnabled(false);
                // signOutTextView.setEnabled(true);
                // editTextView.setEnabled(true);
                // try {
                // if (android.os.Build.VERSION.SDK_INT >=
                // android.os.Build.VERSION_CODES.HONEYCOMB) {
                // linear_signout.setAlpha(1F);
                // }
                // } catch (Exception e) {
                // // TODO: handle exception
                // }

                if (Shared_Object.isOfflineMode) {
                    if (AppMain.pList_default_ebookshelf == null) {
                        setProfileCustomer();
                        setViewPagerOffLine();
                        // get instance state
                        if (bundle.getString(serial_type) != null) {
                            // btn_type.setText(bundle.getString(serial_type));
                            setEnableMenu(bundle.getString(serial_type));
                        }
                        // get instance state
                        if (bundle.getSerializable(Shelf_type) != null) {
                            MyAdapterViewPager_BookShelf
                                    .seteNumtype((Shelf) bundle
                                            .getSerializable(Shelf_type));
                        }
                        // get instance state
                        if (bundle.getString(serial_search) != null) {
                            MyAdapterViewPager_BookShelf
                                    .setEdittextsearch(bundle
                                            .getString(serial_search));
                        }
                        return;
                    }

                }

                // restore book snuxker
                // if (Activity_Detail.alreadyBuyAndDirectToShelf) {
                // Activity_Detail.alreadyBuyAndDirectToShelf = false;
                //
                //
                // } else {

                if (AppMain.pList_default_ebookshelf == null
                        || arrayListRestores == null) {
                    ebook_Shelf = new Ebook_Shelf(myActivity);
                    ebook_Shelf.setOnListenerRestore(new ThrowModel_Restore() {

                        @Override
                        public void throwModel(ArrayList<Model_Restore> restore) {
                            arrayListRestores = restore;
                            for (Model_Restore model_Restore : restore) {
                                Log.e("restore",
                                        "restore bid" + model_Restore.getBID());
                                Log.e("restore", "restore cover"
                                        + model_Restore.getCoverFileNameS());
                            }
                        }
                    });
                    ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                        @Override
                        public void StartLoadPList() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList_TopPeriod(PList Plist1, PList Plist2,
                                                    ProgressDialog pd) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList_Detail_Comment(PList Plistdetail,
                                                         PList Plistcomment, ProgressDialog pd) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void PList(final PList resultPlist,
                                          final ProgressDialog pd) {
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    setViewPager(resultPlist, pd);
                                }
                            }, 1000);

                            setProfileCustomer();
                            // setViewPager(resultPlist, pd);
                            // setProfileCustomer();
                        }
                    });
                    ebook_Shelf.LoadEbooksTrashAPI();
                    ebook_Shelf.LoadEbooksDetailAPI();
                    return;
                } else {
                    // get instance state
                    if (bundle.getString(serial_type) != null) {
                        // btn_type.setText(bundle.getString(serial_type));
                        setEnableMenu(bundle.getString(serial_type));
                    }
                    // get instance state
                    if (bundle.getSerializable(Shelf_type) != null) {
                        MyAdapterViewPager_BookShelf.seteNumtype((Shelf) bundle
                                .getSerializable(Shelf_type));
                    }
                    if (bundle.getString(serial_search) != null) {
                        MyAdapterViewPager_BookShelf.setEdittextsearch(bundle
                                .getString(serial_search));
                    }

                    // stou auto_refresh

                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (MyAdapterViewPager_BookShelf.shelf == Shelf.tresh) {
                                setViewPager(arrayListRestores, null);
                            } else {
                                setViewPager(AppMain.pList_default_ebookshelf,
                                        null);
                            }
                        }
                    }, 1000);

                    setProfileCustomer();
                    return;
                }

                // }
            }
        }
    }

    private void setId_Phone() {
        fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);

        listview_phone = (ListView) getActivity().findViewById(
                R.id.shelf_plistview);
        listview_phone.setDividerHeight(0);
        listview_phone.setDivider(null);

        img_profile = (ImageView) getActivity().findViewById(
                R.id.shelf_image_pImageProfile);
        img_refresh = (ImageView) getActivity().findViewById(
                R.id.shelf_image_prefresh);
        img_peditsave = (ImageView) getActivity().findViewById(
                R.id.shelf_image_pdelete);
        btn_pnew = (Button) getActivity().findViewById(
                R.id.shelf_tabview_btn_pnew);
        btn_pfavorite = (Button) getActivity().findViewById(
                R.id.shelf_tabview_btn_pfavorite);
        btn_ptrash = (Button) getActivity().findViewById(
                R.id.shelf_tabview_btn_ptrash);

        img_profile.setOnClickListener(new OnClickControl());
        img_refresh.setOnClickListener(new OnClickControl());
        img_peditsave.setOnClickListener(new OnClickControl());

        btn_pnew.setEnabled(false);
        btn_pnew.setOnClickListener(new OnCLickBtnPhoneType());
        btn_pfavorite.setOnClickListener(new OnCLickBtnPhoneType());
        btn_ptrash.setOnClickListener(new OnCLickBtnPhoneType());

        arrDeletebyBID = null;
        pos = 1;
    }

    private void setListViewOfflineMode(
            ArrayList<Model_EBook_Shelf_List> shelfoffline,
            ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                listview_phone.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {

            adapter_List_BookShelf = null;
            listview_phone.setAdapter(null);
            adapter_List_BookShelf = new Adapter_List_BookShelf(myActivity, 0,
                    shelfoffline, true) {
                private static final long serialVersionUID = 1L;

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    Intent myIntent = new Intent(myActivity,
                            Page_Image_Slide.class);
                    myIntent.putExtra("bid", model.getBID());
                    myIntent.putExtra("model", model);
                    myIntent.putExtra("customer",
                            Shared_Object.getCustomerDetail);

                    fragment_Shelf.startActivityForResult(myIntent, 2013);
                    myActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {

                    dialog_ShowLongClick = new Dialog_ShowLongClick(myActivity,
                            R.style.PauseDialog, model, myActivity);
                    dialog_ShowLongClick
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dia) {
                                    Log.e(tag, "Dialog cancel");
                                    // setProfileCustomer();
                                    try {
                                        setListViewOfflineMode(
                                                arrayListoffline, null);
                                    } catch (NullPointerException e) {
                                        // TODO: handle exception
                                    }

                                }
                            });

                    LoadDetailDialog detailDialog = new LoadDetailDialog();
                    detailDialog.newInstant(model.getBID());
                    detailDialog.LoadDatadetail();
                    // dialog_ShowLongClick.show();

                }

            };

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    listview_phone.setAdapter(adapter_List_BookShelf);

                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }, 50);
            if (adapter_List_BookShelf.getSizePage() != 0) {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);
                alertDialog.show();
            }

            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        } catch (NullPointerException e) {
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    private void setListView(PList plist, ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                listview_phone.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {

            adapter_List_BookShelf = null;
            listview_phone.setAdapter(null);
            adapter_List_BookShelf = new Adapter_List_BookShelf(myActivity, 0,
                    plist) {
                private static final long serialVersionUID = 1L;

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    Intent myIntent = new Intent(myActivity,
                            Page_Image_Slide.class);
                    myIntent.putExtra("bid", model.getBID());
                    myIntent.putExtra("model", model);
                    myIntent.putExtra("customer",
                            Shared_Object.getCustomerDetail);

                    fragment_Shelf.startActivityForResult(myIntent, 2013);
                    myActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {

                    dialog_ShowLongClick = new Dialog_ShowLongClick(myActivity,
                            R.style.PauseDialog, model, myActivity);
                    dialog_ShowLongClick
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dia) {
                                    Log.e(tag, "Dialog cancel");
                                    // setProfileCustomer();
                                    try {
                                        setListView(
                                                AppMain.pList_default_ebookshelf,
                                                null);
                                    } catch (NullPointerException e) {
                                        // TODO: handle exception
                                    }

                                }
                            });

                    LoadDetailDialog detailDialog = new LoadDetailDialog();
                    detailDialog.newInstant(model.getBID());
                    detailDialog.LoadDatadetail();
                    // dialog_ShowLongClick.show();

                }

            };
            adapter_List_BookShelf.setDeleteItem(new clickDeleteP() {

                @Override
                public void deleteBook(ArrayList<Integer> arrayListDelete) {
                    for (Integer bid : arrayListDelete) {
                        Log.e("", "deleteBook bid " + bid);
                    }
                    arrDeletebyBID = arrayListDelete;
                }
            });

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    listview_phone.setAdapter(adapter_List_BookShelf);

                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }, 50);
            if (adapter_List_BookShelf.getSizePage() != 0) {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);
                alertDialog.show();
            }

            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            if (adapter_List_BookShelf.isDeleteBook()) {
                img_peditsave.setImageResource(R.drawable.content_save);
                isdelete = false;
            } else {
                img_peditsave.setImageResource(R.drawable.content_discard);
                isdelete = true;
            }
        } catch (NullPointerException e) {
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    private void setListViewTrash(ArrayList<Model_Restore> arrayListRestores,
                                  ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                listview_phone.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }

        try {

            adapter_List_BookShelf = null;
            listview_phone.setAdapter(null);
            adapter_List_BookShelf = new Adapter_List_BookShelf(myActivity, 0,
                    arrayListRestores) {
                private static final long serialVersionUID = 1L;

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {
                    Log.i("", "");
                }

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    Log.i("", "Model_EBook_Shelf_List");
                }
            };

            adapter_List_BookShelf.setDeleteItem(new clickDeleteP() {

                @Override
                public void deleteBook(ArrayList<Integer> arrayListDelete) {
                    for (Integer bid : arrayListDelete) {
                        Log.e("", "deleteBook bid " + bid);
                    }
                    arrDeletebyBID = arrayListDelete;
                }
            });
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    listview_phone.setAdapter(adapter_List_BookShelf);

                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }, 50);
            if (adapter_List_BookShelf.getSizePage() != 0) {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);
                alertDialog.show();
            }

            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            if (adapter_List_BookShelf.isDeleteBook()) {
                img_peditsave.setImageResource(R.drawable.content_save);
                isdelete = false;
            } else {
                img_peditsave.setImageResource(R.drawable.content_discard);
                isdelete = true;
            }
        } catch (NullPointerException e) {
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    private class OnCLickBtnPhoneType implements OnClickListener {

        @Override
        public void onClick(View v) {
            setDefaultEnableType(v.getId());
            arrDeletebyBID = null;
            pos = 1;

        }

    }

    private void setDefaultEnableType(int id) {

        btn_pnew.setEnabled(true);
        btn_pfavorite.setEnabled(true);
        btn_ptrash.setEnabled(true);

        if (id == btn_pnew.getId()) {
            btn_pnew.setEnabled(false);
            bundle.putInt(bundle_ebook_shelftype_phone, btn_pnew.getId());
            Adapter_List_BookShelf.seteNumtypePhone(ShelfPhone.news);

            if (Shared_Object.isOfflineMode) {
                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                    return;
                } else {
                    if (AppMain.pList_default_ebookshelf == null) {
                        setListViewOfflineMode(arrayListoffline, null);
                        return;
                    }
                }

            }

            if (AppMain.pList_default_ebookshelf != null) {
                setListView(AppMain.pList_default_ebookshelf, null);
                return;
            }

        }
        if (id == btn_pfavorite.getId()) {
            btn_pfavorite.setEnabled(false);
            bundle.putInt(bundle_ebook_shelftype_phone, btn_pfavorite.getId());
            Adapter_List_BookShelf.seteNumtypePhone(ShelfPhone.favorite);

            if (Shared_Object.isOfflineMode) {
                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                    return;
                } else {
                    if (AppMain.pList_default_ebookshelf == null) {
                        setListViewOfflineMode(arrayListoffline, null);
                        return;
                    }
                }
            }

            if (AppMain.pList_default_ebookshelf != null) {
                setListView(AppMain.pList_default_ebookshelf, null);
            }
        }
        if (id == btn_ptrash.getId()) {
            btn_ptrash.setEnabled(false);
            bundle.putInt(bundle_ebook_shelftype_phone, btn_ptrash.getId());
            Adapter_List_BookShelf.seteNumtypePhone(ShelfPhone.tresh);

            if (Shared_Object.isOfflineMode) {
                if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                    return;
                } else {
                    if (AppMain.pList_default_ebookshelf == null) {
                        setListViewOfflineMode(arrayListoffline, null);
                        return;
                    }
                }
            }

            // remove if STOU
            // if (arrayListRestores != null) {
            setListViewTrash(arrayListRestores, null);
            // }else
        }

    }

    private void profilePhone() {
        try {
            if (Shared_Object.getCustomerDetail.getCID() > 0) {
                downloader_forCache.download(
                        Shared_Object.getCustomerDetail.getPictureUrl(),
                        img_profile);
            } else {
                img_profile.setImageResource(R.drawable.icon_user100);
            }
        } catch (NullPointerException e) {
            img_profile.setImageResource(R.drawable.icon_user100);
        }

    }

    private void setProfileCustomer() {
        removeProfile();
        setProfile();
    }

    private void setViewPager(final PList pList, final ProgressDialog dialog) {

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }

        try {

            myViewPager.removeAllViews();
            adapterViewPager_BookShelf = null;
            adapterViewPager_BookShelf = new MyAdapterViewPager_BookShelf(
                    myActivity, pList) {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    Intent myIntent = new Intent(myActivity,
                            Page_Image_Slide.class);
                    myIntent.putExtra("bid", model.getBID());
                    myIntent.putExtra("model", model);
                    myIntent.putExtra("customer",
                            Shared_Object.getCustomerDetail);

                    fragment_Shelf.startActivityForResult(myIntent, 2013);
                    myActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);

                }

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {
                    dialog_ShowLongClick = new Dialog_ShowLongClick(myActivity,
                            R.style.PauseDialog, model, myActivity);
                    dialog_ShowLongClick
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dia) {
                                    Log.e(tag, "Dialog cancel");
                                    // setProfileCustomer();
                                    try {
                                        // txt_favorite.setText("" +
                                        // Shared_Object.getCustomerDetail.getFavorites());
                                        setViewPager(
                                                AppMain.pList_default_ebookshelf,
                                                null);
                                    } catch (NullPointerException e) {
                                        // TODO: handle exception
                                    }

                                }
                            });

                    LoadDetailDialog detailDialog = new LoadDetailDialog();
                    detailDialog.newInstant(model.getBID());
                    detailDialog.LoadDatadetail();
                    // dialog_ShowLongClick.show();
                }

            };// constance default
            adapterViewPager_BookShelf.setDeleteItem(new clickDelete() {

                @Override
                public void deleteBook(ArrayList<Integer> arrayListDelete) {
                    for (Integer bid : arrayListDelete) {
                        Log.e("", "deleteBook bid " + bid);
                    }
                    arrDeletebyBID = arrayListDelete;
                }
            });

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_BookShelf
                                    .getCount()));
                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
                                    bundle.putInt(Shelf_Currunt,
                                            myViewPager.getCurrentItem());
                                }

                                @Override
                                public void onPageScrolled(int arg0,
                                                           float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int arg0) {

                                }
                            });

                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            if (adapterViewPager_BookShelf != null) {
                                dialog.dismiss();
                            }
                        }
                    }
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
            }, 50);
            try {

                if (adapterViewPager_BookShelf.getSizePage() != 0) {
                    alertDialog = new AlertDialog.Builder(myActivity).create();
                    TextView msg = new TextView(myActivity);
                    msg.setText("Please Wait... Loading");
                    msg.setPadding(10, 10, 10, 10);
                    msg.setGravity(Gravity.CENTER);
                    alertDialog.setView(msg);
                    alertDialog.show();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {

                //
                if (adapterViewPager_BookShelf.isDeleteBook()) {
                    // image_editsave.setImageResource(R.drawable.icon_save2x);
                    // txt_editsave.setText("Save");
                    editTextView.setText(getResources().getString(R.string.record_book_shelf));
                    isdelete = false;
                } else {
                    // image_editsave.setImageResource(R.drawable.icon_edit2x);
                    // txt_editsave.setText(" Edit ");
                    editTextView.setText(getResources().getString(R.string.deleted_book_shelf));
                    isdelete = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }

    }

    // After LongClick Display Deatil Book
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
                    .getModel_DetailByNever(myActivity.getApplicationContext(),
                            model_EbookList);
            if (DSebooks_Detail != null) {
                Log.v("OnClickToDetailEbook", "DeserializeObject Success"
                        + model_EbookList);
                // intent
                dialog_ShowLongClick.setDetail(DSebooks_Detail, 1);
                dialog_ShowLongClick.show();
            } else {
                ebook_DetailApi = new Ebook_Detail(myActivity,
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
                                    myActivity.getApplicationContext(),
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

                            alertDialog = new AlertDialog.Builder(myActivity)
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

    private void setViewPager(final ArrayList<Model_Restore> arrayListRestores,
                              final ProgressDialog dialog) {

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {

            myViewPager.removeAllViews();
            adapterViewPager_BookShelf = null;
            adapterViewPager_BookShelf = new MyAdapterViewPager_BookShelf(
                    myActivity, arrayListRestores) {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {
                    // TODO Auto-generated method stub

                }

            };// constance restore
            adapterViewPager_BookShelf.setDeleteItem(new clickDelete() {

                @Override
                public void deleteBook(ArrayList<Integer> arrayListDelete) {
                    for (Integer bid : arrayListDelete) {
                        Log.e("", "deleteBook bid " + bid);
                    }
                    arrDeletebyBID = arrayListDelete;
                }
            });
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_BookShelf
                                    .getCount()));
                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
                                    bundle.putInt(Shelf_Currunt,
                                            myViewPager.getCurrentItem());
                                }

                                @Override
                                public void onPageScrolled(int arg0,
                                                           float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int arg0) {

                                }
                            });

                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            if (adapterViewPager_BookShelf != null) {
                                dialog.dismiss();
                            }
                        }
                    }
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
            }, 50);

            if (adapterViewPager_BookShelf.getSizePage() != 0) {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);
                alertDialog.show();
            }
            //
            if (adapterViewPager_BookShelf.isDeleteBook()) {
                editTextView.setText(getResources().getString(R.string.record_book_shelf));
                isdelete = false;
            } else {
                // image_editsave.setImageResource(R.drawable.icon_edit2x);
                // txt_editsave.setText(" Edit ");
                editTextView.setText(getResources().getString(R.string.deleted_book_shelf));
                isdelete = true;
            }

        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }

    }

    private void removeViewDotPage() {
        if (linear_dotpage.getChildCount() > 0) {
            linear_dotpage.removeAllViews();
        }
    }

    private void removeProfile() {
        rt_profile.removeAllViews();
    }

    public void setProfile() {
        if (Shared_Object.getCustomerDetail.getCID() <= 0) {
            LinearLayout profile_notlogin = (LinearLayout) LayoutInflater.from(
                    myActivity).inflate(R.layout.layout_profile_notlogin, null);
            if (profile_notlogin != null) {
                rt_profile.addView(profile_notlogin);

                ImageView btn = (ImageView) profile_notlogin
                        .findViewById(R.id.rt_profile_loginButton1);
                TextView tv1 = (TextView) profile_notlogin
                        .findViewById(R.id.rt_profile_textView1);
                TextView tv2 = (TextView) profile_notlogin
                        .findViewById(R.id.rt_profile_textView2);

                tv1.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));
                tv2.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));
                btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DialogLogin_Show(getActivity(), R.style.PauseDialog, Fragment_Shelf.this).show();
                    }
                });
            }
        } else {
            View profile_customer = LayoutInflater.from(myActivity).inflate(
                    R.layout.layout_profile_customer, null);
            if (profile_customer != null) {
                rt_profile.addView(profile_customer);
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

                signInTextView = (TextView) profile_customer
                        .findViewById(R.id.sign_in_textView);
                signOutTextView = (TextView) profile_customer
                        .findViewById(R.id.sign_out_textView);
                editTextView = (TextView) profile_customer
                        .findViewById(R.id.edit_textView);

                // stou
                signInTextView.setVisibility(View.INVISIBLE);
                String userPicURL = Shared_Object.getCustomerDetail
                        .getPictureUrl();

                if (userPicURL.length() <= 0)
                    userPicURL = AppMain.DEFAULT_USER_PIC_URL;

                downloader_forCache.download(userPicURL, img_cover);
                // end

                Log.e("snuxker",
                        "Shared_Object.getCustomerDetail.getPictureUrl() : "
                                + Shared_Object.getCustomerDetail
                                .getPictureUrl());

                img_cover.setOnClickListener(new onclickShowZoomImage(
                        Shared_Object.getCustomerDetail.getPictureUrl()));

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

                txt_name.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));
                txt_email.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));
                txt_address.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));
                txt_phone.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.THSarabanNew));

                signInTextView.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.DB_Helvethaica_X_Med));
                signOutTextView.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.DB_Helvethaica_X_Med));
                editTextView.setTypeface(StaticUtils.getTypeface(myActivity,
                        Font.DB_Helvethaica_X_Med));

                // stou
                signInTextView.setEnabled(false);
                signOutTextView.setEnabled(true);
                editTextView.setEnabled(true);

                signInTextView.setOnClickListener(new OnClickControl());
                signOutTextView.setOnClickListener(new OnClickControl());
                editTextView.setOnClickListener(new OnClickControl());

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
            new Dialog_ShowLargeImage(myActivity, R.style.PauseDialog, url)
                    .show();
        }

    }

    private void setId() {
        rt_head = (RelativeLayout) getActivity().findViewById(
                R.id.shelf_rt_head);
        rt_profile = (RelativeLayout) getActivity().findViewById(
                R.id.shelf_rt_profilecustomer);
        myViewPager = (MyViewPager) getActivity().findViewById(
                R.id.shelf_myViewPager1);
        // editText = (EditText)
        // getActivity().findViewById(R.id.shelf_edittext_search);
        linear_dotpage = (LinearLayout) getActivity().findViewById(
                R.id.shelf_linear_dotpage);
        rt_dummyscreen = (RelativeLayout) getActivity().findViewById(
                R.id.shelf_rt_dummyscreen);
        linear_customforTabview = (LinearLayout) getActivity().findViewById(
                R.id.shelf_linear_fortabview);
        linear_refresh = (LinearLayout) getActivity().findViewById(
                R.id.shelf_linear_head_refresh);
        // linear_editsave = (LinearLayout)
        // getActivity().findViewById(R.id.shelf_linear_head_editsave);
        linear_setting = (LinearLayout) getActivity().findViewById(
                R.id.shelf_linear_head_setting);
        // linear_signout = (LinearLayout)
        // getActivity().findViewById(R.id.shelf_linear_head_signout);

        // image_refresh = (ImageView)
        // getActivity().findViewById(R.id.shelf_image_refresh);
        // image_editsave = (ImageView)
        // getActivity().findViewById(R.id.shelf_image_editsave);
        // image_setting = (ImageView)
        // getActivity().findViewById(R.id.shelf_image_setting);
        // image_signout = (ImageView)
        // getActivity().findViewById(R.id.shelf_image_signout);

        txt_refresh = (TextView) getActivity().findViewById(
                R.id.shelf_txt_refresh);
        // txt_editsave = (TextView)
        // getActivity().findViewById(R.id.shelf_txt_editsave);
        txt_setting = (TextView) getActivity().findViewById(
                R.id.shelf_txt_setting);
        // txt_signout = (TextView)
        // getActivity().findViewById(R.id.shelf_txt_signout);
        // btn_type = (Button) getActivity().findViewById(R.id.shelf_btn_type);

        // stou
        searchLinearLayout = (LinearLayout) getActivity().findViewById(
                R.id.shelf_linear_head_search);
        txt_search = (TextView) getActivity().findViewById(
                R.id.shelf_txt_search);

        txt_search.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_Helvethaica_X_Med));
        // end

        // txt_editsave.setTypeface(StaticUtils.getTypeface(myActivity,
        // Font.DB_Helvethaica_X_Med));
        // txt_signout.setTypeface(StaticUtils.getTypeface(myActivity,
        // Font.DB_Helvethaica_X_Med));
        txt_refresh.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_Helvethaica_X_Med));
        txt_setting.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_Helvethaica_X_Med));

        // stou
        btnMenuNew = (Button) getActivity().findViewById(R.id.shelf_btn_new);
        btnMenuFavorite = (Button) getActivity().findViewById(
                R.id.shelf_btn_favorite);
        btnMenuTrash = (Button) getActivity()
                .findViewById(R.id.shelf_btn_trash);

        btnMenuNew.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.THSarabanNew));
        btnMenuFavorite.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.THSarabanNew));
        btnMenuTrash.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.THSarabanNew));
        btnMenuNew.setOnClickListener(new onClickBtnAlertView());
        btnMenuFavorite.setOnClickListener(new onClickBtnAlertView());
        btnMenuTrash.setOnClickListener(new onClickBtnAlertView());
        // end

        // btn_type.setTypeface(StaticUtils.getTypeface(myActivity,
        // Font.DB_Helvethaica_X_Med));
        // btn_type.setOnClickListener(new onClickBtnAlertView());

        // linear_signout.setEnabled(false);

        searchLinearLayout.setOnClickListener(new OnClickControl());
        linear_refresh.setOnClickListener(new OnClickControl());
        // linear_editsave.setOnClickListener(new OnClickControl());
        linear_setting.setOnClickListener(new OnClickControl());
        // linear_signout.setOnClickListener(new OnClickControl());
        // editText.addTextChangedListener(textWatcher);

        fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
        try {
            checkID();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void checkID() {
        try {
            if (!StaticUtils.isAMasterDegree) {

            } else {
                try {

                    linear_setting.setVisibility(View.GONE);
                    rt_head.setBackgroundColor(getResources().getColor(
                            R.color.orange_stou));

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            if (StaticUtils.shelfID == 0) {
                linear_setting.setVisibility(View.VISIBLE);
                rt_head.setBackgroundColor(getResources().getColor(
                        R.color.green_stou));
            } else if (StaticUtils.shelfID == 1) {
                try {

                    linear_setting.setVisibility(View.VISIBLE);
                    rt_head.setBackgroundColor(getResources().getColor(
                            R.color.green_stou));

                } catch (Exception e) {
                    // TODO: handle exception
                }

            } else {
                linear_setting.setVisibility(View.GONE);
                rt_head.setBackgroundColor(getResources().getColor(
                        R.color.orange_stou));

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void setBackground() throws NullPointerException {
        try {

            int[] id_drawble = {R.drawable.bg_main_default,
                    R.drawable.bg_finewood, R.drawable.bg_whilte_hardwood,
                    R.drawable.bg_wood, R.drawable.bg_wood_wall,
                    R.drawable.bg_light_green};

            ((RelativeLayout) getActivity().findViewById(R.id.shelf_mainlayout))
                    .setBackgroundResource(id_drawble[5]);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void setShelf() throws NullPointerException {
        try {

            int[] id_drawableshelf = {R.drawable.shelf_glass2x,
                    R.drawable.shelf_wood2x, R.drawable.shelf_wood_steel2x,
                    R.drawable.shelf_blue2x, R.drawable.shelf_red2x,
                    R.drawable.shelf_dark2x};

            ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf1))
                    .setImageResource(id_drawableshelf[3]);
            ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf2))
                    .setImageResource(id_drawableshelf[3]);
            ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf3))
                    .setImageResource(id_drawableshelf[3]);
            ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf4))
                    .setImageResource(id_drawableshelf[3]);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private class onClickBtnAlertView implements OnClickListener {

        @Override
        public void onClick(View v) {
            // if (v.getId() == btn_type.getId()) {
            // // type
            // view_TypeEbookShelf = new View_TypeEbookShelf(myActivity,
            // linear_customforTabview, rt_dummyscreen, rt_head.getId()) {
            //
            // @Override
            // public void onClickSrceen(RelativeLayout dummyscreen,
            // LinearLayout layout_fortabber, LinearLayout layout_tabbar) {
            // // unlock screen and remove
            // for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
            // if (layout_fortabber.getChildAt(i).equals(layout_tabbar)) {
            // layout_fortabber.removeViewAt(i);
            // // layout_fortabber.removeAllViews();
            // dummyscreen.setVisibility(View.INVISIBLE);
            // }
            // }
            // }
            //
            // @Override
            // public void TypeName(final Hashtable<Integer, String> hashtable)
            // {
            // // set btn text and type ebook
            // handler.post(new Runnable() {
            //
            // @Override
            // public void run() {
            // for (Entry<Integer, String> each : hashtable.entrySet()) {
            // btn_type.setText(each.getValue());
            // setTypeEbookFor_Api(each.getKey());
            //
            // }
            // }
            //
            // });
            // }
            // };
            // view_TypeEbookShelf.initView(0, 0, 0, 0);
            // }

            if (v.getId() == btnMenuNew.getId()) {

                setEnableMenu(getString(R.string.shelf_news));

                setTypeEbookFor_Api(0);
            }

            if (v.getId() == btnMenuFavorite.getId()) {

                setEnableMenu(getString(R.string.shelf_favorite));

                setTypeEbookFor_Api(1);
            }

            if (v.getId() == btnMenuTrash.getId()) {

                setEnableMenu(getString(R.string.shelf_trash));

                setTypeEbookFor_Api(2);

            }
        }
    }

    private void setEnableMenu(String type) {

        if (type.equals(getString(R.string.shelf_news))) {

            btnMenuNew.setEnabled(false);
            btnMenuFavorite.setEnabled(true);
            btnMenuTrash.setEnabled(true);

        }

        if (type.equals(getString(R.string.shelf_favorite))) {

            btnMenuNew.setEnabled(true);
            btnMenuFavorite.setEnabled(false);
            btnMenuTrash.setEnabled(true);

        }

        if (type.equals(getString(R.string.shelf_trash))) {

            btnMenuNew.setEnabled(true);
            btnMenuFavorite.setEnabled(true);
            btnMenuTrash.setEnabled(false);

        }

    }

    private void setTypeEbookFor_Api(Integer key) {
        switch (key) {
            case 0:

                if (AppMain.pList_default_ebookshelf != null) {
                    arrDeletebyBID = null;
                    pos = 1;

                    bundle.putString(serial_type,
                            myActivity.getString(R.string.shelf_news));
                    MyAdapterViewPager_BookShelf.seteNumtype(Shelf.news);
                    bundle.putSerializable(Shelf_type, Shelf.news);

                    setViewPager(AppMain.pList_default_ebookshelf, null);
                    break;
                }
                if (Shared_Object.isOfflineMode) {
                    if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                        break;
                    } else {
                        arrDeletebyBID = null;
                        pos = 1;

                        bundle.putString(serial_type,
                                myActivity.getString(R.string.shelf_news));
                        MyAdapterViewPager_BookShelf.seteNumtype(Shelf.news);
                        bundle.putSerializable(Shelf_type, Shelf.news);

                        setViewPagerOffLine();
                        break;
                    }

                }

            case 1:

                if (AppMain.pList_default_ebookshelf != null) {
                    arrDeletebyBID = null;
                    pos = 1;

                    bundle.putString(serial_type,
                            myActivity.getString(R.string.shelf_favorite));
                    MyAdapterViewPager_BookShelf.seteNumtype(Shelf.favorite);
                    bundle.putSerializable(Shelf_type, Shelf.favorite);

                    setViewPager(AppMain.pList_default_ebookshelf, null);
                    break;
                }
                if (Shared_Object.isOfflineMode) {
                    if (Shared_Object.getCustomerDetail.getCID() <= 0) {
                        break;
                    } else {
                        arrDeletebyBID = null;
                        pos = 1;

                        bundle.putString(serial_type,
                                myActivity.getString(R.string.shelf_favorite));
                        MyAdapterViewPager_BookShelf.seteNumtype(Shelf.favorite);
                        bundle.putSerializable(Shelf_type, Shelf.favorite);

                        setViewPagerOffLine();
                        break;
                    }

                }

            case 2:

                if (arrayListRestores != null) {
                    arrDeletebyBID = null;
                    pos = 1;

                    bundle.putString(serial_type,
                            myActivity.getString(R.string.shelf_trash));
                    MyAdapterViewPager_BookShelf.seteNumtype(Shelf.tresh);
                    bundle.putSerializable(Shelf_type, Shelf.tresh);

                    setViewPager(arrayListRestores, null);

                } else {
                    // nbrenof
                    setViewPager(arrayListRestores, null);
                }

                break;
        }
        // set viewpager by type

    }

    private void setViewPagerOffLine() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                myViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }

        try {

            myViewPager.removeAllViews();
            adapterViewPager_BookShelf = null;
            adapterViewPager_BookShelf = new MyAdapterViewPager_BookShelf(
                    myActivity, true, arrayListoffline) {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void OnClickCover(Model_EBook_Shelf_List model) {
                    Intent myIntent = new Intent(myActivity,
                            Page_Image_Slide.class);
                    myIntent.putExtra("bid", model.getBID());
                    myIntent.putExtra("model", model);
                    myIntent.putExtra("customer",
                            Shared_Object.getCustomerDetail);

                    fragment_Shelf.startActivityForResult(myIntent, 2013);
                    myActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);

                }

                @Override
                public void OnLongClickCover(Model_EBook_Shelf_List model) {
                    dialog_ShowLongClick = new Dialog_ShowLongClick(myActivity,
                            R.style.PauseDialog, model, myActivity);
                    dialog_ShowLongClick
                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dia) {
                                    Log.e(tag, "Dialog cancel");
                                    // setProfileCustomer();
                                    try {
                                        // txt_favorite.setText("" +
                                        // Shared_Object.getCustomerDetail.getFavorites());
                                        setViewPagerOffLine();
                                    } catch (NullPointerException e) {
                                        // TODO: handle exception
                                    }

                                }
                            });

                    LoadDetailDialog detailDialog = new LoadDetailDialog();
                    detailDialog.newInstant(model.getBID());
                    detailDialog.LoadDatadetail();
                    // dialog_ShowLongClick.show();
                }

            };// constance default
            adapterViewPager_BookShelf.setDeleteItem(new clickDelete() {

                @Override
                public void deleteBook(ArrayList<Integer> arrayListDelete) {
                    for (Integer bid : arrayListDelete) {
                        Log.e("", "deleteBook bid " + bid);
                    }
                    arrDeletebyBID = arrayListDelete;
                }
            });

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_BookShelf
                                    .getCount()));
                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
                                    bundle.putInt(Shelf_Currunt,
                                            myViewPager.getCurrentItem());
                                }

                                @Override
                                public void onPageScrolled(int arg0,
                                                           float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int arg0) {

                                }
                            });

                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
            }, 50);

            if (adapterViewPager_BookShelf.getSizePage() != 0) {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);
                alertDialog.show();
            }
            //
            if (adapterViewPager_BookShelf.isDeleteBook()) {
                // image_editsave.setImageResource(R.drawable.icon_save2x);
                // txt_editsave.setText("Save");
                editTextView.setText(getResources().getString(R.string.record_book_shelf));
                isdelete = false;
            } else {
                // image_editsave.setImageResource(R.drawable.icon_edit2x);
                // txt_editsave.setText(" Edit ");
                editTextView.setText(getResources().getString(R.string.deleted_book_shelf));
                isdelete = true;
            }
        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
        }

    }

    private class OnClickControl implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (AppMain.isphone) {
                if (v.getId() == img_profile.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                if (v.getId() == img_refresh.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                if (v.getId() == img_peditsave.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
            } else {
                if (v.getId() == linear_refresh.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                // if (v.getId() == linear_editsave.getId()) {
                // v.startAnimation(fade_in);
                // fade_in.setAnimationListener(new
                // onAnimationLitener(v.getId()));
                // }
                if (v.getId() == linear_setting.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                // if (v.getId() == linear_signout.getId()) {
                // v.startAnimation(fade_in);
                // fade_in.setAnimationListener(new
                // onAnimationLitener(v.getId()));
                // }

                // STOU
                try {

                    if (v.getId() == signOutTextView.getId()) {
                        v.startAnimation(fade_in);
                        fade_in.setAnimationListener(new onAnimationLitener(v
                                .getId()));
                    }

                    if (v.getId() == editTextView.getId()) {
                        v.startAnimation(fade_in);
                        fade_in.setAnimationListener(new onAnimationLitener(v
                                .getId()));
                    }
                    if (v.getId() == searchLinearLayout.getId()) {
                        v.startAnimation(fade_in);
                        fade_in.setAnimationListener(new onAnimationLitener(v
                                .getId()));
                    }

                } catch (Exception e) {

                    // Case Signout
                }

                // END

            }

        }

    }

    private class onAnimationLitener implements AnimationListener {
        int id = 0;

        public onAnimationLitener(int id) {
            this.id = id;
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            if (AppMain.isphone) {
                // phone
                if (id == img_profile.getId()) {
                    if (Shared_Object.getCustomerDetail.getCID() > 0) {
                        // removeAccount();
                        // Log.i("", "facebook is login5");
                        dialogProfile_Show = new DialogProfile_Show(myActivity,
                                R.style.PauseDialog, fragment_Shelf);
                        dialogProfile_Show
                                .setOnCancelListener(new OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface v) {
                                        if (Shared_Object.getCustomerDetail
                                                .getCID() <= 0) {
                                            profilePhone();
                                            dialogLogin_Show.show();
                                            AppMain.pList_default_ebookshelf = null;
                                            arrayListRestores = null;

                                            bundle.putInt(
                                                    bundle_ebook_shelftype_phone,
                                                    btn_pnew.getId());
                                            if (bundle
                                                    .getInt(bundle_ebook_shelftype_phone) != 0) {
                                                setDefaultEnableType(bundle
                                                        .getInt(bundle_ebook_shelftype_phone));
                                            }
                                            arrDeletebyBID = null;
                                            pos = 1;
                                            StaticUtils.arrShilf.clear();
                                            adapter_List_BookShelf = null;
                                            listview_phone.setAdapter(null);

                                            StaticUtils.isAMasterDegree = false;
                                            StaticUtils.phoneValue = 0;
                                            StaticUtils.phonePage = 0;

                                            Log.i("", "facebook is login1");
                                            if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                                Log.i("", "facebook is login");
                                                StaticUtils
                                                        .setFacebooklogin(true);
                                            } else {
                                                Log.i("", "facebook not login");
                                                StaticUtils
                                                        .setFacebooklogin(false);
                                            }
                                        } else {

                                        }
                                    }
                                });
                        dialogProfile_Show.show();

                    } else {
                        dialogLogin_Show.show();
                    }
                }
                if (id == img_refresh.getId()) {
                    if (AppMain.pList_default_ebookshelf != null) {
                        AppMain.pList_default_ebookshelf = null;
                        arrayListRestores = null;

                        bundle.putInt(bundle_ebook_shelftype_phone,
                                btn_pnew.getId());

                        if (bundle.getInt(bundle_ebook_shelftype_phone) != 0) {
                            setDefaultEnableType(bundle
                                    .getInt(bundle_ebook_shelftype_phone));
                        }
                        arrDeletebyBID = null;
                        pos = 1;
                        StaticUtils.arrShilf.clear();

                        ebook_Shelf = new Ebook_Shelf(myActivity);
                        ebook_Shelf
                                .setOnListenerRestore(new ThrowModel_Restore() {

                                    @Override
                                    public void throwModel(
                                            ArrayList<Model_Restore> restore) {
                                        arrayListRestores = restore;
                                        for (Model_Restore model_Restore : restore) {
                                            Log.e("restore", "restore bid"
                                                    + model_Restore.getBID());
                                        }
                                    }
                                });
                        ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                            @Override
                            public void StartLoadPList() {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void PList_TopPeriod(PList Plist1,
                                                        PList Plist2, ProgressDialog pd) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void PList_Detail_Comment(PList Plistdetail,
                                                             PList Plistcomment, ProgressDialog pd) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void PList(final PList resultPlist,
                                              final ProgressDialog pd) {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        setListView(resultPlist, pd);
                                    }
                                }, 0);
                            }
                        });
                        ebook_Shelf.LoadEbooksTrashAPI();
                        ebook_Shelf.LoadEbooksDetailAPI();

                    }
                }
                if (id == img_peditsave.getId()) {
                    if (AppMain.pList_default_ebookshelf != null) {
                        if (isdelete) {
                            PhoneseteBookEdit();
                        } else {
                            PhoneseteBookSave();
                        }
                    }
                }
            } else {
                // tablet
                if (id == linear_refresh.getId()) {

                    refreshTablet();

                }
                // if (id == linear_editsave.getId()) {
                // // editsave
                // // open delete
                // if (AppMain.pList_default_ebookshelf != null) {
                // if (isdelete) {
                // seteBookEdit();
                // } else {
                // seteBookSave();
                // }
                // }
                //
                // }
                // STOU

                try {
                    if (id == editTextView.getId()) {
                        // editsave
                        // open delete
                        if (AppMain.pList_default_ebookshelf != null) {
                            if (isdelete) {
                                seteBookEdit();
                            } else {
                                seteBookSave();
                            }
                        }

                    }
                } catch (Exception e) {

                    // signout case
                }

                if (id == searchLinearLayout.getId()) {
                    viewSearchEbook();
                }

                // end

                if (id == linear_setting.getId()) {
                    // setting
                    viewSetShelf();
                }
                // if (id == linear_signout.getId()) {
                // // logout
                //
                // if (Shared_Object.getCustomerDetail.getCID() > 0) {
                // Shared_Object.getCustomerDetail = new
                // Model_Customer_Detail(null);
                // File customerFile = new File(myActivity.getFilesDir(), "/" +
                // "customer_detail.porar");
                // if (customerFile.exists()) {
                // customerFile.delete();
                // customerFile = null;
                //
                // dialogLogin_Show.show(); // default
                // clearCacheFacebookLogin(); // default
                //
                // if (adapterViewPager_BookShelf != null) {
                // myViewPager.removeAllViews(); // default
                // adapterViewPager_BookShelf = null; // default
                // }
                // if (editText.getEditableText() != null) {
                // if (editText.getEditableText().length() > 0) {
                // editText.setText("");
                // if (bundle.getString(serial_search) != null) {
                // bundle.putString(serial_search, "");
                // Log.e("", "bundle.getString(serial_search) clear");
                // }
                // if (MyAdapterViewPager_BookShelf.getEdittextsearch().length()
                // > 0) {
                // MyAdapterViewPager_BookShelf.setEdittextsearch("");
                // Log.e("",
                // "MyAdapterViewPager_BookShelf setEdittextsearch clear");
                // }
                // }
                // }
                // linear_signout.setEnabled(false); // default
                // try {
                // if (android.os.Build.VERSION.SDK_INT >=
                // android.os.Build.VERSION_CODES.HONEYCOMB) {
                // linear_signout.setAlpha(0.5F);
                // }
                // } catch (Exception e) {
                // // TODO: handle exception
                // } // default
                //
                // AppMain.pList_default_ebookshelf = null;// default
                // arrayListRestores = null;// default
                // bundle.putString(serial_type,
                // myActivity.getString(R.string.shelf_news)); // default //
                // instance state
                // bundle.putSerializable(Shelf_type, Shelf.news);// default //
                // instance state
                //
                // setProfileCustomer();// default
                // arrpublisher_Fans = null; // default
                // arrDeletebyBID = null;// default
                // pos = 1;// default
                // bundle.putInt(Shelf_Currunt, 0);
                // StaticUtils.arrShilf.clear();
                //
                // if (initializeActiveSessionWithCachedToken(myActivity)) {
                // Log.i("", "facebook is login");
                // StaticUtils.setFacebooklogin(true);
                // } else {
                // Log.i("", "facebook not login");
                // StaticUtils.setFacebooklogin(false);
                // }
                // }
                //
                // } else {
                //
                // }
                // }
                try {
                    if (id == signOutTextView.getId()) {
                        // logout

                        if (Shared_Object.getCustomerDetail.getCID() > 0) {
                            removeAccount();
                            Log.i("", "facebook is login2");
                            Shared_Object.getCustomerDetail = new Model_Customer_Detail(
                                    null);
                            File customerFile = new File(
                                    myActivity.getFilesDir(), "/"
                                    + "customer_detail.porar");
                            if (customerFile.exists()) {

                                customerFile.delete();
                                customerFile = null;
                                StaticUtils.isAMasterDegree = false;
                                StaticUtils.pageID = 0;
                                StaticUtils.shelfID = 0;
                                // StaticUtils.Login = 0;
                                dialogLogin_Show.show(); // default
                                clearCacheFacebookLogin(); // default

                                if (adapterViewPager_BookShelf != null) {
                                    myViewPager.removeAllViews(); // default
                                    adapterViewPager_BookShelf = null; // default

                                }

                                try {
                                    if (editText.getEditableText() != null) {
                                        if (editText.getEditableText().length() > 0) {
                                            editText.setText("");
                                            if (bundle.getString(serial_search) != null) {
                                                bundle.putString(serial_search,
                                                        "");
                                                Log.e("",
                                                        "bundle.getString(serial_search) clear");
                                            }
                                            if (MyAdapterViewPager_BookShelf
                                                    .getEdittextsearch()
                                                    .length() > 0) {
                                                MyAdapterViewPager_BookShelf
                                                        .setEdittextsearch("");
                                                Log.e("",
                                                        "MyAdapterViewPager_BookShelf setEdittextsearch clear");
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                    // never Click search

                                }

                                // linear_signout.setEnabled(false); // default
                                // try {
                                // if (android.os.Build.VERSION.SDK_INT >=
                                // android.os.Build.VERSION_CODES.HONEYCOMB) {
                                // linear_signout.setAlpha(0.5F);
                                // }
                                // } catch (Exception e) {
                                // TODO: handle exception
                                // } // default

                                AppMain.pList_default_ebookshelf = null;// default
                                arrayListRestores = null;// default
                                bundle.putString(serial_type, myActivity
                                        .getString(R.string.shelf_news)); // default
                                // //
                                // instance
                                // state
                                bundle.putSerializable(Shelf_type, Shelf.news);// default
                                // //
                                // instance
                                // state

                                setProfileCustomer();// default
                                // arrpublisher_Fans = null; // default
                                arrDeletebyBID = null;// default
                                pos = 1;// default
                                bundle.putInt(Shelf_Currunt, 0);
                                StaticUtils.arrShilf.clear();
                                StaticUtils.isAMasterDegree = false;
                                StaticUtils.isBechalorDegree = false;
                                StaticUtils.phoneValue = 0;
                                StaticUtils.phonePage = 0;
                                // Log.i("", "facebook is login3");
                                // removeAccount();
                                Shared_Object.getCustomerDetail
                                        .setMasterDegree(0);
                                Shared_Object.getCustomerDetail
                                        .setBechalorDegree(0);
                                if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
                                    Log.i("", "facebook is login");
                                    StaticUtils.setFacebooklogin(true);
                                } else {
                                    Log.i("", "facebook not login");
                                    StaticUtils.setFacebooklogin(false);
                                }
                            }

                        } else {

                        }
                    }
                } catch (Exception e) {

                    // sign out case
                }

            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
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
            }
        });
        apiResultString.execute(registerURL);

    }

    private void viewSetShelf() {
        view_Setting = new View_Setting(myActivity, linear_customforTabview,
                rt_dummyscreen, rt_head.getId()) {

            @Override
            public void onClickSrceen(RelativeLayout dummyscreen,
                                      LinearLayout layout_fortabber, LinearLayout layout_search) {
                for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
                    if (layout_fortabber.getChildAt(i).equals(layout_search)) {
                        layout_fortabber.removeViewAt(i);
                        dummyscreen.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onChangeBackgroundStyle(int drawableStyle) {
                ((RelativeLayout) getActivity().findViewById(
                        R.id.shelf_mainlayout))
                        .setBackgroundResource(drawableStyle);
            }

            @Override
            public void onChangeShelfStyle(int drawableStyle) {
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf1))
                        .setImageResource(drawableStyle);
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf2))
                        .setImageResource(drawableStyle);
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf3))
                        .setImageResource(drawableStyle);
                ((ImageView) getActivity().findViewById(R.id.shelf_img_shelf4))
                        .setImageResource(drawableStyle);

            }
        };
        view_Setting.initView();
    }

    private void viewSearchEbook() {
        new View_SearchEbookShelf(myActivity, linear_customforTabview,
                rt_dummyscreen, rt_head.getId(), searchEditTextSaveInstance) {

            @Override
            public void onFinishInflateSearchBookshelf(EditText searchEditText) {

                editText = searchEditText;
                editText.addTextChangedListener(textWatcher);

                // //Show soft keyboard
                // InputMethodManager imm =
                // (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.showSoftInput(editText,
                // InputMethodManager.SHOW_IMPLICIT);

            }

            @Override
            public void onClickSrceen(RelativeLayout dummyscreen,
                                      LinearLayout layout_fortabber, LinearLayout layout_search) {

                // unlock screen and remove
                for (int i = 0; i < layout_fortabber.getChildCount(); i++) {
                    if (layout_fortabber.getChildAt(i).equals(layout_search)) {
                        layout_fortabber.removeViewAt(i);
                        // layout_fortabber.removeAllViews();
                        dummyscreen.setVisibility(View.INVISIBLE);
                    }
                }

                searchEditTextSaveInstance = editText.getText().toString();

            }
        };
    }

    private void PhoneseteBookEdit() {
        try {
            if (adapter_List_BookShelf != null) {
                adapter_List_BookShelf.setDeleteBook(true);
            }
            if (adapter_List_BookShelf.isDeleteBook()) {
                listview_phone.setAdapter(adapter_List_BookShelf);
                isdelete = false;
                img_peditsave.setImageResource(R.drawable.content_save);
            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    private void refreshTablet() {
        if (AppMain.pList_default_ebookshelf != null) {

            bundle.putString(serial_type,
                    myActivity.getString(R.string.shelf_news));
            if (bundle.getString(serial_type) != null) {
                // btn_type.setText(bundle.getString(serial_type));
                setEnableMenu(bundle.getString(serial_type));
            }
            bundle.putSerializable(Shelf_type, Shelf.news);
            if (bundle.getSerializable(Shelf_type) != null) {
                MyAdapterViewPager_BookShelf.seteNumtype((Shelf) bundle
                        .getSerializable(Shelf_type));
            }
            bundle.putInt(Shelf_Currunt, 0);
            arrDeletebyBID = null;
            pos = 1;
            StaticUtils.arrShilf.clear();

            try {
                if (editText.getEditableText() != null) {
                    if (editText.getEditableText().length() > 0) {
                        editText.setText("");
                        if (bundle.getString(serial_search) != null) {
                            bundle.putString(serial_search, "");
                            Log.e("", "bundle.getString(serial_search) clear");
                        }
                        if (MyAdapterViewPager_BookShelf.getEdittextsearch()
                                .length() > 0) {
                            MyAdapterViewPager_BookShelf.setEdittextsearch("");
                            Log.e("",
                                    "MyAdapterViewPager_BookShelf setEdittextsearch clear");
                        }
                    }
                }
            } catch (Exception e) {

                // case never click search
            }

            ebook_Shelf = new Ebook_Shelf(myActivity);
            ebook_Shelf.setOnListenerRestore(new ThrowModel_Restore() {

                @Override
                public void throwModel(ArrayList<Model_Restore> restore) {
                    arrayListRestores = restore;
                    for (Model_Restore model_Restore : restore) {
                        Log.e("restore", "restore bid" + model_Restore.getBID());
                    }
                }
            });
            ebook_Shelf.setOnListener(new Throw_IntefacePlist() {

                @Override
                public void StartLoadPList() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void PList_TopPeriod(PList Plist1, PList Plist2,
                                            ProgressDialog pd) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void PList_Detail_Comment(PList Plistdetail,
                                                 PList Plistcomment, ProgressDialog pd) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void PList(final PList resultPlist,
                                  final ProgressDialog pd) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            setViewPager(resultPlist, pd);
                        }
                    }, 1000);

                    setProfileCustomer();
                    // setViewPager(resultPlist, pd);
                    // setProfileCustomer();
                }
            });
            ebook_Shelf.LoadEbooksTrashAPI();
            ebook_Shelf.LoadEbooksDetailAPI();

        }

    }

    private void PhoneseteBookSave() {
        try {
            if (adapter_List_BookShelf != null) {
                adapter_List_BookShelf.setDeleteBook(false);
            }
            if (!adapter_List_BookShelf.isDeleteBook()) {
                listview_phone.setAdapter(adapter_List_BookShelf);
                isdelete = true;
                img_peditsave.setImageResource(R.drawable.content_discard);
            }
            if (arrDeletebyBID == null) {
                if (!adapterViewPager_BookShelf.isDeleteBook()) {
                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                }
                myViewPager.setCurrentItem(bundle.getInt(Shelf_Currunt));// check
                // size
                // bid
            } else {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);

                switch (Adapter_List_BookShelf.shelf) {
                    case favorite:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=0";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(myActivity,
                                                                "delete to trash",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        img_refresh.performClick();
                                                    }
                                                    pos++;

                                                }
                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }
                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;
                    case news:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=0";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(myActivity,
                                                                "delete to trash",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        img_refresh.performClick();
                                                    }

                                                    pos++;

                                                }
                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }

                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;
                    case tresh:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=1";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(
                                                                myActivity,
                                                                "delete to complete",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        img_refresh.performClick();
                                                    }

                                                    pos++;

                                                }

                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }

                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;

                }

            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    private void seteBookEdit() {
        try {
            if (adapterViewPager_BookShelf != null) {
                adapterViewPager_BookShelf.setDeleteBook(true);
            }
            if (adapterViewPager_BookShelf.isDeleteBook()) {
                myViewPager.setAdapter(adapterViewPager_BookShelf);
            }
            if (adapterViewPager_BookShelf.isDeleteBook()) {
                // image_editsave.setImageResource(R.drawable.icon_save2x);
                // txt_editsave.setText("Save");
                editTextView.setText(getResources().getString(R.string.record_book_shelf));
                isdelete = false;
            } else {
                // image_editsave.setImageResource(R.drawable.icon_edit2x);
                // txt_editsave.setText(" Edit ");
                editTextView.setText(getResources().getString(R.string.deleted_book_shelf));
                isdelete = true;
            }
            myViewPager.setCurrentItem(bundle.getInt(Shelf_Currunt));

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

    }

    private void seteBookSave() {
        try {
            if (adapterViewPager_BookShelf != null) {
                adapterViewPager_BookShelf.setDeleteBook(false);
            }
            if (adapterViewPager_BookShelf.isDeleteBook()) {
                // image_editsave.setImageResource(R.drawable.icon_save2x);
                // txt_editsave.setText("Save");
                editTextView.setText(getResources().getString(R.string.record_book_shelf));
                isdelete = false;
            } else {
                // image_editsave.setImageResource(R.drawable.icon_edit2x);
                // txt_editsave.setText(" Edit ");
                editTextView.setText(getResources().getString(R.string.deleted_book_shelf));
                isdelete = true;
            }

            if (arrDeletebyBID == null) {
                if (!adapterViewPager_BookShelf.isDeleteBook()) {
                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                }
                myViewPager.setCurrentItem(bundle.getInt(Shelf_Currunt));// check
                // size
                // bid
            } else {
                alertDialog = new AlertDialog.Builder(myActivity).create();
                TextView msg = new TextView(myActivity);
                msg.setText("Please Wait... Loading");
                msg.setPadding(10, 10, 10, 10);
                msg.setGravity(Gravity.CENTER);
                alertDialog.setView(msg);

                switch (MyAdapterViewPager_BookShelf.shelf) {
                    case favorite:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=0";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(myActivity,
                                                                "delete to trash",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        linear_refresh
                                                                .performClick();
                                                    }
                                                    pos++;

                                                }
                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }
                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;
                    case news:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=0";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(myActivity,
                                                                "delete to trash",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        linear_refresh
                                                                .performClick();
                                                    }

                                                    pos++;

                                                }
                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }

                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;
                    case tresh:
                        alertDialog.show();
                        for (Integer each : arrDeletebyBID) {
                            // String url =
                            // "http://api.ebooks.in.th/deletebookshelf.ashx?";
                            String url = AppMain.DELETE_BOOK_SHELF_URL;
                            url += "bid=" + each;
                            url += "&cid="
                                    + Shared_Object.getCustomerDetail.getCID();
                            url += "&confirm=1";
                            apiResultString = new LoadAPIResultString();
                            apiResultString
                                    .setOnLoadResultMethodGet(new OnLoadResultFromHttpGet() {

                                        @Override
                                        public void completeResult(String result) {
                                            try {
                                                if (result.contains("1")) {
                                                    handler.removeCallbacks(runnable);
                                                    handler.postDelayed(runnable,
                                                            POST_DELEY);

                                                    Log.e("", "pos" + pos);
                                                    if (pos == arrDeletebyBID
                                                            .size()) {
                                                        handler.removeCallbacks(runnable);

                                                        if (alertDialog.isShowing()) {
                                                            alertDialog.dismiss();
                                                        }
                                                        Toast.makeText(
                                                                myActivity,
                                                                "delete to complete",
                                                                Toast.LENGTH_SHORT).show();
                                                        arrDeletebyBID = null;
                                                        pos = 1;
                                                        linear_refresh
                                                                .performClick();
                                                    }

                                                    pos++;

                                                }

                                            } catch (NullPointerException e) {
                                                if (alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(
                                                        myActivity,
                                                        "WARNING: An error has ocurred. Delete item "
                                                                + pos + " each",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(myActivity,
                                                        "WARNING: Please Refresh.",
                                                        Toast.LENGTH_SHORT).show();
                                                apiResultString.cancel(true);
                                            }

                                        }
                                    });
                            apiResultString.execute(url);
                            Class_Manage.deleteEbooksCache(myActivity, each);
                        }

                        break;

                }

            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

        // isdelete = true;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            Toast.makeText(
                    myActivity,
                    "WARNING: An error has ocurred. Delete item " + pos
                            + " each", Toast.LENGTH_SHORT).show();
            Toast.makeText(myActivity, "WARNING: Please Refresh.", Toast.LENGTH_SHORT)
                    .show();
            apiResultString.cancel(true);

        }
    };


    private void clearCacheFacebookLogin() {
        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("") && !Shared_Object.getCustomerDetail.getFacebookId().equals("0")) {
            RegisterFacebook.FacebookLogout();
        }
        RegisterFacebook.resetValue();
    }


    @Override
    public void onDestroy() {
        RegisterFacebook.unRegisterFacebook(getActivity());
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
        if (requestCode == 2013) {
            if (AppMain.isphone) {
                if (data != null) {
                    int pageindex = Integer.parseInt(data.getExtras()
                            .getString("pointer"));
                    int bid = Integer.parseInt(data.getExtras()
                            .getString("bid"));

                    StaticUtils.setBID(bid, pageindex);

                    listview_phone.setAdapter(adapter_List_BookShelf);
                }
            } else {
                if (data != null) {
                    int pageindex = Integer.parseInt(data.getExtras()
                            .getString("pointer"));
                    int bid = Integer.parseInt(data.getExtras()
                            .getString("bid"));

                    StaticUtils.setBID(bid, pageindex);

                    myViewPager.setAdapter(adapterViewPager_BookShelf);
                }
            }

        }

    }

    private void setPage() {
        if (StaticUtils.pageID == 0) {

            if (!StaticUtils.isAMasterDegree) {
                linear_setting.setVisibility(View.VISIBLE);
                Log.e(tag, "ID" + StaticUtils.isAMasterDegree);
                rt_head.setBackgroundColor(getResources().getColor(
                        R.color.green_stou));
            } else {
                try {
                    linear_setting.setVisibility(View.VISIBLE);
                    rt_head.setBackgroundColor(getResources().getColor(R.color.orange_stou));
                    setShelf();
                    setBackground();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        } else if (StaticUtils.pageID == 1) {

            if (!StaticUtils.isAMasterDegree) {
                linear_setting.setVisibility(View.VISIBLE);
                Log.e(tag, "ID" + StaticUtils.isAMasterDegree);
                rt_head.setBackgroundColor(getResources().getColor(
                        R.color.green_stou));
            } else {
                try {

                    linear_setting.setVisibility(View.VISIBLE);
                    rt_head.setBackgroundColor(getResources().getColor(
                            R.color.orange_stou));
                    setShelf();
                    setBackground();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        } else {
            setBackground();
            setShelf();
            linear_setting.setVisibility(View.GONE);
            rt_head.setBackgroundColor(getResources().getColor(
                    R.color.orange_stou));

        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // stou
        if (AppMain.isphone) {
            img_refresh.performClick();
        } else {
            linear_refresh.performClick();
            setPage();

        }
    }
}
