package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayMailActivity extends AppCompatActivity {
    private TextView mailBodyContent;
    private JSONObject mail;
    private JSONObject body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        mailBodyContent = (TextView) findViewById(R.id.mailBody);
        mailBodyContent.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        String JsonString = intent.getStringExtra("mailObjext");

        try {
            mail = new JSONObject(JsonString);
            body = mail.getJSONObject("body");
            mailBodyContent.setText(Html.fromHtml(body.getString("content")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
