package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.SE114PMCL.chatMessenger.Holder.FindFriendViewHolder;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class FindFriendActivity extends AppCompatActivity {

    FirebaseRecyclerOptions<UserModel>options;
    FirebaseRecyclerAdapter<UserModel, FindFriendViewHolder>adapter;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText search;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyCleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.searchFriend);

        loadUsers("");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadUsers(String s) {
        Query query = mUserRef.orderByChild("timkiem").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();

        adapter = new FirebaseRecyclerAdapter<UserModel, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull UserModel model) {
                if(!mUser.getUid().equals(getRef(position).getKey().toString())) {
                    if (model.getImageURL().equals("default")) {
                        holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(model.getImageURL()).into(holder.profileImage);
                    }
                    holder.profileName.setText(model.getUsername());
                }
                else{
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(FindFriendActivity.this, ViewFriendActivity.class);
                    intent.putExtra("userKey", getRef(position).getKey().toString());
                    startActivity(intent);
                });
            }

            @NonNull
            @NotNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friend, parent, false);
                return new FindFriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}