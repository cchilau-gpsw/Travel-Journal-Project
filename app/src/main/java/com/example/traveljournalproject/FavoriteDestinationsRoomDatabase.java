package com.example.traveljournalproject;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

@Database(entities = {FavoriteDestination.class}, version = 1)
public abstract class FavoriteDestinationsRoomDatabase extends RoomDatabase {

    public abstract FavoriteDestinationsDao itemDao();

    private static FavoriteDestinationsRoomDatabase INSTANCE;

    static FavoriteDestinationsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteDestinationsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDestinationsRoomDatabase.class, "item_database_v2")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
//            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more items, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FavoriteDestinationsDao mDao;
//        String [] items = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(FavoriteDestinationsRoomDatabase db) {
            mDao = db.itemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //mDao.deleteAll();

//            String currentUserID = FirebaseAuth.getInstance().getUid();
//            FirebaseFirestore.getInstance().collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if (document.getBoolean("isFavorite") == true) {
//
//                                        FavoriteDestination favoriteDestination = document.toObject(FavoriteDestination.class);
//
//                                        mDao.insertItem(new FavoriteDestination(document.getString("season"), document.getString("location"),
//                                                document.getString("imageLocation"), document.getLong("price").intValue(),
//                                                (float)document.getLong("rating")));
//                                        Log.e("**********", "Added item for document id" + document.getId());
//                                    }
//                                }
//                            }
//                        }
//                    });



            return null;
        }
    }
}