package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class SafeGallery extends AppCompatActivity {

    ImageView iv_img, iv_upload;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    VideoView vv_ved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_gallery);
        iv_img = findViewById(R.id.iv_img);
        iv_upload = findViewById(R.id.iv_upload);
//        vv_ved = findViewById(R.id.vv_ved);

        iv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            String videopath = getRealPathFromURI(imageUri);
//            String v_path =imageUri.getPath();
            String mri = imageUri.toString();

            iv_img.setImageURI(Uri.parse(mri));

            String result = imageUri.getPath();
            Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
            Log.d("in_activity", "File Name : " + result);

//            vv_ved.setVideoPath(videopath);
//            Log.e("in_activity","Path is " + proj);
//            vv_ved.start();
//            vv_ved.seekTo(100);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}