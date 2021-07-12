package Main.ContactTab;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.Adapter.AdapterGroupChat;
import com.SE114PMCL.chatMessenger.GroupParticipantAddActivity;
import com.SE114PMCL.chatMessenger.Model.GroupData;
import com.SE114PMCL.chatMessenger.Model.ModelGroupChat;
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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth fauth;
    DatabaseReference reference;
    String groupId, myGroupRole;

    ImageView icongr;
    ImageButton btnText, btnImage;
    TextView namegr;
    EditText textSend;
    RecyclerView chatGroup;
    Toolbar toolbar;
    Intent intent;

    List<ModelGroupChat> mGroupChat;
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

        fauth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarGr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        System.out.println("UID: " + groupId);
        icongr = findViewById(R.id.GrIcon);
        namegr = findViewById(R.id.GrName);
        textSend = findViewById(R.id.inputMessage);
        btnText = findViewById(R.id.btn_send);
        btnImage = findViewById(R.id.sendImage);

        chatGroup = findViewById(R.id.chatGroup);
        chatGroup.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatGroup.setLayoutManager(linearLayoutManager);

        cameraPer = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePer = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnText.setOnClickListener(view -> {
            String msg = textSend.getText().toString();
            if (!msg.equals("")) {
                sendMessage(msg);
            } else {
                Toast.makeText(GroupChatActivity.this, "Không gửi trống tin nhắn", Toast.LENGTH_SHORT).show();
            }
            textSend.setText("");
        });

        btnImage.setOnClickListener(v -> {
            showImagePickDialog();
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String groupTitle = "" + snapshot.child("groupTitle").getValue();
                String groupIcon = "" + snapshot.child("groupIcon").getValue();
                namegr.setText(groupTitle);
                if (groupIcon.equals("")) {
                    icongr.setImageResource(R.drawable.ic_creategroup);
                } else {
                    Glide.with(getApplicationContext()).load(groupIcon).into(icongr);
                }
                loadGroupMessages();
                loadMyGroupRole();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadMyGroupRole() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .orderByChild("id").equalTo(fauth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            myGroupRole = "" + ds.child("role").getValue();
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }


    private void loadGroupMessages() {
        mGroupChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child(groupId)
                .child("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mGroupChat.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                    mGroupChat.add(model);
                }
                adapterGroupChat = new AdapterGroupChat(GroupChatActivity.this, mGroupChat);
                chatGroup.setAdapter(adapterGroupChat);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        String timestamp = getCurrentTimeStamp();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", fauth.getUid());
        hashMap.put("timestamp", timestamp);
        hashMap.put("type", "text");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Messages").push().setValue(hashMap);

    }

    private void sendImageMessage(Uri uri) throws IOException {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang gửi");
        progressDialog.show();

        String timestamp = getCurrentTimeStamp();
        String filePath = "ChatImages/" + "post_" + timestamp;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
        byte[] data = b.toByteArray();
        StorageReference r = FirebaseStorage.getInstance().getReference().child(filePath);
        r.putBytes(data).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String downloadUri = uriTask.getResult().toString();

            if (uriTask.isSuccessful()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("message", downloadUri);
                hashMap.put("sender", fauth.getUid());
                hashMap.put("timestamp", timestamp);
                hashMap.put("type", "image");

                DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Messages").push().setValue(hashMap)
                        .addOnSuccessListener(aVoid -> textSend.setText(""))
                        .addOnFailureListener(e -> {
                            //message sending failed
                            Toast.makeText(GroupChatActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        });;
            }
        }).addOnFailureListener(e -> progressDialog.dismiss());
    }

    private void showImagePickDialog() {
        String[] options = {"Chụp", "Thư viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                if (!checkCameraPer()) {
                    requestCameraPer();
                } else {
                    pickFromCamera();
                }
            }
            if (which == 1) {
                if (!checkStoragePer()) {
                    requestStoragePer();
                } else {
                    pickFromStorage();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Chọn");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Miêu tả");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA);
    }

    private boolean checkStoragePer() {
        boolean kq = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return kq;
    }

    private void requestStoragePer() {
        ActivityCompat.requestPermissions(this, storagePer, STORAGE_REQUEST);
    }

    private boolean checkCameraPer() {
        boolean kq1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean kq2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return kq1 && kq2;
    }

    private void requestCameraPer() {
        ActivityCompat.requestPermissions(this, cameraPer, CAMERA_REQUEST);
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

        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera && storage) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Cần sự cho phép của máy ảnh hoặc thư viên", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage) {
                        pickFromStorage();
                    } else {
                        Toast.makeText(this, "Cần sự cho phép của thư viên", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY) {
                imageUri = data.getData();
                try {
                    sendImageMessage(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == IMAGE_PICK_CAMERA) {
                try {
                    sendImageMessage(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void status(String status){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.groupmenu, menu);
        System.out.println("Role: " + myGroupRole);
        if (myGroupRole != null) {
            if (myGroupRole.equals("creator") || myGroupRole.equals("admin")) {
                menu.findItem(R.id.action_add_participant).setVisible(true);
            } else {
                menu.findItem(R.id.action_add_participant).setVisible(false);
            }
        } else {
            menu.findItem(R.id.action_add_participant).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_participant) {
            Intent intent = new Intent(this, GroupParticipantAddActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}