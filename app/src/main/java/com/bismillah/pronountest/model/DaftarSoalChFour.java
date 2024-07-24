package com.bismillah.pronountest.model;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;

public class DaftarSoalChFour extends RecyclerView.ViewHolder{
    public View view;
    public TextView soal;
    public DaftarSoalChFour(View view){
        super(view);

        soal = itemView.findViewById(R.id.soalCh4);
        this.view = view;
    }
}
