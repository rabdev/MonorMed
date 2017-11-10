package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.BaseInputConnection;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monormed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Patient extends Fragment {

    View patient;
    SharedPreferences pref;
    Window window;
    private ViewPager patientviewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    TextView tv_patient_name;
    String patientname;

    public Patient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        patient= inflater.inflate(R.layout.fragment_patient, container, false);

        pref = getActivity().getPreferences(0);
        getActivity().findViewById(R.id.show_add).setVisibility(View.GONE);
        window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorBlue, getActivity().getTheme()));

        patientviewPager = (ViewPager) patient.findViewById(R.id.patient_container);
        tabLayout = (TabLayout) patient.findViewById(R.id.patient_tab);
        tv_patient_name = (TextView) patient.findViewById(R.id.tv_patient_name);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        patientviewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(patientviewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tv_patient_name.setText(patientname);

        return patient;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Settings settings = new Settings();
                    return settings;
                case 1:
                    Settings settings1 = new Settings();
                    return settings1;
                case 2:
                    Settings settings2 = new Settings();
                    return settings2;
                case 3:
                    Settings settings3 = new Settings();
                    return settings3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Diagnózisok";
                case 1:
                    return "Anamnézisek";
                case 2:
                    return "Leletek";
                case 3:
                    return "Személyes adatok";
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
                    getActivity().findViewById(R.id.add_container).setVisibility(View.GONE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
                    return true;
                }
                return false;
            }
        });
    }

    public String setPatientData(String patient_name) {
        patientname=patient_name;
        return patientname;
    }

}
