package com.porar.ebooks.stou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.porar.ebooks.stou.R;
import com.porar.ebooks.utils.StaticUtils;
import com.porar.ebooks.utils.StaticUtils.Font;

public class Fragment_Contact extends Fragment {

    TextView headerTextView;
    TextView detailsTextView;
    TextView linkTextView;
    TextView titleTextView;
    TextView versionAppTextView;

    Toolbar toolbar;

    public static Fragment_Contact newInstance() {
        Fragment_Contact fragment = new Fragment_Contact();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        headerTextView = (TextView) view.findViewById(R.id.contact_textView);
        detailsTextView = (TextView) view.findViewById(R.id.details_textView);
        linkTextView = (TextView) view.findViewById(R.id.link_textView);
        titleTextView = (TextView) view.findViewById(R.id.title_textView);
        versionAppTextView = (TextView) view.findViewById(R.id.version_app_textView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headerTextView.setTypeface(StaticUtils.getTypeface(getActivity(), Font.DB_HelvethaicaMon_X));
        detailsTextView.setTypeface(StaticUtils.getTypeface(getActivity(), Font.DB_HelvethaicaMon_X));
        linkTextView.setTypeface(StaticUtils.getTypeface(getActivity(), Font.DB_HelvethaicaMon_X));
        titleTextView.setTypeface(StaticUtils.getTypeface(getActivity(), Font.DB_HelvethaicaMon_X));
        versionAppTextView.setTypeface(StaticUtils.getTypeface(getActivity(), Font.DB_HelvethaicaMon_X));
        linkTextView.setClickable(true);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + getString(R.string.contact_link) + " '> " + getString(R.string.contact_link) + " </a>";
        linkTextView.setText(Html.fromHtml(text));
        versionAppTextView.setText(getResources().getString(R.string.version_app));
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
