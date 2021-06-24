package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    Context context;
    ArrayList<GroupData> listGroup;
    private GroupListAdapter.OnGroupListener mOnGroupListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public GroupListAdapter(Context context, ArrayList<GroupData> listGroup,OnGroupListener onGroupListener) {
        this.context = context;
        this.listGroup = listGroup;
        this.mOnGroupListener=onGroupListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.group_view, parent, false);
        return new ViewHolder(view,mOnGroupListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        GroupData group = listGroup.get(position);
        holder.txtTenGroup.setText(group.getTenGroup());
        holder.imgAvatar.setImageResource(group.getAvatar());
        holder.itemView.setSelected(selectedPos == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
                mOnGroupListener.onGroupClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroup.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgAvatar;
        TextView txtTenGroup;
        OnGroupListener onGroupListener;
        public ViewHolder(@NonNull View itemView,OnGroupListener onGroupListener) {
            super(itemView);
            // Ánh xạ view
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTenGroup = itemView.findViewById(R.id.txtTenGroup);
            this.onGroupListener=mOnGroupListener;
        }
    }
    public interface OnGroupListener{
        void onGroupClick(int position);
    }
}
