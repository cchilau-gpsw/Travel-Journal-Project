package com.example.traveljournalproject;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    private static List<FavoriteDestination> sTripList;

    public static void addTrip(final FavoriteDestinationsRoomDatabase db, FavoriteDestination trip) {
        db.itemDao().insertItem(trip);
    }

    public static List<FavoriteDestination> getTripList() {
        return sTripList;
    }

    public static void populateAsync(@NonNull final FavoriteDestinationsRoomDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, List<FavoriteDestination>> {

        private final FavoriteDestinationsRoomDatabase mDb;

        @Override
        protected List<FavoriteDestination> doInBackground(Void... voids) {

            return mDb.itemDao().getAllItems();
        }

        @Override
        protected void onPostExecute(List<FavoriteDestination> items) {
            super.onPostExecute(items);
            sTripList = items;
        }

        PopulateDbAsync(FavoriteDestinationsRoomDatabase db) {
            mDb = db;
        }
    }
}