package com.example.keiichi.project_mobile.POJO;


public class Location {

    private String displayName;
    private PhysicalAddress address;
    private GeoCoordinates coordinates;
    private String locationEmailAddress;

    public Location(String displayName, PhysicalAddress address, GeoCoordinates coordinates, String locationEmailAddress) {
        this.displayName = displayName;
        this.address = address;
        this.coordinates = coordinates;
        this.locationEmailAddress = locationEmailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public PhysicalAddress getAddress() {
        return address;
    }

    public void setAddress(PhysicalAddress address) {
        this.address = address;
    }

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getLocationEmailAddress() {
        return locationEmailAddress;
    }

    public void setLocationEmailAddress(String locationEmailAddress) {
        this.locationEmailAddress = locationEmailAddress;
    }
}
