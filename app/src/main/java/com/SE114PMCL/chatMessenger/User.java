package com.SE114PMCL.chatMessenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User extends Fragment implements UserListAdapter.OnUserListener{
    NavController navController;
    BottomNavigationView navBar;
    RecyclerView recyclerView;
    ArrayList<FriendData> listUser;
    UserListAdapter userListAdapter;

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
        recyclerView=view.findViewById(R.id.userView);
        listUser=new ArrayList<>();
        listUser.add(new FriendData("11111","Boss",R.drawable.avatar1,"Boss: Feed me hooman !",true));
        listUser.add(new FriendData("22222","Chaien",R.drawable.chaien,"Chaien: No don't leave me",true));
        listUser.add(new FriendData("33333","Hooney",R.drawable.avatar2,"Me: Why you are crying ?",false));
        userListAdapter=new UserListAdapter(getActivity().getApplicationContext(),listUser,this::onUserClick);
        recyclerView.setAdapter(userListAdapter);
    }

    @Override
    public void onUserClick(int position) {
        navBar.setVisibility(View.GONE);
        navController.navigate(R.id.action_user_to_messenger);
    }


}