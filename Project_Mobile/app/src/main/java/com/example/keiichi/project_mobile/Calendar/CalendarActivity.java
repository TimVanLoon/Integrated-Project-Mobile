package com.example.keiichi.project_mobile.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.DAL.POJOs.MailFolder;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.example.keiichi.project_mobile.SettingsActivity;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarManager;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements CalendarPickerController {

    private CalendarView calendarView;
    private TextView myDate;
    private BottomNavigationView mBottomNav;
    private SearchView searchView;

    private static final String LOG_TAG = CalendarActivity.class.getSimpleName();
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/events?$orderby=start/dateTime&$top=500&$count=true";

    private AgendaCalendarView mAgendaCalendarView;
    private Drawer drawer;
    private String accessToken;
    private String userName;
    private String userEmail;
    private JSONArray eventsArray;
    private List<Event> events = new ArrayList<>();
    private List<CalendarEvent> eventList = new ArrayList<>();
    private EventAdapter eventAdapter;

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();

    // minimum and maximum date of our calendar
    // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mAgendaCalendarView = (AgendaCalendarView) findViewById(R.id.agenda_calendar_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        callGraphAPI();

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        // callGraphAPI();

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.action_calendar:

                        break;
                    case R.id.action_mail:
                        Intent intentMail = new Intent(CalendarActivity.this, ListMailsActvity.class);
                        intentMail.putExtra("AccessToken", accessToken);
                        intentMail.putExtra("userName", userName);
                        intentMail.putExtra("userEmail", userEmail);

                        startActivity(intentMail);

                        CalendarActivity.this.finish();

                        break;
                    case R.id.action_user:
                        Intent intentContacts = new Intent(CalendarActivity.this, ContactsActivity.class);
                        intentContacts.putExtra("AccessToken", accessToken);
                        intentContacts.putExtra("userName", userName);
                        intentContacts.putExtra("userEmail", userEmail);

                        startActivity(intentContacts);

                        CalendarActivity.this.finish();

                        break;

                }

                return false;
            }
        });

        List<MailFolder> mailFolders = new ArrayList<>();
        mailFolders.add(new MailFolder("1", "Not found", 0, 0));

        buildDrawer(userName, userEmail, myToolbar, mailFolders);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_calendar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //contactAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.action_listEvents:


                Intent intentListEvents = new Intent(CalendarActivity.this, ListEventsActivity.class);
                intentListEvents.putExtra("AccessToken", accessToken);
                intentListEvents.putExtra("userName", userName);
                intentListEvents.putExtra("userEmail", userEmail);

                startActivity(intentListEvents);

                CalendarActivity.this.finish();

                return true;

            // WANNEER + ICON WORDT AANGEKLIKT
            case R.id.action_add:

                Intent intentAddEvent = new Intent(CalendarActivity.this, AddEventActivity.class);
                intentAddEvent.putExtra("AccessToken", accessToken);
                intentAddEvent.putExtra("userName", userName);
                intentAddEvent.putExtra("userEmail", userEmail);

                startActivity(intentAddEvent);

                CalendarActivity.this.finish();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private void mockList(List<CalendarEvent> eventList) throws ParseException {

        for (Event event : events) {
            System.out.println("test events : " + event.getSubject());

            Calendar startTime1 = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            startTime1.setTime(output.parse(event.getStart().getDateTime()));
            Calendar endTime1 = Calendar.getInstance();
            endTime1.setTime(output.parse(event.getEnd().getDateTime()));

            ColorGenerator generator = ColorGenerator.MATERIAL;

            // generate random color
            int color1 = generator.getRandomColor();

            BaseCalendarEvent event1 = new BaseCalendarEvent(event.getSubject(), event.getBodyPreview(), event.getLocation().getDisplayName(),
                    color1, startTime1, endTime1, true);
            eventList.add(event1);
        }

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);


    }


    @Override
    public void onDaySelected(DayItem dayItem) {

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
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

            JSONObject eventsList = graphResponse;

            JSONArray eventArray = eventsList.getJSONArray("value");


            // VUL POJO
            Type listType = new TypeToken<List<Event>>() {
            }.getType();

            events = new Gson().fromJson(String.valueOf(eventArray), listType);

            mockList(eventList);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void buildDrawer(String name, String email, Toolbar toolbar, List<MailFolder> folders){

        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();

        for(MailFolder folder : folders) {
            PrimaryDrawerItem item = new PrimaryDrawerItem();

            String folderName = folder.getDisplayName().toLowerCase();

            switch(folderName) {

            }

            item.withTag(folder);
            item.withBadge(String.valueOf(folder.getUnreadItemCount())).withTextColor(Color.BLACK);
            drawerItems.add(item);

        }

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(userName.substring(0,1));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(userName.substring(0,1), color2); // radius in px

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorWhite)
                .withSelectionListEnabledForSingleProfile(false)
                .withTextColor(Color.BLACK)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(drawable)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult)
                .withDrawerItems(drawerItems)
                .withSelectedItemByPosition(7)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof PrimaryDrawerItem){


                        }
                        return false;
                    }
                })
                .build();

    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
