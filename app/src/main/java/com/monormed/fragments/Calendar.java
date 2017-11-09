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

    MaterialCalendarView calendarView;
    int year, month, day, x;
    SharedPreferences pref;
    RecyclerView calendar_rv;
    TextView month_view, week_view;
    String datepick, unique_id;
    java.util.Calendar cal;
    LinearLayout calendarheader;
    com.monormed.adapters.UpComingList adapter_ul;
    SwipeRefreshLayout calendar_refresh;


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
        x=0;

        calendarheader = (LinearLayout) calendar.findViewById(R.id.calendar_header);
        month_view = (TextView) calendar.findViewById(R.id.month_view);
        week_view= (TextView) calendar.findViewById(R.id.week_view);
        calendarView = (MaterialCalendarView) calendar.findViewById(R.id.calendarview);
        calendar_rv = (RecyclerView) calendar.findViewById(R.id.calendar_rv);
        calendar_refresh = (SwipeRefreshLayout) calendar.findViewById(R.id.calendar_refresh);

        cal = java.util.Calendar.getInstance();
        calendarView.setDateSelected(cal, true);

        year = calendarView.getSelectedDate().getYear();
        month = calendarView.getSelectedDate().getMonth()+1;
        day = calendarView.getSelectedDate().getDay();
        datepick = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        calendarheader.setVisibility(View.GONE);

        week_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));
        month_view.setTextColor(getResources().getColor(R.color.colorGrey, getActivity().getTheme()));
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

        week_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarheader.setVisibility(View.GONE);
                month_view.setTextColor(getResources().getColor(R.color.colorGrey, getActivity().getTheme()));
                week_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));
                calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                x=1;
            }
        });

        month_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarheader.setVisibility(View.VISIBLE);
                month_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));
                week_view.setTextColor(getResources().getColor(R.color.colorGrey, getActivity().getTheme()));
                calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                x=0;
            }
        });

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext());

        calendar_rv.setLayoutManager(layoutManager1);
        loadJSON(unique_id,datepick);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                year = calendarView.getSelectedDate().getYear();
                month = calendarView.getSelectedDate().getMonth()+1;
                day = calendarView.getSelectedDate().getDay();
                datepick = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                loadJSON(unique_id,datepick);
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
                    if(x==0) {
                        x = 1;
                        calendarheader.setVisibility(View.GONE);
                        month_view.setTextColor(getResources().getColor(R.color.colorGrey, getActivity().getTheme()));
                        week_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));
                        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                    }
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
