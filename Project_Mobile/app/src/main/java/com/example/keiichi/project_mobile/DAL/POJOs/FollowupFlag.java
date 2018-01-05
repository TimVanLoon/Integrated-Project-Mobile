package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class FollowupFlag implements Serializable {

    private DateTimeTimeZone completedDateTime;
    private DateTimeTimeZone dueDateTime;
    private String flagStatus;
    private DateTimeTimeZone startDateTime;

    public FollowupFlag(DateTimeTimeZone completedDateTime, DateTimeTimeZone dueDateTime, String flagStatus, DateTimeTimeZone startDateTime) {
        this.completedDateTime = completedDateTime;
        this.dueDateTime = dueDateTime;
        this.flagStatus = flagStatus;
        this.startDateTime = startDateTime;
    }

    public DateTimeTimeZone getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(DateTimeTimeZone completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public DateTimeTimeZone getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(DateTimeTimeZone dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getFlagStatus() {
        return flagStatus;
    }

    public void setFlagStatus(String flagStatus) {
        this.flagStatus = flagStatus;
    }

    public DateTimeTimeZone getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTimeTimeZone startDateTime) {
        this.startDateTime = startDateTime;
    }
}
