package com.monormed.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monormed.Constants;
import com.monormed.MainActivity;
import com.monormed.R;
import com.monormed.fragments.AddEvent;
import com.monormed.fragments.Patient;

import java.util.ArrayList;

/**
 * Created by NYG on 2017. 11. 09..
 */

public class PatientList extends RecyclerView.Adapter<PatientList.ViewHolder> {
    View view;
    SharedPreferences pref;
    private ArrayList<com.monormed.datastreams.PatientList> android;


    public PatientList(ArrayList<com.monormed.datastreams.PatientList> android) {
        this.android = android;
    }

    @Override
    public PatientList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_patientlist, viewGroup, false);
        return new PatientList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientList.ViewHolder viewHolder, int i) {

        viewHolder.tv_my_patient_name.setText(android.get(i).getSzemely_nev());
        viewHolder.tv_my_patient_id.setText(android.get(i).getSzemely_id());
        viewHolder.tv_my_patient_id.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_my_patient_name, tv_my_patient_id;
        private LinearLayout mypatient;
        ImageView pl_addevent, pl_addtest;

        public ViewHolder(View view) {
            super(view);

            tv_my_patient_name = (TextView) view.findViewById(R.id.my_patient_name);
            tv_my_patient_id = (TextView) view.findViewById(R.id.my_patient_id);
            pl_addevent = (ImageView) view.findViewById(R.id.pl_addevent);
            mypatient = (LinearLayout) view.findViewById(R.id.my_patient);
            mypatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mypatientid = tv_my_patient_id.getText().toString();
                    String mypatientname = tv_my_patient_name.getText().toString();
                    /*pref = ((MainActivity) v.getContext()).getPreferences(0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.SZEMELY_ID, mypatientid);
                    editor.putString(Constants.SZEMELY_NEV, mypatientname);
                    editor.apply();*/
                        Patient patient = new Patient();
                        FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, patient, patient.getTag())
                                .addToBackStack("1")
                                .commit();
                        patient.setPatientName(mypatientname);
                        patient.setPatientID(mypatientid);
                }
            });
            pl_addevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mypatientid = tv_my_patient_id.getText().toString();
                    AddEvent addEvent = new AddEvent();
                    FragmentManager fragmentManager = ((FragmentActivity)view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, addEvent, addEvent.getTag())
                            .addToBackStack("1")
                            .commit();
                    addEvent.loadPatientData(mypatientid);
                }
            });

        }
    }


}

