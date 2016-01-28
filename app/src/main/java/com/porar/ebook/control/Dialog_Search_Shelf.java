package com.porar.ebook.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porar.ebook.adapter.Adapter_List_Ebook;
import com.porar.ebooks.event.DrawableClickListener;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import plist.xml.PList;

/**
 * Created by Porar on 10/12/2015.
 */
public abstract class Dialog_Search_Shelf extends Dialog {
    private Context context;
    private Fragment fragment;
    FragmentActivity myActivity;

    private LinearLayout layout_search;
    private CustomEditText et_search;
    //	private ListView lv_search;
    private Drawable imgL, imgR;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    private int id_head = 0;
    private String lastEdittext = "";

    public Dialog_Search_Shelf(Context context, int theme, Fragment fragment) {
        super(context, theme);
        this.context = context;
        this.fragment = fragment;
        myActivity = fragment.getActivity();
        setStyleDialog();
        initView();
    }

    private void setStyleDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setFullScreenDialog();
    }

    private void initView() {
        setContentView(R.layout.layout_search_bookshelf);
        et_search = (CustomEditText)findViewById(R.id.search_et_search);
        et_search.setTypeface(StaticUtils.getTypeface(context, StaticUtils.Font.DB_Helvethaica_X_Med));
        et_search.setInputType(InputType.TYPE_CLASS_TEXT);


        et_search.setDrawableClickListener(new DrawableClickListener() {

            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        // do something
//					performClick(et_search.getEditableText().toString());
                        break;

                    case RIGHT:
                        // do something
                        et_search.setText("");
                        break;
                    case BOTTOM:
                        break;
                    case TOP:
                        break;
                    default:
                        break;
                }
            }

        });

        et_search.setText(lastEdittext);

        onFinishInflateSearchBookshelf(et_search);
    }




    private void dismissDialog() {
        dismiss();
    }

    private void setFullScreenDialog() {
        try {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = getWindow();
            lp.copyFrom(window.getAttributes());
            // This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.TOP;
            try {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } catch (NullPointerException e) {
                window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            }

            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.getAttributes().windowAnimations = R.style.FadeInOutDialog;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public abstract void onFinishInflateSearchBookshelf(EditText searchEditText);
}
