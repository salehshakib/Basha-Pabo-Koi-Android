package com.example.bashapabokoi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.Models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdCreateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private int REQUEST_CAMERA;
    private int SELECT_FILE;

    File imageFile;
    Uri outputImageUri;
    String currentImagePath = null;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String randomKey;

    LottieAnimationView liftBox, generatorBox, parkingBox, securityBox, gasBox, wifiBox, termBox;
    boolean isLiftChecked = false, isGeneratorChecked = false, isParkingChecked = false, isSecurityChecked = false, isGasChecked = false, isWifiChecked = false, isTermChecked = false;


    Spinner thanaSpinner, washSpinner, bedSpinner, religionSpinner, flatTypeSpinner, verandaSpinner, floorSpinner, genreSpinner;
    TextView vacantText;

    ImageView img1, img2, img3, img4, img5;

    String longitude, latitude;
    String image_url1 = "no_image", image_url2 = "no_image", image_url3 = "no_image", image_url4 = "no_image", image_url5 = "no_image";

    Button createButton;
    EditText titleTextBox, addressTextBox, currentBillTextBox, waterBillTextBox, gasBillTextBox, otherChargeTextBox, descriptionTextBox, rentTextBox;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_create);

        randomKey = database.getReference().push().getKey();

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

        titleTextBox = findViewById(R.id.ad_title);
        addressTextBox = findViewById(R.id.address);
        currentBillTextBox = findViewById(R.id.current_bill);
        waterBillTextBox = findViewById(R.id.water_bill);
        gasBillTextBox = findViewById(R.id.gas_bill);
        otherChargeTextBox = findViewById(R.id.service_charge);
        descriptionTextBox = findViewById(R.id.description);
        rentTextBox = findViewById(R.id.ad_rent);



        img1 = findViewById(R.id.image_1);
        img2 = findViewById(R.id.image_2);
        img3 = findViewById(R.id.image_3);
        img4 = findViewById(R.id.image_4);
        img5 = findViewById(R.id.image_5);

        img1.setOnClickListener(v -> {

            REQUEST_CAMERA = 1;
            SELECT_FILE = 6;
            selectImage();
        });

        img2.setOnClickListener(v -> {

            REQUEST_CAMERA = 2;
            SELECT_FILE = 7;
            selectImage();
        });

        img3.setOnClickListener(v -> {

            REQUEST_CAMERA = 3;
            SELECT_FILE = 8;
            selectImage();
        });

        img4.setOnClickListener(v -> {

            REQUEST_CAMERA = 4;
            SELECT_FILE = 9;
            selectImage();
        });

        img5.setOnClickListener(v -> {

            REQUEST_CAMERA = 5;
            SELECT_FILE = 10;
            selectImage();
        });

        createButton = findViewById(R.id.but_ad_done);

        createButton.setOnClickListener(v -> {


            if (isTermChecked) {

                //TODO database handa //  done
                String title = titleTextBox.getText().toString();
                String address = addressTextBox.getText().toString();
                String currentBill = currentBillTextBox.getText().toString();
                String waterBill = waterBillTextBox.getText().toString();
                String gasBill = gasBillTextBox.getText().toString();
                String otherCharge = otherChargeTextBox.getText().toString();
                String description = descriptionTextBox.getText().toString();
                String rent = rentTextBox.getText().toString();



                String vacFrom = vacantText.getText().toString();
                String thana = thanaSpinner.getSelectedItem().toString();
                String washroom = washSpinner.getSelectedItem().toString();
                String bedroom = bedSpinner.getSelectedItem().toString();
                String religion = religionSpinner.getSelectedItem().toString();
                String flatType = flatTypeSpinner.getSelectedItem().toString();
                String veranda = verandaSpinner.getSelectedItem().toString();
                String floor = floorSpinner.getSelectedItem().toString();
                String genre = genreSpinner.getSelectedItem().toString();



                AlertDialog.Builder markerPlacementDialog = new AlertDialog.Builder(this);
                markerPlacementDialog.setTitle("Are you at your place?");
                markerPlacementDialog.setMessage("We need to add a marker in the map so that renters can find your place easily. We recommend you to be at your place for precise marking.");

                markerPlacementDialog.setPositiveButton("Yes", (dialog, which) -> {

                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    Task location = fusedLocationProviderClient.getLastLocation();

                    location.addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Location currentLocation = (Location) task.getResult();
                            longitude = Double.toString(currentLocation.getLongitude());
                            latitude = Double.toString(currentLocation.getLatitude());

                            Log.d("lungi",longitude);
                            Log.d("titti", latitude);
                            CreateAd newAd = new CreateAd(auth.getUid()+randomKey, title, address, thana, vacFrom, flatType, washroom, veranda, bedroom, floor, religion, genre, currentBill, waterBill, gasBill, otherCharge, Boolean.toString(isLiftChecked), Boolean.toString(isGeneratorChecked), Boolean.toString(isParkingChecked), Boolean.toString(isSecurityChecked), Boolean.toString(isGasChecked), Boolean.toString(isWifiChecked), description, rent, image_url1,image_url2,image_url3,image_url4,image_url5, longitude, latitude);

                            /*database.getReference().child("Create_ad").child(auth.getUid()).child(randomKey).setValue(newAd)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(AdCreateActivity.this,"ok", Toast.LENGTH_SHORT).show());
                            */
                            database.getReference().child("All_ad").child(auth.getUid()+randomKey).setValue(newAd)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(AdCreateActivity.this,"ok", Toast.LENGTH_SHORT).show());



                        }
                    });
                });

                markerPlacementDialog.setNegativeButton("No", (dialog, which) -> {



                });

                markerPlacementDialog.show();

            } else{

                Toast.makeText(this, "Please accept our terms and agreements", Toast.LENGTH_LONG).show();
            }




            /*StorageReference reference = storage.getReference().child("Ads").child(auth.getUid());
            reference.putFile(ehan e uri/bitmap img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                        }
                    });

                }
            });*/

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
                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(headerProPic);
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

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void selectImage(){

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AdCreateActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, (dialog, which) -> {

            if(items[which].equals("Camera")){

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getPackageManager()) != null){

                    imageFile = null;

                    try {
                        imageFile = getImageFile();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null){

                        outputImageUri = FileProvider.getUriForFile(this, "com.example.android.fileProvider", imageFile);
                        Log.d("aaa", outputImageUri.toString());



                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);
                        startActivityForResult(intent, REQUEST_CAMERA);

                    }
                }

            } else if(items[which].equals("Gallery")){

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);


            } else if (items[which].equals("Cancel")) {

                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == REQUEST_CAMERA){

                if(REQUEST_CAMERA == 1){

                    Bitmap image = BitmapFactory.decodeFile(currentImagePath);
                    img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img1.setImageBitmap(image);
                    //Log.d("ddd", image.toString());


                    //getImageUri(image, Bitmap.CompressFormat.JPEG, 100);
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_1");
                    reference.putFile(getImageUri(image, Bitmap.CompressFormat.JPEG, 100)).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url1 = uri.toString();
                                Log.d("img_1", image_url1);
                            });

                        }

                    });


                } else if(REQUEST_CAMERA == 2){

                    Bitmap image = BitmapFactory.decodeFile(currentImagePath);
                    img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img2.setImageBitmap(image);

                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_2");
                    reference.putFile(getImageUri(image, Bitmap.CompressFormat.JPEG, 100)).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url2 = uri.toString();
                                Log.d("img_2", image_url2);
                            });

                        }

                    });

                } else if(REQUEST_CAMERA == 3){

                    Bitmap image = BitmapFactory.decodeFile(currentImagePath);
                    img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img3.setImageBitmap(image);

                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_3");
                    reference.putFile(getImageUri(image, Bitmap.CompressFormat.JPEG, 100)).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url3 = uri.toString();
                                Log.d("img_3", image_url3);

                            });

                        }

                    });

                } else if(REQUEST_CAMERA == 4){

                    Bitmap image = BitmapFactory.decodeFile(currentImagePath);
                    img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img4.setImageBitmap(image);

                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_4");
                    reference.putFile(getImageUri(image, Bitmap.CompressFormat.JPEG, 100)).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url4 = uri.toString();
                                Log.d("img_4", image_url4);

                            });

                        }

                    });

                } else if(REQUEST_CAMERA == 5){

                    Bitmap image = BitmapFactory.decodeFile(currentImagePath);
                    img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img5.setImageBitmap(image);

                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_5");
                    reference.putFile(getImageUri(image, Bitmap.CompressFormat.JPEG, 100)).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url5 = uri.toString();
                                Log.d("img_5", image_url5);

                            });

                        }

                    });

                }


            } else if(requestCode == SELECT_FILE){

                if(SELECT_FILE == 6){

                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    Log.d("aaa",selectedImageUri.toString());
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_1");
                    reference.putFile(selectedImageUri).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url1 = uri.toString();
                                Log.d("img_1", image_url1);

                            });

                        }

                    });


                    img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img1.setImageURI(selectedImageUri);
                    //Log.d("ddd", da.toString());

                } else if(SELECT_FILE == 7){

                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_2");
                    reference.putFile(outputImageUri).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url2 = uri.toString();
                                Log.d("img_2", image_url2);

                            });

                        }

                    });
                    img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img2.setImageURI(selectedImageUri);

                } else if(SELECT_FILE == 8){

                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_3");
                    reference.putFile(outputImageUri).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url3 = uri.toString();
                                Log.d("img_3", image_url3);

                            });

                        }

                    });
                    img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img3.setImageURI(selectedImageUri);

                } else if(SELECT_FILE == 9){

                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_4");
                    reference.putFile(outputImageUri).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url4 = uri.toString();
                                Log.d("img_4", image_url4);

                            });

                        }

                    });
                    img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img4.setImageURI(selectedImageUri);

                } else if(SELECT_FILE == 10){

                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    StorageReference reference = storage.getReference().child("Ad_pictures").child(auth.getUid()).child(randomKey).child("Image_5");
                    reference.putFile(outputImageUri).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Got the download URL for 'users/me/profile.png'
                                image_url5 = uri.toString();
                                Log.d("img_5", image_url5);

                            });

                        }

                    });
                    img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img5.setImageURI(selectedImageUri);
                }

            }

        }

    }

    private File getImageFile()throws IOException{

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(timeStamp,".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }



    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "title", null);
        Log.d("aaa",Uri.parse(path).toString());

        return Uri.parse(path);

    }
}