package com.example.notesvault.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesvault.Models.Notes;
import com.example.notesvault.NotesClickListner;
import com.example.notesvault.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter <NotesListAdapter.NotesViewHolder> {

    Context context;
    List<Notes> list;
    NotesClickListner listner;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public NotesListAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false);
        NotesViewHolder vh = new NotesViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.NotesViewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_title.setSelected(true);

        holder.tv_notes.setText(list.get(position).getNotes());
        holder.tv_date.setText(list.get(position).getDate());
        holder.tv_date.setSelected(true);

        Boolean isPinned = list.get(position).getPinned();
        if (isPinned){
            holder.iv_pin.setImageResource(R.drawable.ic_pin);
        }else {
            holder.iv_pin.setImageResource(0);
        }

        int color_code = getRandomColor();
//        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listner.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.c1);
        colorCode.add(R.color.c2);
        colorCode.add(R.color.c3);
        colorCode.add(R.color.c4);
        colorCode.add(R.color.c5);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    class NotesViewHolder extends RecyclerView.ViewHolder {

        CardView notes_container;
        TextView tv_title, tv_notes, tv_date;
        ImageView iv_pin;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_container = itemView.findViewById(R.id.notes_container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_notes = itemView.findViewById(R.id.tv_notes);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_pin = itemView.findViewById(R.id.iv_pin);
        }
    }
}