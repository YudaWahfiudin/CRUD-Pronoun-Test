package com.bismillah.pronountest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;
import com.bismillah.pronountest.model.DaftarSoalChTwo;
import com.bismillah.pronountest.model.SoalChTwo;
import com.bumptech.glide.Glide;

import java.util.List;

public class SoalChTwoAdapter extends RecyclerView.Adapter<DaftarSoalChTwo> {

    List<SoalChTwo> mlist;
    Context context;
    private SoalChTwoAdapter.FirebaseDataListener listener;

    public SoalChTwoAdapter(List<SoalChTwo> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        if (context instanceof SoalChTwoAdapter.FirebaseDataListener) {
            this.listener = (SoalChTwoAdapter.FirebaseDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FirebaseDataListener");
        }
    }

    @NonNull
    @Override
    public DaftarSoalChTwo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_soal_ch_two, parent, false);
        DaftarSoalChTwo holder = new DaftarSoalChTwo(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarSoalChTwo holder, int position) {
        SoalChTwo soalChTwo = mlist.get(position);
        holder.soal.setText(soalChTwo.getSoalText());
        if (soalChTwo.getImageUrl() != null && !soalChTwo.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(soalChTwo.getImageUrl())
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
        void onDataClick(SoalChTwo soalChTwo, int position);
    }
}
