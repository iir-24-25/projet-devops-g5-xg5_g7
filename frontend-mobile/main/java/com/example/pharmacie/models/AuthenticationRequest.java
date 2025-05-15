package com.example.pharmacie.models;

public class AuthenticationRequest {
    private String email;
    private String motDePasse;

    // Constructeur
    public AuthenticationRequest(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters (obligatoires pour Retrofit/Gson)
    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}