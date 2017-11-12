package com.monormed.adapters;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monormed.Interface;
import com.monormed.MainActivity;
import com.monormed.Operations;
import com.monormed.R;
import com.monormed.ServerRequest;
import com.monormed.ServerResponse;
import com.monormed.datastreams.Anamnezis;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * Created by nyulg on 2017. 11. 10..
 */

public class PatientLog extends RecyclerView.Adapter<PatientLog.ViewHolder> {
    View view;
    int x;
    private ArrayList<com.monormed.datastreams.PatientLog> android;
    private com.monormed.fragments.PatientLog fragment;


    public PatientLog(ArrayList<com.monormed.datastreams.PatientLog> android, com.monormed.fragments.PatientLog fragment) {
        this.fragment = fragment;
        this.android = android;
    }

    @Override
    public PatientLog.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_patientlog, viewGroup, false);
        return new PatientLog.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientLog.ViewHolder viewHolder, int i) {

        if (android.get(i).getAnamnezisID()!=null) {
            x=1;
            viewHolder.tv_id.setText(android.get(i).getAnamnezisID());
            viewHolder.tv_id.setVisibility(View.GONE);
            viewHolder.patientlog_dropdown_close.setVisibility(View.GONE);
            viewHolder.tv_diagnozis.setText("Anamnézis került felvételre");
            if (android.get(i).getEllatasVege()!=null) {
                viewHolder.tv_date.setText(android.get(i).getEllatasVege());
            } else {
                viewHolder.tv_date.setText("2017-01-01");
            }
            loadJSON(android.get(i).getAnamnezisID(),viewHolder);
        } else if (android.get(i).getDiagnozisok_id()!=null){
            x=0;
            viewHolder.tv_id.setText(android.get(i).getKOD10());
            viewHolder.tv_diagnozis.setText(android.get(i).getNEV());
            if (android.get(i).getEllatasVege()!=null) {
                viewHolder.tv_date.setText(android.get(i).getEllatasVege());
            } else {
                viewHolder.tv_date.setText("2017-01-01");
            }
            viewHolder.patientlog_webview.setVisibility(View.GONE);
            viewHolder.pl_container.setVisibility(View.GONE);
            viewHolder.patientlog_dropdown_close.setVisibility(View.GONE);
        } else if (android.get(i).getLeletID()!=null){
            x=2;
            viewHolder.tv_id.setText(android.get(i).getLeletID());
            viewHolder.tv_id.setVisibility(View.GONE);
            viewHolder.patientlog_dropdown_close.setVisibility(View.GONE);
            viewHolder.tv_diagnozis.setText("Lelet került hozzáadásra");
            if (android.get(i).getEllatasVege()!=null) {
                viewHolder.tv_date.setText(android.get(i).getEllatasVege());
            } else {
                viewHolder.tv_date.setText("2017-01-01");
            }
            loadJSON(android.get(i).getLeletID(),viewHolder);
        } else {
            viewHolder.tv_diagnozis.setText("Hellobello");
        }
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_diagnozis, tv_id, tv_date, patientlog_tv;
        private WebView patientlog_webview;
        ImageView patientlog_dropdown, patientlog_dropdown_close;
        LinearLayout pl_container;
        public ViewHolder(View view) {
            super(view);

            tv_diagnozis = (TextView)view.findViewById(R.id.diagnozis);
            tv_id = (TextView) view.findViewById(R.id.id);
            tv_date = (TextView) view.findViewById(R.id.date);
            pl_container = (LinearLayout) view.findViewById(R.id.pl_container);
            patientlog_webview = (WebView) view.findViewById(R.id.patientlog_webview);
            patientlog_webview.setBackgroundColor(Color.TRANSPARENT);
            patientlog_tv = (TextView) view.findViewById(R.id.patientlog_tv);
            patientlog_dropdown = (ImageView) view.findViewById(R.id.patientlog_dropdown);
            patientlog_dropdown_close = (ImageView) view.findViewById(R.id.patientlog_dropdown_close);
            patientlog_dropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientlog_webview.getVisibility()!=View.VISIBLE){
                        patientlog_webview.setVisibility(View.VISIBLE);
                        patientlog_tv.setVisibility(View.INVISIBLE);
                        patientlog_dropdown_close.setVisibility(View.VISIBLE);
                        patientlog_dropdown.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_close, view.getContext().getTheme()));
                    } else {
                        patientlog_webview.setVisibility(View.GONE);
                        patientlog_tv.setVisibility(View.VISIBLE);
                        patientlog_dropdown_close.setVisibility(View.GONE);
                        patientlog_dropdown.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_dropdown, view.getContext().getTheme()));
                    }

                }
            });
            patientlog_dropdown_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    patientlog_webview.setVisibility(View.GONE);
                    patientlog_tv.setVisibility(View.VISIBLE);
                    patientlog_dropdown_close.setVisibility(View.GONE);
                    patientlog_dropdown.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_dropdown, view.getContext().getTheme()));
                }
            });
        }
    }

    private void loadJSON(String patientlogid, final PatientLog.ViewHolder viewHolder){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interface loginInterface = retrofit.create(Interface.class);
        ServerRequest request1 = new ServerRequest();
        Anamnezis anamnezis = new Anamnezis();

        if (x==1){
            anamnezis.setAnamnezisID(patientlogid);
            request1.setOperation(Operations.ANAMNEZIS);
        } else if (x==2) {
            anamnezis.setLeletID(patientlogid);
            request1.setOperation(Operations.LELET);
        }

        request1.setAnamnezis(anamnezis);
        Call<ServerResponse> response = loginInterface.operation(request1);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if (resp.getAnamnezis().getAnamnezisSzoveg() != null){
                    viewHolder.patientlog_webview.loadDataWithBaseURL(null, resp.getAnamnezis().getAnamnezisSzoveg().toString(), "text/html", "utf-8", null);
                    viewHolder.patientlog_webview.setVisibility(View.GONE);
                    viewHolder.patientlog_tv.setText(Html.fromHtml(resp.getAnamnezis().getAnamnezisSzoveg().toString(), Html.FROM_HTML_MODE_COMPACT));
                } else if (resp.getAnamnezis().getLeletSzoveg()!=null){
                    viewHolder.patientlog_webview.loadDataWithBaseURL(null, resp.getAnamnezis().getLeletSzoveg().toString(), "text/html", "utf-8", null);
                    viewHolder.patientlog_webview.setVisibility(View.GONE);
                    viewHolder.patientlog_tv.setText(Html.fromHtml(resp.getAnamnezis().getLeletSzoveg().toString(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Snackbar.make(fragment.getView(), t.getLocalizedMessage(), LENGTH_LONG).show();
            }
        });
    }
}
