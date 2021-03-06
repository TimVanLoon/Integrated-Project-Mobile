package com.example.keiichi.project_mobile.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.AddContactActivity;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.Mail.RecyclerTouchListener;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ListEventsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Serializable {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    final static String UNFILTERED_EVENTS = "https://graph.microsoft.com/v1.0/me/events?$orderby=start/dateTime&$top=999&$count=true";
    final private String URL_DELETE = "https://graph.microsoft.com/beta/me/events/";
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/events?$orderby=start/dateTime&$top=999&$count=true&$filter=start/dateTime%20ge%20('";
    private boolean multiSelect = false;
    private boolean actionModeEnabled = false;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String formattedDate;
    private Toolbar myToolbar;
    private SearchView searchView;
    private List<Event> events = new ArrayList<>();
    private EventAdapter eventAdapter;
    private  BottomNavigationView mBottomNav;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int eventsClickedCount = 0;
    private List<Event> selectedEvents = new ArrayList<>();
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private RecyclerView eventsRecyclerView;
    private ActionMode eventActionMode;
    private URI uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        formattedDate = df.format(c.getTime());

        System.out.println("format date: " + formattedDate);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.action_calendar:

                        break;
                    case R.id.action_mail:
                        Intent intentMail = new Intent(ListEventsActivity.this, ListMailsActvity.class);
                        intentMail.putExtra("AccessToken", accessToken);
                        intentMail.putExtra("userName", userName);
                        intentMail.putExtra("userEmail", userEmail);

                        startActivity(intentMail);

                        ListEventsActivity.this.finish();

                        break;
                    case R.id.action_user:
                        Intent intentContacts = new Intent(ListEventsActivity.this, ContactsActivity.class);
                        intentContacts.putExtra("AccessToken", accessToken);
                        intentContacts.putExtra("userName", userName);
                        intentContacts.putExtra("userEmail", userEmail);

                        startActivity(intentContacts);

                        ListEventsActivity.this.finish();

                        break;

                }

                return false;
            }
        });

        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        getFilteredEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_listevents, menu);
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
                eventAdapter.getFilter().filter(s);

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
                Intent intentCalendar = new Intent(ListEventsActivity.this, CalendarActivity.class);
                intentCalendar.putExtra("AccessToken", accessToken);
                intentCalendar.putExtra("userName", userName);
                intentCalendar.putExtra("userEmail", userEmail);

                startActivity(intentCalendar);

                ListEventsActivity.this.finish();

                return true;


            case R.id.action_add:
                Intent intentAddEvent = new Intent(ListEventsActivity.this, AddEventActivity.class);
                intentAddEvent.putExtra("AccessToken", accessToken);
                intentAddEvent.putExtra("userName", userName);
                intentAddEvent.putExtra("userEmail", userEmail);

                startActivity(intentAddEvent);

                ListEventsActivity.this.finish();

                return true;

            case R.id.action_filter:

                View menuItemView = findViewById(R.id.action_filter);

                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.YOURSTYLE);

                final PopupMenu popupMenu = new PopupMenu(wrapper, menuItemView);

                popupMenu.inflate(R.menu.filter_events_options);

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Menu menu = popupMenu.getMenu();

                        switch(menuItem.getItemId()){
                            case R.id.action_allEvents:

                                getUnfilteredEvents();

                                break;

                            case R.id.action_filterFuture:

                                getFilteredEvents();

                                break;

                        }

                        return false;
                    }
                });

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private void selectedItem(Integer item) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                eventsRecyclerView.getChildAt(item).setBackgroundColor(Color.TRANSPARENT);
                eventsClickedCount--;
                eventActionMode.setTitle(eventsClickedCount+ " Selected");

                if(eventsClickedCount == 0){
                    eventActionMode.finish();
                    actionModeEnabled = false;
                }
            } else {
                selectedItems.add(item);
                eventsRecyclerView.getChildAt(item).setBackgroundColor(Color.LTGRAY);
                eventsClickedCount++;
                eventActionMode.setTitle(eventsClickedCount+ " Selected");
            }
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.delete_navigation, menu);
            multiSelect = true;
            actionModeEnabled = true;
            eventActionMode = actionMode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.action_delete){
                try {
                    deleteEvents(selectedItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                actionModeEnabled = false;
                actionMode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            multiSelect = false;
            actionModeEnabled = false;
            eventsClickedCount = 0;

            for (Integer item : selectedItems) {
                eventsRecyclerView.getChildAt(item).setBackgroundColor(Color.TRANSPARENT);
            }

            selectedEvents.clear();

        }
    };

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void getFilteredEvents() {
        Log.d(TAG, "Starting volley request to graph");
        Log.d(TAG, accessToken);

    /* Make sure we have a token to send to graph */
        if (accessToken == null) {
            if (accessToken == null){
                Intent logout = new Intent(ListEventsActivity.this, MainActivity.class);
                logout.putExtra("AccessToken", accessToken);
                logout.putExtra("userName", userName);
                logout.putExtra("userEmail", userEmail);

                startActivity(logout);

                ListEventsActivity.this.finish();
            }
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }

        String getUrl = MSGRAPH_URL + formattedDate + "')";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                try {
                    updateFiltered(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());

                loadFilteredData();
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

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void getUnfilteredEvents() {
        Log.d(TAG, "Starting volley request to graph");
        Log.d(TAG, accessToken);

    /* Make sure we have a token to send to graph */
        if (accessToken == null) {
            Intent logout = new Intent(ListEventsActivity.this, MainActivity.class);
            logout.putExtra("AccessToken", accessToken);
            logout.putExtra("userName", userName);
            logout.putExtra("userEmail", userEmail);

            startActivity(logout);

            ListEventsActivity.this.finish();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UNFILTERED_EVENTS,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                try {
                    updateUnfiltered(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
                loadUnfilteredData();
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
    private void updateFiltered(JSONObject graphResponse) throws JSONException {

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

            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
            eventsRecyclerView.setLayoutManager(manager);
            eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            eventsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            eventAdapter = new EventAdapter(this, events);
            eventsRecyclerView.setAdapter(eventAdapter);

            saveFilteredData();

            eventsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), eventsRecyclerView, new ListMailsActvity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (actionModeEnabled) {
                        selectedItem(position);

                    } else {

                        if (events.size() != 0) {

                            Event event = eventAdapter.getItemAtPosition(position);

                            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
                            showEventDetails.putExtra("userEmail", userEmail);
                            showEventDetails.putExtra("AccessToken", accessToken);
                            showEventDetails.putExtra("userName", userName);
                            showEventDetails.putExtra("eventId", event.getId());
                            showEventDetails.putExtra("event", event);

                            startActivity(showEventDetails);

                            ListEventsActivity.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Empty event list!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onLongClick(View view, int position) {
                    view.startActionMode(actionModeCallback);
                    selectedItem(position);
                }
            }));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /* Sets the Graph response */
    private void updateUnfiltered(JSONObject graphResponse) throws JSONException {

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

            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
            eventsRecyclerView.setLayoutManager(manager);
            eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            eventsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            eventAdapter = new EventAdapter(this, events);
            eventsRecyclerView.setAdapter(eventAdapter);

            saveUnfilteredData();

            eventsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), eventsRecyclerView, new ListMailsActvity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (actionModeEnabled) {
                        selectedItem(position);

                    } else {

                        if (events.size() != 0) {

                            Event event = eventAdapter.getItemAtPosition(position);

                            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
                            showEventDetails.putExtra("userEmail", userEmail);
                            showEventDetails.putExtra("AccessToken", accessToken);
                            showEventDetails.putExtra("userName", userName);
                            showEventDetails.putExtra("eventId", event.getId());
                            showEventDetails.putExtra("event", event);

                            startActivity(showEventDetails);

                            ListEventsActivity.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Empty event list!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onLongClick(View view, int position) {
                    view.startActionMode(actionModeCallback);
                    selectedItem(position);
                }
            }));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // PATCH REQUEST VOOR DELETEN EVENT
    private void deleteEvent(String eventId) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        String postAddress = URL_DELETE + eventId;

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
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        getFilteredEvents();

        swipeRefreshLayout.setRefreshing(false);

    }

    public void onEventClicked(int position){


        if(events.size() != 0){

            Event event = eventAdapter.getItemAtPosition(position);

            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
            showEventDetails.putExtra("userEmail", userEmail);
            showEventDetails.putExtra("AccessToken", accessToken);
            showEventDetails.putExtra("userName", userName);
            showEventDetails.putExtra("id", event.getId());
            showEventDetails.putExtra("subject", event.getSubject());
            showEventDetails.putExtra("reminderMinutesBeforeStart", event.getReminderMinutesBeforeStart());
            showEventDetails.putExtra("displayAs", event.getShowAs());

            if(event.getLocation() == null){
                showEventDetails.putExtra("location", "");
            }
            else {
                showEventDetails.putExtra("location", event.getLocation().getDisplayName());
            }

            if(event.getStart() == null){
                showEventDetails.putExtra("startDate", "");
            }
            else {
                showEventDetails.putExtra("startDate", event.getStart().getDateTime());
            }

            if(event.getBody().getContent() != null){
                showEventDetails.putExtra("notes", event.getBody().getContent());
            }
            else {
                showEventDetails.putExtra("notes", "");
            }

            if(event.getBody().getContentType() != null){

                showEventDetails.putExtra("contentType", event.getBody().getContentType());

            }

            String eventBody = event.getBody().getContent();

            if(eventBody != null){
                showEventDetails.putExtra("notes", eventBody);
            }
            else {
                showEventDetails.putExtra("notes", "");
            }

            showEventDetails.putExtra("sensitivity", event.getSensitivity());

            showEventDetails.putExtra("responseRequested", event.isResponseRequested());

            if (event.getAttendees() != null) {
                showEventDetails.putExtra("attendeesList", (Serializable) event.getAttendees());
            }

            startActivity(showEventDetails);

            ListEventsActivity.this.finish();


        } else {
            Toast.makeText(getApplicationContext(), "Empty events list!", Toast.LENGTH_SHORT).show();
        }
    }

    // PATCH REQUEST VOOR DELETEN EVENTS
    private void deleteEvents(ArrayList<Integer> selectedItems) throws JSONException {

        this.selectedItems = selectedItems;

        for (Integer integer : selectedItems) {
            RequestQueue queue = Volley.newRequestQueue(this);

            Event event = events.get(integer);

            String postAddress = URL_DELETE + event.getId();

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postAddress,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Events deleted!", Toast.LENGTH_SHORT).show();
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


        eventAdapter.notifyDataSetChanged();

        int DELAY_TIME=2000;

        //start your animation
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //this code will run after the delay time which is 2 seconds.

                getFilteredEvents();

            }
        }, DELAY_TIME);
    }

    private void saveFilteredData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences events", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(events);

        editor.putString("event list", json);
        editor.apply();

    }

    private void loadFilteredData(){

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences events", MODE_PRIVATE);

        if(sharedPreferences.contains("event list")){
            Gson gson = new Gson();
            String json = sharedPreferences.getString("event list", null);
            Type type = new TypeToken<ArrayList<Event>>() {}.getType();
            events = gson.fromJson(json, type);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
            eventsRecyclerView.setLayoutManager(manager);
            eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            eventsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            eventAdapter = new EventAdapter(this, events);
            eventsRecyclerView.setAdapter(eventAdapter);

            eventsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), eventsRecyclerView, new ListMailsActvity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (actionModeEnabled) {
                        selectedItem(position);

                    } else {

                        if (events.size() != 0) {

                            Event event = eventAdapter.getItemAtPosition(position);

                            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
                            showEventDetails.putExtra("userEmail", userEmail);
                            showEventDetails.putExtra("AccessToken", accessToken);
                            showEventDetails.putExtra("userName", userName);
                            showEventDetails.putExtra("eventId", event.getId());
                            showEventDetails.putExtra("event", event);

                            startActivity(showEventDetails);

                            ListEventsActivity.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Empty event list!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onLongClick(View view, int position) {
                    view.startActionMode(actionModeCallback);
                    selectedItem(position);
                }
            }));

        }

        if(events == null){
            events = new ArrayList<>();
        }

    }

    private void saveUnfilteredData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences unfilteredevents", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(events);

        editor.putString("unfilteredevent list", json);
        editor.apply();

    }

    private void loadUnfilteredData(){

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences unfilteredevents", MODE_PRIVATE);

        if(sharedPreferences.contains("unfilteredevent list")){
            Gson gson = new Gson();
            String json = sharedPreferences.getString("unfilteredevent list", null);
            Type type = new TypeToken<ArrayList<Event>>() {}.getType();
            events = gson.fromJson(json, type);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
            eventsRecyclerView.setLayoutManager(manager);
            eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            eventsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            eventAdapter = new EventAdapter(this, events);
            eventsRecyclerView.setAdapter(eventAdapter);

            eventsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), eventsRecyclerView, new ListMailsActvity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (actionModeEnabled) {
                        selectedItem(position);

                    } else {

                        if (events.size() != 0) {

                            Event event = eventAdapter.getItemAtPosition(position);

                            Intent showEventDetails = new Intent(ListEventsActivity.this, EventDetailsActivity.class);
                            showEventDetails.putExtra("userEmail", userEmail);
                            showEventDetails.putExtra("AccessToken", accessToken);
                            showEventDetails.putExtra("userName", userName);
                            showEventDetails.putExtra("eventId", event.getId());
                            showEventDetails.putExtra("event", event);

                            startActivity(showEventDetails);

                            ListEventsActivity.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Empty event list!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onLongClick(View view, int position) {
                    view.startActionMode(actionModeCallback);
                    selectedItem(position);
                }
            }));

        }

        if(events == null){
            events = new ArrayList<>();
        }

    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(ListEventsActivity.this, CalendarActivity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        ListEventsActivity.this.finish();
    }

}
