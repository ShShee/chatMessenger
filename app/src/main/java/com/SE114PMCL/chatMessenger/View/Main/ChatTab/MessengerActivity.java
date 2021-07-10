package Main.ChatTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Adapter.ChatAdapter;
import com.SE114PMCL.chatMessenger.Model.ChatData;
import com.SE114PMCL.chatMessenger.Model.UserModel;

import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;

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


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessengerActivity extends AppCompatActivity {

    CircleImageView avatar;
    TextView tennguoidung;
    FirebaseUser fuser;
    DatabaseReference reference;
    ImageButton btn_send, btn_image;
    EditText text_send;
    ChatAdapter chatAdapter;
    List<ChatData> mchat;
    RecyclerView recyclerView;
    Intent intent;
    ValueEventListener seenListener;
    String userid;

    boolean notify = false;

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
        setContentView(R.layout.activity_messenger);

        Toolbar toolbar = findViewById(R.id.toolbarMess);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        avatar = findViewById(R.id.chatImage);
        tennguoidung = findViewById(R.id.chatName);
        btn_send = findViewById(R.id.btn_send);
        btn_image = findViewById(R.id.sendImage);
        text_send = findViewById(R.id.inputMessage);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        cameraPer = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePer = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btn_send.setOnClickListener(view -> {
            notify = true;
            String msg = text_send.getText().toString();
            if (!msg.equals("")){
                sendMessage(fuser.getUid(), userid, msg);
            } else {
                Toast.makeText(MessengerActivity.this, "Không gửi trống tin nhắn", Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
        });

        btn_image.setOnClickListener(v -> showImagePickDialog());

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                tennguoidung.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(avatar);
                }

                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
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

    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatData chat = snapshot.getValue(ChatData.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message){

        DatabaseReference chatsR = FirebaseDatabase.getInstance().getReference();
        String timestamp = getCurrentTimeStamp();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("type", "text");
        hashMap.put("isseen", false);

        chatsR.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message){
    }

    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatData chat = snapshot.getValue(ChatData.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    chatAdapter = new ChatAdapter(MessengerActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendImageMessage(Uri uri) throws IOException {
        notify = true;

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
                hashMap.put("sender", fuser.getUid());
                hashMap.put("receiver", userid);
                hashMap.put("message", downloadUri);
                hashMap.put("timestamp", timestamp);
                hashMap.put("type", "image");
                hashMap.put("isseen", false);

                databaseReference.child("Chats").push().setValue(hashMap);

                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid);

                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            chatRef.child("id").setValue(userid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(fuser.getUid());
                chatRefReceiver.child("id").setValue(fuser.getUid());

                reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        if (notify) {
                            sendNotifiaction(userid, user.getUsername(), "gửi hình ảnh");
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        })
        .addOnFailureListener(e -> progressDialog.dismiss());
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
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

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}