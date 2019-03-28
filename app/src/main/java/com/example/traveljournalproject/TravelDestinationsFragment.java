package com.example.traveljournalproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelDestinationsFragment extends Fragment {
    public static final String TRIP_NAME = "trip name";
    public static final String DESTINATION = "destination";
    private static final String DESTINATIONS_COLLECTION = "destinations";

    public RecyclerView getRecyclerViewDestinations() {
        return mRecyclerViewDestinations;
    }

    private RecyclerView mRecyclerViewDestinations;

    public TravelDestinationsFragment() { }

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


        final List<Destination> destinations = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(DESTINATIONS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                destinations.add(new Destination(document.getString("season"), document.getString("location"), document.getString("imageLocation")));

                                DestinationAdapter destinationAdapter = new DestinationAdapter(destinations, getActivity());
                                mRecyclerViewDestinations.setAdapter(destinationAdapter);
                                mRecyclerViewDestinations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ManageTrip.class);
                                        intent.putExtra(TRIP_NAME, destinations.get(position).getSeason());
                                        intent.putExtra(DESTINATION, destinations.get(position).getDestination());
                                        startActivity(intent);
                                    }
                                }));
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error getting data from database", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void addNewDestinationOnClick(View view) {
        Intent intent = new Intent(getActivity(), ManageTrip.class);
        startActivity(intent);
    }

//    public void addDestination(String season, String location, String imageLocation) {
//        List<Destination> currentDestinationList = getDestinations();
//        currentDestinationList.add(new Destination(season, location, imageLocation));
//    }


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
