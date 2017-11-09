package com.monormed.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.BaseInputConnection;
import android.widget.ImageView;
import android.widget.TextView;

import com.monormed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPatient extends Fragment {

    View addpatient;
    Window window;
    ImageView btn_addpatient_exit;

    public AddPatient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addpatient = inflater.inflate(R.layout.fragment_addpatient, container, false);
        window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorBlue, getActivity().getTheme()));

        btn_addpatient_exit = (ImageView) addpatient.findViewById(R.id.addpatient_exit);

        btn_addpatient_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseInputConnection mInputConnection = new BaseInputConnection(addpatient, true);
                mInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });

        return addpatient;
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
                    getActivity().getSupportFragmentManager().popBackStack();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
                    return true;
                }
                return false;
            }
        });
    }

}
