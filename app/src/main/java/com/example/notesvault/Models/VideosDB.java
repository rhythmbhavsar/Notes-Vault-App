package com.example.notesvault.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "VideosDB")
public class VideosDB implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @PrimaryKey(autoGenerate = true)
    int id = 0;

    @ColumnInfo(name = "name")
    String name = "";

    @ColumnInfo(name = "path")
    String path = "";

}
