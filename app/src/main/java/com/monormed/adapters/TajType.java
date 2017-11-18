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
 * Created by nyulg on 2017. 11. 18..
 */

public class TajType extends RecyclerView.Adapter<TajType.ViewHolder> {
    View view;
    SharedPreferences pref;
    private ArrayList<com.monormed.datastreams.TajType> android;
    private com.monormed.fragments.TajType fragment;

    public TajType(ArrayList<com.monormed.datastreams.TajType> android, com.monormed.fragments.TajType fragment) {
        this.fragment=fragment;
        this.android = android;
    }

    @Override
    public TajType.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tajtype, viewGroup, false);
        return new TajType.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TajType.ViewHolder viewHolder, int i) {

        viewHolder.tv_taj_type.setText(android.get(i).getTaj_tipus());
        viewHolder.tv_taj_type_id.setText(android.get(i).getTaj_tipus_id());
        viewHolder.tv_taj_type_id.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_taj_type, tv_taj_type_id;
        public ViewHolder(View view) {
            super(view);

            tv_taj_type = (TextView) view.findViewById(R.id.tajtype_name);
            tv_taj_type_id = (TextView) view.findViewById(R.id.tajtype_id);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taj_type = tv_taj_type.getText().toString();
                    String taj_type_id = tv_taj_type_id.getText().toString();
                    fragment.setTajType(taj_type,taj_type_id);
                    fragment.getFragmentManager().popBackStackImmediate();

                }
            });
        }
    }

}