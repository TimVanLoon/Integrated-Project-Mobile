package com.example.keiichi.project_mobile.Mail;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.DAL.POJOs.Attachment;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.MainActivity;
import com.example.keiichi.project_mobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class DisplayMailActivity extends AppCompatActivity {

    final private String URL_MAIL_UPDATE = "https://graph.microsoft.com/v1.0/me/messages/";
    final private String URL_DELETE = "https://graph.microsoft.com/v1.0/me/messages/";
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mailSubjectTextView;
    private TextView senderTimeTextView;
    private TextView senderNameTextView;
    private TextView receiverNameTextView;
    private TextView receiverMailTextView;
    private WebView mailBodyWebView;
    private ImageView profilePicture;
    private ImageView replyIcon;
    private Toolbar myToolbar;
    private String ACCES_TOKEN;
    private String messageBody;
    private Message messageObject;
    private String accessToken;
    private String userName;
    private String userEmail;
    private String mailSubject;
    private String mailAddress;
    private String mailId;
    private String senderName;
    private String timeSent;
    private String receiverName;
    private String receiverMail;

    private  AlertDialog.Builder builder;

    StrictMode.VmPolicy.Builder fileBuilder = new StrictMode.VmPolicy.Builder();



    private String contentType;
    private String isRead;



    private ArrayList<Attachment> attachments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);

        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // VOEG BACK BUTTON TOE AAN ACTION BAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accessToken = getIntent().getStringExtra("AccessToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        mailId = getIntent().getStringExtra("mailId");
        mailSubject = getIntent().getStringExtra("mailSubject");
        mailAddress = getIntent().getStringExtra("mailAddress");
        senderName = getIntent().getStringExtra("senderName");
        timeSent = getIntent().getStringExtra("timeSent");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverMail = getIntent().getStringExtra("receiverMail");
        messageBody = getIntent().getStringExtra("messageBody");
        messageObject = (Message) getIntent().getSerializableExtra("mail");
        contentType = getIntent().getStringExtra("contentType");
        isRead = getIntent().getStringExtra("isRead");

        Intent intent = getIntent();
        ACCES_TOKEN = intent.getStringExtra("accestoken");

        mailSubjectTextView = findViewById(R.id.mailSubjectTextView);
        senderNameTextView = findViewById(R.id.senderNameTextView);
        senderTimeTextView = findViewById(R.id.senderTimeTextView);
        profilePicture = findViewById(R.id.profilePicture);
        receiverNameTextView = findViewById(R.id.receiverNameTextView);
        receiverMailTextView = findViewById(R.id.receiverMailTextView);
        mailBodyWebView = findViewById(R.id.mailBodyWebView);
        replyIcon = (ImageView) findViewById(R.id.replyIcon);

        replyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReplyActivity();
            }
        });


        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(senderName.substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(senderName.substring(0,1), color2, 3); // radius in px

        profilePicture.setImageDrawable(drawable1);


        if(isRead != null){

            messageObject.setRead(true);

            try {
                updateMailIsRead(messageObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        builder = new AlertDialog.Builder(DisplayMailActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Delete Mail");
        builder.setMessage("Are you sure you want to delete this mail?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    deleteMail();

                    int DELAY_TIME=2000;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //this code will run after the delay time which is 2 seconds.
                            Intent intentListMail = new Intent(DisplayMailActivity.this, ListMailsActvity.class);

                            intentListMail.putExtra("AccessToken", accessToken);
                            intentListMail.putExtra("userName", userName);
                            intentListMail.putExtra("userEmail", userEmail);

                            startActivity(intentListMail);


                        }
                    }, DELAY_TIME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        displayMailData();
    }

    private void getAttachments() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_DELETE + messageObject.getId() + "/attachments", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        Type listType = new TypeToken<List<Attachment>>() {
                        }.getType();
                        try {
                            attachments = new Gson().fromJson(String.valueOf(response.getJSONArray("value")),listType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //System.out.println(attachments.get(0).getContentBytes());

                        if (Build.VERSION.SDK_INT < 23) {
                            downloadAttachment(attachments.get(0));
                            //openAttachment(attachments.get(0).getName(), attachments.get(0).getContentType());
                        } else {
                            if (checkAndRequestPermissions()) {
                                for (Attachment attachment: attachments) {
                                    downloadAttachment(attachment);
                                }
                                //downloadAttachment(attachments.get(0));
                                //openAttachment(attachments.get(0).getName(), attachments.get(0).getContentType());
                            }
                        }





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
                headers.put("Authorization", "Bearer " + accessToken);


                return headers;
            }

        };

        queue.add(objectRequest);
    }

    private void toForwardMail() {
        Intent forwardMailIntent = new Intent(DisplayMailActivity.this, ForwardMailActivity.class);

        forwardMailIntent.putExtra("AccessToken", accessToken);
        forwardMailIntent.putExtra("userName", userName);
        forwardMailIntent.putExtra("userEmail", userEmail);
        forwardMailIntent.putExtra("mailId", mailId);
        forwardMailIntent.putExtra("mailSubject", mailSubject);
        forwardMailIntent.putExtra("mailAddress", mailAddress);
        forwardMailIntent.putExtra("senderName", senderName);
        forwardMailIntent.putExtra("timeSent", timeSent);
        forwardMailIntent.putExtra("receiverName", receiverName);
        forwardMailIntent.putExtra("receiverMail", receiverMail);
        forwardMailIntent.putExtra("messageBody", messageBody);
        forwardMailIntent.putExtra("mail", messageObject);
        forwardMailIntent.putExtra("contentType", contentType);

        startActivity(forwardMailIntent);

        DisplayMailActivity.this.finish();

    }

    private void goToReplyActivity() {
        Intent replyMailIntent = new Intent(DisplayMailActivity.this, ReplyToMailActivity.class);

        replyMailIntent.putExtra("AccessToken", accessToken);
        replyMailIntent.putExtra("userName", userName);
        replyMailIntent.putExtra("userEmail", userEmail);
        replyMailIntent.putExtra("mailId", mailId);
        replyMailIntent.putExtra("mailSubject", mailSubject);
        replyMailIntent.putExtra("mailAddress", mailAddress);
        replyMailIntent.putExtra("senderName", senderName);
        replyMailIntent.putExtra("timeSent", timeSent);
        replyMailIntent.putExtra("receiverName", receiverName);
        replyMailIntent.putExtra("receiverMail", receiverMail);
        replyMailIntent.putExtra("messageBody", messageBody);
        replyMailIntent.putExtra("mail", messageObject);
        replyMailIntent.putExtra("contentType", contentType);

        startActivity(replyMailIntent);

        DisplayMailActivity.this.finish();
    }


    // PATCH REQUEST VOOR DELETEN CONTACTPERSOON
    private void deleteMail() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        String postAddress = URL_DELETE + mailId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postAddress,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Mail deleted!", Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };

        queue.add(stringRequest);

    }

    private void updateMail(Message mail) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject body = new JSONObject(buildupdateJsonMail());



        System.out.println(mail.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, URL_DELETE + mailId, body,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_navigation, menu);
        MenuItem addItem = menu.findItem(R.id.action_send);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            // WANNEER BACK BUTTON WORDT AANGEKLIKT (<-)
            case android.R.id.home:

                Intent intentListMails = new Intent(DisplayMailActivity.this, ListMailsActvity.class);
                intentListMails.putExtra("AccessToken", accessToken);
                intentListMails.putExtra("userName", userName);
                intentListMails.putExtra("userEmail", userEmail);

                startActivity(intentListMails);

                DisplayMailActivity.this.finish();

                return true;

            case R.id.action_replyAll:
                goToReplyAllActivity();
                return true;

            case R.id.action_delete:

                builder.show();

                return true;

            case R.id.action_forward:

                toForwardMail();

                return true;

            case R.id.action_downloadAttachments:

                try {
                    getAttachments();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToReplyAllActivity() {
        Intent replyAllIntent = new Intent(DisplayMailActivity.this,ReplyAllActivity.class);
        replyAllIntent.putExtra("mail",messageObject );
        replyAllIntent.putExtra("accestoken",accessToken);

        startActivity(replyAllIntent);
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void updateMailIsRead(Message message) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);

        Message newMessage = new Message();

        newMessage.setRead(true);

        final JSONObject jsonObject = new JSONObject(buildJsonIsRead());

        String patchUrl = URL_MAIL_UPDATE + message.getId();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PATCH, patchUrl , jsonObject,
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
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };

        queue.add(objectRequest);
    }

    private String buildJsonIsRead() {
        JsonObjectBuilder factory = Json.createObjectBuilder()

                        .add("isRead", true);

        return factory.build().toString();
    }

    public void displayMailData(){

        mailSubjectTextView.setText(mailSubject);
        senderNameTextView.setText(senderName);
        senderTimeTextView.setText(timeSent);
        receiverNameTextView.setText(receiverName);
        receiverMailTextView.setText(receiverMail);

        System.out.println("Content type: " + contentType);

        mailBodyWebView.setPadding(0,0,0,0);

        mailBodyWebView.setInitialScale(1);

        //setupWebView();

        if(contentType.equals("html")){

            mailBodyWebView.getSettings().setJavaScriptEnabled(true);
            mailBodyWebView.getSettings().setLoadWithOverviewMode(true);
            mailBodyWebView.getSettings().setUseWideViewPort(true);
            mailBodyWebView.getSettings().setBuiltInZoomControls(true);
            mailBodyWebView.getSettings().setDisplayZoomControls(false);
            mailBodyWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mailBodyWebView.setScrollbarFadingEnabled(false);
            mailBodyWebView.loadDataWithBaseURL("", messageBody, "text/html", "utf-8","");

        } else{

            mailBodyWebView.loadDataWithBaseURL("", messageBody, "text", "utf-8","");

        }



        //mailBodyWebView.loadDataWithBaseURL("", messageBody, "text/html", "utf-8","");
        //mailBodyWebView.getSettings().setLoadWithOverviewMode(true);

    }



    private boolean checkAndRequestPermissions() {
        int storageWritePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        int storageReadPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storageReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storageWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadAttachment(attachments.get(0));
                    //openAttachment(attachments.get(0).getName(), attachments.get(0).getContentType());
                } else {
                    Toast.makeText(DisplayMailActivity.this, "attachement error", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void downloadAttachment(Attachment attachment) {

            String base64 = attachment.getContentBytes();
            try {
                if (base64 != null) {
                    byte[] data = Base64.decode(base64, Base64.DEFAULT);
                    File filePath = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), attachment.getName());
                    System.out.println(filePath.toString());
                    FileOutputStream os = new FileOutputStream(filePath, true);
                    os.write(data);
                    os.close();
                    Uri path = Uri.fromFile(filePath);

                    Toast.makeText(DisplayMailActivity.this,"saved!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(DisplayMailActivity.this, "save fail", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

    }

    private void openAttachment(String filename, String contentType){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +
                filename);

        fileBuilder.detectFileUriExposure();
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, contentType);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            this.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

        }
    }





    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(360);
        val = val * 100d;
        return val.intValue();
    }

    private void setupWebView() {
        mailBodyWebView.getSettings().setJavaScriptEnabled(true);
        mailBodyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mailBodyWebView.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
        mailBodyWebView.addJavascriptInterface(this, "MyApp");
    }

    @JavascriptInterface
    public void resize(final float height) {
        DisplayMailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mailBodyWebView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    @Override
    public void onBackPressed(){
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intentListMails = new Intent(DisplayMailActivity.this, ListMailsActvity.class);
        intentListMails.putExtra("AccessToken", accessToken);
        intentListMails.putExtra("userName", userName);
        intentListMails.putExtra("userEmail", userEmail);

        startActivity(intentListMails);

        DisplayMailActivity.this.finish();
    }

}


