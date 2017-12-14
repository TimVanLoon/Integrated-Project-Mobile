package com.example.keiichi.project_mobile.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.keiichi.project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class SendMailActivity extends AppCompatActivity {
    final private String URL_POSTADRESS = "https://graph.microsoft.com/v1.0/me/sendMail";

    private Button Sendmail;
    private TextView MailAdress;
    private TextView Subject;
    private TextView MailBody;
    private String Acces_Token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        Sendmail = (Button) findViewById(R.id.ButtonSendMail);
        MailAdress = (TextView) findViewById(R.id.TextMailAdress);
        Subject = (TextView) findViewById(R.id.TextMailSubject);
        MailBody = (TextView) findViewById(R.id.TextMailBody);
        Intent intent = getIntent();
        Acces_Token = intent.getStringExtra("accestoken");

        Sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendMail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void SendMail() throws JSONException {
        System.out.println(MailBody.getText());
        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject jsonObject = new JSONObject(buildJsonMail());

        System.out.println(jsonObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_POSTADRESS, jsonObject,
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
                headers.put("Authorization", "Bearer " + Acces_Token);

                return headers;
            }

        };

        queue.add(objectRequest);

    }

    private String buildJsonMail() {
        JsonObjectBuilder factory = Json.createObjectBuilder()
                .add("message", Json.createObjectBuilder().
                        add("subject", Subject.getText().toString()).
                        add("body", Json.createObjectBuilder().
                                add("contentType", "Text").
                                add("content", MailBody.getText().toString() + "\n\n\n\n Sent from PAPA STOP!\n\n Auw dat doet pijn...")).
                        add("toRecipients", Json.createArrayBuilder().
                                add(Json.createObjectBuilder().
                                        add("emailAddress", Json.createObjectBuilder().
                                                add("address", MailAdress.getText().toString()))))
                );
        return factory.build().toString();
    }


}
