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
import android.view.inputmethod.BaseInputConnection;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    Bundle data;
    ProgressBar pl_progressbar;
    ImageView patient_back, patient_addevent, patient_addtest, patient_close_edit, patient_save_edit;
    PatientLogDiag patientLogDiag;
    PatientLogLelet patientLogLelet;
    PatientLogAnam patientLogAnam;
    int pos;

    public Patient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        patient = inflater.inflate(R.layout.fragment_patient, container, false);

        pref = getActivity().getPreferences(0);
        getActivity().findViewById(R.id.show_add).setVisibility(View.GONE);
        window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorBlue, getActivity().getTheme()));

        patientviewPager = (ViewPager) patient.findViewById(R.id.patient_container);
        tabLayout = (TabLayout) patient.findViewById(R.id.patient_tab);
        tv_patient_name = (TextView) patient.findViewById(R.id.tv_patient_name);
        patient_back = (ImageView) patient.findViewById(R.id.patient_back);
        patient_addevent = (ImageView) patient.findViewById(R.id.patient_addevent);
        patient_addtest = (ImageView) patient.findViewById(R.id.patient_addtest);
        patient_close_edit = (ImageView) patient.findViewById(R.id.patient_close_edit);
        patient_save_edit = (ImageView) patient.findViewById(R.id.patient_save_edit);
        pl_progressbar = (ProgressBar) patient.findViewById(R.id.pl_progressbar);

        patient_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseInputConnection mInputConnection = new BaseInputConnection(patient, true);
                mInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        patientviewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(patientviewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.clearOnTabSelectedListeners();

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
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
                pos = position;
                patient_addevent.setVisibility(View.VISIBLE);
                patient_addtest.setVisibility(View.VISIBLE);
                patient_back.setVisibility(View.VISIBLE);
                patient_close_edit.setVisibility(View.GONE);
                patient_save_edit.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    showProgressBarPL();
                    if (patientLogAnam != null) {
                        patientLogAnam.hideRVPL();
                    }
                    if (patientLogDiag != null) {
                        patientLogDiag.hideRVPL();
                    }
                    if (patientLogLelet != null) {
                        patientLogLelet.hideRVPL();
                    }

                    //patientLog.loadPatientLog();
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (pos == 0) {
                        patientLogDiag.loadDIAG(szemely_id);
                    } else if (pos == 1) {
                        patientLogAnam.loadANAM(szemely_id);
                    } else if (pos == 2) {
                        patientLogLelet.loadLELET(szemely_id);
                    } else if (pos == 3) {
                        hideProgressBarPL();
                    }
                }
            }
        });

        patient_addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEvent addEvent = new AddEvent();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patient_frame, addEvent, addEvent.getTag())
                        .addToBackStack(null)
                        .commit();
                addEvent.setCalledFromFragment();
                addEvent.loadPatientData(szemely_id);
            }
        });

        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    patientLogDiag.hideRVPL();
                    patientLogDiag.loadDIAG(szemely_id);
                } else if (tab.getPosition()==1) {
                    patientLogAnam.hideRVPL();
                    patientLogAnam.loadANAM(szemely_id);
                } else if (tab.getPosition()==2){
                    patientLogLelet.hideRVPL();
                    patientLogLelet.loadLELET(szemely_id);
                } else if (tab.getPosition()==3){
                    hideProgressBarPL();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                showProgressBarPL();
                if (patientLogAnam!=null){
                    patientLogAnam.hideRVPL();
                }
                if (patientLogDiag!=null){
                    patientLogDiag.hideRVPL();
                }
                if (patientLogLelet!=null){
                    patientLogLelet.hideRVPL();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    patientLogDiag.hideRVPL();
                    patientLogDiag.loadDIAG(szemely_id);
                } else if (tab.getPosition()==1) {
                    patientLogAnam.hideRVPL();
                    patientLogAnam.loadANAM(szemely_id);
                } else if (tab.getPosition()==2){
                    patientLogLelet.hideRVPL();
                    patientLogLelet.loadLELET(szemely_id);
                } else if (tab.getPosition()==3){
                    hideProgressBarPL();
                }
            }
        });*/

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
                    data = new Bundle();
                    patientLogDiag = new PatientLogDiag();
                    data.putString("id", szemely_id);
                    patientLogDiag.setArguments(data);
                    return patientLogDiag;
                case 1:
                    data = new Bundle();
                    patientLogAnam = new PatientLogAnam();
                    data.putString("id", szemely_id);
                    patientLogAnam.setArguments(data);
                    return patientLogAnam;
                case 2:
                    data = new Bundle();
                    patientLogLelet = new PatientLogLelet();
                    data.putString("id", szemely_id);
                    patientLogLelet.setArguments(data);
                    return patientLogLelet;
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
                    window = getActivity().getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        getActivity().findViewById(R.id.show_add).setVisibility(View.VISIBLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
        super.onPause();
    }

    public String setPatientName(String patient_name) {
        patientname = patient_name;
        return patientname;
    }

    public String setPatientID(String patient_id) {
        szemely_id = patient_id;
        return szemely_id;
    }

    public void showProgressBarPL() {
        pl_progressbar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBarPL() {
        pl_progressbar.setVisibility(View.GONE);
    }

}
