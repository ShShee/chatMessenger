package Main.ContactTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.R;
import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Contact extends Fragment {

    ImageButton btnOpenFriendList,btnOpenGroupList;
    NavController navController;
    BottomNavigationView navBar;

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
    }
}