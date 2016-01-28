package com.porar.ebook.control;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.porar.ebooks.event.OnFetchAPIListener;
import com.porar.ebooks.model.Model_ShowCase_View;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.AsyncTask_FetchAPI;
import com.porar.ebooks.utils.MySharedPref;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.widget.AdjustableImageView;

import java.util.ArrayList;
import java.util.HashMap;

import plist.type.Array;
import plist.xml.PList;
import plist.xml.PListObject;

/**
 * Created by Porar on 10/12/2015.
 */
public class Dialog_ShowCase_View extends Dialog implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private Context context;
    private Fragment fragment;

    private SliderLayout mSliderCaseView;
    private PagerIndicator pagerIndicator;
    private String SHOWCASE_VIEW = "showcase_view";
    private HashMap<String, String> map_showcase_view;

    private AsyncTask_FetchAPI asyncTask_fetchAPI;

    private ArrayList<Model_ShowCase_View> array_showcase_view;
    private AdjustableImageView close_dialog;

    public Dialog_ShowCase_View(Context context, int theme, Fragment fragment) {
        super(context, theme);
        this.context = context;
        this.fragment = fragment;
        setStyleDialog();
        initView();
    }

    private void setStyleDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setFullScreenDialog();
    }

    private void initView() {
        setContentView(R.layout.dialog_showcase_view);
        mSliderCaseView = (SliderLayout) findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        close_dialog = (AdjustableImageView) findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        downloadShowCase();
    }

    private void downloadShowCase() {

        asyncTask_fetchAPI = new AsyncTask_FetchAPI();
        asyncTask_fetchAPI.setOnFetchListener(new OnFetchAPIListener() {
            @Override
            public void onFetchStart(String apiURL, int currentIndex) {

            }

            @Override
            public void onFetchComplete(String apiURL, int currentIndex, PList result) {
                array_showcase_view = new ArrayList<Model_ShowCase_View>();
                for (PListObject pListObject : (Array) result.getRootElement()) {
                    Model_ShowCase_View model_showCase_view = new Model_ShowCase_View(pListObject);
                    array_showcase_view.add(model_showCase_view);
                }
            }

            @Override
            public void onFetchError(String apiURL, int currentIndex, Exception e) {

            }

            @Override
            public void onTimeOut(String apiURL, int currentIndex) {

            }

            @Override
            public void onAllTaskDone() {

                if (StaticUtils.useToFirstApp) {
                    if (array_showcase_view != null && array_showcase_view.size() > 0) {
                        setFirstAPP();
                        map_showcase_view = addShowCaseList(map_showcase_view, array_showcase_view);
                        configSliderShowCase(mSliderCaseView);
                        setupShowCaseView(mSliderCaseView, map_showcase_view);
                    } else {
                        dismissDialog();
                    }
                } else {
                    String urlInstruction = "";
                    if (!StaticUtils.urlInstruction.equals("")) {
                        if (array_showcase_view != null && array_showcase_view.size() > 0) {
                            for (int i = 0; i < array_showcase_view.size(); i++) {
                                urlInstruction += array_showcase_view.get(i).getURL();
                            }
                            // if url equals link from server , dialog will dismiss but if don't like url we will save new url to devices
                            if (StaticUtils.urlInstruction.equals(urlInstruction)) {
                                dismissDialog();
                            }else{
                                setFirstAPP();
                                map_showcase_view = addShowCaseList(map_showcase_view, array_showcase_view);
                                configSliderShowCase(mSliderCaseView);
                                setupShowCaseView(mSliderCaseView, map_showcase_view);
                            }
                        }else{
                            dismissDialog();
                        }

                    }else{
                        dismissDialog();
                    }

                }

            }

            public void setFirstAPP() {
                String urlInstruction = "";
                for (int i = 0; i < array_showcase_view.size(); i++) {
                    urlInstruction += array_showcase_view.get(i).getURL();
                }
                MySharedPref mySharedPref = new MySharedPref(context);
                mySharedPref.setUsedToFirstApp(MySharedPref.TAG_USE_TO_FIRST_APP, false);
                mySharedPref.setUrlInstruction(MySharedPref.TAG_INSTRUCTION, urlInstruction);
                StaticUtils.urlInstruction = mySharedPref.getUrlInstruction(MySharedPref.TAG_INSTRUCTION);
            }
        });


        // SHOWCASE_VIEW
        String urlIMG = "http://203.150.225.223/stoubookapi/api/get_instruction_list.php";
        asyncTask_fetchAPI.execute(urlIMG);
    }


    private HashMap<String, String> addShowCaseList(HashMap<String, String> map_showcase_view, ArrayList<Model_ShowCase_View> array_showcase_view) {
        map_showcase_view = new HashMap<String, String>();
        for (int size = 0; size < array_showcase_view.size(); size++) {
            map_showcase_view.put("showcase_view" + size, array_showcase_view.get(size).getURL());
            Log.e("showcase_view", "SIZE =" + size);
        }
        return map_showcase_view;
    }

    private void configSliderShowCase(SliderLayout mSliderCaseView) {
        mSliderCaseView.setPresetTransformer(SliderLayout.Transformer.Stack);
        mSliderCaseView.setCustomAnimation(new DescriptionAnimation());
        mSliderCaseView.setDuration(15000);
        mSliderCaseView.addOnPageChangeListener(this);
        mSliderCaseView.setCustomIndicator(pagerIndicator);
    }

    private void setupShowCaseView(SliderLayout mSliderShowCase, HashMap<String, String> map_showcase_view) {
        for (String showcase_name : map_showcase_view.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView
                    .image(map_showcase_view.get(showcase_name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString(SHOWCASE_VIEW, showcase_name);
            mSliderShowCase.addSlider(defaultSliderView);
        }
        show();
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
            window.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.white_transaent)));
            window.setAttributes(lp);
            window.getAttributes().windowAnimations = R.style.FadeInOutDialog;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
