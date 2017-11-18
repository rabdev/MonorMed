package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.monormed.Interface;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;
import com.monormed.datastreams.*;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientLogDiag extends Fragment {

    View patientlogdiag;
    SharedPreferences pref;
    RecyclerView patientlog_rv;
    String szemely_id;
    com.monormed.adapters.PatientLog adapter_plog;
    ProgressBar pl_progressbar;
    FloatingActionButton patientlog_add;

    public PatientLogDiag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        patientlogdiag = inflater.inflate(R.layout.fragment_patientlog, container, false);
        pref = getActivity().getPreferences(0);

        szemely_id = getArguments().getString("id");


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        pl_progressbar = (ProgressBar) getParentFragment().getView().findViewById(R.id.pl_progressbar);
        patientlog_rv= (RecyclerView) patientlogdiag.findViewById(R.id.patientlog_recyclerview);
        patientlog_add = (FloatingActionButton) patientlogdiag.findViewById(R.id.patientlog_add);

        patientlog_rv.setLayoutManager(layoutManager);

        loadDIAG(szemely_id);


        return patientlogdiag;
    }

    public void loadDIAG(String szemely_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        com.monormed.datastreams.PatientLog patientLog = new com.monormed.datastreams.PatientLog();
        patientLog.setSzemely_id(szemely_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.PATIENT_LOG_DIAG);
        request.setPatientLog(patientLog);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                ArrayList<com.monormed.datastreams.PatientLog> data = new ArrayList<>(Arrays.asList(resp.getPatientLog()));
                adapter_plog=new com.monormed.adapters.PatientLog(data, PatientLogDiag.this);
                patientlog_rv.setAdapter(adapter_plog);
                adapter_plog.notifyDataSetChanged();
                hideProgressBarPL();
                showRVPL();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), LENGTH_LONG).show();
            }
        });
    }

    public void showProgressBarPL (){
        pl_progressbar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBarPL (){
        pl_progressbar.setVisibility(View.GONE);
    }

    public void hideRVPL(){patientlog_rv.setVisibility(View.GONE);}

    public void showRVPL(){patientlog_rv.setVisibility(View.VISIBLE);
    }

}
