package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

public class ListEventsActivity extends AppCompatActivity {

    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        eventsListView = (ListView) findViewById(R.id.eventsListView);

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

        MailAdapter mailAdapter = new MailAdapter(this, eventsArray);
        eventsListView.setAdapter(mailAdapter);
        final JSONArray finalMailJsonArray = eventsArray;
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent showMail = new Intent(ListEventsActivity.this, DisplayMailActivity.class);
                try {
                    showMail.putExtra("mailObjext", finalMailJsonArray.getString(position));
                    showMail.putExtra("accestoken", authResult.getAccessToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(showMail);
            }
        });
    }
}
