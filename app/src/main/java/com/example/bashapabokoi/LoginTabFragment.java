package com.example.bashapabokoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.abbehSaleh.OTPActivity;
import com.example.bashapabokoi.databinding.LoginTabFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    LoginTabFragmentBinding binding;

    public static boolean isLoggedIn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = LoginTabFragmentBinding.inflate(getLayoutInflater());

        EditText num = binding.phoneNo;
        Button login = binding.butLogin;

        num.setTranslationX(800);
        login.setTranslationX(800);
        binding.keepMeLoggedIn.setTranslationX(800);
        binding.textView6.setTranslationX(800);

        num.setAlpha(0f);
        login.setAlpha(0f);
        binding.keepMeLoggedIn.setAlpha(0f);
        binding.textView6.setAlpha(0f);

        num.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.keepMeLoggedIn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.textView6.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        binding.keepMeLoggedIn.setOnClickListener(v -> {

            if(isLoggedIn){

                binding.keepMeLoggedIn.setSpeed(-1f);
                isLoggedIn = false;

            } else{

                binding.keepMeLoggedIn.setSpeed(1f);
                isLoggedIn = true;
            }
            binding.keepMeLoggedIn.playAnimation();
        });

        //TODO KEEP ME LOGGED IN

        num.requestFocus();

        login.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OTPActivity.class);
            intent.putExtra("phoneNumber", num.getText().toString());
            intent.putExtra("FROM_ACTIVITY", "LogInTabFragment");
            startActivity(intent);
        });

        return binding.getRoot();
    }
}
