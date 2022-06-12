package com.example.notesvault;

import androidx.cardview.widget.CardView;

import com.example.notesvault.Models.VideosDB;

public interface VideosClickListner {
    void onClick(VideosDB videosDB);
    void onLongClick(VideosDB videosDB, CardView cardView);
}
