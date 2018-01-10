package com.example.keiichi.project_mobile.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.keiichi.project_mobile.DAL.POJOs.Attendee;
import com.example.keiichi.project_mobile.DAL.POJOs.DateTimeTimeZone;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.DAL.POJOs.ItemBody;
import com.example.keiichi.project_mobile.DAL.POJOs.Location;
import com.example.keiichi.project_mobile.R;
import com.example.keiichi.project_mobile.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class EditEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/events/";

    private String [] DURATIONSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "Entire day"};
    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours",
            "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Free", "Working elsewhere", "Tentative", "Busy", "Away"};
    private String [] REPEATSPINNERLIST = {"Never", "Each day", "Every sunday", "Every workday", "Day 31 of every month", "Ever last sunday", "Every 31st of december"};

    private List<Attendee> attendees;
    private Toolbar myToolbar;
    private Calendar start;
    private DatePickerDialog datePickerDialog;
    private EditText dateEvent;
    private EditText timeEvent;
    private EditText eventInput;
    private EditText locationInput;
    private EditText personalNotes;
    private TextView reminderTitle;
    private TextView displayAsTitle;
    private TextView repeatTitle;
    private TextView notesTitle;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private Spinner displayAsSpinner;
    private Spinner repeatSpinner;
    private CheckBox privateCheckbox;
    private CheckBox responseCheckbox;
    private ListView attendeeList;
    private AttendeeAdapter attendeeAdapter;
    private boolean isCurrentDate;
    private boolean isCurrentTime;
    private boolean isPrivate;
    private boolean responseRequested;
    private String userName;
    private String userEmail;
    private String accessToken;
    private String id;
    private String subject;
    private String location;
    private String startDate;
    private String displayAs;
    private String displayAsValue;
    private String notes;
    private String finalHourOfDay;
    private String finalMinuteOfHour;
    private String sensitivity;
    private String fromEventDetails;
    private int dayOfMonth;
    private int month;
    private int year;
    private int hourOfDay;
    private int minuteOfHour;
    private int duration;
    private int startingValueReminder;
    private int startingValueDisplayAs;
    private int startingValueDuration;
    private int startingValueRepeat;
    private int reminderMinutesBeforeStart;
    private int reminderMinutesBeforeStartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        id= getIntent().getStringExtra("id");
        subject = getIntent().getStringExtra("subject");
        location = getIntent().getStringExtra("location");
        startDate = getIntent().getStringExtra("startDate");
        displayAs = getIntent().getStringExtra("displayAs");
        notes = getIntent().getStringExtra("notes");
        sensitivity = getIntent().getStringExtra("sensitivity");
        fromEventDetails = getIntent().getStringExtra("fromEventDetails");
        responseRequested = getIntent().getBooleanExtra("responseRequested", false);
        reminderMinutesBeforeStart = getIntent().getIntExtra("reminderMinutesBeforeStart", 0);
        attendees = (List<Attendee>)getIntent().getSerializableExtra("attendeesList");

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
        attendeeList = (ListView) findViewById(R.id.attendeeList);
        durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        displayAsSpinner = (Spinner) findViewById(R.id.displayAsSpinner);
        privateCheckbox = (CheckBox) findViewById(R.id.privateCheckbox);
        responseCheckbox = (CheckBox) findViewById(R.id.responseCheckbox);

        // VUL INPUTS MET DATA VAN CONTACT
        eventInput.setText(subject);
        locationInput.setText(location);
        personalNotes.setText(Html.fromHtml(notes));

        durationSpinner.setOnItemSelectedListener(this);
        reminderSpinner.setOnItemSelectedListener(this);
        displayAsSpinner.setOnItemSelectedListener(this);
        repeatSpinner.setOnItemSelectedListener(this);

        start = Calendar.getInstance();

        if(fromEventDetails != null){

            if(!attendees.isEmpty()){

                attendeeAdapter = new AttendeeAdapter(this, attendees);
                attendeeList.setAdapter(attendeeAdapter);
                Utility.setListViewHeightBasedOnChildren(attendeeList);

            }

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

        if(responseRequested){

            responseCheckbox.setChecked(true);

        } else {

            responseCheckbox.setChecked(false);

        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = sdf.parse(startDate);
            start.setTime(date);

            dayOfMonth = start.get(Calendar.DAY_OF_MONTH);
            month = start.get(Calendar.MONTH) + 1;
            year = start.get(Calendar.YEAR);

            minuteOfHour = start.get(Calendar.MINUTE);
            hourOfDay = start.get(Calendar.HOUR_OF_DAY);


        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(hourOfDay <10) {
            finalHourOfDay = "0" + hourOfDay;
        } else {
            finalHourOfDay = String.valueOf(hourOfDay);
        }

        if(minuteOfHour<10){
            finalMinuteOfHour = "0"+ minuteOfHour;
        }  else {
            finalMinuteOfHour = String.valueOf(minuteOfHour);
        }

        dateEvent.setFocusable(false);
        dateEvent.setClickable(true);
        timeEvent.setFocusable(false);
        timeEvent.setClickable(true);
        dateEvent.setText(dayOfMonth + "-" + month + "-" + year);
        timeEvent.setText(finalHourOfDay + ":" + finalMinuteOfHour);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterDuration = new ArrayAdapter<String>(this, R.layout.spinner_layout, DURATIONSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        durationSpinner.setAdapter(adapterDuration);
        //startingValue = adapterDuration.getPosition("1 Hour");
        //durationSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterReminder = new ArrayAdapter<String>(this, R.layout.spinner_layout, REMINDERSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reminderSpinner.setAdapter(adapterReminder);
        //startingValue = adapterReminder.getPosition("15 Minutes");
        //reminderSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterDisplayAs = new ArrayAdapter<String>(this,R.layout.spinner_layout, DISPLAYASSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displayAsSpinner.setAdapter(adapterDisplayAs);
        //startingValue = adapterDisplayAs.getPosition("Busy");
        //displayAsSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterRepeat = new ArrayAdapter<String>(this,R.layout.spinner_layout, REPEATSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repeatSpinner.setAdapter(adapterRepeat);
        //startingValue = adapterRepeat.getPosition("Never");
        //repeatSpinner.setSelection(startingValue);

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
                datePickerDialog = new DatePickerDialog(EditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearPicked,
                                                  int monthOfYearPicked, int dayOfMonthPicked) {

                                isCurrentDate = false;

                                setStartDate(year, monthOfYearPicked++, dayOfMonthPicked);

                                dayOfMonth = dayOfMonthPicked;
                                month = monthOfYearPicked;
                                year = yearPicked;

                                dateEvent.setText(dayOfMonth + "-" + month + "-" + year);

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
                mTimePicker = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        isCurrentTime = false;

                        minuteOfHour = selectedMinute;
                        hourOfDay = selectedHour;

                        timeEvent.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTONN TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                Intent intentCalendar = new Intent(EditEventActivity.this, EventDetailsActivity.class);
                intentCalendar.putExtra("AccessToken", accessToken);
                intentCalendar.putExtra("userName", userName);
                intentCalendar.putExtra("userEmail", userEmail);
                intentCalendar.putExtra("subject", subject);
                intentCalendar.putExtra("location", location);
                intentCalendar.putExtra("startDate", startDate);
                intentCalendar.putExtra("displayAs", displayAs);
                intentCalendar.putExtra("notes", notes);
                intentCalendar.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);

                startActivity(intentCalendar);

                return true;


            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                try {
                    updateEvent();

                    int DELAY_TIME=2000;

                    //start your animation
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentCalendar = new Intent(EditEventActivity.this, EventDetailsActivity.class);
                            intentCalendar.putExtra("AccessToken", accessToken);
                            intentCalendar.putExtra("userName", userName);
                            intentCalendar.putExtra("userEmail", userEmail);
                            intentCalendar.putExtra("subject", subject);
                            intentCalendar.putExtra("location", location);
                            intentCalendar.putExtra("startDate", startDate);
                            intentCalendar.putExtra("displayAs", displayAs);
                            intentCalendar.putExtra("notes", notes);
                            intentCalendar.putExtra("reminderMinutesBeforeStart", reminderMinutesBeforeStart);

                            startActivity(intentCalendar);
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

    private void setStartDate(int yearPicked, int monthPicked, int dayOfMonthPicked) {
        if (isCurrentDate) {
            //startDate = LocalDateTime.of(yearPicked, monthPicked, dayOfMonthPicked, startDate.getHour(), startDate.getMinute());
        } else {
            // endTime =
        }


    }

    private String getDayInString(int day) {
        switch (day) {
            case 0:
                return "Monday";

            case 1:
                return "Tuesday";


            case 2:
                return "Wednesday";


            case 3:
                return "Thursday";

            case 4:
                return "Friday";


            case 5:
                return "Saturday";

            case 6:
                return "Sunday";


            default:
                return "";


        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);

        if(parent == durationSpinner){
            switch(pos){
                case 0:
                    duration = 0;
                    break;

                case 1:
                    duration = 15;
                    break;

                case 2:
                    duration = 30;
                    break;

                case 3:
                    duration = 45;
                    break;

                case 4:
                    duration = 60;
                    break;

                case 5:
                    duration = 90;
                    break;

                case 6:
                    duration = 120;
                    break;

                case 7:
                    duration = 1440 ;
                    break;

            }
        }

        if (parent == reminderSpinner){
            switch(pos){
                case 0:
                    reminderMinutesBeforeStartValue = 0;
                    break;

                case 1:
                    reminderMinutesBeforeStartValue = 15;
                    break;

                case 2:
                    reminderMinutesBeforeStartValue = 30;
                    break;

                case 3:
                    reminderMinutesBeforeStartValue = 45;
                    break;

                case 4:
                    reminderMinutesBeforeStartValue = 60;
                    break;

                case 5:
                    reminderMinutesBeforeStartValue = 90;
                    break;

                case 6:
                    reminderMinutesBeforeStartValue = 120;
                    break;

                case 7:
                    reminderMinutesBeforeStartValue = 180 ;
                    break;

                case 8:
                    reminderMinutesBeforeStartValue = 240 ;
                    break;

                case 9:
                    reminderMinutesBeforeStartValue = 480 ;
                    break;

                case 10:
                    reminderMinutesBeforeStartValue = 720 ;
                    break;

                case 11:
                    reminderMinutesBeforeStartValue = 1440 ;
                    break;

                case 12:
                    reminderMinutesBeforeStartValue = 2880 ;
                    break;

                case 13:
                    reminderMinutesBeforeStartValue = 4320 ;
                    break;

                case 14:
                    reminderMinutesBeforeStartValue = 10080 ;
                    break;

                case 15:
                    reminderMinutesBeforeStartValue = 20160 ;
                    break;

            }
        }

        if(parent == displayAsSpinner){
            switch(pos){
                case 0:
                    displayAsValue = "Free";
                    displayAs = "free";
                    break;

                case 1:
                    displayAsValue = "WorkingElsewhere";
                    displayAs = "workingElsewhere";
                    break;

                case 2:
                    displayAsValue = "Tentative";
                    displayAs = "tentative";
                    break;

                case 3:
                    displayAsValue = "Busy";
                    displayAs = "busy";
                    break;

                case 4:
                    displayAsValue = "Oof";
                    displayAs = "oof";
                    break;
            }
        }

        if(parent == repeatSpinner){
            switch(pos){
                case 0:
                    break;

                case 1:
                    break;

                case 2:

                    break;

                case 3:
                    break;

                case 4:
                    break;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // PATCH REQUEST VOOR UPDATE EVENT
    private void updateEvent() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);


        Event event = new Event();
        event.setSubject(eventInput.getText().toString());
        subject = eventInput.getText().toString();

        event.setLocation(new Location(locationInput.getText().toString()));
        location = locationInput.getText().toString();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minuteOfHour);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String startTime = sdf.format(cal.getTime());

        event.setStart(new DateTimeTimeZone(startTime, TimeZone.getDefault().getDisplayName()));

        startDate = startTime;

        cal.add(Calendar.MINUTE, duration);
        String endTime = sdf.format(cal.getTime());

        event.setEnd(new DateTimeTimeZone(endTime, TimeZone.getDefault().getDisplayName()));

        event.setBody(new ItemBody("Text", personalNotes.getText().toString()));
        notes = personalNotes.getText().toString();

        event.setReminderMinutesBeforeStart(reminderMinutesBeforeStartValue);
        event.setReminderOn(true);

        reminderMinutesBeforeStart = reminderMinutesBeforeStartValue;

        event.setShowAs(displayAsValue);

        String postAddress = URL_POSTADRESS + id;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, postAddress, new JSONObject(new Gson().toJson(event)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Event updated!", Toast.LENGTH_SHORT).show();
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
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.privateCheckbox:
                if (checked){

                    isPrivate = true;

                }

                else{
                    isPrivate = false;
                }
                break;

            case R.id.responseCheckbox:
                if (checked){

                    responseRequested = true;

                }

                else{
                    responseRequested = false;
                }
                break;


        }
    }
}
