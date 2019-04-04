package com.example.traveljournalproject;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Destination {
    private String mSeason;
    private String mDestination;
    private String mImageLocation;
    private String mTripType;
    private float mRating;

    public Destination(String season, String destination, String imageLocation, String tripType, float rating) {
        mSeason = season;
        mDestination = destination;
        mImageLocation = imageLocation;
        mTripType = tripType;
        mRating = rating;
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
}
