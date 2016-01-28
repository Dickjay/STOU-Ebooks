package com.porar.ebooks.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.porar.ebooks.model.Model_EBook_Shelf_List;
import com.porar.ebooks.stou.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class StaticUtils {
    public static String TAG_DEBUG = "com.porar.ebooks";
    public static String TAG = "com.porar.ebooks";

    public static ArrayList<Map<String, Integer>> p_bid = new ArrayList<Map<String, Integer>>();
    public static ArrayList<Model_EBook_Shelf_List> arrShilf;
    private static boolean isfacebooklogin = false;
    public static Handler handler = new Handler();
    public static boolean isAMasterDegree = false;
    public static boolean isTeacher = false;
    public static boolean isBechalorDegree = false; // check Login
    public static int shelfID = 0; // hideLayout // if shelfID == 2 is
    // AMasterDegree
    public static int pageID = 0; // setShelf // if pageID == 2 is AMasterDegree
    public static int phonePage = 0; // setBGLayout for Phone // if phonePage ==
    // 2 is AMasterDegree
    public static int phoneValue = 0; // setValuePhone // if phoneValue == 2 is
    // AMasterDegree
    public static String deviceID = "", sCat = "", bCode = ""; // checkDeviceID
    public static boolean bookmark = false; // cehckBookmark
    public static int pid = 0;
    public static int Login = 0;

    public static ArrayList arrayList_BCODE = null;
    public static String temp_bcode = "";
    public static String urlInstruction = "";
    public static boolean useToFirstApp = true;

    public static boolean getFacebooklogin() {
        return isfacebooklogin;
    }

    public static void setFacebooklogin(boolean facebooklogin) {
        isfacebooklogin = facebooklogin;
    }

    public static void removeModelShelf(int pos) {

        arrShilf.remove(pos);
    }

    public static void newModelShelf() {
        arrShilf = new ArrayList<Model_EBook_Shelf_List>();
    }

    public static void setModelShelf(Model_EBook_Shelf_List each) {
        if (!arrShilf.contains(each)) {
            arrShilf.add(each);
        } else {
            arrShilf.remove(each);
        }

    }

    public static Model_EBook_Shelf_List getModelShelf(int pos) {
        return arrShilf.get(pos);
    }

    private static boolean remove;

    public static void setBID(int bid, int pageindex) {

        for (Map<String, Integer> each : p_bid) {
            for (Entry<String, Integer> getBP : each.entrySet()) {
                if (getBP.getKey().equals(String.valueOf(bid))) {
                    p_bid.remove(each);
                    remove = true;
                }
            }
            if (remove) {
                break;
            }

        }

        Map<String, Integer> new_map = new HashMap<String, Integer>();
        new_map.put(String.valueOf(bid), pageindex);
        p_bid.add(new_map);
        remove = false;

    }

    public static int getPageIndex(String BID) {
        for (Map<String, Integer> each : p_bid) {
            for (Entry<String, Integer> getBP : each.entrySet()) {
                if (getBP.getKey().equals(BID)) {
                    return getBP.getValue();
                }
            }
        }

        return 0;

    }

    public static enum Font {
        DBHelvethaicaXv3_2(1), DBSaiKrokX(2), LayijiMahaniyomV105ot(3), THSarabanNew(
                4), TBold(5), TBoldSpecial(6), TLight(7), TMedium(8), DB_Helvethaica_X_Med(
                9), DB_HelvethaicaMon_X(10), DB_Ozone_X_Li(11);

        private Font(int index) {
        }
    }

    ;

    public static enum WebViewBasePath {
        ApplicationPath(1), AssetsPath(2), AssetsImages(3), AssetsFonts(4), AssetsjQuery(
                5), UrlPath(6);

        private WebViewBasePath(int index) {
        }
    }

    ;

    public static ArrayList<String> listSocial = null;

    public static void setSocial(ArrayList<String> arrayListSocial) {
        listSocial = arrayListSocial;
    }

    public static ArrayList<String> getSocial() {
        return listSocial;

    }

    public static String Base64toString(byte[] data) {
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;
    }

    public static String DecodeBase64toString(String base64) {
        String str = null;
        try {
            byte[] decode_data = Base64.decode(base64, Base64.DEFAULT);
            str = new String(decode_data, "UTF-8");
            return str;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;

    }

    public static Bitmap getBitmapFromDrawableId(Context context, int drawableId) {
        Bitmap bitmap = null;
        try {
            BitmapDrawable drawable = (BitmapDrawable) context.getResources()
                    .getDrawable(drawableId);
            bitmap = drawable.getBitmap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        // Log.d(AppMain.getTag(), "width  " + width);
        // Log.d(AppMain.getTag(), "height " + height);

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public static String getDeviceName() {
        String model = Build.MODEL;

        return model;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getStringFromUrl(String url) {
        String response = null;
        InputStream is = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            int statuscode = httpResponse.getStatusLine().getStatusCode();
            if (statuscode == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                is = entity.getContent();
            } else {
                return null;
            }

            BufferedReader inputStreamReader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-9"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            response = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML

        return response;
    }

    public static String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = null;
        inputStream = ctx.getResources().openRawResource(resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public static Typeface getTypeface(Context mContext, Font font) {
        String fontPath = "font/THSarabunNew.ttf";
        switch (font) {
            case DB_Helvethaica_X_Med:
                fontPath = "font/DB_Helvethaica_X_Med.ttf";
                break;
            case DB_HelvethaicaMon_X:
                fontPath = "font/DB_HelvethaicaMon_X.ttf";
                break;
            case DB_Ozone_X_Li:
                fontPath = "font/DB_Ozone_X_Li.ttf";
                break;
            case DBHelvethaicaXv3_2:
                fontPath = "font/DBHelvethaicaXv3.2.ttf";
                break;
            case DBSaiKrokX:
                break;
            case LayijiMahaniyomV105ot:
                fontPath = "font/LayijiMahaniyomV105ot.otf";
                break;
            case TBold:
                break;
            case TBoldSpecial:
                break;
            case THSarabanNew:
                fontPath = "font/THSarabunNew.ttf";

                break;
            case TLight:
                break;
            case TMedium:
                break;

            // DB_Helvethaica_X_Med(9), DB_HelvethaicaMon_X(10), DB_Ozone_X_Li(11)
        }
        return Typeface.createFromAsset(mContext.getAssets(), fontPath);
    }

    public static String escapeHTML(String txt) {
        txt = URLEncoder.encode(txt);
        String[] specialChar = new String[]{"+", "&", "\"", "<", ">", "[",
                "]"};
        String[] replaceChar = new String[]{"%20", "&amp;", "&quot;", "&lt;",
                "&gt;", "%5B", "%5D"};
        for (int i = 0; i < replaceChar.length; i++) {
            txt = txt.replace(specialChar[i], replaceChar[i]);
        }
        return txt;
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
        System.gc();
    }

    public static ArrayList<View> History = new ArrayList<View>();

    public static WebView setWebViewCover(Context context, WebView view,
                                          WebViewBasePath basePath, String fileName) {

        switch (basePath) {
            case ApplicationPath:
                fileName = "file://" + context.getFilesDir() + "/" + fileName;
                // Log.i(AppMain.getTag(), "Image : " + fileName);
                break;
            case AssetsPath:
                fileName = "file:///android_asset/" + fileName;
                break;
            case AssetsFonts:
                fileName = "file:///android_asset/fonts/" + fileName;
                break;
            case AssetsImages:
                fileName = "file:///android_asset/images/" + fileName;
                break;
            case AssetsjQuery:
                fileName = "file:///android_asset/jquery/" + fileName;
                break;
        }

        String readRawTextFile = StaticUtils.readRawTextFile(context,
                R.raw.bg_cover_html);
        readRawTextFile = readRawTextFile.replace("{{{url}}}", fileName);
        readRawTextFile = readRawTextFile.replace("{{{jqueryPath}}}",
                context.getFilesDir() + "/assets/jquery/jquery.js");

        view.getSettings().setJavaScriptEnabled(true);
        view.setDrawingCacheEnabled(true);
        view.setAlwaysDrawnWithCacheEnabled(true);

        view.setHorizontalScrollBarEnabled(false);
        view.setVerticalScrollBarEnabled(false);
        view.setScrollbarFadingEnabled(false);

        view.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        switch (basePath) {
            case ApplicationPath:
                view.loadDataWithBaseURL("file://" + context.getFilesDir() + "/",
                        readRawTextFile, "text/html", "UTF-8", "");
                break;
            case AssetsPath:
                view.loadDataWithBaseURL("file:///android_asset/", readRawTextFile,
                        "text/html", "UTF-8", "");
                break;
            case AssetsFonts:
                view.loadDataWithBaseURL("file:///android_asset/fonts/",
                        readRawTextFile, "text/html", "UTF-8", "");
                break;
            case AssetsImages:
                view.loadDataWithBaseURL("file:///android_asset/images/",
                        readRawTextFile, "text/html", "UTF-8", "");
                break;
            case AssetsjQuery:
                view.loadDataWithBaseURL("file:///android_asset/jquery/",
                        readRawTextFile, "text/html", "UTF-8", "");
                break;
            case UrlPath:
                view.loadData(readRawTextFile, "text/html", "UTF-8");
                break;
        }

        return view;
    }

    public static SwipeRefreshLayout setColorSwipeReresh(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(R.color.textColorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        return swipeRefreshLayout;
    }

    public static ArrayList splitStringArray(String bCode) {
        String bookCode = bCode;
        ArrayList arrayList_BCODE = new ArrayList(Arrays.asList(bookCode.split(",")));
        for (int i = 0; i < arrayList_BCODE.size(); i++) {
//            Log.d(MySharedPref.TAG_BCODE, "BCODE =" + arrayList_BCODE.get(i).toString());
        }
        return arrayList_BCODE;
    }

    static int current = 0;

    public static void setCurrentTab(int currentTab) {
        current = currentTab;
    }

    public static int getCurrent() {
        return current;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static Picasso picasso;
    public static boolean setSingletonInstance = false;

    public static void initPicasso(Context context) {
        if (!setSingletonInstance) {
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            picassoBuilder.memoryCache(new LruCache(24000));
            picassoBuilder.downloader(new OkHttpDownloader(new OkHttpClient()));
            Picasso built = picassoBuilder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
            picasso = built;
            picasso.setSingletonInstance(built);
            setSingletonInstance = true;
        }
    }
}