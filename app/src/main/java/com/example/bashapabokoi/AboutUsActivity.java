package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

import io.paperdb.Paper;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton returnFromAboutUs;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else {

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        returnFromAboutUs = findViewById(R.id.return_from_about_us);

        drawerLayout = findViewById(R.id.drawer_ad);
        navigationView = findViewById(R.id.nav_view_about_us);

        View header =  navigationView.getHeaderView(0);
        TextView headerProfileName = header.findViewById(R.id.profile_name_header);
        RoundedImageView headerProPic = header.findViewById(R.id.pro_pic_header);

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                headerProfileName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());

                User u = snapshot.getValue(User.class);
                assert u != null;
                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(headerProPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        returnFromAboutUs.setOnClickListener(v -> finish());

        updateView(Paper.book().read("language"));

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_about);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_pro:

                Intent intentProfile = new Intent(AboutUsActivity.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentProfile);
                break;

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = SplashActivity.sharedPref.edit();
                editor.putBoolean("keepMeLoggedIn", false);
                editor.apply();

                Intent intentLogOut = new Intent(AboutUsActivity.this, LoginActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(AboutUsActivity.this, AdCreateActivity.class);
                intentAd.putExtra("FROM_ACTIVITY", "MapActivity");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentAd);
                break;

            case R.id.nav_settings:

                Intent intentSettings = new Intent(AboutUsActivity.this, SettingsActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentSettings);
                break;
        }

        return true;
    }

    private void updateView(String language) {

        if(language.matches("bn")){

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu_bn);
        } else{

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        }
    }

}