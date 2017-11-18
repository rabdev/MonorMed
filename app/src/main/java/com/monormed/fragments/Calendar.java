package com.monormed.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.ImageView;
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

    int year, month, day, day1, day2;
    SharedPreferences pref;
    RecyclerView calendar_rv;
    String datepick, unique_id;
    java.util.Calendar cal;
    com.monormed.adapters.UpComingList adapter_ul;
    SwipeRefreshLayout calendar_refresh;
    TextView selected_date, selected_date1, selected_date2;
    View dialogdp;
    DatePicker datePicker;
    AlertDialog dialog;
    ImageView btn_datepick;


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
        selected_date1 = (TextView) calendar.findViewById(R.id.selected_date1);
        selected_date2 = (TextView) calendar.findViewById(R.id.selected_date2);
        btn_datepick = (ImageView) calendar.findViewById(R.id.btn_datepick);

        cal = java.util.Calendar.getInstance();
        year = cal.get(java.util.Calendar.YEAR);
        month = cal.get(java.util.Calendar.MONTH)+1;
        day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        day1 = cal.get(java.util.Calendar.DAY_OF_MONTH)+1;
        day2 = cal.get(java.util.Calendar.DAY_OF_MONTH)+2;
        datepick = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        selected_date.setText(String.valueOf(datepick));
        selected_date.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getActivity().getTheme()));

        selected_date1.setText(String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day1));
        selected_date2.setText(String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day2));

        selected_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSON(unique_id, selected_date.getText().toString());
                datepick = selected_date.getText().toString();
                selected_date1.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date2.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getActivity().getTheme()));
            }
        });

        selected_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSON(unique_id, selected_date1.getText().toString());
                datepick = selected_date1.getText().toString();
                selected_date.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date2.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date1.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getActivity().getTheme()));
            }
        });

        selected_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSON(unique_id, selected_date2.getText().toString());
                datepick = selected_date2.getText().toString();
                selected_date.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date1.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date2.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getActivity().getTheme()));
            }
        });

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext());

        calendar_rv.setLayoutManager(layoutManager1);
        loadJSON(unique_id,datepick);

        btn_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


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

    private void showDatePicker () {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater.from(getContext()));
        dialogdp = inflater.inflate(R.layout.dialog_datepicker, null);
        datePicker = (DatePicker) dialogdp.findViewById(R.id.datepicker);
        datePicker.setFirstDayOfWeek(java.util.Calendar.MONDAY);

        builder.setView(dialogdp);

        builder.setPositiveButton("Mentés", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected_date2.setText(datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth());
                datepick = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                selected_date.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date1.setTextColor(getResources().getColor(R.color.colorGrey,getActivity().getTheme()));
                selected_date2.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getActivity().getTheme()));
                loadJSON(unique_id,datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth() );

            }
        });
        builder.setNegativeButton("Bezárás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();

    }

}
