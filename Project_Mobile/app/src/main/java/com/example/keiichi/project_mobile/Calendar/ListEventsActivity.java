package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.keiichi.project_mobile.Contacts.AddContactActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListEventsActivity extends AppCompatActivity {

    private String accessToken;
    private String userName;
    private String userEmail;
    private Toolbar myToolbar;
    private SearchView searchView;
    private ListView eventsListView;
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        eventsListView = (ListView) findViewById(R.id.eventsListView);

        setSupportActionBar(myToolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_contacts, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);



        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search calendar...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){



            case R.id.action_add:
                Intent intentAddEvent = new Intent(ListEventsActivity.this, AddEventActivity.class);
                intentAddEvent.putExtra("AccessToken", accessToken);
                intentAddEvent.putExtra("userName", userName);
                intentAddEvent.putExtra("userEmail", userEmail);

                startActivity(intentAddEvent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
