package com.monormed;

import com.monormed.datastreams.User;

/**
 * Created by NYG on 2017. 11. 09..
 */

public class ServerResponse {
    private String result;
    private String message;
    private User user;
    /*private PatientList patient1;
    private Anamnezis anamnezis;
    private Elojegyzes elojegy;*/

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    /*public PatientList getPatient(){return patient1;}

    public Anamnezis getAnamnezis(){return anamnezis;}

    private PatientList [] patient;

    public PatientList [] getPatientList(){
        return patient;
    }

    private UpComingList [] upComingList;

    public UpComingList [] getUpComingList(){return upComingList;}

    private PatientLog [] patientLog;

    public PatientLog [] getPatientLog(){return patientLog;}

    public Elojegyzes getElojegy() {
        return elojegy;
    }

    private Doctors[] doctorList;

    public Doctors[] getDoctorList() {
        return doctorList;
    }

    private TajType[] taj_tipus;

    public TajType[] getTajType(){
        return taj_tipus;
    }*/
}
