package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.AddContactActivity;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Attendee;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.Mail.RecyclerTouchListener;
import com.example.keiichi.project_mobile.Mail.ReplyToMailActivity;
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

public class AttendeeActivity extends AppCompatActivity {

    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts?$orderBy=displayName&$top=500&$count=true";
    private List<Contact> contacts = new ArrayList<>();
    private List<EmailAddress> emailList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private Toolbar myToolbar;
    private ListView contactsListView;
    private SearchView searchView;
    private RecyclerView contactsRecyclerView;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String firstTime;
    private String eventSubject;
    private String eventLocation;
    private String eventShowAs;
    private String eventNotes;
    private String fromEdit;
    private String subject;
    private String location;
    private String reminderMinutesBeforeStart;
    private String displayAs;
    private String notes;
    private String fromContactDetailsActivity;
    private String fromRoomActivity;
    private String fromUserActivity;
    private String sensitivity;
    private String startDate;
    private String id;
    private String contentType;
    private String eventId;
    private int eventDayOfMonth;
    private int eventMonth;
    private int eventYear;
    private int eventDuration;
    private int eventHour;
    private int eventMinute;
    private int eventReminderMinutesBeforeStart;
    private boolean eventIsPrivate;
    private boolean eventRequestResponses;
    private boolean responseRequested;
    private boolean isPrivate;
    private Event event;

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);

        eventId = getIntent().getStringExtra("eventId");
        event = (Event) getIntent().getSerializableExtra("event");
        fromContactDetailsActivity = getIntent().getStringExtra("fromContactDetailsActivity");
        fromRoomActivity = getIntent().getStringExtra("fromRoomActivity");
        fromUserActivity = getIntent().getStringExtra("fromUserActivity");
        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        firstTime = getIntent().getStringExtra("firstTime");
        eventSubject = getIntent().getStringExtra("eventSubject");
        eventLocation = getIntent().getStringExtra("eventLocation");
        eventDayOfMonth = getIntent().getIntExtra("eventDayOfMonth", 0);
        eventMonth = getIntent().getIntExtra("eventMonth", 0);
        eventYear = getIntent().getIntExtra("eventYear", 0);
        eventHour = getIntent().getIntExtra("eventHour", 0);
        eventMinute = getIntent().getIntExtra("eventMinute", 0);
        eventDuration = getIntent().getIntExtra("eventDuration", 0);
        eventReminderMinutesBeforeStart = getIntent().getIntExtra("eventReminderMinutesBeforeStart", 0);
        eventShowAs = getIntent().getStringExtra("eventShowAs");
        eventNotes = getIntent().getStringExtra("eventNotes");
        eventIsPrivate = getIntent().getBooleanExtra("eventIsPrivate", false);
        eventRequestResponses = getIntent().getBooleanExtra("eventRequestResponses", false);
        fromEdit = getIntent().getStringExtra("fromEdit");
        subject = getIntent().getStringExtra("subject");
        location = getIntent().getStringExtra("location");
        reminderMinutesBeforeStart = getIntent().getStringExtra("reminderMinutesBeforeStart");
        displayAs = getIntent().getStringExtra("displayAs");
        notes = getIntent().getStringExtra("notes");
        sensitivity = getIntent().getStringExtra("sensitivity");
        responseRequested = getIntent().getBooleanExtra("responseRequested", false);
        startDate = getIntent().getStringExtra("startDate");
        isPrivate = getIntent().getBooleanExtra("isPrivate", false);
        id = getIntent().getStringExtra("id");
        contentType = getIntent().getStringExtra("contentType");

        System.out.println("from contact details: " + fromContactDetailsActivity);
        System.out.println("from room: " + fromRoomActivity);
        System.out.println("from user: " + fromUserActivity);

        if(firstTime != null){
            emailList = (List<EmailAddress>)getIntent().getSerializableExtra("emailList");

        }

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        callGraphAPI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_attendees, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search by name...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                contactAdapter.getFilter().filter(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contactAdapter.getFilter().filter(s);

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

                if(fromEdit != null){

                    Intent intentEditEvent = new Intent(AttendeeActivity.this, EditEventActivity.class);
                    intentEditEvent.putExtra("userEmail", userEmail);
                    intentEditEvent.putExtra("AccessToken", accessToken);
                    intentEditEvent.putExtra("userName", userName);
                    intentEditEvent.putExtra("fromAttendeesActivity", "yes");
                    intentEditEvent.putExtra("emailList",(Serializable) emailList);
                    intentEditEvent.putExtra("event", event);
                    intentEditEvent.putExtra("eventId", eventId);

                    startActivity(intentEditEvent);

                    AttendeeActivity.this.finish();

                } else {

                    Intent intentAddEvent = new Intent(AttendeeActivity.this, AddEventActivity.class);
                    intentAddEvent.putExtra("AccessToken", accessToken);
                    intentAddEvent.putExtra("userName", userName);
                    intentAddEvent.putExtra("userEmail", userEmail);
                    intentAddEvent.putExtra("fromAttendeesActivity", "yes");
                    intentAddEvent.putExtra("eventSubject", eventSubject);
                    intentAddEvent.putExtra("eventLocation", eventLocation);
                    intentAddEvent.putExtra("eventDayOfMonth", eventDayOfMonth);
                    intentAddEvent.putExtra("eventMonth", eventMonth);
                    intentAddEvent.putExtra("eventYear", eventYear);
                    intentAddEvent.putExtra("eventHour", eventHour);
                    intentAddEvent.putExtra("eventMinute", eventMinute);
                    intentAddEvent.putExtra("eventDuration", eventDuration);
                    intentAddEvent.putExtra("eventShowAs", eventShowAs);
                    intentAddEvent.putExtra("eventNotes", eventNotes);
                    intentAddEvent.putExtra("eventIsPrivate", eventIsPrivate);
                    intentAddEvent.putExtra("eventRequestResponses", eventRequestResponses);
                    intentAddEvent.putExtra("eventReminderMinutesBeforeStart", eventReminderMinutesBeforeStart);
                    intentAddEvent.putExtra("emailList",(Serializable) emailList);
                    intentAddEvent.putExtra("id", id);
                    intentAddEvent.putExtra("contentType", contentType);

                    intentAddEvent.putExtra("fromContactDetailsActivity", fromContactDetailsActivity);
                    intentAddEvent.putExtra("fromRoomActivity", fromRoomActivity);
                    intentAddEvent.putExtra("fromUserActivity", fromUserActivity);

                    startActivity(intentAddEvent);

                    AttendeeActivity.this.finish();

                }


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");
        Log.d(TAG, accessToken);

    /* Make sure we have a token to send to graph */
        if (accessToken == null) {
            Intent logout = new Intent(AttendeeActivity.this, MainActivity.class);
            logout.putExtra("AccessToken", accessToken);
            logout.putExtra("userName", userName);
            logout.putExtra("userEmail", userEmail);

            startActivity(logout);

            AttendeeActivity.this.finish();
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
        JSONArray contactsJsonArray = null;

        // Haal de contacten binnen
        try {
            contactsJsonArray = (JSONArray) graphResponse.get("value");

            JSONObject contactList = graphResponse;

            JSONArray contactArray = contactList.getJSONArray("value");

            System.out.println("test response: " + contactArray);


            // VUL POJO
            Type listType = new TypeToken<List<Contact>>() {
            }.getType();

            contacts = new Gson().fromJson(String.valueOf(contactArray), listType);

            contactAdapter = new ContactAdapter(this, contacts);
            contactsRecyclerView.setAdapter(contactAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert contactsJsonArray != null;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        contactsRecyclerView.setLayoutManager(manager);
        contactsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        contactAdapter = new ContactAdapter(this, contacts);
        contactsRecyclerView.setAdapter(contactAdapter);

        contactsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), contactsRecyclerView, new ListMailsActvity.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(contacts.size() != 0){

                    Contact contact = contacts.get(position);

                    List<EmailAddress> email = contact.getEmailAddresses();

                    if(email.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No email found for this contact!", Toast.LENGTH_SHORT).show();
                    } else{

                        if(fromEdit != null){

                            Intent intentEditEvent = new Intent(AttendeeActivity.this, EditEventActivity.class);

                            intentEditEvent.putExtra("userEmail", userEmail);
                            intentEditEvent.putExtra("AccessToken", accessToken);
                            intentEditEvent.putExtra("userName", userName);
                            intentEditEvent.putExtra("fromAttendeesActivity", "yes");
                            intentEditEvent.putExtra("event", event);
                            intentEditEvent.putExtra("eventId", eventId);

                            if(contact.getEmailAddresses() != null){

                                EmailAddress emailContact = contact.getEmailAddresses().get(0);

                                emailList.add(emailContact);

                                intentEditEvent.putExtra("emailList",(Serializable) emailList);
                            }


                            startActivity(intentEditEvent);

                        } else {

                            Intent intentAddEvent = new Intent(AttendeeActivity.this, AddEventActivity.class);

                            intentAddEvent.putExtra("userEmail", userEmail);
                            intentAddEvent.putExtra("AccessToken", accessToken);
                            intentAddEvent.putExtra("userName", userName);
                            intentAddEvent.putExtra("fromAttendeesActivity", "yes");
                            intentAddEvent.putExtra("eventSubject", eventSubject);
                            intentAddEvent.putExtra("eventLocation", eventLocation);
                            intentAddEvent.putExtra("eventDayOfMonth", eventDayOfMonth);
                            intentAddEvent.putExtra("eventMonth", eventMonth);
                            intentAddEvent.putExtra("eventYear", eventYear);
                            intentAddEvent.putExtra("eventHour", eventHour);
                            intentAddEvent.putExtra("eventMinute", eventMinute);
                            intentAddEvent.putExtra("eventDuration", eventDuration);
                            intentAddEvent.putExtra("eventShowAs", eventShowAs);
                            intentAddEvent.putExtra("eventNotes", eventNotes);
                            intentAddEvent.putExtra("eventIsPrivate", eventIsPrivate);
                            intentAddEvent.putExtra("eventRequestResponses", eventRequestResponses);
                            intentAddEvent.putExtra("eventReminderMinutesBeforeStart", eventReminderMinutesBeforeStart);
                            intentAddEvent.putExtra("id", id);
                            intentAddEvent.putExtra("contentType", contentType);

                            intentAddEvent.putExtra("fromContactDetailsActivity", fromContactDetailsActivity);
                            intentAddEvent.putExtra("fromRoomActivity", fromRoomActivity);
                            intentAddEvent.putExtra("fromUserActivity", fromUserActivity);

                            if(contact.getEmailAddresses() != null){

                                EmailAddress emailContact = contact.getEmailAddresses().get(0);

                                emailList.add(emailContact);

                                intentAddEvent.putExtra("emailList",(Serializable) emailList);
                            }


                            startActivity(intentAddEvent);

                            AttendeeActivity.this.finish();

                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Empty contact list!", Toast.LENGTH_SHORT).show();
                }

                }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onBackPressed(){

        minimizeApp();

    }

    public void minimizeApp() {

        if(fromEdit != null){

            Intent intentEditEvent = new Intent(AttendeeActivity.this, EditEventActivity.class);
            intentEditEvent.putExtra("userEmail", userEmail);
            intentEditEvent.putExtra("AccessToken", accessToken);
            intentEditEvent.putExtra("userName", userName);
            intentEditEvent.putExtra("fromAttendeesActivity", "yes");
            intentEditEvent.putExtra("emailList",(Serializable) emailList);
            intentEditEvent.putExtra("event", event);
            intentEditEvent.putExtra("eventId", eventId);


            startActivity(intentEditEvent);

            AttendeeActivity.this.finish();

        } else {

            Intent intentAddEvent = new Intent(AttendeeActivity.this, AddEventActivity.class);
            intentAddEvent.putExtra("AccessToken", accessToken);
            intentAddEvent.putExtra("userName", userName);
            intentAddEvent.putExtra("userEmail", userEmail);
            intentAddEvent.putExtra("fromAttendeesActivity", "yes");
            intentAddEvent.putExtra("eventSubject", eventSubject);
            intentAddEvent.putExtra("eventLocation", eventLocation);
            intentAddEvent.putExtra("eventDayOfMonth", eventDayOfMonth);
            intentAddEvent.putExtra("eventMonth", eventMonth);
            intentAddEvent.putExtra("eventYear", eventYear);
            intentAddEvent.putExtra("eventHour", eventHour);
            intentAddEvent.putExtra("eventMinute", eventMinute);
            intentAddEvent.putExtra("eventDuration", eventDuration);
            intentAddEvent.putExtra("eventShowAs", eventShowAs);
            intentAddEvent.putExtra("eventNotes", eventNotes);
            intentAddEvent.putExtra("eventIsPrivate", eventIsPrivate);
            intentAddEvent.putExtra("eventRequestResponses", eventRequestResponses);
            intentAddEvent.putExtra("eventReminderMinutesBeforeStart", eventReminderMinutesBeforeStart);
            intentAddEvent.putExtra("emailList",(Serializable) emailList);
            intentAddEvent.putExtra("id", id);
            intentAddEvent.putExtra("contentType", contentType);

            intentAddEvent.putExtra("fromContactDetailsActivity", fromContactDetailsActivity);
            intentAddEvent.putExtra("fromRoomActivity", fromRoomActivity);
            intentAddEvent.putExtra("fromUserActivity", fromUserActivity);

            startActivity(intentAddEvent);

            AttendeeActivity.this.finish();

        }

    }

}
