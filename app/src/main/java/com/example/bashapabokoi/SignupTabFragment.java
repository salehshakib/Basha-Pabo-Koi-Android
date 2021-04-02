package com.example.bashapabokoi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Helper.LocaleHelper;
import com.example.bashapabokoi.databinding.SignupTabFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import io.paperdb.Paper;

public class SignupTabFragment extends Fragment {

    SignupTabFragmentBinding binding;
    FirebaseAuth auth;

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

        binding = SignupTabFragmentBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        binding.phoneNoSignUp.setText(null);
        binding.phoneNoSignUp.requestFocus();

        updateView(Paper.book().read("language"));

        binding.butSignNext.setOnClickListener(v -> {

            if (binding.phoneNoSignUp.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                return;

            }

            Intent intent = new Intent(getContext(), OTPActivity.class);
            intent.putExtra("phoneNumber", binding.phoneNoSignUp.getText().toString());
            intent.putExtra("FROM_ACTIVITY", "SignUpTabFragment");
            startActivity(intent);

        });

        return binding.getRoot();
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        binding.phoneNoSignUp.setHint(resources.getString(R.string.phone_number));
        binding.butSignNext.setText(resources.getString(R.string.next));
    }
}
