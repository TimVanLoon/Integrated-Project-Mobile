package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class Phone implements Serializable {

    private String number;
    private String type;

    public Phone(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public Phone(String number) {
        this.number = number;
    }

    public Phone(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
