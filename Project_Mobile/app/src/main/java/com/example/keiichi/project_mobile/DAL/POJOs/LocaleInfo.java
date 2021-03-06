package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class LocaleInfo implements Serializable{

    private String locale;
    private String displayName;

    public LocaleInfo(String locale, String displayName) {
        this.locale = locale;
        this.displayName = displayName;
    }

    public LocaleInfo(){

    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
