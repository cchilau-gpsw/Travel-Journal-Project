package com.example.traveljournalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelDestinationsFragment extends Fragment {
    public static final String TRIP_NAME = "trip name";
    public static final String DESTINATION = "destination";
    public static final String TRIP_TYPE = "trip type";
    public static final String RATING = "rating";
    public static final String PRICE = "price";
    public static final String START_DATE = "start date";
    public static final String END_DATE = "end date";
    public static final String DATABASE_DOCUMENT_ID = "document id";
    public static final String DESTINATIONS_COLLECTION = "destinations";

    private RecyclerView mRecyclerViewDestinations;

    public TravelDestinationsFragment() {
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


        final List<Destination> destinations = new ArrayList<>();
        final String currentUserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection(DESTINATIONS_COLLECTION + "_" + currentUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                destinations.add(new Destination(document.getString("season"), document.getString("location"), document.getString("imageLocation"),
                                        document.getString("tripType"), (float) (document.getLong("rating")), document.getLong("price").intValue(),
                                        document.getDate("startDate"), document.getDate("endDate"), document.getBoolean("isFavorite"), document.getId()));

                                final DestinationAdapter destinationAdapter = new DestinationAdapter(destinations, getActivity());
                                mRecyclerViewDestinations.setAdapter(destinationAdapter);
                                mRecyclerViewDestinations.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerViewDestinations, new ClickListener() {
                                    @Override
                                    public void onClick(View view, final int position) {
                                        final CheckBox checkbox = view.findViewById(R.id.check_box_bookmark);
                                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (checkbox.isChecked() == true) {
                                                    FirebaseFirestore.getInstance().collection(DESTINATIONS_COLLECTION + "_" + currentUserID)
                                                            .document(destinations.get(position).getDatabaseDocumentID())
                                                            .update("isFavorite", true);
                                                } else {
                                                    FirebaseFirestore.getInstance().collection(DESTINATIONS_COLLECTION + "_" + currentUserID)
                                                            .document(destinations.get(position).getDatabaseDocumentID())
                                                            .update("isFavorite", false);
                                                }
                                            }
                                        });
                                        DatabaseInitializer.populateAsync(FavoriteDestinationsRoomDatabase.getDatabase(getActivity()));
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ManageTrip.class);
                                        intent.putExtra(TRIP_NAME, destinations.get(position).getSeason());
                                        intent.putExtra(DESTINATION, destinations.get(position).getDestination());
                                        intent.putExtra(TRIP_TYPE, destinations.get(position).getTripType());
                                        intent.putExtra(RATING, destinations.get(position).getRating());
                                        intent.putExtra(PRICE, destinations.get(position).getPrice());
                                        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        if (destinations.get(position).getStartDate() != null) {
                                            intent.putExtra(START_DATE, formatter.format(destinations.get(position).getStartDate()));
                                        }
                                        if (destinations.get(position).getEndDate() != null) {
                                            intent.putExtra(END_DATE, formatter.format(destinations.get(position).getEndDate()));
                                        }
                                        intent.putExtra(DATABASE_DOCUMENT_ID, destinations.get(position).getDatabaseDocumentID());
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
}
