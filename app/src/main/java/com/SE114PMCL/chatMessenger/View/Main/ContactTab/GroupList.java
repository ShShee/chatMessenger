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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.cert.PolicyNode;
import java.util.ArrayList;


public class GroupList extends Fragment {

    private RecyclerView groupsRv;

    private FirebaseAuth firebaseAuth;

    private ArrayList<GroupData> groupData;
    private GroupListAdapter groupListAdapter;

    public GroupList() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        groupsRv = view.findViewById(R.id.groupsRv);

        firebaseAuth = FirebaseAuth.getInstance();

        loadGroupChatList();

        return view;
    }

    private void loadGroupChatList() {
        groupData = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                groupData.size();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("Participants").child(firebaseAuth.getUid()).exists()) {
                        GroupData model = ds.getValue(GroupData.class);
                        groupData.add(model);
                    }
                    groupListAdapter = new GroupListAdapter(getActivity(), groupData);
                    groupsRv.setAdapter(groupListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}