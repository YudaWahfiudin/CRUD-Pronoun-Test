package com.bismillah.pronountest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bismillah.pronountest.R;
import com.bismillah.pronountest.model.DaftarSoalChThree;
import com.bismillah.pronountest.model.SoalChThree;

import java.util.List;

public class SoalChThreeAdapter extends RecyclerView.Adapter<DaftarSoalChThree> {
    
    List<SoalChThree> mlist;
    Context context;
    private SoalChThreeAdapter.FirebaseDataListener listener;

    public SoalChThreeAdapter(List<SoalChThree> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        if (context instanceof SoalChThreeAdapter.FirebaseDataListener) {
            this.listener = (SoalChThreeAdapter.FirebaseDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FirebaseDataListener");
        }
    }
    
    @NonNull
    @Override
    public DaftarSoalChThree onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_soal_ch_three, parent, false);
        DaftarSoalChThree holder = new DaftarSoalChThree(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarSoalChThree holder, int position) {
        SoalChThree soalChThree = mlist.get(position);
        holder.soal.setText(soalChThree.getSoalText());
        holder.view.setOnClickListener(v -> listener.onDataClick(mlist.get(position), position));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface FirebaseDataListener {
        void onDataClick(SoalChThree soalChThree, int position);
    }
}
