package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayVideo extends AppCompatActivity {

    VideoView vv_play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        vv_play = findViewById(R.id.vv_play);


        SharedPreferences sh = getSharedPreferences("vedPath", 0);
        String vedPath = sh.getString("vedPath", "");

//        Toast.makeText(this,  vedPath, Toast.LENGTH_SHORT).show();

//        Uri uri = Uri.parse(vedPath);
//        vv_play.setVideoURI(uri);

        vv_play.setVideoPath(vedPath);

        MediaController mediaController = new MediaController(this);

        // sets the anchor view
        // anchor view for the videoView
        mediaController.setAnchorView(vv_play);

        // sets the media player to the videoView
        mediaController.setMediaPlayer(vv_play);

        // sets the media controller to the videoView
        vv_play.setMediaController(mediaController);

        vv_play.start();
    }
}