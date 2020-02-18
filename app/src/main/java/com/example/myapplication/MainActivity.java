package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }



    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logot_option) {
            mAuth.signOut();
            SendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.main_settings_option) {


        }
        if (item.getItemId() == R.id.main_find_friends_option) {

        }
        return true;
    }
}
