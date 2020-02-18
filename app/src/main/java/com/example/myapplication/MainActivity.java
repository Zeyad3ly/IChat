package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_app_bar)
    Toolbar mainAppBar;
    @BindView(R.id.main_tabs)
    TabLayout mainTabs;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_tabs_pager)
    ViewPager mainTabsPager;
    private FirebaseUser currentUser;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mainAppBar);
        getSupportActionBar().setTitle("IChat");
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        mainTabsPager.setAdapter(myTabsAccessorAdapter);
        mainTabs.setupWithViewPager(mainTabsPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            SendUserToLoginActivity();
        }

    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }
}
