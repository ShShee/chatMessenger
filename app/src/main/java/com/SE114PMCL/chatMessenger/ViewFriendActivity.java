package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendActivity extends AppCompatActivity {

    DatabaseReference mUserRef, requestRef, friendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String imageviewFriend, nameviewFriend;
    CircleImageView cirImage;
    TextView cirName;
    Button btnPerform, btnDecline;
    String CurrentState = "nothing_happen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        final String userID = getIntent().getStringExtra("userKey");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        cirImage = findViewById(R.id.imageViewFriend);
        cirName = findViewById(R.id.nameViewFriend);
        btnPerform = findViewById(R.id.btnPerform);
        btnDecline = findViewById(R.id.btnDecline);

        LoadUser();

        btnPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userID);
            }
        });
    }

    private void PerformAction(String userID) {
        if(CurrentState.equals("nothing_happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("status", "pending");
            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull @NotNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You have sent Friend Request", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        CurrentState = "I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                    }
                    else{
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline")){
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You have canceled Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState = "nothing_happen";
                        btnPerform.setText("Send Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("he_sent_pending")){
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", "friend");
                        hashMap.put("username", nameviewFriend);
                        hashMap.put("profileImageUrl", imageviewFriend);
                        friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task task) {
                                if(task.isSuccessful()){
                                    friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task task) {
                                            Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                            CurrentState = "friend";
                                            btnPerform.setText("Send Message");
                                            btnDecline.setText("Unfriend");
                                            btnDecline.setVisibility(View.VISIBLE);

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
        if(CurrentState.equals("friend")){
            //
        }
    }

    private void LoadUser() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    imageviewFriend = snapshot.child("imageURL").getValue().toString();
                    nameviewFriend = snapshot.child("username").getValue().toString();

                    Picasso.get().load(imageviewFriend).into(cirImage);
                    cirName.setText(nameviewFriend);

                }
                else {
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, ""+ error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}