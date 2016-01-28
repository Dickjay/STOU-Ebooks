package com.porar.ebooks.stou.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.porar.ebook.adapter.AdapterGalleryBanner;
import com.porar.ebook.adapter.AdapterGalleryBannerMaster;
import com.porar.ebook.adapter.AdapterSpinnerCategory;
import com.porar.ebook.adapter.AdapterSpinnerCategoryMaster;
import com.porar.ebook.adapter.Adapter_List_Ebook;
import com.porar.ebook.adapter.Adapter_List_Ebook_Master;
import com.porar.ebook.adapter.Adapter_List_Ebook_MasterDetail;
import com.porar.ebook.adapter.Adapter_SearchList_Ebook;
import com.porar.ebook.adapter.MyAdapterViewPager_Book;
import com.porar.ebook.adapter.MyAdapterViewPager_Book_Master;
import com.porar.ebook.adapter.MyAdapterViewPager_Book_MasterDetail;
import com.porar.ebook.control.Category_Ebook.Page;
import com.porar.ebook.control.Category_EbookOfFragment;
import com.porar.ebook.control.Category_SubCate_Ebookslist;
import com.porar.ebook.control.DialogLogin_Master_STOU;
import com.porar.ebook.control.Dialog_Show_Banner;
import com.porar.ebook.control.Dot_Pageslide;
import com.porar.ebook.control.LoadAPIResultString;
import com.porar.ebook.control.MyViewPager;
import com.porar.ebook.control.Refresh_Ebook;
import com.porar.ebook.control.SaveLastUrl;
import com.porar.ebook.control.Throw_IntefacePlist;
import com.porar.ebook.control.View_SearchEbook;
import com.porar.ebook.control.View_Setting;
import com.porar.ebook.control.View_TypeEbook;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.event.OnLoadResultFromHttpGet;
import com.porar.ebooks.model.Model_Banner;
import com.porar.ebooks.model.Model_Banner_Master;
import com.porar.ebooks.model.Model_Book_Master;
import com.porar.ebooks.model.Model_Cat;
import com.porar.ebooks.model.Model_Categories;
import com.porar.ebooks.model.Model_Setting;
import com.porar.ebooks.model.Model_SettingShelf;
import com.porar.ebooks.stou.activity.Activity_Tab;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.Class_Manage;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.ImageDownloader_forCache;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

public class Fragment_Ebook extends Fragment {
    private Gallery gallery;
    private Gallery gallery2;
    private ImageView img_previous, img_next;
    private ArrayList<Model_Banner> arrayBanners_banner;
    private ArrayList<Model_Banner_Master> arrayBanners_banner_master;
    private int pageGallery = 0;
    private int pageGallery2 = 0;
    private final Handler handler = new Handler();
    private final int scrollDelay = 5000;
    private MyViewPager myViewPager;
    public ProgressDialog progressDialog = null;
    // control
    private TextView txt_refresh, txt_search, txt_setting;
    private ImageView image_refresh, image_search, image_setting;
    private LinearLayout linear_refresh, linear_search, linear_setting,
            linear_customforTabview, linear_dotpage;
    private RelativeLayout rt_dummyscreen, rt_head;
    // btn
    private Button btn_category, btn_type, btn_univer, btn_content;
    private Spinner spinner_category;// spinner_type;
    // private CustomSpinner spinner_category;// spinner_type;
    // animation
    private Animation fade_in;
    // Class
    private View_TypeEbook view_TypeEbook;
    private MyAdapterViewPager_Book adapterViewPager_Book;
    private MyAdapterViewPager_Book_Master adapterViewPager_Book_Master;
    private MyAdapterViewPager_Book_MasterDetail adapterViewPager_Book_MasterDetail;
    private Refresh_Ebook refresh_Ebook;
    private Category_EbookOfFragment category_Ebook;
    private ArrayList<Model_Categories> arr_Categories;

    // stou
    private ArrayList<Model_Categories> arr_Categories2;
    private ArrayList<Model_Categories> arr_Categories3;
    // end
    public static SaveLastUrl lastUrl = null;
    public String type_ebook = "";
    public String catid = "";
    private Dot_Pageslide dot_Pageslide;
    private View_SearchEbook view_SearchEbook;
    private View_Setting view_Setting;
    private AlertDialog alertDialog;
    private Adapter_SearchList_Ebook adapter_SearchList;
    Bundle bundle;
    public static Fragment_Ebook fragment_ebook;
    public static String serial_category = "serial_category";
    public static String serial_type = "serial_type";
    public static String serial_univer = "serial_univer";
    public FragmentActivity myActivity;
    public static String tag = "Fragment_Ebook";
    private DialogLogin_Master_STOU dialogLogin_Show;
    // :: version.phone :: Object
    ListView plistview;
    ImageView picon_refresh, picon_search, ebook_image_back;
    Button pbtn_new, pbtn_hot, pbtn_univer, pbtn_content/*
                                                         * , pbtn_free,
														 * pbtn_sales
														 */;
    Button[] btn_array = {pbtn_new, pbtn_hot /* , pbtn_free, pbtn_sales */};
    String[] str_type = {"N", "R", "F", "P"};
    Adapter_List_Ebook adapter_List_Ebook;
    Adapter_List_Ebook_Master adapter_List_Ebook_Master;
    public static String bundle_ebook_type_phone = "bundle_ebook_type_phone";
    public Fragment frag_search = null;
    public boolean HaveFragment = false;
    private Spinner spinner_category2, spinner_univer, spinner_content;
    LinearLayout linearLayoutUniver, linearLayoutContent, linearLayoutNews,
            linearLayoutRecommend;
    RelativeLayout relativeLayoutUniver, relativeLayoutContent,
            relativeLayoutType, relativeLayoutCat;

    ArrayList<Model_Book_Master> arr_Book_Masters;

    ArrayList<Model_Book_Master> arr_Book_Masters0;
    ArrayList<Model_Book_Master> arr_Book_Masters1;
    String params;
    ImageView ebook_icon_back;
    public static String sCatIDs = "", bCode = "";
    public static boolean isSelectedMaster = false;

    private static String selectedName = "";
    public static boolean isChoice = false;

