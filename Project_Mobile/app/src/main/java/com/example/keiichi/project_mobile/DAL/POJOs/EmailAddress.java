package com.example.keiichi.project_mobile.DAL.POJOs;



public class EmailAddress {

    private String name;
    private String address;

    public EmailAddress(String address) {
        this.address = address;
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
