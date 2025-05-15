package com.example.pharmacie.models;

import android.util.Patterns;

public class RegisterRequest {
    private final String nom;
    private final String prenom;
    private final String email;
    private final String motDePasse;
    private final String telephone;
    private final String adresse;

    public RegisterRequest(String nom, String prenom, String email,
                           String motDePasse, String telephone, String adresse) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Email invalide");
        }

        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        if (motDePasse.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }

        if (telephone == null || telephone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire");
        }

        if (adresse == null || adresse.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse est obligatoire");
        }


        this.nom = nom.trim();
        this.prenom = prenom.trim();
        this.email = email.trim();
        this.motDePasse = motDePasse;
        this.telephone = telephone.trim();
        this.adresse = adresse.trim();
    }

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
}