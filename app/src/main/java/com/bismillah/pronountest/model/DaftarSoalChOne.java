package com.bismillah.pronountest.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;

public class DaftarSoalChOne extends RecyclerView.ViewHolder {
    public View view;
    public TextView soal;
    public ImageView imageView;
    public DaftarSoalChOne(View view){
        super(view);

        soal = itemView.findViewById(R.id.namaGambarChOne);
        imageView = itemView.findViewById(R.id.gambarSoalChOne);
        this.view = view;
    }
}
