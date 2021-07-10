package Login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.SE114PMCL.chatMessenger.VerifyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.sdsmdg.tastytoast.TastyToast;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    EditText gisUsername,gisEmail,gisPassword, gisCPassword;;
    Button  btnRegister;;
    FirebaseAuth auth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gisUsername=view.findViewById(R.id.tendangnhap);
        gisEmail=view.findViewById(R.id.mail);
        gisPassword=view.findViewById(R.id.pass);
        gisCPassword=view.findViewById(R.id.cpass);;
        btnRegister=view.findViewById(R.id.dangky);;
        auth=FirebaseAuth.getInstance();

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

                TastyToast.makeText(getActivity(), "Register Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                auth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //send user to next page
                        auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(new Intent(getActivity().getApplicationContext(), LoginFragment.class));
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TastyToast.makeText(getActivity(), e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                });
            }
        });
    }
}
