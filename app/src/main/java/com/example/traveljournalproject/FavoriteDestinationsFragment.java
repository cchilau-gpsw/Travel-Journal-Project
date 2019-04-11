package com.example.traveljournalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavoriteDestinationsFragment extends Fragment {

    private RecyclerView mRecyclerViewDestinations;

    public FavoriteDestinationsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_travel_destinations, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerViewDestinations = view.findViewById(R.id.recycler_view_destinations);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewDestinations.setLayoutManager(layoutManager);
    }
}
