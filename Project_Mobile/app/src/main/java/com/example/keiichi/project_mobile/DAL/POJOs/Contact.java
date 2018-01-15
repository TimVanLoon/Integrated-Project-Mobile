package com.example.keiichi.project_mobile.DAL.POJOs;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Contact implements Serializable {

    private Contact[] contact;
    private String assistantName;
    private String birthday;
    private PhysicalAddress businessAddress;
    private PhysicalAddress otherAddress;
    private String businessHomePage;
    private List<String> businessPhones;
    private String[] categories;
    private String changeKey;
    private String[] children;
    private String companyName;
    private String createdDateTime;
    private String department;
    private String displayName;
    private List<EmailAddress> emailAddresses;
    private String fileAs;
    private String generation;
    private String givenName;
    private PhysicalAddress homeAddress;
    private List<String> homePhones;
    private String id;
    private List<String> imAddresses;
    private String initials;
    private String jobTitle;
    private String lastModifiedDateTime;
    private String manager;
    private String middleName;
    private String mobilePhone;
    private String nickName;
    private String officeLocation;
    private String parentFolderId;
    private String personalNotes;
    private String profession;
    private String spouseName;
    private String surname;
    private String title;
    private String yomiCompanyName;
    private String yomiGivenName;
    private String yomiSurname;
    private byte[] profilePhoto;

    public PhysicalAddress getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(PhysicalAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public List<String> getHomePhones() {
        return homePhones;
    }

    public void setHomePhones(List<String> homePhones) {
        this.homePhones = homePhones;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Contact[] getContacts() {
        return contact;
    }

    public void setValue(Contact[] events) {
        this.contact = events;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public PhysicalAddress getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(PhysicalAddress businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessHomePage() {
        return businessHomePage;
    }

    public void setBusinessHomePage(String businessHomePage) {
        this.businessHomePage = businessHomePage;
    }

    public List<String> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(List<String> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileAs() {
        return fileAs;
    }

    public void setFileAs(String fileAs) {
        this.fileAs = fileAs;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImAddresses() {
        return imAddresses;
    }

    public void setImAddresses(List<String> imAddresses) {
        this.imAddresses = imAddresses;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYomiCompanyName() {
        return yomiCompanyName;
    }

    public void setYomiCompanyName(String yomiCompanyName) {
        this.yomiCompanyName = yomiCompanyName;
    }

    public String getYomiGivenName() {
        return yomiGivenName;
    }

    public void setYomiGivenName(String yomiGivenName) {
        this.yomiGivenName = yomiGivenName;
    }

    public String getYomiSurname() {
        return yomiSurname;
    }

    public void setYomiSurname(String yomiSurname) {
        this.yomiSurname = yomiSurname;
    }

    public Contact[] getContact() {
        return contact;
    }

    public void setContact(Contact[] contact) {
        this.contact = contact;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public PhysicalAddress getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(PhysicalAddress otherAddress) {
        this.otherAddress = otherAddress;
    }
}
