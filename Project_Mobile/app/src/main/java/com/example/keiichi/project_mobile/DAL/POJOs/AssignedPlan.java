package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;
import java.util.UUID;

public class AssignedPlan implements Serializable {

    private String assignedDateTime;
    private String capabilityStatus;
    private String service;
    private UUID servicePlanId;

    public AssignedPlan(String assignedDateTime, String capabilityStatus, String service, UUID servicePlanId) {
        this.assignedDateTime = assignedDateTime;
        this.capabilityStatus = capabilityStatus;
        this.service = service;
        this.servicePlanId = servicePlanId;
    }

    public AssignedPlan(){

    }

    public String getAssignedDateTime() {
        return assignedDateTime;
    }

    public void setAssignedDateTime(String assignedDateTime) {
        this.assignedDateTime = assignedDateTime;
    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    public void setCapabilityStatus(String capabilityStatus) {
        this.capabilityStatus = capabilityStatus;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public UUID getServicePlanId() {
        return servicePlanId;
    }

    public void setServicePlanId(UUID servicePlanId) {
        this.servicePlanId = servicePlanId;
    }
}
