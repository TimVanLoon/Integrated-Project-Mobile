package com.example.keiichi.project_mobile;

/**
 * Created by keanuvanhees on 16/01/18.
 */

public class AccessTokenSingleton {

    String accessToken;
    String firebaseToken;

    private static AccessTokenSingleton instance;

    private AccessTokenSingleton(){}

    public static AccessTokenSingleton getInstance(){
        if(instance == null){
            instance = new AccessTokenSingleton();
        }
        return instance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String toString()
    {
        return "AT:" + accessToken + " FB:" + firebaseToken;
    }
}