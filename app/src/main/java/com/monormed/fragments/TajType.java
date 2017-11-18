package com.monormed.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

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
public class TajType extends Fragment {

    RecyclerView tajtype_rv;
    com.monormed.adapters.TajType adapter_tt;
    TextView tajtype_tv, tajtypeid_tv;

    public TajType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tajtype = inflater.inflate(R.layout.fragment_tajtype, container, false);

        tajtype_tv = (TextView) getParentFragment().getView().findViewById(R.id.tajtype_tv);
        tajtypeid_tv = (TextView) getParentFragment().getView().findViewById(R.id.tajtypeid_tv);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        tajtype_rv= (RecyclerView) tajtype.findViewById(R.id.tajtype_recyclerview);
        tajtype_rv.setLayoutManager(layoutManager);

        loadJSON();

        return tajtype;
    }

    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.TAJ_TYPE);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                ArrayList<com.monormed.datastreams.TajType> data = new ArrayList<>(Arrays.asList(resp.getTajType()));
                adapter_tt=new com.monormed.adapters.TajType(data, TajType.this);
                tajtype_rv.setAdapter(adapter_tt);


                //Snackbar.make(getView(), data, LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
    public void setTajType (String tajType, String tajtypeid){
        tajtype_tv.setText(tajType);
        tajtypeid_tv.setText(tajtypeid);
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
                    getFragmentManager().popBackStackImmediate();
                    return true;
                }
                return false;
            }
        });
    }
}
