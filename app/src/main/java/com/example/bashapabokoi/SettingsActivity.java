package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Helper.LocaleHelper;
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

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LottieAnimationView langSwitch, langSwitchDark, modeSwitch, modeSwitchDark;

    TextView modeState, langState, title, languageText, mode, eng, on, off, report, reportDes, exp, expDes;

    Button reportButton, submitButton;

    public static boolean isLangOn = false;
    public static boolean isModeOn = false;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    public static final String PREFERENCES = "darkModePrefs";
    public static final String KEY = "isDarkModeOn";
    SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        modeState = findViewById(R.id.switch_mode_state);
        langState = findViewById(R.id.switch_lang_state);

        title = findViewById(R.id.settings_title);
        languageText = findViewById(R.id.language_settings_text);
        mode = findViewById(R.id.dark_mode_settings_text);
        eng = findViewById(R.id.eng);
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);
        report = findViewById(R.id.report_a_bug);
        reportDes = findViewById(R.id.bug_des);
        exp = findViewById(R.id.share_your_exp);
        expDes = findViewById(R.id.exp_des);

        langSwitch = findViewById(R.id.switch_lang);
        langSwitchDark = findViewById(R.id.switch_lang_dark);
        modeSwitch = findViewById(R.id.switch_mode);
        modeSwitchDark = findViewById(R.id.switch_mode_dark);

        reportButton = findViewById(R.id.report_bug_btn);
        submitButton = findViewById(R.id.share_experience_btn);

        FloatingActionButton returnFromSettings = findViewById(R.id.return_from_settings);

        returnFromSettings.setOnClickListener(v -> finish());

        updateView(Paper.book().read("language"));

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            modeState.setText(resources.getString(R.string.on));
        } else{

            modeState.setText(resources.getString(R.string.off));
        }

        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);

        isLangOn = sharedPref.getBoolean("Language", false);
        isModeOn = sharedPref.getBoolean("Mode", false);

        loadDarkModeState();

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            modeSwitch.setProgress(0.5f);
            modeSwitchDark.setProgress(0.5f);

            modeSwitch.setAlpha(0f);
            langSwitch.setAlpha(0f);
        } else{

            modeSwitchDark.setAlpha(0f);
            langSwitchDark.setAlpha(0f);
        }

        if(isLangOn){

            langSwitch.setProgress(0.5f);
            langSwitchDark.setProgress(0.5f);
        }

        drawerLayout = findViewById(R.id.drawer_ad);
        navigationView = findViewById(R.id.nav_view_settings);
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

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_settings);
        navigationView.setNavigationItemSelectedListener(this);

        langSwitch.setOnClickListener(v -> {

            if(isLangOn){

                langSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                langSwitchDark.setMinAndMaxProgress(0.5f, 1.0f);

                Paper.book().write("language", "en");
                isLangOn = false;

            } else{

                langSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                langSwitchDark.setMinAndMaxProgress(0.0f, 0.5f);

                Paper.book().write("language", "bn");
                isLangOn = true;
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Language", isLangOn);
            editor.apply();

            langSwitch.playAnimation();
            langSwitchDark.playAnimation();
            updateView(Paper.book().read("language"));
        });

        modeSwitch.setOnClickListener(v -> {

            modeSwitchDark.setAlpha(1f);
            langSwitchDark.setAlpha(1f);

            if(isModeOn){

                modeSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                modeSwitchDark.setMinAndMaxProgress(0.5f, 1.0f);

                modeSwitch.animate().alpha(1.0f).setDuration(500);
                langSwitch.animate().alpha(1.0f).setDuration(500);

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveDarkModeState(false);

                isModeOn = false;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Mode", isModeOn);
                editor.apply();

            } else{

                modeSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                modeSwitchDark.setMinAndMaxProgress(0.0f, 0.5f);

                modeSwitch.animate().alpha(0.0f).setDuration(500);
                langSwitch.animate().alpha(0.0f).setDuration(500);

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveDarkModeState(true);

                isModeOn = true;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Mode", isModeOn);
                editor.apply();

            }
            modeSwitch.playAnimation();
            modeSwitchDark.playAnimation();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        });

        reportButton.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, ReportABugActivity.class)));

        submitButton.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, ShareYourExperienceActivity.class)));

    }

    private void saveDarkModeState(boolean darkModeState) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY, darkModeState);
        editor.apply();
    }

    private void loadDarkModeState(){

        if(sharedPreferences.getBoolean(KEY, false)){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else{

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        title.setText(resources.getString(R.string.settings_title));
        languageText.setText(resources.getString(R.string.language));
        mode.setText(resources.getString(R.string.dark_mode));
        eng.setText(resources.getString(R.string.eng));
        on.setText(resources.getString(R.string.on));
        off.setText(resources.getString(R.string.off));
        report.setText(resources.getString(R.string.report_a_bug));
        reportDes.setText(resources.getString(R.string.found_a_bug_click_here_to_report_it));
        exp.setText(resources.getString(R.string.share_your_experience));
        expDes.setText(resources.getString(R.string.share_your_using_experience_about_the_app));
        reportButton.setText(resources.getString(R.string.report));
        submitButton.setText(resources.getString(R.string.submit));
        langState.setText(resources.getString(R.string.lang));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_pro:

                Intent intentProfile = new Intent(SettingsActivity.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentProfile);
                break;

            case R.id.nav_out:

                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = SplashActivity.sharedPref.edit();
                editor.putBoolean("keepMeLoggedIn", false);
                editor.apply();

                Intent intentLogOut = new Intent(SettingsActivity.this, LoginActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentLogOut);
                break;

            case R.id.nav_ad:

                Intent intentAd = new Intent(SettingsActivity.this, AdCreateActivity.class);
                intentAd.putExtra("FROM_ACTIVITY", "MapActivity");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intentAd);
                break;

        }

        return true;
    }
}