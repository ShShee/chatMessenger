package com.SE114PMCL.chatMessenger.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import Main.ContactTab.Contact;
import Main.ContactTab.Friends;

import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import Main.AccountTab.Setting;
import Main.ChatTab.Chat;
import Main.ContactTab.GroupList;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    CircleImageView image_chat;
    TextView username_chat;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        toolbar = findViewById(R.id.toolbarUsername);

        image_chat = (CircleImageView) findViewById(R.id.chatImage);
        username_chat = (TextView) findViewById(R.id.chatName);

        /*Intent i = getIntent();
        String fragmentName = i.getStringExtra("fragment");
        String contact = "contact";*/

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                username_chat.setText(userModel.getUsername());
                if (userModel.getImageURL().equals("default")) {
                    image_chat.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(userModel.getImageURL()).into(image_chat);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            //System.out.println("Fragment:"+fragmentName);
            /*if (fragmentName != null && fragmentName.equals("contact")) {
                bottomNav.setSelectedItemId(R.id.nav_friends);
                getSupportActionBar().hide();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_fade_exit).replace(R.id.fragment_container,
                        new GroupList()).commit();
            } else {*/
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Chat()).commit();
           // }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_chat:
                        getSupportActionBar().show();
                        selectedFragment = new Chat();
                        break;
                    case R.id.nav_friends:
                        selectedFragment = new Friends();
                        getSupportActionBar().hide();
                        break;
                    case R.id.nav_setting:
                        selectedFragment = new Setting();
                        getSupportActionBar().hide();
                        break;
                }
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_fade_exit).replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };

    private void Reset(int name) {

    }

    @Override
    public void onBackPressed() {
        NavController navController;
        int seletedItemId = bottomNav.getSelectedItemId();
        int currentFragment;
        switch (seletedItemId) {
            case R.id.nav_friends:
                navController = Navigation.findNavController(this, R.id.fragmentContainerView3);
                currentFragment = navController.getCurrentDestination().getId();
                if (R.id.friendList == currentFragment || R.id.groupList == currentFragment) {
                    bottomNav.setVisibility(View.VISIBLE);
                    navController.popBackStack();
                    //setSupportActionBar(toolbar);
                    /*getSupportActionBar().setDisplayShowTitleEnabled(false);
                    getSupportActionBar().show();*/
                }
                break;
            /*case R.id.nav_setting:
                navController = Navigation.findNavController(this, R.id.fragmentContainerView3);
                currentFragment=navController.getCurrentDestination().getId();
                if(R.id.friendList==currentFragment) {
                    Reset(R.string.userName);
                    navController.navigate(R.id.contact);
                }
                break;*/
        }
        //
        //if (R.id.nav_chat != seletedItemId) {
        //    setHomeItem(MainActivity.this);
        // } else {
        //    super.onBackPressed();
        //}
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}


