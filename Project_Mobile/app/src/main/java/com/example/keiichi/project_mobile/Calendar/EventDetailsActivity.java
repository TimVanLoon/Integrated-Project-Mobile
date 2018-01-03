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

public class EventDetailsActivity extends AppCompatActivity {

    final private String URL_POSTADRESS = "https://graph.microsoft.com/beta/me/events/";
    private Toolbar myToolbar;
    private  AlertDialog.Builder builder;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        id= getIntent().getStringExtra("id");


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
}
