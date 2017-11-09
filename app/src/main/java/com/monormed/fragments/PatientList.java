package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monormed.Constants;
import com.monormed.Interface;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientList extends Fragment {

    SharedPreferences pref;
    RecyclerView patientlist_rv;
    SwipeRefreshLayout patientlist_refresh;
    com.monormed.adapters.PatientList adapter_pl;


    public PatientList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View patientlist = inflater.inflate(R.layout.fragment_patientlist, container, false);
        pref=getActivity().getPreferences(0);

        patientlist_rv = (RecyclerView) patientlist.findViewById(R.id.patientlist_rv);
        patientlist_refresh = (SwipeRefreshLayout) patientlist.findViewById(R.id.patientlist_refresh);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        patientlist_rv.setLayoutManager(layoutManager);

        loadJSON(pref.getString(Constants.UNIQUE_ID,""));

        patientlist_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState ==RecyclerView.SCROLL_STATE_IDLE){
                    getActivity().findViewById(R.id.show_add).setVisibility(View.VISIBLE);
                } else {
                    getActivity().findViewById(R.id.show_add).setVisibility(View.GONE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        patientlist_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON(pref.getString(Constants.UNIQUE_ID,""));
            }
        });

        return patientlist;
    }

    private void loadJSON(String unique_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        com.monormed.datastreams.PatientList patient = new com.monormed.datastreams.PatientList();
        patient.setUnique_id(unique_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.PATIENT_LIST);
        request.setPatientList(patient);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                ArrayList<com.monormed.datastreams.PatientList> data = new ArrayList<>(Arrays.asList(resp.getPatientList()));
                adapter_pl=new com.monormed.adapters.PatientList(data);
                patientlist_rv.setAdapter(adapter_pl);
                if(patientlist_refresh.isRefreshing()) {
                    patientlist_refresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                if(patientlist_refresh.isRefreshing()) {
                    patientlist_refresh.setRefreshing(false);
                }
            }
        });
    }

}
