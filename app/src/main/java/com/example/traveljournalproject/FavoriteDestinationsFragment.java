package com.example.traveljournalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDestinationsFragment extends Fragment {

    private RecyclerView mRecyclerViewDestinations;
    private List<Destination> mFavoriteDestinations;

    public FavoriteDestinationsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        new LoadFavoriteDestinationsAsync().execute();
        View view = inflater.inflate(R.layout.fragment_travel_destinations, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mFavoriteDestinations = new ArrayList<>();

        for(FavoriteDestination item: DatabaseInitializer.getTripList()){
            mFavoriteDestinations.add(new Destination(item.getTripName(), item.getLocation(), item.getImageLocation(), null, item.getRating(),
                    item.getPrice(), null, null, true, null));
        }

        mRecyclerViewDestinations = view.findViewById(R.id.recycler_view_destinations);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewDestinations.setLayoutManager(layoutManager);
        DestinationAdapter destinationAdapter = new DestinationAdapter(mFavoriteDestinations, getActivity());
        mRecyclerViewDestinations.setAdapter(destinationAdapter);




    }

//    private class LoadFavoriteDestinationsAsync extends AsyncTask<Void, Void, List<FavoriteDestination>> {
//
//        @Override
//        protected List<FavoriteDestination> doInBackground(Void... voids) {
//            FavoriteDestinationsRoomDatabase database = FavoriteDestinationsRoomDatabase.getDatabase(getActivity());
//            return database.itemDao().getAllItems();
//        }
//
//        @Override
//        protected void onPostExecute(List<FavoriteDestination> items) {
//            super.onPostExecute(items);
//            for (FavoriteDestination item : items) {
//                mFavoriteDestinations.add(new Destination(item.getTripName(), item.getLocation(), item.getImageLocation(), null, item.getRating(),
//                        item.getPrice(), null, null, true, null));
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            FavoriteDestinationsRoomDatabase.getDatabase(getActivity());
//        }
//    }

}
