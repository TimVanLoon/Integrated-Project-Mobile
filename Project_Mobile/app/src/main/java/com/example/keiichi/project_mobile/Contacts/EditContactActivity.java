package com.example.keiichi.project_mobile.Contacts;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.PhysicalAddress;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class EditContactActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/contacts/";
    final private int DELAY_TIME = 100;
    private DatePickerDialog datePickerDialog;
    private List<String> homePhones = new ArrayList<>();
    private List<String> businessPhones = new ArrayList<>();
    private List<EmailAddress> emailList;
    private List<String> imAddresses = new ArrayList<>();
    private Toolbar myToolbar;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String givenName;
    private String displayName;
    private String phoneNumber;
    private String emailAddress;
    private String notes;
    private String nickname;
    private String spouse;
    private String homeStreet;
    private String homePostalCode;
    private String homeCity;
    private String homeState;
    private String homeCountry;
    private String businessStreet;
    private String businessPostalCode;
    private String businessCity;
    private String businessState;
    private String businessCountry;
    private String otherStreet;
    private String otherPostalCode;
    private String otherCity;
    private String otherState;
    private String otherCountry;
    private String job;
    private String department;
    private String company;
    private String office;
    private String manager;
    private String assistant;
    private String firstName;
    private String lastName;
    private String id;
    private String contactBirthday;
    private String contactMiddleName;
    private String contactTitle;
    private String contactSuffix;
    private String contactYomiCompanyName;
    private String finalMonth;
    private String finalDayOfMonth;
    private String contactYomiFirstName;
    private String contactYomiLastName;
    private String contactId;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText jobTitleInput;
    private EditText departmentInput;
    private EditText companyNameInput;
    private EditText officeLocationInput;
    private EditText managerInput;
    private EditText assistantNameInput;
    private EditText homeStreetNameInput;
    private EditText homePostalCodeInput;
    private EditText homeCityNameInput;
    private EditText homeStateNameInput;
    private EditText homeCountryNameInput;
    private EditText personalNotesInput;
    private EditText nickNameInput;
    private EditText spouseNameInput;
    private EditText businessStreetNameInput;
    private EditText businessPostalCodeInput;
    private EditText businessCityNameInput;
    private EditText businessStateNameInput;
    private EditText businessCountryNameInput;
    private EditText otherStreetNameInput;
    private EditText otherPostalCodeInput;
    private EditText otherCityNameInput;
    private EditText otherStateNameInput;
    private EditText otherCountryNameInput;
    private EditText middleNameInput;
    private EditText titleInput;
    private EditText suffixInput;
    private EditText yomiFirstNameInput;
    private EditText yomiLastNameInput;
    private EditText emailInput2;
    private EditText displayAsInput2;
    private EditText displayAsInput;
    private EditText emailInput3;
    private EditText displayAsInput3;
    private EditText businessPhoneInput2;
    private EditText homePhoneInput;
    private EditText homePhoneInput2;
    private EditText mobilePhoneInput;
    private EditText yomiCompanyInput;
    private EditText imInput;
    private EditText birthdayInput;
    private TextView businessPhoneTitle2;
    private TextView homePhoneTitle;
    private TextView homePhoneTitle2;
    private TextView mobilePhoneTitle;
    private TextView middleNameTitle;
    private TextView titleTitle;
    private TextView suffixTitle;
    private TextView yomiFirstNameTitle;
    private TextView yomiLastNameTitle;
    private TextView emailSubTitle2;
    private TextView displayAsTitle2;
    private TextView emailSubTitle3;
    private TextView displayAsTitle3;
    private TextView jobTitle;
    private TextView departmentTitle;
    private TextView companyNameTitle;
    private TextView officeLocationTitle;
    private TextView managerTitle;
    private TextView assistantTitle;
    private TextView yomiCompanyTitle;
    private TextView imSubTitle;
    private TextView businessAddressTitle;
    private TextView businessStreetNameTitle;
    private TextView businessPostalCodeTitle;
    private TextView businessCityNameTitle;
    private TextView businessStateTitle;
    private TextView businessCountryTitle;
    private TextView homeAddressTitle;
    private TextView homeStreetNameTitle;
    private TextView homePostalCodeTitle;
    private TextView homeCityNameTitle;
    private TextView homeStateTitle;
    private TextView homeCountryTitle;
    private TextView otherAddressTitle;
    private TextView otherStreetNameTitle;
    private TextView otherPostalCodeTitle;
    private TextView otherCityNameTitle;
    private TextView otherStateTitle;
    private TextView otherCountryTitle;
    private TextView nickNameTitle;
    private TextView spouseNameTitle;
    private TextView birthdayTitle;
    private MenuItem saveItem;
    private Contact contact;
    private Contact updatedContact;
    private int dayOfMonth;
    private int month;
    private int year;
    private ImageView plusNameIcon;
    private ImageView plusEmailIcon;
    private ImageView plusPhoneIcon;
    private ImageView plusWorkIcon;
    private ImageView plusNotesIcon;
    private ImageView plusImIcon;
    private ImageView plusAddressIcon;
    private ImageView plusOtherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        contactId = getIntent().getStringExtra("contactId");

        System.out.println("contact id: " + contactId);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        givenName = contact.getGivenName();
        displayName = contact.getDisplayName();
        givenName = contact.getGivenName();
        phoneNumber = contact.getMobilePhone();
        job = contact.getJobTitle();
        department = contact.getDepartment();
        company = contact.getCompanyName();
        office = contact.getOfficeLocation();
        manager = contact.getManager();
        assistant = contact.getAssistantName();
        firstName = contact.getGivenName();
        lastName = contact.getSurname();
        notes = contact.getPersonalNotes();
        spouse = contact.getSpouseName();
        nickname = contact.getNickName();
        emailList = contact.getEmailAddresses();
        businessPhones = contact.getBusinessPhones();
        homePhones = contact.getHomePhones();
        contactBirthday = contact.getBirthday();
        imAddresses = contact.getImAddresses();
        contactMiddleName = contact.getMiddleName();
        contactTitle = contact.getTitle();
        contactSuffix = contact.getInitials();
        contactYomiCompanyName = contact.getYomiCompanyName();
        id = contact.getId();
        contactYomiFirstName = contact.getYomiGivenName();
        contactYomiLastName = contact.getYomiSurname();

        if(contact.getHomeAddress() != null){
            homeStreet = contact.getHomeAddress().getStreet();
            homePostalCode = contact.getHomeAddress().getPostalCode();
            homeCity = contact.getHomeAddress().getCity();
            homeState = contact.getHomeAddress().getState();
            homeCountry = contact.getHomeAddress().getCountryOrRegion();
        }

        if(contact.getBusinessAddress() != null){
            businessStreet = contact.getBusinessAddress().getStreet();
            businessPostalCode = contact.getBusinessAddress().getPostalCode();
            businessCity = contact.getBusinessAddress().getCity();
            businessState = contact.getBusinessAddress().getState();
            businessCountry = contact.getBusinessAddress().getCountryOrRegion();
        }

        if(contact.getOtherAddress() != null){
            otherStreet = contact.getOtherAddress().getStreet();
            otherPostalCode = contact.getOtherAddress().getPostalCode();
            otherCity = contact.getOtherAddress().getCity();
            otherState = contact.getOtherAddress().getState();
            otherCountry = contact.getOtherAddress().getCountryOrRegion();
        }

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        mobilePhoneInput = (EditText) findViewById(R.id.mobilePhoneInput);
        jobTitleInput = (EditText) findViewById(R.id.jobInput);
        departmentInput = (EditText) findViewById(R.id.department);
        companyNameInput = (EditText) findViewById(R.id.companyName);
        officeLocationInput = (EditText) findViewById(R.id.officeLocation);
        managerInput = (EditText) findViewById(R.id.manager);
        assistantNameInput = (EditText) findViewById(R.id.assistantName);
        homeStreetNameInput = (EditText) findViewById(R.id.homeStreetNameInput);
        homePostalCodeInput = (EditText) findViewById(R.id.homePostalCodeInput);
        homeCityNameInput = (EditText) findViewById(R.id.homeCityNameInput);
        homeStateNameInput = (EditText) findViewById(R.id.homeStateNameInput);
        homeCountryNameInput = (EditText) findViewById(R.id.homeCountryNameInput);
        personalNotesInput = (EditText) findViewById(R.id.personalNotes);
        nickNameInput = (EditText) findViewById(R.id.nickNameInput);
        spouseNameInput = (EditText) findViewById(R.id.spouseNameInput);
        businessStreetNameInput = (EditText) findViewById(R.id.businessStreetNameInput);
        businessPostalCodeInput = (EditText) findViewById(R.id.businessPostalCodeInput);
        businessPostalCodeInput = (EditText) findViewById(R.id.businessPostalCodeInput);
        businessCityNameInput = (EditText) findViewById(R.id.businessCityNameInput);
        businessStateNameInput = (EditText) findViewById(R.id.businessStateNameInput);
        businessCountryNameInput = (EditText) findViewById(R.id.businessCountryNameInput);
        homeStreetNameInput = (EditText) findViewById(R.id.homeStreetNameInput);
        homePostalCodeInput = (EditText) findViewById(R.id.homePostalCodeInput);
        homeCityNameInput = (EditText) findViewById(R.id.homeCityNameInput);
        homeStateNameInput = (EditText) findViewById(R.id.homeStateNameInput);
        homeCountryNameInput = (EditText) findViewById(R.id.homeCountryNameInput);
        otherStreetNameInput = (EditText) findViewById(R.id.otherStreetNameInput);
        otherPostalCodeInput = (EditText) findViewById(R.id.otherPostalCodeInput);
        otherCityNameInput = (EditText) findViewById(R.id.otherCityNameInput);
        otherStateNameInput = (EditText) findViewById(R.id.otherStateNameInput);
        otherCountryNameInput = (EditText) findViewById(R.id.otherCountryNameInput);
        yomiCompanyInput = (EditText) findViewById(R.id.yomiCompanyInput);
        imInput = (EditText) findViewById(R.id.imInput);
        birthdayInput = (EditText) findViewById(R.id.birthdayInput);
        middleNameInput = (EditText) findViewById(R.id.middleNameInput);
        titleInput = (EditText) findViewById(R.id.titleInput);
        suffixInput = (EditText) findViewById(R.id.suffixInput);
        yomiFirstNameInput = (EditText) findViewById(R.id.yomiFirstNameInput);
        yomiLastNameInput = (EditText) findViewById(R.id.yomiLastNameInput);
        emailInput2 = (EditText) findViewById(R.id.emailInput2);
        displayAsInput2 = (EditText) findViewById(R.id.displayAsInput2);
        displayAsInput = (EditText) findViewById(R.id.displayAsInput);
        emailInput3 = (EditText) findViewById(R.id.emailInput3);
        displayAsInput3 = (EditText) findViewById(R.id.displayAsInput3);
        businessPhoneInput2 = (EditText) findViewById(R.id.businessPhoneInput2);
        homePhoneInput = (EditText) findViewById(R.id.homePhoneInput);
        homePhoneInput2 = (EditText) findViewById(R.id.homePhoneInput2);
        middleNameTitle = (TextView) findViewById(R.id.middleNameTitle);
        titleTitle = (TextView) findViewById(R.id.titleTitle);
        suffixTitle = (TextView) findViewById(R.id.suffixTitle);
        yomiFirstNameTitle = (TextView) findViewById(R.id.yomiFirstNameTitle);
        yomiLastNameTitle = (TextView) findViewById(R.id.yomiLastNameTitle);
        emailSubTitle2 = (TextView) findViewById(R.id.emailSubTitle2);
        displayAsTitle2 = (TextView) findViewById(R.id.displayAsTitle2);
        emailSubTitle3 = (TextView) findViewById(R.id.emailSubTitle3);
        displayAsTitle3 = (TextView) findViewById(R.id.displayAsTitle3);
        businessPhoneTitle2 = (TextView) findViewById(R.id.businessPhoneTitle2);
        homePhoneTitle = (TextView) findViewById(R.id.homePhoneTitle);
        homePhoneTitle2 = (TextView) findViewById(R.id.homePhoneTitle2);
        mobilePhoneTitle = (TextView) findViewById(R.id.mobilePhoneTitle);
        departmentTitle = (TextView) findViewById(R.id.departmentTitle);
        jobTitle = (TextView) findViewById(R.id.jobTitle);
        companyNameTitle = (TextView) findViewById(R.id.companyNameTitle);
        officeLocationTitle = (TextView) findViewById(R.id.officeLocationTitle);
        managerTitle = (TextView) findViewById(R.id.managerTitle);
        yomiCompanyTitle = (TextView) findViewById(R.id.yomiCompanyTitle);
        assistantTitle = (TextView) findViewById(R.id.assistantTitle);
        imSubTitle = (TextView) findViewById(R.id.imSubTitle);
        businessAddressTitle = (TextView) findViewById(R.id.businessAddressTitle);
        businessStreetNameTitle = (TextView) findViewById(R.id.businessStreetNameTitle);
        businessPostalCodeTitle = (TextView) findViewById(R.id.businessPostalCodeTitle);
        businessCityNameTitle = (TextView) findViewById(R.id.businessCityNameTitle);
        businessStateTitle = (TextView) findViewById(R.id.businessStateTitle);
        businessCountryTitle = (TextView) findViewById(R.id.businessCountryTitle);
        homeAddressTitle = (TextView) findViewById(R.id.homeAddressTitle);
        homeStreetNameTitle = (TextView) findViewById(R.id.homeStreetNameTitle);
        homePostalCodeTitle = (TextView) findViewById(R.id.homePostalCodeTitle);
        homeCityNameTitle = (TextView) findViewById(R.id.homeCityNameTitle);
        homeStateTitle = (TextView) findViewById(R.id.homeStateTitle);
        homeCountryTitle = (TextView) findViewById(R.id.homeCountryTitle);
        otherAddressTitle = (TextView) findViewById(R.id.otherAddressTitle);
        otherStreetNameTitle = (TextView) findViewById(R.id.otherStreetNameTitle);
        otherPostalCodeTitle = (TextView) findViewById(R.id.otherPostalCodeTitle);
        otherCityNameTitle = (TextView) findViewById(R.id.otherCityNameTitle);
        otherStateTitle = (TextView) findViewById(R.id.otherStateTitle);
        otherCountryTitle = (TextView) findViewById(R.id.otherCountryTitle);
        nickNameTitle = (TextView) findViewById(R.id.nickNameTitle);
        spouseNameTitle = (TextView) findViewById(R.id.spouseNameTitle);
        birthdayTitle = (TextView) findViewById(R.id.birthdayTitle);
        plusNameIcon = (ImageView) findViewById(R.id.plusNameIcon);
        plusEmailIcon = (ImageView) findViewById(R.id.plusEmailIcon);
        plusPhoneIcon = (ImageView) findViewById(R.id.plusPhoneIcon);
        plusWorkIcon = (ImageView) findViewById(R.id.plusWorkIcon);
        plusNotesIcon = (ImageView) findViewById(R.id.plusNotesIcon);
        plusImIcon = (ImageView) findViewById(R.id.plusImIcon);
        plusAddressIcon = (ImageView) findViewById(R.id.plusAddressIcon);
        plusOtherIcon = (ImageView) findViewById(R.id.plusOtherIcon);

        makeExtraInvisible();

        fillInputs();

        setEditTextOnFocusListener(firstNameInput);
        setEditTextOnFocusListener(lastNameInput);
        setEditTextOnFocusListener(emailInput);
        setEditTextOnFocusListener(jobTitleInput);
        setEditTextOnFocusListener(departmentInput);
        setEditTextOnFocusListener(companyNameInput);
        setEditTextOnFocusListener(officeLocationInput);
        setEditTextOnFocusListener(managerInput);
        setEditTextOnFocusListener(assistantNameInput);
        setEditTextOnFocusListener(businessStreetNameInput);
        setEditTextOnFocusListener(businessPostalCodeInput);
        setEditTextOnFocusListener(businessCityNameInput);
        setEditTextOnFocusListener(businessStateNameInput);
        setEditTextOnFocusListener(businessCountryNameInput);
        setEditTextOnFocusListener(personalNotesInput);
        setEditTextOnFocusListener(nickNameInput);
        setEditTextOnFocusListener(spouseNameInput);
        setEditTextOnFocusListener(middleNameInput);
        setEditTextOnFocusListener(titleInput);
        setEditTextOnFocusListener(suffixInput);
        setEditTextOnFocusListener(yomiFirstNameInput);
        setEditTextOnFocusListener(yomiLastNameInput);
        setEditTextOnFocusListener(emailInput2);
        setEditTextOnFocusListener(displayAsInput2);
        setEditTextOnFocusListener(displayAsInput);
        setEditTextOnFocusListener(emailInput3);
        setEditTextOnFocusListener(displayAsInput3);
        setEditTextOnFocusListener(businessPhoneInput2);
        setEditTextOnFocusListener(homePhoneInput);
        setEditTextOnFocusListener(homePhoneInput2);
        setEditTextOnFocusListener(mobilePhoneInput);
        setEditTextOnFocusListener(yomiCompanyInput);
        setEditTextOnFocusListener(imInput);
        setEditTextOnFocusListener(homeStreetNameInput);
        setEditTextOnFocusListener(homePostalCodeInput);
        setEditTextOnFocusListener(homeCityNameInput);
        setEditTextOnFocusListener(homeStateNameInput);
        setEditTextOnFocusListener(homeCountryNameInput);
        setEditTextOnFocusListener(otherStreetNameInput);
        setEditTextOnFocusListener(otherPostalCodeInput);
        setEditTextOnFocusListener(otherCityNameInput);
        setEditTextOnFocusListener(otherStateNameInput);
        setEditTextOnFocusListener(otherCountryNameInput);
        setEditTextOnFocusListener(nickNameInput);
        setEditTextOnFocusListener(spouseNameInput);
        setEditTextOnFocusListener(birthdayInput);

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        plusNameIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.inflate(R.menu.name_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_middleName:
                                middleNameInput.setVisibility(View.VISIBLE);
                                middleNameTitle.setVisibility(View.VISIBLE);

                                middleNameInput.setFocusableInTouchMode(true);
                                middleNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(middleNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_title:
                                titleInput.setVisibility(View.VISIBLE);
                                titleTitle.setVisibility(View.VISIBLE);

                                titleInput.setFocusableInTouchMode(true);
                                titleInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(titleInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_suffix:
                                suffixInput.setVisibility(View.VISIBLE);
                                suffixTitle.setVisibility(View.VISIBLE);

                                suffixInput.setFocusableInTouchMode(true);
                                suffixInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(suffixInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_yomiName:
                                yomiFirstNameInput.setVisibility(View.VISIBLE);
                                yomiLastNameInput.setVisibility(View.VISIBLE);
                                yomiFirstNameTitle.setVisibility(View.VISIBLE);
                                yomiLastNameTitle.setVisibility(View.VISIBLE);

                                yomiFirstNameInput.setFocusableInTouchMode(true);
                                yomiFirstNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(yomiFirstNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                        }

                        return false;
                    }
                });
            }
        });

        plusEmailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.inflate(R.menu.email_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_email2:
                                emailInput2.setVisibility(View.VISIBLE);
                                emailSubTitle2.setVisibility(View.VISIBLE);
                                displayAsInput2.setVisibility(View.VISIBLE);
                                displayAsTitle2.setVisibility(View.VISIBLE);

                                emailInput2.setFocusableInTouchMode(true);
                                emailInput2.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(emailInput2);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_email3:
                                emailInput3.setVisibility(View.VISIBLE);
                                emailSubTitle3.setVisibility(View.VISIBLE);
                                displayAsInput3.setVisibility(View.VISIBLE);
                                displayAsTitle3.setVisibility(View.VISIBLE);

                                emailInput3.setFocusableInTouchMode(true);
                                emailInput3.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(emailInput3);

                                    }
                                }, DELAY_TIME);

                                break;

                        }

                        return false;
                    }
                });
            }
        });

        plusPhoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.inflate(R.menu.phone_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_businessPhone:
                                businessPhoneInput2.setVisibility(View.VISIBLE);
                                businessPhoneTitle2.setVisibility(View.VISIBLE);

                                businessPhoneInput2.setFocusableInTouchMode(true);
                                businessPhoneInput2.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(businessPhoneInput2);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_homePhone:
                                homePhoneInput.setVisibility(View.VISIBLE);
                                homePhoneTitle.setVisibility(View.VISIBLE);

                                homePhoneInput.setFocusableInTouchMode(true);
                                homePhoneInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(homePhoneInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_homePhone2:
                                homePhoneInput2.setVisibility(View.VISIBLE);
                                homePhoneTitle2.setVisibility(View.VISIBLE);

                                homePhoneInput2.setFocusableInTouchMode(true);
                                homePhoneInput2.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(homePhoneInput2);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_mobilePhone:
                                mobilePhoneInput.setVisibility(View.VISIBLE);
                                mobilePhoneTitle.setVisibility(View.VISIBLE);

                                mobilePhoneInput.setFocusableInTouchMode(true);
                                mobilePhoneInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(mobilePhoneInput);

                                    }
                                }, DELAY_TIME);

                                break;

                        }

                        return false;
                    }
                });
            }
        });

        plusWorkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jobTitle.setVisibility(View.VISIBLE);
                jobTitleInput.setVisibility(View.VISIBLE);
                departmentTitle.setVisibility(View.VISIBLE);
                departmentInput.setVisibility(View.VISIBLE);
                companyNameTitle.setVisibility(View.VISIBLE);
                companyNameInput.setVisibility(View.VISIBLE);
                officeLocationTitle.setVisibility(View.VISIBLE);
                officeLocationInput.setVisibility(View.VISIBLE);
                managerTitle.setVisibility(View.VISIBLE);
                managerInput.setVisibility(View.VISIBLE);
                assistantTitle.setVisibility(View.VISIBLE);
                assistantNameInput.setVisibility(View.VISIBLE);
                yomiCompanyTitle.setVisibility(View.VISIBLE);
                yomiCompanyInput.setVisibility(View.VISIBLE);

                jobTitleInput.setFocusableInTouchMode(true);
                jobTitleInput.requestFocus();
                autoFocus(jobTitleInput);

            }
        });

        plusImIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imSubTitle.setVisibility(View.VISIBLE);
                imInput.setVisibility(View.VISIBLE);

                imInput.setFocusableInTouchMode(true);
                imInput.requestFocus();
                autoFocus(imInput);
            }
        });

        plusNotesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personalNotesInput.setVisibility(View.VISIBLE);

                personalNotesInput.setFocusableInTouchMode(true);
                personalNotesInput.requestFocus();
                autoFocus(personalNotesInput);
            }
        });

        plusAddressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.inflate(R.menu.address_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_businessAddress:
                                businessAddressTitle.setVisibility(View.VISIBLE);
                                businessStreetNameTitle.setVisibility(View.VISIBLE);
                                businessPostalCodeTitle.setVisibility(View.VISIBLE);
                                businessStreetNameInput.setVisibility(View.VISIBLE);
                                businessPostalCodeInput.setVisibility(View.VISIBLE);
                                businessCityNameTitle.setVisibility(View.VISIBLE);
                                businessCityNameInput.setVisibility(View.VISIBLE);
                                businessStateTitle.setVisibility(View.VISIBLE);
                                businessStateNameInput.setVisibility(View.VISIBLE);
                                businessCountryTitle.setVisibility(View.VISIBLE);
                                businessCountryNameInput.setVisibility(View.VISIBLE);

                                businessStreetNameInput.setFocusableInTouchMode(true);
                                businessStreetNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(businessStreetNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_homeAddress:
                                homeStreetNameInput.setVisibility(View.VISIBLE);
                                homePostalCodeInput.setVisibility(View.VISIBLE);
                                homeCityNameInput.setVisibility(View.VISIBLE);
                                homeStateNameInput.setVisibility(View.VISIBLE);
                                homeCountryNameInput.setVisibility(View.VISIBLE);
                                homeAddressTitle.setVisibility(View.VISIBLE);
                                homeStreetNameTitle.setVisibility(View.VISIBLE);
                                homePostalCodeTitle.setVisibility(View.VISIBLE);
                                homeCityNameTitle.setVisibility(View.VISIBLE);
                                homeStateTitle.setVisibility(View.VISIBLE);
                                homeCountryTitle.setVisibility(View.VISIBLE);

                                homeStreetNameInput.setFocusableInTouchMode(true);
                                homeStreetNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(homeStreetNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_otherAddress:
                                otherStreetNameInput.setVisibility(View.VISIBLE);
                                otherPostalCodeInput.setVisibility(View.VISIBLE);
                                otherCityNameInput.setVisibility(View.VISIBLE);
                                otherStateNameInput.setVisibility(View.VISIBLE);
                                otherCountryNameInput.setVisibility(View.VISIBLE);
                                otherAddressTitle.setVisibility(View.VISIBLE);
                                otherStreetNameTitle.setVisibility(View.VISIBLE);
                                otherPostalCodeTitle.setVisibility(View.VISIBLE);
                                otherCityNameTitle.setVisibility(View.VISIBLE);
                                otherStateTitle.setVisibility(View.VISIBLE);
                                otherCountryTitle.setVisibility(View.VISIBLE);

                                otherStreetNameInput.setFocusableInTouchMode(true);
                                otherStreetNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(otherStreetNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                        }

                        return false;
                    }
                });
            }
        });

        plusOtherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.inflate(R.menu.other_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_nickName:
                                nickNameInput.setVisibility(View.VISIBLE);
                                nickNameTitle.setVisibility(View.VISIBLE);

                                nickNameInput.setFocusableInTouchMode(true);
                                nickNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(nickNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_spouseName:
                                spouseNameTitle.setVisibility(View.VISIBLE);
                                spouseNameInput.setVisibility(View.VISIBLE);

                                spouseNameInput.setFocusableInTouchMode(true);
                                spouseNameInput.requestFocus();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        autoFocus(spouseNameInput);

                                    }
                                }, DELAY_TIME);

                                break;

                            case R.id.action_birthday:
                                birthdayTitle.setVisibility(View.VISIBLE);
                                birthdayInput.setVisibility(View.VISIBLE);

                                // ZET CLICK EVENT OP DE DATE INPUT
                                birthdayInput.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // calender class's instance and get current date , month and year from calender
                                        final Calendar c = Calendar.getInstance();
                                        int mYear = c.get(Calendar.YEAR); // current year
                                        int mMonth = c.get(Calendar.MONTH); // current month
                                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                                        // date picker dialog
                                        datePickerDialog = new DatePickerDialog(EditContactActivity.this,
                                                new DatePickerDialog.OnDateSetListener() {

                                                    @Override
                                                    public void onDateSet(DatePicker view, int yearPicked,
                                                                          int monthOfYearPicked, int dayOfMonthPicked) {

                                                        dayOfMonth = dayOfMonthPicked;
                                                        month = monthOfYearPicked + 1;
                                                        year = yearPicked;

                                                        if(month <10) {
                                                            finalMonth = "0" + month;
                                                        } else {
                                                            finalMonth = String.valueOf(month);
                                                        }

                                                        if(dayOfMonth <10) {
                                                            finalDayOfMonth = "0" + dayOfMonth;
                                                        } else {
                                                            finalDayOfMonth = String.valueOf(dayOfMonth);
                                                        }


                                                        birthdayInput.setText(finalDayOfMonth + "-" + finalMonth + "-" + year);

                                                    }
                                                }, mYear, mMonth, mDay);
                                        datePickerDialog.show();

                                    }
                                });

                                break;

                        }

                        return false;
                    }
                });
            }
        });

        birthdayInput.setFocusable(false);

    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_navigation, menu);

        saveItem = menu.findItem(R.id.action_save);

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentContactDetails = new Intent(EditContactActivity.this, ContactsDetailsActivity.class);
                intentContactDetails.putExtra("AccessToken", accessToken);
                intentContactDetails.putExtra("userName", userName);
                intentContactDetails.putExtra("userEmail", userEmail);
                intentContactDetails.putExtra("contactId", contactId);
                intentContactDetails.putExtra("contact", contact);

                startActivity(intentContactDetails);

                EditContactActivity.this.finish();

                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                if(firstNameInput.getText().toString().isEmpty()|| lastNameInput.getText().toString().isEmpty() ){
                    if (!emailInput.getText().toString().isEmpty() && !emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        emailInput.setError("Invalid Email Address!");
                    }

                    if(!mobilePhoneInput.getText().toString().isEmpty() && !isValidMobile(mobilePhoneInput.getText().toString())){
                        mobilePhoneInput.setError("Invalid phone number!");
                    }

                    if(firstNameInput.getText().toString().isEmpty()){
                        firstNameInput.setError("Required field!");
                    }
                    if(lastNameInput.getText().toString().isEmpty()){
                        lastNameInput.setError("Required field!");
                    }

                    Toast.makeText(getApplicationContext(), "Required fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (!emailInput.getText().toString().isEmpty() && !emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        emailInput.setError("Invalid Email Address!");

                        if(!mobilePhoneInput.getText().toString().isEmpty() && !isValidMobile(mobilePhoneInput.getText().toString())){
                            mobilePhoneInput.setError("Invalid phone number!");
                        }

                    } else {

                        if(!mobilePhoneInput.getText().toString().isEmpty() && !isValidMobile(mobilePhoneInput.getText().toString())){
                            mobilePhoneInput.setError("Invalid phone number!");
                        }else {
                            try {
                                saveItem.setEnabled(false);

                                updateContact();

                                int DELAY_TIME=2000;

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        //this code will run after the delay time which is 2 seconds.
                                        Intent intentContactDetailsSaved = new Intent(EditContactActivity.this, ContactsDetailsActivity.class);
                                        intentContactDetailsSaved.putExtra("AccessToken", accessToken);
                                        intentContactDetailsSaved.putExtra("userName", userName);
                                        intentContactDetailsSaved.putExtra("userEmail", userEmail);
                                        intentContactDetailsSaved.putExtra("contactId", contactId);
                                        intentContactDetailsSaved.putExtra("contact", updatedContact);

                                        startActivity(intentContactDetailsSaved);

                                        EditContactActivity.this.finish();

                                    }
                                }, DELAY_TIME);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }

                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // PATCH REQUEST VOOR UPDATE CONTACTPERSOON
    private void updateContact() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        updatedContact = new Contact();

        if(businessPhones != null){
            businessPhones.clear();
        }

        if(homePhones != null){
            homePhones.clear();
        }

        if(imAddresses != null){
            imAddresses.clear();
        }

        if(emailList != null){
            emailList.clear();
        }

        updatedContact.setGivenName(firstNameInput.getText().toString());

        updatedContact.setSurname(lastNameInput.getText().toString());

        String displayname = firstNameInput.getText().toString() + " " + lastNameInput.getText().toString();
        updatedContact.setDisplayName(displayname);

        if (emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")) {

            List<EmailAddress> listEmails = new ArrayList<>();

            if(!displayAsInput.getText().toString().isEmpty()){
                EmailAddress contactEmail = new EmailAddress(emailInput.getText().toString(), displayAsInput.getText().toString());
                listEmails.add(contactEmail);
            } else {
                EmailAddress contactEmail = new EmailAddress(emailInput.getText().toString(), displayName);
                listEmails.add(contactEmail);
            }

            if(emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")){

                if(!displayAsInput2.getText().toString().isEmpty()){
                    EmailAddress contactEmail = new EmailAddress(emailInput2.getText().toString(), displayAsInput2.getText().toString());
                    listEmails.add(contactEmail);
                } else {
                    EmailAddress contactEmail = new EmailAddress(emailInput2.getText().toString(), displayName);
                    listEmails.add(contactEmail);
                }

            }

            if(emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")){

                if(!displayAsInput3.getText().toString().isEmpty()){
                    EmailAddress contactEmail = new EmailAddress(emailInput3.getText().toString(), displayAsInput3.getText().toString());
                    listEmails.add(contactEmail);
                } else {
                    EmailAddress contactEmail = new EmailAddress(emailInput3.getText().toString(), displayName);
                    listEmails.add(contactEmail);
                }

            }

            updatedContact.setEmailAddresses(listEmails);

        }

        if(!middleNameInput.getText().toString().isEmpty()){
            updatedContact.setMiddleName(middleNameInput.getText().toString());
        }

        if(!titleInput.getText().toString().isEmpty()){
            updatedContact.setTitle(titleInput.getText().toString());
        }

        if(!suffixInput.getText().toString().isEmpty()){
            updatedContact.setInitials(suffixInput.getText().toString());
        }

        if(!yomiFirstNameInput.getText().toString().isEmpty()){
            updatedContact.setYomiGivenName(yomiFirstNameInput.getText().toString());
        }

        if(!yomiLastNameInput.getText().toString().isEmpty()){
            updatedContact.setYomiSurname(yomiLastNameInput.getText().toString());
        }

        if(!mobilePhoneInput.getText().toString().isEmpty()){
            updatedContact.setMobilePhone(mobilePhoneInput.getText().toString());
        }

        if(!homePhoneInput.getText().toString().isEmpty()){

            homePhones.add(homePhoneInput.getText().toString());

            updatedContact.setHomePhones(homePhones);
        }

        if(!homePhoneInput2.getText().toString().isEmpty()){

            homePhones.add(homePhoneInput2.getText().toString());

            updatedContact.setHomePhones(homePhones);
        }

        if(!phoneInput.getText().toString().isEmpty()){

            businessPhones.add(phoneInput.getText().toString());

            updatedContact.setBusinessPhones(businessPhones);
        }

        if(!businessPhoneInput2.getText().toString().isEmpty()){

            businessPhones.add(businessPhoneInput2.getText().toString());

            updatedContact.setBusinessPhones(businessPhones);
        }

        if(!jobTitleInput.getText().toString().isEmpty()){
            updatedContact.setJobTitle(jobTitleInput.getText().toString());
        }

        if(!departmentInput.getText().toString().isEmpty()){
            updatedContact.setDepartment(departmentInput.getText().toString());
        }

        if(!companyNameInput.getText().toString().isEmpty()){
            updatedContact.setCompanyName(companyNameInput.getText().toString());
        }

        if(!yomiCompanyInput.getText().toString().isEmpty()){
            updatedContact.setYomiCompanyName(yomiCompanyInput.getText().toString());
        }

        if(!officeLocationInput.getText().toString().isEmpty()){
            updatedContact.setOfficeLocation(officeLocationInput.getText().toString());
        }

        if(!managerInput.getText().toString().isEmpty()){
            updatedContact.setManager(managerInput.getText().toString());
        }

        if(!assistantNameInput.getText().toString().isEmpty()){
            updatedContact.setAssistantName(assistantNameInput.getText().toString());
        }

        if(!businessStreetNameInput.getText().toString().isEmpty()){
            PhysicalAddress contactBusinessPhysicalAddress = new PhysicalAddress(businessStreetNameInput.getText().toString(), businessCityNameInput.getText().toString(), businessStateNameInput.getText().toString(), businessCountryNameInput.getText().toString(), businessPostalCodeInput.getText().toString());
            updatedContact.setBusinessAddress(contactBusinessPhysicalAddress);
        }

        if(!homeStreetNameInput.getText().toString().isEmpty()){
            PhysicalAddress contactHomePhysicalAddress = new PhysicalAddress(homeStreetNameInput.getText().toString(), homeCityNameInput.getText().toString(), homeStateNameInput.getText().toString(), homeCountryNameInput.getText().toString(), homePostalCodeInput.getText().toString());
            updatedContact.setHomeAddress(contactHomePhysicalAddress);
        }

        if(!otherStreetNameInput.getText().toString().isEmpty()){
            PhysicalAddress contactOtherPhysicalAddress = new PhysicalAddress(otherStreetNameInput.getText().toString(), otherCityNameInput.getText().toString(), otherStateNameInput.getText().toString(), otherCountryNameInput.getText().toString(), otherPostalCodeInput.getText().toString());
            updatedContact.setOtherAddress(contactOtherPhysicalAddress);
        }

        if(!nickNameInput.getText().toString().isEmpty()){
            updatedContact.setNickName(nickNameInput.getText().toString());
        }

        if(!spouseNameInput.getText().toString().isEmpty()){
            updatedContact.setSpouseName(spouseNameInput.getText().toString());
        }

        if(!personalNotesInput.getText().toString().isEmpty()){
            updatedContact.setPersonalNotes(personalNotesInput.getText().toString());
        }

        if(!imInput.getText().toString().isEmpty()){
            List<String> imAddresses = new ArrayList<>();

            imAddresses.add(imInput.getText().toString());

            updatedContact.setImAddresses(imAddresses);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String birthdayDate = sdf.format(cal.getTime());

        if(!birthdayInput.getText().toString().isEmpty()){
            updatedContact.setBirthday(birthdayDate);
        }

        String postAddress = URL_POSTADRESS + contactId;

        System.out.println("test contact: " + new Gson().toJson(updatedContact));

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, postAddress, new JSONObject(new Gson().toJson(updatedContact)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Contact updated!", Toast.LENGTH_SHORT).show();
                        System.out.println(response.toString());
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    private String buildJsonEditContact() {
        JsonObjectBuilder factory = Json.createObjectBuilder()

                .add("isRead", true);

        return factory.build().toString();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void setEditTextOnFocusListener(EditText et){

        et.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){

                    view.setBackgroundResource( R.drawable.edit_text_style_focused);
                }
                else{
                    view.setBackgroundResource( R.drawable.edit_text_style);
                }
            }
        });

    }

    public void autoFocus(EditText editText){
        final InputMethodManager inputMethodManager = (InputMethodManager) EditContactActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentContactDetails = new Intent(EditContactActivity.this, ContactsDetailsActivity.class);
        intentContactDetails.putExtra("AccessToken", accessToken);
        intentContactDetails.putExtra("userName", userName);
        intentContactDetails.putExtra("userEmail", userEmail);
        intentContactDetails.putExtra("contactId", contactId);
        intentContactDetails.putExtra("contact", contact);

        startActivity(intentContactDetails);

        EditContactActivity.this.finish();
    }

    public void makeExtraInvisible(){

        middleNameInput.setVisibility(View.GONE);
        titleInput.setVisibility(View.GONE);
        suffixInput.setVisibility(View.GONE);
        yomiFirstNameInput.setVisibility(View.GONE);
        yomiLastNameInput.setVisibility(View.GONE);
        middleNameTitle.setVisibility(View.GONE);
        titleTitle.setVisibility(View.GONE);
        suffixTitle.setVisibility(View.GONE);
        yomiFirstNameTitle.setVisibility(View.GONE);
        yomiLastNameTitle.setVisibility(View.GONE);
        emailInput2.setVisibility(View.GONE);
        displayAsInput2.setVisibility(View.GONE);
        emailInput3.setVisibility(View.GONE);
        displayAsInput3.setVisibility(View.GONE);
        displayAsTitle2.setVisibility(View.GONE);
        displayAsTitle3.setVisibility(View.GONE);
        emailSubTitle2.setVisibility(View.GONE);
        emailSubTitle3.setVisibility(View.GONE);
        businessPhoneInput2.setVisibility(View.GONE);
        homePhoneInput.setVisibility(View.GONE);
        homePhoneInput2.setVisibility(View.GONE);
        mobilePhoneInput.setVisibility(View.GONE);
        businessPhoneTitle2.setVisibility(View.GONE);
        homePhoneTitle.setVisibility(View.GONE);
        homePhoneTitle2.setVisibility(View.GONE);
        mobilePhoneTitle.setVisibility(View.GONE);
        jobTitle.setVisibility(View.GONE);
        jobTitleInput.setVisibility(View.GONE);
        departmentTitle.setVisibility(View.GONE);
        departmentInput.setVisibility(View.GONE);
        companyNameTitle.setVisibility(View.GONE);
        companyNameInput.setVisibility(View.GONE);
        officeLocationTitle.setVisibility(View.GONE);
        officeLocationInput.setVisibility(View.GONE);
        managerTitle.setVisibility(View.GONE);
        managerInput.setVisibility(View.GONE);
        assistantTitle.setVisibility(View.GONE);
        assistantNameInput.setVisibility(View.GONE);
        yomiCompanyTitle.setVisibility(View.GONE);
        yomiCompanyInput.setVisibility(View.GONE);
        personalNotesInput.setVisibility(View.GONE);
        imInput.setVisibility(View.GONE);
        imSubTitle.setVisibility(View.GONE);
        businessAddressTitle.setVisibility(View.GONE);
        businessStreetNameTitle.setVisibility(View.GONE);
        businessPostalCodeTitle.setVisibility(View.GONE);
        businessStreetNameInput.setVisibility(View.GONE);
        businessPostalCodeInput.setVisibility(View.GONE);
        businessCityNameTitle.setVisibility(View.GONE);
        businessCityNameInput.setVisibility(View.GONE);
        businessStateTitle.setVisibility(View.GONE);
        businessStateNameInput.setVisibility(View.GONE);
        businessCountryTitle.setVisibility(View.GONE);
        businessCountryNameInput.setVisibility(View.GONE);
        homeStreetNameInput.setVisibility(View.GONE);
        homePostalCodeInput.setVisibility(View.GONE);
        homeCityNameInput.setVisibility(View.GONE);
        homeStateNameInput.setVisibility(View.GONE);
        homeCountryNameInput.setVisibility(View.GONE);
        homeAddressTitle.setVisibility(View.GONE);
        homeStreetNameTitle.setVisibility(View.GONE);
        homePostalCodeTitle.setVisibility(View.GONE);
        homeCityNameTitle.setVisibility(View.GONE);
        homeStateTitle.setVisibility(View.GONE);
        homeCountryTitle.setVisibility(View.GONE);
        otherStreetNameInput.setVisibility(View.GONE);
        otherPostalCodeInput.setVisibility(View.GONE);
        otherCityNameInput.setVisibility(View.GONE);
        otherStateNameInput.setVisibility(View.GONE);
        otherCountryNameInput.setVisibility(View.GONE);
        otherAddressTitle.setVisibility(View.GONE);
        otherStreetNameTitle.setVisibility(View.GONE);
        otherPostalCodeTitle.setVisibility(View.GONE);
        otherCityNameTitle.setVisibility(View.GONE);
        otherStateTitle.setVisibility(View.GONE);
        otherCountryTitle.setVisibility(View.GONE);
        nickNameInput.setVisibility(View.GONE);
        nickNameTitle.setVisibility(View.GONE);
        spouseNameTitle.setVisibility(View.GONE);
        spouseNameInput.setVisibility(View.GONE);
        birthdayTitle.setVisibility(View.GONE);
        birthdayInput.setVisibility(View.GONE);

    }

    public void fillInputs(){

        firstNameInput.setText(firstName);
        lastNameInput.setText(lastName);

        if(contactMiddleName != null){
            middleNameInput.setVisibility(View.VISIBLE);
            middleNameTitle.setVisibility(View.VISIBLE);
            middleNameInput.setText(contactMiddleName);
        }

        if(contactTitle != null){
            titleInput.setVisibility(View.VISIBLE);
            titleTitle.setVisibility(View.VISIBLE);
            titleInput.setText(contactTitle);
        }

        if(contactSuffix != null){
            suffixTitle.setVisibility(View.VISIBLE);
            suffixInput.setVisibility(View.VISIBLE);
            suffixInput.setText(contactSuffix);
        }

        if(contactYomiFirstName != null){
            yomiFirstNameInput.setVisibility(View.VISIBLE);
            yomiFirstNameTitle.setVisibility(View.VISIBLE);
            yomiFirstNameInput.setText(contactYomiFirstName);
        }

        if(contactYomiLastName != null){
            yomiLastNameInput.setVisibility(View.VISIBLE);
            yomiLastNameTitle.setVisibility(View.VISIBLE);
            yomiLastNameInput.setText(contactYomiLastName);
        }

        if(spouse != null){
            spouseNameTitle.setVisibility(View.VISIBLE);
            spouseNameInput.setVisibility(View.VISIBLE);
            spouseNameInput.setText(spouse);
        }

        if(nickname != null){
            nickNameTitle.setVisibility(View.VISIBLE);
            nickNameInput.setVisibility(View.VISIBLE);
            nickNameInput.setText(nickname);
        }

        if(notes != null){
            personalNotesInput.setVisibility(View.VISIBLE);
            personalNotesInput.setText(notes);
        }


        if(imAddresses != null){
            if(!imAddresses.isEmpty()){
                imInput.setVisibility(View.VISIBLE);
                imSubTitle.setVisibility(View.VISIBLE);
                imInput.setText(imAddresses.get(0));
            }
        }

        if(phoneNumber != null){
            mobilePhoneInput.setVisibility(View.VISIBLE);
            mobilePhoneTitle.setVisibility(View.VISIBLE);
            mobilePhoneInput.setText(phoneNumber);
        }

        if(!emailList.isEmpty()){
            if(emailList.size() == 1){
                emailInput.setText(emailList.get(0).getAddress());
                displayAsInput.setText(emailList.get(0).getName());
            } else if (emailList.size() == 2){
                emailInput2.setVisibility(View.VISIBLE);
                emailSubTitle2.setVisibility(View.VISIBLE);
                displayAsInput2.setVisibility(View.VISIBLE);
                displayAsTitle2.setVisibility(View.VISIBLE);
                emailInput.setText(emailList.get(0).getAddress());
                emailInput2.setText(emailList.get(1).getAddress());
                displayAsInput.setText(emailList.get(0).getName());
                displayAsInput2.setText(emailList.get(1).getName());
            } else {
                emailInput2.setVisibility(View.VISIBLE);
                emailSubTitle2.setVisibility(View.VISIBLE);
                emailSubTitle3.setVisibility(View.VISIBLE);
                emailInput3.setVisibility(View.VISIBLE);
                displayAsInput2.setVisibility(View.VISIBLE);
                displayAsTitle2.setVisibility(View.VISIBLE);
                displayAsTitle3.setVisibility(View.VISIBLE);
                displayAsInput3.setVisibility(View.VISIBLE);
                emailInput.setText(emailList.get(0).getAddress());
                emailInput2.setText(emailList.get(1).getAddress());
                emailInput3.setText(emailList.get(2).getAddress());
                displayAsInput.setText(emailList.get(0).getName());
                displayAsInput2.setText(emailList.get(1).getName());
                displayAsInput3.setText(emailList.get(2).getName());
            }
        }

        if(job != null || department != null || company != null || office != null || manager != null || assistant != null || contactYomiCompanyName != null){
            jobTitleInput.setVisibility(View.VISIBLE);
            jobTitle.setVisibility(View.VISIBLE);
            departmentInput.setVisibility(View.VISIBLE);
            departmentTitle.setVisibility(View.VISIBLE);
            companyNameInput.setVisibility(View.VISIBLE);
            companyNameTitle.setVisibility(View.VISIBLE);
            officeLocationInput.setVisibility(View.VISIBLE);
            officeLocationTitle.setVisibility(View.VISIBLE);
            managerInput.setVisibility(View.VISIBLE);
            managerTitle.setVisibility(View.VISIBLE);
            assistantNameInput.setVisibility(View.VISIBLE);
            assistantTitle.setVisibility(View.VISIBLE);
            yomiCompanyInput.setVisibility(View.VISIBLE);
            yomiCompanyTitle.setVisibility(View.VISIBLE);
        }

        if(job != null){
            jobTitleInput.setText(job);
        }

        if(department != null){
            departmentInput.setText(department);
        }

        if(company != null){
            companyNameInput.setText(company);
        }

        if(office != null){
            officeLocationInput.setText(office);
        }

        if(manager != null){
            managerInput.setText(manager);
        }

        if(assistant != null){
            assistantNameInput.setText(assistant);
        }

        if(contactYomiCompanyName != null){
            yomiCompanyInput.setText(contactYomiCompanyName);
        }

        if(businessPhones != null){
            if(!businessPhones.isEmpty()){
                if(businessPhones.size() == 1){
                    phoneInput.setText(businessPhones.get(0));
                } else {
                    businessPhoneInput2.setVisibility(View.VISIBLE);
                    businessPhoneTitle2.setVisibility(View.VISIBLE);
                    phoneInput.setText(businessPhones.get(0));
                    businessPhoneInput2.setText(businessPhones.get(1));
                }
            }
        }

        if(homePhones != null){
            if(!homePhones.isEmpty()){
                if(homePhones.size() == 1){
                    homePhoneInput.setVisibility(View.VISIBLE);
                    homePhoneTitle.setVisibility(View.VISIBLE);
                    homePhoneInput.setText(homePhones.get(0));
                } else {
                    homePhoneInput.setVisibility(View.VISIBLE);
                    homePhoneTitle.setVisibility(View.VISIBLE);
                    homePhoneInput2.setVisibility(View.VISIBLE);
                    homePhoneTitle2.setVisibility(View.VISIBLE);
                    homePhoneInput.setText(homePhones.get(0));
                    homePhoneInput2.setText(homePhones.get(1));
                }
            }
        }

        if(businessStreet != null || businessCity != null || businessState != null || businessPostalCode != null || businessCountry != null){
            businessAddressTitle.setVisibility(View.VISIBLE);
            businessStreetNameInput.setVisibility(View.VISIBLE);
            businessStreetNameTitle.setVisibility(View.VISIBLE);
            businessCityNameInput.setVisibility(View.VISIBLE);
            businessCityNameTitle.setVisibility(View.VISIBLE);
            businessStateNameInput.setVisibility(View.VISIBLE);
            businessStateTitle.setVisibility(View.VISIBLE);
            businessPostalCodeInput.setVisibility(View.VISIBLE);
            businessPostalCodeTitle.setVisibility(View.VISIBLE);
            businessCityNameInput.setVisibility(View.VISIBLE);
            businessCityNameTitle.setVisibility(View.VISIBLE);
        }

        if(businessStreet != null){
            businessStreetNameInput.setText(businessStreet);
        }

        if(businessCity != null){
            businessCityNameInput.setText(businessCity);
        }

        if(businessState != null){
            businessStateNameInput.setText(businessState);
        }

        if(businessPostalCode != null){
            businessPostalCodeInput.setText(businessPostalCode);
        }

        if(businessCountry != null){
            businessCountryNameInput.setText(businessCountry);
        }

        if(homeStreet != null || homeCity != null || homeState != null || homePostalCode != null || homeCountry != null){
            homeAddressTitle.setVisibility(View.VISIBLE);
            homeStreetNameInput.setVisibility(View.VISIBLE);
            homeStreetNameTitle.setVisibility(View.VISIBLE);
            homeCityNameInput.setVisibility(View.VISIBLE);
            homeCityNameTitle.setVisibility(View.VISIBLE);
            homeStateNameInput.setVisibility(View.VISIBLE);
            homeStateTitle.setVisibility(View.VISIBLE);
            homePostalCodeInput.setVisibility(View.VISIBLE);
            homePostalCodeTitle.setVisibility(View.VISIBLE);
            homeCityNameInput.setVisibility(View.VISIBLE);
            homeCityNameTitle.setVisibility(View.VISIBLE);
        }

        if(homeStreet != null){
            homeStreetNameInput.setText(homeStreet);
        }

        if(homeCity != null){
            homeCityNameInput.setText(homeCity);
        }

        if(homeState != null){
            homeStateNameInput.setText(homeState);
        }

        if(homePostalCode != null){
            homePostalCodeInput.setText(homePostalCode);
        }

        if(homeCountry != null){
            homeCountryNameInput.setText(homeCountry);
        }

        if(otherStreet != null || otherCity != null || otherState != null || otherPostalCode != null || otherCountry != null){
            otherAddressTitle.setVisibility(View.VISIBLE);
            otherStreetNameInput.setVisibility(View.VISIBLE);
            otherStreetNameTitle.setVisibility(View.VISIBLE);
            otherCityNameInput.setVisibility(View.VISIBLE);
            otherCityNameTitle.setVisibility(View.VISIBLE);
            otherStateNameInput.setVisibility(View.VISIBLE);
            otherStateTitle.setVisibility(View.VISIBLE);
            otherPostalCodeInput.setVisibility(View.VISIBLE);
            otherPostalCodeTitle.setVisibility(View.VISIBLE);
            otherCityNameInput.setVisibility(View.VISIBLE);
            otherCityNameTitle.setVisibility(View.VISIBLE);
        }

        if(otherStreet != null){
            otherStreetNameInput.setText(otherStreet);
        }

        if(otherCity != null){
            otherCityNameInput.setText(otherCity);
        }

        if(otherState != null){
            otherStateNameInput.setText(otherState);
        }

        if(otherPostalCode != null){
            otherPostalCodeInput.setText(otherPostalCode);
        }

        if(otherCountry != null){
            otherCountryNameInput.setText(otherCountry);
        }

        if(contactBirthday != null){
            birthdayInput.setVisibility(View.VISIBLE);
            birthdayTitle.setVisibility(View.VISIBLE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
            Date d = null;
            try {
                d = sdf.parse(contactBirthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            birthdayInput.setText(output.format(d));

            // ZET CLICK EVENT OP DE DATE INPUT
            birthdayInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(EditContactActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int yearPicked,
                                                      int monthOfYearPicked, int dayOfMonthPicked) {

                                    dayOfMonth = dayOfMonthPicked;
                                    month = monthOfYearPicked + 1;
                                    year = yearPicked;

                                    if(month <10) {
                                        finalMonth = "0" + month;
                                    } else {
                                        finalMonth = String.valueOf(month);
                                    }

                                    if(dayOfMonth <10) {
                                        finalDayOfMonth = "0" + dayOfMonth;
                                    } else {
                                        finalDayOfMonth = String.valueOf(dayOfMonth);
                                    }


                                    birthdayInput.setText(finalDayOfMonth + "-" + finalMonth + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            });

        }

    }

}
