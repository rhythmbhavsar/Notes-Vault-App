package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DisplayPhoto extends AppCompatActivity {

    ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);
        pic  = findViewById(R.id.pic);

        SharedPreferences sh = getSharedPreferences("picPath", 0);
        String picPath = sh.getString("picPath", "");

        Glide.with(DisplayPhoto.this).load(picPath).into(pic);
    }
}