package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.R;

import java.io.Serializable;
import java.util.List;

public class EditContactActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private String userName;
    private String userEmail;
    private String accessToken;
    private String displayName;
    private String phoneNumber;
    private String givenName;
    private List<EmailAddress> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        givenName = getIntent().getStringExtra("givenName");
        displayName = getIntent().getStringExtra("displayName");
        phoneNumber = getIntent().getStringExtra("userPhone");
        emailList = (List<EmailAddress>)getIntent().getSerializableExtra("emailList");

        System.out.println("test list 2: " + emailList);

        // INITIALISEER ACTION BAR
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // VOEG ICONS TOE AAN DE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_navigation, menu);


        return super.onCreateOptionsMenu(menu);
    }

    // METHODE VOOR DE CLICKABLE ICOONTJES IN DE ACTION BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:
                Intent intentContactDetails = new Intent(EditContactActivity.this, ContactsDetailsActivity.class);
                intentContactDetails.putExtra("AccessToken", accessToken);
                intentContactDetails.putExtra("userName", userName);
                intentContactDetails.putExtra("userEmail", userEmail);
                intentContactDetails.putExtra("givenName", givenName);
                intentContactDetails.putExtra("displayName", displayName);
                intentContactDetails.putExtra("userPhone", phoneNumber);
                intentContactDetails.putExtra("emailList",(Serializable) emailList);

                startActivity(intentContactDetails);

                return true;

            // WANNEER SAVE ICON WORDT AANGEKLIKT
            case R.id.action_save:
                Intent intentContactDetailsSaved = new Intent(EditContactActivity.this, ContactsDetailsActivity.class);
                intentContactDetailsSaved.putExtra("AccessToken", accessToken);
                intentContactDetailsSaved.putExtra("userName", userName);
                intentContactDetailsSaved.putExtra("userEmail", userEmail);
                intentContactDetailsSaved.putExtra("givenName", givenName);
                intentContactDetailsSaved.putExtra("displayName", displayName);
                intentContactDetailsSaved.putExtra("userPhone", phoneNumber);
                intentContactDetailsSaved.putExtra("emailList",(Serializable) emailList);

                startActivity(intentContactDetailsSaved);

                Toast.makeText(getApplicationContext(), "Contact edited", Toast.LENGTH_SHORT).show();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
