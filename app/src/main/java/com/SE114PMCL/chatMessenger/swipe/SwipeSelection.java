package com.SE114PMCL.chatMessenger.swipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.SE114PMCL.chatMessenger.Adapter.UserListAdapter;
import com.SE114PMCL.chatMessenger.Model.NoteRequest;
import com.SE114PMCL.chatMessenger.Model.RequestModel;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SwipeSelection extends Fragment {
    RecyclerView recyclerView;
    private SwipeAdapter adapter;
    ArrayList<UserModel> userModel = new ArrayList<>();

    UserListAdapter userListAdapter;

    public SwipeSelection() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.swipe_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new SwipeAdapter(getContext(), userModel);
        recyclerView.setAdapter(adapter);

        CreateList();
    }

    private void CreateList() {
        userModel = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            UserModel user = new UserModel();
//            user.setUsername("Friend " + (i+1));
//            userModel.add(user);
//        }
//        adapter.setFriendData(userModel);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference ref_request = FirebaseDatabase.getInstance().getReference("NoteRequest").child(firebaseUser.getUid());

        ref_request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRequest noteRequest = snapshot.getValue(NoteRequest.class);

                String id_request = noteRequest.getId();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModel.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserModel user = snapshot.getValue(UserModel.class);
                            if (id_request.equals(user.getId())) {
                                userModel.add(user);
                            }
                            adapter = new SwipeAdapter(getContext(), userModel);
                            recyclerView.setAdapter(adapter);
                            //adapter.setFriendData(userModel);
                        }
                    }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userModel.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    UserModel user = snapshot.getValue(UserModel.class);
//                    if (user.getId().equals("e6qlrRjJanVrTBz4AdM5bIyZO9Z2")) {
//                        userModel.add(user);
//                    }
//
//                    adapter = new SwipeAdapter(getContext(), userModel);
//                    recyclerView.setAdapter(adapter);
//                    //adapter.setFriendData(userModel);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}