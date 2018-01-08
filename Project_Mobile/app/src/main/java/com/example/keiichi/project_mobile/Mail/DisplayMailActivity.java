package com.example.keiichi.project_mobile.Mail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.keiichi.project_mobile.Contacts.ContactsActivity;
import com.example.keiichi.project_mobile.Contacts.ContactsDetailsActivity;

import com.example.keiichi.project_mobile.DAL.POJOs.Attachment;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
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
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class DisplayMailActivity extends AppCompatActivity {

    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    private JSONObject mail, body;
    private TextView From, iconText, To,mailBodyContent,Subject;
    private ImageView icon_mail;
    private Toolbar myToolbar;
    private String ACCES_TOKEN, messageBody;

    private Message messageObject;

    private String accessToken;
    private String userName;
    private String userEmail;
    private String mailSubject;
    private String mailAddress;
    private String mailId;
    private  AlertDialog.Builder builder;


    private com.example.keiichi.project_mobile.DAL.POJOs.Message message;
    private Button attachmentButton;
    private ArrayList<Attachment> attachments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);

        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        mailId = getIntent().getStringExtra("mailId");
        mailSubject = getIntent().getStringExtra("mailSubject");
        mailAddress = getIntent().getStringExtra("mailAddress");

        Intent intent = getIntent();
        String mB = intent.getStringExtra("messageBody");
        String messageBody = mB.substring(mB.indexOf('\n')+1);
        ACCES_TOKEN = intent.getStringExtra("accestoken");
        message = (com.example.keiichi.project_mobile.DAL.POJOs.Message) intent.getSerializableExtra("mailObject");


        mailBodyContent = findViewById(R.id.mailBody);
        mailBodyContent.setMovementMethod(new ScrollingMovementMethod());
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);
        iconText = findViewById(R.id.icon_txt);
        icon_mail = findViewById(R.id.icon_profileMail);
        Subject = findViewById(R.id.Subject);
        attachmentButton = findViewById(R.id.attachmentButton);


        messageObject = (Message) intent.getSerializableExtra("mailObject");


        builder = new AlertDialog.Builder(DisplayMailActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Delete Mail");
        builder.setMessage("Are you sure you want to delete this mail?");

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
                    deleteMail();

                    int DELAY_TIME=2000;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentListMail = new Intent(DisplayMailActivity.this, ListMailsActvity.class);

                            intentListMail.putExtra("AccessToken", accessToken);
                            intentListMail.putExtra("userName", userName);
                            intentListMail.putExtra("userEmail", userEmail);

                            startActivity(intentListMail);


                        }
                    }, DELAY_TIME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        try {
            mail = new JSONObject(messageBody);
            body = mail.getJSONObject("body");
            mailBodyContent.setText(Html.fromHtml(body.getString("content")));
            JSONObject sender = mail.getJSONObject("from");
            JSONObject emailAddress = sender.getJSONObject("emailAddress");
            iconText.setText(emailAddress.getString("name").substring(0, 1));
            From.setText(emailAddress.getString("name"));
            JSONArray recipient = mail.getJSONArray("toRecipients");
            emailAddress = recipient.getJSONObject(0);
            To.setText(emailAddress.getJSONObject("emailAddress").getString("address"));
            Subject.setText(mail.getString("subject"));

            if (!messageObject.isRead()){
                updateMail(messageObject);

            }

            applyProfilePicture(icon_mail,iconText);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mailBodyContent.setText(Html.fromHtml(messageBody));
        

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getAttachments();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getAttachments() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_DELETE + messageObject.getId() + "/attachments", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        Type listType = new TypeToken<List<Attachment>>() {
                        }.getType();
                        try {
                            attachments = new Gson().fromJson(String.valueOf(response.getJSONArray("value")),listType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println(attachments.get(1).getId());
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
                headers.put("Authorization", "Bearer " + ACCES_TOKEN);


                return headers;
            }

        };

        queue.add(objectRequest);
    }

    private void toForwardMail() {
        Intent showMail = new Intent(DisplayMailActivity.this, ForwardMailActivity.class);

        showMail.putExtra("AccessToken", accessToken);
        showMail.putExtra("userName", userName);
        showMail.putExtra("userEmail", userEmail);
        showMail.putExtra("mailId", mailId);

        startActivity(showMail);

    }

    private void goToReplyActivity() {
        Intent showMail = new Intent(DisplayMailActivity.this, ReplyToMailActivity.class);

            showMail.putExtra("AccessToken", accessToken);
            showMail.putExtra("userName", userName);
            showMail.putExtra("userEmail", userEmail);
            showMail.putExtra("mailId", mailId);
            showMail.putExtra("mailSubject", mailSubject);
            showMail.putExtra("mailAddress", mailAddress);

        startActivity(showMail);
    }


    // PATCH REQUEST VOOR DELETEN CONTACTPERSOON
    private void deleteMail() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        String postAddress = URL_DELETE + mailId;


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postAddress,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Mail deleted!", Toast.LENGTH_SHORT).show();
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

    private void updateMail(Message mail) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject body = new JSONObject(buildupdateJsonMail());



        System.out.println(mail.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, URL_DELETE + mail.getId(), body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                headers.put("Authorization", "Bearer " + ACCES_TOKEN);


                return headers;
            }

        };

        queue.add(objectRequest);
    }

    private String buildupdateJsonMail() {
        JsonObjectBuilder factory = Json.createObjectBuilder()
                .add("isRead", true);
        return factory.build().toString();
    }

    void applyProfilePicture(ImageView imgProfile, TextView iconText) {
        imgProfile.setImageResource(R.drawable.bg_circle);
        imgProfile.setColorFilter(Color.CYAN);
        iconText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_navigation, menu);
        MenuItem addItem = menu.findItem(R.id.action_send);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentListMails = new Intent(DisplayMailActivity.this, ListMailsActvity.class);
                intentListMails.putExtra("AccessToken", accessToken);
                intentListMails.putExtra("userName", userName);
                intentListMails.putExtra("userEmail", userEmail);

                startActivity(intentListMails);

                return true;

            case R.id.action_reply:

                goToReplyActivity();

                return true;

            case R.id.action_delete:

                builder.show();

                return true;

            case R.id.action_forward:

                toForwardMail();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}


