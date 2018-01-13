package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.example.keiichi.project_mobile.DAL.POJOs.PhysicalAddress;
import com.example.keiichi.project_mobile.Mail.DisplayMailActivity;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Toolbar myToolbar;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText jobTitle;
    private EditText department;
    private EditText companyName;
    private EditText officeLocation;
    private EditText manager;
    private EditText assistantName;
    private EditText streetName;
    private EditText postalCode;
    private EditText cityName;
    private EditText stateName;
    private EditText countryName;
    private EditText personalNotes;
    private EditText nickName;
    private EditText spouseName;
    private String userName;
    private String userEmail;
    private String attendeeName;
    private String attendeeMail;
    private String accessToken;
    private boolean isValidEmail;

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
        jobTitle = (EditText) findViewById(R.id.jobInput);
        department = (EditText) findViewById(R.id.department);
        companyName = (EditText) findViewById(R.id.companyName);
        officeLocation = (EditText) findViewById(R.id.officeLocation);
        manager = (EditText) findViewById(R.id.manager);
        assistantName = (EditText) findViewById(R.id.assistantName);
        streetName = (EditText) findViewById(R.id.streetName);
        postalCode = (EditText) findViewById(R.id.postalCode);
        cityName = (EditText) findViewById(R.id.cityName);
        stateName = (EditText) findViewById(R.id.stateName);
        countryName = (EditText) findViewById(R.id.countryName);
        personalNotes = (EditText) findViewById(R.id.personalNotes);
        nickName = (EditText) findViewById(R.id.nickName);
        spouseName = (EditText) findViewById(R.id.spouseName);

        setEditTextOnFocusListener(firstNameInput);
        setEditTextOnFocusListener(lastNameInput);
        setEditTextOnFocusListener(emailInput);
        setEditTextOnFocusListener(phoneInput);
        setEditTextOnFocusListener(jobTitle);
        setEditTextOnFocusListener(department);
        setEditTextOnFocusListener(companyName);
        setEditTextOnFocusListener(officeLocation);
        setEditTextOnFocusListener(manager);
        setEditTextOnFocusListener(assistantName);
        setEditTextOnFocusListener(streetName);
        setEditTextOnFocusListener(postalCode);
        setEditTextOnFocusListener(cityName);
        setEditTextOnFocusListener(stateName);
        setEditTextOnFocusListener(countryName);
        setEditTextOnFocusListener(personalNotes);
        setEditTextOnFocusListener(nickName);
        setEditTextOnFocusListener(spouseName);

    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_navigation, menu);


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

                        if (!emailInput.getText().toString().isEmpty() && !emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                            emailInput.setError("Invalid Email Address!");

                            if(!phoneInput.getText().toString().isEmpty() && !isValidMobile(phoneInput.getText().toString())){
                                phoneInput.setError("Invalid phone number!");
                            }

                        } else {

                            if(!phoneInput.getText().toString().isEmpty() && !isValidMobile(phoneInput.getText().toString())){
                                phoneInput.setError("Invalid phone number!");
                            } else {
                                try {
                                    saveContact();

                                    int DELAY_TIME=2000;

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

        if (!emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            emailInput.setError("Invalid Email Address");
        }
        else {
            EmailAddress contactEmail = new EmailAddress(emailInput.getText().toString(), displayName);
            List<EmailAddress> listEmails = new ArrayList<>();
            listEmails.add(contactEmail);
            contact.setEmailAddresses(listEmails);
        }



        if(!phoneInput.getText().toString().isEmpty()){
            contact.setMobilePhone(phoneInput.getText().toString());
        }


        if(!jobTitle.getText().toString().isEmpty()){
            contact.setJobTitle(jobTitle.getText().toString());
        }

        if(!department.getText().toString().isEmpty()){
            contact.setDepartment(department.getText().toString());
        }

        if(!companyName.getText().toString().isEmpty()){
            contact.setCompanyName(companyName.getText().toString());
        }

        if(!officeLocation.getText().toString().isEmpty()){
            contact.setOfficeLocation(officeLocation.getText().toString());
        }

        if(!manager.getText().toString().isEmpty()){
            contact.setManager(manager.getText().toString());
        }

        if(!assistantName.getText().toString().isEmpty()){
            contact.setAssistantName(assistantName.getText().toString());
        }

        if(!streetName.getText().toString().isEmpty()){
            PhysicalAddress contactPhysicalAddress = new PhysicalAddress(streetName.getText().toString(), cityName.getText().toString(), stateName.getText().toString(), countryName.getText().toString(), postalCode.getText().toString());
            contact.setHomeAddress(contactPhysicalAddress);
        }

        if(!nickName.getText().toString().isEmpty()){
            contact.setNickName(nickName.getText().toString());
        }

        if(!spouseName.getText().toString().isEmpty()){
            contact.setSpouseName(spouseName.getText().toString());
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

}
