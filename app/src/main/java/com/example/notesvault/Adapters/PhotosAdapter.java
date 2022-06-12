package com.example.notesvault.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notesvault.Models.PhotosDB;
import com.example.notesvault.PhotosClickListner;
import com.example.notesvault.R;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter <PhotosAdapter.MyViewHolder>{

    Context context;
    List<PhotosDB> pic_list;
    PhotosClickListner clickListner;

    public PhotosAdapter(Context context, List<PhotosDB> pic_list, PhotosClickListner clickListner) {
        this.context = context;
        this.pic_list = pic_list;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.iv_hpic.setImageURI(Uri.parse(pic_list.get(position).getPath()));
        PhotosDB pic = pic_list.get(position);
        String picPath = pic.getPath();
        Log.e("Pic", picPath);
//        holder.iv_hpic.setImageURI(Uri.parse(picPath));
        Glide.with(context).load(picPath).into(holder.iv_hpic);

        holder.card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(pic_list.get(holder.getAdapterPosition()));
//                Toast.makeText(context, picPath, Toast.LENGTH_SHORT).show();
                SharedPreferences sh = context.getSharedPreferences("picPath", 0);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("picPath", picPath);
                ed.commit();
            }
        });

        holder.card_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListner.onLongClick(pic_list.get(holder.getAdapterPosition()), holder.card_container);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return pic_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_hpic;
        CardView card_container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_hpic=itemView.findViewById(R.id.iv_hpic);
            card_container=itemView.findViewById(R.id.card_container);
        }
    }

}
