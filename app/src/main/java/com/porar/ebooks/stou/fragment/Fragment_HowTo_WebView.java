package com.porar.ebooks.stou.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.widget.AdjustableImageView;

/**
 * Created by Porar on 10/12/2015.
 */
public class Fragment_HowTo_WebView extends Fragment {

    private WebView mWebView;
    private AdjustableImageView back_imageview;
    private String urlHowto = "http://203.150.225.223/stoubookapi/howtoapi.aspx";

    public static Fragment newInstance() {
        Fragment_HowTo_WebView fragment_howTo_webView = new Fragment_HowTo_WebView();
        return fragment_howTo_webView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_howto_webview, container, false);
        mWebView = (WebView) view.findViewById(R.id.howto_webview);

        back_imageview = (AdjustableImageView) view.findViewById(R.id.back_imageview);
        if (AppMain.isphone) {
            back_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
        }

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        // webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        mWebView.setInitialScale(1);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.loadUrl(urlHowto);
        mWebView.setWebViewClient(new MyAppWebViewClient());
        return view;
    }

    public class MyAppWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().length() == 0) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
}
