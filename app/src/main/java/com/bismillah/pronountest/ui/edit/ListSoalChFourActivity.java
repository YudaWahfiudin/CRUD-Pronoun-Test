package com.bismillah.pronountest.ui.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bismillah.pronountest.adapter.SoalChFourAdapter;
import com.bismillah.pronountest.databinding.ActivityListSoalChFourBinding;
import com.bismillah.pronountest.model.SoalChFour;
import com.bismillah.pronountest.ui.create.TambahSoalChFourActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSoalChFourActivity extends AppCompatActivity implements SoalChFourAdapter.FirebaseDataListener {

    private ActivityListSoalChFourBinding binding;
    private SoalChFourAdapter mAdapter;
    List<SoalChFour> mlist;
    private DatabaseReference mDatabaseReference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSoalChFourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> startCreateChapterFour());
        binding.recyclerViewSoalCh4.setAdapter(mAdapter);
        binding.recyclerViewSoalCh4.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference();
        mDatabaseReference.child("soal").child("conversation").child("chapter4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    SoalChFour soalChFour = mDataSnapshot.getValue(SoalChFour.class);
                    if (soalChFour != null) {
                        soalChFour.setKey(mDataSnapshot.getKey());
                        mlist.add(soalChFour);
                    } else {
                        Log.d("ListSoalChFourActivity", "SoalChFour is null for some reason.");
                    }
                }
                mAdapter = new SoalChFourAdapter(mlist, ListSoalChFourActivity.this);
                binding.recyclerViewSoalCh4.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListSoalChFourActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCreateChapterFour() {
        Intent intent = new Intent(ListSoalChFourActivity.this, TambahSoalChFourActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataClick(SoalChFour soalChFour, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", (dialog, id) -> dialogUpdateSoal(soalChFour));

        builder.setNegativeButton("HAPUS", (dialog, id) -> hapusDataSoal(soalChFour));

        builder.setNeutralButton("BATAL", (dialog, id) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateSoal(SoalChFour soalChFour) {
        Intent intent = new Intent(ListSoalChFourActivity.this, EditSoalChFourActivity.class);
        intent.putExtra("SOAL_KEY", soalChFour.getKey());
        intent.putExtra("SOAL_TEXT", soalChFour.getSoalText());
        startActivity(intent);
    }

    private void hapusDataSoal(SoalChFour soalChFour) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("soal").child("conversation").child("chapter4").child(soalChFour.getKey()).removeValue().addOnSuccessListener(mVoid -> Toast.makeText(ListSoalChFourActivity.this, "Data berhasil di hapus!", Toast.LENGTH_LONG).show());
        }
    }
}