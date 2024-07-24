package com.bismillah.pronountest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import com.bismillah.pronountest.R;
import com.bismillah.pronountest.model.DaftarSoalChOne;
import com.bismillah.pronountest.model.SoalChOne;
import com.bumptech.glide.Glide;

import java.util.List;

public class SoalChOneAdapter extends RecyclerView.Adapter<DaftarSoalChOne> {

    List<SoalChOne> mlist;
    Context context;
    private FirebaseDataListener listener;

    public SoalChOneAdapter(List<SoalChOne> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        if (context instanceof FirebaseDataListener) {
            this.listener = (FirebaseDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FirebaseDataListener");
        }
    }

    @NonNull
    @Override
    public DaftarSoalChOne onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_soal_ch_one, parent, false);
        DaftarSoalChOne holder = new DaftarSoalChOne(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarSoalChOne holder, @SuppressLint("RecyclerView") int position) {
        SoalChOne soalChOne = mlist.get(position);
        holder.soal.setText(soalChOne.getSoalText());
        if (soalChOne.getImageUrl() != null && !soalChOne.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(soalChOne.getImageUrl())
                    .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                    .into(holder.imageView);
        }
        holder.view.setOnClickListener(v -> listener.onDataClick(mlist.get(position), position));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface FirebaseDataListener {
        void onDataClick(SoalChOne soalChOne, int position);
    }
}

