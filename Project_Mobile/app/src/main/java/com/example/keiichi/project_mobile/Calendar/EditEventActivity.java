package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.keiichi.project_mobile.R;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class EditEventActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/events/";

    private String [] DURATIONSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "Entire day"};
    private String [] REMINDERSPINNERLIST = {"0 Minutes", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "90 Minutes", " 2 Hours", "3 Hours", "4 Hours", "8 Hours", "12 Hours",
            "1 Day", "2 Days", "3 Days", "1 Week", "2 Weeks"};
    private String [] DISPLAYASSPINNERLIST = {"Free", "Working elsewhere", "Tentative", "Busy", "Away"};
    private String [] REPEATSPINNERLIST = {"Never", "Each day", "Every sunday", "Every workday", "Day 31 of every month", "Ever last sunday", "Every 31st of december"};

    private Toolbar myToolbar;
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
    private String userName;
    private String userEmail;
    private String accessToken;
    private String id;
    private String subject;
    private String location;
    private String startDate;
    private String displayAs;
    private String notes;
    private int startingValueReminder;
    private int startingValueDisplayAs;
    private int startingValueDuration;
    private int startingValueRepeat;
    private int reminderMinutesBeforeStart;

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
        reminderMinutesBeforeStart = getIntent().getIntExtra("reminderMinutesBeforeStart", 0);

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

        // VUL INPUTS MET DATA VAN CONTACT
        eventInput.setText(subject);
        locationInput.setText(location);
        personalNotes.setText(Html.fromHtml(notes));

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
        ArrayAdapter<String> adapterRepeat = new ArrayAdapter<String>(this, R.layout.spinner_layout, REPEATSPINNERLIST);
        // Specify the layout to use when the list of choices appears
        adapterDisplayAs.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
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

                /*
            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                try {
                    //saveEvent();

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
                            startActivity(intentCalendar);
                        }
                    }, DELAY_TIME);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
                */

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
