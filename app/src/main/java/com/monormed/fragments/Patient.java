package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monormed.Operations;
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
    String patientname, szemely_id;
    PatientLog patientLog;
    Bundle data;
    int pos;

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

        tabLayout.clearOnTabSelectedListeners();

        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        patientviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pos =position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    patientLog.hideRVPL();
                    patientLog.showProgressBarPL();
                    //patientLog.loadPatientLog();
                } else if (state == ViewPager.SCROLL_STATE_IDLE){
                    if (pos!=3){
                        patientLog.loadPatientLog(szemely_id, pos);
                    }
                }
            }
        });

        tv_patient_name.setText(patientname);

        return patient;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    data=new Bundle();
                    patientLog = new PatientLog();
                    data.putInt("page",position);
                    data.putString("id",szemely_id);
                    patientLog.setArguments(data);
                    return patientLog;
                case 1:
                    data=new Bundle();
                    patientLog = new PatientLog();
                    data.putInt("page",position);
                    data.putString("id",szemely_id);
                    patientLog.setArguments(data);
                    return patientLog;
                case 2:
                    data=new Bundle();
                    patientLog = new PatientLog();
                    data.putInt("page",position);
                    data.putString("id",szemely_id);
                    patientLog.setArguments(data);
                    return patientLog;
                case 3:
                    PatientData patientData = new PatientData();
                    patientData.loadPatientData(szemely_id);
                    return patientData;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
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

    public String setPatientName(String patient_name) {
        patientname=patient_name;
        return patientname;
    }

    public String setPatientID(String patient_id) {
        szemely_id = patient_id;
        return szemely_id;
    }
}
