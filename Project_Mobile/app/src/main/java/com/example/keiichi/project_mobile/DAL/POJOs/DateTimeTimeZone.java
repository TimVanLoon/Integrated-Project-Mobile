package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;
import java.util.Date;

public class DateTimeTimeZone implements Serializable {

    private String dateTime;
    private String timeZone;

    public DateTimeTimeZone(String dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
