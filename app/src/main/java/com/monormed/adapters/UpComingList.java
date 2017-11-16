package com.monormed.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monormed.R;
import com.monormed.fragments.Patient;

import java.util.ArrayList;

/**
 * Created by NYG on 2017. 11. 09..
 */

public class UpComingList extends RecyclerView.Adapter<UpComingList.ViewHolder> {
    View view;
    SharedPreferences pref;
    private ArrayList<com.monormed.datastreams.UpComingList> android;


    public UpComingList(ArrayList<com.monormed.datastreams.UpComingList> android) {
        this.android = android;
    }

    @Override
    public UpComingList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_upcoming, viewGroup, false);
        return new UpComingList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UpComingList.ViewHolder viewHolder, int i) {

        viewHolder.tv_upcoming_name.setText(android.get(i).getSzemely_nev());
        viewHolder.tv_upcoming_date.setText(android.get(i).getElojegyzes_ido());
        viewHolder.tv_upcoming_id.setText(android.get(i).getSzemely_id());
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_upcoming_name, tv_upcoming_date, tv_upcoming_id;
        private LinearLayout upcoming;
        public ViewHolder(View view) {
            super(view);

            tv_upcoming_name = (TextView)view.findViewById(R.id.upcoming_name);
            tv_upcoming_date = (TextView) view.findViewById(R.id.upcoming_time);
            tv_upcoming_id = (TextView) view.findViewById(R.id.upcoming_id);
            upcoming = (LinearLayout) view.findViewById(R.id.upcoming);
            upcoming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mypatientid = tv_upcoming_id.getText().toString();
                    String mypatientname = tv_upcoming_name.getText().toString();
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
        }
    }
}
