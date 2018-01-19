package com.example.keiichi.project_mobile.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import jp.wasabeef.richeditor.RichEditor;

public class ReplyAllActivity extends AppCompatActivity {
    private String accestoken;
    private String accessToken;
    private String userName;
    private String userEmail;
    private Message message;
    private RichEditor Editor;
    private MenuItem sendItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_all);

        Intent intent = getIntent();

        accestoken = intent.getStringExtra("accestoken");
        message = (Message) intent.getSerializableExtra("mail");
        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        Editor = findViewById(R.id.editor);
        EditText textMailAdress = findViewById(R.id.TextMailAdress);
        EditText textMailSubject = findViewById(R.id.TextMailSubject);

        textMailAdress.setText(message.getFrom().getEmailAddress().getAddress());
        textMailSubject.setText("RE: " + message.getSubject());

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void ReplyToMail() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        if(accestoken == null){
            Intent logout = new Intent(ReplyAllActivity.this, MainActivity.class);
            logout.putExtra("AccessToken", accessToken);
            logout.putExtra("userName", userName);
            logout.putExtra("userEmail", userEmail);

            startActivity(logout);

            ReplyAllActivity.this.finish();
        }

        final JSONObject jsonObject = new JSONObject(buildJsonMail());

        System.out.println(jsonObject.toString());

        String replyUrl = "https://graph.microsoft.com/v1.0/users/me/messages/";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, replyUrl + message.getId() + "/replyAll", jsonObject,
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
                headers.put("Authorization", "Bearer " + accestoken);

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    private String buildJsonMail() {
        JsonObjectBuilder factory = Json.createObjectBuilder()
                .add("comment", Html.fromHtml(Editor.getHtml()).toString());
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

                finish();

                return true;

            case R.id.action_send:
                try {
                    sendItem.setEnabled(false);

                    ReplyToMail();
                    finish();
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
}
