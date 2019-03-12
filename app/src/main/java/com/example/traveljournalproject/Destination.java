package com.example.traveljournalproject;

public class Destination {
    private String mSeason;
    private String mDestination;
    private String mImageLocation;

    public Destination(String season, String destination, String imageLocation) {
        mSeason = season;
        mDestination = destination;
        mImageLocation = imageLocation;
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
}
