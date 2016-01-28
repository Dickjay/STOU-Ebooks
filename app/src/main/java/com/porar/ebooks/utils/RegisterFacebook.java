package com.porar.ebooks.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import java.util.Arrays;
import java.util.List;

public class RegisterFacebook {

    public static AccessToken accessToken;
    public static CallbackManager mCallbackManager;
    public static AccessTokenTracker accessTokenTracker;
    public static ProfileTracker profileTracker;
    public static List<String> PUBLISH_PERMISSION = Arrays.asList("publish_actions");

    public static boolean isLoad = false;

    public static boolean isLogin() {
        try {
            return AccessToken.getCurrentAccessToken() != null;
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            return false;
            // TODO: handle exception
        }

    }

    public static boolean hasPublishPermission() {
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            return accessToken != null
                    && accessToken.getPermissions().contains("publish_actions");
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            return false;
            // TODO: handle exception
        }

    }

    public static void InitFacebookSDK(Context context) {

        if (context != null) {
            if (!isLoad) {
                try {
                    FacebookSdk.sdkInitialize(context);
                    mCallbackManager = CallbackManager.Factory.create();
                    isLoad = true;
                } catch (Exception e) {
                    isLoad = false;
                    Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
                    return;
                    // TODO: handle exception
                }
            }

            AppEventsLogger.activateApp(context);
            if (!isLogin()) {
                trackingAccessToken();
                trackingProfile();
            }
            if (accessTokenTracker != null) {
                accessTokenTracker.startTracking();
                Log.d(StaticUtils.TAG, "accessTokenTracker");
            }
            if (profileTracker != null) {
                profileTracker.startTracking();
                Log.d(StaticUtils.TAG, "profileTracker");
            }

        }

        return;

    }
    public static void trackingAccessToken(){
        accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (oldAccessToken != null && currentAccessToken != null) {
                    if (!oldAccessToken.equals(currentAccessToken)) {
                        oldAccessToken = currentAccessToken;
                        Log.d(StaticUtils.TAG, "Access-Token is: " + oldAccessToken.getToken().toString());
                    }
                }

            }
        };
    }
    public static void trackingProfile(){
        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
                if (oldProfile != null && currentProfile != null) {
                    if (!oldProfile.equals(currentProfile)) {
                        oldProfile = currentProfile;
                        Log.d(StaticUtils.TAG,
                                "Profile is: " + oldProfile.getName());
                    }
                }

            }
        };
    }


    public static void ReadPermissionFacebookActivity(Activity activity) {
        try {
            if (activity != null) {
                LoginManager.getInstance().logInWithReadPermissions(
                        activity,
                        Arrays.asList("public_profile", "email",
                                "user_birthday"));
            }
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

    public static void ReadPublishPermissionFacebookActivity(Activity activity) {
        try {
            if (activity != null) {
                LoginManager.getInstance().logInWithPublishPermissions(
                        activity, PUBLISH_PERMISSION);
            }
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

    public static void ReadPermissionFacebookFragment(Fragment fragment) {
        try {
            if (fragment != null) {
                LoginManager.getInstance().logInWithReadPermissions(
                        fragment,
                        Arrays.asList("public_profile", "email",
                                 "user_birthday"));
            }

        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

    public static void ReadPublishPermissionFacebookFragment(Fragment fragment) {
        try {
            if (fragment != null) {
                LoginManager.getInstance().logInWithPublishPermissions(
                        fragment, PUBLISH_PERMISSION);
            }

        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

    public static void FacebookLogout() {

        StaticUtils.setFacebooklogin(false);
        resetValue();
        stopProfileTracker();
        stopAccessTokenTracker();
        logoutFacebook();
    }

    public static void resetValue(){
        StaticUtils.Login = 0;
        StaticUtils.isAMasterDegree = false;
        StaticUtils.shelfID = 0;
        StaticUtils.pageID = 0;
        StaticUtils.phonePage = 0;
        StaticUtils.phoneValue = 0;
    }

    public static void stopProfileTracker() {
        try {
            if (profileTracker != null && profileTracker.isTracking()) {
                profileTracker.stopTracking();
            }

        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }
    }

    public static void stopAccessTokenTracker() {
        try {
            if (accessTokenTracker != null && accessTokenTracker.isTracking()) {
                accessTokenTracker.stopTracking();
            }
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }
    }

    public static void logoutFacebook() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }
    }

    public static void unRegisterFacebook(Context context) {
        try {
            if (context != null) {
                AppEventsLogger.deactivateApp(context);
            }

        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

    public static void CallbackResult(int requestCode, int resultCode,
                                      Intent data) {
        try {
            if (data != null) {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Log.e(StaticUtils.TAG_DEBUG, e.getMessage().toString());
            // TODO: handle exception
        }

    }

}
