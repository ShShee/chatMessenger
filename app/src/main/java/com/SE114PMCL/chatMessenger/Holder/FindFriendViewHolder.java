package com.SE114PMCL.chatMessenger.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.R;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView profileImage;
    public TextView profileName;

    public FindFriendViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);


        profileImage = itemView.findViewById(R.id.profileimage);
        profileName = itemView.findViewById(R.id.profilename);
    }
}
