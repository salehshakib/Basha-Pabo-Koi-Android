package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bashapabokoi.databinding.ActivityOTPBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    ActivityOTPBinding binding;
    FirebaseAuth auth;
    String verificationId;
    ProgressDialog dialog;
    EditText otpInput;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading hocce...");
        dialog.setCancelable(false);
        dialog.show();

        /////// firebase auth

        auth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.phoneLebel.setText(getString(R.string.is_your_number) + " " + phoneNumber + " ?");

        try{

            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(OTPActivity.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    dialog.dismiss();
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

            String otp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent previousIntent = getIntent();
                    String previousActivity = previousIntent.getStringExtra("FROM_ACTIVITY");
                    FirebaseUser u_s_e_r = task.getResult().getUser();
                    long creationTimestamp = u_s_e_r.getMetadata().getCreationTimestamp();
                    long lastSignInTimestamp = u_s_e_r.getMetadata().getLastSignInTimestamp();

                    if (creationTimestamp == lastSignInTimestamp) {
                        //do create new user
                        //todo ehan e signup na sign in oidar kam
                        if(previousActivity.equals("SignUpTabFragment")){

                            Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this, "Tui janos tor account nai tar poreo tui kn login korte aisos, vag BSDK", Toast.LENGTH_LONG).show();

                            auth.getCurrentUser().delete();

                        }
                    } else {
                        //user is exists, just do login
                        if(previousActivity.equals("LogInTabFragment")){

                            Intent intent = new Intent(OTPActivity.this, MapActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(this, "Tui janos tor account ase tar poreo tui kn signup korte aisos, vag BSDK", Toast.LENGTH_LONG).show();

                            auth.signOut();
                        }

                    }

                    //Toast.makeText(OTPActivity.this, "Sabash mamur beta Log in Success", Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(OTPActivity.this,"Sala OTP vul disos", Toast.LENGTH_SHORT).show();
                }
            });


        });
    }
}