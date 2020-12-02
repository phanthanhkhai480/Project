package com.group12.logintest;

public class Events {

    String EVENT,TIME,DATE,MONTH,YEAR, DESC,SibblingEventDate, sibblingEventTime, budget;

    public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
    }

    public String getEVENT() {
        if(EVENT.split("#").length>0)
            return EVENT.split("#")[0];
        return "";
    }

    public String getBudget() {
        if(EVENT.split("#").length>2)
            return EVENT.split("#")[2];
        return "";
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getDESC() {
        if(EVENT.split("#").length>1)
            return EVENT.split("#")[1];
        return "";
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getSibblingEventDate() {
        if(EVENT.split("#").length>2)
            return EVENT.split("#")[2];
        return "";
    }

    public void setSibblingEventDate(String sibblingEventDate) {
        SibblingEventDate = sibblingEventDate;
    }

    public String getSibblingEventTime() {
        if(EVENT.split("#").length>3)
            return EVENT.split("#")[3];
        return "";
    }

    public void setSibblingEventTime(String sibblingEventTime) {
        this.sibblingEventTime = sibblingEventTime;
    }
}
