package com.example.keiichi.project_mobile.Mail;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
// import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;


import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.keiichi.project_mobile.Contacts.AddContactActivity;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.MailFolder;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.example.keiichi.project_mobile.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ListMailsActvity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Serializable {

    final private String URL_MAIL = "https://graph.microsoft.com/v1.0/me/messages/";
    final private String URL_MAIL_UPDATE = "https://graph.microsoft.com/beta/me/messages/";
    final private String PHOTO_REQUEST = "https://graph.microsoft.com/v1.0/me/photo/$value";
    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailFolders('Inbox')/messages?$top=25";
    final static String CHANNEL_ID = "my_channel_01";
    final static String URL_MAILFOLDERS = "https://graph.microsoft.com/v1.0/me/mailFolders";

    private NavigationView mailNavigationView;
    private ImageView userPicture;
    private DrawerLayout mDrawerLayout;
    private String test;
    private Toolbar myToolbar;
    private SearchView searchView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Drawer drawer;
    private boolean multiSelect = false;
    private boolean actionModeEnabled = false;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<MailFolder> mailFolders;
    private ArrayList<String> mailFolderNames = new ArrayList<>();
    private ArrayList<IDrawerItem> drawerItems = new ArrayList<>();


    private Button attachmentButton;

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            multiSelect = true;
            actionModeEnabled = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            try {
                deleteMails(selectedItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Integer integer : selectedItems) {
                finalMailJsonArray.remove(integer);
            }
            actionModeEnabled = false;
            actionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            multiSelect = false;
            for (Integer item : selectedItems) {
                recyclerView.getChildAt(item).setBackgroundColor(Color.TRANSPARENT);
            }

            selectedItems.clear();
            mailAdapter.notifyDataSetChanged();

        }
    };

    final static String CLIENT_ID = "d3b60662-7768-4a50-b96f-eb1dfcc7ec8d";
    final static String SCOPES[] = {
            "https://graph.microsoft.com/Mail.Send",
            "https://graph.microsoft.com/Mail.ReadWrite",
            "https://graph.microsoft.com/Calendars.ReadWrite",
            "https://graph.microsoft.com/Calendars.Read",
            "https://graph.microsoft.com/Contacts.Read",
            "https://graph.microsoft.com/Contacts.ReadWrite",
            "https://graph.microsoft.com/Calendars.ReadWrite"};

    //final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    private JSONObject graphResponse;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MailAdapter mailAdapter;

    private String accessToken;
    private String userName;
    private String userEmail;

    BottomNavigationView mBottomNav;


    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    // Button callGraphButton;
    Button signOutButton;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;
    private JSONArray finalMailJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mails);

        userPicture = (ImageView) findViewById(R.id.userPicture);
        recyclerView = findViewById(R.id.ListViewMails);
        signOutButton = findViewById(R.id.clearCache);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //mDrawerLayout = findViewById(R.id.drawer_layout);

        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.drawer_open,
        //        R.string.drawer_close);

        //mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //addNotification();

        mBottomNav = findViewById(R.id.navigation);

        ///mailNavigationView = findViewById(R.id.mailNavigationView);
        //View hView = mailNavigationView.getHeaderView(0);
        //TextView nav_userName = hView.findViewById(R.id.userName);
        //TextView nav_userEmail = hView.findViewById(R.id.userEmail);
        //nav_userName.setText(userName);
        //nav_userEmail.setText(userEmail);

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_calendar:
                        Intent intentCalendar = new Intent(ListMailsActvity.this, CalendarActivity.class);
                        intentCalendar.putExtra("AccessToken", accessToken);
                        intentCalendar.putExtra("userName", userName);
                        intentCalendar.putExtra("userEmail", userEmail);
                        startActivity(intentCalendar);
                        break;
                    case R.id.action_mail:

                        break;
                    case R.id.action_user:
                        Intent intentContacts = new Intent(ListMailsActvity.this, ContactsActivity.class);
                        intentContacts.putExtra("AccessToken", accessToken);
                        intentContacts.putExtra("userName", userName);
                        intentContacts.putExtra("userEmail", userEmail);
                        startActivity(intentContacts);
                        break;

                }

                return false;
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

        callGraphAPI();

        getMailFolders();

        /*
        NavigationView navigationView = (NavigationView) findViewById(R.id.mailNavigationView);
        View navView =  navigationView.getHeaderView(0);
        ImageView userPicture = (ImageView)navView.findViewById(R.id.userPicture);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(userName.substring(0,1));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(userName.substring(0,1), color2); // radius in px

        userPicture.setImageDrawable(drawable);
        */

        mailFolders = new ArrayList<>();
        mailFolders.add(new MailFolder("1", "Not found", 0, 0));

        buildDrawer(userName, userEmail, myToolbar, mailFolders);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //actionBarDrawerToggle.syncState();
    }



    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

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
                headers.put("Authorization", "Bearer " +accessToken);
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

        //beetje kloten met mails
        System.out.println(graphResponse);
        this.graphResponse = graphResponse;
        getMails(graphResponse);


    }

    /* Clears a user's tokens from the cache.
 * Logically similar to "sign out" but only signs out of this app.
 */
    private void onSignOutClicked() {

    /* Attempt to get a user and remove their cookies from cache */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users == null) {
            /* We have no users */

            } else if (users.size() == 1) {
            /* We have 1 user */
            /* Remove from token cache */
                sampleApp.remove(users.get(0));
                updateSignedOutUI();

            } else {
            /* We have multiple users */
                for (int i = 0; i < users.size(); i++) {
                    sampleApp.remove(users.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    /* Set the UI for signed-out user */
    private void updateSignedOutUI() {
        //callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
    }


    private void addNotification() {
        android.support.v4.app.NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Hey boo")
                        .setContentText("Wanna cuddle?")
                        .setSmallIcon(R.drawable.bootje);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        try {
            getMails(graphResponse);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);


    }

    void selectedItem(Integer item) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                recyclerView.getChildAt(item).setBackgroundColor(Color.TRANSPARENT);
            } else {
                selectedItems.add(item);
                recyclerView.getChildAt(item).setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void deleteMails(ArrayList<Integer> selectedItems) throws JSONException {
        this.selectedItems = selectedItems;

        for (Integer integer : selectedItems) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject mail = finalMailJsonArray.getJSONObject(integer);


            StringRequest objectRequest = new StringRequest(Request.Method.DELETE, URL_MAIL + mail.getString("id"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Mail deleted!", Toast.LENGTH_SHORT).show();
                            System.out.println(response);
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

    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void getMailFolders() {
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_MAILFOLDERS,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                try {
                    updateFolders(response);
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
    private void updateFolders(JSONObject graphResponse) throws JSONException {

        // Test de response
        JSONArray contactsJsonArray = null;

        // Haal de mailfolders binnen
        try {
            JSONObject folderList = graphResponse;

            JSONArray folders = folderList.getJSONArray("value");

            // VUL POJO
            Type listType = new TypeToken<List<MailFolder>>() {
            }.getType();

            mailFolders = new Gson().fromJson(String.valueOf(folders), listType);

            buildDrawer(userName, userEmail, myToolbar, mailFolders);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void updateMailIsRead(Message message, String id) {
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

        String patchUrl = URL_MAIL_UPDATE + id;

        JsonObjectRequest objectRequest = null;
        try {
            objectRequest = new JsonObjectRequest(Request.Method.PATCH, patchUrl, new JSONObject(new Gson().toJson(message)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Mail updated!", Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue.add(objectRequest);
    }

    /* Sets the Graph response */
    private void updateIsRead(JSONObject graphResponse) throws JSONException {

        // Haal de mailfolders binnen
        try {
            JSONObject list = graphResponse;

            JSONArray mails = list.getJSONArray("value");

            // VUL POJO
            Type listType = new TypeToken<List<Message>>() {
            }.getType();

            messages = new Gson().fromJson(String.valueOf(mails), listType);

            if(!messages.isEmpty()) {

                for (Message message : messages) {
                    // RANDOM COLOR OF ICON
                }

                mailAdapter = new MailAdapter(this, messages);
                recyclerView.setAdapter(mailAdapter);

                mailAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getMails(JSONObject graphResponse) throws JSONException {

        //haal mails binnen

        JSONObject messageList = graphResponse;

        JSONArray messagesArray = messageList.getJSONArray("value");

        System.out.println("test response: " + messagesArray);

        // VUL POJO
        Type listType = new TypeToken<List<Message>>() {
        }.getType();

        messages = new Gson().fromJson(String.valueOf(messagesArray), listType);

        mailAdapter = new MailAdapter(this, messages);
        recyclerView.setAdapter(mailAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (actionModeEnabled) {
                    selectedItem(position);

                    Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();


                } else {
                    Message message = mailAdapter.getItemAtPosition(position);


                    Intent showMail = new Intent(ListMailsActvity.this, DisplayMailActivity.class);

                    if (!message.isRead()){

                        System.out.println("KEVIN BECKWEE");

                        String id = message.getId();
                        message.setRead(true);

                        showMail.putExtra("isRead", "yes");

                        //updateMailIsRead(message, id);

                    }

                    showMail.putExtra("messageBody", message.getBody().getContent());
                    showMail.putExtra("AccessToken", accessToken);
                    showMail.putExtra("userName", userName);
                    showMail.putExtra("userEmail", userEmail);
                    showMail.putExtra("mailId", message.getId());
                    showMail.putExtra("mailSubject", message.getSubject());
                    showMail.putExtra("mailAddress", message.getFrom().getEmailAddress().getAddress());
                    showMail.putExtra("senderName", message.getFrom().getEmailAddress().getName());
                    showMail.putExtra("timeSent", message.getReceivedDateTime());

                    if(!message.getToRecipients().isEmpty()){
                        showMail.putExtra("receiverName", message.getToRecipients().get(0).getEmailAddress().getName());
                    }

                    if(!message.getToRecipients().isEmpty()){
                        showMail.putExtra("receiverMail", message.getToRecipients().get(0).getEmailAddress().getAddress());
                    }

                    showMail.putExtra("contentType", message.getBody().getContentType());
                    showMail.putExtra("messageObject", message);

                    startActivity(showMail);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                view.startActionMode(actionModeCallback);
                selectedItem(position);


            }
        }));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_action_bar_items_contacts, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);



        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search: Inbox...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mailAdapter.getFilter().filter(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mailAdapter.getFilter().filter(s);

                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){



            case R.id.action_add:
                Intent intentSendMail = new Intent(ListMailsActvity.this, SendMailActivity.class);
                intentSendMail.putExtra("AccessToken", accessToken);
                intentSendMail.putExtra("userName", userName);
                intentSendMail.putExtra("userEmail", userEmail);
                startActivity(intentSendMail);
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

    public void buildDrawer(String name, String email, Toolbar toolbar, List<MailFolder> folders){

        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();

        for(MailFolder folder : folders) {
            PrimaryDrawerItem item = new PrimaryDrawerItem();

            String folderName = folder.getDisplayName().toLowerCase();

            switch(folderName) {

                case "archive":
                    mailFolderNames.add("Archive");

                    break;

                case "boxer":
                    mailFolderNames.add("Boxer");

                    break;

                case "concepten":
                    mailFolderNames.add("Drafts");

                    break;

                case "conversation history":
                    mailFolderNames.add("Conversation History");

                    break;

                case "onbelangrijke e-mail":
                    mailFolderNames.add("Unimportant E-Mail");

                    break;

                case "ongewenste e-mail":
                    mailFolderNames.add("Junk E-Mail");

                    break;

                case "postvak in":
                    mailFolderNames.add("Inbox");

                    break;

                case "postvak uit":
                    mailFolderNames.add("Outbox");

                    break;

                case "verwijderde items":
                    mailFolderNames.add("Deleted Items");

                    break;

                case "verzonden items":
                    mailFolderNames.add("Sent Items");

                    break;

                default:
                    mailFolderNames.add(folder.getDisplayName());
                    break;
            }

            /*
            if(folder.getUnreadItemCoungetDisplayNamet() == 0){
                item.withName(folder.getDisplayName())
                        .withIcon(R.drawable.ic_mail);
            } else {
                item.withName(folder.getDisplayName())
                        .withIcon(R.drawable.ic_mail);
            }
            */
            item.withTag(folder);
            drawerItems.add(item);
        }

        for (String mailFolderName : mailFolderNames){


        }

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(userName.substring(0,1));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(userName.substring(0,1), color2); // radius in px

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.action_bar)
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
                .build();


    }

}
