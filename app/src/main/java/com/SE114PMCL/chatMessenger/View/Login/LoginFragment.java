package Login;

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
import android.widget.ImageButton;

import com.SE114PMCL.chatMessenger.Controller.MainActivity;
import com.SE114PMCL.chatMessenger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdsmdg.tastytoast.TastyToast;


import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    EditText logEmail,logPassword;
    Button btnLogin;
    ImageButton btnReset;
    ImageButton btnFb, btnGg;

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
        btnReset=view.findViewById(R.id.resetPassword);
        btnFb=view.findViewById(R.id.loginByFB);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        btnGg = view.findViewById(R.id.loginByGG);
        btnGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivityGg.class));
                getActivity().finish();
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

                        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                            TastyToast.makeText(getActivity(), "Please verify your email", TastyToast.LENGTH_LONG,TastyToast.WARNING);
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(getContext(), "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TastyToast.makeText(getActivity(), e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                });
                firebaseAuth.signInWithEmailAndPassword(logEmail.getText().toString(), logPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Login is successful

                        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                            TastyToast.makeText(getActivity(), "Please verify your email", TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(getContext(), "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            firebaseAuth.signOut();
                        }
                        else {
                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TastyToast.makeText(getActivity(), e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                });
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ForgotpassActivity.class));
                getActivity().finish();
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