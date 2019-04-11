package com.example.traveljournalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationViewHolder> {

    private List<Destination> mDestinations;
    private Context mContext;

    public DestinationAdapter(List<Destination> destinations, Context context) {
        mDestinations = destinations;
        mContext = context;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.destination_item, viewGroup, false);
        return new DestinationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder destinationViewHolder, int i) {
        Destination currentDestination = mDestinations.get(i);
        destinationViewHolder.getTextViewSeason().setText(currentDestination.getSeason());
        destinationViewHolder.getTextViewDestination().setText(currentDestination.getDestination());
        destinationViewHolder.getTextViewPriceAndRating().setText(currentDestination.getPrice() + " / " + currentDestination.getRating());
        Picasso.get().load(currentDestination.getImageLocation()).fit()
                .placeholder(R.drawable.android_3)
                .error(R.drawable.android_3)
                .into(destinationViewHolder.getImageViewDestination());
        if (currentDestination.isFavorite()) {
            destinationViewHolder.getCheckBoxFavorite().setChecked(true);
        } else {
            destinationViewHolder.getCheckBoxFavorite().setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDestinations.size();
    }
}
