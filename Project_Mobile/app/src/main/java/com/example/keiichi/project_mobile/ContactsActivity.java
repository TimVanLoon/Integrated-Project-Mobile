package com.example.keiichi.project_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ContactsActivity extends AppCompatActivity {

    BottomNavigationView mBottomNav;

    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        Menu menu = mBottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.action_calendar:
                        Intent intentCalendar = new Intent(ContactsActivity.this, CalendarActivity.class);
                        intentCalendar.putExtra("AccessToken", accessToken);
                        startActivity(intentCalendar);
                        break;
                    case R.id.action_mail:
                        Intent intentMail = new Intent(ContactsActivity.this, ListMailsActvity.class);
                        intentMail.putExtra("AccessToken", accessToken);
                        startActivity(intentMail);
                        break;
                    case R.id.action_user:

                        break;

                }

                return false;
            }
        });
    }
}
