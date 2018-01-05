package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class Extension implements Serializable {

    private String id;

    public Extension(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
