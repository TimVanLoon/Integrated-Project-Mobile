package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListEventsActivity extends AppCompatActivity {

    private ListView eventsListView;
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        eventsListView = (ListView) findViewById(R.id.eventsListView);

        Intent intent = getIntent();
        String eventsArrayInString = intent.getStringExtra("EventsArray");
       // Bundle args = intent.getBundleExtra("eventList");
        //events = (ArrayList<Event>) args.getSerializable("eventsArrayList");

        try {
            JSONArray eventsArray = new JSONArray(eventsArrayInString);
            fillEventsListView(eventsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillEventsListView(JSONArray eventsArray){

        EventAdapter eventAdapter = new EventAdapter(this, eventsArray);
        eventsListView.setAdapter(eventAdapter);


    }
}
