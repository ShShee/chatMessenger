package com.SE114PMCL.chatMessenger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Setting extends Fragment implements PendingListAdapter.OnPendingListener{
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<FriendData> listPending;
    PendingListAdapter pendingListAdapter;
    public Setting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Swipe to log out
        SwipeButton enableButton = view.findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Toast.makeText(getActivity(), "State: " + active, Toast.LENGTH_SHORT).show();
            }
        });

        //Edit name
        SwipeLayout swipeLayout =  view.findViewById(R.id.sample1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

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
        });

        //Pending
        recyclerView=view.findViewById(R.id.pendingView);
        listPending=new ArrayList<>();
        listPending.add(new FriendData("0","Boss",R.drawable.avatar1,"",false));
        listPending.add(new FriendData("0","Chaien",R.drawable.chaien,"",false));
        listPending.add(new FriendData("0","Hooney",R.drawable.chaien,"",false));
        listPending.add(new FriendData("0","Cat",R.drawable.chaien,"",false));
        pendingListAdapter=new PendingListAdapter(getActivity().getApplicationContext(),listPending,this::onPendingClick);
        recyclerView.setAdapter(pendingListAdapter);
    }

    @Override
    public void onPendingClick(int position) {

    }
}