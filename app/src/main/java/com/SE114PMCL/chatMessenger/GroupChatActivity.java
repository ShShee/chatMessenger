package com.SE114PMCL.chatMessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SE114PMCL.chatMessenger.Adapter.AdapterGroupChat;
import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
import com.SE114PMCL.chatMessenger.Model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String groupId, myGroupRole="";

    Toolbar toolbar;
    ImageView groupIconIv;
    ImageButton attachBtn, sendBtn;
    TextView groupTitleTv;
    EditText messageEt;
    RecyclerView chatRv;
    Intent intent;

    ArrayList<ModelGroupChat> groupChatList;
    AdapterGroupChat adapterGroupChat;

    private static final int CAMERA_REQUEST = 1;
    private static final int STORAGE_REQUEST = 2;
    private static final int IMAGE_PICK_CAMERA = 3;
    private static final int IMAGE_PICK_GALLERY = 4;

    String[] cameraPer;
    String[] storagePer;

    Uri imageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleTv=findViewById(R.id.groupTitleTv);
        attachBtn=findViewById(R.id.attachBtn);
        messageEt=findViewById(R.id.messageEt);
        sendBtn=findViewById(R.id.sendBtn);

        chatRv = findViewById(R.id.chatRv);
        chatRv.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);

        intent = getIntent();
        groupId=intent.getStringExtra("groupId");

        firebaseAuth =FirebaseAuth.getInstance();


        sendBtn.setOnClickListener(v -> {
            String message=messageEt.getText().toString().trim();
            if(TextUtils.isEmpty(message)){
                Toast.makeText(GroupChatActivity.this,"Không gửi trống tin nhắn",Toast.LENGTH_SHORT).show();
            }
            else{ sendMessage(message); }
            messageEt.setText("");
        });

        attachBtn.setOnClickListener(v -> { showImagePickDialog(); });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                loadGroupInfo();
                loadGroupMessages();
                loadMyGroupRole();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void loadMyGroupRole() {
        databaseReference.child(groupId).child("Participants")
                .orderByChild("id").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            myGroupRole=""+ds.child("role").getValue();
                            //refresh menu items
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    private void loadGroupMessages() {
        groupChatList = new ArrayList<>();

        databaseReference.child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    groupChatList.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                        groupChatList.add(model);
                    }
                    adapterGroupChat = new AdapterGroupChat(GroupChatActivity.this, groupChatList);
                    chatRv.setAdapter(adapterGroupChat);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    private void loadGroupInfo() {
        databaseReference.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String groupTitle=""+ds.child("groupTitle").getValue();
                    String groupDescription=""+ds.child("groupDescription").getValue();
                    String groupIcon=""+ds.child("groupIcon").getValue();
                    String timestamp=""+ds.child("timestamp").getValue();
                    String createBy=""+ds.child("createBy").getValue();

                    groupTitleTv.setText(groupTitle);
                    try {
                        Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_white).into(groupIconIv);

                    }
                    catch(Exception e){
                        groupIconIv.setImageResource(R.drawable.ic_group_white);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        String timestamp = getCurrentTimeStamp();

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("sender", firebaseAuth.getUid());
        hashMap.put("message", message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("type","text");

        //add in db
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Messages").push().setValue(hashMap);

    }

    private void sendImageMessage(Uri uri) throws IOException {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang gửi");
        progressDialog.show();

        String timestamp = "" + getCurrentTimeStamp();
        String filePath = "ChatImages/" + "post_" + timestamp;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
        byte[] data = b.toByteArray();
        StorageReference r = FirebaseStorage.getInstance().getReference().child(filePath);
        r.putBytes(data).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while(!uriTask.isSuccessful());
            String downloadUri = uriTask.getResult().toString();

            if(uriTask.isSuccessful()){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", firebaseAuth.getUid());
                hashMap.put("message", downloadUri);
                hashMap.put("timestamp", timestamp);
                hashMap.put("type", "image");

                DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Messages").child(timestamp)
                        .setValue(hashMap)
                        .addOnSuccessListener(aVoid -> messageEt.setText(""))
                        .addOnFailureListener(e -> {
                            //message sending failed
                            Toast.makeText(GroupChatActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        });

            }
        }).addOnFailureListener(e -> progressDialog.dismiss());
    }

    private void showImagePickDialog(){
        String[] options = {"Chụp", "Thư viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0){
                if (!checkCameraPer()) {requestCameraPer();}
                else { pickFromCamera(); }
            }
            if (which == 1){
                if (!checkStoragePer()) { requestStoragePer(); }
                else { pickFromStorage(); }
            }
        });
        builder.create().show();
    }

    private void pickFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Chọn");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Miêu tả");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA);
    }

    private boolean checkStoragePer(){
        boolean kq = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return kq;
    }

    private void requestStoragePer(){
        ActivityCompat.requestPermissions(this, storagePer, STORAGE_REQUEST);
    }

    private boolean checkCameraPer(){
        boolean kq1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean kq2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return kq1 && kq2;
    }

    private void requestCameraPer(){
        ActivityCompat.requestPermissions(this, cameraPer, CAMERA_REQUEST);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.groupmenu, menu);

        if(myGroupRole.equals("creator")||myGroupRole.equals("admin")){
            //in admin/creator, show add person option
            menu.findItem(R.id.action_add_participant).setVisible(true);
        }
        else{
            menu.findItem(R.id.action_add_participant).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_add_participant){
            Intent intent=new Intent(this,GroupParticipantAddActivity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(camera && storage){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Cần sự cho phép của máy ảnh hoặc thư viên", Toast.LENGTH_SHORT).show();
                    }
                }
                else{}
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length>0){
                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storage){
                        pickFromStorage();
                    }
                    else{
                        Toast.makeText(this, "Cần sự cho phép của thư viên", Toast.LENGTH_SHORT).show();
                    }
                }
                else{}
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY){
                imageUri = data.getData();
                try {
                    sendImageMessage(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(requestCode == IMAGE_PICK_CAMERA){
                try {
                    sendImageMessage(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}