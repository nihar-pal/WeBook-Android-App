package com.spacester.webook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.spacester.webook.Pages.CreateActivity;
import com.spacester.webook.Pages.HomeFragment;
import com.spacester.webook.Pages.ProfileFragment;
import com.spacester.webook.Pages.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationSelected);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationSelected =
            item -> {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_reels:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                       startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                        break;
                    case R.id.nav_user:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.logout:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to Logout?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                  FirebaseAuth.getInstance().signOut();
                                  startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                  finish();
                                })
                                .setNegativeButton("No", null)
                                .show();
                        break;
                }
                if (selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                }
                return true;
            };

}