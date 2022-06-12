package com.example.notesvault.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesvault.Models.VideosDB;

@Database(entities = {VideosDB.class}, version = 1)
public abstract class VideosDatabase extends RoomDatabase {
    private static VideosDatabase database;
    private static String DATABASE_NAME = "HideVideos";

    public synchronized static VideosDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), VideosDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract VideosDAO videosDAO();
}
