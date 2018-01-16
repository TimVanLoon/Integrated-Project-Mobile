package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.AddContactActivity;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEventsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();

    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/events?$orderby=start/dateTime&$top=500&$count=true";
    final private String URL_DELETE = "https://graph.microsoft.com/beta/me/events/";

    private String accessToken;
    private String userName;
    private String userEmail;
    private Toolbar myToolbar;
    private SearchView searchView;
    private ListView eventsListView;
    private List<Event> events = new ArrayList<>();
    private EventAdapter eventAdapter;
    private  BottomNavigationView mBottomNav;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean actionModeEnabled = false;
    private int eventsClickedCount = 0;
    private List<Event> selectedEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        eventsListView = (ListView) findViewById(R.id.eventsListView);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);

                onEventClicked(position);

            }
        });

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.action_calendar:

                        break;
                    case R.id.action_mail:
                        Intent intentMail = new Intent(ListEventsActivity.this, ListMailsActvity.class);
                        intentMail.putExtra("AccessToken", accessToken);
                        intentMail.putExtra("userName", userName);
                        intentMail.putExtra("userEmail", userEmail);

                        startActivity(intentMail);

                        ListEventsActivity.this.finish();

                        break;
                    case R.id.action_user:
                        Intent intentContacts = new Intent(ListEventsActivity.this, ContactsActivity.class);
                        intentContacts.putExtra("AccessToken", accessToken);
                        intentContacts.putExtra("userName", userName);
                        intentContacts.putExtra("userEmail", userEmail);

                        startActivity(intentContacts);

                        ListEventsActivity.this.finish();

                        break;

                }

                return false;
            }
        });

        eventsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        eventsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean checked) {
                if(checked) {
                    eventsListView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                    selectedEvents.add(eventAdapter.getItemAtPosition(position));
                    eventsClickedCount++;
                    actionMode.setTitle(eventsClickedCount+ " Selected");
                } else {
                    selectedEvents.remove(eventAdapter.getItemAtPosition(position));
                    eventsClickedCount--;
                    actionMode.setTitle(eventsClickedCount+ " Selected");
                    eventsListView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.delete_navigation, menu);
                actionModeEnabled = true;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_delete){
                    for(Event event : selectedEvents){
                        try {
                            deleteEvent(event.getId());
                            getEvents();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getEvents();
                    actionMode.finish();
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                eventsClickedCount = 0;
                selectedEvents.clear();
            }
        });

        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");


        getEvents();
    }

    private void fillEventsListView(List<Event> eventsList){

        EventAdapter eventAdapter = new EventAdapter(this, eventsList);
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
                eventAdapter.getFilter().filter(s);

                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentCalendar = new Intent(ListEventsActivity.this, CalendarActivity.class);
                intentCalendar.putExtra("AccessToken", accessToken);
                intentCalendar.putExtra("userName", userName);
                intentCalendar.putExtra("userEmail", userEmail);

                startActivity(intentCalendar);

                ListEventsActivity.this.finish();

                return true;


            case R.id.action_add:
                Intent intentAddEvent = new Intent(ListEventsActivity.this, AddEventActivity.class);
                intentAddEvent.putExtra("AccessToken", accessToken);
                intentAddEvent.putExtra("userName", userName);
                intentAddEvent.putExtra("userEmail", userEmail);

                startActivity(intentAddEvent);

                ListEventsActivity.this.finish();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void getEvents() {
        Log.d(TAG, "Starting volley request to graph");
        Log.d(TAG, accessToken);

    /* Make sure we have a token to send to graph */
        if (accessToken == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                try {
                    updateGraphUI(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    /* Sets the Graph response */
    private void updateGraphUI(JSONObject graphResponse) throws JSONException {

        // Test de response
        JSONArray eventsJsonArray = null;

        // Haal de events binnen
        try {

            JSONObject eventList = graphResponse;

            JSONArray eventArray = eventList.getJSONArray("value");


            // VUL POJO
            Type listType = new TypeToken<List<Event>>() {
            }.getType();

            events = new Gson().fromJson(String.valueOf(eventArray), listType);


            eventAdapter = new EventAdapter(this, events);
            eventsListView.setAdapter(eventAdapter);






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // PATCH REQUEST VOOR DELETEN EVENT
    private void deleteEvent(String eventId) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        String postAddress = URL_DELETE + eventId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Event deleted!", Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };

        queue.add(stringRequest);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        getEvents();

        swipeRefreshLayout.setRefreshing(false);

    }

    public void onEventClicked(int position){


        if(events.size() != 0){

            Event event = eventAdapter.getItemAtPosition(position);

            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
            showEventDetails.putExtra("userEmail", userEmail);
            showEventDetails.putExtra("AccessToken", accessToken);
            showEventDetails.putExtra("userName", userName);
            showEventDetails.putExtra("id", event.getId());
            showEventDetails.putExtra("subject", event.getSubject());
            showEventDetails.putExtra("reminderMinutesBeforeStart", event.getReminderMinutesBeforeStart());
            showEventDetails.putExtra("displayAs", event.getShowAs());

            if(event.getLocation() == null){
                showEventDetails.putExtra("location", "");
            }
            else {
                showEventDetails.putExtra("location", event.getLocation().getDisplayName());
            }

            if(event.getStart() == null){
                showEventDetails.putExtra("startDate", "");
            }
            else {
                showEventDetails.putExtra("startDate", event.getStart().getDateTime());
            }

            if(event.getBody().getContent() != null){
                showEventDetails.putExtra("notes", event.getBody().getContent());
            }
            else {
                showEventDetails.putExtra("notes", "");
            }

            if(event.getBody().getContentType() != null){

                showEventDetails.putExtra("contentType", event.getBody().getContentType());

            }

            String eventBody = event.getBody().getContent();

            if(eventBody != null){
                showEventDetails.putExtra("notes", eventBody);
            }
            else {
                showEventDetails.putExtra("notes", "");
            }

            showEventDetails.putExtra("sensitivity", event.getSensitivity());

            showEventDetails.putExtra("responseRequested", event.isResponseRequested());

            if (event.getAttendees() != null) {
                showEventDetails.putExtra("attendeesList", (Serializable) event.getAttendees());
            }

            startActivity(showEventDetails);

            ListEventsActivity.this.finish();


        } else {
            Toast.makeText(getApplicationContext(), "Empty events list!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(ListEventsActivity.this, CalendarActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        ListEventsActivity.this.finish();
    }

}
