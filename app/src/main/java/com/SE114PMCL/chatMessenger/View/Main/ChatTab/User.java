package Main.ChatTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.SE114PMCL.chatMessenger.Adapter.UserListAdapter;
import com.SE114PMCL.chatMessenger.Model.Chatlist;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import java.util.List;


public class User extends Fragment {
    NavController navController;
    BottomNavigationView navBar;
    RecyclerView recyclerView;
    UserListAdapter userListAdapter;
    EditText search;

    List<UserModel> mUsers;
    List<Chatlist> mchatlist;
    FirebaseUser fuser;
    DatabaseReference reference;
  
    public User() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        navBar = getActivity().findViewById(R.id.bottom_navigation);

        recyclerView = view.findViewById(R.id.userView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        mchatlist = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchatlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    mchatlist.add(chatlist);
                }

                chatList("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        search = view.findViewById(R.id.searchUser);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               chatList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void chatList(String s) {
        mUsers = new ArrayList<>();
        Query query=FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("timkiem")
                .startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel user = snapshot.getValue(UserModel.class);
                    for (Chatlist chatlist : mchatlist){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                userListAdapter = new UserListAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}