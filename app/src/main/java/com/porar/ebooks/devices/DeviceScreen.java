package com.porar.ebooks.devices;

import android.content.Context;
import android.content.res.Configuration;

import com.porar.ebooks.stou.AppMain;

/**
 * Created by Porar on 9/25/2015.
 */
public class DeviceScreen {
    public void setScreenSize(Context context) {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            AppMain.setScreen(AppMain.ScreenSize.Large);
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            AppMain.setScreen(AppMain.ScreenSize.Normal);
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            AppMain.setScreen(AppMain.ScreenSize.Small);
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            AppMain.setScreen(AppMain.ScreenSize.XLarge);
        }
    }
}
