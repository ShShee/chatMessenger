package Main.ChatTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.SE114PMCL.chatMessenger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Messenger extends Fragment {
    NavController navController;
    Toolbar toolbar;
    BottomNavigationView navBar;
    public Messenger() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messenger, container, false);
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarUsername);
        navBar = getActivity().findViewById(R.id.bottom_navigation);
        navController= Navigation.findNavController(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.inflateMenu(R.menu.messengermenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked reset user info
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
               /* ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
                TextView textView=(TextView)getActivity().findViewById(R.id.chatName);
                textView.setText(R.string.userName);
                CircleImageView circleImageView=(CircleImageView)getActivity().findViewById(R.id.chatImage);
                circleImageView.setImageResource(R.drawable.avatar3);*/
                NavOptions.Builder navBuilder =  new NavOptions.Builder();
                navBuilder.setEnterAnim(R.anim.fragment_open_enter).setExitAnim(R.anim.fragment_close_exit).setPopEnterAnim(R.anim.fragment_open_enter).setPopExitAnim(R.anim.fragment_close_exit);
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                navBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(view).navigate(R.id.user,null,navBuilder.build());
            }
        });
    }
}