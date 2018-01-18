package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
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
import com.example.keiichi.project_mobile.Calendar.AddEventActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.User;
import com.example.keiichi.project_mobile.Mail.SendMailActivity;
import com.example.keiichi.project_mobile.R;

public class UserDetailsActivity extends AppCompatActivity {

    private User user;
    private Toolbar myToolbar;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String displayName;
    private String mobilePhone;
    private String givenName;
    private String surName;
    private String mail;
    private String department;
    private TextView headerDisplayName;
    private TextView firstName;
    private TextView lastName;
    private TextView userMail;
    private TextView userPhone;
    private TextView mobilePhoneText;
    private TextView departmentDetails;
    private TextView workTitle;
    private TextView imTitle;
    private TextView imText;
    private TextView mailSub;
    private TextView planSub;
    private TextView callSub;
    private TextView smsSub;
    private ImageView profilePic;
    private ImageButton phoneButton;
    private ImageButton calendarButton;
    private ImageButton mailButton;
    private ImageButton smsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        phoneButton = (ImageButton) findViewById(R.id.phoneButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        mailButton = (ImageButton) findViewById(R.id.mailButton);
        smsButton = (ImageButton) findViewById(R.id.smsButton);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        headerDisplayName = (TextView) findViewById(R.id.displayName);
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
        userMail = (TextView) findViewById(R.id.userMail);
        userPhone = (TextView) findViewById(R.id.userPhone);
        mobilePhoneText = (TextView) findViewById(R.id.mobilePhone);
        departmentDetails = (TextView) findViewById(R.id.departmentDetails);
        workTitle = (TextView) findViewById(R.id.workTitle);
        imTitle = (TextView) findViewById(R.id.imTitle);
        imText = (TextView) findViewById(R.id.imText);
        mailSub = (TextView) findViewById(R.id.mailSub);
        planSub = (TextView) findViewById(R.id.planSub);
        callSub = (TextView) findViewById(R.id.callSub);
        smsSub = (TextView) findViewById(R.id.smsSub);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        user = (User) getIntent().getSerializableExtra("user");

        displayName = user.getDisplayName();
        mobilePhone = user.getMobilePhone();
        givenName = user.getGivenName();
        surName = user.getSurname();
        mail = user.getMail();
        department = user.getDepartment();

        System.out.println("test: " + user.getMySite());

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(displayName.substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .beginConfig()
                .width(50)  // width in px
                .height(50) // height in px
                .endConfig()
                .buildRect(displayName.substring(0,1), color2);

        profilePic.setImageDrawable(drawable1);
        headerDisplayName.setText(displayName);

        if(givenName != null){
            firstName.setText(givenName);
        } else {
            firstName.setVisibility(View.GONE);
        }

        if(surName != null){
            lastName.setText(surName);
        } else {
            lastName.setVisibility(View.GONE);
        }

        if(mail != null){
            userMail.setText(mail);

            setMailClickListener(userMail, mail);
        } else {
            userMail.setText("(Empty)");
            userMail.setTextColor(Color.parseColor("#F4E7D7"));
        }

        if(mobilePhone != null){
            mobilePhoneText.setText(mobilePhone);
        } else {
            mobilePhoneText.setVisibility(View.GONE);
            userPhone.setVisibility(View.GONE);
        }

        if(department != null){
            departmentDetails.setText("Department: " + department);
        } else {
            departmentDetails.setVisibility(View.GONE);
            workTitle.setVisibility(View.GONE);
        }

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(mobilePhone == null){

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

        } else {

            phoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobilePhone));
                    startActivity(intent);
                }
            });

            smsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("address", mobilePhone);
                    startActivity(sendIntent);
                }
            });

        }

        setIntentButton();

    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_user_navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentContacts = new Intent(UserDetailsActivity.this, ContactsActivity.class);
                intentContacts.putExtra("AccessToken", accessToken);
                intentContacts.putExtra("userName", userName);
                intentContacts.putExtra("userEmail", userEmail);

                startActivity(intentContacts);

                UserDetailsActivity.this.finish();

                return true;

            case R.id.action_addUser:
                Intent intentAddContact = new Intent(UserDetailsActivity.this, AddContactActivity.class);
                intentAddContact.putExtra("AccessToken", accessToken);
                intentAddContact.putExtra("userName", userName);
                intentAddContact.putExtra("userEmail", userEmail);
                intentAddContact.putExtra("user", user);
                intentAddContact.putExtra("fromUserActivity", "yes");

                startActivity(intentAddContact);

                UserDetailsActivity.this.finish();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void setIntentButton() {

        if (mail == null) {

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

        } else {
            mailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent sendMail = new Intent(UserDetailsActivity.this, SendMailActivity.class);
                    sendMail.putExtra("AccessToken", accessToken);
                    sendMail.putExtra("userName", userName);
                    sendMail.putExtra("userEmail", userEmail);
                    sendMail.putExtra("emailAddress", mail);
                    sendMail.putExtra("user", user);
                    sendMail.putExtra("fromUserActivity", "yes");

                    startActivity(sendMail);

                    UserDetailsActivity.this.finish();
                }
            });


            calendarButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent addEventActivity = new Intent(UserDetailsActivity.this, AddEventActivity.class);
                    addEventActivity.putExtra("AccessToken", accessToken);
                    addEventActivity.putExtra("userName", userName);
                    addEventActivity.putExtra("userEmail", userEmail);
                    addEventActivity.putExtra("emailAddress", mail);
                    addEventActivity.putExtra("attendeeMail", mail);
                    addEventActivity.putExtra("attendeeName", displayName);
                    addEventActivity.putExtra("user", user);
                    addEventActivity.putExtra("fromUserActivity", "yes");

                    startActivity(addEventActivity);

                    UserDetailsActivity.this.finish();
                }
            });
        }

    }

    public void setMailClickListener(TextView textView, final String mail){


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendMail = new Intent(UserDetailsActivity.this, SendMailActivity.class);
                sendMail.putExtra("AccessToken", accessToken);
                sendMail.putExtra("userName", userName);
                sendMail.putExtra("emailAddress", mail);
                sendMail.putExtra("user", user);
                sendMail.putExtra("fromUserActivity", "yes");

                startActivity(sendMail);

                UserDetailsActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(UserDetailsActivity.this, ContactsActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        UserDetailsActivity.this.finish();
    }

}
