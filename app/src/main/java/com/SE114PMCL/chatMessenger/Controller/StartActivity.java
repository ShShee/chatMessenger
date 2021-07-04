package com.SE114PMCL.chatMessenger.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.SE114PMCL.chatMessenger.LoginActivity;
import com.SE114PMCL.chatMessenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;

public class StartActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "f6c11d24-4a5c-4d70-8d04-d8b1388924fc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
    @Override
    protected void onStart() {
       super.onStart();
      if (FirebaseAuth.getInstance().getCurrentUser() != null) {
         startActivity(new Intent(getApplicationContext(), MainActivity.class));
         finish();
      }
    }
}