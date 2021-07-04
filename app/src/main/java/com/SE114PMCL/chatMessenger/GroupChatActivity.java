package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Adapter.AdapterGroupChat;
import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private String groupId, myGroupRole="";

    private Toolbar toolbar;
    private ImageView groupIconIv;
    private ImageButton attachBtn, sendBtn;
    private TextView groupTitleTv;
    private EditText messageEt;
    private RecyclerView chatRv;

    private ArrayList<ModelGroupChat> groupChatList;
    private AdapterGroupChat adapterGroupChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //init views
        toolbar =findViewById(R.id.toolbar);
        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleTv=findViewById(R.id.groupTitleTv);
        attachBtn=findViewById(R.id.attachBtn);
        messageEt=findViewById(R.id.messageEt);
        sendBtn=findViewById(R.id.sendBtn);
        chatRv=findViewById(R.id.chatRv);

        setSupportActionBar(toolbar);

        //get id of the group
        Intent intent=getIntent();
        groupId=intent.getStringExtra("groupId");

        firebaseAuth =FirebaseAuth.getInstance();
        loadGroupInfo();
        loadGroupMessages();
        loadMyGroupRole();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String message=messageEt.getText().toString().trim();
                //valiable
                if(TextUtils.isEmpty(message)){
                    //empty, don't send
                    Toast.makeText(GroupChatActivity.this,"Can' send empty message...",Toast.LENGTH_SHORT).show();
                }
                else{
                    //send message
                    sendMessage(message);

                }

            }
        });
    }

    private void loadMyGroupRole() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            myGroupRole=""+ds.child("role").getValue();
                            //refresh menu items
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    private void loadGroupMessages() {
        //init list
        groupChatList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        groupChatList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelGroupChat model=ds.getValue(ModelGroupChat.class);
                            groupChatList.add(model);
                        }
                        //adapter
                        adapterGroupChat=new AdapterGroupChat(GroupChatActivity.this,groupChatList);
                        chatRv.setAdapter(adapterGroupChat);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    private void sendMessage(String message) {
        //timestmap
        String timestamp= ""+System.currentTimeMillis();

        //setup message data
        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("sender",""+firebaseAuth.getUid());
        hashMap.put("message",""+message);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("type",""+"text");

        //add in db
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Messages").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //message sent
                        //clear messageEt
                        messageEt.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //message sending failed
                        Toast.makeText(GroupChatActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void loadGroupInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String groupTitle=""+ds.child("groupTitle").getValue();
                            String groupDescription=""+ds.child("groupDescription").getValue();
                            String groupIcon=""+ds.child("groupIcon").getValue();
                            String timestamp=""+ds.child("timestamp").getValue();
                            String createBy=""+ds.child("createBy").getValue();

                            groupTitleTv.setText(groupTitle);
                            try {
                                Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_white).into(groupIconIv);

                            }
                            catch(Exception e){
                                groupIconIv.setImageResource(R.drawable.ic_group_white);
                            }



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.groupmenu, menu);

        if(myGroupRole.equals("creator")||myGroupRole.equals("admin")){
            //in admin/creator, show add person option
            menu.findItem(R.id.action_add_participant).setVisible(true);
        }
        else{
            menu.findItem(R.id.action_add_participant).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_add_participant){
            Intent intent=new Intent(this,GroupParticipantAddActivity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}