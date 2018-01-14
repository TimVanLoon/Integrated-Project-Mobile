package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class AssignedLicense implements Serializable{

    private List<UUID> disabledPlans;
    private UUID skuId;

    public AssignedLicense(List<UUID> disabledPlans, UUID skuId) {
        this.disabledPlans = disabledPlans;
        this.skuId = skuId;
    }

    public AssignedLicense(){

    }

    public List<UUID> getDisabledPlans() {
        return disabledPlans;
    }

    public void setDisabledPlans(List<UUID> disabledPlans) {
        this.disabledPlans = disabledPlans;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }
}
