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
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.User;
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
        firstName.setText(givenName);
        lastName.setText(surName);

        if(mail != null){
            userMail.setText(mail);
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


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
