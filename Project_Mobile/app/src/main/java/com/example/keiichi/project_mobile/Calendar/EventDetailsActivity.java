package com.example.keiichi.project_mobile.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.Contacts.EditContactActivity;
import com.example.keiichi.project_mobile.R;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EventDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours",
            "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Free", "Working elsewhere", "Tentative", "Busy", "Away"};

    final private String URL_POSTADRESS = "https://graph.microsoft.com/beta/me/events/";
    private Toolbar myToolbar;
    private  AlertDialog.Builder builder;
    private TextView eventSubjectTextView;
    private TextView locationTextView;
    private TextView startDateTextView;
    private Spinner reminderSpinner;
    private Spinner displayAsSpinner;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String id;
    private String subject;
    private String location;
    private String startDate;
    private String displayAs;
    private String notes;
    private int startingValueReminder;
    private int startingValueDisplayAs;
    private int reminderMinutesBeforeStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventSubjectTextView = (TextView) findViewById(R.id.eventSubject);
        locationTextView = (TextView) findViewById(R.id.eventLocation);
        startDateTextView = (TextView) findViewById(R.id.startDate);
        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        displayAsSpinner = (Spinner) findViewById(R.id.displayAsSpinner);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        id= getIntent().getStringExtra("id");
        subject = getIntent().getStringExtra("subject");
        location = getIntent().getStringExtra("location");
        startDate = getIntent().getStringExtra("startDate");
        displayAs = getIntent().getStringExtra("displayAs");
        notes = getIntent().getStringExtra("notes");
        reminderMinutesBeforeStart = getIntent().getIntExtra("reminderMinutesBeforeStart", 0);

        System.out.println("hey boo " + notes);

        eventSubjectTextView.setText(subject);
        locationTextView.setText(location);
        startDateTextView.setText(startDate);

        reminderSpinner.setOnItemSelectedListener(this);
        displayAsSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterReminder = new ArrayAdapter<String>(this, R.layout.spinner_layout, REMINDERSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reminderSpinner.setAdapter(adapterReminder);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterDisplayAs = new ArrayAdapter<String>(this,R.layout.spinner_layout, DISPLAYASSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displayAsSpinner.setAdapter(adapterDisplayAs);

        switch(reminderMinutesBeforeStart){
            case 0:
                startingValueReminder = adapterReminder.getPosition("0 Minutes");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 15:
                startingValueReminder = adapterReminder.getPosition("15 Minutes");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 30:
                startingValueReminder = adapterReminder.getPosition("30 Minutes");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 45:
                startingValueReminder = adapterReminder.getPosition("45 Minutes");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 60:
                startingValueReminder = adapterReminder.getPosition("1 Hour");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 90:
                startingValueReminder = adapterReminder.getPosition("90 Minutes");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 120:
                startingValueReminder = adapterReminder.getPosition("2 Hours");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 180:
                startingValueReminder = adapterReminder.getPosition("3 Hours");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 240:
                startingValueReminder = adapterReminder.getPosition("4 Hours");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 480:
                startingValueReminder = adapterReminder.getPosition("8 Hours");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 720:
                startingValueReminder = adapterReminder.getPosition("12 Hours");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 1440:
                startingValueReminder = adapterReminder.getPosition("1 Day");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 2880:
                startingValueReminder = adapterReminder.getPosition("2 Days");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 4320:
                startingValueReminder = adapterReminder.getPosition("3 Days");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 10080:
                startingValueReminder = adapterReminder.getPosition("1 Week");
                reminderSpinner.setSelection(startingValueReminder);
                break;

            case 20160:
                startingValueReminder = adapterReminder.getPosition("2 Weeks");
                reminderSpinner.setSelection(startingValueReminder);
                break;
        }

        switch(displayAs){
            case "free":
                startingValueDisplayAs = adapterDisplayAs.getPosition("Free");
                displayAsSpinner.setSelection(startingValueDisplayAs);
                break;

            case "workingElsewhere":
                startingValueDisplayAs = adapterDisplayAs.getPosition("Working elsewhere");
                displayAsSpinner.setSelection(startingValueDisplayAs);
                break;

            case "tentative":
                startingValueDisplayAs = adapterDisplayAs.getPosition("Tentative");
                displayAsSpinner.setSelection(startingValueDisplayAs);
                break;

            case "busy":
                startingValueDisplayAs = adapterDisplayAs.getPosition("Busy");
                displayAsSpinner.setSelection(startingValueDisplayAs);
                break;

            case "oof":
                startingValueDisplayAs = adapterDisplayAs.getPosition("Away");
                displayAsSpinner.setSelection(startingValueDisplayAs);
                break;

        }

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        builder = new AlertDialog.Builder(EventDetailsActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Delete Event");
        builder.setMessage("Are you sure you want to delete this event?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    deleteEvent();

                    int DELAY_TIME=2000;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentListEvents = new Intent(EventDetailsActivity.this, ListEventsActivity.class);
                            intentListEvents.putExtra("AccessToken", accessToken);
                            intentListEvents.putExtra("userName", userName);
                            intentListEvents.putExtra("userEmail", userEmail);
                            intentListEvents.putExtra("subject", subject);
                            intentListEvents.putExtra("location", location);
                            intentListEvents.putExtra("startDate", startDate);
                            intentListEvents.putExtra("displayAs", displayAs);
                            intentListEvents.putExtra("notes", notes);
                            intentListEvents.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);

                            startActivity(intentListEvents);


                        }
                    }, DELAY_TIME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentListEvents = new Intent(EventDetailsActivity.this, ListEventsActivity.class);
                intentListEvents.putExtra("AccessToken", accessToken);
                intentListEvents.putExtra("userName", userName);
                intentListEvents.putExtra("userEmail", userEmail);
                intentListEvents.putExtra("subject", subject);
                intentListEvents.putExtra("location", location);
                intentListEvents.putExtra("startDate", startDate);
                intentListEvents.putExtra("displayAs", displayAs);
                intentListEvents.putExtra("notes", notes);
                intentListEvents.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);

                startActivity(intentListEvents);

                return true;

            case R.id.action_delete:

                builder.show();

                return true;

            case R.id.action_edit:
                Intent intentEditEvent = new Intent(EventDetailsActivity.this, EditEventActivity.class);
                intentEditEvent.putExtra("AccessToken", accessToken);
                intentEditEvent.putExtra("userName", userName);
                intentEditEvent.putExtra("userEmail", userEmail);
                intentEditEvent.putExtra("subject", subject);
                intentEditEvent.putExtra("location", location);
                intentEditEvent.putExtra("startDate", startDate);
                intentEditEvent.putExtra("displayAs", displayAs);
                intentEditEvent.putExtra("notes", notes);
                intentEditEvent.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);

                startActivity(intentEditEvent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // PATCH REQUEST VOOR DELETEN EVENT
    private void deleteEvent() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        String postAddress = URL_POSTADRESS + id;

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
