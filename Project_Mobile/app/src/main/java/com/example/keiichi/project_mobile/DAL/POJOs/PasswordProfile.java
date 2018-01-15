package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class PasswordProfile implements Serializable{

    private boolean forceChangePasswordNextSignIn;
    private String password;

    public PasswordProfile(boolean forceChangePasswordNextSignIn, String password) {
        this.forceChangePasswordNextSignIn = forceChangePasswordNextSignIn;
        this.password = password;
    }

    public PasswordProfile(){

    }

    public boolean isForceChangePasswordNextSignIn() {
        return forceChangePasswordNextSignIn;
    }

    public void setForceChangePasswordNextSignIn(boolean forceChangePasswordNextSignIn) {
        this.forceChangePasswordNextSignIn = forceChangePasswordNextSignIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
