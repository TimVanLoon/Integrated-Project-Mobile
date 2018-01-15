package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;

public class ProvisionedPlan implements Serializable {

    private String capabilityStatus;
    private String provisioningStatus;
    private String service;

    public ProvisionedPlan(String capabilityStatus, String provisioningStatus, String service) {
        this.capabilityStatus = capabilityStatus;
        this.provisioningStatus = provisioningStatus;
        this.service = service;
    }

    public ProvisionedPlan(){

    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    public void setCapabilityStatus(String capabilityStatus) {
        this.capabilityStatus = capabilityStatus;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }

    public void setProvisioningStatus(String provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
