package com.example.backendpharmacie.auth;

public record RegisterRequest(
        String nom,
        String prenom,
        String email,
        String motDePasse,
        String telephone,
        String adresse
) {}