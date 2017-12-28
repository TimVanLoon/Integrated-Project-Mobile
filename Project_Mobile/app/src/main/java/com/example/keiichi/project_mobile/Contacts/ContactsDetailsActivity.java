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
    private List<EmailAddress> emailList;
    private ImageButton phoneButton;
    private ImageButton calendarButton;
    private ImageButton mailButton;
    private ImageButton smsButton;
    private TextView email;
    private TextView userPhone;
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

        System.out.println("test list : " + emailList.toString());


        if(!emailList.isEmpty()){
            emailAddress = emailList.get(0).getAddress();

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
        email.setText(userEmail);
        userPhone.setText(phoneNumber);
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

                startActivity(intentEditContact);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
