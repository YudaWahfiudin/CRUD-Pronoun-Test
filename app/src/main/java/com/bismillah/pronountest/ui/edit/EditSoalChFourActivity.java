package com.bismillah.pronountest.ui.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bismillah.pronountest.databinding.ActivityEditSoalChFourBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditSoalChFourActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_AUDIO = 1001;

    private ActivityEditSoalChFourBinding binding;
    private String soalKey, currentAudioUrl;
    private Uri selectedAudioUri;
    private MediaPlayer mediaPlayer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditSoalChFourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Dapatkan data dari Intent
        Intent intent = getIntent();
        soalKey = intent.getStringExtra("SOAL_KEY");
        String soalText = intent.getStringExtra("SOAL_TEXT");
        currentAudioUrl = intent.getStringExtra("AUDIO_URL");
        
        binding.editSoal.setText(soalText);

        binding.buttonPilihAudioFour.setOnClickListener(v -> {
            Intent pickAudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickAudioIntent, REQUEST_CODE_PICK_AUDIO);
        });

        binding.buttonPlayAudio.setOnClickListener(v -> playAudio());

        binding.buttonUbah.setOnClickListener(v -> {
            String updatedSoalText = binding.editSoal.getText().toString().trim();
            if (!updatedSoalText.isEmpty()) {
                if (selectedAudioUri != null) {
                    uploadAudioAndSaveData(updatedSoalText);
                } else {
                    saveData(updatedSoalText, currentAudioUrl);
                }
            } else {
                Toast.makeText(EditSoalChFourActivity.this, "Soal tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
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
        } else if (currentAudioUrl != null) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(currentAudioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memutar audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadAudioAndSaveData(String updatedSoalText) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("audios/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedAudioUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String newAudioUrl = uri.toString();
                    saveData(updatedSoalText, newAudioUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(EditSoalChFourActivity.this, "Gagal mengunggah audio", Toast.LENGTH_SHORT).show());
    }
    
    private void saveData(String updatedSoalText, String audioUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("soal")
                .child("conversation")
                .child("chapter4")
                .child(soalKey);

        Map<String, Object> soalData = new HashMap<>();
        soalData.put("soalText", updatedSoalText);
        soalData.put("audioUrl", audioUrl);

        databaseRef.updateChildren(soalData).addOnSuccessListener(aVoid -> {
            Toast.makeText(EditSoalChFourActivity.this, "Soal berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(EditSoalChFourActivity.this, "Gagal memperbarui soal", Toast.LENGTH_SHORT).show());
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