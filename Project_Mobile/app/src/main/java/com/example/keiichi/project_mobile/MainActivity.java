package com.example.keiichi.project_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.microsoft.identity.client.*;

public class MainActivity extends AppCompatActivity {
    public static final String TEST_MESSAGE = "heyboo";

    private int counter;

    private AdView mAdView;

    private Button loginButton;
    private Button rewardButton;

    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.counter = 0;

        loginButton = (Button)findViewById(R.id.loginButton);
        rewardButton = (Button)findViewById(R.id.adButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                //check();
                login(findViewById(android.R.id.content));
            }
        });

        rewardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

        MobileAds.initialize(this, "ca-app-pub-6055872829043526~7103179841");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                // Reward the user for watching the ad.
                Toast.makeText(getBaseContext(), "U hebt 1 token verdiend!", Toast.LENGTH_SHORT).show();
                addToCounter(1);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

        });

        loadRewardedVideoAd();

    }

    public void addToCounter(int aantal){
        this.counter = counter + aantal;
    }

   public void check(){
        if(this.counter == 0){
            new AlertDialog.Builder(this).setTitle("Unable to login").setMessage("Insufficient tokens!").setNeutralButton("Close", null).show();
        } else {
            counter = counter--;
            login(findViewById(android.R.id.content));
        }
    }

    public void login(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText3);
        String message = editText.getText().toString();
        intent.putExtra(TEST_MESSAGE, message);
        startActivity(intent);

    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-6055872829043526/2682387409",
                new AdRequest.Builder().build());
    }


    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
