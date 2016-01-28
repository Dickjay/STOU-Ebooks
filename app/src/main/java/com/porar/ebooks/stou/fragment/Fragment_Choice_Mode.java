package com.porar.ebooks.stou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;

/**
 * Created by Porar on 9/25/2015.
 */
public class Fragment_Choice_Mode extends Fragment {
    public static Fragment_Choice_Mode newInstance() {
        Fragment_Choice_Mode fragment = new Fragment_Choice_Mode();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_choice_mode, container, false);
        addFragment();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void addFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, Fragment_MainLogin.newInstance());
        fragmentTransaction.commit();
    }


    public static void addFragment(FragmentTransaction ft, Fragment fragment,
                                   boolean addToBackStack, final int transition) {
        AppMain.isShelf = false;
        try {
            if (!fragment.isAdded()) {
                ft.replace(R.id.frame_container, fragment);
                ft.setTransition(transition);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
