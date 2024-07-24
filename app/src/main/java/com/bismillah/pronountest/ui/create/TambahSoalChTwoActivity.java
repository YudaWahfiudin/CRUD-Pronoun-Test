package com.bismillah.pronountest.ui.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bismillah.pronountest.databinding.ActivityTambahSoalChTwoBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TambahSoalChTwoActivity extends AppCompatActivity {

    private ActivityTambahSoalChTwoBinding binding;
    private Uri selectedImageUri;
    private Uri selectedAudioUri;
    private static final int REQUEST_PERMISSION = 1;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahSoalChTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
        
        binding.buttonPilihGambarTwo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        });

        binding.buttonPilihAudioTwo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2000);
        });

        binding.buttonPlayAudio.setOnClickListener(v -> playAudio());
        binding.buttonUpload.setOnClickListener(v -> uploadData());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.imagePreviewTwo.setImageURI(selectedImageUri);
        }else if (requestCode == 2000 && resultCode == Activity.RESULT_OK && data != null) {
            selectedAudioUri = data.getData();
            Toast.makeText(this, "Audio selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData() {
        String soalText = binding.inputSoalTwo.getText().toString().trim();
        if (soalText.isEmpty() || selectedImageUri == null || selectedAudioUri == null) {
            Toast.makeText(this, "Soal dan gambar harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageFileName = "vocabulary/chapter2/" + UUID.randomUUID().toString();
        String audioFileName = "vocabulary/chapter2/audio/" + UUID.randomUUID().toString();
        
        //Upload Image
        Bitmap bitmap = ((BitmapDrawable) binding.imagePreviewTwo.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageData = baos.toByteArray();

        UploadTask uploadImageTask = storageRef.child(imageFileName).putBytes(imageData);
        uploadImageTask.addOnSuccessListener(taskSnapshot -> storageRef.child(imageFileName).getDownloadUrl().addOnSuccessListener(imageUri -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedAudioUri);
                UploadTask uploadAudioTask = storageRef.child(audioFileName).putStream(inputStream);
                uploadAudioTask.addOnSuccessListener(audioSnapshot -> storageRef.child(audioFileName).getDownloadUrl().addOnSuccessListener(audioUri -> {
                    saveDataToDatabase(soalText, imageUri.toString(), audioUri.toString());
                    Toast.makeText(TambahSoalChTwoActivity.this, "Gambar dan audio berhasil diunggah", Toast.LENGTH_SHORT).show();
                })).addOnFailureListener(e -> Toast.makeText(TambahSoalChTwoActivity.this, "Upload audio gagal", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Toast.makeText(TambahSoalChTwoActivity.this, "Error accessing audio file", Toast.LENGTH_SHORT).show();
            }
        })).addOnFailureListener(e -> Toast.makeText(TambahSoalChTwoActivity.this, "Upload gambar gagal", Toast.LENGTH_SHORT).show());
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

    private void saveDataToDatabase(String soalText, String imageUrl, String audioUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("soal").child("vocabulary").child("chapter2").push();
        Map<String, Object> soalData = new HashMap<>();
        soalData.put("soalText", soalText);
        soalData.put("imageUrl", imageUrl);
        soalData.put("audioUrl", audioUrl);

        databaseRef.setValue(soalData).addOnSuccessListener(aVoid -> {
            Toast.makeText(TambahSoalChTwoActivity.this, "Soal berhasil disimpan", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(TambahSoalChTwoActivity.this, "Gagal menyimpan soal", Toast.LENGTH_SHORT).show());
    }
}