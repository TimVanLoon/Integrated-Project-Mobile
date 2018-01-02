package com.example.keiichi.project_mobile.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.DAL.POJOs.DateTimeTimeZone;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.DAL.POJOs.Location;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/events";

    private String [] DURATIONSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "Entire day"};
    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours",
                                                "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Available", "Works somewhere else", "For the time being", "Busy", "Absent"};
    private String [] REPEATSPINNERLIST = {"Never", "Each day", "Every sunday", "Every workday", "Day 31 of every month", "Ever last sunday", "Every 31st of december"};

    private int startingValue;
    private Button moreDetailsButton;
    private EditText dateEvent;
    private EditText timeEvent;
    private EditText eventInput;
    private EditText locationInput;
    private EditText personalNotes;
    private TextView reminderTitle;
    private TextView displayAsTitle;
    private TextView repeatTitle;
    private TextView notesTitle;
    private String userName;
    private String userEmail;
    private String accessToken;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private Spinner displayAsSpinner;
    private Spinner repeatSpinner;
    DatePickerDialog datePickerDialog;
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTONN TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // INITIALISEER DE INPUT FIELDS
        dateEvent = (EditText) findViewById(R.id.dateEvent);
        timeEvent = (EditText) findViewById(R.id.timeEvent);
        eventInput = (EditText) findViewById(R.id.eventInput);
        locationInput = (EditText) findViewById(R.id.locationInput);
        personalNotes = (EditText) findViewById(R.id.personalNotes);
        reminderTitle = (TextView) findViewById(R.id.reminderTitle);
        displayAsTitle = (TextView) findViewById(R.id.displayAsTitle);
        repeatTitle = (TextView) findViewById(R.id.repeatTitle);
        notesTitle = (TextView) findViewById(R.id.notesTitle);
        durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        displayAsSpinner = (Spinner) findViewById(R.id.displayAsSpinner);
        moreDetailsButton = (Button) findViewById(R.id.moreDetailsButton);

        reminderSpinner.setVisibility(View.GONE);
        repeatSpinner.setVisibility(View.GONE);
        displayAsSpinner.setVisibility(View.GONE);
        notesTitle.setVisibility(View.GONE);
        repeatTitle.setVisibility(View.GONE);
        displayAsTitle.setVisibility(View.GONE);
        reminderTitle.setVisibility(View.GONE);
        personalNotes.setVisibility(View.GONE);



        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moreDetailsButton.setVisibility(View.GONE);
                reminderSpinner.setVisibility(View.VISIBLE);
                repeatSpinner.setVisibility(View.VISIBLE);
                displayAsSpinner.setVisibility(View.VISIBLE);
                notesTitle.setVisibility(View.VISIBLE);
                repeatTitle.setVisibility(View.VISIBLE);
                displayAsTitle.setVisibility(View.VISIBLE);
                reminderTitle.setVisibility(View.VISIBLE);
                personalNotes.setVisibility(View.VISIBLE);

            }
        });

        durationSpinner.setOnItemSelectedListener(this);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterDuration = new ArrayAdapter<String>(this, R.layout.spinner_layout, DURATIONSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        durationSpinner.setAdapter(adapterDuration);
        startingValue = adapterDuration.getPosition("1 Hour");
        durationSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterReminder = new ArrayAdapter<String>(this, R.layout.spinner_layout, REMINDERSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reminderSpinner.setAdapter(adapterReminder);
        startingValue = adapterReminder.getPosition("15 Minutes");
        reminderSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterDisplayAs = new ArrayAdapter<String>(this,R.layout.spinner_layout, DISPLAYASSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displayAsSpinner.setAdapter(adapterDisplayAs);
        startingValue = adapterDisplayAs.getPosition("Busy");
        displayAsSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterRepeat = new ArrayAdapter<String>(this, R.layout.spinner_layout, REPEATSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repeatSpinner.setAdapter(adapterRepeat);
        startingValue = adapterRepeat.getPosition("Never");
        repeatSpinner.setSelection(startingValue);

        // ZET CLICK EVENT OP DE DATE INPUT
        dateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateEvent.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        timeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        /*
                        String finalHour = "";
                        String finalMinutes = "";

                        if(selectedHour <10) {
                            finalHour ="0"+ selectedHour;
                        }
                        else if(selectedMinute<10){
                            finalMinutes ="0"+selectedMinute;
                        }

                        timeEvent.setText(finalHour + ":" + finalMinutes);
                        */
                        timeEvent.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_navigation, menu);


        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentCalendar = new Intent(AddEventActivity.this, CalendarActivity.class);
                intentCalendar.putExtra("AccessToken", accessToken);
                intentCalendar.putExtra("userName", userName);
                intentCalendar.putExtra("userEmail", userEmail);
                startActivity(intentCalendar);

                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                try {
                    saveEvent();

                    int DELAY_TIME=2000;

                    //start your animation
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentContacts = new Intent(AddEventActivity.this, CalendarActivity.class);
                            intentContacts.putExtra("AccessToken", accessToken);
                            startActivity(intentContacts);
                        }
                    }, DELAY_TIME);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // POST REQUEST VOOR NIEWE EVENT
    private void saveEvent() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);


        Event event = new Event();
        event.setSubject(eventInput.getText().toString());
        event.setLocation(new Location(locationInput.getText().toString()));
        event.setStart(new DateTimeTimeZone(dateEvent.getText().toString()+"T"+ timeEvent.getText().toString(), "UTC"));
        event.setEnd(new DateTimeTimeZone("2018-12-12T20:16:30.033", "UTC"));


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_POSTADRESS, new JSONObject(new Gson().toJson(event)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Event saved!", Toast.LENGTH_SHORT).show();
                        System.out.println(response.toString());
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

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);



        switch(pos){
            case 0:


                Toast.makeText(getApplicationContext(), "Hey boo!", Toast.LENGTH_SHORT).show();

                break;

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}
