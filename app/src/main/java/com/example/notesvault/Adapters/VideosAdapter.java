package com.example.notesvault.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notesvault.Models.PhotosDB;
import com.example.notesvault.Models.VideosDB;
import com.example.notesvault.R;
import com.example.notesvault.VideosClickListner;

import java.io.File;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter <VideosAdapter.MyViewHolder>{

    Context context;
    List<VideosDB> ved_list;
    VideosClickListner clickListner;

    public VideosAdapter(Context context, List<VideosDB> ved_list, VideosClickListner clickListner) {
        this.context = context;
        this.ved_list = ved_list;
        this.clickListner = clickListner;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ved_card, parent, false);
        VideosAdapter.MyViewHolder vh = new VideosAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideosDB ved = ved_list.get(position);
        String vedPath = ved.getPath();
//        holder.iv_hved.setVideoPath(vedPath);

        Glide.with(context)
                .asBitmap()
                .load(vedPath) // or URI/path
                .into(holder.iv_hved); //imageview to set thumbnail to

        Log.e("Ved", vedPath);
//        holder.iv_hved.seekTo(100);
//        Glide.with(context).load(vedPath).into(holder.iv_hved);

        String vPath = vedPath.toString();
        holder.ved_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(ved_list.get(holder.getAdapterPosition()));

               SharedPreferences sh = context.getSharedPreferences("vedPath", 0);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("vedPath", vedPath);
                ed.commit();

            }
        });

        holder.ved_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListner.onLongClick(ved_list.get(holder.getAdapterPosition()), holder.ved_container);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return ved_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_hved;
        CardView ved_container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_hved=itemView.findViewById(R.id.iv_hved);
            ved_container=itemView.findViewById(R.id.ved_container);

        }
    }


}
