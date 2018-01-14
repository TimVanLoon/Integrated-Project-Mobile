package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;
import java.util.Date;

public class RecurrenceRange implements Serializable {

    private String type;
    private Date startDate;
    private Date endDate;
    private int numberOfOccurrences;

    public RecurrenceRange(String type, Date startDate, Date endDate, int numberOfOccurrences) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void setNumberOfOccurrences(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }
}
