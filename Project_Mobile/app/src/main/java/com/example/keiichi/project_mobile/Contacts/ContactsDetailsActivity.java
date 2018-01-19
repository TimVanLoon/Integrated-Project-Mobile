package com.example.keiichi.project_mobile.Contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Calendar.AddEventActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.Mail.SendMailActivity;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ContactsDetailsActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/beta/me/contacts/";
    private Contact contact;
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
    private String contactYomiName;
    private String contactYomiCompanyName;
    private String contactId;
    private List<EmailAddress> emailList;
    private List<String> imAddresses;
    private List<String> businessPhones;
    private List<String> homePhones;
    private ImageButton phoneButton;
    private ImageButton calendarButton;
    private ImageButton mailButton;
    private ImageButton smsButton;
    private TextView email;
    private TextView mobilePhone;
    private TextView notesText;
    private TextView spouseText;
    private TextView nicknameText;
    private TextView homeAddressTitle;
    private TextView homeStreetText;
    private TextView homeLocationText;
    private TextView homeCountryText;
    private TextView businessAddressTitle;
    private TextView businessStreetText;
    private TextView businessLocationText;
    private TextView businessCountryText;
    private TextView otherAddressTitle;
    private TextView otherStreetText;
    private TextView otherLocationText;
    private TextView otherCountryText;
    private TextView jobText;
    private TextView departmentText;
    private TextView companyText;
    private TextView officeText;
    private TextView managerText;
    private TextView assisantText;
    private TextView firstNameText;
    private TextView lastNameText;
    private TextView spouseTitle;
    private TextView notesTitle;
    private TextView nicknameTitle;
    private TextView workTitle;
    private TextView userEmail2;
    private TextView userEmail3;
    private TextView businessPhone1;
    private TextView businessPhone2;
    private TextView homePhone1;
    private TextView homePhone2;
    private TextView homePhoneTitle;
    private TextView businessPhoneTitle;
    private TextView birthdayTitle;
    private TextView birthdayText;
    private TextView imTitle;
    private TextView imText;
    private TextView middleNameTitle;
    private TextView middleNameText;
    private TextView titleTitle;
    private TextView titleText;
    private TextView suffixTitle;
    private TextView suffixText;
    private TextView yomiNameTitle;
    private TextView yomiNameText;
    private TextView yomiCompany;
    private TextView mailSub;
    private TextView planSub;
    private TextView callSub;
    private TextView smsSub;
    private ImageView profilePic;
    private Toolbar myToolbar;
    private  AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        phoneButton = (ImageButton) findViewById(R.id.phoneButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        mailButton = (ImageButton) findViewById(R.id.mailButton);
        smsButton = (ImageButton) findViewById(R.id.smsButton);
        email = (TextView) findViewById(R.id.userEmail);
        mobilePhone = (TextView) findViewById(R.id.mobilePhone);
        notesText = (TextView) findViewById(R.id.notes);
        nicknameText = (TextView) findViewById(R.id.nickname);
        spouseText = (TextView) findViewById(R.id.spouse);
        homeAddressTitle = (TextView) findViewById(R.id.homeAddressTitle);
        homeStreetText = (TextView) findViewById(R.id.homeStreet);
        homeLocationText = (TextView) findViewById(R.id.homeLocation);
        homeCountryText = (TextView) findViewById(R.id.homeCountry);
        businessAddressTitle = (TextView) findViewById(R.id.businessAddressTitle);
        businessStreetText = (TextView) findViewById(R.id.businessStreet);
        businessLocationText = (TextView) findViewById(R.id.businessLocation);
        businessCountryText = (TextView) findViewById(R.id.businessCountry);
        otherAddressTitle = (TextView) findViewById(R.id.otherAddressTitle);
        otherStreetText = (TextView) findViewById(R.id.otherStreet);
        otherLocationText = (TextView) findViewById(R.id.otherLocation);
        otherCountryText = (TextView) findViewById(R.id.otherCountry);
        jobText = (TextView) findViewById(R.id.jobDetails);
        departmentText = (TextView) findViewById(R.id.departmentDetails);
        companyText = (TextView) findViewById(R.id.companyDetails);
        officeText = (TextView) findViewById(R.id.officeDetails);
        managerText = (TextView) findViewById(R.id.managerDetails);
        assisantText = (TextView) findViewById(R.id.assistantDetails);
        firstNameText = (TextView) findViewById(R.id.firstName);
        lastNameText = (TextView) findViewById(R.id.lastName);
        spouseTitle = (TextView) findViewById(R.id.spouseTitle);
        notesTitle = (TextView) findViewById(R.id.notesTitle);
        nicknameTitle = (TextView) findViewById(R.id.nicknameTitle);
        workTitle = (TextView) findViewById(R.id.workTitle);
        userEmail2 = (TextView) findViewById(R.id.userEmail2);
        userEmail3 = (TextView) findViewById(R.id.userEmail3);
        businessPhone1 = (TextView) findViewById(R.id.businessPhone1);
        businessPhone2 = (TextView) findViewById(R.id.businessPhone2);
        homePhone1 = (TextView) findViewById(R.id.homePhone1);
        homePhone2 = (TextView) findViewById(R.id.homePhone2);
        homePhoneTitle = (TextView) findViewById(R.id.homePhoneTitle);
        businessPhoneTitle = (TextView) findViewById(R.id.businessPhoneTitle);
        birthdayTitle = (TextView) findViewById(R.id.birthdayTitle);
        birthdayText = (TextView) findViewById(R.id.birthdayText);
        imTitle = (TextView) findViewById(R.id.imTitle);
        imText = (TextView) findViewById(R.id.imText);
        middleNameTitle = (TextView) findViewById(R.id.middleNameTitle);
        middleNameText = (TextView) findViewById(R.id.middleNameText);
        titleTitle = (TextView) findViewById(R.id.titleTitle);
        titleText = (TextView) findViewById(R.id.titleText);
        suffixTitle = (TextView) findViewById(R.id.suffixTitle);
        suffixText = (TextView) findViewById(R.id.suffixText);
        yomiNameTitle = (TextView) findViewById(R.id.yomiNameTitle);
        yomiNameText = (TextView) findViewById(R.id.yomiNameText);
        yomiCompany = (TextView) findViewById(R.id.yomiCompany);
        mailSub = (TextView) findViewById(R.id.mailSub);
        planSub = (TextView) findViewById(R.id.planSub);
        callSub = (TextView) findViewById(R.id.callSub);
        smsSub = (TextView) findViewById(R.id.smsSub);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        contactId = getIntent().getStringExtra("contactId");
        contact = (Contact) getIntent().getSerializableExtra("contact");

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        if(contact.getBusinessPhones() != null){
            businessPhones = contact.getBusinessPhones();
        } else {
            businessPhones = new ArrayList<>();
        }

        if(contact.getHomePhones() != null){
            homePhones = contact.getHomePhones();
        } else {
            homePhones = new ArrayList<>();
        }

        notes = contact.getPersonalNotes();
        spouse = contact.getSpouseName();
        nickname = contact.getNickName();
        firstName = contact.getGivenName();
        lastName = contact.getSurname();
        displayName = contact.getDisplayName();
        emailList = contact.getEmailAddresses();
        phoneNumber = contact.getMobilePhone();
        job = contact.getJobTitle();
        department = contact.getDepartment();
        company = contact.getCompanyName();
        office = contact.getOfficeLocation();
        manager = contact.getManager();
        assistant = contact.getManager();
        contactBirthday = contact.getBirthday();
        imAddresses = contact.getImAddresses();
        contactMiddleName = contact.getMiddleName();
        contactTitle = contact.getTitle();
        contactSuffix = contact.getInitials();
        contactYomiCompanyName = contact.getYomiCompanyName();
        id = contact.getId();

        if(contact.getYomiGivenName() != null && contact.getYomiSurname() != null){
            contactYomiName = contact.getYomiGivenName() +" " + contact.getYomiSurname();
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(displayName.substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .beginConfig()
                .width(50)  // width in px
                .height(50) // height in px
                .endConfig()
                .buildRect(displayName.substring(0,1), color2);

        profilePic.setImageDrawable(drawable1);

        if(emailList != null){
            if(!emailList.isEmpty()){

                if( emailList.size() == 1){
                    email.setText(emailList.get(0).getAddress());
                    userEmail2.setVisibility(View.GONE);
                    userEmail3.setVisibility(View.GONE);

                    setMailClickListener(email, emailList.get(0).getAddress());
                }

                if( emailList.size() > 1 && emailList.size() < 3){
                    email.setText(emailList.get(0).getAddress());
                    userEmail2.setText(emailList.get(1).getAddress());
                    userEmail3.setVisibility(View.GONE);

                    setMailClickListener(email, emailList.get(0).getAddress());
                    setMailClickListener(userEmail2, emailList.get(1).getAddress());
                }

                if( emailList.size() > 2 && emailList.size() < 4){
                    email.setText(emailList.get(0).getAddress());
                    userEmail2.setText(emailList.get(1).getAddress());
                    userEmail3.setText(emailList.get(2).getAddress());

                    setMailClickListener(email, emailList.get(0).getAddress());
                    setMailClickListener(userEmail2, emailList.get(1).getAddress());
                    setMailClickListener(userEmail3, emailList.get(2).getAddress());

                }

                mailButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent sendMail = new Intent(ContactsDetailsActivity.this, SendMailActivity.class);
                        sendMail.putExtra("AccessToken", accessToken);
                        sendMail.putExtra("userName", userName);
                        sendMail.putExtra("userEmail", userEmail);
                        sendMail.putExtra("emailAddress", emailList.get(0).getAddress());
                        sendMail.putExtra("contact", contact);
                        sendMail.putExtra("fromContactDetailsActivity", "yes");

                        startActivity(sendMail);


                    }
                });

                calendarButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent addEventActivity = new Intent(ContactsDetailsActivity.this, AddEventActivity.class);
                        addEventActivity.putExtra("AccessToken", accessToken);
                        addEventActivity.putExtra("userName", userName);
                        addEventActivity.putExtra("userEmail", userEmail);
                        addEventActivity.putExtra("emailAddress", emailList.get(0).getAddress());
                        addEventActivity.putExtra("attendeeMail", emailList.get(0).getAddress());
                        addEventActivity.putExtra("attendeeName", emailList.get(0).getName());
                        addEventActivity.putExtra("contact", contact);
                        addEventActivity.putExtra("fromContactDetailsActivity", "yes");

                        startActivity(addEventActivity);


                    }
                });
        } else {
                email.setText("(Empty)");
                email.setTextColor(Color.parseColor("#F4E7D7"));
                userEmail2.setVisibility(View.GONE);
                userEmail3.setVisibility(View.GONE);

                mailButton.setColorFilter(Color.GRAY);
                mailSub.setTextColor(Color.GRAY);
                calendarButton.setColorFilter(Color.GRAY);
                planSub.setTextColor(Color.GRAY);

                mailButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "No E-mail Address found!", Toast.LENGTH_SHORT).show();
                    }
                });

                calendarButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "No E-mail Address found!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        else {
            email.setText("(Empty)");
            email.setTextColor(Color.parseColor("#F4E7D7"));
            userEmail2.setVisibility(View.GONE);
            userEmail3.setVisibility(View.GONE);

            mailButton.setColorFilter(Color.GRAY);
            mailSub.setTextColor(Color.GRAY);
            calendarButton.setColorFilter(Color.GRAY);
            planSub.setTextColor(Color.GRAY);

            mailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No E-mail Address found!", Toast.LENGTH_SHORT).show();
                }
            });

            calendarButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No E-mail Address found!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (phoneNumber == null && businessPhones.isEmpty() && homePhones.isEmpty()){
            smsButton.setColorFilter(Color.GRAY);
            smsSub.setTextColor(Color.GRAY);
            phoneButton.setColorFilter(Color.GRAY);
            callSub.setTextColor(Color.GRAY);

            phoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No Phone Number found!", Toast.LENGTH_SHORT).show();
                }
            });

            smsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No Phone Number found!", Toast.LENGTH_SHORT).show();
                }
            });

        } else if((phoneNumber == null) || !businessPhones.isEmpty() || !homePhones.isEmpty()){

            if(phoneNumber == null){
                smsButton.setColorFilter(Color.GRAY);
                smsSub.setTextColor(Color.GRAY);

                smsButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "No Phone Number found!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                smsButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:"));
                        sendIntent.putExtra("address", phoneNumber);
                        startActivity(sendIntent);
                    }
                });
            }


            if(businessPhones.isEmpty()){

                phoneButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + homePhones.get(0)));
                        startActivity(intent);
                    }
                });

            } else if (homePhones.isEmpty()){

                phoneButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + businessPhones.get(0)));
                        startActivity(intent);
                    }
                });

            } else {

                phoneButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + businessPhones.get(0)));
                        startActivity(intent);
                    }
                });

            }


        } else if(phoneNumber!= null && businessPhones.isEmpty() && homePhones.isEmpty()){

            phoneButton.setColorFilter(Color.GRAY);
            callSub.setTextColor(Color.GRAY);

            phoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No Phone Number found!", Toast.LENGTH_SHORT).show();
                }
            });

            smsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("address", phoneNumber);
                    startActivity(sendIntent);
                }
            });

        } else if(phoneNumber != null && !homePhones.isEmpty() && !businessPhones.isEmpty()){

            phoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            });


            smsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("address", phoneNumber);
                    startActivity(sendIntent);
                }
            });
        }

        TextView headerDisplayName = (TextView) findViewById(R.id.displayName);
        headerDisplayName.setText(displayName);

        if (phoneNumber != null){
            mobilePhone.setText(phoneNumber);

            setPhoneNumberClickListener(mobilePhone, phoneNumber);

        } else {
            mobilePhone.setText("(Empty)");
            mobilePhone.setTextColor(Color.parseColor("#F4E7D7"));
        }

        if(businessPhones != null) {

            if(!businessPhones.isEmpty()){

                if (businessPhones.size() == 1) {
                    businessPhone1.setText(businessPhones.get(0));
                    businessPhone2.setVisibility(View.GONE);

                    businessPhone1.setTextColor(Color.BLUE);

                    setPhoneNumberClickListener(businessPhone1, businessPhones.get(0));

                } else if (businessPhones.size() > 1) {
                    businessPhone1.setText(businessPhones.get(0));
                    businessPhone2.setText(businessPhones.get(1));

                    setPhoneNumberClickListener(businessPhone1, businessPhones.get(0));
                    setPhoneNumberClickListener(businessPhone2, businessPhones.get(1));

                }

            } else {
                businessPhoneTitle.setVisibility(View.GONE);
                businessPhone1.setVisibility(View.GONE);
                businessPhone2.setVisibility(View.GONE);
            }

        } else {
            businessPhoneTitle.setVisibility(View.GONE);
            businessPhone1.setVisibility(View.GONE);
            businessPhone2.setVisibility(View.GONE);
        }


        if(homePhones != null) {
            if(!homePhones.isEmpty()){

                if (homePhones.size() == 1) {
                    homePhone1.setText(homePhones.get(0));
                    homePhone2.setVisibility(View.GONE);

                    setPhoneNumberClickListener(homePhone1, homePhones.get(0));

                } else if (homePhones.size() > 1) {
                    homePhone1.setText(homePhones.get(0));
                    homePhone2.setText(homePhones.get(1));

                    setPhoneNumberClickListener(homePhone1, homePhones.get(0));
                    setPhoneNumberClickListener(homePhone2, homePhones.get(1));

                }
            } else {
                homePhoneTitle.setVisibility(View.GONE);
                homePhone1.setVisibility(View.GONE);
                homePhone2.setVisibility(View.GONE);
            }

        } else {
            homePhoneTitle.setVisibility(View.GONE);
            homePhone1.setVisibility(View.GONE);
            homePhone2.setVisibility(View.GONE);
        }

        if(notes != null){
            if(!notes.equals("")){
                notesText.setText(notes);
            }else {
                notesText.setVisibility(View.GONE);
                notesTitle.setVisibility(View.GONE);
            }
        } else {
            notesText.setVisibility(View.GONE);
            notesTitle.setVisibility(View.GONE);
        }

        if(spouse != null){
            spouseText.setText(spouse);
        } else {
            spouseText.setVisibility(View.GONE);
            spouseTitle.setVisibility(View.GONE);
        }

        if(nickname != null){
            nicknameText.setText(nickname);
        } else {
            nicknameText.setVisibility(View.GONE);
            nicknameTitle.setVisibility(View.GONE);
        }

        if(contactBirthday != null){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
            Date d = null;
            try {
                d = sdf.parse(contactBirthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            birthdayText.setText(output.format(d));

        } else {
            birthdayText.setVisibility(View.GONE);
            birthdayTitle.setVisibility(View.GONE);
        }

        if((homeStreet == null) && (homePostalCode == null)  && (homeCity  == null) && (homeState == null) && (homeCountry == null)){
            homeAddressTitle.setVisibility(View.GONE);
        }


        if(homeStreet != null){
            homeStreetText.setText(homeStreet);
        } else {
            homeStreetText.setVisibility(View.GONE);
        }

        if(homePostalCode == null && homeCity == null && homeState == null){
            homeLocationText.setVisibility(View.GONE);
        } else if (homePostalCode == null && homeCity == null ){
            homeLocationText.setText(homeState);
        } else if (homePostalCode == null){
            homeLocationText.setText(homeCity + " " + homeState);
        } else if (homeCity == null){
            homeLocationText.setText(homePostalCode + " " + homeState);
        } else if (homeState == null){
            homeLocationText.setText(homePostalCode + " " + homeCity);
        } else{
            homeLocationText.setText(homeCity + " " + homeState + " " + homePostalCode);
        }

        if(homeCountry !=null){
            homeCountryText.setText(homeCountry);
        } else {
            homeCountryText.setVisibility(View.GONE);
        }

        if((businessStreet == null) && (businessPostalCode == null)  && (businessCity  == null) && (businessState == null) && (businessCountry == null)){
            businessAddressTitle.setVisibility(View.GONE);
        }

        if(businessStreet != null){
            businessStreetText.setText(businessStreet);
        } else {
            businessStreetText.setVisibility(View.GONE);
        }

        if(businessPostalCode == null && businessCity == null && businessState == null){
            businessLocationText.setVisibility(View.GONE);
        } else if (businessPostalCode == null && businessCity == null ){
            businessLocationText.setText(businessState);
        } else if (businessPostalCode == null){
            businessLocationText.setText(businessCity + " " + businessState);
        } else if (businessCity == null){
            businessLocationText.setText(businessPostalCode + " " + businessState);
        } else if (businessState == null){
            businessLocationText.setText(businessPostalCode + " " + businessCity);
        } else{
            businessLocationText.setText(businessCity + " " + businessState + " " + businessPostalCode);
        }

        if(businessCountry !=null){
            businessCountryText.setText(businessCountry);
        } else {
            businessCountryText.setVisibility(View.GONE);
        }

        if((otherStreet == null) && (otherPostalCode == null)  && (otherCity  == null) && (otherState == null) && (otherCountry == null)){
            otherAddressTitle.setVisibility(View.GONE);
        }

        if(otherStreet != null){
            otherStreetText.setText(otherStreet);
        } else {
            otherStreetText.setVisibility(View.GONE);
        }

        if(otherPostalCode == null && otherCity == null && otherState == null){
            otherLocationText.setVisibility(View.GONE);
        } else if (otherPostalCode == null && otherCity == null ){
            otherLocationText.setText(otherState);
        } else if (otherPostalCode == null){
            otherLocationText.setText(otherCity + " " + otherState);
        } else if (otherCity == null){
            otherLocationText.setText(otherPostalCode + " " + otherState);
        } else if (otherState == null){
            otherLocationText.setText(otherPostalCode + " " + otherCity);
        } else{
            otherLocationText.setText(otherCity + " " + otherState + " " + otherPostalCode);
        }

        if(otherCountry !=null){
            otherCountryText.setText(otherCountry);
        } else {
            otherCountryText.setVisibility(View.GONE);
        }

        if((job == null) && (department == null) && (company == null) && (office == null) && (manager == null) && (assistant == null && (contactYomiCompanyName == null))){
            workTitle.setVisibility(View.GONE);
        }

        if(job != null){
            jobText.setText("Job title: " + job);
        } else {
            jobText.setVisibility(View.GONE);
        }

        if(department != null){
            departmentText.setText("Department: " + department);
        } else {
            departmentText.setVisibility(View.GONE);
        }

        if(company != null){
            companyText.setText("Company: " + company);
        } else {
            companyText.setVisibility(View.GONE);
        }

        if(office != null){
            officeText.setText("Office: " + office);
        } else {
            officeText.setVisibility(View.GONE);
            officeText.setVisibility(View.GONE);
        }

        if(manager != null){
            managerText.setText("Manager: " + manager);
        } else {
            managerText.setVisibility(View.GONE);
        }

        if(assistant != null){
            assisantText.setText("Assistant: " + assistant);
        } else {
            assisantText.setVisibility(View.GONE);
        }

        if(contactYomiCompanyName != null){
            yomiCompany.setText("Yomi company: " + assistant);
        } else {
            yomiCompany.setVisibility(View.GONE);
        }

        if(firstName != null){
            firstNameText.setText(firstName);
        } else {
            firstNameText.setVisibility(View.GONE);
        }

        if(lastName != null){
            lastNameText.setText(lastName);
        } else {
            lastNameText.setVisibility(View.GONE);
        }

        if(imAddresses != null){
            if(!imAddresses.isEmpty()){
                imText.setText(imAddresses.get(0));
            } else {
                imText.setVisibility(View.GONE);
                imTitle.setVisibility(View.GONE);
            }
        } else {
            imText.setVisibility(View.GONE);
            imTitle.setVisibility(View.GONE);
        }



        if(contactMiddleName != null){
            middleNameText.setText(contactMiddleName);
        } else {
            middleNameTitle.setVisibility(View.GONE);
            middleNameText.setVisibility(View.GONE);
        }

        if(contactTitle != null){
            titleText.setText(contactTitle);
        } else {
            titleText.setVisibility(View.GONE);
            titleTitle.setVisibility(View.GONE);
        }

        if(contactSuffix != null){
            suffixText.setText(contactSuffix);
        } else {
            suffixText.setVisibility(View.GONE);
            suffixTitle.setVisibility(View.GONE);
        }

        if(contactYomiName != null){
            yomiNameText.setText(contactYomiName);
        } else {
            yomiNameText.setVisibility(View.GONE);
            yomiNameTitle.setVisibility(View.GONE);
        }

        builder = new AlertDialog.Builder(ContactsDetailsActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete this contact?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    deleteContact();

                    int DELAY_TIME=2000;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentContacts = new Intent(ContactsDetailsActivity.this, ContactsActivity.class);
                            intentContacts.putExtra("AccessToken", accessToken);
                            intentContacts.putExtra("userName", userName);
                            intentContacts.putExtra("userEmail", userEmail);
                            intentContacts.putExtra("contact", contact);

                            startActivity(intentContacts);

                            ContactsDetailsActivity.this.finish();


                        }
                    }, DELAY_TIME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentContacts = new Intent(ContactsDetailsActivity.this, ContactsActivity.class);
                intentContacts.putExtra("AccessToken", accessToken);
                intentContacts.putExtra("userName", userName);
                intentContacts.putExtra("userEmail", userEmail);
                intentContacts.putExtra("contact", contact);

                startActivity(intentContacts);

                ContactsDetailsActivity.this.finish();

                return true;

            case R.id.action_delete:

                builder.show();

                return true;

            case R.id.action_edit:
                Intent intentEditContact = new Intent(ContactsDetailsActivity.this, EditContactActivity.class);
                intentEditContact.putExtra("AccessToken", accessToken);
                intentEditContact.putExtra("userName", userName);
                intentEditContact.putExtra("userEmail", userEmail);
                intentEditContact.putExtra("contactId", contactId);
                intentEditContact.putExtra("contact", contact);

                startActivity(intentEditContact);

                ContactsDetailsActivity.this.finish();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // PATCH REQUEST VOOR DELETEN CONTACTPERSOON
    private void deleteContact() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        if (accessToken == null){
            Intent logout = new Intent(ContactsDetailsActivity.this, MainActivity.class);
            logout.putExtra("AccessToken", accessToken);
            logout.putExtra("userName", userName);
            logout.putExtra("userEmail", userEmail);

            startActivity(logout);

            ContactsDetailsActivity.this.finish();
        }

        String postAddress = URL_POSTADRESS + id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Contact deleted!", Toast.LENGTH_SHORT).show();
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

        queue.add(stringRequest);

    }

    public void setMailClickListener(TextView textView, final String mail){


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendMail = new Intent(ContactsDetailsActivity.this, SendMailActivity.class);
                sendMail.putExtra("AccessToken", accessToken);
                sendMail.putExtra("userName", userName);
                sendMail.putExtra("emailAddress", mail);
                sendMail.putExtra("contact", contact);
                sendMail.putExtra("fromContactDetailsActivity", "yes");

                startActivity(sendMail);

                ContactsDetailsActivity.this.finish();
            }
        });

    }

    public void setPhoneNumberClickListener(TextView textView, final String number){

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(ContactsDetailsActivity.this, ContactsActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);
        intentListMails.putExtra("contact", contact);

        startActivity(intentListMails);

        ContactsDetailsActivity.this.finish();
    }

}
