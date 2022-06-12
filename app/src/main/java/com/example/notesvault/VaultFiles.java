package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VaultFiles extends AppCompatActivity {

    CardView  cv_pic, cv_ved, cv_aud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_files);
        cv_pic = findViewById(R.id.cv_pic);
        cv_ved = findViewById(R.id.cv_ved);
//        cv_aud = findViewById(R.id.cv_aud);

        cv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultFiles.this, Photos.class);
                startActivity(i);
            }
        });

        cv_ved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultFiles.this, Videos.class);
                startActivity(i);
            }
        });
    }
}