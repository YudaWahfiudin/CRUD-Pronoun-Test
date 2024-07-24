package com.bismillah.pronountest.ui.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bismillah.pronountest.adapter.SoalChOneAdapter;
import com.bismillah.pronountest.databinding.ActivityListSoalChOneBinding;
import com.bismillah.pronountest.model.SoalChOne;
import com.bismillah.pronountest.ui.create.TambahSoalChOneActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSoalChOneActivity extends AppCompatActivity implements SoalChOneAdapter.FirebaseDataListener{

    private ActivityListSoalChOneBinding binding;
    private SoalChOneAdapter mAdapter;
    List<SoalChOne> mlist;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSoalChOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> startCreateChapterOne());
        binding.recyclerViewSoalNoun.setAdapter(mAdapter);
        binding.recyclerViewSoalNoun.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference();
        mDatabaseReference.child("soal").child("vocabulary").child("chapter1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    SoalChOne soalChOne = mDataSnapshot.getValue(SoalChOne.class);
                    if (soalChOne != null) {
                        soalChOne.setKey(mDataSnapshot.getKey());
                        mlist.add(soalChOne);
                    } else {
                        Log.d("ListSoalChOneActivity", "SoalChOne is null for some reason.");
                    }
                }
                mAdapter = new SoalChOneAdapter(mlist, ListSoalChOneActivity.this);
                binding.recyclerViewSoalNoun.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListSoalChOneActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCreateChapterOne() {
        Intent intent = new Intent(ListSoalChOneActivity.this, TambahSoalChOneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataClick(final SoalChOne soalChOne, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", (dialog, id) -> dialogUpdateSoal(soalChOne));

        builder.setNegativeButton("HAPUS", (dialog, id) -> hapusDataSoal(soalChOne));

        builder.setNeutralButton("BATAL", (dialog, id) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateSoal(SoalChOne soalChOne) {
        Intent intent = new Intent(ListSoalChOneActivity.this, EditSoalChOneActivity.class);
        intent.putExtra("SOAL_KEY", soalChOne.getKey());
        intent.putExtra("SOAL_TEXT", soalChOne.getSoalText());
        intent.putExtra("IMAGE_URL", soalChOne.getImageUrl());
        startActivity(intent);
    }
    private void hapusDataSoal(SoalChOne soalChOne) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("soal").child("vocabulary").child("chapter1").child(soalChOne.getKey()).removeValue().addOnSuccessListener(mVoid -> Toast.makeText(ListSoalChOneActivity.this, "Data berhasil di hapus!", Toast.LENGTH_LONG).show());
        }
    }
}