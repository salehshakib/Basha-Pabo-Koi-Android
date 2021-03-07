package com.example.bashapabokoi.abbehSaleh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bashapabokoi.MapActivity;
import com.example.bashapabokoi.Models.User;
import com.example.bashapabokoi.databinding.ActivitySetupProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri selectedImage;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading DATA..!!");
        dialog.setCancelable(false);


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.proPic.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(intent,45);
        });

        binding.continueBtn3.setOnClickListener(v -> {
            String name = binding.profileNameText.getText().toString();

            if(name.isEmpty()) {
                binding.profileNameText.setError("Please type a name");

                return;
            }

            dialog.show();

            if(selectedImage != null){
                StorageReference reference = storage.getReference().child("profiles").child(auth.getUid());
                reference.putFile(selectedImage).addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       reference.getDownloadUrl().addOnSuccessListener(uri -> {
                           String imageUrl = uri.toString();
                           String uid = auth.getUid();
                           String phone = auth.getCurrentUser().getPhoneNumber();

                           User user = new User(uid, name, phone,imageUrl);

                           database.getReference().child("Users").child(uid).setValue(user).addOnSuccessListener(aVoid -> {

                               dialog.dismiss();

                               Toast.makeText(SetupProfileActivity.this,"ok", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(SetupProfileActivity.this, MapActivity.class);
                               startActivity(intent);

                               finish();

                           });

                       });

                   }
                });
            } else {
                String uid = auth.getUid();
                String phone = auth.getCurrentUser().getPhoneNumber();



                User user = new User(uid, name, phone, "No Image");

                database.getReference().child("Users").child(uid).setValue(user).addOnSuccessListener(aVoid -> {

                    dialog.dismiss();

                    Toast.makeText(SetupProfileActivity.this,"ok", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetupProfileActivity.this, MapActivity.class);
                    startActivity(intent);

                    finish();

                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            if(data.getData() != null) {
                binding.proPic.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }

    }
}