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

}
