package com.SE114PMCL.chatMessenger.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.ViewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<ModelGroupChat> mChat;
    FirebaseUser fuser;

    boolean s = true;

    public AdapterGroupChat(Context mContext, List<ModelGroupChat> mChat){
        this.mChat=mChat;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public AdapterGroupChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new AdapterGroupChat.ViewHolder(view);
        }else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new AdapterGroupChat.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroupChat.ViewHolder holder,int position){

        ModelGroupChat chat = mChat.get(position);
        String type = chat.getType();
        String sender = chat.getSender();

        holder.thoigian.setText(chat.getTimestamp());
        String message = chat.getMessage();

        if(type.equals("text")){
            holder.show_message.setVisibility(View.VISIBLE);
            holder.show_image.setVisibility((View.GONE));

            holder.show_message.setText(chat.getMessage());
        }
        else{
            holder.show_message.setVisibility(View.GONE);
            holder.show_image.setVisibility((View.VISIBLE));
            Glide.with(mContext).load(message).into(holder.show_image);
        }

        holder.show_message.setOnClickListener(v -> {
            if(s){
                holder.thoigian.setVisibility(View.VISIBLE);
                s = false;
            }
            else {
                holder.thoigian.setVisibility(View.GONE);
                s = true;
            }
        });

        holder.show_image.setOnClickListener(v -> {
            if(s){
                holder.thoigian.setVisibility(View.VISIBLE);
                s = false;
            }
            else {
                holder.thoigian.setVisibility(View.GONE);
                s = true;
            }
        });

        setUserInfo(sender, holder);
    }

    @Override
    public int getItemCount(){
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message, chatname;
        public ImageView profile_image, show_image;
        public TextView thoigian;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            show_image = itemView.findViewById(R.id.show_image);
            profile_image = itemView.findViewById(R.id.profile_image);
            thoigian = itemView.findViewById(R.id.timestamp);
            chatname = itemView.findViewById(R.id.tenchat);
        }

    }

    private void setUserInfo(String sender, AdapterGroupChat.ViewHolder holder){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(sender);
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                String avatar = user.getImageURL();
                String name = user.getUsername();
                if(avatar.equals("default")){
                    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    if (isValidContextForGlide(mContext)){
                        // Load image via Glide lib using context
                        Glide.with(mContext).load(avatar).into(holder.profile_image);
                    }
                }
                holder.chatname.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}