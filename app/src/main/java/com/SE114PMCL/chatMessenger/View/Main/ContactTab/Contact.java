package Main.ContactTab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.SE114PMCL.chatMessenger.FindFriendActivity;
import com.SE114PMCL.chatMessenger.GroupCreateActivity;
import com.SE114PMCL.chatMessenger.R;
import com.SE114PMCL.chatMessenger.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
    public class Contact extends Fragment {

        ImageButton btnOpenFriendList, btnOpenGroupList;
        NavController navController;
        BottomNavigationView navBar;
        ImageButton btnAddGroup;

        ImageButton findFriend;

    public Contact() {
        // Required empty public constructor
    }



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
        btnAddGroup=view.findViewById(R.id.addGroup);

        findFriend=view.findViewById(R.id.findFriend);

        navController= Navigation.findNavController(view);
        navBar = getActivity().findViewById(R.id.bottom_navigation);

        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), FindFriendActivity.class));
            }
        });

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
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), GroupCreateActivity.class));
            }
        });
    }
}