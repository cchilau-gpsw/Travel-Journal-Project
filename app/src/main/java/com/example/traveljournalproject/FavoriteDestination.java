package com.example.traveljournalproject;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteDestination {

//    public FavoriteDestination(int id) {
//        mId = id;
//    }

    public FavoriteDestination(String tripName, String location, String imageLocation, int price, float rating) {
        mTripName = tripName;
        mLocation = location;
        mImageLocation = imageLocation;
        mPrice = price;
        mRating = rating;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "tripName")
    private String mTripName;

    @ColumnInfo(name = "location")
    private String mLocation;

    @ColumnInfo(name = "imageLocation")
    private String mImageLocation;

    @ColumnInfo(name = "price")
    private int mPrice;

    @ColumnInfo(name = "rating")
    private float mRating;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTripName() {
        return mTripName;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getImageLocation() {
        return mImageLocation;
    }

    public int getPrice() {
        return mPrice;
    }

    public float getRating() {
        return mRating;
    }
}
