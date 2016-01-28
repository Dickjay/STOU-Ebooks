package com.porar.ebook.control;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.porar.ebook.adapter.Adapter_List_Ebook;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_EbookList;
import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.widget.AdjustableImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import plist.xml.PList;

/**
 * Created by Porar on 10/12/2015.
 */
public class Dialog_SearchEbooks extends Dialog {
    private Context context;
    private Fragment fragment;

    ProgressDialog progressDialog;
    ArrayList<Model_EbookList> ebookLists_search;
    AsyncTask_FetchAPI asyncTask_FetchAPI;
    public static String tag = "Fragment_Search";
    ListView search_list;
    AdjustableImageView back_imageview;
    FragmentActivity myActivity;
    EditText et_search;
    int actionX, actionY;
    Drawable drawble[];
    OnClickDrawBle clickDrawBle;
    private boolean check = false;
    String keyword = "";
    Handler handler = new Handler();
    AlertDialog alertDialog;
    Adapter_List_Ebook adapter_List_Ebook;
    Drawable right;
    private boolean is_drawblesearch = false;

    public void setOnClickDrawBle(OnClickDrawBle drawBle) {
        clickDrawBle = drawBle;
    }

    public Dialog_SearchEbooks(Context context, int theme, Fragment fragment) {
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
        setContentView(R.layout.dialog_search_ebooks_phone);

        et_search = (EditText) findViewById(R.id.search_edittext);
        search_list = (ListView) findViewById(R.id.search_plistView1);
        back_imageview = (AdjustableImageView) findViewById(R.id.back_imageview);
        et_search.setInputType(InputType.TYPE_CLASS_TEXT);
        et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getEditableText().toString().length() > 0) {
                    if (event != null && event.getAction() != KeyEvent.ACTION_DOWN)
                        return check;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        performClick(v.getEditableText().toString());
                    check = true;
                }

                return check;
            }

        });
        setMethodDrwable();
        onClickInterface();
        if (adapter_List_Ebook != null) {
            search_list.setAdapter(adapter_List_Ebook);
        }
    }

    private void onClickInterface() {

        this.setOnClickDrawBle(new OnClickDrawBle() {

            @Override
            public void ClickDeawbleRight() {

                if (is_drawblesearch) {
                    performClick(et_search.getText().toString());
                } else {
                    et_search.setText("");
                }
            }
        });

        back_imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    private void setMethodDrwable() {
        // drawable right only
        drawble = et_search.getCompoundDrawables();
        et_search.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                Rect bounds;
                actionX = (int) ev.getX();
                actionY = (int) ev.getY();
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    for (int i = 0; i < drawble.length; i++) {
                        if (drawble[i] != null) {
                            bounds = null;
                            bounds = drawble[i].getBounds();
                            // drawble[i].getBounds().right != 0
                            int x, y;
                            int extraTapArea = 13;

                            x = actionX + extraTapArea;
                            y = actionY - extraTapArea;
                            x = et_search.getWidth() - x;

                            if (x <= 0) {
                                x += extraTapArea;
                            }

                            if (y <= 0)
                                y = actionY;

                            if (bounds.contains(x, y) && clickDrawBle != null) {
                                clickDrawBle.ClickDeawbleRight();
                                ev.setAction(MotionEvent.ACTION_CANCEL);
                                return false;
                            }
                        }
                    }

                }

                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (str.length() > 0) {
                    right = myActivity.getResources().getDrawable(R.drawable.picon_searchdrak);
                    is_drawblesearch = true;
                } else {
                    right = myActivity.getResources().getDrawable(R.drawable.content_remove);
                    is_drawblesearch = false;
                }

                et_search.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void performClick(String text) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                progressDialog = ProgressDialog.show(myActivity, "Download", "downloading", true, true);
            }
        });

        keyword = text;

        ebookLists_search = new ArrayList<Model_EbookList>();
        asyncTask_FetchAPI = new AsyncTask_FetchAPI();
        asyncTask_FetchAPI.setOnFetchListener(new OnFetchAPIListener() {

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        progressDialog.dismiss();

                        alertDialog = new AlertDialog.Builder(myActivity).create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage("A problem occurred while Internet Connection");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                System.gc();
                                RefreshloadperformClick();

                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pid = android.os.Process.myPid();
                                android.os.Process.killProcess(pid);
                                Runtime r = Runtime.getRuntime();

                                r.gc();
                                System.gc();
                            }
                        });
                        alertDialog.show();

                    }
                });

            }

            @Override
            public void onFetchStart(String apiURL, int currentIndex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFetchError(String apiURL, int currentIndex, Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        progressDialog.dismiss();

                        alertDialog = new AlertDialog.Builder(myActivity).create();
                        alertDialog.setTitle(AppMain.getTag());
                        alertDialog.setMessage("A problem occurred while Internet Connection");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                System.gc();
                                RefreshloadperformClick();

                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                });

            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex, final PList result) {
                Log.e("search onFetchComplete", apiURL);

                // for (PListObject each : (Array) result.getRootElement()) {
                // ebookLists_search.add(new Model_EbookList(each));
                // }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter_List_Ebook = new Adapter_List_Ebook(myActivity, 0, result,Dialog_SearchEbooks.this);
                        search_list.setAdapter(adapter_List_Ebook);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();

                        }
                    }
                });

            }

            @Override
            public void onAllTaskDone() {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });
        try {
            asyncTask_FetchAPI.execute(
                    AppMain.getAPIbyRefKey("search", "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&page=1&count=25"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

    }

    private void RefreshloadperformClick() {
        if (keyword != null) {
            performClick(keyword);
        } else {

        }

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

    interface OnClickDrawBle {
        void ClickDeawbleRight();
    }

}
