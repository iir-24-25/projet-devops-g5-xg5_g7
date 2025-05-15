package com.example.pharmacie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacie.models.RegisterRequest;
import com.example.pharmacie.models.AuthenticationResponse;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etPassword, etTelephone, etAdresse;
    private Button btnSignup, btnGoToLogin;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialisation de Retrofit
        apiService = ApiClient.getClient().create(ApiInterface.class);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etTelephone = findViewById(R.id.etTelephone);
        etAdresse = findViewById(R.id.etAdresse);
        btnSignup = findViewById(R.id.btnSignup);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnSignup.setOnClickListener(v -> {
            try {
                RegisterRequest request = new RegisterRequest(
                        etNom.getText().toString(),
                        etPrenom.getText().toString(),
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        etTelephone.getText().toString(),
                        etAdresse.getText().toString()
                );

                registerUser(request);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToLogin.setOnClickListener(v -> goToLogin());
    }

    private void registerUser(RegisterRequest request) {
        apiService.register(request).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Erreur d'inscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}