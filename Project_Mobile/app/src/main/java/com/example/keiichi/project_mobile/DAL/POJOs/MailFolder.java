package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class MailFolder implements Serializable{

    private int childFolderCount;
    private String displayName;
    private String id;
    private String parentFolderId;
    private int totalItemCount;
    private int unreadItemCount;

    public MailFolder(int childFolderCount, String displayName, String id, String parentFolderId, int totalItemCount, int unreadItemCount) {
        this.childFolderCount = childFolderCount;
        this.displayName = displayName;
        this.id = id;
        this.parentFolderId = parentFolderId;
        this.totalItemCount = totalItemCount;
        this.unreadItemCount = unreadItemCount;
    }

    public MailFolder(){

    }

    public int getChildFolderCount() {
        return childFolderCount;
    }

    public void setChildFolderCount(int childFolderCount) {
        this.childFolderCount = childFolderCount;
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

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public int getUnreadItemCount() {
        return unreadItemCount;
    }

    public void setUnreadItemCount(int unreadItemCount) {
        this.unreadItemCount = unreadItemCount;
    }
}
