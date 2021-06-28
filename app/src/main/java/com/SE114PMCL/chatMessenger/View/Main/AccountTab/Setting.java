package Main.AccountTab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Controller.StartActivity;

import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.SE114PMCL.chatMessenger.Adapter.PendingListAdapter;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.schwaab.avvylib.AvatarView;

import static android.app.Activity.RESULT_OK;


public class Setting extends Fragment implements PendingListAdapter.OnPendingListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<UserModel> listPending;
    PendingListAdapter pendingListAdapter;

    DatabaseReference reference;
    FirebaseUser fuser;

    AvatarView image_setting;
    TextView username_setting;

    ImageButton change_name;
    ImageButton change_avatar;


    LayoutInflater inflater;
    AlertDialog.Builder rename_alert;

    FragmentContainerView fragmentContainerView;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

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

        image_setting = (AvatarView) view.findViewById(R.id.Image_setting);
        username_setting = (TextView) view.findViewById(R.id.Name);


        change_name = (ImageButton) view.findViewById(R.id.changeName);
        change_avatar = (ImageButton) view.findViewById(R.id.changeAvatar);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        rename_alert = new AlertDialog.Builder(getContext());

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                //View view = inflater.inflate(R.layout.rename, null);
                View view  = getActivity().getLayoutInflater().inflate(R.layout.rename, null);

                rename_alert.setTitle("Rename")
                        .setMessage("Enter your new name to change")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //validate the email address
                                EditText renametxt = view.findViewById(R.id.txtRename);

                                if(renametxt.getText().toString().isEmpty()){
                                    renametxt.setError("Required Field");
                                    return;
                                }
                                fuser = FirebaseAuth.getInstance().getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("Users");

                                HashMap hashMap = new HashMap();
                                hashMap.put("username",renametxt.getText().toString());
                                reference.child(fuser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(getContext(), "Your name has been changed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            }
        });

        change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


        username_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                //View view = inflater.inflate(R.layout.rename, null);
                View view  = getActivity().getLayoutInflater().inflate(R.layout.rename, null);

                rename_alert.setTitle("Rename")
                        .setMessage("Enter your new name to change")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //validate the email address
                                EditText renametxt = view.findViewById(R.id.txtRename);

                                if(renametxt.getText().toString().isEmpty()){
                                    renametxt.setError("Required Field");
                                    return;
                                }
                                fuser = FirebaseAuth.getInstance().getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("Users");

//                                reference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                                        username_setting.setText(renametxt.getText().toString());
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                                    }
//                                });
                                HashMap hashMap = new HashMap();
                                hashMap.put("username",renametxt.getText().toString());
                                reference.child(fuser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(getContext(), "Your name has been changed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            }
        });

        image_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                username_setting.setText(userModel.getUsername());

                if(userModel.getImageURL().equals("default")){
                    image_setting.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getContext()).load(userModel.getImageURL()).into(image_setting);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Swipe to log out
        SwipeButton enableButton = view.findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                //Toast.makeText(getActivity(), "State: " + active, Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), StartActivity.class));
                getActivity().finish();

            }
        });

        //Pending
//        recyclerView=view.findViewById(R.id.pendingView);
//        listPending=new ArrayList<>();
//
//        listPending.add(new FriendData("0","Boss",R.drawable.avatar1,"",false, ""));
//        listPending.add(new FriendData("0","Chaien",R.drawable.chaien,"",false, ""));
//        listPending.add(new FriendData("0","Hooney",R.drawable.chaien,"",false, ""));
//        listPending.add(new FriendData("0","Cat",R.drawable.chaien,"",false, ""));
//
//        pendingListAdapter=new PendingListAdapter(getActivity().getApplicationContext(),listPending,this::onPendingClick);
//        recyclerView.setAdapter(pendingListAdapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        fragmentContainerView = view.findViewById(R.id.fragmentContainerView4);
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase .getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "upload in progress", Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }

    @Override
    public void onPendingClick(int position) {

    }
}