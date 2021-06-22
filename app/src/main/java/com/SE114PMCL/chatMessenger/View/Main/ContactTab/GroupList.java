package Main.ContactTab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SE114PMCL.chatMessenger.Adapter.GroupListAdapter;
import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class GroupList extends Fragment implements GroupListAdapter.OnGroupListener {
    RecyclerView recyclerView;
    NavController navController;
    Toolbar toolbar;
    BottomNavigationView navBar;
    ArrayList<GroupData> listGroup;
    GroupListAdapter groupListAdapter;

    public GroupList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.friendView);
        listGroup=new ArrayList<>();
        listGroup.add(new GroupData("Cat KingDom",R.drawable.avatar1));
        listGroup.add(new GroupData("Chaien and Friends",R.drawable.chaien));
        groupListAdapter=new GroupListAdapter(getActivity().getApplicationContext(),listGroup,this::onGroupClick);
        recyclerView.setAdapter(groupListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        toolbar = view.findViewById(R.id.toolbarGroup);
        //toolbar.inflateMenu(R.menu.groupmenu);
    }

    @Override
    public void onGroupClick(int position) {

    }
}