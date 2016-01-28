package com.porar.ebooks.stou.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.porar.ebook.adapter.Adapter_List_BookShelf;
import com.porar.ebook.adapter.MyAdapterViewPager_BookShelf;
import com.porar.ebooks.devices.DeviceScreen;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.Shared_Object;
import com.porar.ebooks.stou.fragment.Fragment_Ebook;
import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.StaticUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressLint("NewApi")
public class Activity_Preload extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenSize();
        initDownloader();
        setVerionAPI();
        getConfiguration();
        setEbookShelf();
        StaticUtils.newModelShelf();
        checkLoginFacebook();
        printKeyHash();
        getDataSharePref();
    }

    public void initDownloader() {
        StaticUtils.initPicasso(Activity_Preload.this);
    }


    private void getScreenSize() {
        DeviceScreen deviceScreen = new DeviceScreen();
        deviceScreen.setScreenSize(Activity_Preload.this);
    }

    private void getConfiguration() {
        Shared_Object.loadConfigData(Activity_Preload.this);
    }

    private void setEbookShelf() {
        MyAdapterViewPager_BookShelf.seteNumtype(MyAdapterViewPager_BookShelf.Shelf.news);
        Adapter_List_BookShelf.seteNumtypePhone(Adapter_List_BookShelf.ShelfPhone.news);
        MyAdapterViewPager_BookShelf.setEdittextsearch("");
        AppMain.pList_default_ebookshelf = null;
        Fragment_Ebook.fragment_ebook = null;
    }

    private void checkLoginFacebook() {
        if (!Shared_Object.getCustomerDetail.getFacebookId().equals("0") && !Shared_Object.getCustomerDetail.getFacebookId().equals("")) {
            StaticUtils.setFacebooklogin(true);
        } else {
            StaticUtils.setFacebooklogin(false);
        }
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.porar.ebooks.stou", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
                StaticUtils.deviceID = Base64.encodeToString(md.digest(),
                        Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void setVerionAPI() {
        int version_code = android.os.Build.VERSION.SDK_INT;
        if (version_code > android.os.Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void getEbookItem() {
        if (Shared_Object.checkInternetConnection(Activity_Preload.this)) {
            Call_ActivityTab();
        } else {
            Shared_Object.isOfflineMode = true;
            Call_ActivityTab();
        }
    }

    private void getDataSharePref() {
        MySharedPref mySharedPref = new MySharedPref(getApplicationContext());
        StaticUtils.useToFirstApp = mySharedPref.getUsedToFirstApp(MySharedPref.TAG_USE_TO_FIRST_APP);
        StaticUtils.arrayList_BCODE = mySharedPref.getBCodeList(MySharedPref.TAG_BCODE, StaticUtils.temp_bcode);
        StaticUtils.urlInstruction = mySharedPref.getUrlInstruction(MySharedPref.TAG_INSTRUCTION);
    }

    public void Call_ActivityTab() {
        Intent intents = new Intent(Activity_Preload.this, Activity_MainTab.class);
        Activity_Preload.this.startActivity(intents);
        Activity_Preload.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEbookItem();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Runtime r = Runtime.getRuntime();
        r.gc();
        System.gc();
    }
}

