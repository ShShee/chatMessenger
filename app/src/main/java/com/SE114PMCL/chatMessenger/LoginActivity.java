package com.SE114PMCL.chatMessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.R;

public class LoginActivity extends AppCompatActivity {
    EditText e1, e2;
    Button b1;
    DatabaseWorker db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=findViewById(R.id.textViewSignUp);

        db = new DatabaseWorker(this);
        e1 = (EditText)findViewById(R.id.PTtendangnhap);
        e2 = (EditText)findViewById(R.id.PWmatkhau);
        b1 = (Button)findViewById(R.id.BTNdangnhap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean checknamepass = db.usernamepassword(username, password);
                if(checknamepass == true){
                    Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_main);
                }
                else
                    Toast.makeText(getApplicationContext(), "Wrong username or password !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}