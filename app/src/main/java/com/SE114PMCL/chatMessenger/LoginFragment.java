package com.SE114PMCL.chatMessenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    EditText logEmail,logPassword;
    Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        logEmail = view.findViewById(R.id.EMemail);
        logPassword = view.findViewById(R.id.PWmatkhau);
        btnLogin = view.findViewById(R.id.BTNdangnhap);

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
                        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
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