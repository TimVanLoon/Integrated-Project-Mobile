package com.example.keiichi.project_mobile.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
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
import com.example.keiichi.project_mobile.DAL.POJOs.Attendee;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.R;
import com.example.keiichi.project_mobile.Utility;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EventDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", "2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours",
            "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Free", "Working elsewhere", "Tentative", "Busy", "Away"};
    final private String URL_POSTADRESS = "https://graph.microsoft.com/beta/me/events/";
    private List<Attendee> attendees;
    private Toolbar myToolbar;
    private  AlertDialog.Builder builder;
    private TextView eventSubjectTextView;
    private TextView locationTextView;
    private TextView startDateTextView;
    private TextView notesTextViewTitle;
    private TextView attendeesTitle;
    private Spinner reminderSpinner;
    private Spinner displayAsSpinner;
    private CheckBox privateCheckbox;
    private CheckBox responseCheckbox;
    private ListView attendeeList;
    private WebView notesWebView;
    private AttendeeAdapter attendeeAdapter;
    private boolean responseRequested;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String id;
    private String subject;
    private String location;
    private String startDate;
    private String displayAs;
    private String notes;
    private String sensitivity;
    private String contentType;
    private String eventId;
    private int startingValueReminder;
    private int startingValueDisplayAs;
    private int reminderMinutesBeforeStart;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventSubjectTextView = (TextView) findViewById(R.id.eventSubject);
        locationTextView = (TextView) findViewById(R.id.eventLocation);
        startDateTextView = (TextView) findViewById(R.id.startDate);
        attendeesTitle = (TextView) findViewById(R.id.attendeesTitle);
        notesTextViewTitle = (TextView) findViewById(R.id.notesTextViewTitle);
        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        displayAsSpinner = (Spinner) findViewById(R.id.displayAsSpinner);
        privateCheckbox = (CheckBox) findViewById(R.id.privateCheckbox);
        responseCheckbox = (CheckBox) findViewById(R.id.responseCheckbox);
        attendeeList = (ListView) findViewById(R.id.attendeeList);
        notesWebView = (WebView) findViewById(R.id.notesWebView);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        eventId = getIntent().getStringExtra("eventId");
        event = (Event) getIntent().getSerializableExtra("event");
        subject = event.getSubject();
        location = event.getLocation().getDisplayName();
        startDate = event.getStart().getDateTime();
        displayAs = event.getShowAs();
        contentType = event.getBody().getContentType();
        notes = event.getBody().getContent();
        sensitivity = event.getSensitivity();
        responseRequested = event.isResponseRequested();
        reminderMinutesBeforeStart = event.getReminderMinutesBeforeStart();
        attendees = event.getAttendees();

        if(notes != null && !notes.equals("")){

            notesWebView.setPadding(0,0,0,0);

            notesWebView.setInitialScale(1);

            //setupWebView();

            if(contentType.equals("html")){

                notesWebView.getSettings().setJavaScriptEnabled(true);
                notesWebView.getSettings().setLoadWithOverviewMode(true);
                notesWebView.getSettings().setUseWideViewPort(true);
                notesWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                notesWebView.setScrollbarFadingEnabled(false);
                notesWebView.loadDataWithBaseURL("", notes, "text/html", "utf-8","");

            } else{

                notesWebView.loadDataWithBaseURL("", notes, "text", "utf-8","");

            }

        } else {

            notesWebView.setVisibility(View.GONE);
            notesTextViewTitle.setVisibility(View.GONE);

        }

        if(responseRequested){
            responseCheckbox.setChecked(true);
        } else {
            responseCheckbox.setChecked(false);
        }

        switch(sensitivity){
            case "normal":
                privateCheckbox.setChecked(false);
                break;

            case "personal":
                privateCheckbox.setChecked(false);
                break;

            case "private":
                privateCheckbox.setChecked(true);
                break;

            case "confidential":
                privateCheckbox.setChecked(false);
                break;
        }

        if(attendees != null){

            if(!attendees.isEmpty()){

                attendeeAdapter = new AttendeeAdapter(this, attendees);
                attendeeList.setAdapter(attendeeAdapter);
                Utility.setListViewHeightBasedOnChildren(attendeeList);

            } else {
                attendeesTitle.setVisibility(View.GONE);
            }
        } else {
            attendeesTitle.setVisibility(View.GONE);
        }



        eventSubjectTextView.setText(subject);
        locationTextView.setText(location);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date d = null;
        try {
            d = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDateTextView.setText(output.format(d));

        //startDateTextView.setText(startDate);

        reminderSpinner.setOnItemSelectedListener(this);
        reminderSpinner.setClickable(false);
        displayAsSpinner.setOnItemSelectedListener(this);
        displayAsSpinner.setClickable(false);

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

                            startActivity(intentListEvents);

                            EventDetailsActivity.this.finish();


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
                intentListEvents.putExtra("id", id);
                intentListEvents.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);
                intentListEvents.putExtra("contentType", contentType);

                startActivity(intentListEvents);

                EventDetailsActivity.this.finish();

                return true;

            case R.id.action_delete:

                builder.show();

                return true;

            case R.id.action_edit:
                Intent intentEditEvent = new Intent(EventDetailsActivity.this, EditEventActivity.class);
                intentEditEvent.putExtra("AccessToken", accessToken);
                intentEditEvent.putExtra("userName", userName);
                intentEditEvent.putExtra("userEmail", userEmail);
                intentEditEvent.putExtra("eventId", eventId);
                intentEditEvent.putExtra("event", event);

                startActivity(intentEditEvent);

                EventDetailsActivity.this.finish();

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

        String postAddress = URL_POSTADRESS + eventId;

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

    @Override
    public void onBackPressed(){

        minimizeApp();

    }

    public void minimizeApp() {

        Intent intentListEvents = new Intent(EventDetailsActivity.this, ListEventsActivity.class);
        intentListEvents.putExtra("AccessToken", accessToken);
        intentListEvents.putExtra("userName", userName);
        intentListEvents.putExtra("userEmail", userEmail);

        startActivity(intentListEvents);

        EventDetailsActivity.this.finish();

    }

}
