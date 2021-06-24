package com.SE114PMCL.chatMessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Controller.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyActivity extends AppCompatActivity {
    TextView verifyTxt;
    Button verifyBtn, startBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        auth = FirebaseAuth.getInstance();

//        if(!auth.getCurrentUser().isEmailVerified()){
//            verifyTxt.setVisibility(View.VISIBLE);
//            verifyBtn.setVisibility(View.VISIBLE);
//        }

        verifyTxt = (TextView) findViewById(R.id.txtVerify);
        verifyBtn = (Button) findViewById(R.id.btnVerify);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send verification email
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VerifyActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
//                        verifyTxt.setVisibility(View.GONE);
//                        verifyBtn.setVisibility(View.GONE);
                    }
                });
            }
        });

        startBtn = (Button) findViewById(R.id.btnStart);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}