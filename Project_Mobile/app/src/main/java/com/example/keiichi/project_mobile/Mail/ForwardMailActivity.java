package com.example.keiichi.project_mobile.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.Calendar.ListEventsActivity;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import jp.wasabeef.richeditor.RichEditor;

public class ForwardMailActivity extends AppCompatActivity {

    final private String URL_FORWARD = "https://graph.microsoft.com/v1.0/me/messages/";
    private String accessToken;
    private String userName;
    private String userEmail;
    private String mailId;
    private String mailSubject;
    private String mailAddress;
    private String senderName;
    private String timeSent;
    private String receiverName;
    private String receiverMail;
    private String messageBody;
    private String contentType;
    private Message messageObject;
    private Toolbar myToolbar;
    private EditText TextMailAdress,TextMailSubject;
    private RichEditor Editor;
    private MenuItem sendItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_mail);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        mailId = getIntent().getStringExtra("mailId");
        mailSubject = getIntent().getStringExtra("mailSubject");
        mailAddress = getIntent().getStringExtra("mailAddress");
        senderName = getIntent().getStringExtra("senderName");
        timeSent = getIntent().getStringExtra("timeSent");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverMail = getIntent().getStringExtra("receiverMail");
        messageBody = getIntent().getStringExtra("messageBody");
        messageObject = (Message) getIntent().getSerializableExtra("mail");
        contentType = getIntent().getStringExtra("contentType");

        TextMailAdress = findViewById(R.id.TextMailAdress);
        TextMailSubject = findViewById(R.id.TextMailSubject);

        TextMailSubject.setText("forward: " + mailSubject);


        Editor = findViewById(R.id.editor);

        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private void forwardMail() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        if(accessToken == null){
            Intent logout = new Intent(ForwardMailActivity.this, MainActivity.class);
            logout.putExtra("AccessToken", accessToken);
            logout.putExtra("userName", userName);
            logout.putExtra("userEmail", userEmail);

            startActivity(logout);

            ForwardMailActivity.this.finish();
        }

        final JSONObject jsonObject = new JSONObject(buildJsonMail());

        System.out.println(jsonObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_FORWARD + mailId + "/forward", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Mail send!", Toast.LENGTH_SHORT).show();
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

    private String buildJsonMail() {
        JsonObjectBuilder factory = Json.createObjectBuilder()
                .add("comment", Html.fromHtml(Editor.getHtml()).toString())
                .add("toRecipients", Json.createArrayBuilder().
                add(Json.createObjectBuilder().
                        add("emailAddress", Json.createObjectBuilder().
                                add("address", TextMailAdress.getText().toString()))));
        return factory.build().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_navigation, menu);
        MenuItem addItem = menu.findItem(R.id.action_send);

        sendItem = menu.findItem(R.id.action_send);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:

                Intent intentDisplayMail = new Intent(ForwardMailActivity.this, DisplayMailActivity.class);
                intentDisplayMail.putExtra("AccessToken", accessToken);
                intentDisplayMail.putExtra("userName", userName);
                intentDisplayMail.putExtra("userEmail", userEmail);
                intentDisplayMail.putExtra("mailId", mailId);
                intentDisplayMail.putExtra("mailSubject", mailSubject);
                intentDisplayMail.putExtra("mailAddress", mailAddress);
                intentDisplayMail.putExtra("senderName", senderName);
                intentDisplayMail.putExtra("timeSent", timeSent);
                intentDisplayMail.putExtra("receiverName", receiverName);
                intentDisplayMail.putExtra("receiverMail", receiverMail);
                intentDisplayMail.putExtra("messageBody", messageBody);
                intentDisplayMail.putExtra("mail", messageObject);
                intentDisplayMail.putExtra("contentType", contentType);

                startActivity(intentDisplayMail);

                ForwardMailActivity.this.finish();


                return true;

            case R.id.action_send:
                try {
                    sendItem.setEnabled(false);

                    forwardMail();
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

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentDisplayMail = new Intent(ForwardMailActivity.this, DisplayMailActivity.class);
        intentDisplayMail.putExtra("AccessToken", accessToken);
        intentDisplayMail.putExtra("userName", userName);
        intentDisplayMail.putExtra("userEmail", userEmail);
        intentDisplayMail.putExtra("mailId", mailId);
        intentDisplayMail.putExtra("mailSubject", mailSubject);
        intentDisplayMail.putExtra("mailAddress", mailAddress);
        intentDisplayMail.putExtra("senderName", senderName);
        intentDisplayMail.putExtra("timeSent", timeSent);
        intentDisplayMail.putExtra("receiverName", receiverName);
        intentDisplayMail.putExtra("receiverMail", receiverMail);
        intentDisplayMail.putExtra("messageBody", messageBody);
        intentDisplayMail.putExtra("mail", messageObject);
        intentDisplayMail.putExtra("contentType", contentType);

        startActivity(intentDisplayMail);

        ForwardMailActivity.this.finish();
    }
}
