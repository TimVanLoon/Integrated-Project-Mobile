package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class AutomaticRepliesSetting implements Serializable{

    private String externalAudience;
    private String externalReplyMessage;
    private String internalReplyMessage;
    private DateTimeTimeZone scheduledEndDateTime;
    private DateTimeTimeZone scheduledStartDateTime;
    private String status;

    public AutomaticRepliesSetting(String externalAudience, String externalReplyMessage, String internalReplyMessage, DateTimeTimeZone scheduledEndDateTime, DateTimeTimeZone scheduledStartDateTime, String status) {
        this.externalAudience = externalAudience;
        this.externalReplyMessage = externalReplyMessage;
        this.internalReplyMessage = internalReplyMessage;
        this.scheduledEndDateTime = scheduledEndDateTime;
        this.scheduledStartDateTime = scheduledStartDateTime;
        this.status = status;
    }

    public AutomaticRepliesSetting(){

    }

    public String getExternalAudience() {
        return externalAudience;
    }

    public void setExternalAudience(String externalAudience) {
        this.externalAudience = externalAudience;
    }

    public String getExternalReplyMessage() {
        return externalReplyMessage;
    }

    public void setExternalReplyMessage(String externalReplyMessage) {
        this.externalReplyMessage = externalReplyMessage;
    }

    public String getInternalReplyMessage() {
        return internalReplyMessage;
    }

    public void setInternalReplyMessage(String internalReplyMessage) {
        this.internalReplyMessage = internalReplyMessage;
    }

    public DateTimeTimeZone getScheduledEndDateTime() {
        return scheduledEndDateTime;
    }

    public void setScheduledEndDateTime(DateTimeTimeZone scheduledEndDateTime) {
        this.scheduledEndDateTime = scheduledEndDateTime;
    }

    public DateTimeTimeZone getScheduledStartDateTime() {
        return scheduledStartDateTime;
    }

    public void setScheduledStartDateTime(DateTimeTimeZone scheduledStartDateTime) {
        this.scheduledStartDateTime = scheduledStartDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
