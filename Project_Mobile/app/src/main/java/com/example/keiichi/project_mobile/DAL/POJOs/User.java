package com.example.keiichi.project_mobile.DAL.POJOs;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String aboutMe;
    private boolean accountEnabled;
    private List<AssignedLicense> assignedLicenses;
    private List<AssignedPlan> assignedPlans;
    private String birthday;
    private List<String> businessPhones;
    private String city;
    private String companyName;
    private String country;
    private String department;
    private String displayName;
    private String givenName;
    private String hireDate;
    private String id;
    private List<String> interests;
    private String jobTitle;
    private String mail;
    private MailboxSettings mailboxSettings;
    private String mailNickname;
    private String mobilePhone;
    private String mySite;
    private String officeLocation;
    private String onPremisesImmutableId;
    private String onPremisesLastSyncDateTime;
    private String onPremisesSecurityIdentifier;
    private boolean onPremisesSyncEnabled;
    private String passwordPolicies;
    private PasswordProfile passwordProfile;
    private List<String> pastProjects;
    private String postalCode;
    private String preferredLanguage;
    private String preferredName;
    private List<ProvisionedPlan> provisionedPlans;
    private List<String> proxyAddresses;
    private List<String> responsibilities;
    private List<String> schools;
    private List<String> skills;
    private String state;
    private String streetAddress;
    private String surname;
    private String usageLocation;
    private String userPrincipalName;
    private String userType;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public List<AssignedLicense> getAssignedLicenses() {
        return assignedLicenses;
    }

    public void setAssignedLicenses(List<AssignedLicense> assignedLicenses) {
        this.assignedLicenses = assignedLicenses;
    }

    public List<AssignedPlan> getAssignedPlans() {
        return assignedPlans;
    }

    public void setAssignedPlans(List<AssignedPlan> assignedPlans) {
        this.assignedPlans = assignedPlans;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(List<String> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public MailboxSettings getMailboxSettings() {
        return mailboxSettings;
    }

    public void setMailboxSettings(MailboxSettings mailboxSettings) {
        this.mailboxSettings = mailboxSettings;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMySite() {
        return mySite;
    }

    public void setMySite(String mySite) {
        this.mySite = mySite;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOnPremisesImmutableId() {
        return onPremisesImmutableId;
    }

    public void setOnPremisesImmutableId(String onPremisesImmutableId) {
        this.onPremisesImmutableId = onPremisesImmutableId;
    }

    public String getOnPremisesLastSyncDateTime() {
        return onPremisesLastSyncDateTime;
    }

    public void setOnPremisesLastSyncDateTime(String onPremisesLastSyncDateTime) {
        this.onPremisesLastSyncDateTime = onPremisesLastSyncDateTime;
    }

    public String getOnPremisesSecurityIdentifier() {
        return onPremisesSecurityIdentifier;
    }

    public void setOnPremisesSecurityIdentifier(String onPremisesSecurityIdentifier) {
        this.onPremisesSecurityIdentifier = onPremisesSecurityIdentifier;
    }

    public boolean isOnPremisesSyncEnabled() {
        return onPremisesSyncEnabled;
    }

    public void setOnPremisesSyncEnabled(boolean onPremisesSyncEnabled) {
        this.onPremisesSyncEnabled = onPremisesSyncEnabled;
    }

    public String getPasswordPolicies() {
        return passwordPolicies;
    }

    public void setPasswordPolicies(String passwordPolicies) {
        this.passwordPolicies = passwordPolicies;
    }

    public PasswordProfile getPasswordProfile() {
        return passwordProfile;
    }

    public void setPasswordProfile(PasswordProfile passwordProfile) {
        this.passwordProfile = passwordProfile;
    }

    public List<String> getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(List<String> pastProjects) {
        this.pastProjects = pastProjects;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public List<ProvisionedPlan> getProvisionedPlans() {
        return provisionedPlans;
    }

    public void setProvisionedPlans(List<ProvisionedPlan> provisionedPlans) {
        this.provisionedPlans = provisionedPlans;
    }

    public List<String> getProxyAddresses() {
        return proxyAddresses;
    }

    public void setProxyAddresses(List<String> proxyAddresses) {
        this.proxyAddresses = proxyAddresses;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsageLocation() {
        return usageLocation;
    }

    public void setUsageLocation(String usageLocation) {
        this.usageLocation = usageLocation;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(){

    }

}
