package Main.ContactTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Adapter.UserListAdapter;
import com.SE114PMCL.chatMessenger.Controller.MainActivity;

import com.SE114PMCL.chatMessenger.Holder.FriendMyViewHolder;
import com.SE114PMCL.chatMessenger.Model.Friends;
import com.SE114PMCL.chatMessenger.Model.UserModel;

import com.SE114PMCL.chatMessenger.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import java.util.List;

public class FriendList extends Fragment {

    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>adapter;

    RecyclerView recyclerView;
    Toolbar toolbar;
    EditText search_users;

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;

    public FriendList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar=view.findViewById(R.id.toolbarFriend);

        recyclerView=view.findViewById(R.id.friendView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef=FirebaseDatabase.getInstance().getReference().child("Friends");

        LoadFriends("");


        search_users = view.findViewById(R.id.searchFriend);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoadFriends(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void LoadFriends(String s) {
        Query query=mRef.child(mUser.getUid()).orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter =new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull FriendMyViewHolder holder, int position, @NonNull @NotNull Friends model) {
                if(model.getImageURL().equals("default")){
                    holder.imageURl.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(model.getImageURL()).into(holder.imageURl);
                }
                holder.username.setText(model.getUsername());

            }

            @NonNull
            @NotNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend,parent,false);

                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}