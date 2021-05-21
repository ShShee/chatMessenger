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

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> {
    Context context;
    ArrayList<FriendData> listPending;

    public PendingListAdapter(Context context, ArrayList<FriendData> listPending) {
        this.context = context;
        this.listPending = listPending;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.pending_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        FriendData pending = listPending.get(position);
        holder.txtPendingName.setText(pending.getTenUser());
        holder.pendingAvatar.setImageResource(pending.getAvatar());
    }

    @Override
    public int getItemCount() {
        return listPending.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView pendingAvatar;

        TextView txtPendingName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
            pendingAvatar= itemView.findViewById(R.id.pendingAvatar);
            txtPendingName = itemView.findViewById(R.id.txtPendingName);

        }
    }
}
