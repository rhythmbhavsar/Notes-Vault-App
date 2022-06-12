package com.example.notesvault.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.notesvault.Models.VideosDB;

import java.util.List;

@Dao
public interface VideosDAO {
    @Insert
    void insert(VideosDB videosDB);

    @Query("SELECT * FROM VideosDB")
    List<VideosDB> getAll();

    @Delete
    void delete(VideosDB videosDB);
}
