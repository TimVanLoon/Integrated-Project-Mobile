package com.example.keiichi.project_mobile.Contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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
    private String notes;
    private String nickname;
    private String spouse;
    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String job;
    private String department;
    private String company;
    private String office;
    private String manager;
    private String assistant;
    private String firstName;
    private String lastName;
    private String email;
    private List<EmailAddress> emailList;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText jobTitleInput;
    private EditText departmentInput;
    private EditText companyNameInput;
    private EditText officeLocationInput;
    private EditText managerInput;
    private EditText assistantNameInput;
    private EditText streetNameInput;
    private EditText postalCodeInput;
    private EditText cityNameInput;
    private EditText stateNameInput;
    private EditText countryNameInput;
    private EditText personalNotesInput;
    private EditText nickNameInput;
    private EditText spouseNameInput;

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
        email = getIntent().getStringExtra("email");
        notes = getIntent().getStringExtra("notes");
        nickname = getIntent().getStringExtra("nickname");
        spouse = getIntent().getStringExtra("spouse");
        street = getIntent().getStringExtra("street");
        postalCode = getIntent().getStringExtra("postalcode");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");
        job = getIntent().getStringExtra("job");
        department = getIntent().getStringExtra("department");
        company = getIntent().getStringExtra("company");
        office = getIntent().getStringExtra("office");
        manager = getIntent().getStringExtra("manager");
        assistant = getIntent().getStringExtra("assistant");
        firstName = getIntent().getStringExtra("firstname");
        lastName = getIntent().getStringExtra("lastname");

        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        jobTitleInput = (EditText) findViewById(R.id.jobInput);
        departmentInput = (EditText) findViewById(R.id.department);
        companyNameInput = (EditText) findViewById(R.id.companyName);
        officeLocationInput = (EditText) findViewById(R.id.officeLocation);
        managerInput = (EditText) findViewById(R.id.manager);
        assistantNameInput = (EditText) findViewById(R.id.assistantName);
        streetNameInput = (EditText) findViewById(R.id.streetName);
        postalCodeInput = (EditText) findViewById(R.id.postalCode);
        cityNameInput = (EditText) findViewById(R.id.cityName);
        stateNameInput = (EditText) findViewById(R.id.stateName);
        countryNameInput = (EditText) findViewById(R.id.countryName);
        personalNotesInput = (EditText) findViewById(R.id.personalNotes);
        nickNameInput = (EditText) findViewById(R.id.nickName);
        spouseNameInput = (EditText) findViewById(R.id.spouseName);

        // VUL INPUTS MET DATA VAN CONTACT
        firstNameInput.setText(firstName);
        lastNameInput.setText(lastName);
        emailInput.setText(email);
        phoneInput.setText(phoneNumber);
        jobTitleInput.setText(job);
        departmentInput.setText(department);
        companyNameInput.setText(company);
        officeLocationInput.setText(office);
        managerInput.setText(manager);
        assistantNameInput.setText(assistant);
        streetNameInput.setText(street);
        postalCodeInput.setText(postalCode);
        cityNameInput.setText(city);
        stateNameInput.setText(state);
        countryNameInput.setText(country);
        personalNotesInput.setText(notes);
        nickNameInput.setText(nickname);
        spouseNameInput.setText(spouse);

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
                intentContactDetails.putExtra("notes", notes);
                intentContactDetails.putExtra("nickname", nickname);
                intentContactDetails.putExtra("spouse", spouse);
                intentContactDetails.putExtra("street", street);
                intentContactDetails.putExtra("postalcode", postalCode);
                intentContactDetails.putExtra("city", city);
                intentContactDetails.putExtra("state", state);
                intentContactDetails.putExtra("country", country);
                intentContactDetails.putExtra("job", job);
                intentContactDetails.putExtra("department", department);
                intentContactDetails.putExtra("company", company);
                intentContactDetails.putExtra("office", office);
                intentContactDetails.putExtra("manager", manager);
                intentContactDetails.putExtra("assistant", assistant);
                intentContactDetails.putExtra("firstname", firstName);
                intentContactDetails.putExtra("lastname", lastName);

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
                intentContactDetailsSaved.putExtra("notes", notes);
                intentContactDetailsSaved.putExtra("nickname", nickname);
                intentContactDetailsSaved.putExtra("spouse", spouse);
                intentContactDetailsSaved.putExtra("street", street);
                intentContactDetailsSaved.putExtra("postalcode", postalCode);
                intentContactDetailsSaved.putExtra("city", city);
                intentContactDetailsSaved.putExtra("state", state);
                intentContactDetailsSaved.putExtra("country", country);
                intentContactDetailsSaved.putExtra("job", job);
                intentContactDetailsSaved.putExtra("department", department);
                intentContactDetailsSaved.putExtra("company", company);
                intentContactDetailsSaved.putExtra("office", office);
                intentContactDetailsSaved.putExtra("manager", manager);
                intentContactDetailsSaved.putExtra("assistant", assistant);
                intentContactDetailsSaved.putExtra("firstname", firstName);
                intentContactDetailsSaved.putExtra("lastname", lastName);


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
