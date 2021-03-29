package com.example.bashapabokoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class SettingsActivity extends AppCompatActivity {

    LottieAnimationView langSwitch, langSwitchDark, modeSwitch, modeSwitchDark;

    Button reportButton, submitButton;

    public static boolean isLangOn = false;
    public static boolean isModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        langSwitch = findViewById(R.id.switch_lang);
        langSwitchDark = findViewById(R.id.switch_lang_dark);
        modeSwitch = findViewById(R.id.switch_mode);
        modeSwitchDark = findViewById(R.id.switch_mode_dark);

        reportButton = findViewById(R.id.report_bug_btn);
        submitButton = findViewById(R.id.share_experience_btn);

        final SharedPreferences sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);

        isLangOn = sharedPref.getBoolean("Language", false);
        isModeOn = sharedPref.getBoolean("Mode", false);

        if(isModeOn){

            modeSwitch.setProgress(0.5f);
            modeSwitchDark.setProgress(0.5f);

            modeSwitch.setAlpha(0f);
            langSwitch.setAlpha(0f);

        } else {

            modeSwitchDark.setAlpha(0f);
            langSwitchDark.setAlpha(0f);
        }

        if(isLangOn){

            langSwitch.setProgress(0.5f);
            langSwitchDark.setProgress(0.5f);
        }

        langSwitch.setOnClickListener(v -> {

            if(isLangOn){

                langSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                langSwitchDark.setMinAndMaxProgress(0.5f, 1.0f);

                isLangOn = false;

            } else{

                langSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                langSwitchDark.setMinAndMaxProgress(0.0f, 0.5f);

                isLangOn = true;
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Language", isLangOn);
            editor.apply();

            langSwitch.playAnimation();
            langSwitchDark.playAnimation();
        });

        modeSwitch.setOnClickListener(v -> {

            modeSwitchDark.setAlpha(1f);
            langSwitchDark.setAlpha(1f);

            if(isModeOn){

                modeSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                modeSwitchDark.setMinAndMaxProgress(0.5f, 1.0f);

                modeSwitch.animate().alpha(1.0f).setDuration(500);
                langSwitch.animate().alpha(1.0f).setDuration(500);

                isModeOn = false;

            } else{

                modeSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                modeSwitchDark.setMinAndMaxProgress(0.0f, 0.5f);

                modeSwitch.animate().alpha(0.0f).setDuration(500);
                langSwitch.animate().alpha(0.0f).setDuration(500);

                isModeOn = true;
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Mode", isModeOn);
            editor.apply();

            modeSwitch.playAnimation();
            modeSwitchDark.playAnimation();
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO goto bug a report page
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO goto experience share page
            }
        });

    }
}