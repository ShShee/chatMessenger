package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity {
    DatabaseWorker db;
    EditText gisUsername, gisEmail, gisPassword, gisCPassword;
    Button  btnRegister;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        db = new DatabaseWorker(this);
        gisUsername = (EditText)findViewById(R.id.tendangnhap);
        gisEmail = (EditText)findViewById(R.id.mail);
        gisPassword = (EditText)findViewById(R.id.pass);
        gisCPassword = (EditText)findViewById(R.id.cpass);
        btnRegister = (Button)findViewById(R.id.dangky);

        TextView btn=findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = gisUsername.getText().toString();
                String Email = gisEmail.getText().toString();
                String Password = gisPassword.getText().toString();
                String CPassword = gisCPassword.getText().toString();

                if(Username.isEmpty()){
                    gisUsername.setError("Username is required");
                    return;
                }

                if(Email.isEmpty()){
                    gisUsername.setError("Email is required");
                    return;
                }

                if(Password.isEmpty()){
                    gisUsername.setError("Password is required");
                    return;
                }

                if(CPassword.isEmpty()){
                    gisUsername.setError("Confirm Password is required");
                    return;
                }

                if(Password.length() < 6){
                    gisPassword.setError("Password must have at least 6 characters !");
                    return;
                }

                if(!Password.equals(CPassword)){
                    gisCPassword.setError("Password Do not Match !");
                    return;
                }

                // data is validated
                // register the user using firebase

                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                auth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //send user to next page
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}