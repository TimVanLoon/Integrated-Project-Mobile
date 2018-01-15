package com.example.keiichi.project_mobile.DAL.POJOs;


import java.io.Serializable;

public class GeoCoordinates implements Serializable {

    private double altitude;
    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitudeAccuracy;

    public GeoCoordinates(double altitude, double latitude, double longitude, double accuracy, double altitudeAccuracy) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitudeAccuracy = altitudeAccuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }
}
