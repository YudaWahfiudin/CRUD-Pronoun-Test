package com.bismillah.pronountest.model;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;

public class DaftarSoalChThree extends RecyclerView.ViewHolder{
    public View view;
    public TextView soal;
    public DaftarSoalChThree(View view){
        super(view);

        soal = itemView.findViewById(R.id.soalCh3);
        this.view = view;
    }
}
