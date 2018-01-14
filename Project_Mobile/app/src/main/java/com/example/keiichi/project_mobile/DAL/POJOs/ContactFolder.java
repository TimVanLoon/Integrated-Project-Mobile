package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class ContactFolder implements Serializable {

    private String displayName;
    private String id;
    private String parentFolderId;

    public ContactFolder(String displayName, String id, String parentFolderId) {
        this.displayName = displayName;
        this.id = id;
        this.parentFolderId = parentFolderId;
    }

    public ContactFolder(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
}
