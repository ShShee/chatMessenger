package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.SE114PMCL.chatMessenger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        toolbar=findViewById(R.id.toolbarUsername);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Chat()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment=null;
                    switch (item.getItemId()) {
                        case R.id.nav_chat:
                            selectedFragment = new Chat();
                            break;
                        case R.id.nav_friends:
                            selectedFragment = new Friends();
                            break;
                        case R.id.nav_setting:
                            selectedFragment = new Setting();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    private void Reset(int name) {

    }
    @Override
    public void onBackPressed() {
        NavController navController;
        int seletedItemId = bottomNav.getSelectedItemId();
        int currentFragment;
        switch (seletedItemId) {
            case R.id.nav_chat:
                navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
                currentFragment=navController.getCurrentDestination().getId();
                if(R.id.messenger==currentFragment) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    TextView textView=findViewById(R.id.chatName);
                    textView.setText(R.string.userName);
                    bottomNav.setVisibility(View.VISIBLE);
                    CircleImageView circleImageView=findViewById(R.id.chatImage);
                    circleImageView.setImageResource(R.drawable.avatar3);
                    navController.navigate(R.id.user);
                }
                break;
            case R.id.nav_friends:
                navController = Navigation.findNavController(this, R.id.fragmentContainerView3);
                currentFragment=navController.getCurrentDestination().getId();
                if(R.id.friendList==currentFragment || R.id.groupList==currentFragment) {
                    bottomNav.setVisibility(View.VISIBLE);
                    navController.navigate(R.id.contact);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    getSupportActionBar().show();
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
}


