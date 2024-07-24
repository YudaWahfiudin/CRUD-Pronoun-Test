package com.bismillah.pronountest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;
import com.bismillah.pronountest.model.DaftarSoalChFour;
import com.bismillah.pronountest.model.SoalChFour;

import java.util.List;

public class SoalChFourAdapter extends RecyclerView.Adapter<DaftarSoalChFour> {
    
    List<SoalChFour> mlist;
    Context context;
    private SoalChFourAdapter.FirebaseDataListener listener;
    
    public SoalChFourAdapter(List<SoalChFour> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        if (context instanceof SoalChFourAdapter.FirebaseDataListener) {
            this.listener = (SoalChFourAdapter.FirebaseDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FirebaseDataListener");
        }
    }
    
    @NonNull
    @Override
    public DaftarSoalChFour onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_soal_ch_four, parent, false);
        DaftarSoalChFour holder = new DaftarSoalChFour(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarSoalChFour holder, int position) {
        SoalChFour soalChFour = mlist.get(position);
        holder.soal.setText(soalChFour.getSoalText());
        holder.view.setOnClickListener(v -> listener.onDataClick(mlist.get(position), position));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface FirebaseDataListener {
        void onDataClick(SoalChFour soalChFour, int position);
    }
}
