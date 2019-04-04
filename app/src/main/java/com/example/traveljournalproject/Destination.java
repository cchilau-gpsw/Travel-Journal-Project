package com.example.traveljournalproject;

public class Destination {
    private String mSeason;
    private String mDestination;
    private String mImageLocation;
    private String mTripType;
    private float mRating;
    private int mPrice;

    public Destination(String season, String destination, String imageLocation, String tripType, float rating, int price) {
        mSeason = season;
        mDestination = destination;
        mImageLocation = imageLocation;
        mTripType = tripType;
        mRating = rating;
        mPrice = price;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season) {
        mSeason = season;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getImageLocation() {
        return mImageLocation;
    }

    public void setImageLocation(String imageLocation) {
        mImageLocation = imageLocation;
    }

    public String getTripType() {
        return mTripType;
    }

    public void setTripType(String tripType) {
        mTripType = tripType;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }
}
