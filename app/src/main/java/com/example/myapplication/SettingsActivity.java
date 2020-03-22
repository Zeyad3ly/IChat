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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button UpdateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        InitializeFields();
        userName.setVisibility(View.INVISIBLE);
        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && (dataSnapshot.hasChild("name") &&
                                (dataSnapshot.hasChild("image"))))
                        {
                            String RetrieveUserName=dataSnapshot.child("name").getValue().toString();
                            String RetrieveStatus=dataSnapshot.child("status").getValue().toString();
                            String RetriveProfileImage=dataSnapshot.child("image").getValue().toString();
                            userName.setText(RetrieveUserName);
                            userStatus.setText(RetrieveStatus);
                        }else if (dataSnapshot.exists()&&(dataSnapshot.hasChild("name")))
                        {
                            String RetrieveUserName=dataSnapshot.child("name").getValue().toString();
                            String RetrieveStatus=dataSnapshot.child("status").getValue().toString();
                            userName.setText(RetrieveUserName);
                            userStatus.setText(RetrieveStatus);
                        }
                        else
                            {
                                userName.setVisibility(View.VISIBLE);
                                Toast.makeText(SettingsActivity.this, "Update your info"
                                        , Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void InitializeFields() {
        UpdateAccountSettings = findViewById(R.id.update_settings_button);
        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_profile_status);
        userProfileImage = findViewById(R.id.set_profile_image);
    }

    private void UpdateSettings() {
        String setUserName = userName.getText().toString();
        String setUserStatus = userStatus.getText().toString();
        if (TextUtils.isEmpty(setUserName)) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setUserStatus)) {
            Toast.makeText(this, "Enter status", Toast.LENGTH_SHORT).show();

        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatus);
            RootRef.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated"
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error " + message
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
