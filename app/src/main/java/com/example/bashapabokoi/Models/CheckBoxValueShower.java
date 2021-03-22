package com.example.bashapabokoi.Models;

import android.graphics.drawable.Drawable;

public class CheckBoxValueShower {

    public Drawable imageId;
    public String checkBoxName, value;

    public CheckBoxValueShower(Drawable imageId, String checkBoxName, String value) {
        this.imageId = imageId;
        this.checkBoxName = checkBoxName;
        this.value = value;
    }
}
