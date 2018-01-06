package com.example.keiichi.project_mobile.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.keiichi.project_mobile.R;

public class AttendeeActivity extends AppCompatActivity {

    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts?$orderBy=displayName&$top=500&$count=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);
    }
}
