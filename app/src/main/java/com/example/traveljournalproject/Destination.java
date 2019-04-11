package com.example.traveljournalproject;

import java.util.Date;

public class Destination {
    private String mSeason;
    private String mDestination;
    private String mImageLocation;
    private String mTripType;
    private float mRating;
    private int mPrice;
    private Date mStartDate;
    private Date mEndDate;
    private boolean mIsFavorite;
    private String mDatabaseDocumentID;


    public Destination(String season, String destination, String imageLocation, String tripType, float rating, int price, Date startDate, Date endDate, boolean isFavorite, String databaseDocumentID) {
        mSeason = season;
        mDestination = destination;
        mImageLocation = imageLocation;
        mTripType = tripType;
        mRating = rating;
        mPrice = price;
        mStartDate = startDate;
        mEndDate = endDate;
        mIsFavorite = isFavorite;
        mDatabaseDocumentID = databaseDocumentID;
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

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public String getDatabaseDocumentID() {
        return mDatabaseDocumentID;
    }

    public void setDatabaseDocumentID(String databaseDocumentID) {
        mDatabaseDocumentID = databaseDocumentID;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }
}
