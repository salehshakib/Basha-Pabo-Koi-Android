package com.example.bashapabokoi;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Adapters.BottomSheetImageAdapter;
import com.example.bashapabokoi.Models.BottomSheetImageShower;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.Models.User;
import com.example.bashapabokoi.Notifications.Token;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int locationPermissionRequestCode = 1234;
    private static final float defaultZoom = 18f;
    private int navItemIndex;

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    private long backPressedTime;
    private Toast backToast;

    private AutoCompleteTextView searchText;
    private PlaceAutocompleteAdapterNew placeAutocompleteAdapterNew;
    private ImageView gps;

    private static final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

    private boolean mLocationPermissionGranted = false;
    private GoogleMap map;
    private AutocompleteSessionToken autocompleteSessionToken;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    View bottomSheetView;

    BottomSheetDialog bottomSheetDialog;

    private TextView netInfo;

    private static String[] ownerKey ;
    private static String anotherOwnerKey;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<LatLng> latLongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //this is new // 26 march 2021
        updateToken(FirebaseInstanceId.getInstance().getToken());

        setContentView(R.layout.activity_map);

        anotherOwnerKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        navItemIndex = R.id.nav_home;
        searchText = findViewById(R.id.input_search);
        gps = findViewById(R.id.ic_gps);

        netInfo = findViewById(R.id.con_notification);
        netInfo.setAlpha(0f);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView headerProfileName = header.findViewById(R.id.profile_name_header);
        RoundedImageView headerProPic = header.findViewById(R.id.pro_pic_header);

        bottomSheetDialog = new BottomSheetDialog(MapActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, findViewById(R.id.bottom_sheet_container));

        TextView botOwnerName = bottomSheetView.findViewById(R.id.profile_name_bottom);
        TextView botOwnerNumber = bottomSheetView.findViewById(R.id.phone_no_bottom);
        RoundedImageView botOwnerPic = bottomSheetView.findViewById(R.id.pro_pic_bottom);

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



        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latLongs.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("All_ad").getChildren()) {

                    if (dataSnapshot.child("latitude").exists() && dataSnapshot.child("longitude").exists()) {

                        latLongs.add(new LatLng(Double.parseDouble(dataSnapshot.child("latitude").getValue().toString()), Double.parseDouble(dataSnapshot.child("longitude").getValue().toString())));
                    }

                }

                for (int i = 0; i < latLongs.size(); i++) {

                    map.addMarker(new MarkerOptions().position(latLongs.get(i)).icon(vectorToBitmap()));
                }





                map.setOnMarkerClickListener(marker -> {

                    CreateAd ad = null;

                    for (DataSnapshot snapshot1 : snapshot.child("All_ad").getChildren()) {

                        if (snapshot1.child("latitude").exists() && snapshot1.child("longitude").exists()) {

                            if (marker.getPosition().latitude == Double.parseDouble(snapshot1.child("latitude").getValue().toString()) && marker.getPosition().longitude == Double.parseDouble(snapshot1.child("longitude").getValue().toString())) {

                                ad = snapshot1.getValue(CreateAd.class);
                                assert ad != null;

                                ownerKey = ad.getKey().split("-");

                                botOwnerName.setText(snapshot.child("Users").child(ownerKey[0]).child("name").getValue().toString());
                                botOwnerNumber.setText(snapshot.child("Users").child(ownerKey[0]).child("phoneNumber").getValue().toString());

                                User u = snapshot.child("Users").child(ownerKey[0]).getValue(User.class);
                                assert u != null;

                                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(botOwnerPic);


                            }

                        }

                    }





                    TextView botTitle = bottomSheetView.findViewById(R.id.title_bottom);
                    TextView botRent = bottomSheetView.findViewById(R.id.rent_bottom);
                    TextView botFlatType = bottomSheetView.findViewById(R.id.flat_type_bottom_text);
                    TextView botGenre = bottomSheetView.findViewById(R.id.genre_bottom_text);
                    TextView botReligion = bottomSheetView.findViewById(R.id.religion_bottom_text);
                    TextView botVacFrom = bottomSheetView.findViewById(R.id.vacant_from_bottom_text);
                    TextView botRating = bottomSheetView.findViewById(R.id.ad_rating_bottom);
                    Button botToDescription = bottomSheetView.findViewById(R.id.go_to_description_btn);

                    ViewPager2 botAd = bottomSheetView.findViewById(R.id.bottom_ad_shower);
                    List<BottomSheetImageShower> bottomSheetImageShowers = new ArrayList<>();

                    if (!ad.getImageUrl1().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl1()));
                    }

                    if (!ad.getImageUrl2().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl2()));
                    }

                    if (!ad.getImageUrl3().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl3()));
                    }

                    if (!ad.getImageUrl4().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl4()));
                    }

                    if (!ad.getImageUrl5().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl5()));
                    }

                    botTitle.setText(ad.getTitle());
                    botRent.setText(ad.getRent());
                    botFlatType.setText(ad.getFlatType());
                    botGenre.setText(ad.getGenre());
                    botReligion.setText(ad.getReligion());
                    botRating.setText("4.9/5");

                    try {

                        String[] date = ad.getVacFrom().split("-");
                        String month;

                        switch (date[1]) {
                            case "01":

                                month = "January";

                                break;
                            case "02":

                                month = "February";

                                break;
                            case "03":

                                month = "March";

                                break;
                            case "04":

                                month = "April";

                                break;
                            case "05":

                                month = "May";

                                break;
                            case "06":

                                month = "June";

                                break;
                            case "07":

                                month = "July";

                                break;
                            case "08":

                                month = "August";

                                break;
                            case "09":

                                month = "September";

                                break;
                            case "10":

                                month = "October";

                                break;
                            case "11":

                                month = "November";

                                break;
                            case "12":

                                month = "December";

                                break;
                            default:

                                month = ad.getVacFrom();
                        }

                        botVacFrom.setText(month);

                    } catch (ArrayIndexOutOfBoundsException e) {

                        botVacFrom.setText(ad.getVacFrom());
                    }

                    botAd.setAdapter(new BottomSheetImageAdapter(bottomSheetImageShowers));

                    botAd.setClipToPadding(false);
                    botAd.setClipChildren(false);
                    botAd.setOffscreenPageLimit(3);
                    botAd.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                    compositePageTransformer.addTransformer((page, position) -> {

                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.95f + r * 0.05f);
                    });

                    botAd.setPageTransformer(compositePageTransformer);

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();

                    CreateAd finalAd = ad;
                    botToDescription.setOnClickListener(v -> {

                        Intent intent = new Intent(MapActivity.this, AdDescriptionActivity.class);
                        intent.putExtra("imageUri1", finalAd.getImageUrl1());
                        intent.putExtra("imageUri2", finalAd.getImageUrl2());
                        intent.putExtra("imageUri3", finalAd.getImageUrl3());
                        intent.putExtra("imageUri4", finalAd.getImageUrl4());
                        intent.putExtra("imageUri5", finalAd.getImageUrl5());
                        intent.putExtra("title", finalAd.getTitle());
                        intent.putExtra("address", finalAd.getAddress());
                        intent.putExtra("flatType", finalAd.getFlatType());
                        intent.putExtra("washroom", finalAd.getWashroom());
                        intent.putExtra("vacantFrom", finalAd.getVacFrom());
                        intent.putExtra("veranda", finalAd.getVeranda());
                        intent.putExtra("bedroom", finalAd.getBedroom());
                        intent.putExtra("floor", finalAd.getFloor());
                        intent.putExtra("religion", finalAd.getReligion());
                        intent.putExtra("genre", finalAd.getGenre());
                        intent.putExtra("electricityBill", finalAd.getCurrentBill());
                        intent.putExtra("waterBill", finalAd.getWaterBill());
                        intent.putExtra("gasBill", finalAd.getGasBill());
                        intent.putExtra("serviceCharge", finalAd.getOtherCharges());
                        intent.putExtra("lift", finalAd.getLift());
                        intent.putExtra("generator", finalAd.getGenerator());
                        intent.putExtra("parking", finalAd.getParking());
                        intent.putExtra("security", finalAd.getSecurity());
                        intent.putExtra("gas", finalAd.getGas());
                        intent.putExtra("wifi", finalAd.getWifi());
                        intent.putExtra("details", finalAd.getDescription());
                        intent.putExtra("rent", finalAd.getRent());
                        intent.putExtra("lat", finalAd.getLatitude());
                        intent.putExtra("long", finalAd.getLongitude());
                        intent.putExtra("ownerKey", finalAd.getKey());
                        intent.putExtra("thana", finalAd.getThana());
                        intent.putExtra("FROM_ACTIVITY", "ListViewFragment");
                        startActivity(intent);
                    });
                    return true;
                });









            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        /*FirebaseDatabase.getInstance().getReference().child("All_ad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*latLongs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("latitude").exists() && dataSnapshot.child("longitude").exists()) {

                        latLongs.add(new LatLng(Double.parseDouble(dataSnapshot.child("latitude").getValue().toString()), Double.parseDouble(dataSnapshot.child("longitude").getValue().toString())));
                    }

                }

                for (int i = 0; i < latLongs.size(); i++) {

                    map.addMarker(new MarkerOptions().position(latLongs.get(i)).icon(vectorToBitmap()));
                }

                map.setOnMarkerClickListener(marker -> {

                    CreateAd ad = null;



                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        if (snapshot1.child("latitude").exists() && snapshot1.child("longitude").exists()) {

                            if (marker.getPosition().latitude == Double.parseDouble(snapshot1.child("latitude").getValue().toString()) && marker.getPosition().longitude == Double.parseDouble(snapshot1.child("longitude").getValue().toString())) {

                                ad = snapshot1.getValue(CreateAd.class);
                                assert ad != null;

                                ownerKey = ad.getKey().split("-");
                                ok.setOwnerKey(ownerKey[0]);


                            }

                        }

                    }

                    TextView botTitle = bottomSheetView.findViewById(R.id.title_bottom);
                    TextView botRent = bottomSheetView.findViewById(R.id.rent_bottom);
                    TextView botFlatType = bottomSheetView.findViewById(R.id.flat_type_bottom_text);
                    TextView botGenre = bottomSheetView.findViewById(R.id.genre_bottom_text);
                    TextView botReligion = bottomSheetView.findViewById(R.id.religion_bottom_text);
                    TextView botVacFrom = bottomSheetView.findViewById(R.id.vacant_from_bottom_text);
                    TextView botRating = bottomSheetView.findViewById(R.id.ad_rating_bottom);
                    Button botToDescription = bottomSheetView.findViewById(R.id.go_to_description_btn);

                    ViewPager2 botAd = bottomSheetView.findViewById(R.id.bottom_ad_shower);
                    List<BottomSheetImageShower> bottomSheetImageShowers = new ArrayList<>();

                    if (!ad.getImageUrl1().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl1()));
                    }

                    if (!ad.getImageUrl2().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl2()));
                    }

                    if (!ad.getImageUrl3().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl3()));
                    }

                    if (!ad.getImageUrl4().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl4()));
                    }

                    if (!ad.getImageUrl5().matches("no_image")) {

                        bottomSheetImageShowers.add(new BottomSheetImageShower(ad.getImageUrl5()));
                    }

                    botTitle.setText(ad.getTitle());
                    botRent.setText(ad.getRent());
                    botFlatType.setText(ad.getFlatType());
                    botGenre.setText(ad.getGenre());
                    botReligion.setText(ad.getReligion());
                    botRating.setText("4.9/5");

                    try {

                        String[] date = ad.getVacFrom().split("-");
                        String month;

                        switch (date[1]) {
                            case "01":

                                month = "January";

                                break;
                            case "02":

                                month = "February";

                                break;
                            case "03":

                                month = "March";

                                break;
                            case "04":

                                month = "April";

                                break;
                            case "05":

                                month = "May";

                                break;
                            case "06":

                                month = "June";

                                break;
                            case "07":

                                month = "July";

                                break;
                            case "08":

                                month = "August";

                                break;
                            case "09":

                                month = "September";

                                break;
                            case "10":

                                month = "October";

                                break;
                            case "11":

                                month = "November";

                                break;
                            case "12":

                                month = "December";

                                break;
                            default:

                                month = ad.getVacFrom();
                        }

                        botVacFrom.setText(month);

                    } catch (ArrayIndexOutOfBoundsException e) {

                        botVacFrom.setText(ad.getVacFrom());
                    }

                    botAd.setAdapter(new BottomSheetImageAdapter(bottomSheetImageShowers));

                    botAd.setClipToPadding(false);
                    botAd.setClipChildren(false);
                    botAd.setOffscreenPageLimit(3);
                    botAd.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                    compositePageTransformer.addTransformer((page, position) -> {

                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.95f + r * 0.05f);
                    });

                    botAd.setPageTransformer(compositePageTransformer);

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();

                    CreateAd finalAd = ad;
                    botToDescription.setOnClickListener(v -> {

                        Intent intent = new Intent(MapActivity.this, AdDescriptionActivity.class);
                        intent.putExtra("imageUri1", finalAd.getImageUrl1());
                        intent.putExtra("imageUri2", finalAd.getImageUrl2());
                        intent.putExtra("imageUri3", finalAd.getImageUrl3());
                        intent.putExtra("imageUri4", finalAd.getImageUrl4());
                        intent.putExtra("imageUri5", finalAd.getImageUrl5());
                        intent.putExtra("title", finalAd.getTitle());
                        intent.putExtra("address", finalAd.getAddress());
                        intent.putExtra("flatType", finalAd.getFlatType());
                        intent.putExtra("washroom", finalAd.getWashroom());
                        intent.putExtra("vacantFrom", finalAd.getVacFrom());
                        intent.putExtra("veranda", finalAd.getVeranda());
                        intent.putExtra("bedroom", finalAd.getBedroom());
                        intent.putExtra("floor", finalAd.getFloor());
                        intent.putExtra("religion", finalAd.getReligion());
                        intent.putExtra("genre", finalAd.getGenre());
                        intent.putExtra("electricityBill", finalAd.getCurrentBill());
                        intent.putExtra("waterBill", finalAd.getWaterBill());
                        intent.putExtra("gasBill", finalAd.getGasBill());
                        intent.putExtra("serviceCharge", finalAd.getOtherCharges());
                        intent.putExtra("lift", finalAd.getLift());
                        intent.putExtra("generator", finalAd.getGenerator());
                        intent.putExtra("parking", finalAd.getParking());
                        intent.putExtra("security", finalAd.getSecurity());
                        intent.putExtra("gas", finalAd.getGas());
                        intent.putExtra("wifi", finalAd.getWifi());
                        intent.putExtra("details", finalAd.getDescription());
                        intent.putExtra("rent", finalAd.getRent());
                        intent.putExtra("lat", finalAd.getLatitude());
                        intent.putExtra("long", finalAd.getLongitude());
                        intent.putExtra("ownerKey", finalAd.getKey());
                        intent.putExtra("thana", finalAd.getThana());
                        intent.putExtra("FROM_ACTIVITY", "ListViewFragment");
                        startActivity(intent);
                    });
                    return true;
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        /*FirebaseDatabase.getInstance().getReference().child("Users").child(ok.getOwnerKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                botOwnerName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                botOwnerNumber.setText(Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString());

                User u = snapshot.getValue(User.class);
                assert u != null;

                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(botOwnerPic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        drawerLayout.addDrawerListener(drawerListener);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        if (isServicesOK()) {

            getLocationPermission();
        }

    }

    private void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);


    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {
                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

                    ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#212121"), Color.parseColor("#0BA100"));
                    valueAnimator.setDuration(500);

                    valueAnimator.addUpdateListener(animation -> {

                        netInfo.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                        netInfo.setText(R.string.back_online);
                    });
                    valueAnimator.start();

                    netInfo.animate().translationY(0).alpha(0).setDuration(1000).setStartDelay(600).start();

                } else {

                    netInfo.setText(R.string.no_connection);
                    netInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));

                    netInfo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
                }
            }

        }
    };

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragments;

            if (item.getItemId() != R.id.nav_menu) {

                navItemIndex = item.getItemId();

            }

            switch (item.getItemId()) {

                case R.id.nav_menu:
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;

                case R.id.nav_list_view:
                    selectedFragments = new ListViewFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bot_nav, selectedFragments).commit();
                    break;

                case R.id.nav_home:
                    selectedFragments = new TransparentFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bot_nav, selectedFragments).commit();
                    break;

                case R.id.nav_wish_list:
                    selectedFragments = new WishListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bot_nav, selectedFragments).commit();
                    break;

                case R.id.nav_chats:
                    selectedFragments = new ChatsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bot_nav, selectedFragments).commit();
                    break;
            }

            return true;
        }
    };

    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

            if (navItemIndex == R.id.nav_list_view) {

                bottomNavigationView.getMenu().getItem(1).setChecked(true);

            } else if (navItemIndex == R.id.nav_home) {

                bottomNavigationView.getMenu().getItem(2).setChecked(true);

            } else if (navItemIndex == R.id.nav_wish_list) {

                bottomNavigationView.getMenu().getItem(3).setChecked(true);

            } else if (navItemIndex == R.id.nav_chats) {

                bottomNavigationView.getMenu().getItem(4).setChecked(true);

            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (mLocationPermissionGranted) {

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            init();

        }
    }

    private void init() {

        //TODO NEEDS FUNDING !!!
        /*Places.initialize(getApplicationContext(), "AIzaSyBqQWQmhVknktG58BFC4w8pFenyn_xtD60");
        autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient=Places.createClient(getApplicationContext());
        placeAutocompleteAdapterNew = new PlaceAutocompleteAdapterNew(MapActivity.this,placesClient,autocompleteSessionToken);
        searchText.setAdapter(placeAutocompleteAdapterNew);*/

        searchText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                geoLocate();
            }
            return false;
        });

        gps.setOnClickListener(v -> getDeviceLocation());
    }

    private void geoLocate() {

        String search = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {

            list = geocoder.getFromLocationName(search, 1);
        } catch (IOException ignored) {

        }

        if (list.size() > 0) {

            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), address.getAddressLine(0));

            map.setOnMarkerClickListener(marker -> {


                return true;
            });
        }


    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (backPressedTime + 2000 > System.currentTimeMillis()) {

                backToast.cancel();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());

                final SharedPreferences sharedPref = MapActivity.this.getPreferences(Context.MODE_PRIVATE);

                if (!sharedPref.getBoolean("keepMeLoggedIn", false)) {

                    FirebaseAuth.getInstance().signOut();
                }

                System.exit(1);
                return;
            } else {

                backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backPressedTime = System.currentTimeMillis();
        }

    }

    private void getDeviceLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocationPermissionGranted) {

                Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Location currentLocation = (Location) task.getResult();

                        if (isDeviceLocationOn() && currentLocation != null) {

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), "My Location");
                        }
                    } else {

                        Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (SecurityException ignored) {

        }
    }

    private void autoSuggestions() {


    }

    private void enableLoc() {

        googleApiClient = null;

        googleApiClient = new GoogleApiClient.Builder(MapActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {
                googleApiClient.connect();
            }
        })
                .addOnConnectionFailedListener(connectionResult -> {

                }).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(MapActivity.this, REQUEST_LOCATION);

                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
            }
        });
    }

    private boolean isDeviceLocationOn() {

        LocationManager lm = (LocationManager) MapActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        if (!gpsEnabled) {

            // notify user
            enableLoc();
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return gpsEnabled;
    }

    private void moveCamera(LatLng latLng, String title) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapActivity.defaultZoom));

        if (!title.equals("My Location")) {

            MarkerOptions options = new MarkerOptions().position(latLng).title(title).icon(vectorToBitmap());

            map.addMarker(options);
        }
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {

        String[] permissions = {fineLocation, coarseLocation};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), fineLocation) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), coarseLocation) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                initMap();
            } else {

                ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode);
            }
        } else {

            ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.length > 0) {

                for (int grantResult : grantResults) {

                    if (grantResult != PackageManager.PERMISSION_GRANTED) {

                        mLocationPermissionGranted = false;
                        return;
                    }
                }

                mLocationPermissionGranted = true;
                initMap();
            }
        }

    }

    public boolean isServicesOK() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);

        if (available == ConnectionResult.SUCCESS) {

            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapActivity.this, available, ERROR_DIALOG_REQUEST);
            assert dialog != null;
            dialog.show();

        } else {

            Toast.makeText(this, "No Resolve Found", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    //TODO need to add menu items
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_pro:

                Intent intentProfile = new Intent(MapActivity.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentProfile);
                break;

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = SplashActivity.sharedPref.edit();
                editor.putBoolean("keepMeLoggedIn", false);
                editor.apply();

                Intent intentLogOut = new Intent(MapActivity.this, LoginActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(MapActivity.this, AdCreateActivity.class);
                intentAd.putExtra("FROM_ACTIVITY", "MapActivity");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentAd);
                break;

            case R.id.nav_settings:

                Intent intentSettings = new Intent(MapActivity.this, SettingsActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentSettings);
                break;
        }

        return true;
    }

    private BitmapDescriptor vectorToBitmap() {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_custom_marker, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}