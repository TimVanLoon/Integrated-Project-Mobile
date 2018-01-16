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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.Contacts.RoomDetailsActivity;
import com.example.keiichi.project_mobile.Contacts.UserDetailsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Attendee;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.DateTimeTimeZone;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.DAL.POJOs.ItemBody;
import com.example.keiichi.project_mobile.DAL.POJOs.Location;
import com.example.keiichi.project_mobile.DAL.POJOs.User;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.Mail.SendMailActivity;
import com.example.keiichi.project_mobile.R;
import com.example.keiichi.project_mobile.Utility;
import com.google.gson.Gson;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/events";
    private String [] DURATIONSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "Entire day"};
    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours", "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Free", "Working elsewhere", "Tentative", "Busy", "Away"};
    private String [] REPEATSPINNERLIST = {"Never", "Each day", "Every sunday", "Every workday", "Day 31 of every month", "Ever last sunday", "Every 31st of december"};
    private final Calendar c = Calendar.getInstance();
    private List<Attendee> attendees;
    private List<EmailAddress> emailList = new ArrayList<>();
    private int startingValue;
    private int dayOfMonth;
    private int month;
    private int year;
    private int hourOfDay;
    private int minuteOfHour;
    private int duration;
    private int reminderMinutesBeforeStart;
    private String currentDay;
    private String userName;
    private String userEmail;
    private String accessToken;
    private String finalHourOfDay;
    private String finalMinuteOfHour;
    private String showAs;
    private String fromAttendeesActivity;
    private String firstTime;
    private String finalMonth;
    private String finalDayOfMonth;
    private String eventSubject;
    private String eventLocation;
    private String eventNotes;
    private String userMailName;
    private String fromContactDetailsActivity;
    private String fromRoomActivity;
    private String fromUserActivity;
    private String emailAddress;
    private String attendeeMail;
    private String attendeeName;
    private boolean isCurrentDate;
    private boolean isCurrentTime;
    private boolean isPrivate;
    private boolean responseRequested;
    private Button moreDetailsButton;
    private CheckBox privateCheckbox;
    private CheckBox responseCheckbox;
    private EditText dateEvent;
    private EditText timeEvent;
    private EditText eventInput;
    private EditText locationInput;
    private EditText personalNotes;
    private TextView reminderTitle;
    private TextView displayAsTitle;
    private TextView repeatTitle;
    private TextView notesTitle;
    private TextView attendeesTitle;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private Spinner displayAsSpinner;
    private Spinner repeatSpinner;
    private ImageView plusAttendeeIcon;
    private ListView attendeeList;
    private DatePickerDialog datePickerDialog;
    private Toolbar myToolbar;
    private LinearLayout privateLayout;
    private LinearLayout requestReponseLayout;
    private AttendeeAdapter attendeeAdapter;
    private ArrayAdapter<String> adapterDuration;
    private ArrayAdapter<String> adapterReminder;
    private ArrayAdapter<String> adapterDisplayAs;
    private ArrayAdapter<String> adapterRepeat;
    private MenuItem saveItem;
    private Contact contact;
    private EmailAddress room;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        emailList = (List<EmailAddress>)getIntent().getSerializableExtra("emailList");
        fromAttendeesActivity = getIntent().getStringExtra("fromAttendeesActivity");
        attendeeName = getIntent().getStringExtra("attendeeName");
        attendeeMail = getIntent().getStringExtra("attendeeMail");
        contact = (Contact) getIntent().getSerializableExtra("contact");
        fromContactDetailsActivity = getIntent().getStringExtra("fromContactDetailsActivity");
        fromRoomActivity = getIntent().getStringExtra("fromRoomActivity");
        fromUserActivity = getIntent().getStringExtra("fromUserActivity");
        room = (EmailAddress) getIntent().getSerializableExtra("room");
        user = (User) getIntent().getSerializableExtra("user");

        System.out.println("from contact details: " + fromContactDetailsActivity);
        System.out.println("from room: " + fromRoomActivity);
        System.out.println("from user: " + fromUserActivity);

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
        attendeesTitle = (TextView) findViewById(R.id.attendeesTitle);
        notesTitle = (TextView) findViewById(R.id.notesTitle);
        durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        displayAsSpinner = (Spinner) findViewById(R.id.displayAsSpinner);
        moreDetailsButton = (Button) findViewById(R.id.moreDetailsButton);
        plusAttendeeIcon = (ImageView) findViewById(R.id.plusAttendeeIcon);
        attendeeList = (ListView) findViewById(R.id.attendeeList);
        privateLayout = (LinearLayout) findViewById(R.id.privateLayout);
        requestReponseLayout = (LinearLayout) findViewById(R.id.requestReponseLayout);
        privateCheckbox = (CheckBox) findViewById(R.id.privateCheckbox);
        responseCheckbox = (CheckBox) findViewById(R.id.responseCheckbox);

        setEditTextOnFocusListener(eventInput);
        setEditTextOnFocusListener(locationInput);
        setEditTextOnFocusListener(personalNotes);
        setSpinnerOnFocusListener(durationSpinner);
        setSpinnerOnFocusListener(reminderSpinner);
        setSpinnerOnFocusListener(repeatSpinner);
        setSpinnerOnFocusListener(durationSpinner);

        attendeesTitle.setVisibility(View.GONE);
        plusAttendeeIcon.setVisibility(View.GONE);
        reminderSpinner.setVisibility(View.GONE);
        repeatSpinner.setVisibility(View.GONE);
        displayAsSpinner.setVisibility(View.GONE);
        notesTitle.setVisibility(View.GONE);
        repeatTitle.setVisibility(View.GONE);
        displayAsTitle.setVisibility(View.GONE);
        reminderTitle.setVisibility(View.GONE);
        personalNotes.setVisibility(View.GONE);
        privateLayout.setVisibility(View.GONE);
        requestReponseLayout.setVisibility(View.GONE);

        plusAttendeeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentAttendees = new Intent(AddEventActivity.this, AttendeeActivity.class);
                intentAttendees.putExtra("AccessToken", accessToken);
                intentAttendees.putExtra("userName", userName);
                intentAttendees.putExtra("userEmail", userEmail);
                intentAttendees.putExtra("emailList",(Serializable) emailList);
                intentAttendees.putExtra("firstTime", firstTime);
                intentAttendees.putExtra("eventSubject", eventInput.getText().toString());
                intentAttendees.putExtra("eventLocation", locationInput.getText().toString());
                intentAttendees.putExtra("eventDayOfMonth", dayOfMonth);
                intentAttendees.putExtra("eventMonth", month);
                intentAttendees.putExtra("eventYear", year);
                intentAttendees.putExtra("eventHour", hourOfDay);
                intentAttendees.putExtra("eventMinute", minuteOfHour);
                intentAttendees.putExtra("eventDuration", duration);
                intentAttendees.putExtra("eventReminderMinutesBeforeStart", reminderMinutesBeforeStart);
                intentAttendees.putExtra("eventShowAs", showAs);
                intentAttendees.putExtra("eventNotes", personalNotes.getText().toString());
                intentAttendees.putExtra("eventIsPrivate", isPrivate);
                intentAttendees.putExtra("eventRequestResponses", responseRequested);

                startActivity(intentAttendees);

                AddEventActivity.this.finish();
            }
        });


        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                makeExtraVisible();

            }
        });

        durationSpinner.setOnItemSelectedListener(this);
        reminderSpinner.setOnItemSelectedListener(this);
        displayAsSpinner.setOnItemSelectedListener(this);
        repeatSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterDuration = new ArrayAdapter<String>(this, R.layout.spinner_layout, DURATIONSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        durationSpinner.setAdapter(adapterDuration);
        startingValue = adapterDuration.getPosition("1 Hour");
        durationSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterReminder = new ArrayAdapter<String>(this, R.layout.spinner_layout, REMINDERSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        reminderSpinner.setAdapter(adapterReminder);
        startingValue = adapterReminder.getPosition("15 Minutes");
        reminderSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterDisplayAs = new ArrayAdapter<String>(this,R.layout.spinner_layout, DISPLAYASSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displayAsSpinner.setAdapter(adapterDisplayAs);
        startingValue = adapterDisplayAs.getPosition("Busy");
        displayAsSpinner.setSelection(startingValue);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterRepeat = new ArrayAdapter<String>(this,R.layout.spinner_layout, REPEATSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repeatSpinner.setAdapter(adapterRepeat);
        startingValue = adapterRepeat.getPosition("Never");
        repeatSpinner.setSelection(startingValue);

        currentDay = getDayInString(c.get(Calendar.DAY_OF_WEEK_IN_MONTH));

        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);

        if(month <10) {
            finalMonth = "0" + month;
        } else {
            finalMonth = String.valueOf(month);
        }

        if(dayOfMonth <10) {
            finalDayOfMonth = "0" + dayOfMonth;
        } else {
            finalDayOfMonth = String.valueOf(dayOfMonth);
        }

        minuteOfHour = c.get(Calendar.MINUTE);
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);

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
        dateEvent.setText(finalDayOfMonth + "-" + finalMonth + "-" + year);
        timeEvent.setText(finalHourOfDay + ":" + finalMinuteOfHour);

        isCurrentDate = true;
        isCurrentTime = true;

        if(fromContactDetailsActivity != null){

            makeExtraVisible();

            firstTime = "no";

            attendees = new ArrayList<>();
            emailList = new ArrayList<>();

            EmailAddress email = new EmailAddress(attendeeMail, attendeeName);

            emailList.add(email);

            String type = "optional";

            Attendee attendee = new Attendee(type, email);

            attendees.add(attendee);

            attendeeAdapter = new AttendeeAdapter(this, attendees);
            attendeeList.setAdapter(attendeeAdapter);
            Utility.setListViewHeightBasedOnChildren(attendeeList);

        }

        if(fromRoomActivity != null){

            makeExtraVisible();

            firstTime = "no";

            attendees = new ArrayList<>();
            emailList = new ArrayList<>();

            EmailAddress email = new EmailAddress(attendeeMail, attendeeName);

            emailList.add(email);

            String type = "optional";

            Attendee attendee = new Attendee(type, email);

            attendees.add(attendee);

            attendeeAdapter = new AttendeeAdapter(this, attendees);
            attendeeList.setAdapter(attendeeAdapter);
            Utility.setListViewHeightBasedOnChildren(attendeeList);

        }

        if(fromUserActivity != null){

            makeExtraVisible();

            firstTime = "no";

            attendees = new ArrayList<>();
            emailList = new ArrayList<>();

            EmailAddress email = new EmailAddress(attendeeMail, attendeeName);

            emailList.add(email);

            String type = "optional";

            Attendee attendee = new Attendee(type, email);

            attendees.add(attendee);

            attendeeAdapter = new AttendeeAdapter(this, attendees);
            attendeeList.setAdapter(attendeeAdapter);
            Utility.setListViewHeightBasedOnChildren(attendeeList);

        }

        if(fromAttendeesActivity != null ){

            makeExtraVisible();

            refillFromAttendeeActivity();

            attendees = new ArrayList<>();

            if(!emailList.isEmpty()){

                for (EmailAddress email : emailList) {

                    firstTime = "no";

                    String contactEmail = email.getAddress();
                    String contactName = email.getName();
                    String type = "optional";

                    Attendee attendee = new Attendee(type, email);

                    attendees.add(attendee);
                }

                attendeeAdapter = new AttendeeAdapter(this, attendees);
                attendeeList.setAdapter(attendeeAdapter);
                Utility.setListViewHeightBasedOnChildren(attendeeList);

            }



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
                datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearPicked,
                                                  int monthOfYearPicked, int dayOfMonthPicked) {

                                isCurrentDate = false;

                                dayOfMonth = dayOfMonthPicked;
                                month = monthOfYearPicked + 1;
                                year = yearPicked;

                                if(month <10) {
                                    finalMonth = "0" + month;
                                } else {
                                    finalMonth = String.valueOf(month);
                                }

                                if(dayOfMonth <10) {
                                    finalDayOfMonth = "0" + dayOfMonth;
                                } else {
                                    finalDayOfMonth = String.valueOf(dayOfMonth);
                                }


                                dateEvent.setText(finalDayOfMonth + "-" + finalMonth + "-" + year);

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
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_navigation, menu);

        saveItem = menu.findItem(R.id.action_save);

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:

                if(fromContactDetailsActivity == null && fromRoomActivity == null && fromUserActivity == null){

                    Intent intentCalendar = new Intent(AddEventActivity.this, CalendarActivity.class);
                    intentCalendar.putExtra("AccessToken", accessToken);
                    intentCalendar.putExtra("userName", userName);
                    intentCalendar.putExtra("userEmail", userEmail);

                    startActivity(intentCalendar);

                    AddEventActivity.this.finish();

                } else if(fromUserActivity == null && fromRoomActivity == null) {

                    Intent intentContactDetails = new Intent(AddEventActivity.this, ContactsDetailsActivity.class);
                    intentContactDetails.putExtra("AccessToken", accessToken);
                    intentContactDetails.putExtra("userName", userName);
                    intentContactDetails.putExtra("userEmail", userEmail);
                    intentContactDetails.putExtra("contact", contact);

                    startActivity(intentContactDetails);

                    AddEventActivity.this.finish();

                } else if(fromUserActivity == null && fromContactDetailsActivity == null){

                    Intent intentContactDetails = new Intent(AddEventActivity.this, RoomDetailsActivity.class);
                    intentContactDetails.putExtra("AccessToken", accessToken);
                    intentContactDetails.putExtra("userName", userName);
                    intentContactDetails.putExtra("userEmail", userEmail);
                    intentContactDetails.putExtra("room", room);

                    startActivity(intentContactDetails);

                    AddEventActivity.this.finish();

                } else if(fromRoomActivity == null && fromContactDetailsActivity == null){

                    Intent intentContactDetails = new Intent(AddEventActivity.this, UserDetailsActivity.class);
                    intentContactDetails.putExtra("AccessToken", accessToken);
                    intentContactDetails.putExtra("userName", userName);
                    intentContactDetails.putExtra("userEmail", userEmail);
                    intentContactDetails.putExtra("user", user);

                    startActivity(intentContactDetails);

                    AddEventActivity.this.finish();

                }


                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                try {
                    saveItem.setEnabled(false);

                    saveEvent();

                    int DELAY_TIME=2000;

                    //start your animation
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentCalendar = new Intent(AddEventActivity.this, ListEventsActivity.class);
                            intentCalendar.putExtra("AccessToken", accessToken);
                            intentCalendar.putExtra("userName", userName);
                            intentCalendar.putExtra("userEmail", userEmail);

                            startActivity(intentCalendar);

                            AddEventActivity.this.finish();

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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay - 1);
        cal.set(Calendar.MINUTE, minuteOfHour);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String startTime = sdf.format(cal.getTime());

        event.setStart(new DateTimeTimeZone(startTime, TimeZone.getDefault().getDisplayName()));

        cal.add(Calendar.MINUTE, duration);
        String endTime = sdf.format(cal.getTime());

        event.setEnd(new DateTimeTimeZone(endTime, TimeZone.getDefault().getDisplayName()));

        event.setBody(new ItemBody("Text", personalNotes.getText().toString()));

        event.setAttendees(attendees);

        event.setReminderMinutesBeforeStart(reminderMinutesBeforeStart);
        event.setReminderOn(true);

        event.setShowAs(showAs);

        if(isPrivate){

            event.setSensitivity("private");

        } else {

            event.setSensitivity("normal");

        }

        event.setResponseRequested(responseRequested);

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
                    reminderMinutesBeforeStart = 0;
                    break;

                case 1:
                    reminderMinutesBeforeStart = 15;
                    break;

                case 2:
                    reminderMinutesBeforeStart = 30;
                    break;

                case 3:
                    reminderMinutesBeforeStart = 45;
                    break;

                case 4:
                    reminderMinutesBeforeStart = 60;
                    break;

                case 5:
                    reminderMinutesBeforeStart = 90;
                    break;

                case 6:
                    reminderMinutesBeforeStart = 120;
                    break;

                case 7:
                    reminderMinutesBeforeStart = 180 ;
                    break;

                case 8:
                    reminderMinutesBeforeStart = 240 ;
                    break;

                case 9:
                    reminderMinutesBeforeStart = 480 ;
                    break;

                case 10:
                    reminderMinutesBeforeStart = 720 ;
                    break;

                case 11:
                    reminderMinutesBeforeStart = 1440 ;
                    break;

                case 12:
                    reminderMinutesBeforeStart = 2880 ;
                    break;

                case 13:
                    reminderMinutesBeforeStart = 4320 ;
                    break;

                case 14:
                    reminderMinutesBeforeStart = 10080 ;
                    break;

                case 15:
                    reminderMinutesBeforeStart = 20160 ;
                    break;

            }
        }

        if(parent == displayAsSpinner){
            switch(pos){
                case 0:
                    showAs = "Free";
                    break;

                case 1:
                    showAs = "WorkingElsewhere";
                    break;

                case 2:
                    showAs = "Tentative";
                    break;

                case 3:
                    showAs = "Busy";
                    break;

                case 4:
                    showAs = "Oof";
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

    private void makeExtraVisible(){

        moreDetailsButton.setVisibility(View.GONE);
        reminderSpinner.setVisibility(View.VISIBLE);
        repeatSpinner.setVisibility(View.VISIBLE);
        displayAsSpinner.setVisibility(View.VISIBLE);
        notesTitle.setVisibility(View.VISIBLE);
        repeatTitle.setVisibility(View.VISIBLE);
        displayAsTitle.setVisibility(View.VISIBLE);
        reminderTitle.setVisibility(View.VISIBLE);
        personalNotes.setVisibility(View.VISIBLE);
        attendeesTitle.setVisibility(View.VISIBLE);
        plusAttendeeIcon.setVisibility(View.VISIBLE);
        privateLayout.setVisibility(View.VISIBLE);
        requestReponseLayout.setVisibility(View.VISIBLE);
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

    public void refillFromAttendeeActivity(){

        eventSubject = getIntent().getStringExtra("eventSubject");
        eventLocation = getIntent().getStringExtra("eventLocation");
        dayOfMonth = getIntent().getIntExtra("eventDayOfMonth", 0);
        month = getIntent().getIntExtra("eventMonth", 0);
        year = getIntent().getIntExtra("eventYear", 0);
        hourOfDay = getIntent().getIntExtra("eventHour", 0);
        minuteOfHour = getIntent().getIntExtra("eventMinute", 0);
        duration = getIntent().getIntExtra("eventDuration", 0);
        showAs = getIntent().getStringExtra("eventShowAs");
        eventNotes = getIntent().getStringExtra("eventNotes");
        isPrivate = getIntent().getBooleanExtra("eventIsPrivate", false);
        responseRequested = getIntent().getBooleanExtra("eventRequestResponses", false);
        reminderMinutesBeforeStart = getIntent().getIntExtra("eventReminderMinutesBeforeStart", 0);

        eventInput.setText(eventSubject);
        locationInput.setText(eventLocation);
        personalNotes.setText(eventNotes);

        if(month <10) {
            finalMonth = "0" + month;
        } else {
            finalMonth = String.valueOf(month);
        }

        if(dayOfMonth <10) {
            finalDayOfMonth = "0" + dayOfMonth;
        } else {
            finalDayOfMonth = String.valueOf(dayOfMonth);
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

        dateEvent.setText(finalDayOfMonth + "-" + finalMonth + "-" + year);
        timeEvent.setText(finalHourOfDay + ":" + finalMinuteOfHour);


        if(isPrivate){

            privateCheckbox.setChecked(true);

        } else {

            privateCheckbox.setChecked(false);

        }

        if(responseRequested){

            responseCheckbox.setChecked(true);

        } else {

            responseCheckbox.setChecked(false);

        }

        switch(showAs){
            case "Free":
                startingValue = adapterDisplayAs.getPosition("Free");
                displayAsSpinner.setSelection(startingValue);
                break;

            case "WorkingElsewhere":
                startingValue = adapterDisplayAs.getPosition("Working elsewhere");
                displayAsSpinner.setSelection(startingValue);
                break;

            case "Tentative":
                startingValue = adapterDisplayAs.getPosition("Tentative");
                displayAsSpinner.setSelection(startingValue);
                break;

            case "Busy":
                startingValue = adapterDisplayAs.getPosition("Busy");
                displayAsSpinner.setSelection(startingValue);
                break;

            case "Oof":
                startingValue = adapterDisplayAs.getPosition("Away");
                displayAsSpinner.setSelection(startingValue);
                break;

        }

        switch(duration){
            case 0:
                startingValue = adapterDuration.getPosition("0 Minutes");
                durationSpinner.setSelection(startingValue);
                break;

            case 15:
                startingValue = adapterDuration.getPosition("15 Minutes");
                durationSpinner.setSelection(startingValue);
                break;

            case 30:
                startingValue = adapterDuration.getPosition("30 Minutes");
                durationSpinner.setSelection(startingValue);
                break;

            case 45:
                startingValue = adapterDuration.getPosition("45 Minutes");
                durationSpinner.setSelection(startingValue);
                break;

            case 60:
                startingValue = adapterDuration.getPosition("1 Hour");
                durationSpinner.setSelection(startingValue);
                break;

            case 90:
                startingValue = adapterDuration.getPosition("90 Minutes");
                durationSpinner.setSelection(startingValue);
                break;

            case 120:
                startingValue = adapterDuration.getPosition("2 Hours");
                durationSpinner.setSelection(startingValue);
                break;

            case 1440:
                startingValue = adapterDuration.getPosition("Entire day");
                durationSpinner.setSelection(startingValue);
                break;

        }


        switch(reminderMinutesBeforeStart){
            case 0:
                startingValue = adapterReminder.getPosition("0 Minutes");
                reminderSpinner.setSelection(startingValue);
                break;

            case 15:
                startingValue = adapterReminder.getPosition("15 Minutes");
                reminderSpinner.setSelection(startingValue);
                break;

            case 30:
                startingValue = adapterReminder.getPosition("30 Minutes");
                reminderSpinner.setSelection(startingValue);
                break;

            case 45:
                startingValue = adapterReminder.getPosition("45 Minutes");
                reminderSpinner.setSelection(startingValue);
                break;

            case 60:
                startingValue = adapterReminder.getPosition("1 Hour");
                reminderSpinner.setSelection(startingValue);
                break;

            case 90:
                startingValue = adapterReminder.getPosition("90 Minutes");
                reminderSpinner.setSelection(startingValue);
                break;

            case 120:
                startingValue = adapterReminder.getPosition("2 Hours");
                reminderSpinner.setSelection(startingValue);
                break;

            case 180:
                startingValue = adapterReminder.getPosition("3 Hours");
                reminderSpinner.setSelection(startingValue);
                break;

            case 240:
                startingValue = adapterReminder.getPosition("4 Hours");
                reminderSpinner.setSelection(startingValue);
                break;

            case 480:
                startingValue = adapterReminder.getPosition("8 Hours");
                reminderSpinner.setSelection(startingValue);
                break;

            case 720:
                startingValue = adapterReminder.getPosition("12 Hours");
                reminderSpinner.setSelection(startingValue);
                break;

            case 1440:
                startingValue = adapterReminder.getPosition("1 Day");
                reminderSpinner.setSelection(startingValue);
                break;

            case 2880:
                startingValue = adapterReminder.getPosition("2 Days");
                reminderSpinner.setSelection(startingValue);
                break;

            case 4320:
                startingValue = adapterReminder.getPosition("3 Days");
                reminderSpinner.setSelection(startingValue);
                break;

            case 10080:
                startingValue = adapterReminder.getPosition("1 Week");
                reminderSpinner.setSelection(startingValue);
                break;

            case 20160:
                startingValue = adapterReminder.getPosition("2 Weeks");
                reminderSpinner.setSelection(startingValue);
                break;
        }

    }

    public void setEditTextOnFocusListener(EditText et){

        et.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){

                    view.setBackgroundResource( R.drawable.edit_text_style_focused);
                }
                else{
                    view.setBackgroundResource( R.drawable.edit_text_style);
                }
            }
        });

    }

    public void setSpinnerOnFocusListener(Spinner spinnner){

        spinnner.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){

                    view.setBackgroundResource( R.drawable.edit_text_style_focused);
                }
                else{
                    view.setBackgroundResource( R.drawable.edit_text_style);
                }
            }
        });

    }

    @Override
    public void onBackPressed(){

        if(fromContactDetailsActivity == null && fromRoomActivity == null && fromUserActivity == null){

            Intent intentCalendar = new Intent(AddEventActivity.this, CalendarActivity.class);
            intentCalendar.putExtra("AccessToken", accessToken);
            intentCalendar.putExtra("userName", userName);
            intentCalendar.putExtra("userEmail", userEmail);

            startActivity(intentCalendar);

            AddEventActivity.this.finish();

        } else if(fromUserActivity == null && fromRoomActivity == null) {

            Intent intentContactDetails = new Intent(AddEventActivity.this, ContactsDetailsActivity.class);
            intentContactDetails.putExtra("AccessToken", accessToken);
            intentContactDetails.putExtra("userName", userName);
            intentContactDetails.putExtra("userEmail", userEmail);
            intentContactDetails.putExtra("contact", contact);

            startActivity(intentContactDetails);

            AddEventActivity.this.finish();

        } else if(fromUserActivity == null && fromContactDetailsActivity == null){

            Intent intentContactDetails = new Intent(AddEventActivity.this, RoomDetailsActivity.class);
            intentContactDetails.putExtra("AccessToken", accessToken);
            intentContactDetails.putExtra("userName", userName);
            intentContactDetails.putExtra("userEmail", userEmail);
            intentContactDetails.putExtra("room", room);

            startActivity(intentContactDetails);

            AddEventActivity.this.finish();

        } else if(fromRoomActivity == null && fromContactDetailsActivity == null){

            Intent intentContactDetails = new Intent(AddEventActivity.this, UserDetailsActivity.class);
            intentContactDetails.putExtra("AccessToken", accessToken);
            intentContactDetails.putExtra("userName", userName);
            intentContactDetails.putExtra("userEmail", userEmail);
            intentContactDetails.putExtra("user", user);

            startActivity(intentContactDetails);

            AddEventActivity.this.finish();

        }


    }

    public void minimizeApp() {

        Intent intentListMails = new Intent(AddEventActivity.this, ListEventsActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        AddEventActivity.this.finish();

    }

}
