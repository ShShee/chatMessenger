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

public class RegisterActivity extends AppCompatActivity {
    DatabaseWorker db;
    EditText e1, e2, e3;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.activity_login);
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                                return;
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Username Already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Password do not match !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}