package com.example.notesvault;

import androidx.cardview.widget.CardView;

import com.example.notesvault.Models.PhotosDB;

public interface PhotosClickListner {
    void onClick(PhotosDB photosDB);
    void onLongClick(PhotosDB photosDB, CardView cardView);
}
