package com.bismillah.pronountest.ui.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bismillah.pronountest.databinding.ActivityEditSoalChTwoBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditSoalChTwoActivity extends AppCompatActivity {

    private ActivityEditSoalChTwoBinding binding;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_PICK_AUDIO = 1002;
    private String soalKey, currentImageUrl, currentAudioUrl;
    private Uri selectedImageUri, selectedAudioUri;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditSoalChTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Dapatkan data dari Intent
        Intent intent = getIntent();
        soalKey = intent.getStringExtra("SOAL_KEY");
        String soalText = intent.getStringExtra("SOAL_TEXT");
        currentImageUrl = intent.getStringExtra("IMAGE_URL");
        currentAudioUrl = intent.getStringExtra("AUDIO_URL");

        binding.editSoal.setText(soalText);
        Glide.with(this).load(currentImageUrl).into(binding.imagePreview);

        binding.buttonPilihGambar.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, REQUEST_CODE_PICK_IMAGE);
        });

        binding.buttonPilihAudioTwo.setOnClickListener(v -> {
            Intent pickAudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickAudioIntent, REQUEST_CODE_PICK_AUDIO);
        });

        binding.buttonPlayAudio.setOnClickListener(v -> playAudio());

        binding.buttonUbah.setOnClickListener(v -> {
            String updatedSoalText = binding.editSoal.getText().toString().trim();
            if (selectedImageUri != null) {
                uploadImageAndSaveData(updatedSoalText);
            } else if (selectedAudioUri != null) {
                uploadAudioAndSaveData(updatedSoalText, currentImageUrl);
            } else {
                saveData(updatedSoalText, currentImageUrl, currentAudioUrl);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.imagePreview.setImageURI(selectedImageUri);
        }else if (requestCode == REQUEST_CODE_PICK_AUDIO && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedAudioUri = data.getData();
        }
    }

    private void uploadImageAndSaveData(String updatedSoalText) {
        if (selectedImageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newImageUrl = uri.toString();
                        if (selectedAudioUri != null) {
                            uploadAudioAndSaveData(updatedSoalText, newImageUrl);
                        } else {
                            saveData(updatedSoalText, newImageUrl, currentAudioUrl);
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(EditSoalChTwoActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show());
        }
    }

    private void uploadAudioAndSaveData(String updatedSoalText, String imageUrl) {
        if (selectedAudioUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("audios/" + UUID.randomUUID().toString());
            storageRef.putFile(selectedAudioUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newAudioUrl = uri.toString();
                        saveData(updatedSoalText, imageUrl, newAudioUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(EditSoalChTwoActivity.this, "Gagal mengunggah audio", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveData(String updatedSoalText, String imageUrl, String audioUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("soal")
                .child("vocabulary")
                .child("chapter2")
                .child(soalKey);

        Map<String, Object> soalData = new HashMap<>();
        soalData.put("soalText", updatedSoalText);
        soalData.put("imageUrl", imageUrl);
        soalData.put("audioUrl", audioUrl);

        databaseRef.updateChildren(soalData).addOnSuccessListener(aVoid -> {
            Toast.makeText(EditSoalChTwoActivity.this, "Soal berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(EditSoalChTwoActivity.this, "Gagal memperbarui soal", Toast.LENGTH_SHORT).show());
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
            }
        }
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