package Main.ContactTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SE114PMCL.chatMessenger.R;
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
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GroupEditActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =200;

    private static final int IMAGE_PICK_CAMERA_CODE=300;
    private static final int IMAGE_PICK_GALLERY_CODE=400;

    private String[] cameraPermissions;
    private  String[] storagePermissions;

    private Uri image_uri=null;

    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private TextView infoName;
    private ImageView groupIconIv;
    private EditText groupTitleEt,groupDescriptionEt;
    private Button editGroupBt;

    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        groupId=getIntent().getStringExtra("groupId");
        infoName=findViewById(R.id.infoName);
        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleEt=findViewById(R.id.groupTitleEt);
        groupDescriptionEt=findViewById(R.id.groupDescriptionEt);
        editGroupBt=findViewById(R.id.editGroupBt);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait . . .");
        progressDialog.setCanceledOnTouchOutside(false);

        //Init permission arrays
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth=FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String groupTitle = "" + snapshot.child("groupTitle").getValue();
                String groupIcon = "" + snapshot.child("groupIcon").getValue();
                String groupDescription = "" + snapshot.child("groupDescription").getValue();

                infoName.setText("Edit "+groupTitle);
                groupTitleEt.setText(groupTitle);
                groupDescriptionEt.setText(groupDescription);

                try {
                    Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_white).into(groupIconIv);
                } catch (Exception e) {
                    groupIconIv.setImageResource(R.drawable.ic_group_white);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //pick image
        groupIconIv.setOnClickListener(v -> showImagePickDialog());


        //handle click event
        editGroupBt.setOnClickListener(v -> startUpdatingGroup());
    }

    private void startUpdatingGroup() {
        String groupTitle=groupTitleEt.getText().toString().trim();
        String groupDescription=groupDescriptionEt.getText().toString().trim();
        if(TextUtils.isEmpty(groupTitle)) {
            TastyToast.makeText(GroupEditActivity.this,"Please enter group title !",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
            return;
        }

        progressDialog.setMessage("Updating group info . . .");
        progressDialog.show();

        if(image_uri==null) {

            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("groupTitle",groupTitle);
            hashMap.put("groupDescription",groupDescription);

            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
            ref.child(groupId).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            TastyToast.makeText(GroupEditActivity.this,"Group info updated !",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            progressDialog.dismiss();
                            TastyToast.makeText(GroupEditActivity.this,""+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                        }
                    });
        } else {
            String timestamp=""+System.currentTimeMillis();
            String filePathAndName="Group_Imags/"+"image"+"_"+timestamp;

            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> p_uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!p_uriTask.isSuccessful());
                            Uri p_downloadUri= p_uriTask.getResult();
                            if(p_uriTask.isSuccessful()) {
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("groupTitle",groupTitle);
                                hashMap.put("groupDescription",groupDescription);
                                hashMap.put("groupIcon",""+p_downloadUri);

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
                                ref.child(groupId).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                TastyToast.makeText(GroupEditActivity.this,"Group info updated !",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                progressDialog.dismiss();
                                                TastyToast.makeText(GroupEditActivity.this,""+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            progressDialog.dismiss();
                            TastyToast.makeText(GroupEditActivity.this,""+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                        }
                    });
        }
    }

    private void showImagePickDialog() {
        //options to pick image from
        String[] options={"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Image:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle click
                        if(which==0){
                            //camera clicked
                            if(!checkCameraPermissions()){
                                requestCameraPermissions();
                            }
                            else{
                                pickFromCamera();
                            }
                        }
                        else{
                            //gallery clicked
                            if(!checkStoragePermissions()){
                                requestStoragePermissions();
                            }
                            else{
                                pickFromGallery();
                            }
                        }
                    }
                }).show();
    }

    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){

        ContentValues cv =new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Group Image Icon Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Group Image Icon Description");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions(){
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        boolean result=ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
    private void  requestCameraPermissions(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        //handle permission result
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //permission allowed
                        pickFromCamera();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this,"Camera & Storage permissions required",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this,"Storage permissions required",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        //handle image pick result
        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                //was picked from gallery
                image_uri=data.getData();
                //set to imageview
                groupIconIv.setImageURI(image_uri);
            }
            else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                //was picked from camera
                //set to imageview
                groupIconIv.setImageURI(image_uri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}