package com.monormed.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.BaseInputConnection;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.monormed.Interface;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;
import com.monormed.datastreams.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEvent extends Fragment {

    View addevent;
    Window window;
    ImageView btn_addevent_exit;
    EditText addevent_taj, addevent_szemelynev;
    boolean calledFromFragment;


    public AddEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addevent = inflater.inflate(R.layout.fragment_addevent, container, false);

        window = getActivity().getWindow();

        if(!calledFromFragment) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlue, getActivity().getTheme()));
            getActivity().findViewById(R.id.show_add).setVisibility(View.GONE);
        }

        addevent_szemelynev= (EditText) addevent.findViewById(R.id.addevent_szemelynev);
        addevent_taj = (EditText) addevent.findViewById(R.id.addevent_taj);
        btn_addevent_exit = (ImageView) addevent.findViewById(R.id.addevent_exit);

        btn_addevent_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseInputConnection mInputConnection = new BaseInputConnection(addevent, true);
                mInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });

        return addevent;
    }

    public void loadPatientData(String szemely_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        com.monormed.datastreams.PatientList patient = new com.monormed.datastreams.PatientList();
        patient.setSzemely_id(szemely_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.GET_PATIENT);
        request.setPatientList(patient);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                addevent_szemelynev.setText(resp.getPatient().getSzemely_nev().toString());
                if (resp.getPatient().getTaj()!= null) {
                    addevent_taj.setText(resp.getPatient().getTaj().toString());
                } else {
                    addevent_taj.setText("Felhasználóhoz nincs TAJ rendelve!");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), LENGTH_LONG).show();
            }
        });
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
                    if(!calledFromFragment) {
                        getActivity().getSupportFragmentManager().popBackStack();
                        if (getActivity().findViewById(R.id.add_container).getVisibility() != View.VISIBLE) {
                            getActivity().findViewById(R.id.show_add).setVisibility(View.VISIBLE);
                        }
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
                    } else {
                        getFragmentManager().popBackStack();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setCalledFromFragment(){
        calledFromFragment = true;
    }

}
