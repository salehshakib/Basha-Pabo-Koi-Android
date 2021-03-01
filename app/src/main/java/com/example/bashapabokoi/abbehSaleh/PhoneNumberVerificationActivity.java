package com.example.bashapabokoi.abbehSaleh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bashapabokoi.MapActivity;
import com.example.bashapabokoi.databinding.ActivityPhoneNumberVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberVerificationActivity extends AppCompatActivity {



    ActivityPhoneNumberVerificationBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneNumberVerificationBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();
        }

        binding.phoneBox.requestFocus();

        binding.continueBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.bashapabokoi.abbehSaleh.PhoneNumberVerificationActivity.this, com.example.bashapabokoi.abbehSaleh.OTPActivity.class);
                intent.putExtra("phoneNumber", binding.phoneBox.getText().toString());
                startActivity(intent);
            }
        });
    }
}