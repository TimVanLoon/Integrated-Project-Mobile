package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

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
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
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

public class ListEventsActivity extends AppCompatActivity {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();

    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/events?$select=subject,body,bodyPreview,organizer,attendees,start,end,location";

    private String accessToken;
    private String userName;
    private String userEmail;
    private Toolbar myToolbar;
    private SearchView searchView;
    private ListView eventsListView;
    private List<Event> events = new ArrayList<>();
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        eventsListView = (ListView) findViewById(R.id.eventsListView);

        setSupportActionBar(myToolbar);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");


        callGraphAPI();
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


                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){



            case R.id.action_add:
                Intent intentAddEvent = new Intent(ListEventsActivity.this, AddEventActivity.class);
                intentAddEvent.putExtra("AccessToken", accessToken);
                intentAddEvent.putExtra("userName", userName);
                intentAddEvent.putExtra("userEmail", userEmail);

                startActivity(intentAddEvent);
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
}
