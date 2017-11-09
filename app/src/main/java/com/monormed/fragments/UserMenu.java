package com.monormed.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monormed.Constants;
import com.monormed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMenu extends Fragment {

    SharedPreferences pref;
    private ViewPager usermenuviewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    FloatingActionButton fab_logout;

    public UserMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View usermenu = inflater.inflate(R.layout.fragment_usermenu, container, false);
        pref = getActivity().getPreferences(0);

        usermenuviewPager = (ViewPager) usermenu.findViewById(R.id.usermenu_container);
        tabLayout = (TabLayout) usermenu.findViewById(R.id.usermenu_tab);
        fab_logout = (FloatingActionButton) usermenu.findViewById(R.id.fab_logout);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        usermenuviewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(usermenuviewPager);

        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN,false);
                editor.putString(Constants.USERNAME,"");
                editor.putString(Constants.NAME,"");
                editor.putString(Constants.UNIQUE_ID,"");
                editor.putString(Constants.OSZTALY,"");
                editor.apply();
                Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return usermenu;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Settings settings = new Settings();
                    return settings;
                case 1:
                    Profile profile = new Profile();
                    return profile;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Beállítások";
                case 1:
                    return "Profil";
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().findViewById(R.id.show_add).setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

}
