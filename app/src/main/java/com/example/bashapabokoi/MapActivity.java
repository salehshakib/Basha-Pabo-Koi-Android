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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int locationPermissionRequestCode = 1234;
    private static final float defaultZoom = 18f;
    private int navItemIndex;

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    private  long backPressedTime;
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

    private TextView netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        navItemIndex = R.id.nav_home;
        searchText = findViewById(R.id.input_search);
        gps = findViewById(R.id.ic_gps);

        netInfo = findViewById(R.id.con_notification);
        netInfo.setAlpha(0f);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        View header =  navigationView.getHeaderView(0);
        TextView headerProfileName = (TextView)header.findViewById(R.id.profile_name_header);
        RoundedImageView headerProPic = (RoundedImageView)header.findViewById(R.id.pro_pic_header);

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

            if(intent.getExtras()!=null) {
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {

                    ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#212121"), Color.parseColor("#0BA100"));
                    valueAnimator.setDuration(500);

                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            netInfo.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                            netInfo.setText(R.string.back_online);
                        }
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

            Fragment selectedFragments = null;

            if(item.getItemId() != R.id.nav_menu){

                navItemIndex = item.getItemId();

            }

            switch (item.getItemId()){

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

            Log.d("ItemIndex", "Value " + navItemIndex);

            if(navItemIndex == R.id.nav_list_view){

                bottomNavigationView.getMenu().getItem(1).setChecked(true);

            } else if(navItemIndex == R.id.nav_home){

                bottomNavigationView.getMenu().getItem(2).setChecked(true);

            } else if(navItemIndex == R.id.nav_wish_list){

                bottomNavigationView.getMenu().getItem(3).setChecked(true);

            } else if(navItemIndex == R.id.nav_chats){

                bottomNavigationView.getMenu().getItem(4).setChecked(true);

            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "MAP IS READY", Toast.LENGTH_SHORT).show();
        map = googleMap;

        if (mLocationPermissionGranted) {

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            init();
            //autoSuggestions();
        }
    }

    private void init(){

        //TODO NEEDS FUNDING !!!
        /*Places.initialize(getApplicationContext(), "AIzaSyBqQWQmhVknktG58BFC4w8pFenyn_xtD60");
        autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient=Places.createClient(getApplicationContext());
        placeAutocompleteAdapterNew = new PlaceAutocompleteAdapterNew(MapActivity.this,placesClient,autocompleteSessionToken);
        searchText.setAdapter(placeAutocompleteAdapterNew);*/

        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();
                }
                return false;
            }
        });

        gps.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                getDeviceLocation();
            }
        });
    }

    private void geoLocate(){

        String search = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try{

            list = geocoder.getFromLocationName(search, 1);
        } catch (IOException ignored){

        }

        if(list.size() > 0){

            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), defaultZoom, address.getAddressLine(0));

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapActivity.this, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, findViewById(R.id.bottom_sheet_container));

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();

                    TextView botAdd = (TextView)bottomSheetView.findViewById(R.id.bottom_address);
                    botAdd.setText(address.getAddressLine(0));
                    return true;
                }
            });
        }


    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        } else{

            if(backPressedTime + 2000 > System.currentTimeMillis()){

                backToast.cancel();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return;
            } else{

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

                        if(isDeviceLocationOn() && currentLocation != null){

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), defaultZoom, "My Location");
                        }
                    } else {

                        Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (SecurityException ignored) {

        }
    }

    private void autoSuggestions(){


    }

    private void enableLoc() {

        googleApiClient = null;

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MapActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NotNull ConnectionResult connectionResult) {

                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapActivity.this, REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                    }
                }
            });
        }
    }

    private boolean isDeviceLocationOn(){

        LocationManager lm = (LocationManager)MapActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        if(!gpsEnabled) {

            // notify user
            enableLoc();
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return gpsEnabled;
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){

            //TODO MARKER E JHAMELA ASE 
            MarkerOptions options = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker));

            map.addMarker(options);
        }
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

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

        switch(item.getItemId()){

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();
                Intent intentLogOut = new Intent(MapActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(MapActivity.this, AdCreateActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.d("NAV MENU", "VALUE: " + navigationView.getMenu().getItem(3));
                startActivity(intentAd);
                break;
        }

        return true;
    }
}

