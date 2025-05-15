package com.example.pharmacie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;

import com.example.pharmacie.models.AuthUtils;
import com.example.pharmacie.models.AuthenticationRequest;
import com.example.pharmacie.models.AuthenticationResponse;
import com.example.pharmacie.retrofit.ApiClient;
import com.example.pharmacie.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToSignup;
    private ProgressBar progressBar;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignup = findViewById(R.id.btnGoToSignup);
        progressBar = findViewById(R.id.progressBar);

        // Initialisation Retrofit
        apiService = ApiClient.getClient().create(ApiInterface.class);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInputs(email, password)) {
                authenticateUser(email, password);
            }
        });

        btnGoToSignup.setOnClickListener(v -> goToSignup());
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            etEmail.setError("Email requis");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Mot de passe requis");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email invalide");
            return false;
        }

        return true;
    }

    private void authenticateUser(String email, String password) {
        // Afficher le loading
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        apiService.login(request).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body().getToken());
                } else {
                    handleLoginError("Email ou mot de passe incorrect");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                handleLoginError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(String token) {

            if (token == null || token.isEmpty()) {
                Toast.makeText(this, "Erreur: token vide", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Sauvegarde du token
            AuthUtils.saveToken(this, token);

            // 2. Redirection vers MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // 3. Fermeture propre
            finishAffinity();
    }


    private void handleLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void goToSignup() {
        startActivity(new Intent(this, SignupActivity.class));
    }
}
