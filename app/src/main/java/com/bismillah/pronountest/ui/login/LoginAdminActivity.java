package com.bismillah.pronountest.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bismillah.pronountest.databinding.ActivityLoginAdminBinding;
import com.bismillah.pronountest.ui.create.TambahSoalChOneActivity;
import com.bismillah.pronountest.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginAdminActivity extends AppCompatActivity {

    private ActivityLoginAdminBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        binding.etEmail.addTextChangedListener(loginTextWatcher);
        binding.etPassword.addTextChangedListener(loginTextWatcher);
        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                signIn(email, password);
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateForm();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void validateForm() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        binding.loginbtn.setEnabled(!email.isEmpty() && !password.isEmpty());
    }

    private void signIn(String email, String password) {
        String textEmail = binding.etEmail.getText().toString();
        String textPwd = binding.etPassword.getText().toString();
        if (email.equals("admin@gmail.com") && password.equals("admin123")) {
            Toast.makeText(LoginAdminActivity.this, "Anda berhasil login", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginAdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(TextUtils.isEmpty(textEmail)){
                Toast.makeText(LoginAdminActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                binding.etEmail.setError("Email is required");
                binding.etEmail.requestFocus();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                Toast.makeText(LoginAdminActivity.this,"Please re-enter email",Toast.LENGTH_LONG).show();
                binding.etEmail.setError("Valid Email is required");
                binding.etEmail.requestFocus();
            } else if(TextUtils.isEmpty(textPwd)){
                Toast.makeText(LoginAdminActivity.this,"Please enter password", Toast.LENGTH_LONG).show();
                binding.etPassword.setError("Password is required");
                binding.etPassword.requestFocus();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginAdminActivity.this, "Anda berhasil login", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginAdminActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginAdminActivity.this, "Terjadi kesalahan!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}