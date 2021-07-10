package Main.ContactTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.SE114PMCL.chatMessenger.Adapter.AdapterGroupChat;
import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Main.ChatTab.MessengerActivity;

public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth fauth;
    DatabaseReference reference;
    String groupId, myGroupRole;
    Toolbar toolbarGr;
    ImageView icon;
    TextView tenGr;
    ImageButton btnText, btnImage;
    EditText textSend;
    RecyclerView chatGr;
    Intent intent;
    ArrayList<ModelGroupChat> mGroupChat;
    AdapterGroupChat GrAdapter;

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

        icon = findViewById(R.id.groupIconIv);
        tenGr = findViewById(R.id.groupTitleTv);
        btnText = findViewById(R.id.sendBtn);
        btnImage = findViewById(R.id.attachBtn);
        fauth = FirebaseAuth.getInstance();

        chatGr = findViewById(R.id.chatRv);
        chatGr.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatGr.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        reference = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String Gtitle = "" + snapshot.child("groupTitle").getValue();
                tenGr.setText(Gtitle);
                String Gicon = "" + snapshot.child("groupIcon").getValue();
                if(Gicon == "") { icon.setImageResource(R.drawable.ic_creategroup); }
                else{Glide.with(getApplicationContext()).load(Gicon).into(icon);}
                loadGroupMessages();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btnText.setOnClickListener(v -> {
            String message=textSend.getText().toString();
            if(!message.equals("")){
                Toast.makeText(GroupChatActivity.this,"Không gửi trống tin nhắn",Toast.LENGTH_SHORT).show();
            }
            else { sendMessage(message); }
            textSend.setText("");
        });

        btnImage.setOnClickListener(v -> { showImagePickDialog(); });

    }

    private void loadGroupMessages() {
        mGroupChat = new ArrayList<>();
        DatabaseReference refG = FirebaseDatabase.getInstance().getReference();

        refG.child("Groups").child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mGroupChat.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                    mGroupChat.add(model);
                }
                GrAdapter = new AdapterGroupChat(GroupChatActivity.this, mGroupChat);
                chatGr.setAdapter(GrAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        DatabaseReference refG = FirebaseDatabase.getInstance().getReference();

        String timestamp = getCurrentTimeStamp();

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("sender", fauth.getUid());
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("type", "text");

        refG.child("Groups").child(groupId).child("Messages").push().setValue(hashMap);

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
                hashMap.put("sender", fauth.getUid());
                hashMap.put("message", downloadUri);
                hashMap.put("timestamp", timestamp);
                hashMap.put("type", "image");

                DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Messages").setValue(hashMap)
                        .addOnSuccessListener(aVoid -> textSend.setText(""))
                        .addOnFailureListener(e -> {
                            //message sending failed
                            Toast.makeText(GroupChatActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        });

            }
        }).addOnFailureListener(e -> progressDialog.dismiss());
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
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