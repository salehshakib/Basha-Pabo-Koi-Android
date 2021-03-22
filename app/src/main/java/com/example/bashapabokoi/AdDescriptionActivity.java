package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bashapabokoi.Adapters.CheckboxValueAdapter;
import com.example.bashapabokoi.Adapters.DescriptionImageAdapter;
import com.example.bashapabokoi.Models.CheckBoxValueShower;
import com.example.bashapabokoi.Models.DescriptionImageShower;

import java.util.ArrayList;
import java.util.List;

public class AdDescriptionActivity extends AppCompatActivity {

    DescriptionImageShower image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_description);

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

        RecyclerView checkBoxes = findViewById(R.id.checkbox_value_description);

        List<CheckBoxValueShower> checkBoxValueShowers = new ArrayList<>();

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox1 = new CheckBoxValueShower(getDrawable(R.drawable.ic_lift), "Lift", "True");
        checkBoxValueShowers.add(checkBox1);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox2 = new CheckBoxValueShower(getDrawable(R.drawable.ic_generator), "Generator", "False");
        checkBoxValueShowers.add(checkBox2);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox3 = new CheckBoxValueShower(getDrawable(R.drawable.ic_parking), "Parking", "True");
        checkBoxValueShowers.add(checkBox3);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox4 = new CheckBoxValueShower(getDrawable(R.drawable.ic_security), "Security", "False");
        checkBoxValueShowers.add(checkBox4);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox5 = new CheckBoxValueShower(getDrawable(R.drawable.ic_gas_checkbox), "Gas", "True");
        checkBoxValueShowers.add(checkBox5);

        @SuppressLint("UseCompatLoadingForDrawables")
        CheckBoxValueShower checkBox6 = new CheckBoxValueShower(getDrawable(R.drawable.ic_wifi), "WiFi", "False");
        checkBoxValueShowers.add(checkBox6);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AdDescriptionActivity.this, LinearLayoutManager.HORIZONTAL, false);
        checkBoxes.setLayoutManager(horizontalLayoutManager);
        checkBoxes.setAdapter(new CheckboxValueAdapter(checkBoxValueShowers));
    }

}