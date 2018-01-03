package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Calendar.CalendarActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.DAL.POJOs.PhysicalAddress;
import com.example.keiichi.project_mobile.Mail.ListMailsActvity;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {

    BottomNavigationView mBottomNav;

    Toolbar myToolbar;

    private TextDrawable drawable;
    private ImageView profilePicture;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private ListView contactsListView;

    private SearchView searchView;

    NavigationView contactNavigationView;

    ContactAdapter contactAdapter;

    private List<Contact> contacts = new ArrayList<>();

    private List<EmailAddress> emailList;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String id;

    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/contacts?$orderBy=displayName&$top=500&$count=true";
    final static String MSGRAPH_URL_FOTO = "https://graph.microsoft.com/beta/me/contacts/";
    final static String MSGRAPH_URL_FOTO2 = "/photo/$value";

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);


        contactsListView = (ListView) findViewById(R.id.contactsListView);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);

                onContactClicked(position);

            }
        });


        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        id = getIntent().getStringExtra("id");

        getProfilePhotos();

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        contactNavigationView = (NavigationView) findViewById(R.id.contactNavigationView);
        View hView =  contactNavigationView.getHeaderView(0);
        TextView nav_userName = (TextView)hView.findViewById(R.id.userName);
        TextView nav_userEmail = (TextView)hView.findViewById(R.id.userEmail);
        nav_userName.setText(userName);
        nav_userEmail.setText(userEmail);

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        callGraphAPI();

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.action_calendar:
                        Intent intentCalendar = new Intent(ContactsActivity.this, CalendarActivity.class);
                        intentCalendar.putExtra("AccessToken", accessToken);
                        intentCalendar.putExtra("userName", userName);
                        intentCalendar.putExtra("userEmail", userEmail);
                        startActivity(intentCalendar);
                        break;
                    case R.id.action_mail:
                        Intent intentMail = new Intent(ContactsActivity.this, ListMailsActvity.class);
                        intentMail.putExtra("AccessToken", accessToken);
                        intentMail.putExtra("userName", userName);
                        intentMail.putExtra("userEmail", userEmail);
                        startActivity(intentMail);
                        break;
                    case R.id.action_user:

                        break;

                }

                return false;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.contactNavigationView);
        View navView =  navigationView.getHeaderView(0);
        ImageView userPicture = (ImageView)navView.findViewById(R.id.userPicture);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(userName.substring(0,1));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(userName.substring(0,1), color2); // radius in px

        userPicture.setImageDrawable(drawable);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_contacts, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);



        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search by name...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                contactAdapter.getFilter().filter(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contactAdapter.getFilter().filter(s);

                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){



            case R.id.action_add:
                Intent intentAddContact = new Intent(ContactsActivity.this, AddContactActivity.class);
                intentAddContact.putExtra("AccessToken", accessToken);
                intentAddContact.putExtra("userName", userName);
                intentAddContact.putExtra("userEmail", userEmail);
                startActivity(intentAddContact);
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
        JSONArray contactsJsonArray = null;

        // Haal de contacten binnen
        try {
            contactsJsonArray = (JSONArray) graphResponse.get("value");

            JSONObject contactList = graphResponse;

            JSONArray contactArray = contactList.getJSONArray("value");

            System.out.println("test response: " + contactArray);

            JSONArray sortedContactArray = new JSONArray();


            // VUL POJO
            Type listType = new TypeToken<List<Contact>>() {
            }.getType();

            contacts = new Gson().fromJson(String.valueOf(contactArray), listType);




            contactAdapter = new ContactAdapter(this, contacts);
            contactsListView.setAdapter(contactAdapter);






        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert contactsJsonArray != null;

        contactAdapter = new ContactAdapter(this, contacts );
        contactsListView.setAdapter(contactAdapter);

    }

    public void onContactClicked(int position){


        if(contacts.size() != 0){
            Contact contact = contacts.get(position);

            Intent showContactDetails = new Intent(ContactsActivity.this, ContactsDetailsActivity.class);
            showContactDetails.putExtra("givenName", contact.getGivenName());
            showContactDetails.putExtra("displayName", contact.getDisplayName());
            showContactDetails.putExtra("id", contact.getId());

            if(contact.getMobilePhone() == null){
                showContactDetails.putExtra("userPhone", "");
            }
            else {
                showContactDetails.putExtra("userPhone", contact.getMobilePhone());
            }

            if(contact.getEmailAddresses() != null){
                showContactDetails.putExtra("emailList",(Serializable) contact.getEmailAddresses());
            }

            if(contact.getPersonalNotes() != null){
                showContactDetails.putExtra("notes", contact.getPersonalNotes());
            }
            else {
                showContactDetails.putExtra("notes", "");
            }

            if(contact.getSpouseName() != null){
                showContactDetails.putExtra("spouse", contact.getSpouseName());
            }
            else {
                showContactDetails.putExtra("spouse", "");
            }

            if(contact.getNickName() != null){
                showContactDetails.putExtra("nickname", contact.getNickName());
            }
            else {
                showContactDetails.putExtra("nickname", "");
            }

            if(contact.getJobTitle() != null){
                showContactDetails.putExtra("job", contact.getJobTitle());
            } else {
                showContactDetails.putExtra("job", "");
            }

            if(contact.getDepartment() != null){
                showContactDetails.putExtra("department", contact.getDepartment());
            } else {
                showContactDetails.putExtra("department", "");
            }

            if(contact.getCompanyName() != null){
                showContactDetails.putExtra("company", contact.getCompanyName());
            } else {
                showContactDetails.putExtra("company", "");
            }

            if(contact.getOfficeLocation() != null) {
                showContactDetails.putExtra("office", contact.getOfficeLocation());
            } else {
                showContactDetails.putExtra("office", "");
            }

            if(contact.getManager() != null){
                showContactDetails.putExtra("manager", contact.getManager());
            } else {
                showContactDetails.putExtra("manager", "");
            }

            if(contact.getAssistantName() != null){
                showContactDetails.putExtra("assistant", contact.getAssistantName());
            } else {
                showContactDetails.putExtra("assistant", "");
            }

            if(contact.getHomeAddress() != null){
                showContactDetails.putExtra("street",contact.getHomeAddress().getStreet());
                showContactDetails.putExtra("postalcode",contact.getHomeAddress().getPostalCode());
                showContactDetails.putExtra("city",contact.getHomeAddress().getCity());
                showContactDetails.putExtra("state",contact.getHomeAddress().getState());
                showContactDetails.putExtra("country",contact.getHomeAddress().getCountryOrRegion());
            } else {
                showContactDetails.putExtra("street", "");
                showContactDetails.putExtra("postalcode","");
                showContactDetails.putExtra("city","");
                showContactDetails.putExtra("state", "");
                showContactDetails.putExtra("country", "");
            }

            if(contact.getHomeAddress().getStreet() == null){
                showContactDetails.putExtra("street", "");
            }

            if(contact.getHomeAddress().getPostalCode() == null){
                showContactDetails.putExtra("postalcode", "");
            }

            if(contact.getHomeAddress().getCity() == null){
                showContactDetails.putExtra("city", "");
            }

            if(contact.getHomeAddress().getState() == null){
                showContactDetails.putExtra("state", "");
            }

            if(contact.getHomeAddress().getCountryOrRegion() == null){
                showContactDetails.putExtra("country", "");
            }

            if(contact.getGivenName() != null){
                showContactDetails.putExtra("firstname",contact.getGivenName());
            }

            if(contact.getSurname() != null){
                showContactDetails.putExtra("lastname",contact.getSurname());
            }

            showContactDetails.putExtra("userEmail", userEmail);
            showContactDetails.putExtra("AccessToken", accessToken);
            showContactDetails.putExtra("userName", userName);


            startActivity(showContactDetails);

        } else {
            Toast.makeText(getApplicationContext(), "Empty contact list!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getProfilePhotos(){
        Log.d(TAG, "Starting volley request to graph");
        System.out.println("auw papa");
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

        String contactId = id;

        String PHOTO_URL = "https://graph.microsoft.com/beta/me/photo/$value";


        StringRequest request = new StringRequest(Request.Method.GET, PHOTO_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            /* Successfully called graph, process data and send to UI */
                System.out.println("Response fotos: " + response);

                System.out.println("papa aaauwwwwwwwwwww: " +response.getClass().getName());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
                System.out.println("papa stop");
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





}
