package com.example.bashapabokoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.abbehSaleh.OTPActivity;
import com.example.bashapabokoi.databinding.SignupTabFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment extends Fragment {

    SignupTabFragmentBinding binding;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       // super.onCreate(savedInstanceState);
        binding = SignupTabFragmentBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        binding.phoneNoSignUp.setText(null);
        binding.phoneNoSignUp.requestFocus();

        binding.butSignNext.setOnClickListener(v -> {

            if(!binding.phoneNoSignUp.getText().toString().matches("")){

                Intent intent = new Intent(getContext(), OTPActivity.class);
                intent.putExtra("phoneNumber", binding.phoneNoSignUp.getText().toString());
                intent.putExtra("FROM_ACTIVITY", "SignUpTabFragment");
                startActivity(intent);
            } else{

                Toast.makeText(getContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
            }

        });

        return binding.getRoot();
    }
}
