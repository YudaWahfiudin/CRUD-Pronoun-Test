package com.bismillah.pronountest.ui.create;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.bismillah.pronountest.databinding.ActivityTambahSoalChThreeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TambahSoalChThreeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_AUDIO = 1001;

    private ActivityTambahSoalChThreeBinding binding;
    private Uri selectedAudioUri;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahSoalChThreeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonPilihAudioThree.setOnClickListener(v -> {
            Intent pickAudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickAudioIntent, REQUEST_CODE_PICK_AUDIO);
        });

        binding.buttonPlayAudio.setOnClickListener(v -> playAudio());
        binding.buttonUpload.setOnClickListener(v -> uploadData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_AUDIO && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedAudioUri = data.getData();
        }
    }

    private void playAudio() {
        if (selectedAudioUri != null) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, selectedAudioUri);
            mediaPlayer.start();
        }
    }

    private void uploadData() {
        String soalText = binding.inputSoalThree.getText().toString().trim();
        if (soalText.isEmpty()) {
            Toast.makeText(this, "Soal harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAudioUri == null) {
            Toast.makeText(this, "Audio harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadAudioAndSaveData(soalText);
    }

    private void uploadAudioAndSaveData(String soalText) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("audios/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedAudioUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String audioUrl = uri.toString();
                    saveDataToDatabase(soalText, audioUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(TambahSoalChThreeActivity.this, "Gagal mengunggah audio", Toast.LENGTH_SHORT).show());
    }

    private void saveDataToDatabase(String soalText, String audioUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("soal").child("conversation").child("chapter3").push();
        Map<String, Object> soalData = new HashMap<>();
        soalData.put("soalText", soalText);
        soalData.put("audioUrl", audioUrl);

        databaseRef.setValue(soalData).addOnSuccessListener(aVoid -> {
            Toast.makeText(TambahSoalChThreeActivity.this, "Soal berhasil disimpan", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(TambahSoalChThreeActivity.this, "Gagal menyimpan soal", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
