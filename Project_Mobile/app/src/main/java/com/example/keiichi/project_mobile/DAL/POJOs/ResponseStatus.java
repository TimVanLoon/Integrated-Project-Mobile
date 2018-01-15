package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class ResponseStatus implements Serializable{

    private ResponseType responseType;
    private String time;

    private enum ResponseType{
        None, Organizer, TentativelyAccepted, Accepted, Declined, NotResponded
    }

    public ResponseStatus(ResponseType responseType, String time) {
        this.responseType = responseType;
        this.time = time;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
