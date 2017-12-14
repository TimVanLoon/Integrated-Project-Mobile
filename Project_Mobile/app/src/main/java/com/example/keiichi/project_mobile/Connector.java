package com.example.keiichi.project_mobile;

import android.content.Context;
import android.util.Log;

import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

/**
 * Created by Keiichi on 11/12/2017.
 */

public class Connector {
    /* Azure AD Variables */
    private PublicClientApplication application;
    private AuthenticationResult authResult;
    final static String CLIENT_ID = "d3b60662-7768-4a50-b96f-eb1dfcc7ec8d";
    final static String SCOPES[] = {
            "https://graph.microsoft.com/Mail.Send",
            "https://graph.microsoft.com/Mail.ReadWrite",
            "https://graph.microsoft.com/Calendars.ReadWrite",
            "https://graph.microsoft.com/Calendars.Read",
            "https://graph.microsoft.com/Contacts.Read",
            "https://graph.microsoft.com/Calendars.ReadWrite"};

      /* Configure your sample app and save state for this activity */
      public void setApp(Context context){
          application = null;
          if (application == null) {
              application = new PublicClientApplication(
                      context,
                      CLIENT_ID);
          }
      }







}
