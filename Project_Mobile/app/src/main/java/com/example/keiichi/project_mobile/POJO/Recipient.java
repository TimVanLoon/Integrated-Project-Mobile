package com.example.keiichi.project_mobile.POJO;


public class Recipient {

    private EmailAddress emailAddress;

    public Recipient(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
