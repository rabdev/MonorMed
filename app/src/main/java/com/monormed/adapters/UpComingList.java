package com.monormed.adapters;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monormed.R;

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
        viewHolder.tv_upcoming_date.setText(android.get(i).getElojegyzes_nap()+" "+android.get(i).getElojegyzes_ido());
        /*viewHolder.tv_my_patient_id.setText(android.get(i).getSzemely_id());
        viewHolder.tv_my_patient_id.setVisibility(View.GONE);*/
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_upcoming_name, tv_upcoming_date;
        //private LinearLayout mypatient;
        public ViewHolder(View view) {
            super(view);

            tv_upcoming_name = (TextView)view.findViewById(R.id.upcoming_name);
            tv_upcoming_date = (TextView) view.findViewById(R.id.upcoming_time);
            /*mypatient = (LinearLayout) view.findViewById(R.id.my_patient);
            mypatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mypatientid = tv_my_patient_id.getText().toString();
                    String mypatientname= tv_my_patient_name.getText().toString();
                    pref = ((MainActivity)v.getContext()).getPreferences(0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.SZEMELY_ID, mypatientid);
                    editor.putString(Constants.SZEMELY_NEV, mypatientname);
                    editor.apply();
                    Patient patient = new Patient();
                    FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, patient, patient.getTag())
                            .addToBackStack(null)
                            .commit();
                }
            });*/
        }
    }
}
