package com.example.keiichi.project_mobile.Contacts;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.keiichi.project_mobile.R;

public class ContactsDetailsActivity extends AppCompatActivity {

    private String accessToken;
    private String userName;
    private String userEmail;
    private String givenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        givenName = getIntent().getStringExtra("givenName");

        TextView headerUserName = (TextView) findViewById(R.id.headerUserName);
        headerUserName.setText(givenName);
    }
}
