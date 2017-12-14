package com.example.keiichi.project_mobile.Mail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
// import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;


import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.HEAD;

public class ListMailsActvity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private boolean multiSelect = false;
    private boolean actionModeEnabled = false;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
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
    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me/mailFolders('Inbox')/messages?$top=25";
    final static String CHANNEL_ID = "my_channel_01";

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
    Button toSendMailActivity;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;
    private JSONArray finalMailJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mails);

        recyclerView = findViewById(R.id.ListViewMails);
        signOutButton = findViewById(R.id.clearCache);
        toSendMailActivity = findViewById(R.id.ButtonSendMail);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        addNotification();

        mBottomNav = findViewById(R.id.navigation);

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

        toSendMailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSendMailActivity();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

  /* Configure your sample app and save state for this activity */
        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    CLIENT_ID);
        }

  /* Attempt to get a user and acquireTokenSilent
   * If this fails we do an interactive request
   */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
          /* We have 1 user */

                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), getAuthSilentCallback());
            } else {
          /* We have no user */

          /* Let's do an interactive request */
                sampleApp.acquireToken(this, SCOPES, getAuthInteractiveCallback());
            }
        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
        if (accessToken == null) {
            onCallGraphClicked();
        }


    }


//
// App callbacks for MSAL
// ======================
// getActivity() - returns activity so we can acquireToken within a callback
// getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
// getAuthInteractiveCallback() - callback defined to handle acquireToken() case
//

    public Activity getActivity() {
        return this;
    }

    /* Callback method for acquireTokenSilent calls
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
            /* Successfully got a token, call Graph now */
                Log.d(TAG, "Successfully authenticated");

                //Print auth result om te dubbel checken
                System.out.println(Arrays.toString(authenticationResult.getScope()));

            /* Store the authResult */
                authResult = authenticationResult;

                // accesstoken en andere user vars in var steken
                accessToken = authResult.getAccessToken();
                userName = authResult.getUser().getName();
                userEmail = authResult.getUser().getDisplayableId();


            /* call graph */
                callGraphAPI();

            /* update the UI to post call Graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
            /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
            /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }


    /* Callback used for interactive request.  If succeeds we use the access
         * token to call the Microsoft Graph. Does not check cache
         */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
            /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());
                Log.d(TAG, "Acces Token: " + authenticationResult.getAccessToken());

            /* Store the auth result */
                authResult = authenticationResult;

            /* call Graph */
                callGraphAPI();

            /* update the UI to post call Graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
            /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
            /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI() {
        signOutButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                authResult.getUser().getName());
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
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
        if (authResult.getAccessToken() == null) {
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
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
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
        JSONArray mailJsonArray = null;
        //haal mails binnen
        try {
            mailJsonArray = (JSONArray) graphResponse.get("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert mailJsonArray != null;
        JSONObject object = mailJsonArray.getJSONObject(1);
        System.out.println(object.get("from"));

        this.finalMailJsonArray = mailJsonArray;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mailAdapter = new MailAdapter(this, finalMailJsonArray);
        recyclerView.setAdapter(mailAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (actionModeEnabled) {
                    selectedItem(position);
                    Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();


                } else {
                    Intent showMail = new Intent(ListMailsActvity.this, DisplayMailActivity.class);
                    try {
                        showMail.putExtra("mailObjext", finalMailJsonArray.getString(position));
                        showMail.putExtra("accestoken", authResult.getAccessToken());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(showMail);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "hey long boo", Toast.LENGTH_SHORT).show();
                view.startActionMode(actionModeCallback);
                selectedItem(position);

            }
        }));


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

    private void toSendMailActivity() {
        Intent intent = new Intent(this, SendMailActivity.class);
        intent.putExtra("accestoken", authResult.getAccessToken());

        startActivity(intent);
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
        Toast.makeText(getApplicationContext(), "hey boo", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);


    }

    void selectedItem(Integer item) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                recyclerView.getChildAt(item).setBackgroundColor(Color.WHITE);
            } else {
                selectedItems.add(item);
                recyclerView.getChildAt(item).setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    private void deleteMails(ArrayList<Integer> selectedItems) throws JSONException {
        this.selectedItems = selectedItems;

        for (Integer integer : selectedItems) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject mail = finalMailJsonArray.getJSONObject(integer);


            StringRequest objectRequest = new StringRequest(Request.Method.DELETE, URL_DELETE + mail.getString("id"),
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
                    headers.put("Authorization", "Bearer " + authResult.getAccessToken());

                    return headers;
                }

            };

            queue.add(objectRequest);
        }

    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}
