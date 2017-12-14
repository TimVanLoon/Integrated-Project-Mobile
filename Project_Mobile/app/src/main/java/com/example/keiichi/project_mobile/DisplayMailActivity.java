package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class DisplayMailActivity extends AppCompatActivity {
    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    private TextView mailBodyContent;
    private TextView Subject;
    private JSONObject mail;
    private JSONObject body;
    private ImageButton deleteButton;
    private String ACCES_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        mailBodyContent = (TextView) findViewById(R.id.mailBody);
        mailBodyContent.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        String JsonString = intent.getStringExtra("mailObjext");
        ACCES_TOKEN = intent.getStringExtra("accestoken");
        deleteButton = (ImageButton) findViewById(R.id.ButtonDelete);


        try {
            mail = new JSONObject(JsonString);
            body = mail.getJSONObject("body");
            mailBodyContent.setText(Html.fromHtml(body.getString("content")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

}


