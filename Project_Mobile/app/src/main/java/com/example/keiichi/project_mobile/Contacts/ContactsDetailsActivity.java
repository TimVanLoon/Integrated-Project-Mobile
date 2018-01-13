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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Calendar.AddEventActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.PhysicalAddress;
import com.example.keiichi.project_mobile.Mail.SendMailActivity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ContactsDetailsActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/beta/me/contacts/";
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
    private TextView addressTitle;
    private TextView spouseTitle;
    private TextView notesTitle;
    private TextView nicknameTitle;
    private TextView workTitle;
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
        addressTitle = (TextView) findViewById(R.id.addressTitle);
        spouseTitle = (TextView) findViewById(R.id.spouseTitle);
        notesTitle = (TextView) findViewById(R.id.notesTitle);
        nicknameTitle = (TextView) findViewById(R.id.nicknameTitle);
        workTitle = (TextView) findViewById(R.id.workTitle);
        profilePic = (ImageView) findViewById(R.id.profilePic);

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

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(displayName.substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .beginConfig()
                .width(50)  // width in px
                .height(50) // height in px
                .endConfig()
                .buildRect(displayName.substring(0,1), color2);

        profilePic.setImageDrawable(drawable1);

        if(!emailList.isEmpty()){
            emailAddress = emailList.get(0).getAddress();

            email.setText(emailAddress);

            mailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendMail = new Intent(ContactsDetailsActivity.this, SendMailActivity.class);
                    sendMail.putExtra("AccessToken", accessToken);
                    sendMail.putExtra("userName", userName);
                    sendMail.putExtra("userEmail", emailList.get(0).getAddress());
                    sendMail.putExtra("userMailName", emailList.get(0).getName());
                    sendMail.putExtra("emailAddress", emailAddress);
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
                    addEventActivity.putExtra("emailAddress", emailAddress);
                    addEventActivity.putExtra("attendeeMail", emailList.get(0).getAddress());
                    addEventActivity.putExtra("attendeeName", emailList.get(0).getName());
                    addEventActivity.putExtra("fromContactDetailsActivity", "yes");

                    startActivity(addEventActivity);


                }
            });
        }
        else {
            email.setText("(Empty)");
            email.setTextColor(Color.parseColor("#F4E7D7"));

            mailButton.setColorFilter(Color.GRAY);
            calendarButton.setColorFilter(Color.GRAY);

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

        TextView headerDisplayName = (TextView) findViewById(R.id.displayName);
        headerDisplayName.setText(displayName);

        if (!phoneNumber.equals("")){
            userPhone.setText(phoneNumber);
        } else {
            userPhone.setText("(Empty)");
            userPhone.setTextColor(Color.parseColor("#F4E7D7"));
        }

        if(!notes.equals("")){
            notesText.setText(notes);
        } else {
            notesText.setVisibility(View.GONE);
            notesTitle.setVisibility(View.GONE);
        }

        if(!spouse.equals("")){
            spouseText.setText(spouse);
        } else {
            spouseText.setVisibility(View.GONE);
            spouseTitle.setVisibility(View.GONE);
        }

        if(!nickname.equals("")){
            nicknameText.setText(nickname);
        } else {
            nicknameText.setVisibility(View.GONE);
            nicknameTitle.setVisibility(View.GONE);
        }


        if(street.equals("") && postalCode.equals("") && city.equals("") && state.equals("") && country.equals("")){
            addressTitle.setVisibility(View.GONE);
        }


        if(!street.equals("")){
            streetText.setText(street);
        } else {
            streetText.setVisibility(View.GONE);
        }

        if(postalCode.equals("") && city.equals("") && state.equals("")){
            locationText.setVisibility(View.GONE);
        } else if (postalCode.equals("") && city.equals("") ){
            locationText.setText(state);
        } else if (postalCode.equals("")){
            locationText.setText(city + " " + state);
        } else if (city.equals("")){
            locationText.setText(postalCode + " " + state);
        } else if (state.equals("")){
            locationText.setText(postalCode + " " + city);
        }

        if(!country.equals("")){
            countryText.setText(country);
        } else {
            countryText.setVisibility(View.GONE);
        }


        if(job.equals("") && department.equals("") && company.equals("") && office.equals("") && manager.equals("") && assistant.equals("")){
            workTitle.setVisibility(View.GONE);
        }

        if(!job.equals("")){
            jobText.setText(job);
        } else {
            jobText.setVisibility(View.GONE);
        }

        if(!department.equals("")){
            departmentText.setText(department);
        } else {
            departmentText.setVisibility(View.GONE);
        }

        if(!company.equals("")){
            companyText.setText(company);
        } else {
            companyText.setVisibility(View.GONE);
        }

        if(!office.equals("")){
            officeText.setText(office);
        } else {
            officeText.setVisibility(View.GONE);
            officeText.setVisibility(View.GONE);
        }

        if(!manager.equals("")){
            managerText.setText(manager);
        } else {
            managerText.setVisibility(View.GONE);
        }

        if(!assistant.equals("")){
            assisantText.setText(assistant);
        } else {
            assisantText.setVisibility(View.GONE);
        }

        if(!firstName.equals("")){
            firstNameText.setText(firstName);
        } else {
            firstNameText.setVisibility(View.GONE);
        }

        if(!lastName.equals("")){
            lastNameText.setText(lastName);
        } else {
            lastNameText.setVisibility(View.GONE);
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
                intentContacts.putExtra("id", id);

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

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(ContactsDetailsActivity.this, ContactsActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        ContactsDetailsActivity.this.finish();
    }

}
