package com.SE114PMCL.chatMessenger;

import android.content.Context;
import android.icu.util.Freezable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.ViewHolder> {
    Context context;
    ArrayList<FriendData> listPending;
    private PendingListAdapter.OnPendingListener mOnPendingListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public PendingListAdapter(Context context, ArrayList<FriendData> listPending, OnPendingListener onPendingListener) {
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
        FriendData pending = listPending.get(position);
        holder.txtPendingName.setText(pending.getTenUser());
        holder.pendingAvatar.setImageResource(pending.getAvatar());
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
        return listPending.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

        @Override
        public void onClick(View v) {
            onPendingListener.onPendingClick(getBindingAdapterPosition());
        }
    }
    public interface OnPendingListener{
        void onPendingClick(int position);
    }
}
