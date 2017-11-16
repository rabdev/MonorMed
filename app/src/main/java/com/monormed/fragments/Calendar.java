package com.monormed.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monormed.Constants;
import com.monormed.Interface;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;
import com.monormed.datastreams.UpComingList;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
public class Calendar extends Fragment {

    int year, month, day;
    SharedPreferences pref;
    RecyclerView calendar_rv;
    String datepick, unique_id;
    java.util.Calendar cal;
    com.monormed.adapters.UpComingList adapter_ul;
    SwipeRefreshLayout calendar_refresh;
    TextView selected_date;


    public Calendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View calendar = inflater.inflate(R.layout.fragment_calendar, container, false);
        pref=getActivity().getPreferences(0);
        unique_id = pref.getString(Constants.UNIQUE_ID,"");

        calendar_rv = (RecyclerView) calendar.findViewById(R.id.calendar_rv);
        calendar_refresh = (SwipeRefreshLayout) calendar.findViewById(R.id.calendar_refresh);
        selected_date = (TextView) calendar.findViewById(R.id.selected_date);

        cal = java.util.Calendar.getInstance();
        year = cal.get(java.util.Calendar.YEAR);
        month = cal.get(java.util.Calendar.MONTH)+1;
        day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        datepick = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        selected_date.setText(String.valueOf(datepick));

        Toast.makeText(getContext(), String.valueOf(datepick),Toast.LENGTH_LONG).show();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext());

        calendar_rv.setLayoutManager(layoutManager1);
        loadJSON(unique_id,datepick);


        calendar_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState ==RecyclerView.SCROLL_STATE_IDLE){
                    getActivity().findViewById(R.id.show_add).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.add_container).setVisibility(View.GONE);
                } else {
                    getActivity().findViewById(R.id.show_add).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.add_container).setVisibility(View.GONE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        calendar_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON(unique_id,datepick);
            }
        });

        return calendar;
    }

    private void loadJSON(String unique_id, String date){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);

        UpComingList upComingList = new UpComingList();
        upComingList.setSzemely_id(unique_id);
        upComingList.setDate(date);
        ServerRequest request = new ServerRequest();
        request.setOperation(Operations.CALENDAR_LIST);
        request.setUpComingList(upComingList);
        Call<ServerResponse> response = loginInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                ArrayList<UpComingList> upcl = new ArrayList<>(Arrays.asList(resp.getUpComingList()));
                adapter_ul=new com.monormed.adapters.UpComingList(upcl);
                calendar_rv.setAdapter(adapter_ul);

                if(calendar_refresh.isRefreshing()) {
                    calendar_refresh.setRefreshing(false);
                }

                //Snackbar.make(getView(), data, LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(getView(), t.getLocalizedMessage(), LENGTH_LONG).show();

                if(calendar_refresh.isRefreshing()) {
                    calendar_refresh.setRefreshing(false);
                }
            }
        });
    }
}
