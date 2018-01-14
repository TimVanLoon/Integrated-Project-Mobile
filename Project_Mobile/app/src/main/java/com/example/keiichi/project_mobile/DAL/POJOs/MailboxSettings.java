package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class MailboxSettings implements Serializable {

    private AutomaticRepliesSetting automaticRepliesSetting;
    private LocaleInfo language;
    private String timeZone;

    public MailboxSettings(AutomaticRepliesSetting automaticRepliesSetting, LocaleInfo language, String timeZone) {
        this.automaticRepliesSetting = automaticRepliesSetting;
        this.language = language;
        this.timeZone = timeZone;
    }

    public MailboxSettings(){

    }

    public AutomaticRepliesSetting getAutomaticRepliesSetting() {
        return automaticRepliesSetting;
    }

    public void setAutomaticRepliesSetting(AutomaticRepliesSetting automaticRepliesSetting) {
        this.automaticRepliesSetting = automaticRepliesSetting;
    }

    public LocaleInfo getLanguage() {
        return language;
    }

    public void setLanguage(LocaleInfo language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
