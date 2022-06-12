package com.example.notesvault.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesvault.Models.PhotosDB;

@Database(entities = {PhotosDB.class}, version = 1)
public abstract class PhotoDatabase extends RoomDatabase {
    private static PhotoDatabase database;
    private static String DATABASE_NAME = "HidePhots";

    public synchronized static PhotoDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), PhotoDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract PhotosDAO photosDAO();
}
