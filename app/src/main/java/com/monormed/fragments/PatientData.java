package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.monormed.Interface;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;
import com.monormed.datastreams.*;
import com.monormed.datastreams.PatientList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientData extends Fragment {

    SharedPreferences pref;
    EditText szemelynev, taj, nem, szulido, szulhely, szulnev, anyja_neve, orszag, irszam, varos, utca, telefon, email, megjegyzes;
    LinearLayout cim_orszag;
    FloatingActionButton patientdata_edit;
    ImageView patient_edit_close;

    public PatientData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View patientdata = inflater.inflate(R.layout.fragment_patientdata, container, false);

        pref = getActivity().getPreferences(0);

        patient_edit_close = (ImageView) getParentFragment().getView().findViewById(R.id.patient_close_edit);
        patientdata_edit = (FloatingActionButton) patientdata.findViewById(R.id.patientdata_edit);
        szemelynev = (EditText) patientdata.findViewById(R.id.patient_szemelynev);
        taj = (EditText) patientdata.findViewById(R.id.patient_taj);
        nem = (EditText) patientdata.findViewById(R.id.patient_nem);
        szulido = (EditText) patientdata.findViewById(R.id.patient_szulido);
        szulhely = (EditText) patientdata.findViewById(R.id.patient_szulhely);
        szulnev = (EditText) patientdata.findViewById(R.id.patient_szulnev);
        anyja_neve = (EditText) patientdata.findViewById(R.id.patient_anyja_neve);
        orszag = (EditText) patientdata.findViewById(R.id.patient_orszag);
        irszam = (EditText) patientdata.findViewById(R.id.patient_irszam);
        varos = (EditText) patientdata.findViewById(R.id.patient_varos);
        utca = (EditText) patientdata.findViewById(R.id.patient_utca);
        telefon = (EditText) patientdata.findViewById(R.id.patient_telefon);
        email = (EditText) patientdata.findViewById(R.id.patient_email);
        megjegyzes = (EditText) patientdata.findViewById(R.id.patient_megjegyzes);

        patientdata_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patient_edit_close.getVisibility()!=View.VISIBLE){
                    patient_edit_close.setVisibility(View.VISIBLE);
                    getParentFragment().getView().findViewById(R.id.patient_addtest).setVisibility(View.GONE);
                    getParentFragment().getView().findViewById(R.id.patient_addevent).setVisibility(View.GONE);
                    patientdata_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_save, getActivity().getTheme()));
                } else {
                    patient_edit_close.setVisibility(View.GONE);
                    getParentFragment().getView().findViewById(R.id.patient_addtest).setVisibility(View.VISIBLE);
                    getParentFragment().getView().findViewById(R.id.patient_addevent).setVisibility(View.VISIBLE);
                    patientdata_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit, getActivity().getTheme()));
                    Toast.makeText(getContext(), "Working on it...", Toast.LENGTH_LONG).show();
                }
            }
        });

        patient_edit_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient_edit_close.setVisibility(View.GONE);
                getParentFragment().getView().findViewById(R.id.patient_addtest).setVisibility(View.VISIBLE);
                getParentFragment().getView().findViewById(R.id.patient_addevent).setVisibility(View.VISIBLE);
                patientdata_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit, getActivity().getTheme()));
            }
        });

        //szemelynev.setTag(szemelynev.getKeyListener());
        szemelynev.setKeyListener(null);
        taj.setKeyListener(null);
        nem.setTag(nem.getKeyListener());
        nem.setKeyListener(null);
        szulido.setTag(szulido.getKeyListener());
        szulido.setKeyListener(null);
        szulhely.setTag(szulhely.getKeyListener());
        szulhely.setKeyListener(null);
        szulnev.setTag(szulnev.getKeyListener());
        szulnev.setKeyListener(null);
        anyja_neve.setTag(anyja_neve.getKeyListener());
        anyja_neve.setKeyListener(null);
        orszag.setTag(orszag.getKeyListener());
        orszag.setKeyListener(null);
        irszam.setTag(irszam.getKeyListener());
        irszam.setKeyListener(null);
        varos.setTag(varos.getKeyListener());
        varos.setKeyListener(null);
        utca.setTag(utca.getKeyListener());
        utca.setKeyListener(null);
        telefon.setTag(telefon.getKeyListener());
        telefon.setKeyListener(null);
        email.setTag(email.getKeyListener());
        email.setKeyListener(null);
        megjegyzes.setTag(megjegyzes.getKeyListener());
        megjegyzes.setKeyListener(null);

        cim_orszag = (LinearLayout) patientdata.findViewById(R.id.cim_orszag_layout);

        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                szemelynev.setKeyListener((KeyListener) szemelynev.getTag());
                nem.setKeyListener((KeyListener) nem.getTag());
                szulido.setKeyListener((KeyListener) szulido.getTag());
                szulhely.setKeyListener((KeyListener) szulhely.getTag());
                szulnev.setKeyListener((KeyListener) szulnev.getTag());
                anyja_neve.setKeyListener((KeyListener) anyja_neve.getTag());
                cim_orszag.setVisibility(View.VISIBLE);
                orszag.setKeyListener((KeyListener) orszag.getTag());
                irszam.setKeyListener((KeyListener) irszam.getTag());
                varos.setKeyListener((KeyListener) varos.getTag());
                utca.setKeyListener((KeyListener) utca.getTag());
                telefon.setKeyListener((KeyListener) telefon.getTag());
                email.setKeyListener((KeyListener) email.getTag());
                megjegyzes.setKeyListener((KeyListener) megjegyzes.getTag());
            }
        });*/

        return patientdata;
    }

    public void loadPatientData(String szemely_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        com.monormed.datastreams.PatientList patient = new PatientList();
        patient.setSzemely_id(szemely_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.GET_PATIENT);
        request.setPatientList(patient);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                szemelynev.setText(resp.getPatient().getSzemely_nev().toString());
                if (resp.getPatient().getTaj()!= null) {
                    taj.setText(resp.getPatient().getTaj().toString());
                }
                if (resp.getPatient().getNeme() != null) {
                    nem.setText(resp.getPatient().getNeme().toString());
                }
                if (resp.getPatient().getSzuletesi_ido()!= null) {
                    szulido.setText(resp.getPatient().getSzuletesi_ido().toString());
                }
                if (resp.getPatient().getSzuletesi_hely_orszag() !=null) {
                    szulhely.setText(resp.getPatient().getSzuletesi_hely_orszag().toString());
                }
                if (resp.getPatient().getSzuletesi_nev() !=null) {
                    szulnev.setText(resp.getPatient().getSzuletesi_nev().toString());
                }
                if (resp.getPatient().getAnyja_neve()!=null) {
                    anyja_neve.setText(resp.getPatient().getAnyja_neve().toString());
                }
                if (resp.getPatient().getIranyitoszam() !=null) {
                    irszam.setText(resp.getPatient().getIranyitoszam().toString());
                }
                if (resp.getPatient().getLakcim_orszag() !=null) {
                    orszag.setText(resp.getPatient().getLakcim_orszag().toString());
                } else {
                    cim_orszag.setVisibility(View.GONE);
                }
                if (resp.getPatient().getVaros_nev()!=null) {
                    varos.setText(resp.getPatient().getVaros_nev().toString());
                }
                if (resp.getPatient().getUtca_hazszam()!=null) {
                    utca.setText(resp.getPatient().getUtca_hazszam().toString());
                }
                if (resp.getPatient().getTelefon() !=null) {
                    telefon.setText(resp.getPatient().getTelefon().toString());
                }
                if (resp.getPatient().getEmail()!=null) {
                    email.setText(resp.getPatient().getEmail().toString());
                }
                if (resp.getPatient().getMegjegyzes()!=null) {
                    megjegyzes.setText(resp.getPatient().getMegjegyzes().toString());
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), LENGTH_LONG).show();
            }
        });
    }

}
