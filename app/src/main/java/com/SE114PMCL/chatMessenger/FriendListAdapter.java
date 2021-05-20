package com.SE114PMCL.chatMessenger;

import android.content.Context;
import android.icu.util.Freezable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    Context context;
    ArrayList<FriendData> listFriend;

    public FriendListAdapter(Context context, ArrayList<FriendData> listFriend) {
        this.context = context;
        this.listFriend = listFriend;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.friend_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        FriendData friend = listFriend.get(position);
        holder.txtTenUser.setText(friend.getTenUser());
        holder.imgAvatar.setImageResource(friend.getAvatar());
    }

    @Override
    public int getItemCount() {
        return listFriend.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;

        TextView txtTenUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTenUser = itemView.findViewById(R.id.txtTenUser);

        }
    }
}