    public static boolean isTabletChoose = false;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        RegisterFacebook.InitFacebookSDK(getActivity());
        Log.e(tag, "onCreate");
        CreateActivityTask createActivityTask = new CreateActivityTask();
        createActivityTask.execute();
        if (AppMain.isphone) {

            fragment_ebook = this;
            Activity_Tab.ebook = Fragment_Ebook.this;
        } else {
            fragment_ebook = this;
            Activity_Tab.ebook = Fragment_Ebook.this;
        }
    }


    class CreateActivityTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (AppMain.isphone) {
                Log.e(tag, "onCreate isphone");
                bundle = new Bundle();
                myActivity = getActivity();
                lastUrl = new SaveLastUrl();

                HaveFragment = false;

            } else {
                bundle = new Bundle();
                myActivity = getActivity();
                lastUrl = new SaveLastUrl();
            }

            return true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(tag, "onCreateView");
        View view = inflater.inflate(R.layout.activity_ebook, container, false);

        // RelativeLayout layout_main = (RelativeLayout)
        // getActivity().findViewById(R.id.ebook_rt_main_phone);
        if (AppMain.isphone) {

            CreateCatagoriesTask catagoriesTask = new CreateCatagoriesTask();
            catagoriesTask.execute();
            setId_Phone(view);

            if (bundle.getInt(bundle_ebook_type_phone) != 0) {
                setDefaultEnableType(bundle.getInt(bundle_ebook_type_phone));
            }

            dialogLogin_Show = new DialogLogin_Master_STOU(myActivity,
                    R.style.PauseDialog, fragment_ebook);
            // dialogLogin_Show.setCancelable(false);

            dialogLogin_Show.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                }
            });// End of dialogShow.Cancel

            // stou

        } else {
            CreateBannerTask banneTask = new CreateBannerTask();
            banneTask.execute();
            CreateBannerMasterTask banneMasterTask = new CreateBannerMasterTask();
            banneMasterTask.execute();
            setId(view);

            dialogLogin_Show = new DialogLogin_Master_STOU(myActivity,
                    R.style.PauseDialog, fragment_ebook);
            // dialogLogin_Show.setCancelable(false);

            dialogLogin_Show.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                }
            });// End of dialogShow.Cancel

            // snuxker
            setSpinnerAdapter();
            setBannerGallery();
            setOnclick();

            // get InstanceState
            if (bundle.getString(serial_category) != null) {
                btn_category.setText(bundle.getString(serial_category));
            }
            if (bundle.getString(serial_type) != null) {
                btn_type.setText(bundle.getString(serial_type));
            }
            if (bundle.getString(serial_univer) != null) {
                btn_univer.setText(bundle.getString(serial_univer));
            }
        }

        return view;
    }

    class CreateCatagoriesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            setCatagoriesDialog();

            return true;
        }

    }

    private void setCatagoriesDialog() {

        addCatagoriesToArraylist1();
        addCatagoriesToArraylist2();
        addCatagoriesToArraylist3();

    }

    private void showCatagoriesDialog(final int catagories) {

        CatagoriesAdapter catagoriesAdapter = null;

        switch (catagories) {
            case 1:
                catagoriesAdapter = new CatagoriesAdapter(getActivity(),
                        arr_Categories);
                break;

            case 2:

                catagoriesAdapter = new CatagoriesAdapter(getActivity(),
                        arr_Categories2);

                break;
            case 3:

                catagoriesAdapter = new CatagoriesAdapter(getActivity(),
                        arr_Categories3);

                break;
            default:
                break;
        }

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());

        builderSingle.setAdapter(catagoriesAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        // Model_Categories strName =
                        // arrayAdapter.getItem(position);

                        switch (catagories) {
                            case 1:
                                catid = String.valueOf(arr_Categories.get(position)
                                        .getCatID());
                                setTextCategory(1, arr_Categories.get(position)
                                        .getName());
                                AppMain.pList_default_ebook = null;

                                if (position == 0) {

                                    loadApiBySubCateEbookFirstPosition(catid,
                                            type_ebook, Page.EBOOK_FRAGMENT);

                                } else {

                                    int sCatID = arr_Categories.get(position)
                                            .getSCatID();
                                    int catID = arr_Categories.get(position)
                                            .getCatID();

                                    loadApiBySubCateTypeEbook(catID, sCatID);
                                }
                                StaticUtils.phonePage = 1;
                                break;

                            case 2:

                                catid = String.valueOf(arr_Categories2
                                        .get(position).getCatID());
                                setTextCategory(2, arr_Categories2.get(position)
                                        .getName());
                                AppMain.pList_default_ebook = null;

                                if (position == 0) {

                                    loadApiBySubCateEbookFirstPosition(catid,
                                            type_ebook, Page.EBOOK_FRAGMENT);

                                } else {

                                    int sCatID = arr_Categories2.get(position)
                                            .getSCatID();
                                    int catID = arr_Categories2.get(position)
                                            .getCatID();

                                    loadApiBySubCateTypeEbook(catID, sCatID);

                                }
                                StaticUtils.phonePage = 1;
                                break;

                            case 3:

                                catid = String.valueOf(arr_Categories3
                                        .get(position).getCatID());
                                setTextCategory(3, arr_Categories3.get(position)
                                        .getName());
                                AppMain.pList_default_ebook = null;

                                if (position == 0) {

                                    loadApiBySubCateEbookFirstPosition(catid,
                                            type_ebook, Page.EBOOK_FRAGMENT);

                                } else {

                                    int sCatID = arr_Categories3.get(position)
                                            .getSCatID();
                                    int catID = arr_Categories3.get(position)
                                            .getCatID();

                                    loadApiBySubCateTypeEbook(catID, sCatID);

                                }
                                break;
                            default:
                                break;
                        }

                    }

                    private void setTextCategory(int fromCategories, String name) {

                        // stou
                        switch (fromCategories) {
                            case 1:

                                pbtn_new.setText(name);
                                bundle.putString(serial_category, name);

                                break;

                            case 2:

                                pbtn_hot.setText(name);
                                bundle.putString(serial_type, name);

                                break;

                            case 3:

                                pbtn_univer.setText(name);
                                bundle.putString(serial_univer, name);

                                break;
                            default:
                                break;
                        }

                    }
                });
        builderSingle.show();

    }

    private void showCatagoriesDialogMaster(final int catagories) {

        CatagoriesAdapterMaster catagoriesAdapter = null;

        switch (catagories) {
            case 0:
                catagoriesAdapter = new CatagoriesAdapterMaster(getActivity(),
                        arr_Book_Masters0);
                break;

            case 1:

                catagoriesAdapter = new CatagoriesAdapterMaster(getActivity(),
                        arr_Book_Masters1);

                break;

            default:
                break;
        }

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());

        builderSingle.setAdapter(catagoriesAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        // Model_Categories strName =
                        // arrayAdapter.getItem(position);

                        switch (catagories) {
                            case 0:
                                isChoice = true;
                                sCatIDs = arr_Book_Masters0.get(position)
                                        .getMCatID();
                                bCode = arr_Book_Masters0.get(position).getBCode();

                                getSubCatMasterDegree(sCatIDs, bCode);
                                selectedName = arr_Book_Masters0.get(position)
                                        .getBookName();
                                pbtn_univer.setText(selectedName);
                                break;

                            case 1:
                                isChoice = true;
                                sCatIDs = arr_Book_Masters1.get(position)
                                        .getMCatID();
                                bCode = arr_Book_Masters1.get(position).getBCode();

                                getSubCatMasterDegree(sCatIDs, bCode);
                                selectedName = arr_Book_Masters1.get(position)
                                        .getBookName();
                                pbtn_content.setText(selectedName);
                                break;

                            default:
                                break;
                        }

                    }

                });
        builderSingle.show();

    }

    private void setListView(PList plist, ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {
            Log.i(tag, "plist " + plist.toString());

            adapter_List_Ebook = null;
            adapter_List_Ebook = new Adapter_List_Ebook(myActivity, 0, plist);
            plistview.setAdapter(adapter_List_Ebook);

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
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public void setListViewMaster(PList plist, ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {
            Log.i(tag, "plist " + plist.toString());

            adapter_List_Ebook_Master = null;
            adapter_List_Ebook_Master = new Adapter_List_Ebook_Master(
                    myActivity, 0, plist, Fragment_Ebook.this);
            plistview.setAdapter(adapter_List_Ebook_Master);

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
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setListViewMasterDetail(PList plistDetail, ProgressDialog dialog) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                plistview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Older OS, No HW acceleration anyway",
                    Toast.LENGTH_LONG).show();
        }
        try {
            Log.i(tag, "plist " + plistDetail.toString());

            Adapter_List_Ebook_MasterDetail adapter_List_Ebook_MasterDetail = null;
            adapter_List_Ebook_MasterDetail = new Adapter_List_Ebook_MasterDetail(
                    myActivity, 0, plistDetail);
            plistview.setAdapter(adapter_List_Ebook_MasterDetail);

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
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void setId_Phone(View getView) {
        linearLayoutNews = (LinearLayout) getView
                .findViewById(R.id.linear_news);
        linearLayoutRecommend = (LinearLayout) getView
                .findViewById(R.id.linear_recommend);
        linearLayoutContent = (LinearLayout) getView
                .findViewById(R.id.linear_content);
        linearLayoutContent = (LinearLayout) getView
                .findViewById(R.id.linear_content);
        linearLayoutUniver = (LinearLayout) getView
                .findViewById(R.id.linear_univer);
        plistview = (ListView) getView.findViewById(R.id.ebook_plistview);
        picon_refresh = (ImageView) getView
                .findViewById(R.id.ebook_image_prefresh);
        picon_search = (ImageView) getView
                .findViewById(R.id.ebook_image_psearch);

        ebook_image_back = (ImageView) getView
                .findViewById(R.id.ebook_image_back);
        pbtn_new = (Button) getView.findViewById(R.id.tabview_btn_pnews);
        pbtn_hot = (Button) getView.findViewById(R.id.tabview_btn_precommend);
        pbtn_univer = (Button) getView.findViewById(R.id.tabview_btn_puniver);
        pbtn_content = (Button) getView.findViewById(R.id.tabview_btn_pcontent);

        // pbtn_free = (Button)
        // getActivity().findViewById(R.id.tabview_btn_pfree);
        // pbtn_sales = (Button)
        // getActivity().findViewById(R.id.tabview_btn_psales);
        fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
        picon_refresh.setOnClickListener(new OnClickControl());
        picon_search.setOnClickListener(new OnClickControl());
        ebook_image_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Fragment_Ebook.isChoice) {

                    Fragment_Ebook.isChoice = false;
                    Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
                    Fragment_Ebook.sCatIDs = "";
                    Fragment_Ebook.bCode = "";
                    if (Adapter_List_Ebook_MasterDetail.phoneToDetail) {
                        getSubCatMasterDegree(sCatIDs, bCode);
                    } else {
                        setListViewMaster(AppMain.pList_default_ebook_master,
                                null);
                    }
                    return;
                } else {
                    getFragmentManager().popBackStack();
                }

            }
        });

        // snuxker
        pbtn_content.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_HelvethaicaMon_X));
        pbtn_new.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_HelvethaicaMon_X));
        pbtn_hot.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_HelvethaicaMon_X));
        pbtn_univer.setTypeface(StaticUtils.getTypeface(myActivity,
                Font.DB_HelvethaicaMon_X));
        String title = getResources().getString(R.string.guideline_education);
        pbtn_content.setText(title);

        try {
            checkIDPhone();
            setOnClickButton();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void checkIDPhone() throws NullPointerException {
        try {

            if (!StaticUtils.isAMasterDegree) {
                linearLayoutUniver.setVisibility(View.VISIBLE);
            } else {
                linearLayoutUniver.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void setOnClickButton() throws RuntimeException {
        pbtn_new.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle.putInt(bundle_ebook_type_phone, v.getId());
                // setDefaultEnableType(v.getId());
                // loadApiByType("N");
                showCatagoriesDialog(1);
            }
        });
        pbtn_hot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle.putInt(bundle_ebook_type_phone, v.getId());
                // setDefaultEnableType(v.getId());
                // loadApiByType("R");

                showCatagoriesDialog(2);

            }

        });

        pbtn_univer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle.putInt(bundle_ebook_type_phone, v.getId());
                // setDefaultEnableType(v.getId());
                // loadApiByType("R");

                showCatagoriesDialogMaster(0);

            }

        });
        pbtn_content.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle.putInt(bundle_ebook_type_phone, v.getId());
                // setDefaultEnableType(v.getId());
                // loadApiByType("R");

                showCatagoriesDialogMaster(1);

            }

        });

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

        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("0") && !Shared_Object.getCustomerDetail.getFacebookId().equals("")) {
            RegisterFacebook.FacebookLogout();
        }
        StaticUtils.Login = 0;
        StaticUtils.isAMasterDegree = false;
        StaticUtils.shelfID = 0;
        StaticUtils.pageID = 0;
        StaticUtils.phonePage = 0;
        StaticUtils.phoneValue = 0;
    }


    @SuppressWarnings("unused")
    private void loadApiByType(final String type_ebook) {
        AppMain.pList_default_ebook = null;
        category_Ebook = new Category_EbookOfFragment(myActivity, catid,
                type_ebook, Page.EBOOK_FRAGMENT);
        category_Ebook.setOnListener(new Throw_IntefacePlist() {

            @Override
            public void PList(PList resultPlist, ProgressDialog pd) {
                setListView(resultPlist, pd);

            }

            @Override
            public void PList_Detail_Comment(plist.xml.PList Plistdetail,
                                             plist.xml.PList Plistcomment, ProgressDialog pd) {
                // TODO Auto-generated method stub
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
        category_Ebook.LoadCategoryAPI();

    }

    private void setDefaultEnableType(int id) {/*
                                                 *
												 * pbtn_new.setEnabled(true);
												 * pbtn_hot.setEnabled(true); //
												 * pbtn_free.setEnabled(true);
												 * //
												 * pbtn_sales.setEnabled(true);
												 * 
												 * if (id == pbtn_new.getId()) {
												 * pbtn_new.setEnabled(false); }
												 * if (id == pbtn_hot.getId()) {
												 * pbtn_hot.setEnabled(false); }
												 * // if (id ==
												 * pbtn_free.getId()) { //
												 * pbtn_free.setEnabled(false);
												 * // } // if (id ==
												 * pbtn_sales.getId()) { //
												 * pbtn_sales.setEnabled(false);
												 * // }
												 */
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
            adapterViewPager_Book = null;
            adapterViewPager_Book = new MyAdapterViewPager_Book(myActivity,
                    pList);

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_Book.getCount()));
                    myViewPager.setAdapter(adapterViewPager_Book);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
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
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }

    }

    public void setViewPagerMaster(final PList pList,
                                   final ProgressDialog dialog) { // For Tablet

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
            adapterViewPager_Book_Master = null;
            adapterViewPager_Book_Master = new MyAdapterViewPager_Book_Master(
                    myActivity, pList, arr_Book_Masters, Fragment_Ebook.this);

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_Book_Master
                                    .getCount()));
                    myViewPager.setAdapter(adapterViewPager_Book_Master);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
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
                            if (adapterViewPager_Book_Master != null) {
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
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        alertDialog.dismiss();
    }

    public void setViewPagerMasterDetail(final PList pList,
                                         final ProgressDialog dialog) { // For Tablet

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
            adapterViewPager_Book_MasterDetail = null;
            adapterViewPager_Book_MasterDetail = new MyAdapterViewPager_Book_MasterDetail(
                    myActivity, pList);

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeViewDotPage();

                    dot_Pageslide = new Dot_Pageslide(myActivity);
                    linear_dotpage.addView(dot_Pageslide
                            .setImage_slide(adapterViewPager_Book_MasterDetail
                                    .getCount()));
                    myViewPager.setAdapter(adapterViewPager_Book_MasterDetail);
                    dot_Pageslide.setHeighlight(myViewPager.getCurrentItem());
                    myViewPager
                            .setOnPageChangeListener(new OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    dot_Pageslide.setHeighlight(position);
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
                            if (adapterViewPager_Book_Master != null) {
                                dialog.dismiss();
                            }
                        }
                    }

                }
            }, 50);

            //

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
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    private void removeViewDotPage() {
        if (linear_dotpage.getChildCount() > 0) {
            linear_dotpage.removeAllViews();
        }
    }

    class CreateBannerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            saveBanner();

            return true;
        }

    }

    class CreateBannerMasterTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            saveBannerMaster();
            return true;
        }

    }

    public void saveBanner() {
        arrayBanners_banner = new ArrayList<Model_Banner>();
        for (PListObject each : (Array) AppMain.pList_news2.getRootElement()) {
            arrayBanners_banner.add(new Model_Banner(each));
        }
        Log.e("MASTER", "MASTER1\t" + arrayBanners_banner.size());

    }

    public void saveBannerMaster() {

        arrayBanners_banner_master = new ArrayList<Model_Banner_Master>();
        for (PListObject each : (Array) AppMain.pList_news2_master
                .getRootElement()) {
            arrayBanners_banner_master.add(new Model_Banner_Master(each));
        }
        Log.e("MASTER", "MASTER2\t" + arrayBanners_banner_master.size());
    }

    private void setBannerGallery() {
        try {

            gallery.removeAllViewsInLayout();
            gallery.setAdapter(new AdapterGalleryBanner(myActivity,
                    arrayBanners_banner));
            gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View view,
                                           int position, long arg3) {

                    handler.removeCallbacks(runnableNext);
                    handler.removeCallbacks(runnablePrevious);
                    pageGallery = (position + 1);
                    if (pageGallery == gallery.getCount()) {
                        handler.postDelayed(runnablePrevious, scrollDelay);
                    } else {
                        handler.postDelayed(runnableNext, scrollDelay);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            gallery.setOnItemClickListener(new OnItemClickListener() {

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

            // gallery.setOnItemClickListener(new OnItemClickListener() {
            //
            // @Override
            // public void onItemClick(AdapterView<?> arg0, View arg1,
            // int arg2, long arg3) {
            // // TODO Auto-generated method stub
            //
            // Log.e("", "click")
            // String url_moreImage2 =
            // "http://203.150.225.223/stoubookapi/api/news-api2.aspx?nid=";
            // url_moreImage2 += arrayBanners_banner.get(arg2).getNID();
            // onclickShowZoomImage url_moreImage = new onclickShowZoomImage(
            // url_moreImage2);
            //
            // }
            // });

            gallery.refreshDrawableState();
        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setBannerGalleryMaster() {
        try {
            gallery.removeAllViewsInLayout();
            gallery.setAdapter(new AdapterGalleryBannerMaster(myActivity,
                    arrayBanners_banner_master));
            gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View view,
                                           int position, long arg3) {

                    handler.removeCallbacks(runnableNext);
                    handler.removeCallbacks(runnablePrevious);
                    pageGallery = (position + 1);
                    if (pageGallery == gallery.getCount()) {
                        handler.postDelayed(runnablePrevious, scrollDelay);
                    } else {
                        handler.postDelayed(runnableNext, scrollDelay);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            gallery.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    String bannerPosition = arrayBanners_banner_master.get(
                            position).getNID();
                    new Dialog_Show_Banner(getActivity(), bannerPosition) {

                    };
                }
            });
            // gallery.setOnItemClickListener(new OnItemClickListener() {
            //
            // @Override
            // public void onItemClick(AdapterView<?> arg0, View arg1,
            // int arg2, long arg3) {
            // // TODO Auto-generated method stub
            //
            // Log.e("", "click")
            // String url_moreImage2 =
            // "http://203.150.225.223/stoubookapi/api/news-api2.aspx?nid=";
            // url_moreImage2 += arrayBanners_banner.get(arg2).getNID();
            // onclickShowZoomImage url_moreImage = new onclickShowZoomImage(
            // url_moreImage2);
            //
            // }
            // });

            gallery.refreshDrawableState();
        } catch (NullPointerException e) {
            // load again
            Toast.makeText(myActivity, "WARINNG: Parse Error",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setId(View getView) {
        relativeLayoutUniver = (RelativeLayout) getView
                .findViewById(R.id.relative_univer);
        relativeLayoutContent = (RelativeLayout) getView
                .findViewById(R.id.relative_content);
        relativeLayoutCat = (RelativeLayout) getView
                .findViewById(R.id.relative_category);
        relativeLayoutType = (RelativeLayout) getView
                .findViewById(R.id.relative_type);

        rt_head = (RelativeLayout) getView.findViewById(R.id.ebook_rt_head);
        gallery = (Gallery) getView.findViewById(R.id.ebook_gallery);
        img_previous = (ImageView) getView
                .findViewById(R.id.ebook_imageView_previous);
        img_next = (ImageView) getView.findViewById(R.id.ebook_imageView_next);
        myViewPager = (MyViewPager) getView
                .findViewById(R.id.ebook_myViewPager1);

        linear_dotpage = (LinearLayout) getView
                .findViewById(R.id.ebook_linear_dotpage);
        rt_dummyscreen = (RelativeLayout) getView
                .findViewById(R.id.ebook_rt_dummyscreen);
        linear_customforTabview = (LinearLayout) getView
                .findViewById(R.id.ebook_linear_fortabview);
        linear_refresh = (LinearLayout) getView
                .findViewById(R.id.ebook_linear_head_refresh);
        linear_search = (LinearLayout) getView
                .findViewById(R.id.ebook_linear_head_search);
        linear_setting = (LinearLayout) getView
                .findViewById(R.id.ebook_linear_head_setting);
        image_refresh = (ImageView) getView
                .findViewById(R.id.ebook_image_refresh);
        image_search = (ImageView) getView
                .findViewById(R.id.ebook_image_search);

        image_setting = (ImageView) getView
                .findViewById(R.id.ebook_image_setting);
        txt_refresh = (TextView) getView.findViewById(R.id.ebook_txt_refresh);
        txt_search = (TextView) getView.findViewById(R.id.ebook_txt_search);
        txt_setting = (TextView) getView.findViewById(R.id.ebook_txt_setting);

        btn_category = (Button) getView.findViewById(R.id.ebook_btn_category);
        btn_type = (Button) getView.findViewById(R.id.ebook_btn_type);
        btn_univer = (Button) getView.findViewById(R.id.ebook_btn_univer);
        btn_content = (Button) getView.findViewById(R.id.ebook_btn_content);
        spinner_category = (Spinner) getView
                .findViewById(R.id.ebook_spinner_category);

        // stou
        spinner_category2 = (Spinner) getView
                .findViewById(R.id.ebook_spinner_category2);
        spinner_univer = (Spinner) getView
                .findViewById(R.id.ebook_spinner_univer);
        spinner_content = (Spinner) getView
                .findViewById(R.id.ebook_spinner_content);
        // end
        try {

            txt_refresh.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            txt_search.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            txt_setting.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));

            btn_category.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            btn_type.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            btn_univer.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            btn_content.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
        } catch (Exception e) {
            // TODO: handle exception
        }

        String title = getResources().getString(R.string.guideline_education);
        btn_content.setText(title);

        linear_refresh.setOnClickListener(new OnClickControl());
        linear_search.setOnClickListener(new OnClickControl());
        linear_setting.setOnClickListener(new OnClickControl());

        // ebook_icon_back.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // }
        // });

        btn_category.setOnTouchListener(new onTouchBtnAlertView());
        // stou
        // btn_type.setOnClickListener(new onClickBtnAlertView());
        btn_type.setOnTouchListener(new onTouchBtnAlertView());
        // end
        btn_univer.setOnTouchListener(new onTouchBtnAlertView());
        btn_content.setOnTouchListener(new onTouchBtnAlertView());
        try {

            fade_in = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {

            hideLayout();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void hideLayout() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                    if (StaticUtils.shelfID == 0) {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.VISIBLE);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));
                    } else if (StaticUtils.shelfID == 1) {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.VISIBLE);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));
                    } else {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.GONE);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.orange_stou));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void getSaveShelf() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                    Model_Setting model_setting = Class_Manage
                            .getModel_Setting(myActivity);
                    if (model_setting != null) {

                        ((RelativeLayout) getActivity().findViewById(
                                R.id.ebook_mainlayout))
                                .setBackgroundResource(model_setting
                                        .getDrawable_backgroundStyle());
                    }
                    Model_SettingShelf settingShelf = Class_Manage
                            .getModel_SettingShelf(myActivity);

                    if (settingShelf != null) {
                        Picasso.with(getActivity())
                                .load(settingShelf.getDrawable_ShelfStyle())
                                .into(((ImageView) getActivity().findViewById(
                                        R.id.ebook_img_shelf1)));
                        Picasso.with(getActivity())
                                .load(settingShelf.getDrawable_ShelfStyle())
                                .into(((ImageView) getActivity().findViewById(
                                        R.id.ebook_img_shelf2)));
                        Picasso.with(getActivity())
                                .load(settingShelf.getDrawable_ShelfStyle())
                                .into(((ImageView) getActivity().findViewById(
                                        R.id.ebook_img_shelf3)));
                        Picasso.with(getActivity())
                                .load(settingShelf.getDrawable_ShelfStyle())
                                .into(((ImageView) getActivity().findViewById(
                                        R.id.ebook_img_shelf4)));

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void setPageRelativeGreen() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                    if (!StaticUtils.isAMasterDegree) {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        Log.e(tag, "ID" + StaticUtils.isAMasterDegree);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));
                    } else {
                        try {
                            relativeLayoutUniver.setVisibility(View.VISIBLE);
                            linear_setting.setVisibility(View.GONE);
                            rt_head.setBackgroundColor(getResources().getColor(
                                    R.color.green_stou));

                            setBackground();
                            setShelf();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void setPageRelativeOrange() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    if (!StaticUtils.isAMasterDegree) {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        Log.e(tag, "ID" + StaticUtils.isAMasterDegree);
                    } else {
                        try {
                            relativeLayoutUniver.setVisibility(View.VISIBLE);
                            linear_setting.setVisibility(View.GONE);
                            rt_head.setBackgroundColor(getResources().getColor(
                                    R.color.orange_stou));

                            setBackground();
                            setShelf();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

    }

    private void setBackground() throws NullPointerException {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                    int[] id_drawble = {R.drawable.bg_main_default,
                            R.drawable.bg_finewood,
                            R.drawable.bg_whilte_hardwood, R.drawable.bg_wood,
                            R.drawable.bg_wood_wall, R.drawable.bg_light_green};

                    ((RelativeLayout) getActivity().findViewById(
                            R.id.ebook_mainlayout))
                            .setBackgroundResource(id_drawble[5]);

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void setShelf() throws NullPointerException {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                    int[] id_drawableshelf = {R.drawable.shelf_glass2x,
                            R.drawable.shelf_wood2x,
                            R.drawable.shelf_wood_steel2x,
                            R.drawable.shelf_blue2x, R.drawable.shelf_red2x,
                            R.drawable.shelf_dark2x};

                    Picasso.with(getActivity())
                            .load(id_drawableshelf[3])
                            .into(((ImageView) getActivity().findViewById(
                                    R.id.ebook_img_shelf1)));

                    Picasso.with(getActivity())
                            .load(id_drawableshelf[3])
                            .into(((ImageView) getActivity().findViewById(
                                    R.id.ebook_img_shelf2)));
                    Picasso.with(getActivity())
                            .load(id_drawableshelf[3])
                            .into(((ImageView) getActivity().findViewById(
                                    R.id.ebook_img_shelf3)));
                    Picasso.with(getActivity())
                            .load(id_drawableshelf[3])
                            .into(((ImageView) getActivity().findViewById(
                                    R.id.ebook_img_shelf4)));

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void setSpinnerAdapter() {
        try {

            spinner_category.setAdapter(new AdapterSpinnerCategory(myActivity, /* arr_Categories */
                    addCatagoriesToArraylist1()));

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        // stou

        try {

            spinner_category2.setAdapter(new AdapterSpinnerCategory(myActivity, /* arr_Categories2 */
                    addCatagoriesToArraylist2()));

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        try {

            spinner_univer.setAdapter(new AdapterSpinnerCategoryMaster(
                    myActivity, addCatagoriesToArraylistMaster0()));

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        try {

            spinner_content.setAdapter(new AdapterSpinnerCategoryMaster(
                    myActivity, addCatagoriesToArraylistMaster1()));

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        // end
    }

    private ArrayList<Model_Categories> addCatagoriesToArraylist1() {

        try {
            arr_Categories = new ArrayList<Model_Categories>();
            Model_Categories model_allbook = new Model_Categories();
            model_allbook.setCatID(1);
            model_allbook.setSCatID(0);
            model_allbook.setName(getString(R.string.tutorial));
            model_allbook.setDetail(getString(R.string.allbook_detail));
            model_allbook.setPictureURL("default");

            arr_Categories.add(model_allbook);
            for (PListObject each : (Array) AppMain.pList_categories
                    .getRootElement()) {
                arr_Categories.add(new Model_Categories(each));
            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

        return arr_Categories;

    }

    private ArrayList<Model_Categories> addCatagoriesToArraylist2() {

        try {
            arr_Categories2 = new ArrayList<Model_Categories>();
            Model_Categories model_allbook = new Model_Categories();
            model_allbook.setCatID(2);
            model_allbook.setSCatID(0);
            model_allbook.setName(getString(R.string.rare_book));
            model_allbook.setDetail(getString(R.string.allbook_detail));
            model_allbook.setPictureURL("default");

            arr_Categories2.add(model_allbook);
            for (PListObject each : (Array) AppMain.pList_categories2
                    .getRootElement()) {
                arr_Categories2.add(new Model_Categories(each));

            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

        return arr_Categories2;

    }

    private ArrayList<Model_Categories> addCatagoriesToArraylist3() {

        try {
            arr_Categories3 = new ArrayList<Model_Categories>();
            Model_Categories model_allbook = new Model_Categories();
            model_allbook.setCatID(3);
            model_allbook.setSCatID(0);
            model_allbook.setName(getString(R.string.univer_book));
            model_allbook.setDetail(getString(R.string.allbook_detail));
            model_allbook.setPictureURL("default");

            arr_Categories3.add(model_allbook);
            for (PListObject each : (Array) AppMain.pList_categories3
                    .getRootElement()) {
                arr_Categories3.add(new Model_Categories(each));

            }

        } catch (NullPointerException e) {
            // TODO: handle exception
        }

        return arr_Categories3;

    }

    class CreateModel_CategoriesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            addCatagoriesToArraylistMaster0();
            addCatagoriesToArraylistMaster1();
            return true;
        }
    }

    private ArrayList<Model_Book_Master> addCatagoriesToArraylistMaster0() {
        arr_Book_Masters0 = new ArrayList<Model_Book_Master>();
        for (PListObject each : (Array) AppMain.pList_default_ebook_master0
                .getRootElement()) {
            arr_Book_Masters0.add(new Model_Book_Master(each));
        }

        return arr_Book_Masters0;

    }

    private ArrayList<Model_Book_Master> addCatagoriesToArraylistMaster1() {
        arr_Book_Masters1 = new ArrayList<Model_Book_Master>();
        for (PListObject each : (Array) AppMain.pList_default_ebook_master1
                .getRootElement()) {
            arr_Book_Masters1.add(new Model_Book_Master(each));
        }

        return arr_Book_Masters1;

    }

    private class onTouchBtnAlertView implements OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == btn_category.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_category
                            .setOnItemSelectedListener(new OnSelectSpinner());
                }

                // stou
                if (v.getId() == btn_type.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_category2
                            .setOnItemSelectedListener(new OnSelectSpinner());
                }
                if (v.getId() == btn_univer.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_univer
                            .setOnItemSelectedListener(new OnSelectSpinner());
                }
                if (v.getId() == btn_content.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_content
                            .setOnItemSelectedListener(new OnSelectSpinner());
                }
                // end

                return true;
            }
            // end
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v.getId() == btn_category.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_category.performClick();

                }

                // stou
                if (v.getId() == btn_type.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_category2.performClick();

                }
                // stou
                if (v.getId() == btn_content.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_content.performClick();

                }
                if (v.getId() == btn_univer.getId()) {

                    // snuxker
                    setBackgroundMenuButton(true);

                    // category
                    spinner_univer.performClick();

                }

                // end

                return true;
            }
            return false;

        }
    }

    private void setBackgroundMenuButton(boolean isActive) {

    }

    private interface EventToText {
        // public void setTextCategory();
        // stou
        public void setTextCategory(int fromCategories, String name);
        // end
    }

    private class OnSelectSpinner implements EventToText,
            OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            if (parent.getId() == spinner_category.getId()) {
                catid = String.valueOf(arr_Categories.get(position).getCatID());
                setTextCategory(1, arr_Categories.get(position).getName());
                AppMain.pList_default_ebook = null;

                if (position == 0) {

                    loadApiBySubCateEbookFirstPosition(catid, type_ebook,
                            Page.EBOOK_FRAGMENT);

                } else {
                    int sCatID = arr_Categories.get(position).getSCatID();
                    int catID = arr_Categories.get(position).getCatID();

                    loadApiBySubCateTypeEbook(catID, sCatID);
                }
                if (!StaticUtils.isAMasterDegree) {
                    relativeLayoutUniver.setVisibility(View.VISIBLE);

                } else {
                    try {
                        StaticUtils.shelfID = 1;
                        StaticUtils.pageID = 1;
                        setBannerGallery();
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.VISIBLE);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));

                        getSaveShelf();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }

            // stou
            if (parent.getId() == spinner_category2.getId()) {
                catid = String
                        .valueOf(arr_Categories2.get(position).getCatID());
                setTextCategory(2, arr_Categories2.get(position).getName());
                AppMain.pList_default_ebook = null;

                if (position == 0) {

                    loadApiBySubCateEbookFirstPosition(catid, type_ebook,
                            Page.EBOOK_FRAGMENT);

                } else {

                    int sCatID = arr_Categories2.get(position).getSCatID();
                    int catID = arr_Categories2.get(position).getCatID();

                    loadApiBySubCateTypeEbook(catID, sCatID);

                }
                if (!StaticUtils.isAMasterDegree) {
                    relativeLayoutUniver.setVisibility(View.VISIBLE);

                } else {
                    try {
                        StaticUtils.shelfID = 1;
                        StaticUtils.pageID = 1;
                        setBannerGallery();
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.VISIBLE);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));

                        // arrayBanners_banner2.clear();
                        // setBannerGallery();

                        // checkBackground2();
                        //
                        // prepareBackground2();
                        // setBackground2();
                        // checkShelf2();
                        // prepareShelf2();
                        // setShelf2();
                        getSaveShelf();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }
            if (parent.getId() == spinner_univer.getId()) {
                isTabletChoose = true;
                sCatIDs = arr_Book_Masters0.get(position).getMCatID();
                bCode = arr_Book_Masters0.get(position).getBCode();

                getSubCatMasterDegree(sCatIDs, bCode);
                selectedName = arr_Book_Masters0.get(position).getBookName();
                btn_univer.setText(selectedName);
            }
            if (parent.getId() == spinner_content.getId()) {
                isTabletChoose = true;
                sCatIDs = arr_Book_Masters1.get(position).getMCatID();
                bCode = arr_Book_Masters1.get(position).getBCode();

                getSubCatMasterDegree(sCatIDs, bCode);
                selectedName = arr_Book_Masters1.get(position).getBookName();
                btn_content.setText(selectedName);
            }

            if (!StaticUtils.isAMasterDegree) {
                relativeLayoutType.setVisibility(View.VISIBLE);
                relativeLayoutCat.setVisibility(View.VISIBLE);
                relativeLayoutUniver.setVisibility(View.GONE);
                relativeLayoutContent.setVisibility(View.GONE);
            }
            // end
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

            System.out.println("onNothingSelected");
            setBackgroundMenuButton(false);

        }

        @Override
        public void setTextCategory(int fromCategories, String name) {

            // stou
            switch (fromCategories) {
                case 1:

                    // for (Model_Categories each : arr_Categories) {
                    // if (String.valueOf(each.getCatID()) == catid) {

                    // btn_category.setText(each.getName());
                    // bundle.putString(serial_category, each.getName());

                    btn_category.setText(name);
                    bundle.putString(serial_category, name);

                    // }
                    // }

                    break;

                case 2:

                    // for (Model_Categories each : arr_Categories2) {
                    // if (String.valueOf(each.getCatID()) == catid) {
                    //
                    // btn_type.setText(each.getName());
                    // bundle.putString(serial_type, each.getName());

                    btn_type.setText(name);
                    bundle.putString(serial_type, name);

                    // }
                    // }
                    // end

                    break;
                case 3:

                    // for (Model_Categories each : arr_Categories2) {
                    // if (String.valueOf(each.getCatID()) == catid) {
                    //
                    // btn_type.setText(each.getName());
                    // bundle.putString(serial_type, each.getName());

                    btn_univer.setText(name);
                    bundle.putString(serial_univer, name);

                    // }
                    // }
                    // end

                    break;

                default:
                    break;
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
                if (id == picon_refresh.getId()) {
                    if (StaticUtils.isAMasterDegree) {
                        if (!isChoice) {
                            setListViewMaster(
                                    AppMain.pList_default_ebook_master, null);
                        } else {
                            getSubCatMasterDegree(StaticUtils.sCat,
                                    StaticUtils.bCode);
                        }

                    } else {
                        refreshItem();
                    }

                }
                if (id == picon_search.getId()) {
                    Log.i(tag, "phone search");
                    if (Activity_Tab.mLastTab != null) {

                        FragmentTransaction ft = myActivity
                                .getSupportFragmentManager().beginTransaction();
                        // detach lastTab
//						if (Activity_Tab.mLastTab.fragment != null) {
//							ft.detach(Activity_Tab.mLastTab.fragment);
//						}

                        // new fragment search
                        if (frag_search == null) {
                            frag_search = Fragment.instantiate(myActivity, Fragment_Search.class.getName(), bundle);
                            ft.replace(R.id.realtabcontent, frag_search,
                                    "search").addToBackStack(null);

                            HaveFragment = true;
                        } else {
//							ft.attach(frag_search);
//							HaveFragment = true;
                        }

                        ft.commit();
                        myActivity.getSupportFragmentManager()
                                .executePendingTransactions();
                    }

                }
            } else {

                if (id == linear_refresh.getId()) {
                    // refresh
                    if (StaticUtils.isAMasterDegree) {
                        if (!isChoice) {
                            setViewPagerMaster(
                                    AppMain.pList_default_ebook_master, null);
                        } else {
                            getSubCatMasterDegree(StaticUtils.sCat,
                                    StaticUtils.bCode);
                        }
                    } else {
                        refreshItem();
                    }
                }
                if (id == linear_search.getId()) {
                    // search
                    viewSearchEbook();
                }

                if (id == linear_setting.getId()) {
                    // setting
                    viewSettingShelf();
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

    private void refreshItem() {
        refresh_Ebook = new Refresh_Ebook(myActivity);
        refresh_Ebook.setOnListener(new Throw_IntefacePlist() {

            @Override
            public void PList(plist.xml.PList resultPlist, ProgressDialog pd) {
                if (AppMain.isphone) {
                    setListView(resultPlist, pd);
                } else {
                    setViewPager(resultPlist, pd);
                }

            }

            @Override
            public void PList_Detail_Comment(plist.xml.PList Plistdetail,
                                             plist.xml.PList Plistcomment, ProgressDialog pd) {
                // TODO Auto-generated method stub
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
        refresh_Ebook.LoadRefreshAPI();
    }

    private void viewSearchEbook() {
        view_SearchEbook = new View_SearchEbook(myActivity,
                linear_customforTabview, rt_dummyscreen, rt_head.getId()) {

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
            }

            @Override
            public void onAdapter_SearchList(
                    Adapter_SearchList_Ebook adapter_SearchList) {
                setAdapter_SearchList(adapter_SearchList);
            }
        };
        view_SearchEbook.setAdapter(getAdapter_SearchList());
        view_SearchEbook.initView(0, 0, 0);
    }

    private void viewSettingShelf() {
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
                        R.id.ebook_mainlayout))
                        .setBackgroundResource(drawableStyle);
            }

            @Override
            public void onChangeShelfStyle(int drawableStyle) {

                Picasso.with(getActivity())
                        .load(drawableStyle)
                        .into(((ImageView) getActivity().findViewById(
                                R.id.ebook_img_shelf1)));

                Picasso.with(getActivity())
                        .load(drawableStyle)
                        .into(((ImageView) getActivity().findViewById(
                                R.id.ebook_img_shelf2)));
                Picasso.with(getActivity())
                        .load(drawableStyle)
                        .into(((ImageView) getActivity().findViewById(
                                R.id.ebook_img_shelf3)));
                Picasso.with(getActivity())
                        .load(drawableStyle)
                        .into(((ImageView) getActivity().findViewById(
                                R.id.ebook_img_shelf4)));

            }
        };
        view_Setting.initView();
    }

    private class OnClickControl implements OnClickListener {

        @Override
        public void onClick(View v) {
            // for Phone
            if (AppMain.isphone) {

                if (v.getId() == picon_refresh.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                if (v.getId() == picon_search.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }

            }

            // /for Tablet
            else {
                if (v.getId() == linear_refresh.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                if (v.getId() == linear_search.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
                if (v.getId() == linear_setting.getId()) {
                    v.startAnimation(fade_in);
                    fade_in.setAnimationListener(new onAnimationLitener(v
                            .getId()));
                }
            }

        }

    }

    private void setOnclick() {
        img_previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(displayMetrics);
                int width = (displayMetrics.widthPixels);
                gallery.onFling(null, null, (float) 2.5 * width, 0);
            }
        });
        img_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(displayMetrics);
                int width = (displayMetrics.widthPixels);
                if ((pageGallery) < gallery.getCount()) {
                    gallery.onFling(null, null, (float) -2.5 * width, 0);
                }
            }
        });
    }

    Runnable runnableNext = new Runnable() {

        @Override
        public void run() {

            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();

                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(displayMetrics);
                int width = (displayMetrics.widthPixels);

                gallery.onFling(null, null, (float) -2.5 * width, 0);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    };
    Runnable runnablePrevious = new Runnable() {

        @Override
        public void run() {
            gallery.onFling(null, null, (1000 * gallery.getCount()), 0);
        }
    };

    private Category_SubCate_Ebookslist category_SubCate_Ebookslist;

    @Override
    public void onPause() {

        Log.e(tag, "onPause");
        System.gc();
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        Log.e(tag, "onDestroyView");

        if (AppMain.isphone) {

        } else {
            handler.removeCallbacks(runnableNext);
            handler.removeCallbacks(runnablePrevious);
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppMain.isphone) {

            if (!StaticUtils.isAMasterDegree) {
                loadApiBySubCateEbookFirstPosition("0", type_ebook,
                        Page.EBOOK_FRAGMENT);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        linearLayoutNews.setVisibility(View.VISIBLE);
                        linearLayoutRecommend.setVisibility(View.VISIBLE);
                        linearLayoutUniver.setVisibility(View.GONE);
                        linearLayoutContent.setVisibility(View.GONE);
                    }
                });

                setListView(AppMain.pList_default_ebook, null);
            } else {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        linearLayoutNews.setVisibility(View.GONE);
                        linearLayoutRecommend.setVisibility(View.GONE);
                        linearLayoutContent.setVisibility(View.VISIBLE);
                        linearLayoutUniver.setVisibility(View.VISIBLE);
                    }
                });

                if (isChoice) {

                    if (Adapter_List_Ebook_MasterDetail.phoneToDetail) {
                        getSubCatMasterDegree(StaticUtils.sCat,
                                StaticUtils.bCode);
                    } else {
                        setListViewMaster(AppMain.pList_default_ebook_master,
                                null);
                    }

                } else {
                    StaticUtils.sCat = "";
                    StaticUtils.bCode = "";
                    if (Adapter_List_Ebook_MasterDetail.phoneToDetail) {
                        getSubCatMasterDegree(StaticUtils.sCat,
                                StaticUtils.bCode);
                    } else {
                        setListViewMaster(AppMain.pList_default_ebook_master,
                                null);
                    }
                }

            }

        } else {

            // checkID();

            Log.e(tag, "onResume");

            if (!StaticUtils.isAMasterDegree) {
                Log.e(tag, "onResume");
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        relativeLayoutUniver.setVisibility(View.VISIBLE);
                        linear_setting.setVisibility(View.VISIBLE);
                        Log.e(tag, "ID" + StaticUtils.isAMasterDegree);
                        rt_head.setBackgroundColor(getResources().getColor(
                                R.color.green_stou));
                    }
                });
            }

            if (!StaticUtils.isAMasterDegree) {
                if (StaticUtils.pageID == 0) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            linear_setting.setVisibility(View.VISIBLE);
                        }
                    });
                    getSaveShelf();
                    setPageRelativeGreen();
                    setBannerGallery();

                } else if (StaticUtils.pageID == 1) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            linear_setting.setVisibility(View.VISIBLE);
                        }
                    });
                    getSaveShelf();
                    setPageRelativeGreen();
                    setBannerGallery();

                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        relativeLayoutType.setVisibility(View.VISIBLE);
                        relativeLayoutCat.setVisibility(View.VISIBLE);
                        relativeLayoutUniver.setVisibility(View.GONE);
                        relativeLayoutContent.setVisibility(View.GONE);
                    }
                });
                if (AppMain.pList_default_ebook != null) {
                    setViewPager(AppMain.pList_default_ebook, null);
                } else {
                    setViewPager(AppMain.pList_default_ebook_copy, null);

                }

            } else {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        linear_setting.setVisibility(View.GONE);
                    }
                });

                getSaveShelf();
                setBannerGalleryMaster();
                setPageRelativeOrange();

                StaticUtils.phonePage = 2;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        relativeLayoutType.setVisibility(View.GONE);
                        relativeLayoutCat.setVisibility(View.GONE);
                        relativeLayoutContent.setVisibility(View.VISIBLE);
                    }
                });
                if (isTabletChoose) {
                    if (Adapter_List_Ebook_MasterDetail.phoneToDetail) {
                        getSubCatMasterDegree(StaticUtils.sCat,
                                StaticUtils.bCode);
                    } else {
                        setViewPagerMaster(AppMain.pList_default_ebook_master,
                                null);
                    }

                } else {
                    StaticUtils.sCat = "";
                    StaticUtils.bCode = "";
                    if (Adapter_List_Ebook_MasterDetail.phoneToDetail) {
                        getSubCatMasterDegree(StaticUtils.sCat,
                                StaticUtils.bCode);
                    } else {
                        setViewPagerMaster(AppMain.pList_default_ebook_master,
                                null);
                    }
                }
            }

        }

        // STOU

        // if (!Activity_Detail.alreadyBuyAndDirectToShelf)
        // return;
        //
        // if (AppMain.isphone) {
        // Activity_Tab.mTabHost.setCurrentTabByTag("Tab5");
        // }else {
        // Activity_Tab.mTabHost.setCurrentTabByTag("Tab4");
        //
        // }

        // end

        CreateModel_CategoriesTask categoriesTask = new CreateModel_CategoriesTask();
        categoriesTask.execute();
    }

    /**
     * @return the adapter_SearchList
     */
    public Adapter_SearchList_Ebook getAdapter_SearchList() {
        return adapter_SearchList;
    }

    /**
     * @param adapter_SearchList the adapter_SearchList to set
     */
    public void setAdapter_SearchList(
            Adapter_SearchList_Ebook adapter_SearchList) {
        this.adapter_SearchList = adapter_SearchList;
    }

    // stou
    private void loadApiBySubCateTypeEbook(int CatID, int SCatID) {

        category_SubCate_Ebookslist = new Category_SubCate_Ebookslist(
                myActivity, CatID, SCatID);
        category_SubCate_Ebookslist.setOnListener(new Throw_IntefacePlist() {

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
            public void PList(PList resultPlist, ProgressDialog pd) {

                if (AppMain.isphone) {

                    setListView(resultPlist, pd);

                } else {

                    // snuxker
                    setBackgroundMenuButton(false);

                    setViewPager(resultPlist, pd);
                }
            }
        });
        category_SubCate_Ebookslist.LoadCategoryAPI();
    }

    private void loadApiBySubCateEbookFirstPosition(String CatID,
                                                    String type_ebook, Page page) {
        category_Ebook = new Category_EbookOfFragment(myActivity, CatID,
                type_ebook, page);
        category_Ebook.setOnListener(new Throw_IntefacePlist() {

            @Override
            public void PList(PList resultPlist, ProgressDialog pd) {

                if (AppMain.isphone) {

                    setListView(resultPlist, pd);

                } else {

                    // snuxker
                    setBackgroundMenuButton(false);

                    setViewPager(resultPlist, pd);
                }

            }

            @Override
            public void PList_Detail_Comment(plist.xml.PList Plistdetail,
                                             plist.xml.PList Plistcomment, ProgressDialog pd) {
                // TODO Auto-generated method stub
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
        category_Ebook.LoadCategoryAPI();
    }

    @SuppressLint("InflateParams")
    private class CatagoriesAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Model_Categories> categoriesArrayList;
        private LayoutInflater inflater;
        private ImageDownloader_forCache imageDownloader;
        ArrayList<Model_Cat> array;

        public CatagoriesAdapter(Context context,
                                 ArrayList<Model_Categories> categoriesArrayList) {

            this.context = context;
            this.categoriesArrayList = categoriesArrayList;
            this.inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageDownloader = new ImageDownloader_forCache();
            addValueImages();
        }

        private class ViewHolder {

            ImageView img_cover;
            TextView txt_name;
            TextView txt_detail;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (categoriesArrayList == null)
                return 0;
            return categoriesArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return categoriesArrayList.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final int index = position;
            ViewHolder viewHolder = null;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.layout_category, null);

                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.category_txt_name);
                viewHolder.txt_detail = (TextView) convertView
                        .findViewById(R.id.category_txt_detail);
                viewHolder.img_cover = (ImageView) convertView
                        .findViewById(R.id.category_image_cover);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.txt_name.setText(categoriesArrayList.get(index)
                    .getName());
            viewHolder.txt_name.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            viewHolder.txt_detail.setText(categoriesArrayList.get(index)
                    .getDetail());
            viewHolder.txt_detail.setTypeface(StaticUtils.getTypeface(
                    myActivity, Font.DB_Helvethaica_X_Med));

            String url = "http://203.150.225.223/stoubookapi/"
                    + categoriesArrayList.get(index).getPictureURL();

            if (categoriesArrayList.get(index).getPictureURL()
                    .equals("default")) {

                try {
                    Picasso.with(context)
                            .load("http://203.150.225.223/stoubookapi/"
                                    + array.get(0).getPictureURL())
                            .into(viewHolder.img_cover);

                } catch (Exception e) {
                    // TODO: handle exception
                    Bitmap bm = StaticUtils.getBitmapFromDrawableId(context,
                            R.drawable.ic_launcher);
                    viewHolder.img_cover.setImageBitmap(bm);
                }

            } else {
                try {

                    Picasso.with(context).load(url).into(viewHolder.img_cover);

                } catch (Exception e) {
                    // TODO: handle exception
                    Bitmap bm = StaticUtils.getBitmapFromDrawableId(context,
                            R.drawable.ic_launcher);
                    viewHolder.img_cover.setImageBitmap(bm);
                }

            }

            return convertView;

        }
    }

    public void addValueImages() {
        try {

            ArrayList<Model_Cat> array;
            array = new ArrayList<Model_Cat>();
            Model_Cat model_allbook = new Model_Cat();
            model_allbook.setCatID(0);
            model_allbook.setSCatID(0);
            model_allbook.setName("�͡��á���͹");
            model_allbook.setDetail("˹ѧ��ͷ������ء��Ǵ㹤�ѧ˹ѧ���");
            model_allbook.setPictureURL("images/allcat.png");
            array.add(model_allbook);

        } catch (NullPointerException e) {
            // TODO: handle exception
        }
    }

    @SuppressLint("InflateParams")
    private class CatagoriesAdapterMaster extends BaseAdapter {

        private Context context;
        private ArrayList<Model_Book_Master> categoriesArrayList;
        private LayoutInflater inflater;
        private ImageDownloader_forCache imageDownloader;
        ArrayList<Model_Cat> array;

        public CatagoriesAdapterMaster(Context context,
                                       ArrayList<Model_Book_Master> categoriesArrayList) {

            this.context = context;
            this.categoriesArrayList = categoriesArrayList;
            this.inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageDownloader = new ImageDownloader_forCache();

        }

        private class ViewHolder {

            ImageView img_cover;
            TextView txt_name;
            TextView txt_detail;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (categoriesArrayList == null)
                return 0;
            return categoriesArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return categoriesArrayList.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.layout_category, null);

                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.category_txt_name);
                viewHolder.txt_detail = (TextView) convertView
                        .findViewById(R.id.category_txt_detail);
                viewHolder.img_cover = (ImageView) convertView
                        .findViewById(R.id.category_image_cover);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.txt_name.setText(categoriesArrayList.get(position)
                    .getBookName());
            viewHolder.txt_name.setTypeface(StaticUtils.getTypeface(myActivity,
                    Font.DB_Helvethaica_X_Med));
            viewHolder.txt_detail.setVisibility(View.GONE);
            viewHolder.txt_detail.setTypeface(StaticUtils.getTypeface(
                    myActivity, Font.DB_Helvethaica_X_Med));

            try {

                Picasso.with(context)
                        .load(categoriesArrayList.get(position).getBookCover())
                        .into(viewHolder.img_cover);

            } catch (Exception e) {
                // TODO: handle exception
                Bitmap bm = StaticUtils.getBitmapFromDrawableId(context,
                        R.drawable.ic_launcher);
                viewHolder.img_cover.setImageBitmap(bm);

            }

            return convertView;

        }

    }

    private void getSubCatMasterDegree(String mCat, String bCode) {
        if (!isChoice) {
            isChoice = true;
        }
        if (!isSelectedMaster) {
            isSelectedMaster = true;
        }
        if (!isTabletChoose) {
            isTabletChoose = true;
        }
        isSelectedMaster = true;
        StaticUtils.sCat = mCat;
        StaticUtils.bCode = bCode;

        final AsyncTask_FetchAPI asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        final ProgressDialog dialogs = new ProgressDialog(getActivity());
        dialogs.setMax(100);
        dialogs.show();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            private PList pList;

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                // TODO Auto-generated method stub
                asyncTask_FetchAPI.cancel(true);
            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                // TODO Auto-generated method stub
                Log.d("TAG", "URL :" + " " + apiURL);

            }

            @Override
            public void onFetchError(String apiURL, int currentIndex,
                                     Exception e) {
                // TODO Auto-generated method stub
                asyncTask_FetchAPI.cancel(true);
            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex,
                                        PList result) {
                // TODO Auto-generated method stub
                if (apiURL.contains("getMasterBookDetail")) {
                    pList = result;

                }

            }

            @Override
            public void onAllTaskDone() {
                // TODO Auto-generated method stub
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (AppMain.isphone) {

                            setListViewMasterDetail(pList, dialogs);

                        } else {

                            setViewPagerMasterDetail(pList, dialogs);
                        }

                    }
                });

            }

        });
        String url = "http://203.150.225.223/stoubookapi/api/getMasterBookDetail.php";
        url += "?mcat=" + mCat;
        url += "&bcode=" + bCode;
        asyncTask_FetchAPI.execute(url);

    }

}
