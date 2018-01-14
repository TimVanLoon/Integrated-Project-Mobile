package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class Website implements Serializable{

    private String url;

    public Website(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
