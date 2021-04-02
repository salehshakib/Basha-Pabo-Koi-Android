package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Adapters.OwnerAdAdapter;
import com.example.bashapabokoi.Helper.LocaleHelper;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.Models.OwnerAdShower;
import com.example.bashapabokoi.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RoundedImageView profilePicture;
    @SuppressLint("StaticFieldLeak")
    public static TextView ProfileName, profilePhoneNo, profileAddress, profileMail;

    TextView yourAdTitle;

    CardView editAddress, editEmail;

    FloatingActionButton returnFromProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        yourAdTitle = findViewById(R.id.your_ad_title);

        profilePicture = findViewById(R.id.pro_pic);
        ProfileName = findViewById(R.id.profile_name);
        profilePhoneNo = findViewById(R.id.phone_no_profile);
        profileAddress = findViewById(R.id.address_profile);
        profileMail = findViewById(R.id.email_profile);

        returnFromProfile = findViewById(R.id.return_from_profile);

        editEmail = findViewById(R.id.profile_email);
        editAddress = findViewById(R.id.profile_address);

        drawerLayout = findViewById(R.id.drawer_profile);
        navigationView = findViewById(R.id.nav_view_profile);
        View header =  navigationView.getHeaderView(0);
        TextView headerProfileName = header.findViewById(R.id.profile_name_header);
        RoundedImageView headerProPic = header.findViewById(R.id.pro_pic_header);

        updateView(Paper.book().read("language"));

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                headerProfileName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                ProfileName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                profilePhoneNo.setText(Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString());

                User u = snapshot.getValue(User.class);

                assert u != null;
                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(headerProPic);
                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(profilePicture);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {

                    profileAddress.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());

                } catch (NullPointerException e){

                    profileAddress.setText(getString(R.string.address_eg));
                }

                try {

                    profileMail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());

                } catch (NullPointerException e){

                    profileMail.setText(getString(R.string.email_eg));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("All_ad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ViewPager2 ownerAd = findViewById(R.id.owner_ad_shower);
                List<OwnerAdShower> ownerAdShowers = new ArrayList<>();
                List<CreateAd> ownerAdDetails = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String[] arrOfStr = Objects.requireNonNull(dataSnapshot.getKey()).split("-");
                    for (String s : arrOfStr){
                        if(FirebaseAuth.getInstance().getUid().equals(s)){

                            OwnerAdShower ad = new OwnerAdShower(Objects.requireNonNull(dataSnapshot.child("rent").getValue()).toString(), Objects.requireNonNull(dataSnapshot.child("thana").getValue()).toString(), Objects.requireNonNull(dataSnapshot.child("flatType").getValue()).toString(), Objects.requireNonNull(dataSnapshot.child("genre").getValue()).toString(), Objects.requireNonNull(dataSnapshot.child("imageUrl1").getValue()).toString(), 4.5f);

                            ownerAdShowers.add(ad);                                //"https://t.auntmia.com/nthumbs/2016-03-08/2284861/2284861_12b.jpg"

                            /*OwnerAdShower ad2 = new OwnerAdShower("78000", "Motijheel", "Flat", "Female only", "https://t.auntmia.com/nthumbs/2015-05-07/2360911/2360911_10b.jpg", 4.9f);

                            ownerAdShowers.add(ad2);                                //"https://t.auntmia.com/nthumbs/2015-05-07/2360911/2360911_10b.jpg"

                            OwnerAdShower ad3 = new OwnerAdShower("6000", "Mohammadpur", "Sublet", "Family", "https://t.auntmia.com/nthumbs/2014-01-26/2697314/2697314_19b.jpg", 3.5f);

                            ownerAdShowers.add(ad3);  */                              //"https://t.auntmia.com/nthumbs/2014-01-26/2697314/2697314_19b.jpg"

                            ownerAdDetails.add(dataSnapshot.getValue(CreateAd.class));

                            break;

                        }
                        break;
                    }


                }
                ownerAd.setAdapter(new OwnerAdAdapter(ProfileActivity.this, ownerAdShowers, ownerAdDetails));

                ownerAd.setClipToPadding(false);
                ownerAd.setClipChildren(false);
                ownerAd.setOffscreenPageLimit(3);
                ownerAd.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                compositePageTransformer.addTransformer((page, position) -> {

                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.95f + r * 0.05f);
                });

                ownerAd.setPageTransformer(compositePageTransformer);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_pro);
        navigationView.setNavigationItemSelectedListener(this);

        editEmail.setOnClickListener(v -> {

            CustomEditDialog editNumber = new CustomEditDialog(ProfileActivity.this, "Edit email", "Enter your new email", 1);
            editNumber.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            editNumber.show();
        });

        editAddress.setOnClickListener(v -> {

            CustomEditDialog editNumber = new CustomEditDialog(ProfileActivity.this, "Edit address", "Enter your new address", 2);
            editNumber.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            editNumber.show();
        });


        returnFromProfile.setOnClickListener(v -> finish());
    }

    //TODO need to add menu items
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = SplashActivity.sharedPref.edit();
                editor.putBoolean("keepMeLoggedIn", false);
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intentLogOut = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(ProfileActivity.this, AdCreateActivity.class);
                intentAd.putExtra("FROM_ACTIVITY", "ProfileActivity");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentAd);
                break;

            case R.id.nav_settings:

                Intent intentSettings = new Intent(ProfileActivity.this, SettingsActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentSettings);
                break;
        }

        return true;
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        yourAdTitle.setText(resources.getString(R.string.your_ads));
    }
}