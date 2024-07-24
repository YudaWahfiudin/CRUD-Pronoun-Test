package com.bismillah.pronountest.ui.main;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.bismillah.pronountest.databinding.ActivityMainBinding;
import com.bismillah.pronountest.ui.edit.ListSoalChFourActivity;
import com.bismillah.pronountest.ui.edit.ListSoalChOneActivity;
import com.bismillah.pronountest.ui.edit.ListSoalChThreeActivity;
import com.bismillah.pronountest.ui.edit.ListSoalChTwoActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnKelolaCh1.setOnClickListener(v -> startCreateChapterOne());
        binding.btnKelolaCh2.setOnClickListener(v -> startCreateChapterTwo());
        binding.btnKelolaCh3.setOnClickListener(v -> startCreateChapterThree());
        binding.btnKelolaCh4.setOnClickListener(v -> startCreateChapterFour());
    }

    private void startCreateChapterOne() {
        Intent intent = new Intent(MainActivity.this, ListSoalChOneActivity.class);
        startActivity(intent);
    }
    private void startCreateChapterTwo() {
        Intent intent = new Intent(MainActivity.this, ListSoalChTwoActivity.class);
        startActivity(intent);
    }
    private void startCreateChapterThree() {
        Intent intent = new Intent(MainActivity.this, ListSoalChThreeActivity.class);
        startActivity(intent);
    }
    private void startCreateChapterFour() {
        Intent intent = new Intent(MainActivity.this, ListSoalChFourActivity.class);
        startActivity(intent);
    }
}