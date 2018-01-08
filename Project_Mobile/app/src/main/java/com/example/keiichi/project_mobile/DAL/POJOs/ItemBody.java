package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class ItemBody implements Serializable {

    private String contentType;
    private String content;

    public ItemBody(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
