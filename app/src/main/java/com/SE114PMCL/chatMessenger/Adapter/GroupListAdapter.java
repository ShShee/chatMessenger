package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.GroupChatActivity;
import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.HolderGroupChatList> {

    private Context context;
    private ArrayList<GroupData> groupData;

    public GroupListAdapter(Context context, ArrayList<GroupData> groupData){
        this.context=context;
        this.groupData=groupData;
    }

    @NonNull
    @NotNull
    @Override
    public HolderGroupChatList onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_groupchats_list,parent,false);
        return new HolderGroupChatList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderGroupChatList holder, int position) {
        //get data
        GroupData model=groupData.get(position);
        String groupId=model.getGroupId();
        String groupIcon=model.getGroupIcon();
        String groupTitle=model.getGroupTitle();

        holder.nameTv.setText("");
        holder.timeTv.setText("");
        holder.messageTv.setText("");

        //load last message and message-time
        loadLastMessage(model, holder);

        //set data
        holder.groupTitleTv.setText(groupTitle);
        try {
            Picasso.get().load(groupIcon).placeholder(R.drawable.ic_creategroup).into(holder.groupIconIv);

        }
        catch (Exception c){
            holder.groupIconIv.setImageResource(R.drawable.ic_creategroup);

        }

        //handle group click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupChatActivity.class);
            intent.putExtra("groupId",groupId);

            context.startActivity(intent);

        });
    }

    private void loadLastMessage(GroupData model, HolderGroupChatList holder) {
        //get last message from group
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(model.getGroupId()).child("Messages").limitToLast(1) //get last item() from that child
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            //get data
                            String message=""+ds.child("message").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();
                            String sender=""+ds.child("sender").getValue();
                            String type = "" + ds.child("type").getValue();

                            if(type.equals("image")){
                                holder.messageTv.setText("Gửi hình ảnh");
                            }
                            else{
                                holder.messageTv.setText(message);
                            }
                            holder.timeTv.setText(timestamp);

                            //get info of sender of last message
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                            ref.orderByChild("id").equalTo(sender).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            String name=""+ds.child("username").getValue();
                                            holder.nameTv.setText(name);
                                        }
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

    @Override
    public int getItemCount() {
        return groupData.size();
    }


    //view holder class
    class HolderGroupChatList extends RecyclerView.ViewHolder {
        //ui views
        private ImageView groupIconIv;
        private TextView groupTitleTv, nameTv, messageTv, timeTv;

        public HolderGroupChatList(@NonNull @NotNull View itemView) {
            super(itemView);

            groupIconIv=itemView.findViewById(R.id.groupIconIv);
            groupTitleTv=itemView.findViewById(R.id.groupTitleTv);
            nameTv=itemView.findViewById(R.id.nameTv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);

        }
    }
}



