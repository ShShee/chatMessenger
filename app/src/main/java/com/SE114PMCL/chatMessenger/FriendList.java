package com.SE114PMCL.chatMessenger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendList extends Fragment implements FriendListAdapter.OnFriendListener{
    RecyclerView recyclerView;
    NavController navController;
    Toolbar toolbar;
    BottomNavigationView navBar;
    ArrayList<FriendData> listFriend;
    FriendListAdapter friendListAdapter;
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
        recyclerView=view.findViewById(R.id.friendView);
        listFriend=new ArrayList<>();
        listFriend.add(new FriendData("0","Boss",R.drawable.avatar1,"",false));
        listFriend.add(new FriendData("0","Chaien",R.drawable.chaien,"",false));
        listFriend.add(new FriendData("0","Hooney",R.drawable.chaien,"",false));
        listFriend.add(new FriendData("0","Cat",R.drawable.chaien,"",false));
        friendListAdapter=new FriendListAdapter(getActivity().getApplicationContext(),listFriend,this::onFriendClick);
        recyclerView.setAdapter(friendListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        toolbar=view.findViewById(R.id.toolbarFriend);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.friendmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteFriend:
                Toast.makeText(getActivity(), "CLick here to delete", Toast.LENGTH_SHORT).show();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Thong tin chon luu o day
    @Override
    public void onFriendClick(int position) {

    }
}