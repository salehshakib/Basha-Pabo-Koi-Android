package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bashapabokoi.Helper.LocaleHelper;
import com.example.bashapabokoi.databinding.ActivityOTPBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

@SuppressWarnings("deprecation")
public class OTPActivity extends AppCompatActivity {

    ActivityOTPBinding binding;
    FirebaseAuth auth;
    String verificationId;
    ProgressDialog dialog;
    EditText otpInput;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            setTheme(R.style.Theme_BashaPaboKoi);
        }

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        binding.resendOtpText.setVisibility(View.INVISIBLE);
        binding.resendOtpBtn.setVisibility(View.INVISIBLE);
        binding.otpTimer.setVisibility(View.INVISIBLE);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading hocce...");
        dialog.setCancelable(false);
        dialog.show();

        /////// firebase auth

        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();

        auth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.phoneLabel.setText(resources.getString(R.string.is_your_number) + " " + phoneNumber + " ?");
        binding.textView2.setText(resources.getString(R.string.please_check_your_phone_s_inbox_for_the_otp));
        binding.otpText.setHint(resources.getString(R.string.enter_your_otp_here));
        binding.butContinueSignUp.setHint(resources.getString(R.string.continue_sign_up));

        try{

            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(OTPActivity.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    dialog.dismiss();
                    binding.otpTimer.setVisibility(View.VISIBLE);

                    long time = TimeUnit.MINUTES.toMillis(1);
                    new CountDownTimer(time, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            String sTime = String.format(Locale.ENGLISH, "%02d", TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1)));
                            binding.otpTimer.setText(sTime);
                        }

                        @Override
                        public void onFinish() {

                            binding.otpTimer.setVisibility(View.INVISIBLE);
                            binding.resendOtpText.setVisibility(View.VISIBLE);
                            binding.resendOtpBtn.setVisibility(View.VISIBLE);
                        }
                    }.start();
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    dialog.dismiss();
                    Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(verifyId, forceResendingToken);
                    dialog.dismiss();
                    verificationId = verifyId;

                }
            }).build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (IllegalArgumentException ignored){


        }

        otpInput  = findViewById(R.id.otp_text);

        binding.butContinueSignUp.setOnClickListener(v -> {

            if (otpInput.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Enter Your OTP First", Toast.LENGTH_SHORT).show();
                return;
            }

            String otp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent previousIntent = getIntent();
                    String previousActivity = previousIntent.getStringExtra("FROM_ACTIVITY");
                    FirebaseUser u_s_e_r = task.getResult().getUser();
                    assert u_s_e_r != null;
                    long creationTimestamp = Objects.requireNonNull(u_s_e_r.getMetadata()).getCreationTimestamp();
                    long lastSignInTimestamp = u_s_e_r.getMetadata().getLastSignInTimestamp();

                    if (creationTimestamp == lastSignInTimestamp) {

                        if(previousActivity.equals("SignUpTabFragment")){

                            Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this, "No account found! Try signing in.", Toast.LENGTH_LONG).show();

                            Objects.requireNonNull(auth.getCurrentUser()).delete();

                        }
                    } else {
                        //user is exists, just do login
                        if(previousActivity.equals("LogInTabFragment")){

                            Intent intent = new Intent(OTPActivity.this, MapActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(this, "User already exists, try log in!", Toast.LENGTH_LONG).show();

                            auth.signOut();
                        }

                    }

                    Toast.makeText(OTPActivity.this, "Log in Successful!", Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(OTPActivity.this,"Wrong OTP!", Toast.LENGTH_SHORT).show();
                }
            });


        });
    }

}