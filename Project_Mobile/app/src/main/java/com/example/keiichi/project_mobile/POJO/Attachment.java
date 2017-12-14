package com.example.keiichi.project_mobile.POJO;


public class Attachment {

    private String contentType;
    private String id;
    private boolean isInline;
    private String lastModifiedDateTime;
    private String name;
    private int size;

    public Attachment(String contentType, String id, boolean isInline, String lastModifiedDateTime, String name, int size) {
        this.contentType = contentType;
        this.id = id;
        this.isInline = isInline;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.name = name;
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInline() {
        return isInline;
    }

    public void setInline(boolean inline) {
        isInline = inline;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
