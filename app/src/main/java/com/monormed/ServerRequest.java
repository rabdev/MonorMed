package com.monormed;

import com.monormed.datastreams.Anamnezis;
import com.monormed.datastreams.PatientList;
import com.monormed.datastreams.PatientLog;
import com.monormed.datastreams.UpComingList;
import com.monormed.datastreams.User;

/**
 * Created by NYG on 2017. 11. 09..
 */

public class ServerRequest {
    private String operation;
    private User user;
    private PatientList patient;
    private UpComingList upComingList;
    private PatientLog patientLog;
    private Anamnezis anamnezis;
    /*private Elojegyzes elojegy;
    private TajType tajType;*/

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPatientList (PatientList patient) {
        this.patient = patient;
    }

    public void setUpComingList (UpComingList upComingList) {this.upComingList = upComingList;}

    public void setPatientLog (PatientLog patientLog) {this.patientLog=patientLog;}

    public void setAnamnezis (Anamnezis anamnezis) {this.anamnezis=anamnezis;};

    /*



    public void setElojegy(Elojegyzes elojegy) {this.elojegy = elojegy;}

    public void setTajType(TajType tajType) {
        this.tajType = tajType;
    }*/
}
