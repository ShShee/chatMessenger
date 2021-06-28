package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> listPending;
    private PendingListAdapter.OnPendingListener mOnPendingListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public PendingListAdapter(Context context, ArrayList<UserModel> listPending, OnPendingListener onPendingListener) {
        this.context = context;
        this.listPending = listPending;
        this.mOnPendingListener=onPendingListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.pending_view, parent, false);
        return new ViewHolder(view,mOnPendingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        UserModel pending = listPending.get(position);
        holder.txtPendingName.setText(pending.getUsername());
        if (pending.getImageURL().equals("default")){
            holder.pendingAvatar.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(pending.getImageURL()).into(holder.pendingAvatar);
        }
        holder.itemView.setSelected(selectedPos == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
                mOnPendingListener.onPendingClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPending.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView pendingAvatar;
        TextView txtPendingName;
        OnPendingListener onPendingListener;
        public ViewHolder(@NonNull View itemView,OnPendingListener onPendingListener) {
            super(itemView);
            // Ánh xạ view
            pendingAvatar= itemView.findViewById(R.id.pendingAvatar);
            txtPendingName = itemView.findViewById(R.id.txtPendingName);
            this.onPendingListener=onPendingListener;
        }
    }
    public interface OnPendingListener{
        void onPendingClick(int position);
    }
}
