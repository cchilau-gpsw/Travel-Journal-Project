package com.example.traveljournalproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDestinationsDao {

    @Query("SELECT * FROM favorites")
    List<FavoriteDestination> getAllItems();

    @Insert
    void insertItem(FavoriteDestination favoriteDestination);

    @Query("DELETE FROM favorites")
    void deleteAll();
}
