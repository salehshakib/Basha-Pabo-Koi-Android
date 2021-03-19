package com.example.bashapabokoi;

public class OwnerAdShower {

    public String rent, location, flatType, genre, imageUri;
    public float starRating;

    public OwnerAdShower(String rent, String location, String flatType, String genre, String imageUri, float starRating) {
        this.rent = rent;
        this.location = location;
        this.flatType = flatType;
        this.genre = genre;
        this.imageUri = imageUri;
        this.starRating = starRating;
    }
}
