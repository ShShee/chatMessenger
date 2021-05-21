package com.SE114PMCL.chatMessenger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Contact extends Fragment {
    RecyclerView recyclerView;
    ImageButton btnOpenFriendList,btnOpenGroupList,btnAddPending;
    NavController navController;
    BottomNavigationView navBar;
    ArrayList<FriendData> listPending;
    PendingListAdapter pendingListAdapter;
    public Contact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnOpenFriendList=view.findViewById(R.id.openFriendList);
        btnOpenGroupList=view.findViewById(R.id.openGroupList);
        btnAddPending=view.findViewById(R.id.addPending);
        navController= Navigation.findNavController(view);
        navBar = getActivity().findViewById(R.id.bottom_navigation);
        btnOpenFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.setVisibility(View.GONE);
                navController.navigate(R.id.action_contact_to_friendList);
            }
        });
        btnOpenGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.setVisibility(View.GONE);
                navController.navigate(R.id.action_contact_to_groupList);
            }
        });
        btnAddPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "CLick here to delete", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView=view.findViewById(R.id.pendingView);
        listPending=new ArrayList<>();
        listPending.add(new FriendData("0","Boss",R.drawable.avatar1,"",false));
        listPending.add(new FriendData("0","Chaien",R.drawable.chaien,"",false));
        listPending.add(new FriendData("0","Hooney",R.drawable.chaien,"",false));
        listPending.add(new FriendData("0","Cat",R.drawable.chaien,"",false));
        pendingListAdapter=new PendingListAdapter(getActivity().getApplicationContext(),listPending);
        recyclerView.setAdapter(pendingListAdapter);

        /*SwipeLayout swipeLayout =  view.findViewById(R.id.sample1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });*/
    }
}