package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.push.Push;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import android.view.View;

import com.example.keiichi.project_mobile.Mail.ListMailsActvity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCenter.start(getApplication(), "0dad3b08-3653-41ea-9c9b-689e0d88fbcf",
                Analytics.class, Crashes.class, Push.class);

        Intent intent = new Intent(this, ListMailsActvity.class);
        startActivity(intent);
    }

    public void login(View view){
        Intent intent = new Intent(this, ListMailsActvity.class);
        startActivity(intent);

    }
}
