package com.example.bashapabokoi.abbehSaleh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bashapabokoi.R;
import com.example.bashapabokoi.databinding.ActivityOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
        //////



        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.phoneLebel.setText("Tor Number ki " + phoneNumber + " ?");


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(com.example.bashapabokoi.abbehSaleh.OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        dialog.dismiss();
                        verificationId = verifyId;




                    }
                }).build();


        PhoneAuthProvider.verifyPhoneNumber(options);

        otpInput  = (EditText) findViewById(R.id.otpView);



        binding.continueBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otpInput.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(com.example.bashapabokoi.abbehSaleh.OTPActivity.this, "Sabash mamur beta Log in Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(com.example.bashapabokoi.abbehSaleh.OTPActivity.this, SetupProfileActivity.class);
                            startActivity(intent);
                            //finishAffinity();
                        }

                        else {
                            Toast.makeText(com.example.bashapabokoi.abbehSaleh.OTPActivity.this,"Sala OTP vul disos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        /*binding.otpView.{
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(OTPActivity.this, "Sabash mamur beta Log in Success", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(OTPActivity.this,"Sala OTP vul disos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/



        /*OtpView otpView;
        otpView = findViewById(R.id.otp_view);
        otpView.setListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                // do Stuff
                Log.d("onOtpCompleted=>", otp);
            }
        });
*/

    }
}