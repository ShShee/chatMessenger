package com.SE114PMCL.chatMessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.HolderGroupChatList> {

    private Context context;
    private ArrayList<GroupData> groupData;

    public GroupListAdapter(Context context, ArrayList<GroupData> groupData){
        this.context=context;
        this.groupData=groupData;
    }

    @NonNull
    @NotNull
    @Override
    public HolderGroupChatList onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_groupchats_list,parent,false);
        return new HolderGroupChatList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderGroupChatList holder, int position) {
        //get data
        GroupData model=groupData.get(position);
        String groupId=model.getGroupId();
        String groupIcon=model.getGroupIcon();
        String groupTitle=model.getGroupTitle();

        //set data
        holder.groupTitleTv.setText(groupTitle);
        try {
            Picasso.get().load(groupIcon).placeholder(R.drawable.ic_creategroup).into(holder.groupIconIv);

        }
        catch (Exception c){
            holder.groupIconIv.setImageResource(R.drawable.ic_creategroup);

        }

        //handle group click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will do
            }
        });



    }

    @Override
    public int getItemCount() {
        return groupData.size();
    }


    //view holder class
    class HolderGroupChatList extends RecyclerView.ViewHolder {
        //ui views
        private ImageView groupIconIv;
        private TextView groupTitleTv, nameTv, messageTv, timeTv;




        public HolderGroupChatList(@NonNull @NotNull View itemView) {
            super(itemView);

            groupIconIv=itemView.findViewById(R.id.groupIconIv);
            groupTitleTv=itemView.findViewById(R.id.groupTitleTv);
            nameTv=itemView.findViewById(R.id.nameTv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);

        }
    }
}



