package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class EmailAddress implements Serializable {

    private String name;
    private String address;

    public EmailAddress(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public EmailAddress(String address) {
        this.address = address;
    }

    public EmailAddress() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
