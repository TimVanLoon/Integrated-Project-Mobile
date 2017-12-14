package com.example.keiichi.project_mobile.POJO;


import java.util.Date;

public class DateTimeTimeZone {

    private Date dateTime;
    private String timeZone;

    public DateTimeTimeZone(Date dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
