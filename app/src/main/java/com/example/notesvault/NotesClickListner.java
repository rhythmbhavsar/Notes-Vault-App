package com.example.notesvault;

import androidx.cardview.widget.CardView;

import com.example.notesvault.Models.Notes;

public interface NotesClickListner {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
