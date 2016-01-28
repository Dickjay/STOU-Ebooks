package com.porar.ebooks.stou.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.porar.ebook.adapter.Adapter_List_Ebook_MasterDetail;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.stou.fragment.Fragment_Category;
import com.porar.ebooks.stou.fragment.Fragment_Contact;
import com.porar.ebooks.stou.fragment.Fragment_Ebook;
import com.porar.ebooks.stou.fragment.Fragment_Ebook_Mode;
import com.porar.ebooks.stou.fragment.Fragment_Publisher;
import com.porar.ebooks.stou.fragment.Fragment_Shelf;
import com.porar.ebooks.utils.RegisterFacebook;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

import java.util.HashMap;

public class Activity_Tab extends FragmentActivity implements
        OnTabChangeListener {

    private TabHost mTabHost;
    public static HashMap<String, TabInfo> mapTabInfo = new HashMap<String, Activity_Tab.TabInfo>();
    public static TabInfo mLastTab = null;
    AlertDialog alertDialog;
    public static Fragment_Ebook ebook;

    @Override
    public void onDestroy() {
        RegisterFacebook.unRegisterFacebook(Activity_Tab.this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RegisterFacebook.CallbackResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tab_main);
        RegisterFacebook.InitFacebookSDK(Activity_Tab.this);

        LinearLayout tab_main = (LinearLayout) findViewById(R.id.tab_main_default);
        if (tab_main != null) {
            AppMain.isphone = true;
        } else {
            AppMain.isphone = false;
        }
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            Log.e("ActivityTab", "get instance savedInstanceState");
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // get
            // instance
        }

    }

    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        TabInfo tabInfo = null;
        if (AppMain.isphone) {

            Activity_Tab.AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec("Tab1").setIndicator("Tab 1"),
                    (tabInfo = new TabInfo("Tab1", Fragment_Ebook_Mode.class,
                            args)), getResources().getString(R.string.book_tab_title), R.drawable.tab_ebooks);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            // Activity_Tab.AddTab(this, this.mTabHost,
            // this.mTabHost.newTabSpec("Tab2").setIndicator("Tab 2"),
            // (tabInfo = new TabInfo("Tab2", Fragment_Publisher.class, args)),
            // "publisher", R.drawable.tab_publishers);
            // Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);
            //
            // Activity_Tab.AddTab(this, this.mTabHost,
            // this.mTabHost.newTabSpec("Tab3").setIndicator("Tab 3"),
            // (tabInfo = new TabInfo("Tab3", Fragment_Category.class, args)),
            // "category", R.drawable.tab_category);
            // Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);
            //
            // Activity_Tab.AddTab(this, this.mTabHost,
            // this.mTabHost.newTabSpec("Tab4").setIndicator("Tab 4"),
            // (tabInfo = new TabInfo("Tab4", Fragment_Top.class, args)), "top",
            // R.drawable.tab_top);
            // Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            Activity_Tab
                    .AddTab(this,
                            this.mTabHost,
                            this.mTabHost.newTabSpec("Tab5").setIndicator(
                                    "Tab 5"), (tabInfo = new TabInfo("Tab5",
                                    Fragment_Shelf.class, args)), getResources().getString(R.string.shelf_tab_title),
                            R.drawable.tab_bookshelf);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            // STOU
            Activity_Tab
                    .AddTab(this,
                            this.mTabHost,
                            this.mTabHost.newTabSpec("Tab6").setIndicator(
                                    "Tab 6"), (tabInfo = new TabInfo("Tab6",
                                    Fragment_Contact.class, args)), getResources().getString(R.string.contact_tab_title),
                            R.drawable.tab_contact);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            mTabHost.setOnTabChangedListener(this);

            if (Shared_Object.isOfflineMode) {
                mTabHost.setCurrentTabByTag("Tab5");
            } else {
                this.onTabChanged("Tab1");
            }

        } else {

            Activity_Tab.AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec("Tab1").setIndicator("Tab 1"),
                    (tabInfo = new TabInfo("Tab1", Fragment_Ebook_Mode.class,
                            args)), getResources().getString(R.string.book_tab_title), R.drawable.tab_ebooks);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            // Activity_Tab.AddTab(this, this.mTabHost,
            // this.mTabHost.newTabSpec("Tab2").setIndicator("Tab 2"),
            // (tabInfo = new TabInfo("Tab2", Fragment_Publisher.class, args)),
            // "publisher", R.drawable.tab_publishers);
            // Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);
            //
            // Activity_Tab.AddTab(this, this.mTabHost,
            // this.mTabHost.newTabSpec("Tab3").setIndicator("Tab 3"),
            // (tabInfo = new TabInfo("Tab3", Fragment_Top.class, args)), "top",
            // R.drawable.tab_top);
            // Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            Activity_Tab
                    .AddTab(this,
                            this.mTabHost,
                            this.mTabHost.newTabSpec("Tab4").setIndicator(
                                    "Tab 4"), (tabInfo = new TabInfo("Tab4",
                                    Fragment_Shelf.class, args)), getResources().getString(R.string.shelf_tab_title),
                            R.drawable.tab_bookshelf);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            // STOU
            Activity_Tab
                    .AddTab(this,
                            this.mTabHost,
                            this.mTabHost.newTabSpec("Tab5").setIndicator(
                                    "Tab 5"), (tabInfo = new TabInfo("Tab5",
                                    Fragment_Contact.class, args)), getResources().getString(R.string.contact_tab_title),
                            R.drawable.tab_contact);
            Activity_Tab.mapTabInfo.put(tabInfo.tag, tabInfo);

            mTabHost.setOnTabChangedListener(this);

            if (Shared_Object.isOfflineMode) {

                mTabHost.setCurrentTabByTag("Tab4");
            } else {
                this.onTabChanged("Tab1");
            }
        }

    }

    private static void AddTab(Activity_Tab activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec, TabInfo tabInfo, String label, int drawable) {
        tabSpec.setContent(activity.new TabFactory(activity));

        View tabIndicator = LayoutInflater.from(activity).inflate(
                R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView txtTitle = (TextView) tabIndicator.findViewById(R.id.title);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);

        txtTitle.setText(label);
        txtTitle.setTextSize(Float.parseFloat(activity.getResources()
                .getString(R.string.size_pointer)));
        txtTitle.setTypeface(StaticUtils.getTypeface(activity,
                Font.THSarabanNew));
        icon.setImageResource(drawable);

        tabSpec.setIndicator(tabIndicator);

        tabHost.addTab(tabSpec);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("ActivityTab", "onSaveInstanceState");
        outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
        // selected
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e("ActivityTab", "onRestoreInstanceState");
        mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {

        Log.d("", "resume activity tab");
        if (AppMain.isphone) {
            if (AppMain.isGotoshelf) {
                Log.d("", "Go to shelf");
                if (AppMain.isphone) {
                    mTabHost.setCurrentTabByTag("Tab5");
                    AppMain.isGotoshelf = false;
                } else {
                    mTabHost.setCurrentTabByTag("Tab5");
                    AppMain.isGotoshelf = false;
                }
            }
        } else {

            if (AppMain.isGotoshelf) {
                Log.d("", "Go to shelf");
                if (AppMain.isphone) {
                    mTabHost.setCurrentTabByTag("Tab4");
                    AppMain.isGotoshelf = false;
                } else {
                    mTabHost.setCurrentTabByTag("Tab4");
                    AppMain.isGotoshelf = false;
                }
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("ActivityTab", "onPause");
        super.onPause();
    }


    public class TabInfo {
        public final String tag;
        private final Class<?> clss;
        private final Bundle args;
        public Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;

        }

    }

    class TabFactory implements TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    @Override
    public void onTabChanged(String tag) {
        AppMain.isShelf = false;

        if (AppMain.isphone) {

            TabInfo newTab = mapTabInfo.get(tag);
            if (mLastTab != newTab) {
                FragmentTransaction ft = this.getSupportFragmentManager()
                        .beginTransaction();
                // pause
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                    if (mLastTab.tag.equals("Tab1")) {
                        if (Fragment_Ebook.isChoice) {

                            Fragment_Ebook.isChoice = false;
                            Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
                            Fragment_Ebook.sCatIDs = "";
                            Fragment_Ebook.bCode = "";

                        }
                        if (getSupportFragmentManager()
                                .getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();

                        }
                        if (Fragment_Ebook.fragment_ebook != null) {
                            if (Fragment_Ebook.fragment_ebook.HaveFragment) {
                                if (Fragment_Ebook.fragment_ebook.frag_search != null) {
                                    ft.detach(Fragment_Ebook.fragment_ebook.frag_search);
                                }
                            }
                        }
                    }
                    if (mLastTab.tag.equals("Tab2")) {
                        if (Fragment_Publisher.fragment_Publisher != null) {
                            if (Fragment_Publisher.fragment_Publisher.HaveFragment) {
                                if (Fragment_Publisher.fragment_Publisher.frag_searchpublisher != null) {
                                    ft.detach(Fragment_Publisher.fragment_Publisher.frag_searchpublisher);
                                }
                            }
                        }
                    }
                }
                // resume
                if (newTab != null) {
                    if (newTab.tag.equals("Tab1")) {
                        if (Fragment_Ebook.fragment_ebook != null) {
                            if (Fragment_Ebook.fragment_ebook.HaveFragment) {
                                if (Fragment_Ebook.fragment_ebook.frag_search != null) {
                                    ft.attach(Fragment_Ebook.fragment_ebook.frag_search);
                                }
                                mLastTab = newTab;
                                ft.commit();
                                this.getSupportFragmentManager()
                                        .executePendingTransactions();

                                return;
                            }
                        }
                    }
                    if (newTab.tag.equals("Tab2")) {
                        if (Fragment_Publisher.fragment_Publisher != null) {
                            if (Fragment_Publisher.fragment_Publisher.HaveFragment) {
                                if (Fragment_Publisher.fragment_Publisher.frag_searchpublisher != null) {
                                    ft.attach(Fragment_Publisher.fragment_Publisher.frag_searchpublisher);
                                }
                                mLastTab = newTab;
                                ft.commit();
                                this.getSupportFragmentManager()
                                        .executePendingTransactions();

                                return;
                            }
                        }
                    }
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(this,
                                newTab.clss.getName(), newTab.args);
                        ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }

                }

                mLastTab = newTab;
                ft.commit();
                this.getSupportFragmentManager().executePendingTransactions();

            }
        } else {
            if (Fragment_Ebook.isTabletChoose) {

                Fragment_Ebook.isTabletChoose = false;
                Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
                Fragment_Ebook.sCatIDs = "";
                Fragment_Ebook.bCode = "";

            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();

            }
            // tablet
            TabInfo newTab = mapTabInfo.get(tag);
            if (mLastTab != newTab) {
                FragmentTransaction ft = this.getSupportFragmentManager()
                        .beginTransaction();
                // pause
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                // resume
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(this,
                                newTab.clss.getName(), newTab.args);
                        ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }

                }

                mLastTab = newTab;
                ft.commit();
                this.getSupportFragmentManager().executePendingTransactions();

            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (AppMain.isphone) {

            if (Fragment_Ebook.isChoice) {
                ProgressDialog dialog = new ProgressDialog(ebook.getActivity());
                dialog.show();
                Fragment_Ebook.isChoice = false;
                Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
                Fragment_Ebook.sCatIDs = "";
                Fragment_Ebook.bCode = "";

                if (ebook != null) {
                    ebook.setListViewMaster(AppMain.pList_default_ebook_master,
                            dialog);
                    return false;
                }
                return true;
            } else {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {

                        case KeyEvent.KEYCODE_BACK:
                            if (mLastTab.tag.equals("Tab3")) {
                                if (AppMain.pList_Subcategories != null
                                        && AppMain.pList_Subcategories_ebook != null) {
                                    Fragment_Category.category
                                            .setListView_SubCategory(
                                                    AppMain.pList_Subcategories,
                                                    null);
                                    AppMain.pList_Subcategories_ebook = null;

                                    return true;
                                }
                                if (AppMain.pList_Subcategories != null) {
                                    Fragment_Category.category.setListViewCategory(
                                            AppMain.pList_categories, null);
                                    AppMain.pList_Subcategories = null;

                                    return true;
                                }
                            }
                            try {

                                if (mLastTab.tag.equals("Tab1")) {

                                    if (Fragment_Ebook.fragment_ebook.HaveFragment) {
                                        FragmentTransaction ft = this
                                                .getSupportFragmentManager()
                                                .beginTransaction();

                                        if (Fragment_Ebook.fragment_ebook.frag_search != null) {
                                            ft.detach(Fragment_Ebook.fragment_ebook.frag_search);
                                        }
                                        if (Activity_Tab.mLastTab.fragment != null) {
                                            ft.attach(mLastTab.fragment);
                                        }
                                        ft.commit();
                                        this.getSupportFragmentManager()
                                                .executePendingTransactions();

                                        Fragment_Ebook.fragment_ebook.HaveFragment = false;
                                        return true;
                                    }

                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            if (mLastTab.tag.equals("Tab2")) {
                                if (Fragment_Publisher.fragment_Publisher.HaveFragment) {
                                    FragmentTransaction ft = this
                                            .getSupportFragmentManager()
                                            .beginTransaction();

                                    if (Fragment_Publisher.fragment_Publisher.frag_searchpublisher != null) {
                                        ft.detach(Fragment_Publisher.fragment_Publisher.frag_searchpublisher);
                                    }
                                    if (Activity_Tab.mLastTab.fragment != null) {
                                        ft.attach(mLastTab.fragment);
                                    }
                                    ft.commit();
                                    this.getSupportFragmentManager()
                                            .executePendingTransactions();

                                    Fragment_Publisher.fragment_Publisher.HaveFragment = false;
                                    return true;
                                }

                            }
                    }
                }
            }
        } else {
            if (Fragment_Ebook.isTabletChoose) {

                Fragment_Ebook.isTabletChoose = false;
                Adapter_List_Ebook_MasterDetail.phoneToDetail = false;
                Fragment_Ebook.sCatIDs = "";
                Fragment_Ebook.bCode = "";

                if (ebook != null) {
                    ebook.setViewPagerMaster(
                            AppMain.pList_default_ebook_master, null);
                    return false;
                }
                return true;
            }
            // tablet
        }

        return super.onKeyDown(keyCode, event);
    }

    public static void addFragment(FragmentTransaction ft, Fragment fragment,
                                   boolean addToBackStack, final int transition) {
        AppMain.isShelf = false;
        try {

            if (!fragment.isAdded()) {
                ft.replace(R.id.realtabcontent, fragment);
                ft.setTransition(transition);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
