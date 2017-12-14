package com.example.keiichi.project_mobile.DAL.POJOs;


public class RecurrencePattern {

    private String type;
    private int interval;
    private int dayOfMonth;
    private int month;
    private String[] daysOfWeek;
    private String firstDayOfWeek;
    private String index;

    public RecurrencePattern(String type, int interval, int dayOfMonth, int month, String[] daysOfWeek, String firstDayOfWeek, String index) {
        this.type = type;
        this.interval = interval;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.daysOfWeek = daysOfWeek;
        this.firstDayOfWeek = firstDayOfWeek;
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(String firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
