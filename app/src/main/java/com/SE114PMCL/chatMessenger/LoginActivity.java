package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText logEmail, logPassword;
    Button btnLogin;
    DatabaseWorker db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        db = new DatabaseWorker(this);
        logEmail = (EditText)findViewById(R.id.EMemail);
        logPassword = (EditText)findViewById(R.id.PWmatkhau);
        btnLogin = (Button)findViewById(R.id.BTNdangnhap);

        TextView btn=findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract / validate
                if(logEmail.getText().toString().isEmpty()){
                     logEmail.setError("Email is Missing !");
                     return;
                }

                if(logPassword.getText().toString().isEmpty()){
                    logPassword.setError("Password is Missing !");
                    return;
                }

                // data is valid
                //Login user
                firebaseAuth.signInWithEmailAndPassword(logEmail.getText().toString(), logPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Login is successful
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // phần này dùng để vào app mà không cần đăng nhập lại, khi nào có nút Log out sẽ gẵn vào phần StartActivity
    //@Override
    //protected void onStart() {
     //   super.onStart();
      //  if (FirebaseAuth.getInstance().getCurrentUser() != null) {
       //     startActivity(new Intent(getApplicationContext(), MainActivity.class));
        //    finish();
       // }
    //}
}