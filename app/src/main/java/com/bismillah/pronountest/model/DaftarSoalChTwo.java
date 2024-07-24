package com.bismillah.pronountest.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;

public class DaftarSoalChTwo extends RecyclerView.ViewHolder{
    public View view;
    public TextView soal;
    public ImageView imageView;
    public DaftarSoalChTwo(View view){
        super(view);

        soal = itemView.findViewById(R.id.namaGambarChTwo);
        imageView = itemView.findViewById(R.id.gambarSoalChTwo);
        this.view = view;
    }
}
