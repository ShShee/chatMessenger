package Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Controller.StartActivity;
import com.SE114PMCL.chatMessenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, confirm_password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference, noteRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.tendangnhap);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.pass);
        confirm_password = (EditText) findViewById(R.id.cpass);
        btn_register = (Button) findViewById(R.id.dangky);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance("https://chatmessenger-dfe5b-default-rtdb.asia-southeast1.firebasedatabase.app/");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirm_password = confirm_password.getText().toString();

                if(txt_username.isEmpty()){
                    username.setError("Username is required");
                    return;
                }

                if(txt_email.isEmpty()){
                    email.setError("Email is required");
                    return;
                }

                if(txt_password.isEmpty()){
                    password.setError("Password is required");
                    return;
                }

                if(txt_confirm_password.isEmpty()){
                    confirm_password.setError("Confirm Password is required");
                    return;
                }

                if(txt_password.length() < 6){
                    password.setError("Password must have at least 6 characters !");
                    return;
                }

                if(!txt_password.equals(txt_confirm_password)){
                    confirm_password.setError("Password Do not Match !");
                    return;
                }

                register(txt_username, txt_email, txt_password);
            }
        });
    }

    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            noteRef = FirebaseDatabase.getInstance().getReference("NoteRequest").child(userid);

                            HashMap<String, String> hashMap=new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("timkiem", username.toLowerCase());

                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("id", "none");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        noteRef.setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                }
                                            }
                                        });

                                        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}