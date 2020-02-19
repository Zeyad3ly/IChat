package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.set_profile_image)
    CircleImageView setProfileImage;
    @BindView(R.id.set_user_name)
    EditText setUserName;
    @BindView(R.id.set_profile_status)
    EditText setProfileStatus;
    @BindView(R.id.update_settings_button)
    Button updateSettingsButton;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        updateSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
    }

    private void UpdateSettings() {
        String name = setUserName.getText().toString();
        String status = setProfileStatus.getText().toString();
        if (TextUtils.isEmpty(name)) {
            TastyToast.makeText(SettingsActivity.this, "You must provide a name",
                    TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        } else if (TextUtils.isEmpty(status)) {
            TastyToast.makeText(SettingsActivity.this, "You must provide a status",
                    TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("name", name);
            profileMap.put("status", status);
            RootRef.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                TastyToast.makeText(SettingsActivity.this,
                                        "Profile Updated",
                                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            } else {
                                String message = task.getException().toString();
                                TastyToast.makeText(SettingsActivity.this,
                                        "Error " + message,
                                        TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }
                        }

                    });

        }
    }

    private void SendUserToMainActivity() {
        Intent MainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }
}
