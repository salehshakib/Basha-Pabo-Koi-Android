package com.example.bashapabokoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdCreateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    LottieAnimationView liftBox, generatorBox, parkingBox, securityBox, gasBox, wifiBox, termBox;
    boolean isLiftChecked = false, isGeneratorChecked = false, isParkingChecked = false, isSecurityChecked = false, isGasChecked = false, isWifiChecked = false, isTermChecked = false;


    Spinner thanaSpinner, washSpinner, bedSpinner, religionSpinner, flatTypeSpinner, verandaSpinner, floorSpinner, genreSpinner;
    TextView vacantText;

    ImageView openCamera;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_create);

        FloatingActionButton returnFromAd = findViewById(R.id.return_from_ad);

        returnFromAd.setOnClickListener(v -> this.finish());

        liftBox = findViewById(R.id.lift);
        generatorBox = findViewById(R.id.generator);
        parkingBox = findViewById(R.id.parking);
        securityBox = findViewById(R.id.security);
        gasBox = findViewById(R.id.gas);
        wifiBox = findViewById(R.id.wifi);
        termBox = findViewById(R.id.terms_agreed);

        checkTheBoxes();

        thanaSpinner = findViewById(R.id.thana);
        washSpinner = findViewById(R.id.washroom_spinner);
        bedSpinner = findViewById(R.id.bedroom_spinner);
        religionSpinner = findViewById(R.id.religion_spinner);
        flatTypeSpinner = findViewById(R.id.flat_type_spinner);
        verandaSpinner = findViewById(R.id.verandas_spinner);
        floorSpinner = findViewById(R.id.floor_spinner);
        genreSpinner = findViewById(R.id.genre_spinner);

        vacantText = findViewById(R.id.vacant_text);
        vacantText.setOnClickListener(this);

        setSpinnerList();

        openCamera = findViewById(R.id.photos);

        openCamera.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        });

        drawerLayout = findViewById(R.id.drawer_ad);
        navigationView = findViewById(R.id.nav_view_ad);
        View header =  navigationView.getHeaderView(0);
        TextView headerProfileName = header.findViewById(R.id.profile_name_header);
        RoundedImageView headerProPic = header.findViewById(R.id.pro_pic_header);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                headerProfileName.setText(snapshot.child("name").getValue().toString());

                User u = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.me).into(headerProPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_ad);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //TODO need to add menu items
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();
                Intent intentLogOut = new Intent(AdCreateActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(AdCreateActivity.this, AdCreateActivity.class);
                startActivity(intentAd);
                break;
        }

        return true;
    }

    private void checkTheBoxes(){

        liftBox.setOnClickListener(v -> {

            if(isLiftChecked){

                liftBox.setSpeed(-1f);
                isLiftChecked = false;

            } else{

                liftBox.setSpeed(1f);
                isLiftChecked = true;
            }
            liftBox.playAnimation();
        });

        generatorBox.setOnClickListener(v -> {

            if(isGeneratorChecked){

                generatorBox.setSpeed(-1f);
                isGeneratorChecked = false;

            } else{

                generatorBox.setSpeed(1f);
                isGeneratorChecked = true;
            }
            generatorBox.playAnimation();
        });

        parkingBox.setOnClickListener(v -> {

            if(isParkingChecked){

                parkingBox.setSpeed(-1f);
                isParkingChecked = false;

            } else {

                parkingBox.setSpeed(1f);
                isParkingChecked = true;
            }
            parkingBox.playAnimation();
        });

        securityBox.setOnClickListener(v -> {

            if(isSecurityChecked){

                securityBox.setSpeed(-1f);
                isSecurityChecked = false;

            } else{

                securityBox.setSpeed(1f);
                isSecurityChecked = true;
            }
            securityBox.playAnimation();
        });

        gasBox.setOnClickListener(v -> {

            if(isGasChecked){

                gasBox.setSpeed(-1f);
                isGasChecked = false;

            } else{

                gasBox.setSpeed(1f);
                isGasChecked = true;
            }
            gasBox.playAnimation();
        });

        wifiBox.setOnClickListener(v -> {

            if(isWifiChecked){

                wifiBox.setSpeed(-1f);
                isWifiChecked = false;

            } else{

                wifiBox.setSpeed(1f);
                isWifiChecked = true;
            }
            wifiBox.playAnimation();
        });

        termBox.setOnClickListener(v -> {

            if(isTermChecked){

                termBox.setSpeed(-1f);
                isTermChecked = false;

            } else{

                termBox.setSpeed(1f);
                isTermChecked = true;
            }
            termBox.playAnimation();
        });
    }

    void setSpinnerList(){

        List<String> thanaCategories = new ArrayList<>();
        List<String> washBedVerandaCategories = new ArrayList<>();
        List<String> religionCategories = new ArrayList<>();
        List<String> flatTypeCategories = new ArrayList<>();
        List<String> floorCategories = new ArrayList<>();
        List<String> genreCategories = new ArrayList<>();

        //TODO need to add more thana
        thanaCategories.add(getString(R.string.none));
        thanaCategories.add("Adabor");
        thanaCategories.add("Motijheel");
        thanaCategories.add("Mohammadpur");
        thanaCategories.add("Ramna");
        thanaCategories.add("Paltan");

        washBedVerandaCategories.add(getString(R.string.none));
        washBedVerandaCategories.add(getString(R.string.one));
        washBedVerandaCategories.add(getString(R.string.two));
        washBedVerandaCategories.add(getString(R.string.three));
        washBedVerandaCategories.add(getString(R.string.four));
        washBedVerandaCategories.add(getString(R.string.five));
        washBedVerandaCategories.add(getString(R.string.five_plus));

        religionCategories.add(getString(R.string.none));
        religionCategories.add(getString(R.string.any));
        religionCategories.add(getString(R.string.muslim));
        religionCategories.add(getString(R.string.hindu));
        religionCategories.add(getString(R.string.christian));
        religionCategories.add(getString(R.string.buddhism));

        flatTypeCategories.add(getString(R.string.none));
        flatTypeCategories.add(getString(R.string.flat));
        flatTypeCategories.add(getString(R.string.seat));
        flatTypeCategories.add(getString(R.string.sublet));

        floorCategories.add(getString(R.string.none));
        for(int i = 1; i < 26; i++){

            floorCategories.add(Integer.toString(i));
        }

        genreCategories.add(getString(R.string.none));
        genreCategories.add(getString(R.string.family));
        genreCategories.add(getString(R.string.small_family));
        genreCategories.add(getString(R.string.only_student_male));
        genreCategories.add(getString(R.string.only_student_female));
        genreCategories.add(getString(R.string.bachelor));
        genreCategories.add(getString(R.string.only_male));
        genreCategories.add(getString(R.string.only_female));
        genreCategories.add(getString(R.string.male_service_holder));
        genreCategories.add(getString(R.string.female_service_holder));

        ArrayAdapter<String> thanaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thanaCategories);
        ArrayAdapter<String> washBedVerandaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, washBedVerandaCategories);
        ArrayAdapter<String> religionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religionCategories);
        ArrayAdapter<String> flatTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flatTypeCategories);
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floorCategories);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreCategories);

        thanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        washBedVerandaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flatTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        thanaSpinner.setAdapter(thanaAdapter);
        washSpinner.setAdapter(washBedVerandaAdapter);
        bedSpinner.setAdapter(washBedVerandaAdapter);
        verandaSpinner.setAdapter(washBedVerandaAdapter);
        religionSpinner.setAdapter(religionAdapter);
        flatTypeSpinner.setAdapter(flatTypeAdapter);
        floorSpinner.setAdapter(floorAdapter);
        genreSpinner.setAdapter(genreAdapter);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        vacantText.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {

        calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(AdCreateActivity.this, R.style.DialogTheme , this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap image = (Bitmap) data.getExtras().get("data");
        openCamera.setImageBitmap(image);
    }
}