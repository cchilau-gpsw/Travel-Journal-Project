package com.example.traveljournalproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DestinationViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageViewDestination;
    private TextView mTextViewSeason;
    private TextView mTextViewDestination;
    private TextView mTextViewPriceAndRating;


    public DestinationViewHolder(@NonNull View itemView) {
        super(itemView);

        mImageViewDestination = itemView.findViewById(R.id.image_view_destination);
        mTextViewSeason = itemView.findViewById(R.id.text_view_season);
        mTextViewDestination = itemView.findViewById(R.id.text_view_destination);
        mTextViewPriceAndRating = itemView.findViewById(R.id.text_view_price_rating);
    }

    public ImageView getImageViewDestination() {
        return mImageViewDestination;
    }

    public void setImageViewDestination(ImageView imageViewDestination) {
        mImageViewDestination = imageViewDestination;
    }

    public TextView getTextViewSeason() {
        return mTextViewSeason;
    }

    public void setTextViewSeason(TextView textViewSeason) {
        mTextViewSeason = textViewSeason;
    }

    public TextView getTextViewDestination() {
        return mTextViewDestination;
    }

    public void setTextViewDestination(TextView textViewDestination) {
        mTextViewDestination = textViewDestination;
    }

    public TextView getTextViewPriceAndRating() {
        return mTextViewPriceAndRating;
    }

    public void setTextViewPriceAndRating(TextView textViewPriceAndRating) {
        mTextViewPriceAndRating = textViewPriceAndRating;
    }
}
