package com.porar.ebooks.stou.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.porar.ebooks.stou.AppMain;
import com.porar.ebooks.stou.R;
import com.porar.ebooks.stou.fragment.Fragment_AllEbooks;
import com.porar.ebooks.stou.fragment.Fragment_Choice_Mode;
import com.porar.ebooks.stou.fragment.Fragment_Contact;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Phone;
import com.porar.ebooks.stou.fragment.Fragment_Shelf_Tablet;

/**
 * Created by Porar on 9/25/2015.
 */
public class Activity_MainTab extends AppCompatActivity {

    Fragment_Shelf_Phone fragment_shelf_phone;
    Fragment_Shelf_Tablet fragment_shelf_tablet;

    public static ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.selector_book,
            R.drawable.selector_bookshelf,
            R.drawable.selector_contact
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_phone);
        if (linearLayout != null) {
            AppMain.isphone = true;
        } else {
            AppMain.isphone = false;
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), Activity_MainTab.this));
        viewPager.setOffscreenPageLimit(2);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int page = tab.getPosition() + 1;
                if (page == 1) {
                    viewPager.setCurrentItem(tab.getPosition());
                    Fragment_AllEbooks.clearItem();
                } else if (page == 2) {
                    viewPager.setCurrentItem(tab.getPosition());
                    if (AppMain.isphone) {
                        fragment_shelf_phone.loadItem();
                    } else {
                        fragment_shelf_tablet.loadItem();
                    }
                } else {
                    viewPager.setCurrentItem(tab.getPosition());
                    Fragment_AllEbooks.clearItem();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabIcons(tabLayout);
    }


    private void setupTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{getResources().getString(R.string.book_tab_title), getResources().getString(R.string.shelf_tab_title),
                getResources().getString(R.string.contact_tab_title)};

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            int currentPage = position + 1;
            if (currentPage == 1) {
                return Fragment_Choice_Mode.newInstance();
            } else if (currentPage == 2) {
                if (AppMain.isphone) {
                    fragment_shelf_phone = Fragment_Shelf_Phone.newInstance();
                    return fragment_shelf_phone;
                } else {
                    fragment_shelf_tablet = Fragment_Shelf_Tablet.newInstance();
                    return fragment_shelf_tablet;
                }
            } else if (currentPage == 3) {
                return Fragment_Contact.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
