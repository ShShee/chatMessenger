package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Controller.StartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class ForgotpassActivity extends AppCompatActivity {
    Button forgotBtn;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), StartActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        firebaseAuth = FirebaseAuth.getInstance();

        forgotBtn = (Button) findViewById(R.id.btnForgot);

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                View view = inflater.inflate(R.layout.reset_pop, null);

                reset_alert.setTitle("Reset Forgot Password ?")
                            . setMessage("Enter your Email to get Password Reset link.")
                            .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //validate the email address
                                    EditText email = view.findViewById(R.id.txtEmailReset);
                                    if(email.getText().toString().isEmpty()){
                                        email.setError("Required Field");
                                        return;
                                    }
                                    //send the reset link
                                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ForgotpassActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(ForgotpassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", null)
                            .setView(view)
                            .create().show();
            }
        });
    }
}