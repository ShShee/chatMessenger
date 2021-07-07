package com.SE114PMCL.chatMessenger.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.R;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public CircleImageView imageURl;
    public FriendMyViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        imageURl=itemView.findViewById(R.id.profileImage);
        username=itemView.findViewById(R.id.username);
    }
}
