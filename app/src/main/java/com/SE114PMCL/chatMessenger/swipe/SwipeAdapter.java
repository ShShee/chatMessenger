package com.SE114PMCL.chatMessenger.swipe;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {
    //1-
    private Context context;
    private ArrayList<UserModel> usermodel;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public SwipeAdapter(Context context, ArrayList<UserModel> usermodel) {
        this.context = context;
        this.usermodel = usermodel;
    }

    public void setFriendData(ArrayList<UserModel> usermodel){
        this.usermodel = new ArrayList<>();
        this.usermodel = usermodel;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_swipe, parent, false);
        return new SwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SwipeViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(usermodel.get(position).getUsername()));
        viewBinderHelper.closeLayout(String.valueOf(usermodel.get(position).getUsername()));
        holder.bindData(usermodel.get(position));
    }

    @Override
    public int getItemCount() {
        return usermodel.size();
    }

    // 2-
    public class SwipeViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        private TextView textView;
        private TextView txtAdd;
        private TextView txtDelete;
        private SwipeRevealLayout swipeRevealLayout;

        public SwipeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            txtAdd = itemView.findViewById(R.id.txtAddFriend);
            txtDelete = itemView.findViewById(R.id.txtDeleteFriend);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);
            profile_image = itemView.findViewById(R.id.imgAvatar);

            txtAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Accept is clicked.", Toast.LENGTH_SHORT).show();
                }
            });

            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Delete is clicked.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        void bindData(UserModel userModel){
            textView.setText(userModel.getUsername());
            if (userModel.getImageURL().equals("default")){
                profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(userModel.getImageURL()).into(profile_image);
            }
        }
    }
}
