package com.bismillah.pronountest.ui.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bismillah.pronountest.adapter.SoalChThreeAdapter;
import com.bismillah.pronountest.databinding.ActivityListSoalChThreeBinding;
import com.bismillah.pronountest.model.SoalChThree;
import com.bismillah.pronountest.ui.create.TambahSoalChThreeActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSoalChThreeActivity extends AppCompatActivity implements SoalChThreeAdapter.FirebaseDataListener{

    private ActivityListSoalChThreeBinding binding;
    private SoalChThreeAdapter mAdapter;
    List<SoalChThree> mlist;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSoalChThreeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> startCreateChapterThree());
        binding.recyclerViewSoalCh3.setAdapter(mAdapter);
        binding.recyclerViewSoalCh3.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference();
        mDatabaseReference.child("soal").child("conversation").child("chapter3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    SoalChThree soalChThree = mDataSnapshot.getValue(SoalChThree.class);
                    if (soalChThree != null) {
                        soalChThree.setKey(mDataSnapshot.getKey());
                        mlist.add(soalChThree);
                    } else {
                        Log.d("ListSoalChThreeActivity", "SoalChThree is null for some reason.");
                    }
                }
                mAdapter = new SoalChThreeAdapter(mlist, ListSoalChThreeActivity.this);
                binding.recyclerViewSoalCh3.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListSoalChThreeActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCreateChapterThree() {
        Intent intent = new Intent(ListSoalChThreeActivity.this, TambahSoalChThreeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataClick(SoalChThree soalChThree, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", (dialog, id) -> dialogUpdateSoal(soalChThree));

        builder.setNegativeButton("HAPUS", (dialog, id) -> hapusDataSoal(soalChThree));

        builder.setNeutralButton("BATAL", (dialog, id) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateSoal(SoalChThree soalChThree) {
        Intent intent = new Intent(ListSoalChThreeActivity.this, EditSoalChThreeActivity.class);
        intent.putExtra("SOAL_KEY", soalChThree.getKey());
        intent.putExtra("SOAL_TEXT", soalChThree.getSoalText());
        startActivity(intent);
    }

    private void hapusDataSoal(SoalChThree soalChThree) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("soal").child("conversation").child("chapter3").child(soalChThree.getKey()).removeValue().addOnSuccessListener(mVoid -> Toast.makeText(ListSoalChThreeActivity.this, "Data berhasil di hapus!", Toast.LENGTH_LONG).show());
        }
    }
}