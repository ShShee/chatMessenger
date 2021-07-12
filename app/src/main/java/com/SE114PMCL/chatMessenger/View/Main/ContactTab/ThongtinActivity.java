package Main.ContactTab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.SE114PMCL.chatMessenger.R;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ThongtinActivity extends AppCompatActivity {

    ImageView myPhoto;
    TextView myName;
    Button myContinue;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        myPhoto = (ImageView) findViewById(R.id.imgPhoto);
        myName = (TextView)  findViewById(R.id.txtFullName);
        myContinue = (Button) findViewById(R.id.btnContinue);
        myContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                finish();
            }
        });

        if(mUser != null){
            String name = mUser.getDisplayName();
            String mail = mUser.getEmail();
            String photoURL = mUser.getPhotoUrl().toString();

            Glide.with(this).load(photoURL).into(myPhoto);
            myName.setText(name);
        }
    }
}