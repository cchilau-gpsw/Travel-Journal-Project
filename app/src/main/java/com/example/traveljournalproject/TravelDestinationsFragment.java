package com.example.traveljournalproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelDestinationsFragment extends Fragment {

    public static final String TRIP_NAME = "trip name";
    public static final String DESTINATION = "destination";

    public RecyclerView getRecyclerViewDestinations() {
        return mRecyclerViewDestinations;
    }

    private RecyclerView mRecyclerViewDestinations;


    public TravelDestinationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_destinations, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerViewDestinations = view.findViewById(R.id.recycler_view_destinations);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewDestinations.setLayoutManager(layoutManager);
        DestinationAdapter destinationAdapter = new DestinationAdapter(getDestinations(), getActivity());
        mRecyclerViewDestinations.setAdapter(destinationAdapter);

        mRecyclerViewDestinations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ManageTrip.class);
                intent.putExtra(TRIP_NAME, getDestinations().get(position).getSeason());
                intent.putExtra(DESTINATION, getDestinations().get(position).getDestination());
                startActivity(intent);
            }
        }));
    }

    private List<Destination> getDestinations() {
        List<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Holiday 2017", "Islands", "https://upload.wikimedia.org/wikipedia/commons/5/58/Fernando_noronha.jpg"));
        destinations.add(new Destination("Fall 2017", "Rome", "https://cdn.fodors.com/wp-content/uploads/2018/10/2_UltimateRome_TheColosseum.jpg"));
        destinations.add(new Destination("Summer 2017", "London", "https://london.ac.uk/sites/default/files/styles/promo_large/public/2018-10/london-aerial-cityscape-river-thames_1.jpg?itok=BMaDUhjp"));
        destinations.add(new Destination("Winter 2017", "Paris", "https://www.triumemba.org/content/uploads/2018/09/pexels-photo-338515.jpg"));
        destinations.add(new Destination("Spring 2018", "San Francisco", "https://cdn.britannica.com/s:700x450/85/155085-004-E7605258.jpg"));
        destinations.add(new Destination("Summer 2018", "Bucharest", "https://cdn.tourradar.com/s3/tour/750x400/99500_630b34a9.jpg"));
        return destinations;
    }

    public void addNewDestinationOnClick(View view) {
        Intent intent = new Intent(getActivity(), ManageTrip.class);
        startActivity(intent);
    }

    public void addDestination(String season, String location, String imageLocation) {
        List<Destination> currentDestinationList = getDestinations();
        currentDestinationList.add(new Destination(season, location, imageLocation));
    }


    public void onClick(View view) {

        mRecyclerViewDestinations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ManageTrip.class);
//                intent.putExtra(TRIP_NAME, getDestinations().get(position).getSeason());
//                intent.putExtra(DESTINATION, getDestinations().get(position).getDestination());
                startActivity(intent);
            }
        }));
    }

}
