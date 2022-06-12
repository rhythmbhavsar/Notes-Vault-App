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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesvault.Adapters.PhotosAdapter;
import com.example.notesvault.Database.PhotoDatabase;
import com.example.notesvault.Database.RoomDB;
import com.example.notesvault.Models.Notes;
import com.example.notesvault.Models.PhotosDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Photos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView rv_pic;
    FloatingActionButton fab_add;
    List<PhotosDB> photosdb = new ArrayList<>();
    PhotoDatabase database;
    PhotosAdapter pa;
    PhotosDB selectedPhotos;
    LinearLayoutManager llm;
    GridLayoutManager glm;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        rv_pic= findViewById(R.id.rv_pic);
        fab_add= findViewById(R.id.fab_add);

        database = PhotoDatabase.getInstance(this);
        photosdb = database.photosDAO().getAll();

//        updateRecycler(photosdb);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        glm = new GridLayoutManager(getApplicationContext(), 2);
        rv_pic.setLayoutManager(glm);
        getTasks();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }



    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            String mri = imageUri.toString();

            String imagepath = getRealPathFromURI(imageUri);

            String result = imageUri.getPath().toString();
            Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
            Log.d("in_activity", "File Name : " + imagepath);

//            File from = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+imagepath);
//            File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/NotesVault/"+result);
////            from.renameTo(to);

//            Log.e("file", from.toString());
//            Log.e("file", to.toString());




            InputStream in = null;
            OutputStream out = null;
            try {

                //create output directory if it doesn't exist
                String outputPath = "/NotesVault/";
                File dir = new File (outputPath);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }


                in = new FileInputStream(imagepath);
                out = new FileOutputStream(outputPath + result);

                Log.e("file", imagepath);
                Log.e("file", out.toString());

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                // delete the original file
                new File(imagepath).delete();


            }

            catch (FileNotFoundException fnfe1) {
                Log.e("tag", fnfe1.getMessage());
            }
            catch (Exception e) {
                Log.e("tag", e.getMessage());
            }






            PhotosDB pdb = new PhotosDB();
            pdb.setName(result);
            pdb.setPath(imagepath);

            PhotoDatabase.getInstance(getApplicationContext()).photosDAO().insert(pdb);

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

//    private void updateRecycler(List<PhotosDB> photosdb) {
//        photosdb = PhotoDatabase.getInstance(getApplicationContext()).photosDAO().getAll();
//        GridLayoutManager glm = new GridLayoutManager(this, 2);
//        pa = new PhotosAdapter(this, photosdb);
//        rv_pic.setAdapter(pa);
//        rv_pic.setLayoutManager(glm);
//    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<PhotosDB>> {

            @Override
            protected List<PhotosDB> doInBackground(Void... voids) {
                List<PhotosDB> taskList = PhotoDatabase
                        .getInstance(getApplicationContext()).photosDAO().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<PhotosDB> pidb) {
                super.onPostExecute(pidb);
                pa = new PhotosAdapter(Photos.this, pidb, photolistner);
                rv_pic.setAdapter(pa);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private final PhotosClickListner photolistner = new PhotosClickListner() {
        @Override
        public void onClick(PhotosDB photosDB) {
            Intent i = new Intent(Photos.this, DisplayPhoto.class);
            startActivity(i);
        }

        @Override
        public void onLongClick(PhotosDB photosDB, CardView cardView) {
            selectedPhotos = new PhotosDB();
            selectedPhotos = photosDB;
            showPopup(cardView);
        }
    };

//    private final PhotosClickListner photosListner = new PhotosClickListner() {
//        @Override
//        public void onClick(Notes notes) {
//            Intent i = new Intent(Photos.this, NotesTakeActivity.class);
//            i.putExtra("old_note", notes);
//            startActivityForResult(i, 102);
//        }
//
//        @Override
//        public void onLongClick(Notes notes, CardView cardView) {
//            selectedNote = new Notes();
//            selectedNote = notes;
//            showPopup(cardView);
//        }
//    };

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
                database.photosDAO().delete(selectedPhotos);
                photosdb.remove(selectedPhotos);
                pa.notifyDataSetChanged();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}