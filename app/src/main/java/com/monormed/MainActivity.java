package com.monormed;

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

public class MainActivity extends AppCompatActivity {

    private ViewPager homeviewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    FloatingActionButton show_add;
    LinearLayout add_container;
    ImageView hide_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeviewPager = (ViewPager) findViewById(R.id.home_container);
        tabLayout = (TabLayout) findViewById(R.id.home_tab);
        show_add = (FloatingActionButton) findViewById(R.id.show_add);
        add_container = (LinearLayout) findViewById(R.id.add_container);
        hide_add = (ImageView) findViewById(R.id.hide_add);

        add_container.setVisibility(View.GONE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        homeviewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(homeviewPager);

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
                    Calendar calendar1 = new Calendar();
                    return calendar1;
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
