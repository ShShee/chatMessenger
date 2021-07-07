package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterParticipantAdd extends RecyclerView.Adapter<AdapterParticipantAdd.HolderParticipantAdd> {
    private Context context;
    private ArrayList<UserModel> userList;
    private String groupId, myGroupRole; //creator/admin/participant

    public AdapterParticipantAdd(Context context, ArrayList<UserModel> userList, String s, String s1){
        this.context=context;
        this.userList=userList;
        this.groupId=groupId;
        this.myGroupRole=myGroupRole;
    }

    @NonNull
    @NotNull
    @Override
    public HolderParticipantAdd onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate  layout
        View view= LayoutInflater.from(context).inflate(R.layout.row_participant,parent,false);

        return new HolderParticipantAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderParticipantAdd holder, int position) {
        //get data
        UserModel modellUser=userList.get(position);
        String name= modellUser.getUsername();
        String image=modellUser.getImageURL();
        String uid=modellUser.getId();

        //set data
        holder.nameTv.setText(name);
        try{
            Picasso.get().load(image).placeholder(R.mipmap.ic_launcher).into(holder.avatarIv);

        }
        catch (Exception e){
            holder.avatarIv.setImageResource(R.mipmap.ic_launcher);
        }

        checkIfAlreadyExists(modellUser,holder);

        //handle click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Participants").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    //user exists/not participant
                                    String hisPreviousRole=""+snapshot.child("role").getValue();

                                    //options to display in dialog
                                    String[] options;

                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Choose Option");
                                    if(myGroupRole.equals("creator")){
                                        if(hisPreviousRole.equals("admin")){
                                            //in creator , he is admin
                                            options =new String[]{"Remove Admin","Remove User"};
                                           builder.setItems(options, new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   //handle item clicks
                                                   if(which==0){
                                                       //Remove Admin clicked
                                                       removeAdmin(modellUser);
                                                   }
                                                   else {
                                                       //Remove User clicked
                                                       removeParticipant(modellUser);
                                                   }
                                               }
                                           }).show();
                                        }
                                        else if(hisPreviousRole.equals("participant")){
                                            //in creator,he is participant
                                            options=new String[]{"Make Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if(which==0){
                                                        //Remove Admin clicked
                                                        makeAdmin(modellUser);
                                                    }
                                                    else {
                                                        //Remove User clicked
                                                        removeParticipant(modellUser);
                                                    }
                                                }
                                            }).show();

                                        }
                                    }
                                    else if(myGroupRole.equals("admin")){
                                        if(hisPreviousRole.equals("creator")){
                                            //in admin, he is creator
                                            Toast.makeText(context,"Creator of Group...",Toast.LENGTH_SHORT).show();
                                        }
                                        else if(hisPreviousRole.equals("admin")){
                                            //in admin, he is admin too
                                            options=new String[]{"Remove Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if(which==0){
                                                        //Remove Admin clicked
                                                        removeAdmin(modellUser);
                                                    }
                                                    else {
                                                        //Remove User clicked
                                                        removeParticipant(modellUser);
                                                    }
                                                }
                                            }).show();
                                        }
                                        else if(hisPreviousRole.equals("participant")){
                                            //in admin, he is participant
                                            options=new String[]{"Make Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if(which==0){
                                                        //Make Admin clicked
                                                        makeAdmin(modellUser);
                                                    }
                                                    else{
                                                        //Remove User clicked
                                                        removeParticipant(modellUser);
                                                    }

                                                }
                                            }).show();
                                        }
                                    }
                                }
                                else {
                                    //user doesn't exists/not participant: add
                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Add Participant")
                                            .setMessage("Add this user in this group?")
                                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //add user
                                                    addParticipant(modellUser);
                                                }
                                            })
                                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

            }
        });



        //set data

    }
    private void addParticipant (UserModel modelUser){
        //setup user data
        String timestamp=""+System.currentTimeMillis();
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("uid",modelUser.getId());
        hashMap.put("role","participant");
        hashMap.put("timestamp",""+timestamp);
        //add that user in Groups>groupId>Participants
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modelUser.getId()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //added successfully
                        Toast.makeText(context,"Added successfully...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //failed adding user in group
                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void makeAdmin(UserModel modellUser) {
        //setup data
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("role", "admin");//role are: participant/admin/creator
        //update role in db
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modellUser.getId()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //make admin
                        Toast.makeText(context,"This user in now admin...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //failed making admin
                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void removeParticipant(UserModel modellUser) {
        //
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modellUser.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //removed successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //failed making admin
                    }
                });
    }


    private void removeAdmin(UserModel modellUser) {
        //setup data
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("role", "participant");//role are: participant/admin/creator
        //update role in db
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modellUser.getId()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //make admin
                        Toast.makeText(context,"This user in no longer admin...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //failed making admin
                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkIfAlreadyExists(UserModel modellUser, HolderParticipantAdd holder) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modellUser.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //already exists
                            String hisRole=""+snapshot.child("role").getValue();
                            holder.statusTv.setText(hisRole);
                        }
                        else {
                            //doesn't exists
                            holder.statusTv.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class HolderParticipantAdd extends RecyclerView.ViewHolder{

        private ImageView avatarIv;
        private TextView nameTv, statusTv;

        public HolderParticipantAdd(@NonNull @NotNull View itemView) {
            super(itemView);

            avatarIv=itemView.findViewById(R.id.avatarIv);
            nameTv=itemView.findViewById(R.id.nameTv);
            statusTv=itemView.findViewById(R.id.statusTv);
        }
    }

}
