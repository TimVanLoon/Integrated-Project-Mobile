package com.example.keiichi.project_mobile.Mail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
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

    private String ACCES_TOKEN,ID;
    private Message message;
    EditText TextMailAdress,TextMailSubject;
    Button ForwardButton;
    RichEditor Editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_mail);

        Intent intent = getIntent();
        message = (Message) intent.getSerializableExtra("mailObject");
        ACCES_TOKEN = intent.getStringExtra("accestoken");
        ID = message.getId();

        TextMailAdress = findViewById(R.id.TextMailAdress);
        TextMailSubject = findViewById(R.id.TextMailSubject);
        TextMailSubject.setText("forward: " + message.getSubject());
        ForwardButton = findViewById(R.id.ForwardButton);
        Editor = findViewById(R.id.editor);

        ForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    forwardMail();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void forwardMail() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject jsonObject = new JSONObject(buildJsonMail());

        System.out.println(jsonObject.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_FORWARD + message.getId() + "/forward", jsonObject,
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
                headers.put("Authorization", "Bearer " + ACCES_TOKEN);

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
}
