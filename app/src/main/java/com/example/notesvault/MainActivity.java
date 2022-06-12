package com.example.notesvault;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesvault.Adapters.NotesListAdapter;
import com.example.notesvault.Database.RoomDB;
import com.example.notesvault.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView rv_home;
    NotesListAdapter nla;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
//    SearchView sv_home;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_home = findViewById(R.id.rv_home);
        fab_add = findViewById(R.id.fab_add);
//        sv_home = findViewById(R.id.sv_home);

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecycler(notes);

        SharedPreferences sh = getSharedPreferences("Passcode",MODE_PRIVATE);
        Boolean isSet = sh.getBoolean("isSet", false);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NotesTakeActivity.class);
                startActivityForResult(i, 101);
            }
        });

        fab_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isSet){
                    Intent i = new Intent(MainActivity.this, Passcode.class);
                    startActivityForResult(i, 103);
                }else {
                    Intent i = new Intent(MainActivity.this, SetPassword.class);
                    startActivity(i);
                }

                return true;
            }
        });

//        sv_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes){
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        nla.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (resultCode == Activity.RESULT_OK){

                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                nla.notifyDataSetChanged();
            }
        }
        else if(requestCode == 102){
            if (resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getId(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                nla.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        rv_home.setHasFixedSize(true);
        rv_home.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        nla = new NotesListAdapter(MainActivity.this, notes, notesClickListner);
        rv_home.setAdapter(nla);
    }

    private final NotesClickListner notesClickListner = new NotesClickListner() {
        @Override
        public void onClick(Notes notes) {
            Intent i = new Intent(MainActivity.this, NotesTakeActivity.class);
            i.putExtra("old_note", notes);
            startActivityForResult(i, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        Boolean isPinned = notes.get(selectedNote.getId()).getPinned();
        switch (item.getItemId()){
            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                nla.notifyDataSetChanged();
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}