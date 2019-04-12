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

    public String getDestination() {
        return mDestination;
    }

    public String getImageLocation() {
        return mImageLocation;
    }

    public String getTripType() {
        return mTripType;
    }

    public float getRating() {
        return mRating;
    }

    public int getPrice() {
        return mPrice;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public String getDatabaseDocumentID() {
        return mDatabaseDocumentID;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }
}
