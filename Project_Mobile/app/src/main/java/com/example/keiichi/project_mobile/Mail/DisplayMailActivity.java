package com.example.keiichi.project_mobile.Mail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class DisplayMailActivity extends AppCompatActivity {

    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    private JSONObject mail, body;
    private ImageButton deleteButton, replyButton;
    private TextView From, iconText, To,mailBodyContent,Subject;
    private ImageView icon_mail;
    private String ACCES_TOKEN, messageBody;
    private com.example.keiichi.project_mobile.DAL.POJOs.Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);


        Intent intent = getIntent();
        messageBody = intent.getStringExtra("messageBody");
        ACCES_TOKEN = intent.getStringExtra("accestoken");
        message = (com.example.keiichi.project_mobile.DAL.POJOs.Message) intent.getSerializableExtra("mailObject");


        mailBodyContent = findViewById(R.id.mailBody);
        mailBodyContent.setMovementMethod(new ScrollingMovementMethod());
        deleteButton = findViewById(R.id.ButtonDelete);
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);
        iconText = findViewById(R.id.icon_txt);
        icon_mail = findViewById(R.id.icon_profileMail);
        replyButton = findViewById(R.id.ReplyButton);
        Subject = findViewById(R.id.Subject);



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

            applyProfilePicture(icon_mail,iconText);
            updateMail(mail);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mailBodyContent.setText(Html.fromHtml(messageBody));



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteMail(mail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReplyActivity();
            }
        });



    }

    private void goToReplyActivity() {
        Intent showMail = new Intent(DisplayMailActivity.this, ReplyToMailActivity.class);

            showMail.putExtra("mailObject",  message);
            showMail.putExtra("accestoken", ACCES_TOKEN);

        startActivity(showMail);
    }


    private void deleteMail(JSONObject mail) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest objectRequest = new StringRequest(Request.Method.DELETE, URL_DELETE + mail.getString("id") ,
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
                headers.put("Authorization", "Bearer " + ACCES_TOKEN);

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    private void updateMail(JSONObject mail) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject body = new JSONObject(buildupdateJsonMail());



        System.out.println(mail.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, URL_DELETE + mail.getString("id"), body,
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

}


