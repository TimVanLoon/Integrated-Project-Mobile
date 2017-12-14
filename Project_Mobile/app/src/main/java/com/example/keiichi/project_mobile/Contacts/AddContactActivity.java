package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class AddContactActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/contacts";

    private String accessToken;

    Toolbar myToolbar;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText phoneInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        accessToken = getIntent().getStringExtra("AccessToken");

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTONN TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);

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
                startActivity(intentContacts);

                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
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
                            startActivity(intentContacts);
                        }
                    }, DELAY_TIME);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;

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

        EmailAddress contactEmailAddress = new EmailAddress(firstNameInput.getText().toString() + " " + lastNameInput.getText().toString(), emailInput.getText().toString());
        List<EmailAddress> contactList = new ArrayList<>();
        contactList.add(contactEmailAddress);

        contact.setEmailAddresses(contactList);

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

}
