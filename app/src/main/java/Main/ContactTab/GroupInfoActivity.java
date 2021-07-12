package Main.ContactTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.SE114PMCL.chatMessenger.Adapter.AdapterParticipantAdd;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GroupInfoActivity extends AppCompatActivity {

    private String groupId;
    private  String myGroupRole="",creatorName="";
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    private Toolbar toolbar;

    private ImageView groupIconIv;
    private TextView descriptionTv,createByTv,infoName,participantsBt;
    private Button editGroupBt,addParticipantBt,leaveGroupBt;
    private RecyclerView participantsRv;

    private ArrayList<UserModel> userList;
    private AdapterParticipantAdd adapterParticipantAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        toolbar=findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        infoName=findViewById(R.id.infoName);
        groupIconIv=findViewById(R.id.groupIconIv);
        descriptionTv=findViewById(R.id.descriptionTv);
        editGroupBt=findViewById(R.id.editGroupBt);
        addParticipantBt=findViewById(R.id.addParticipantBt);
        leaveGroupBt=findViewById(R.id.leaveGroupBt);
        participantsBt=findViewById(R.id.participantsBt);
        participantsRv=findViewById(R.id.participantsRv);

        groupId=getIntent().getStringExtra("groupId");

        addParticipantBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, GroupParticipantAddActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String groupTitle = "" + snapshot.child("groupTitle").getValue();
                String groupIcon = "" + snapshot.child("groupIcon").getValue();
                String groupDescription = "" + snapshot.child("groupDescription").getValue();
                String creatorID=""+snapshot.child("createBy").getValue();

                findCreatorName(creatorID);
                descriptionTv.setText(groupDescription);
                participantsBt.setText("Members of "+groupTitle);
                try {
                    Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_white).into(groupIconIv);
                } catch (Exception e) {
                    groupIconIv.setImageResource(R.drawable.ic_group_white);
                }

                loadMyGroupRole();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadMyGroupRole() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .orderByChild("id").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            myGroupRole = "" + ds.child("role").getValue();
                            if (myGroupRole.equals("participant")) {
                                editGroupBt.setVisibility(View.GONE);
                                addParticipantBt.setVisibility(View.GONE);
                                leaveGroupBt.setText("Leave group");
                            } else if (myGroupRole.equals("admin")) {
                                editGroupBt.setVisibility(View.GONE);
                                addParticipantBt.setVisibility(View.VISIBLE);
                                leaveGroupBt.setText("Leave group");
                            } else if (myGroupRole.equals("creator")) {
                                editGroupBt.setVisibility(View.VISIBLE);
                                addParticipantBt.setVisibility(View.VISIBLE);
                                leaveGroupBt.setText("Delete group");
                            }
                        }
                        loadParticipants();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void loadParticipants() {
        userList = new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()) {
                    String id=""+ds.child("id").getValue();
                    System.out.println("Id in group: "+ds.child("id").getValue(String.class));
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(id);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                UserModel modelUser = new UserModel(snapshot.child("id").getValue(String.class),
                                        ""+snapshot.child("username").getValue(),
                                        ""+snapshot.child("imageURL").getValue(),
                                        ""+snapshot.child("status").getValue(),
                                        ""+snapshot.child("timkiem").getValue());
                               System.out.println("Id: "+snapshot.child("id").getValue()+ "- Ten:"+snapshot.child("username").getValue());
                                userList.add(modelUser);
                            adapterParticipantAdd =new AdapterParticipantAdd(GroupInfoActivity.this,userList,groupId,myGroupRole);
                            participantsRv.setAdapter(adapterParticipantAdd);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void findCreatorName(String inputID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(inputID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                creatorName = "" + snapshot.child("username").getValue();

                infoName.setText("Group of "+creatorName);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}