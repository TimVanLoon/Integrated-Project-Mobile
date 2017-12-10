package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;

public class ListEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        Intent intent = getIntent();
        String eventsArrayInString = intent.getStringExtra("EventsArray");

        try {
            JSONArray eventsArray = new JSONArray(eventsArrayInString);
            fillEventsListView(eventsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillEventsListView(JSONArray eventsArray){

    }
}
