package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.ViewHolder> {

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private ArrayList<ModelGroupChat> modelGroupChatList;

    private FirebaseAuth firebaseAuth;

    public AdapterGroupChat(Context context, ArrayList<ModelGroupChat> modelGroupChatList){
        this.mContext=context;
        this.modelGroupChatList=modelGroupChatList;
    }

    @NonNull
    @Override
    public AdapterGroupChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.row_groupchat_right,parent,false);
            return new AdapterGroupChat.ViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(mContext).inflate(R.layout.row_groupchat_left,parent,false);
            return new AdapterGroupChat.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroupChat.ViewHolder holder, int position) {

        ModelGroupChat model = modelGroupChatList.get(position);
        String timestamp = model.getTimestamp();
        String message=model.getMessage();
        String type=model.getType();

        holder.timeTv.setText(timestamp);

        if(type.equals("text")){
            holder.imageSend.setVisibility(View.GONE);
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setText(message);
        }
        else {
            holder.imageSend.setVisibility(View.VISIBLE);
            holder.messageTv.setVisibility(View.GONE);

            Glide.with(mContext).load(message).into(holder.imageSend);

        }

        setUserName(model, holder);

    }

    private void setUserName(ModelGroupChat model, AdapterGroupChat.ViewHolder holder) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("id").equalTo(model.getSender()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren()){
                        String name = "" + ds.child("username").getValue();

                        holder.nameTv.setText(name);
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return modelGroupChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTv, messageTv, timeTv;
        private ImageView imageSend;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTv=itemView.findViewById(R.id.nameTv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            imageSend = itemView.findViewById(R.id.ImageSend);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        if (modelGroupChatList.get(position).getSender().equals(firebaseAuth.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

}
