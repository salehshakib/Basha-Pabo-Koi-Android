package com.example.bashapabokoi.Models;

public class CreateAd {

    private String title, address, thana, vacFrom, flatType, washroom, veranda, bedroom, floor, religion, genre, currentBill, waterBill, gasBill, otherCharges, lift, generator, parking, security, gas, wifi, description, rent, imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5;

    private String longitude, latitude;
    private String key;

    public CreateAd(String key, String title, String address, String thana, String vacFrom, String flatType, String washroom, String veranda, String bedroom, String floor, String religion, String genre, String currentBill, String waterBill, String gasBill, String otherCharges, String lift, String generator, String parking, String security, String gas, String wifi, String description, String rent, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String imageUrl5, String longitude, String latitude) {
        this.key = key;
        this.title = title;
        this.address = address;
        this.thana = thana;
        this.vacFrom = vacFrom;
        this.flatType = flatType;
        this.washroom = washroom;
        this.veranda = veranda;
        this.bedroom = bedroom;
        this.floor = floor;
        this.religion = religion;
        this.genre = genre;
        this.currentBill = currentBill;
        this.waterBill = waterBill;
        this.gasBill = gasBill;
        this.otherCharges = otherCharges;
        this.lift = lift;
        this.generator = generator;
        this.parking = parking;
        this.security = security;
        this.gas = gas;
        this.wifi = wifi;
        this.description = description;
        this.rent = rent;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.imageUrl5 = imageUrl5;
        this.latitude = latitude;
        this.longitude = longitude;


    }

    public CreateAd() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getVacFrom() {
        return vacFrom;
    }

    public void setVacFrom(String vacFrom) {
        this.vacFrom = vacFrom;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getWashroom() {
        return washroom;
    }

    public void setWashroom(String washroom) {
        this.washroom = washroom;
    }

    public String getVeranda() {
        return veranda;
    }

    public void setVeranda(String veranda) {
        this.veranda = veranda;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCurrentBill() {
        return currentBill;
    }

    public void setCurrentBill(String currentBill) {
        this.currentBill = currentBill;
    }

    public String getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(String waterBill) {
        this.waterBill = waterBill;
    }

    public String getGasBill() {
        return gasBill;
    }

    public void setGasBill(String gasBill) {
        this.gasBill = gasBill;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        this.otherCharges = otherCharges;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) { this.imageUrl3 = imageUrl3; }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public String getImageUrl5() {
        return imageUrl5;
    }

    public void setImageUrl5(String imageUrl5) {
        this.imageUrl5 = imageUrl5;
    }
}
