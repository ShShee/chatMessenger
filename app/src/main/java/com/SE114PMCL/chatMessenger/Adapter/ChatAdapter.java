package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.ChatData;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<ChatData> mChat;
    private String imageurl;

    FirebaseUser fuser;
    boolean s = true;

    public ChatAdapter(Context mContext,List<ChatData> mChat,String imageurl){
        this.mChat=mChat;
        this.mContext=mContext;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }else{
            View view=LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder,int position){

        ChatData chat=mChat.get(position);
        String type = chat.getType();
        holder.thoigian.setText(chat.getTimestamp());
        String message = chat.getMessage();
        holder.tenchat.setVisibility(View.GONE);

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

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

        if(position==mChat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Đã xem");
            }else{
                holder.txt_seen.setText("Đã chuyển");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
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

    }

    @Override
    public int getItemCount(){
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image, show_image;
        public TextView txt_seen;
        public TextView thoigian;
        public TextView tenchat;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            show_image = itemView.findViewById(R.id.show_image);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            thoigian = itemView.findViewById(R.id.timestamp);
            tenchat = itemView.findViewById(R.id.tenchat);
        }

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
}