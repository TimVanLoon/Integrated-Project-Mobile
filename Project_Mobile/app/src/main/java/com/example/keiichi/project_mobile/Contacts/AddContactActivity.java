package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.keiichi.project_mobile.Calendar.AddEventActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.Phone;
import com.example.keiichi.project_mobile.DAL.POJOs.PhysicalAddress;
import com.example.keiichi.project_mobile.Mail.DisplayMailActivity;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContactActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/contacts";
    final private int DELAY_TIME = 100;
    private List<String> mobilePhones = new ArrayList<>();
    private List<String> homePhones = new ArrayList<>();
    private List<String> businessPhones = new ArrayList<>();
    private Toolbar myToolbar;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneInput;
    private EditText jobInput;
    private EditText department;
    private EditText companyNameInput;
    private EditText officeLocationInput;
    private EditText managerInput;
    private EditText assistantNameInput;

    private EditText businessStreetNameInput;
    private EditText businessPostalCodeInput;
    private EditText businessCityNameInput;
    private EditText businessStateNameInput;
    private EditText businessCountryNameInput;

    private EditText personalNotes;
    private EditText nickName;
    private EditText spouseName;
    private EditText middleNameInput;
    private EditText titleInput;
    private EditText suffixInput;
    private EditText yomiFirstNameInput;
    private EditText yomiLastNameInput;
    private EditText emailInput;
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
    private EditText input;
    private TextView businessPhoneTitle2;
    private TextView homePhoneTitle;
    private TextView homePhoneTitle2;
    private TextView mobilePhoneTitle;
    private TextView middleNameTitle;
    private TextView titleTitle;
    private TextView suffixTitle;
    private TextView yomiFirstNameTitle;
    private TextView yomiLastNameTitle;
    private TextView displayAsTitle;
    private TextView emailSubTitle2;
    private TextView displayAsTitle2;
    private TextView emailSubTitle3;
    private TextView displayAsTitle3;
    private TextView emailSubTitle;
    private TextView jobTitle;
    private TextView departmentTitle;
    private TextView companyNameTitle;
    private TextView officeLocationTitle;
    private TextView managerTitle;
    private TextView assistantTitle;
    private TextView yomiCompanyTitle;
    private TextView imTitle;
    private TextView imSubTitle;

    private TextView businessAddressTitle;
    private TextView businessStreetNameTitle;
    private TextView businessPostalCodeTitle;
    private TextView businessCityNameTitle;
    private TextView businessStateTitle;
    private TextView businessCountryTitle;

    private String userName;
    private String userEmail;
    private String attendeeName;
    private String attendeeMail;
    private String accessToken;
    private boolean isValidEmail;
    private boolean myItemShouldBeEnabled = true;
    private MenuItem saveItem;
    private ImageView plusNameIcon;
    private ImageView plusEmailIcon;
    private ImageView plusPhoneIcon;
    private ImageView plusWorkIcon;
    private ImageView plusNotesIcon;
    private ImageView plusImIcon;
    private ImageView plusAddressIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        jobInput = (EditText) findViewById(R.id.jobInput);
        department = (EditText) findViewById(R.id.department);
        companyNameInput = (EditText) findViewById(R.id.companyName);
        officeLocationInput = (EditText) findViewById(R.id.officeLocation);
        managerInput = (EditText) findViewById(R.id.manager);
        assistantNameInput = (EditText) findViewById(R.id.assistantName);

        businessStreetNameInput = (EditText) findViewById(R.id.businessStreetNameInput);
        businessPostalCodeInput = (EditText) findViewById(R.id.businessPostalCodeInput);

        businessPostalCodeInput = (EditText) findViewById(R.id.businessPostalCodeInput);
        businessCityNameInput = (EditText) findViewById(R.id.businessCityNameInput);
        businessStateNameInput = (EditText) findViewById(R.id.businessStateNameInput);
        businessCountryNameInput = (EditText) findViewById(R.id.businessCountryNameInput);
        personalNotes = (EditText) findViewById(R.id.personalNotes);
        nickName = (EditText) findViewById(R.id.nickName);
        spouseName = (EditText) findViewById(R.id.spouseName);
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
        mobilePhoneInput = (EditText) findViewById(R.id.mobilePhoneInput);
        yomiCompanyInput = (EditText) findViewById(R.id.yomiCompanyInput);
        imInput = (EditText) findViewById(R.id.imInput);
        middleNameTitle = (TextView) findViewById(R.id.middleNameTitle);
        titleTitle = (TextView) findViewById(R.id.titleTitle);
        suffixTitle = (TextView) findViewById(R.id.suffixTitle);
        yomiFirstNameTitle = (TextView) findViewById(R.id.yomiFirstNameTitle);
        yomiLastNameTitle = (TextView) findViewById(R.id.yomiLastNameTitle);
        displayAsTitle = (TextView) findViewById(R.id.displayAsTitle);
        emailSubTitle2 = (TextView) findViewById(R.id.emailSubTitle2);
        displayAsTitle2 = (TextView) findViewById(R.id.displayAsTitle2);
        emailSubTitle3 = (TextView) findViewById(R.id.emailSubTitle3);
        displayAsTitle3 = (TextView) findViewById(R.id.displayAsTitle3);
        emailSubTitle = (TextView) findViewById(R.id.emailSubTitle);
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
        imTitle = (TextView) findViewById(R.id.imTitle);
        imSubTitle = (TextView) findViewById(R.id.imSubTitle);

        businessAddressTitle = (TextView) findViewById(R.id.businessAddressTitle);
        businessStreetNameTitle = (TextView) findViewById(R.id.businessStreetNameTitle);
        businessPostalCodeTitle = (TextView) findViewById(R.id.businessPostalCodeTitle);
        businessCityNameTitle = (TextView) findViewById(R.id.businessCityNameTitle);
        businessStateTitle = (TextView) findViewById(R.id.businessStateTitle);
        businessCountryTitle = (TextView) findViewById(R.id.businessCountryTitle);

        plusNameIcon = (ImageView) findViewById(R.id.plusNameIcon);
        plusEmailIcon = (ImageView) findViewById(R.id.plusEmailIcon);
        plusPhoneIcon = (ImageView) findViewById(R.id.plusPhoneIcon);
        plusWorkIcon = (ImageView) findViewById(R.id.plusWorkIcon);
        plusNotesIcon = (ImageView) findViewById(R.id.plusNotesIcon);
        plusImIcon = (ImageView) findViewById(R.id.plusImIcon);
        plusAddressIcon = (ImageView) findViewById(R.id.plusAddressIcon);

        makeExtraInvisible();

        setEditTextOnFocusListener(firstNameInput);
        setEditTextOnFocusListener(lastNameInput);
        setEditTextOnFocusListener(emailInput);
        setEditTextOnFocusListener(phoneInput);
        setEditTextOnFocusListener(jobInput);
        setEditTextOnFocusListener(department);
        setEditTextOnFocusListener(companyNameInput);
        setEditTextOnFocusListener(officeLocationInput);
        setEditTextOnFocusListener(managerInput);
        setEditTextOnFocusListener(assistantNameInput);
        setEditTextOnFocusListener(businessStreetNameInput);
        setEditTextOnFocusListener(businessPostalCodeInput);
        setEditTextOnFocusListener(businessCityNameInput);
        setEditTextOnFocusListener(businessStateNameInput);
        setEditTextOnFocusListener(businessCountryNameInput);
        setEditTextOnFocusListener(personalNotes);
        setEditTextOnFocusListener(nickName);
        setEditTextOnFocusListener(spouseName);
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

        plusNameIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

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

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

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

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

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
                jobInput.setVisibility(View.VISIBLE);
                departmentTitle.setVisibility(View.VISIBLE);
                department.setVisibility(View.VISIBLE);
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

                jobInput.setFocusableInTouchMode(true);
                jobInput.requestFocus();
                autoFocus(jobInput);

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

                personalNotes.setVisibility(View.VISIBLE);

                personalNotes.setFocusableInTouchMode(true);
                personalNotes.requestFocus();
                autoFocus(personalNotes);
            }
        });

        plusAddressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

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

                            case R.id.action_otherAddress:
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
                Intent intentContacts = new Intent(AddContactActivity.this, ContactsActivity.class);
                intentContacts.putExtra("AccessToken", accessToken);
                intentContacts.putExtra("userName", userName);
                intentContacts.putExtra("userEmail", userEmail);

                startActivity(intentContacts);

                AddContactActivity.this.finish();

                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:

                    if(firstNameInput.getText().toString().isEmpty()|| lastNameInput.getText().toString().isEmpty() ){
                        if (!emailInput.getText().toString().isEmpty() && !emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                            emailInput.setError("Invalid Email Address!");
                        }

                        if (!emailInput2.getText().toString().isEmpty() && (!emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && !emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+"))) {
                            emailInput2.setError("Invalid Email Address!");
                        }

                        if (!emailInput3.getText().toString().isEmpty() && (!emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && !emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+"))) {
                            emailInput3.setError("Invalid Email Address!");
                        }

                        if(!phoneInput.getText().toString().isEmpty() && !isValidMobile(phoneInput.getText().toString())){
                            phoneInput.setError("Invalid phone number!");
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

                            if (!emailInput2.getText().toString().isEmpty() && (!emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && !emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+"))) {
                                emailInput2.setError("Invalid Email Address!");
                            }

                            if (!emailInput3.getText().toString().isEmpty() && (!emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && !emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+"))) {
                                emailInput3.setError("Invalid Email Address!");
                            }

                            if (!emailInput.getText().toString().isEmpty() && (!emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && !emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+"))) {
                                emailInput.setError("Invalid Email Address!");

                                if (!phoneInput.getText().toString().isEmpty() && !isValidMobile(phoneInput.getText().toString())) {
                                    phoneInput.setError("Invalid phone number!");
                                }

                            } else {

                                if (!phoneInput.getText().toString().isEmpty() && !isValidMobile(phoneInput.getText().toString())) {
                                    phoneInput.setError("Invalid phone number!");

                                } else {
                                    try {
                                        saveItem.setEnabled(false);

                                        saveContact();

                                        int DELAY_TIME = 2000;

                                        //start your animation
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                //this code will run after the delay time which is 2 seconds.
                                                Intent intentContacts = new Intent(AddContactActivity.this, ContactsActivity.class);
                                                intentContacts.putExtra("AccessToken", accessToken);
                                                intentContacts.putExtra("userName", userName);
                                                intentContacts.putExtra("userEmail", userEmail);

                                                startActivity(intentContacts);

                                                AddContactActivity.this.finish();

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


    // POST REQUEST VOOR NIEWE CONTACTSPERSOON
    private void saveContact() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        Contact contact = new Contact();
        contact.setGivenName(firstNameInput.getText().toString());
        contact.setSurname(lastNameInput.getText().toString());

        String displayName = firstNameInput.getText().toString() + " " + lastNameInput.getText().toString();
        contact.setDisplayName(displayName);

        if(!middleNameInput.getText().toString().isEmpty()){
            contact.setMiddleName(middleNameInput.getText().toString());
        }

        if(!titleInput.getText().toString().isEmpty()){
            contact.setTitle(titleInput.getText().toString());
        }

        if(!suffixInput.getText().toString().isEmpty()){
            contact.setInitials(suffixInput.getText().toString());
        }

        if(!yomiFirstNameInput.getText().toString().isEmpty()){
            contact.setYomiGivenName(yomiFirstNameInput.getText().toString());
        }

        if(!yomiLastNameInput.getText().toString().isEmpty()){
            contact.setYomiSurname(yomiLastNameInput.getText().toString());
        }

        if (emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")) {

            List<EmailAddress> listEmails = new ArrayList<>();

            if(!displayAsInput.getText().toString().isEmpty()){
                EmailAddress contactEmail = new EmailAddress(emailInput.getText().toString(), displayAsInput.getText().toString());
                listEmails.add(contactEmail);
            } else {
                EmailAddress contactEmail = new EmailAddress(emailInput.getText().toString(), "");
                listEmails.add(contactEmail);
            }

            if(emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")){

                if(!displayAsInput2.getText().toString().isEmpty()){
                    EmailAddress contactEmail = new EmailAddress(emailInput2.getText().toString(), displayAsInput2.getText().toString());
                    listEmails.add(contactEmail);
                } else {
                    EmailAddress contactEmail = new EmailAddress(emailInput2.getText().toString(), "");
                    listEmails.add(contactEmail);
                }

            }

            if(emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ) || emailInput3.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")){

                if(!displayAsInput3.getText().toString().isEmpty()){
                    EmailAddress contactEmail = new EmailAddress(emailInput3.getText().toString(), displayAsInput3.getText().toString());
                    listEmails.add(contactEmail);
                } else {
                    EmailAddress contactEmail = new EmailAddress(emailInput3.getText().toString(), "");
                    listEmails.add(contactEmail);
                }

            }

            contact.setEmailAddresses(listEmails);

        }

        if(!mobilePhoneInput.getText().toString().isEmpty()){
            contact.setMobilePhone(mobilePhoneInput.getText().toString());
        }

        if(!phoneInput.getText().toString().isEmpty()){

            businessPhones.add(phoneInput.getText().toString());

            contact.setBusinessPhones(businessPhones);
        }

        if(!businessPhoneInput2.getText().toString().isEmpty()){

            businessPhones.add(businessPhoneInput2.getText().toString());

            contact.setBusinessPhones(businessPhones);
        }

        if(!homePhoneInput.getText().toString().isEmpty()){

            homePhones.add(homePhoneInput.getText().toString());

            contact.setHomePhones(homePhones);
        }

        if(!homePhoneInput2.getText().toString().isEmpty()){

            homePhones.add(homePhoneInput2.getText().toString());

            contact.setHomePhones(homePhones);
        }

        if(!jobInput.getText().toString().isEmpty()){
            contact.setJobTitle(jobInput.getText().toString());
        }

        if(!department.getText().toString().isEmpty()){
            contact.setDepartment(department.getText().toString());
        }

        if(!companyNameInput.getText().toString().isEmpty()){
            contact.setCompanyName(companyNameInput.getText().toString());
        }

        if(!officeLocationInput.getText().toString().isEmpty()){
            contact.setOfficeLocation(officeLocationInput.getText().toString());
        }

        if(!managerInput.getText().toString().isEmpty()){
            contact.setManager(managerInput.getText().toString());
        }

        if(!assistantNameInput.getText().toString().isEmpty()){
            contact.setAssistantName(assistantNameInput.getText().toString());
        }

        if(!yomiCompanyInput.getText().toString().isEmpty()){
            contact.setYomiCompanyName(yomiCompanyInput.getText().toString());
        }

        if(!businessStreetNameInput.getText().toString().isEmpty()){
            PhysicalAddress contactBusinessPhysicalAddress = new PhysicalAddress(businessStreetNameInput.getText().toString(), businessCityNameInput.getText().toString(), businessStateNameInput.getText().toString(), businessCountryNameInput.getText().toString(), businessPostalCodeInput.getText().toString());
            contact.setBusinessAddress(contactBusinessPhysicalAddress);
        }

        if(!nickName.getText().toString().isEmpty()){
            contact.setNickName(nickName.getText().toString());
        }

        if(!spouseName.getText().toString().isEmpty()){
            contact.setSpouseName(spouseName.getText().toString());
        }

        if(!imInput.getText().toString().isEmpty()){
            List<String> imAddresses = new ArrayList<>();

            imAddresses.add(imInput.getText().toString());

            contact.setImAddresses(imAddresses);
        }

        if(!personalNotes.getText().toString().isEmpty()){
            contact.setPersonalNotes(personalNotes.getText().toString());
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_POSTADRESS,new JSONObject(new Gson().toJson(contact)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Contact saved!", Toast.LENGTH_SHORT).show();
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

                return headers;
            }

        };

        queue.add(objectRequest);

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
        final InputMethodManager inputMethodManager = (InputMethodManager) AddContactActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(AddContactActivity.this, ContactsActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        AddContactActivity.this.finish();
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
        jobInput.setVisibility(View.GONE);
        departmentTitle.setVisibility(View.GONE);
        department.setVisibility(View.GONE);
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
        personalNotes.setVisibility(View.GONE);
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

    }

}
