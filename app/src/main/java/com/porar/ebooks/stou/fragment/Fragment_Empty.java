package com.porar.ebooks.stou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.porar.ebooks.stou.R;

/**
 * Created by Porar on 10/1/2015.
 */
public class Fragment_Empty extends Fragment {
    public static Fragment newInstance() {
        Fragment_Empty fragment_empty = new Fragment_Empty();
        return fragment_empty;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty,container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.logout_success),Toast.LENGTH_LONG).show();
    }
}
