package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.Mail.SendMailActivity;
import com.example.keiichi.project_mobile.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class ContactsDetailsActivity extends AppCompatActivity {

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
    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String job;
    private String department;
    private String company;
    private String office;
    private String manager;
    private String assistant;
    private String firstName;
    private String lastName;
    private String id;
    private List<EmailAddress> emailList;
    private ImageButton phoneButton;
    private ImageButton calendarButton;
    private ImageButton mailButton;
    private ImageButton smsButton;
    private TextView email;
    private TextView userPhone;
    private TextView notesText;
    private TextView spouseText;
    private TextView nicknameText;
    private TextView streetText;
    private TextView locationText;
    private TextView countryText;
    private TextView jobText;
    private TextView departmentText;
    private TextView companyText;
    private TextView officeText;
    private TextView managerText;
    private TextView assisantText;
    private TextView firstNameText;
    private TextView lastNameText;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        phoneButton = (ImageButton) findViewById(R.id.phoneButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        mailButton = (ImageButton) findViewById(R.id.mailButton);
        smsButton = (ImageButton) findViewById(R.id.smsButton);
        email = (TextView) findViewById(R.id.userEmail);
        userPhone = (TextView) findViewById(R.id.userPhone);
        notesText = (TextView) findViewById(R.id.notes);
        nicknameText = (TextView) findViewById(R.id.nickname);
        spouseText = (TextView) findViewById(R.id.spouse);
        streetText = (TextView) findViewById(R.id.street);
        locationText = (TextView) findViewById(R.id.location);
        countryText = (TextView) findViewById(R.id.country);
        jobText = (TextView) findViewById(R.id.jobDetails);
        departmentText = (TextView) findViewById(R.id.departmentDetails);
        companyText = (TextView) findViewById(R.id.companyDetails);
        officeText = (TextView) findViewById(R.id.officeDetails);
        managerText = (TextView) findViewById(R.id.managerDetails);
        assisantText = (TextView) findViewById(R.id.assistantDetails);
        firstNameText = (TextView) findViewById(R.id.firstName);
        lastNameText = (TextView) findViewById(R.id.lastName);

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        givenName = getIntent().getStringExtra("givenName");
        displayName = getIntent().getStringExtra("displayName");
        phoneNumber = getIntent().getStringExtra("userPhone");
        emailList = (List<EmailAddress>)getIntent().getSerializableExtra("emailList");
        emailAddress = getIntent().getStringExtra("email");
        notes = getIntent().getStringExtra("notes");
        nickname = getIntent().getStringExtra("nickname");
        spouse = getIntent().getStringExtra("spouse");
        street = getIntent().getStringExtra("street");
        postalCode = getIntent().getStringExtra("postalcode");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");
        job = getIntent().getStringExtra("job");
        department = getIntent().getStringExtra("department");
        company = getIntent().getStringExtra("company");
        office = getIntent().getStringExtra("office");
        manager = getIntent().getStringExtra("manager");
        assistant = getIntent().getStringExtra("assistant");
        firstName = getIntent().getStringExtra("firstname");
        lastName = getIntent().getStringExtra("lastname");
        id = getIntent().getStringExtra("id");

        if(!emailList.isEmpty()){
            emailAddress = emailList.get(0).getAddress();

            email.setText(emailAddress);

            mailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendMail = new Intent(ContactsDetailsActivity.this, SendMailActivity.class);
                    sendMail.putExtra("AccessToken", accessToken);
                    sendMail.putExtra("userName", userName);
                    sendMail.putExtra("userEmail", userEmail);
                    sendMail.putExtra("emailAddress", emailAddress);
                    startActivity(sendMail);
                }
            });
        }
        else {

            mailButton.setColorFilter(Color.GRAY);

            mailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "No E-mail Address found!", Toast.LENGTH_SHORT).show();
                }
            });
        }


        if (phoneNumber.equals("")){
            smsButton.setColorFilter(Color.GRAY);
            phoneButton.setColorFilter(Color.GRAY);

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
        } else{

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




        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Plan!", Toast.LENGTH_SHORT).show();
            }
        });


        TextView headerDisplayName = (TextView) findViewById(R.id.displayName);
        headerDisplayName.setText(displayName);
        userPhone.setText(phoneNumber);
        notesText.setText(notes);
        spouseText.setText(spouse);
        nicknameText.setText(nickname);
        streetText.setText(street);
        locationText.setText(postalCode + " " + city + " " + state);
        countryText.setText(country);
        jobText.setText("Job: " + job);
        departmentText.setText("Department: " +department);
        companyText.setText("Company: " + company);
        officeText.setText("Office: " + office);
        managerText.setText("Manager: " + manager);
        assisantText.setText("Assistant: " +assistant);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
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
                intentContacts.putExtra("givenName", givenName);
                intentContacts.putExtra("displayName", displayName);
                intentContacts.putExtra("userPhone", phoneNumber);
                intentContacts.putExtra("emailList",(Serializable) emailList);
                intentContacts.putExtra("notes", notes);
                intentContacts.putExtra("nickname", nickname);
                intentContacts.putExtra("spouse", spouse);
                intentContacts.putExtra("street", street);
                intentContacts.putExtra("postalcode", postalCode);
                intentContacts.putExtra("city", city);
                intentContacts.putExtra("state", state);
                intentContacts.putExtra("country", country);
                intentContacts.putExtra("job", job);
                intentContacts.putExtra("department", department);
                intentContacts.putExtra("company", company);
                intentContacts.putExtra("office", office);
                intentContacts.putExtra("manager", manager);
                intentContacts.putExtra("assistant", assistant);
                intentContacts.putExtra("firstname", firstName);
                intentContacts.putExtra("lastname", lastName);

                startActivity(intentContacts);

                return true;

            case R.id.action_edit:
                Intent intentEditContact = new Intent(ContactsDetailsActivity.this, EditContactActivity.class);
                intentEditContact.putExtra("AccessToken", accessToken);
                intentEditContact.putExtra("userName", userName);
                intentEditContact.putExtra("userEmail", userEmail);
                intentEditContact.putExtra("givenName", givenName);
                intentEditContact.putExtra("displayName", displayName);
                intentEditContact.putExtra("userPhone", phoneNumber);
                intentEditContact.putExtra("emailList",(Serializable) emailList);
                intentEditContact.putExtra("email", emailAddress);
                intentEditContact.putExtra("notes", notes);
                intentEditContact.putExtra("nickname", nickname);
                intentEditContact.putExtra("spouse", spouse);
                intentEditContact.putExtra("street", street);
                intentEditContact.putExtra("postalcode", postalCode);
                intentEditContact.putExtra("city", city);
                intentEditContact.putExtra("state", state);
                intentEditContact.putExtra("country", country);
                intentEditContact.putExtra("job", job);
                intentEditContact.putExtra("department", department);
                intentEditContact.putExtra("company", company);
                intentEditContact.putExtra("office", office);
                intentEditContact.putExtra("manager", manager);
                intentEditContact.putExtra("assistant", assistant);
                intentEditContact.putExtra("firstname", firstName);
                intentEditContact.putExtra("lastname", lastName);
                intentEditContact.putExtra("id", id);

                startActivity(intentEditContact);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
