package com.porar.ebooks.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Porar on 10/12/2015.
 */
public class MySharedPref {

    private Context context;

    private SharedPreferences sharedpreferences;

    // tag bcode
    public final String MyPREFERENCES_BCODE = "BCODE-LIST";
    public static final String TAG_BCODE = "tag_bcode";

    // tag use to first app
    public final String MyPREFERENCES_FIRST_IMPRESSION = "FIRST-IMPRESSION";
    public static final String TAG_USE_TO_FIRST_APP = "tag_use_to_first_app";

    // tag instruction
    public final String MyPREFERENCES_UR_INSTRUCTION = "URL-INSTRUCTION";
    public static final String TAG_INSTRUCTION = "tag_instruction";

    public MySharedPref(Context context) {
        this.context = context;
    }

    public void setUsedToFirstApp(String TAG_USE_TO_FIRST_APP, boolean usedToFirstApp) {
        sharedpreferences = this.context.getSharedPreferences(MyPREFERENCES_FIRST_IMPRESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(TAG_USE_TO_FIRST_APP, usedToFirstApp);
        editor.commit();
    }

    public boolean getUsedToFirstApp(String TAG_USE_TO_FIRST_APP) {
        SharedPreferences prefs = this.context.getSharedPreferences(MyPREFERENCES_FIRST_IMPRESSION, Context.MODE_PRIVATE);
        return prefs.getBoolean(TAG_USE_TO_FIRST_APP, true);
    }

    public void setUrlInstruction(String TAG_INSTRUCTION, String urlInstruction) {
        sharedpreferences = this.context.getSharedPreferences(MyPREFERENCES_UR_INSTRUCTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TAG_INSTRUCTION, urlInstruction);
        editor.commit();
    }

    public String getUrlInstruction(String TAG_INSTRUCTION) {
        SharedPreferences prefs = this.context.getSharedPreferences(MyPREFERENCES_UR_INSTRUCTION, Context.MODE_PRIVATE);
        try {
            Log.d(TAG_INSTRUCTION, "URL =" + prefs.getString(TAG_INSTRUCTION, ""));
            return prefs.getString(TAG_INSTRUCTION, "");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setBCodeList(String TAG_BCODE, String bCode) {
        sharedpreferences = this.context.getSharedPreferences(MyPREFERENCES_BCODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TAG_BCODE, bCode);
        editor.commit();
    }

    public ArrayList getBCodeList(String TAG_BCODE, String bcode_list) {
        ArrayList arrayList_BCODE;
        SharedPreferences prefs = this.context.getSharedPreferences(MyPREFERENCES_BCODE, Context.MODE_PRIVATE);
        try {
            bcode_list = prefs.getString(TAG_BCODE, "");
            Log.d(TAG_BCODE, "BCODE =" + bcode_list);
            arrayList_BCODE = StaticUtils.splitStringArray(bcode_list);
        } catch (NullPointerException e) {
            bcode_list = "";
            Log.d(TAG_BCODE, "BCODE =" + bcode_list);
            arrayList_BCODE = StaticUtils.splitStringArray(bcode_list);
            e.printStackTrace();
        }
        return arrayList_BCODE;
    }


//    public void setStringArrayPref() {
//        SharedPreferences prefs = this.context.getSharedPreferences(MyPREFERENCES_URL_INSTRUCTION, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < array_instruction.size(); i++) {
//            jsonArray.put(array_instruction.get(i).getURL());
//        }
//        if (!array_instruction.isEmpty()) {
//            editor.putString(TAG_INSTRUCTION, jsonArray.toString());
//        } else {
//            editor.putString(TAG_INSTRUCTION, null);
//        }
//        editor.commit();
//    }

//    public ArrayList<String> getStringArrayPref() {
//        SharedPreferences prefs = this.context.getSharedPreferences(MyPREFERENCES_URL_INSTRUCTION, Context.MODE_PRIVATE);
//        String jsonString = prefs.getString(TAG_INSTRUCTION, null);
//        ArrayList<String> urls_json = new ArrayList<String>();
//        if (jsonString != null) {
//            try {
//                JSONArray a = new JSONArray(jsonString);
//                for (int i = 0; i < a.length(); i++) {
//                    String url = a.optString(i);
//                    urls_json.add(url);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return urls_json;
//    }

}
