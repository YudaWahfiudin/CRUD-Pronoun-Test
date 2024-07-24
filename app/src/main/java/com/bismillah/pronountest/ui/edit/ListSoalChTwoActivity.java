package com.bismillah.pronountest.ui.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bismillah.pronountest.adapter.SoalChTwoAdapter;
import com.bismillah.pronountest.databinding.ActivityListSoalChTwoBinding;
import com.bismillah.pronountest.model.SoalChTwo;
import com.bismillah.pronountest.ui.create.TambahSoalChTwoActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSoalChTwoActivity  extends AppCompatActivity implements SoalChTwoAdapter.FirebaseDataListener {

    private ActivityListSoalChTwoBinding binding;
    private SoalChTwoAdapter mAdapter;
    List<SoalChTwo> mlist;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSoalChTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> startCreateChapterTwo());
        binding.recyclerViewSoalHouse.setAdapter(mAdapter);
        binding.recyclerViewSoalHouse.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference();
        mDatabaseReference.child("soal").child("vocabulary").child("chapter2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    SoalChTwo soalChTwo = mDataSnapshot.getValue(SoalChTwo.class);
                    if (soalChTwo != null) {
                        soalChTwo.setKey(mDataSnapshot.getKey());
                        mlist.add(soalChTwo);
                    } else {
                        Log.d("ListSoalChTwoActivity", "SoalChTwo is null for some reason.");
                    }
                }
                mAdapter = new SoalChTwoAdapter(mlist, ListSoalChTwoActivity.this);
                binding.recyclerViewSoalHouse.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListSoalChTwoActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startCreateChapterTwo(){
        Intent intent = new Intent(ListSoalChTwoActivity.this, TambahSoalChTwoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataClick(SoalChTwo soalChTwo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", (dialog, id) -> dialogUpdateSoal(soalChTwo));

        builder.setNegativeButton("HAPUS", (dialog, id) -> hapusDataSoal(soalChTwo));

        builder.setNeutralButton("BATAL", (dialog, id) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateSoal(SoalChTwo soalChTwo) {
        Intent intent = new Intent(ListSoalChTwoActivity.this, EditSoalChTwoActivity.class);
        intent.putExtra("SOAL_KEY", soalChTwo.getKey());
        intent.putExtra("SOAL_TEXT", soalChTwo.getSoalText());
        intent.putExtra("IMAGE_URL", soalChTwo.getImageUrl());
        startActivity(intent);
    }

    private void hapusDataSoal(SoalChTwo soalChTwo) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("soal").child("vocabulary").child("chapter2").child(soalChTwo.getKey()).removeValue().addOnSuccessListener(mVoid -> Toast.makeText(ListSoalChTwoActivity.this, "Data berhasil di hapus!", Toast.LENGTH_LONG).show());
        }
    }
}
