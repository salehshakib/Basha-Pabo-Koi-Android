package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Adapters.CheckboxValueAdapter;
import com.example.bashapabokoi.Adapters.DescriptionImageAdapter;
import com.example.bashapabokoi.Models.CheckBoxValueShower;
import com.example.bashapabokoi.Models.DescriptionImageShower;
import com.example.bashapabokoi.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdDescriptionActivity extends AppCompatActivity {

    DescriptionImageShower image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_description);

        final TextView title = findViewById(R.id.description_title);
        final TextView address = findViewById(R.id.address_description_text);
        final TextView flatType = findViewById(R.id.flat_type_description_text);
        final TextView washroom = findViewById(R.id.washroom_description_text);
        final TextView vacFrom = findViewById(R.id.vacant_from_description_text);
        final TextView veranda = findViewById(R.id.veranda_description_text);
        final TextView bedroom = findViewById(R.id.bedroom_description_text);
        final TextView floor = findViewById(R.id.floor_description_text);
        final TextView religion = findViewById(R.id.religion_description_text);
        final TextView genre = findViewById(R.id.genre_description_text);
        final TextView electricityBill = findViewById(R.id.electricity_bill_description_text);
        final TextView waterBill = findViewById(R.id.water_bill_description_text);
        final TextView gasBill = findViewById(R.id.gas_bill_description_text);
        final TextView serviceCharge = findViewById(R.id.service_charge_description_text);
        final TextView details = findViewById(R.id.description_description_text);
        final TextView rent = findViewById(R.id.rent_description_text);
        final TextView ownerName = findViewById(R.id.description_profile_name);
        final TextView ownerPhoneNo = findViewById(R.id.description_phone_no);

        final ImageButton descriptionToChat = findViewById(R.id.description_to_chat);

        final RoundedImageView ownerProPic = findViewById(R.id.pro_pic_description);

        final ViewPager2 adImage = findViewById(R.id.description_image_shower);
        final List<DescriptionImageShower> descriptionImageShowers = new ArrayList<>();

        Intent previousIntentImage = getIntent();

        if(!previousIntentImage.getStringExtra("imageUri1").equals("no_image")){

            image = new DescriptionImageShower(previousIntentImage.getStringExtra("imageUri1"));
            descriptionImageShowers.add(image);
        }

        if(!previousIntentImage.getStringExtra("imageUri2").equals("no_image")){

            image = new DescriptionImageShower(previousIntentImage.getStringExtra("imageUri2"));
            descriptionImageShowers.add(image);
        }

        if(!previousIntentImage.getStringExtra("imageUri3").equals("no_image")){

            image = new DescriptionImageShower(previousIntentImage.getStringExtra("imageUri3"));
            descriptionImageShowers.add(image);
        }

        if(!previousIntentImage.getStringExtra("imageUri4").equals("no_image")){

            image = new DescriptionImageShower(previousIntentImage.getStringExtra("imageUri4"));
            descriptionImageShowers.add(image);
        }

        if(!previousIntentImage.getStringExtra("imageUri5").equals("no_image")){

            image = new DescriptionImageShower(previousIntentImage.getStringExtra("imageUri5"));
            descriptionImageShowers.add(image);
        }

        adImage.setAdapter(new DescriptionImageAdapter(descriptionImageShowers));

        adImage.setClipToPadding(false);
        adImage.setClipChildren(false);
        adImage.setOffscreenPageLimit(3);
        adImage.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        /*DescriptionImageShower image1 = new DescriptionImageShower("https://t.auntmia.com/nthumbs/2016-03-08/2284861/2284861_12b.jpg");
        descriptionImageShowers.add(image1);

        DescriptionImageShower image2 = new DescriptionImageShower("https://t.auntmia.com/nthumbs/2015-05-07/2360911/2360911_10b.jpg");
        descriptionImageShowers.add(image2);

        DescriptionImageShower image3 = new DescriptionImageShower("https://t.auntmia.com/nthumbs/2014-01-26/2697314/2697314_19b.jpg");
        descriptionImageShowers.add(image3);*/

        title.setText(previousIntentImage.getStringExtra("title"));
        address.setText(previousIntentImage.getStringExtra("address"));
        flatType.setText(previousIntentImage.getStringExtra("flatType"));
        religion.setText(previousIntentImage.getStringExtra("religion"));
        genre.setText(previousIntentImage.getStringExtra("genre"));
        details.setText(previousIntentImage.getStringExtra("details"));
        electricityBill.setText(previousIntentImage.getStringExtra("electricityBill") +" "+ getString(R.string.electricity_bill));
        waterBill.setText(previousIntentImage.getStringExtra("waterBill") +" "+ getString(R.string.water_bill_));
        gasBill.setText(previousIntentImage.getStringExtra("gasBill") +" "+ getString(R.string.gas_bill_));
        serviceCharge.setText(previousIntentImage.getStringExtra("serviceCharge") +" "+ getString(R.string.service_charge_));
        rent.setText(previousIntentImage.getStringExtra("rent"));

        if(previousIntentImage.getStringExtra("washroom").equals("1")){

            washroom.setText(previousIntentImage.getStringExtra("washroom") +" "+ getString(R.string.washroom));
        } else{

            washroom.setText(previousIntentImage.getStringExtra("washroom") +" "+ getString(R.string.washroom_));
        }

        if(previousIntentImage.getStringExtra("veranda").equals("1")){

            veranda.setText(previousIntentImage.getStringExtra("veranda") +" "+ getString(R.string.veranda));
        } else{

            veranda.setText(previousIntentImage.getStringExtra("veranda") +" "+ getString(R.string.veranda_));
        }

        if(previousIntentImage.getStringExtra("bedroom").equals("1")){

            bedroom.setText(previousIntentImage.getStringExtra("bedroom") +" "+ getString(R.string.bedroom));
        } else{

            bedroom.setText(previousIntentImage.getStringExtra("bedroom") +" "+ getString(R.string.bedroom_));
        }

        switch (previousIntentImage.getStringExtra("floor")) {
            case "1":

                floor.setText(previousIntentImage.getStringExtra("floor") + "st" + " " + getString(R.string.floor_));
                break;
            case "2":

                floor.setText(previousIntentImage.getStringExtra("floor") + "nd" + " " + getString(R.string.floor_));
                break;
            case "3":

                floor.setText(previousIntentImage.getStringExtra("floor") + "rd" + " " + getString(R.string.floor_));
                break;
            default:

                floor.setText(previousIntentImage.getStringExtra("floor") + "th" + " " + getString(R.string.floor_));
                break;
        }

        String[] date = previousIntentImage.getStringExtra("vacantFrom").split("-");
        String month;

        switch (date[1]) {
            case "01":

                month = "January";

                break;
            case "02":

                month = "February";

                break;
            case "03":

                month = "March";

                break;
            case "04":

                month = "April";

                break;
            case "05":

                month = "May";

                break;
            case "06":

                month = "June";

                break;
            case "07":

                month = "July";

                break;
            case "08":

                month = "August";

                break;
            case "09":

                month = "September";

                break;
            case "10":

                month = "October";

                break;
            case "11":

                month = "November";

                break;
            case "12":

                month = "December";

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + date[1]);
        }

        vacFrom.setText(date[0] + " " + month + ", " + date[2]);

        String[] strOfOwnerUid = Objects.requireNonNull(previousIntentImage.getStringExtra("ownerKey")).split("-");

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(strOfOwnerUid[0])).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    /*headerProfileName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    ProfileName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    profilePhoneNo.setText(Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString());*/

                ownerName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                ownerPhoneNo.setText(Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString());

                User u = snapshot.getValue(User.class);

                assert u != null;

                descriptionToChat.setOnClickListener(v -> {

                    //TODO description to chat intent code here

                    Intent intent = new Intent(AdDescriptionActivity.this, ChatActivity.class);
                    intent.putExtra("name", snapshot.child("name").getValue().toString());
                    intent.putExtra("uid", strOfOwnerUid[0]);
                    startActivity(intent);
                });

                Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(ownerProPic);
                //Glide.with(getApplicationContext()).load(u.getProfileImage()).placeholder(R.drawable.user).into(profilePicture);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView checkBoxes = findViewById(R.id.checkbox_value_description);

        List<CheckBoxValueShower> checkBoxValueShowers = new ArrayList<>();

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox1 = new CheckBoxValueShower(getDrawable(R.drawable.ic_lift), "Lift", previousIntentImage.getStringExtra("lift"));
        checkBoxValueShowers.add(checkBox1);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox2 = new CheckBoxValueShower(getDrawable(R.drawable.ic_generator), "Generator", previousIntentImage.getStringExtra("generator"));
        checkBoxValueShowers.add(checkBox2);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox3 = new CheckBoxValueShower(getDrawable(R.drawable.ic_parking), "Parking", previousIntentImage.getStringExtra("parking"));
        checkBoxValueShowers.add(checkBox3);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox4 = new CheckBoxValueShower(getDrawable(R.drawable.ic_security), "Security", previousIntentImage.getStringExtra("security"));
        checkBoxValueShowers.add(checkBox4);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox5 = new CheckBoxValueShower(getDrawable(R.drawable.ic_gas_checkbox), "Gas", previousIntentImage.getStringExtra("gas"));
        checkBoxValueShowers.add(checkBox5);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox6 = new CheckBoxValueShower(getDrawable(R.drawable.ic_wifi), "WiFi", previousIntentImage.getStringExtra("wifi"));
        checkBoxValueShowers.add(checkBox6);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AdDescriptionActivity.this, LinearLayoutManager.HORIZONTAL, false);
        checkBoxes.setLayoutManager(horizontalLayoutManager);
        checkBoxes.setAdapter(new CheckboxValueAdapter(checkBoxValueShowers));
    }

}