package com.monormed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.monormed.fragments.Calendar;
import com.monormed.fragments.Login;
import com.monormed.fragments.PatientList;
import com.monormed.fragments.UserMenu;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    FragmentManager fragmentManager;
    private ViewPager homeviewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    FloatingActionButton show_add;
    LinearLayout add_container;
    ImageView hide_add, btn_exit, btn_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(0);
        fragmentManager = getSupportFragmentManager();

        if (preferences.getBoolean(Constants.IS_LOGGED_IN,true)){
            setContentView(R.layout.activity_main);
        } else {
            Login login = new Login();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, login, login.getTag())
                    .commit();
        }

        homeviewPager = (ViewPager) findViewById(R.id.home_container);
        tabLayout = (TabLayout) findViewById(R.id.home_tab);
        show_add = (FloatingActionButton) findViewById(R.id.show_add);
        add_container = (LinearLayout) findViewById(R.id.add_container);
        hide_add = (ImageView) findViewById(R.id.hide_add);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_profile = (ImageView) findViewById(R.id.btn_profile);

        add_container.setVisibility(View.GONE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        homeviewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(homeviewPager);
        //setupTabIcons();

        show_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_container.setVisibility(View.VISIBLE);
                show_add.setVisibility(View.GONE);
            }
        });

        hide_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_container.setVisibility(View.GONE);
                show_add.setVisibility(View.VISIBLE);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMenu userMenu = new UserMenu();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, userMenu, userMenu.getTag())
                        .addToBackStack(null)
                        .commit();
                show_add.setVisibility(View.GONE);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN,false);
                editor.putString(Constants.USERNAME,"");
                editor.putString(Constants.NAME,"");
                editor.putString(Constants.UNIQUE_ID,"");
                editor.putString(Constants.OSZTALY,"");
                editor.apply();
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });


    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Calendar calendar = new Calendar();
                    return calendar;
                case 1:
                    PatientList patientList = new PatientList();
                    return patientList;
                case 2:
                    Calendar calendar2 = new Calendar();
                    return calendar2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Kezdőlap";
                case 1:
                    return "Betegeim";
                case 2:
                    return "Vizsgálatok";
            }
            return null;
        }
    }
}