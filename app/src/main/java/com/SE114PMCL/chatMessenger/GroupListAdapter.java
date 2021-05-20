package com.SE114PMCL.chatMessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    Context context;
    ArrayList<GroupData> listGroup;

    public GroupListAdapter(Context context, ArrayList<GroupData> listGroup) {
        this.context = context;
        this.listGroup = listGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.group_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        GroupData group = listGroup.get(position);
        holder.txtTenGroup.setText(group.getTenGroup());
        holder.imgAvatar.setImageResource(group.getAvatar());
    }

    @Override
    public int getItemCount() {
        return listGroup.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        TextView txtTenGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTenGroup = itemView.findViewById(R.id.txtTenGroup);

        }
    }
}
