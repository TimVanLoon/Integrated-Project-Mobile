package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.keiichi.project_mobile.R;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class EditEventActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private String userName;
    private String userEmail;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

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
