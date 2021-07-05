package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Friend_Profile_Activity;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.SE114PMCL.chatMessenger.RegisterActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Main.ChatTab.Chat;
import Main.ChatTab.MessengerActivity;
import Main.ChatTab.User;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<UserModel> userList;

    public AdapterUsers(Context context, List<UserModel> userList){
        this.context=context;
        this.userList=userList;
    }




    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        //inflate layout(row_user.xml)
        View view=LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder myHolder, int i) {

        //get data
        String hisUID=userList.get(i).getId();
        String userImage=userList.get(i).getImageURL();
        String userName=userList.get(i).getUsername();

        //set data
        myHolder.mNameTv.setText(userName);
        try {
            Picasso.get().load(userImage)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(myHolder.mAvatarIv);
        }
        catch (Exception e){

        }

        //handle item click
       myHolder.itemView.setOnClickListener(view -> {
           Intent intent = new Intent(context, Friend_Profile_Activity.class);
            intent.putExtra("hisuid", hisUID);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarIv;
        TextView mNameTv;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            //init views
            mAvatarIv=itemView.findViewById(R.id.avatarIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
        }
    }
}

