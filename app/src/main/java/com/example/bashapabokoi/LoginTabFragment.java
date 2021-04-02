package com.example.bashapabokoi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Helper.LocaleHelper;
import com.example.bashapabokoi.databinding.LoginTabFragmentBinding;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginTabFragment extends Fragment {

    LoginTabFragmentBinding binding;

    public static boolean isLoggedIn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi);
        }

        super.onCreate(savedInstanceState);

        Paper.init(getContext());

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        binding = LoginTabFragmentBinding.inflate(getLayoutInflater());

        EditText num = binding.phoneNo;
        Button login = binding.butLogin;

        num.setTranslationX(800);
        login.setTranslationX(800);
        binding.keepMeLoggedIn.setTranslationX(800);
        binding.keepMeLoggedInText.setTranslationX(800);

        num.setAlpha(0f);
        login.setAlpha(0f);
        binding.keepMeLoggedIn.setAlpha(0f);
        binding.keepMeLoggedInText.setAlpha(0f);

        num.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.keepMeLoggedIn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.keepMeLoggedInText.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            binding.keepMeLoggedIn.setAnimation("tick_dark.json");
        }

        updateView(Paper.book().read("language"));

        binding.keepMeLoggedIn.setOnClickListener(v -> {

            if(isLoggedIn){

                binding.keepMeLoggedIn.setSpeed(-1f);
                isLoggedIn = false;

            } else{

                binding.keepMeLoggedIn.setSpeed(1f);
                isLoggedIn = true;
            }

            SharedPreferences.Editor editor = SplashActivity.sharedPref.edit();
            editor.putBoolean("keepMeLoggedIn", isLoggedIn);
            editor.apply();

            binding.keepMeLoggedIn.playAnimation();
        });

        num.requestFocus();

        login.setOnClickListener(v -> {
            if (num.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                return;

            }
            Intent intent = new Intent(getContext(), OTPActivity.class);
            intent.putExtra("phoneNumber", num.getText().toString());
            intent.putExtra("FROM_ACTIVITY", "LogInTabFragment");
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        binding.phoneNo.setHint(resources.getString(R.string.phone_number));
        binding.keepMeLoggedInText.setText(resources.getString(R.string.keep_me_logged_in));
        binding.butLogin.setText(resources.getString(R.string.log_in));
    }
}
