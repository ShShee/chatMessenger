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
    private FriendListAdapter.OnFriendListener mOnFriendListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public FriendListAdapter(Context context, ArrayList<FriendData> listFriend,OnFriendListener onFriendListener) {
        this.context = context;
        this.listFriend = listFriend;
        this.mOnFriendListener=onFriendListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.friend_view, parent, false);
        return new ViewHolder(view,mOnFriendListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        FriendData friend = listFriend.get(position);
        holder.txtTenUser.setText(friend.getTenUser());
        holder.imgAvatar.setImageResource(friend.getAvatar());
        holder.itemView.setSelected(selectedPos == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFriend.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView imgAvatar;

        TextView txtTenUser;
        OnFriendListener onFriendListener;
        public ViewHolder(@NonNull View itemView,OnFriendListener onFriendListener) {
            super(itemView);
            // Ánh xạ view
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTenUser = itemView.findViewById(R.id.txtTenUser);
            this.onFriendListener=onFriendListener;
        }

        @Override
        public void onClick(View v) {
            onFriendListener.onFriendClick(getBindingAdapterPosition());
        }
    }
    public interface OnFriendListener{
        void onFriendClick(int position);
    }
}
