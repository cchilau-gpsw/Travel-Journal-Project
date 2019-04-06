package com.example.traveljournalproject;

import android.view.View;

public interface DateChangedListener {

    void onStartDateChanged(int year, int month, int day);
    void onEndDateChanged(int year, int month, int day);
}
