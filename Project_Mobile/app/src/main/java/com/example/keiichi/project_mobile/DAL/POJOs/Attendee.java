package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class Attendee implements Serializable {

    private String type;
    private ResponseStatus status;
    private EmailAddress emailAddress;

    public Attendee(String type, EmailAddress emailAddress) {

        this.type = type;
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
