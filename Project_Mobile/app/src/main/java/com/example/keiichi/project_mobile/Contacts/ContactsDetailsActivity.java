package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keiichi.project_mobile.R;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class ContactsDetailsActivity extends AppCompatActivity {

    private String accessToken;
    private String userName;
    private String userEmail;
    private String givenName;
    private String displayName;
    private ImageButton phoneButton;
    private ImageButton calendarButton;
    private ImageButton mailButton;
    private ImageButton smsButton;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        phoneButton = (ImageButton) findViewById(R.id.phoneButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        mailButton = (ImageButton) findViewById(R.id.mailButton);
        smsButton = (ImageButton) findViewById(R.id.smsButton);

        phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Call!", Toast.LENGTH_SHORT).show();
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Plan!", Toast.LENGTH_SHORT).show();
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Mail!", Toast.LENGTH_SHORT).show();
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Text!", Toast.LENGTH_SHORT).show();
            }
        });
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

        TextView headerDisplayName = (TextView) findViewById(R.id.displayName);
        headerDisplayName.setText(displayName);
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
                startActivity(intentContacts);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
