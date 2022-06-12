package com.example.notesvault.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notesvault.Models.PhotosDB;

import java.util.List;

@Dao
public interface PhotosDAO {

    @Insert
    void insert(PhotosDB phtosdb);

    @Query("SELECT * FROM PhotosDB")
    List<PhotosDB> getAll();

    @Delete
    void delete(PhotosDB photosdb);
}
