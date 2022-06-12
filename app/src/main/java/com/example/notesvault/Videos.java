package com.example.notesvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesvault.Adapters.PhotosAdapter;
import com.example.notesvault.Adapters.VideosAdapter;
import com.example.notesvault.Database.PhotoDatabase;
import com.example.notesvault.Database.VideosDatabase;
import com.example.notesvault.Models.PhotosDB;
import com.example.notesvault.Models.VideosDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Videos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView rv_ved;
    FloatingActionButton fab_add;
    List<VideosDB> videosdb = new ArrayList<>();
    VideosDatabase database;
    VideosAdapter va;
    VideosDB selectedVed;
    LinearLayoutManager llm;
    GridLayoutManager glm;

    private static final int PICK_IMAGE = 100;
    Uri vedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        rv_ved= findViewById(R.id.rv_ved);
        fab_add= findViewById(R.id.fab_add);

        database = VideosDatabase.getInstance(this);
        videosdb = database.videosDAO().getAll();
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        glm = new GridLayoutManager(getApplicationContext(), 2);
        rv_ved.setLayoutManager(glm);
        getTasks();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
                gallery.setType("video/*");
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            vedUri = data.getData();
            String mri = vedUri.toString();

            String imagepath = getRealPathFromURI(vedUri);

            String result = vedUri.getPath().toString();
            Cursor cursor = getContentResolver().query(vedUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
            Log.d("in_activity", "File Name : " + imagepath);


            VideosDB pdb = new VideosDB();
            pdb.setName(result);
            pdb.setPath(imagepath);

            VideosDatabase.getInstance(getApplicationContext()).videosDAO().insert(pdb);

            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
            getTasks();


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



    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<VideosDB>> {

            @Override
            protected List<VideosDB> doInBackground(Void... voids) {
                List<VideosDB> taskList = VideosDatabase
                        .getInstance(getApplicationContext()).videosDAO().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<VideosDB> vidb) {
                super.onPostExecute(vidb);
                va = new VideosAdapter(Videos.this, vidb,videosClickListner);
                rv_ved.setAdapter(va);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private final VideosClickListner videosClickListner = new VideosClickListner() {
        @Override
        public void onClick(VideosDB videosDB) {
            Intent i = new Intent(Videos.this, PlayVideo.class);
            startActivity(i);
        }

        @Override
        public void onLongClick(VideosDB videosDB, CardView cardView) {
            selectedVed = new VideosDB();
            selectedVed = videosDB;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popupMenu.inflate(R.menu.popup_menu2);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //        Toast.makeText(this, "Thay che", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.unhide:
                database.videosDAO().delete(selectedVed);
                videosdb.remove(selectedVed);
                va.notifyDataSetChanged();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}