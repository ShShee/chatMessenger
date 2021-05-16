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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity {
    DatabaseWorker db;
    EditText e1, e2, e3;
    Button b1;

    protected FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        db = new DatabaseWorker(this);
        e1 = (EditText)findViewById(R.id.tendangnhap);
        e2 = (EditText)findViewById(R.id.pass);
        e3 = (EditText)findViewById(R.id.cpass);
        b1 = (Button)findViewById(R.id.dangky);

        TextView btn=findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    public void dangkyFB(View view) {
        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();
        String s3 = e3.getText().toString();

        if (s1.equals("")||s2.equals("")||s3.equals("")){
            Toast.makeText(getApplicationContext(), "Fields are empty !", Toast.LENGTH_SHORT).show();
        }
        else{
            if(s2.equals(s3)){
                Boolean checkUsername = db.checkUsername(s1);
                if(checkUsername == true){
                    Boolean insert = db.insert(s1, s2);
                    if(insert == true){
                        auth.createUserWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isComplete()) {
                                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    setContentView(R.layout.activity_login);
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                            }
                        });
                    }
                    return;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username Already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "Password do not match !", Toast.LENGTH_SHORT).show();
        }
    }
}