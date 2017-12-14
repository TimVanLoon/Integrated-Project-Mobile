package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.keiichi.project_mobile.Mail.ListMailsActvity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ListMailsActvity.class);
        startActivity(intent);
    }

    public void login(View view){
        Intent intent = new Intent(this, ListMailsActvity.class);
        startActivity(intent);

    }
}
